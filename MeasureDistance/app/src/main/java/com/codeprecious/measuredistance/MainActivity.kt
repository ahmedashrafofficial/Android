package com.codeprecious.measuredistance

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.SurfaceTexture
import android.hardware.*
import android.hardware.camera2.*
import android.hardware.camera2.CameraCaptureSession.StateCallback
import android.hardware.camera2.params.StreamConfigurationMap
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.provider.MediaStore
import android.util.Size
import android.view.Surface
import android.view.TextureView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.codeprecious.measuredistance.databinding.ActivityMainBinding
import kotlin.math.*


class MainActivity : AppCompatActivity() {

    private lateinit var sensorManager: SensorManager
    private lateinit var gyroScopeSensorListener: SensorEventListener
    private lateinit var binding: ActivityMainBinding
    private lateinit var event: SensorEvent
    private val CAMERA_REQUEST = 1888
    private val MY_CAMERA_PERMISSION_CODE = 100

    private var a: Float = 0.0f
    private var b: Float = 0.0f
    private var distance: Double = 0.0
    private var height: Double = 0.0
    private var check: Byte = 1


    private var cameraID = ""
    private lateinit var imageDimensions: Size
    private lateinit var mBackgroundHandler: Handler
    private lateinit var mBackgroundThread: HandlerThread

    private lateinit var cameraDevice: CameraDevice
    private lateinit var cameraCaptureSession: CameraCaptureSession
    private lateinit var captureRequestBuilder: CaptureRequest.Builder


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        val gyroScopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)

        gyroScopeSensorListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event != null) {
                    this@MainActivity.event = event
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            }
        }

        sensorManager.registerListener(
            gyroScopeSensorListener,
            gyroScopeSensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )

        binding.apply {
            radioGroup.getChildAt(0).setOnClickListener {
                resetNormal()
                check = 1
                makeToast()
            }
            radioGroup.getChildAt(1).setOnClickListener {
                resetAll()
                check = 2
                makeToast()
            }
            radioGroup.getChildAt(2).setOnClickListener {
                resetNormal()
                check = 3
                makeToast()
            }
        }

        binding.apply {
            btnShot.setOnClickListener {
//            for (i in 0..2) {
//                orientations[i] = Math.toDegrees(orientations[i].toDouble()).toFloat()
//            }

//            binding.tv.text =
//                "X:${orientations[0].toInt()} - Y:${orientations[1].toInt()} - Z:${orientations[2].toInt()}"

                var personHeight = 1.75
                if (binding.etHeight.text.isNotEmpty()) {
                    personHeight = binding.etHeight.text.toString().toDouble() - 0.25
                }

                when (check) {
                    1.toByte() -> {
                        a = measureDistanceOfObject()[1]
                        distance = personHeight / abs(tan(a)) // distance
                        binding.tvTotalDistance.text =
                            String.format("%.2f", distance).toDouble().toString()
                    }
                    2.toByte() -> {
                        if (distance == 0.00) {
                            val axis = measureDistanceOfObject()
                            a = axis[0]
                            distance = personHeight / abs(tan(axis[1])) // distance
                            binding.apply {
                                tvDistance11.text =
                                    String.format("%.2f", distance).toDouble().toString()
                                tvDistance11.isVisible = true
                                tvDistance1.isVisible = true
                            }

                            Toast.makeText(
                                this@MainActivity,
                                "Now aim to the bottom of the other object also from the ground ",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            val axis = measureDistanceOfObject()
                            b = axis[0]
                            distance = personHeight / abs(tan(axis[1])) // distance
                            binding.tvDistance22.text =
                                String.format("%.2f", distance).toDouble().toString()
                            tvDistance22.isVisible = true
                            tvDistance2.isVisible = true

                            measureDistanceOfTwoObjects()
                        }

                    }
                    3.toByte() -> {
                        if (a == 0.0f) {
                            a = measureDistanceOfObject()[1]
                            distance = personHeight / abs(tan(a)) // distance
                            binding.tvTotalDistance.text =
                                String.format("%.2f", distance).toDouble().toString()

                            Toast.makeText(
                                this@MainActivity,
                                "Now aim to the highest point of the object to measure height",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            b = measureDistanceOfObject()[1]
                            height = if (b < 0) {
                                (distance * abs(tan(b))) + personHeight // height
                            } else {
                                personHeight - (distance * abs(tan(b)))// height
                            }
                            binding.apply {
                                tvHeight.text = String.format("%.2f", height).toDouble().toString()
                            }
                        }
                    }
                }
            }

            btnReset.setOnClickListener {
                resetAll()
            }
        }
    }

    private fun measureDistanceOfTwoObjects() {
        a = Math.toDegrees(a.toDouble()).toFloat()
        b = Math.toDegrees(b.toDouble()).toFloat()
        if (a < 0) {
            a += 360
        }
        if (b < 0) {
            b += 360
        }

        var angle = abs(b - a)

        if (angle > 180) {
            angle = 360 - angle
        }

        when {
            angle == 180f -> {

                binding.tvTotalDistance.text = (binding.tvDistance11.text.toString()
                    .toFloat() + binding.tvDistance22.text.toString()
                    .toFloat()).toString()
                binding.tvTotalDistance.text =
                    String.format("%.2f", binding.tvTotalDistance.text.toString().toDouble())
                        .toDouble()
                        .toString()
            }
            angle == 90f -> {
                binding.tvTotalDistance.text = sqrt(
                    (binding.tvDistance11.text.toString().toFloat()
                        .pow(2) + binding.tvDistance22.text.toString()
                        .toFloat().pow(2))
                ).toString()
                binding.tvTotalDistance.text =
                    String.format("%.2f", binding.tvTotalDistance.text.toString().toDouble())
                        .toDouble()
                        .toString()
            }
            angle < 90 -> {
                val c = binding.tvDistance22.text.toString()
                    .toFloat() * sin(Math.toRadians(angle.toDouble()))

                val d = binding.tvDistance22.text.toString()
                    .toFloat() * cos(Math.toRadians(angle.toDouble()))

                val e = binding.tvDistance11.text.toString().toFloat() - d

                binding.tvTotalDistance.text = sqrt(c.pow(2) + e.pow(2)).toString()
                binding.tvTotalDistance.text =
                    String.format("%.2f", binding.tvTotalDistance.text.toString().toDouble())
                        .toDouble()
                        .toString()
            }
            angle > 90 -> {
                angle = 180 - angle
                val c = binding.tvDistance22.text.toString()
                    .toFloat() * sin(Math.toRadians(angle.toDouble()))
                val d = binding.tvDistance22.text.toString()
                    .toFloat() * cos(Math.toRadians(angle.toDouble()))
                val e = binding.tvDistance11.text.toString().toFloat() + d
                binding.tvTotalDistance.text = sqrt(c.pow(2) + e.pow(2)).toString()
                binding.tvTotalDistance.text =
                    String.format("%.2f", binding.tvTotalDistance.text.toString().toDouble())
                        .toDouble()
                        .toString()
            }
        }
    }

    private fun resetNormal() {
        binding.apply {
            tvTotalDistance.text = "0"
            tvHeight.text = "0"
            b = 0f
            a = 0f
            distance = 0.0
            height = 0.0
        }
    }

    private fun resetAll() {
        resetNormal()
        binding.apply {
            tvDistance11.text = "0"
            tvDistance22.text = "0"
        }
    }

    private fun makeToast() {
        Toast.makeText(
            this@MainActivity,
            "Aim to the bottom of the object from the ground",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun measureDistanceOfObject(): FloatArray {
        val rotationMatrix = FloatArray(16)
        SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)

        // Remap coordinate system
        val remappedRotationMatrix = FloatArray(16)
        SensorManager.remapCoordinateSystem(
            rotationMatrix,
            SensorManager.AXIS_X,
            SensorManager.AXIS_Z,
            remappedRotationMatrix
        )

        // Convert to orientations
        val orientations = FloatArray(3)
        SensorManager.getOrientation(remappedRotationMatrix, orientations)

        return orientations
    }

    private val textListener: TextureView.SurfaceTextureListener =
        object : TextureView.SurfaceTextureListener {
            @RequiresApi(Build.VERSION_CODES.M)
            override fun onSurfaceTextureAvailable(
                surface: SurfaceTexture,
                width: Int,
                height: Int
            ) {
                openCamera()
            }

            override fun onSurfaceTextureSizeChanged(
                surface: SurfaceTexture,
                width: Int,
                height: Int
            ) {
            }

            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
                return true
            }

            override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
            }
        }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun openCamera() {
        val manager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        cameraID = manager.cameraIdList[0]
        val char: CameraCharacteristics = manager.getCameraCharacteristics(cameraID)
        val map: StreamConfigurationMap? =
            char.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
        imageDimensions = map!!.getOutputSizes(SurfaceTexture::class.java)[0]

        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), MY_CAMERA_PERMISSION_CODE)
        } else {
            manager.openCamera(cameraID, stateCallback, null)
        }
    }

    private val stateCallback: CameraDevice.StateCallback = object : CameraDevice.StateCallback() {
        override fun onOpened(camera: CameraDevice) {
            cameraDevice = camera
            createCameraPreview()
        }

        override fun onDisconnected(camera: CameraDevice) {
        }

        override fun onError(camera: CameraDevice, error: Int) {
        }
    }

    private fun createCameraPreview() {
        val texture: SurfaceTexture? = binding.textureView.surfaceTexture
        texture!!.setDefaultBufferSize(imageDimensions.width, imageDimensions.height)
        val surface = Surface(texture)
        captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
        captureRequestBuilder.addTarget(surface)
        cameraDevice.createCaptureSession(mutableListOf(surface), object : StateCallback() {
            override fun onConfigured(session: CameraCaptureSession) {
                cameraCaptureSession = session
                updatePreview()
            }

            override fun onConfigureFailed(session: CameraCaptureSession) {

            }
        }, null)
    }

    private fun updatePreview() {
        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO)
        cameraCaptureSession.setRepeatingRequest(
            captureRequestBuilder.build(),
            null,
            mBackgroundHandler
        )
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onResume() {
        super.onResume()

        startBackGroundThread()
        if (binding.textureView.isAvailable) {
            openCamera()
        } else {
            binding.textureView.surfaceTextureListener = textListener
        }
    }

    override fun onPause() {
        super.onPause()
        stopBackGroundThread()
    }

    private fun stopBackGroundThread() {
        mBackgroundThread.quitSafely()
        mBackgroundThread.join()
    }

    private fun startBackGroundThread() {
        mBackgroundThread = HandlerThread("Camera Background")
        mBackgroundThread.start()
        mBackgroundHandler = Handler(mBackgroundThread.looper)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show()
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, CAMERA_REQUEST)
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show()
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(gyroScopeSensorListener)
    }
}