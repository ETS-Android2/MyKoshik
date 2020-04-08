package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import android.os.StrictMode;
import android.widget.ImageView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;

// Activity для показа SplashScreen (Экран загрузки)
public class SplashScreenActivity extends Activity {
    // Картинка с продуктом
    private ImageView picture_image_of_product;
    // Надпись с типом продукта
    private TextView textview_name_of_product;

    // Количество продуктов разных супермаркетов
    private int count_novus, count_megamarket, count_fozzy;

    // Тип продукта
    String typeOfProduct;

    // ArrayList продуктов из разных супермаркетов
    public ArrayList<Product> products_novus = new ArrayList<Product>();
    public ArrayList<Product> products_megamarket = new ArrayList<Product>();
    public ArrayList<Product> products_fozzy = new ArrayList<Product>();

    // Время в милесекундах, в течение которого будет отображаться SplashScreen
    private final int SPLASH_DISPLAY_LENGTH = 500;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        typeOfProduct = getTypeOfProduct();

        formSplashScreen();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        count_novus = 0;
        count_megamarket = 0;
        count_fozzy = 0;

        formProducts();
    }

    // Метод, котрый формирует SplashScreen в зависимости от типа продукта
    public void formSplashScreen() {
        setContentView(R.layout.activity_splash_screen);

        textview_name_of_product = findViewById(R.id.texview_typeOfProduct);
        picture_image_of_product = findViewById(R.id.picture_splash_screen);

        // Меняються картинки и текста в зависимости от типа продукта
        switch (typeOfProduct) {
            case "Milk" :
                textview_name_of_product.setText(getString(R.string.text_milk));
                picture_image_of_product.setImageResource(R.drawable.milk);
                break;
            case "Bread" :
                textview_name_of_product.setText(getString(R.string.text_bread));
                picture_image_of_product.setImageResource(R.drawable.bread);
                break;
            case "Eggs" :
                textview_name_of_product.setText(getString(R.string.text_eggs));
                picture_image_of_product.setImageResource(R.drawable.eggs);
                break;
        }

        // Размеры картинки с продуктом
        picture_image_of_product.getLayoutParams().height = 760;
        picture_image_of_product.getLayoutParams().width = 800;
        picture_image_of_product.setScaleType(ImageView.ScaleType.FIT_XY);
    }

    // Метод получения типа продукта
    public String getTypeOfProduct() {
        Bundle arguments1 = getIntent().getExtras();
        return arguments1.getString("TypeOfProduct");

    }

    // Метод формулирования продуктов и отправки их в AboutProductsActivity
    public void formProducts() {
        // По истечении времени, запускаем AboutProductsActivity, а SplashScreenActivity закрываем
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (tryInternetConnection() == true)
                {
                    // + Интернет
                    getInfoAboutNovus();
                    if (typeOfProduct.equals("Bread") == false)
                        getInfoAboutMegaMarket();
                    getInfoAboutFozzy();
                }
                else
                {
                    // - Интернет
                   readFromFile("filenovus" + typeOfProduct.toLowerCase() + ".txt");
                   readFromFile("filemegamarket" + typeOfProduct.toLowerCase() + ".txt");
                   readFromFile("filefozzy" + typeOfProduct.toLowerCase() + ".txt");
                }

                // Передача данных о продуктах в AboutProductsActivity
                Intent intent = new Intent(SplashScreenActivity.this, AboutProductsActivity.class);
                Bundle args = new Bundle();

                args.putSerializable("Name1", (Serializable) products_novus);
                if (typeOfProduct.equals("Bread") == false)
                    args.putSerializable("Name2", (Serializable) products_megamarket);
                args.putSerializable("Name3", (Serializable) products_fozzy);

                args.putString("TypeOfProduct", typeOfProduct);

                intent.putExtra("BUNDLE",args);

                SplashScreenActivity.this.startActivity(intent);
                SplashScreenActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    // Метод для парсинга супермаркета Novus
    public void getInfoAboutNovus () {
        String html_sign;

        switch (typeOfProduct) {
            case "Milk" :
                html_sign = "milk";
                break;
            case "Bread" :
                html_sign = "bread";
                break;
            case "Eggs" :
                html_sign = "eggs";
                break;
            default:
                html_sign = "";
                break;
        }

        String html1, html2;
        // html для названия продукта
        html1 = "span.jsx-3273641156.product-tile__title";
        // html для цены продукта
        html2 = "span.jsx-3273641156.product-tile__active-price-value";

        try {
            // Количество продуктов
            int n = 2;

            // Формулирование url с продуктами
            String url[] = new String [n];

            url[0] = "https://novus.zakaz.ua/uk/categories/" + html_sign + "/?sort=price_asc";
            for (int i = 1; i < n; i++)
            {
                url[i] = url[0] + "&page=" + (i+1);
            }

            Document doc[] = new Document[1000];
            Elements formElements1[] = new Elements[1000];
            Elements formElements2[] = new Elements[1000];

            // Получение информации о продуктах
            for (int i = 0; i < n; i++)
            {
                doc[i] = Jsoup.connect(url[i]).get();
                formElements1[i] = doc[i].select(html1);
                formElements2[i] = doc[i].select(html2);

                for (int j = 0; j < formElements1[i].size(); j++) {
                    // Формулировка цены продукта
                    String s = formElements2[i].get(j).text();
                    String s1 = s.substring(0, s.lastIndexOf('.'));
                    String s2 = s.substring(s.lastIndexOf('.') + 1, s.length());

                    products_novus.add(new Product());
                    products_novus.get(count_novus).setName_of_product(formElements1[i].get(j).text() + " - " + s1 + "," + s2 + " грн " + "(Novus)");
                    products_novus.get(count_novus).setPrice_of_product(Integer.parseInt(s1 + s2));

                    count_novus++;
                }
            }
        }
        catch (IOException e) {
        }
    }

    // Метод для парсинга супермаркета MegaMarket
    public void getInfoAboutMegaMarket () {
        String html_sign;

        switch (typeOfProduct) {
            case "Milk" :
                html_sign = "moloko";
                break;
            case "Eggs" :
                html_sign = "yajtsya";
                break;
            default:
                html_sign = "";
                break;
        }

        String html1, html2;

        html1 = "li.product > div.product_info > h3";
        html2 = "div.price";

        try {
            int n= 2;

            if (typeOfProduct.equals("Eggs"))
                n = 1;

            String url[] = new String[n];
            if (html_sign != "") {
                url[0] = "https://megamarket.ua/catalog/" + html_sign + "?sort=price";
                for (int i = 1; i < n; i++) {
                    url[i] = url[0] + "&page=" + (i + 1);
                }

                Document doc[] = new Document[n];
                Elements formElements1[] = new Elements[n];
                Elements formElements2[] = new Elements[n];
                for (int i = 0; i < n; i++) {

                    doc[i] = Jsoup.connect(url[i]).get();
                    formElements1[i] = doc[i].select(html1);
                    formElements2[i] = doc[i].select(html2);

                    for (int j = 0; j < formElements2[i].size(); j++) {
                        products_megamarket.add(new Product());
                        products_megamarket.get(count_megamarket).setName_of_product(formElements1[i].get(j).text() + " - " + formElements2[i].get(j).text() + " (MegaMarket)");
                        products_megamarket.get(count_megamarket).formPrice_of_product(products_megamarket.get(count_megamarket).getName_of_product());

                        count_megamarket++;
                    }
                }
            }
        }
        catch (IOException e) {
        }
    }

    // Метод для парсинга супермаркета Fozzy
    public void getInfoAboutFozzy () {
        String html1 = "div.h3.product-title > a";
        String html2 = "span.product-price";

        try {
            int n = 2;

            String url[] = new String[n];
            switch (typeOfProduct) {
                case "Milk" :
                    n = 1;

                    url[0] = "https://fozzyshop.com.ua/300200-moloko?order=product.price.asc";
                    break;
                case "Bread" :
                    url[0] = "https://fozzyshop.com.ua/300505-khleb?order=product.price.asc";
                    break;
                case "Eggs" :
                    url[0] = "https://fozzyshop.com.ua/300212-yajca-kurinye/s-15/kategoriya-yajca_kurinye?order=product.price.asc";
                    break;
            }

            for (int i = 1; i < n; i++) {
                url[i] = url[0] + "&page=" + Integer.toString(i + 1);
            }

            Document doc[] = new Document[1000];
            Elements formElements1[] = new Elements[1000];
            Elements formElements2[] = new Elements[1000];

            for (int i = 0; i < n; i++) {

                doc[i] = Jsoup.connect(url[i]).get();
                formElements1[i] = doc[i].select(html1);
                formElements2[i] = doc[i].select(html2);

                for (int j = 0; j < formElements2[i].size(); j++) {
                    products_fozzy.add(new Product());
                    products_fozzy.get(count_fozzy).setName_of_product(formElements1[i].get(j).text() + " - " + formElements2[i].get(j).text() + " (Fozzy)");
                    products_fozzy.get(count_fozzy).formPrice_of_product(products_fozzy.get(count_fozzy).getName_of_product());

                    count_fozzy++;
                }
            }
        }
        catch (IOException e) {
        }
    }

    // Метод для чтения информации о продуктах из файла при отсутсвие интернета
   public void readFromFile(String fileName) {
        try{
            BufferedReader reader = null;
            reader = new BufferedReader(
                    new InputStreamReader(getAssets().open(fileName)));

            String mLine;
            while ((mLine = reader.readLine()) != null) {
                // Выбор к какой сети супермаркетов относить прочитаный продукт из файла
                if (fileName.lastIndexOf("novus") != -1)
                {
                    products_novus.add(new Product());
                    products_novus.get(count_novus).setName_of_product(mLine);
                    products_novus.get(count_novus).formPrice_of_product(mLine);
                    count_novus++;
                }

                if (fileName.lastIndexOf("megamarket") != -1)
                {
                    products_megamarket.add(new Product());
                    products_megamarket.get(count_megamarket).setName_of_product(mLine);
                    products_megamarket.get(count_megamarket).formPrice_of_product(mLine);
                    count_megamarket++;
                }

                if (fileName.lastIndexOf("fozzy") != -1)
                {
                    products_fozzy.add(new Product());
                    products_fozzy.get(count_fozzy).setName_of_product(mLine);
                    products_fozzy.get(count_fozzy).formPrice_of_product(mLine);
                    count_fozzy++;
                }

            }
        } catch (IOException e) {
        }
    }

    // Метод, который проверяет соединение с интернетом
    public boolean tryInternetConnection() {
        String cs = Context.CONNECTIVITY_SERVICE;
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(cs);
        if (cm.getActiveNetworkInfo() == null)
            return false;
        else
            return true;
    }

}