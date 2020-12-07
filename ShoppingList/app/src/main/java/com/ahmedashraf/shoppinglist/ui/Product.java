package com.ahmedashraf.shoppinglist.ui;

import java.util.Objects;

public class Product {
    private String imgProduct, Title, Description;
    private int APrice, BPrice, Stock, Amount;

    public Product(String imgProduct, String title, String description, int aPrice, int bPrice, int stock) {
        this.imgProduct = imgProduct;
        Title = title;
        Description = description;
        APrice = aPrice;
        BPrice = bPrice;
        Stock = stock;
    }

    public String getImgProduct() {
        return imgProduct;
    }

    public String getTitle() {
        return Title;
    }

    public String getDescription() {
        return Description;
    }

    public int getAPrice() {
        return APrice;
    }

    public int getBPrice() {
        return BPrice;
    }

    public int getStock() {
        return Stock;
    }

    public int getAmount() {
        return Amount;
    }

    public void setAmount(int amount) {
        Amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(Title, product.Title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Title);
    }
}
