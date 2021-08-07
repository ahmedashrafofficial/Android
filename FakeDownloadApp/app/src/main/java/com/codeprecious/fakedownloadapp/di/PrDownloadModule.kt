package com.codeprecious.fakedownloadapp.di

import android.content.Context
import com.downloader.PRDownloader
import com.downloader.PRDownloaderConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PrDownloadModule {

    @Provides
    @Singleton
    fun providePrDownloadConfig(): PRDownloaderConfig {
        return PRDownloaderConfig.newBuilder()
            .setConnectTimeout(30000)
            .setReadTimeout(30000)
            .build()
    }
}