package com.example.myapplication;

import java.io.Serializable;

// Класс с информацией о каждом продукте
public class Product implements Serializable {

    public String name_of_product;
    public int price_of_product;
    public String super_market_of_product;

    public String getSuper_market_of_product() {
        return super_market_of_product;
    }

    public void setSuper_market_of_product(String super_market_of_product) {
        this.super_market_of_product = super_market_of_product;
    }

    public String getName_of_product() {
        return name_of_product;
    }

    public void setName_of_product(String name_of_product) {
        this.name_of_product = name_of_product;
    }

    public int getPrice_of_product() {
        return price_of_product;
    }

    public void setPrice_of_product(int price_of_product) {
        this.price_of_product = price_of_product;
    }

    // Метод для формулировки цены продукта по имени продукта
    public void formPrice_of_product(String s) {
        String s1 = s.substring(s.lastIndexOf(" - ") - 1 + 4, s.lastIndexOf(" грн"));
        String s2 = s1.substring(0, s1.lastIndexOf(",")) +  s1.substring(s1.lastIndexOf(",") + 1, s1.length());
        this.price_of_product = Integer.parseInt(s2);
    }

}
