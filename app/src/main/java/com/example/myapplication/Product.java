package com.example.myapplication;

import java.io.Serializable;

// Класс с информацией о продукте
public class Product implements Serializable {

    // Название продукта
    public String name_of_product;

    // Цена продукта
    public int price_of_product;

    // Количество повторений продукта (Только для списка)
    public int count_of_product;


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

    public int getCount_of_product() {
        return count_of_product;
    }

    public void setCount_of_product(int count_of_product) {
        this.count_of_product = count_of_product;
    }

    // Метод для формулировки количества повторений продукта по имени продукта
    public void formCount_of_product(String s) {
        if (s.lastIndexOf("X") > s.lastIndexOf(")"))
        {
            String stroka = s.substring(s.lastIndexOf("X") + 1, s.length());

            this.count_of_product = Integer.parseInt(stroka);
        }
        else
            this.count_of_product = 1;
    }

}
