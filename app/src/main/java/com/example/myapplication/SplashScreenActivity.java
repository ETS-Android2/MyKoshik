package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
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

public class SplashScreenActivity extends Activity {

    private int count_novus, count_megamarket, count_fozzy;

    public ArrayList<Product> products_novus = new ArrayList<Product>();
    public ArrayList<Product> products_megamarket = new ArrayList<Product>();
    public ArrayList<Product> products_fozzy = new ArrayList<Product>();

    // Время в милесекундах, в течение которого будет отображаться Splash Screen
    private final int SPLASH_DISPLAY_LENGTH = 500;

    private ImageView image_of_product;

    private TextView name_of_product;

    String z, typeOfProduct;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Тип продукта, который мы получаем с MainActivity
        typeOfProduct = getTypeOfProduct();

        formLayout();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        count_novus = 0;
        count_megamarket = 0;
        count_fozzy = 0;

        formProducts();
    }

    // Метод, котрый формирует Layout в зависимости от типа продукта
    public void formLayout() {
        setContentView(R.layout.splash_screen);

        name_of_product = findViewById(R.id.typeOfProduct);
        image_of_product = findViewById(R.id.splash_screen);

        switch (typeOfProduct) {
            case "Milk" :
                image_of_product.setImageResource(R.drawable.milk);
                break;
            case "Bread" :
                image_of_product.setImageResource(R.drawable.bread);
                break;
            case "Eggs" :
                image_of_product.setImageResource(R.drawable.eggs);
                break;
        }

        image_of_product.getLayoutParams().height = 760;
        image_of_product.getLayoutParams().width = 800;
        image_of_product.setScaleType(ImageView.ScaleType.FIT_XY);
    }

    // Метод получения типа продукта
    public String getTypeOfProduct() {
        Bundle arguments1 = getIntent().getExtras();
        return arguments1.getString("TypeOfProduct");

    }

    // Метод отправки названий с ценами продуктов в AboutProduct
    public void formProducts() {
        // по истечении времени, запускаем активити, а SplashScreenActivity закрываем
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (tryInternetConnection() == true)
                {
                    getInfoAboutNovus();
                    getInfoAboutMegaMarket();
                    getInfoAboutFozzy();
                }
                else
                {
                   readFromFile("filenovus" + typeOfProduct.toLowerCase() + ".txt");
                   readFromFile("filemegamarket" + typeOfProduct.toLowerCase() + ".txt");
                   readFromFile("filefozzy" + typeOfProduct.toLowerCase() + ".txt");
                }

                Intent intent = new Intent(SplashScreenActivity.this, AboutProduct.class);
                Bundle args = new Bundle();

                args.putSerializable("Name1", (Serializable) products_novus);
                args.putSerializable("Name2", (Serializable) products_megamarket);
                args.putSerializable("Name3", (Serializable) products_fozzy);
                args.putInt("Numbers1", count_novus);
                args.putInt("Numbers2", count_megamarket);
                args.putInt("Numbers3", count_fozzy);
                args.putString("Stroka", z);
                args.putString("TypeOfProduct", typeOfProduct);
                intent.putExtra("BUNDLE",args);
                SplashScreenActivity.this.startActivity(intent);
                SplashScreenActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    // Метод для парсинга супермаркета Novus
    public void getInfoAboutNovus () {

        StringBuffer str = new StringBuffer();
        String a;
        switch (typeOfProduct) {
            case "Milk" :
                a = "milk";
                break;
            case "Bread" :
                a = "bread";
                break;
            case "Eggs" :
                a = "eggs";
                break;
            default:
                a = "";
                break;
        }
        String html1, html2, html3, html4, html5;

        // html для названия продукта
        html1 = "div.one-product-name";
        // html для цены в гривнах
        html2 = "span.grivna.price";
        // html для цены в копейках
        html3 = "span.kopeiki";
        // html для количества продуктов
        html4 = "span.search-number-of-items";

        html5 = "button.btn.btn-mini.product-add-to-cart-button > span.one-product-price";

        try {
            Document doc1 = Jsoup.connect("https://old.novus.zakaz.ua/uk/" + a + "/?&sort=up").get();
            Elements formElements4 = doc1.select(html4);

            // количество продуктов
            int n = 1;
            // количество страниц с продуктами
            int m;
            // высчитывание количества страниц
            if (n % 50 != 0)
                m = n / 50 + 1;
            else
                m = n / 50;

            // формулирование url с продуктами
            String url[] = new String [m];
            url[0] = "https://old.novus.zakaz.ua/uk/" + a + "/?&sort=up";
            for (int i = 1; i < m; i++)
            {
                url[i] = "https://old.novus.zakaz.ua/uk/" + a + "/?&sort=up&page=" + (i+1);
            }

            Document doc[] = new Document[m];
            Elements formElements1[] = new Elements[m];
            Elements formElements2[] = new Elements[m];
            Elements formElements3[] = new Elements[m];
            Elements formElements5[] = new Elements[m];
            // получение информации о продуктах
            for (int i = 0; i < m; i++)
            {
                doc[i] = Jsoup.connect(url[i]).get();
                formElements1[i] = doc[i].select(html1);
                formElements2[i] = doc[i].select(html2);
                formElements3[i] = doc[i].select(html3);
                formElements5[i] = doc[i].select(html5);

                for (int j = 0; j < formElements1[i].size(); j++) {
                    String s = formElements5[i].get(j).text();
                    Log.d("SpanPrice", "."+s.substring(0, s.length()-4)+".");
                    Log.d("SpanPrice", s);
                    Log.d("SpanPrice", "."+s.substring(0, s.length()-6)+","+s.substring(s.length()-6, s.length())+".");
                    // название и цена каждого продукта
                    products_novus.add(new Product());
                    products_novus.get(count_novus).setName_of_product(formElements1[i].get(j).text() + " - " + s.substring(0, s.length()-6)+","+s.substring(s.length()-6, s.length()) + " (Novus)");
                    products_novus.get(count_novus).setPrice_of_product(Integer.parseInt(s.substring(0, s.length()-4)));

                    Log.d("###", products_novus.get(count_novus).getName_of_product());
                    Log.d("###", Integer.toString(products_novus.get(count_novus).getPrice_of_product()));

                    count_novus++;
                }
            }
        }
        catch (IOException e) {
        }
    }

    // Метод для парсинга супермаркета MegaMarket
    public void getInfoAboutMegaMarket () {
        String a;
        switch (typeOfProduct) {
            case "Milk" :
                a = "moloko";
                break;
            case "Eggs" :
                a = "yajtsya";
                break;
            default:
                a = "";
                break;
        }

        String html1, html2;

        html1 = "li.product > div.product_info > h3";
        html2 = "div.price";

        try {
            // количество страниц с продуктами
            int m;
            if (typeOfProduct.equals("Eggs"))
                m = 1;
            else
                m = 2;
            // формулирование url с продуктами
            String url[] = new String[m];
            if (a != "") {
                url[0] = "https://megamarket.ua/catalog/" + a + "?sort=price";
                for (int i = 1; i < m; i++) {
                    url[i] = url[0] + "&page=" + (i + 1);
                }

                Document doc[] = new Document[m];
                Elements formElements1[] = new Elements[m];
                Elements formElements2[] = new Elements[m];
                for (int i = 0; i < m; i++) {

                    doc[i] = Jsoup.connect(url[i]).get();
                    formElements1[i] = doc[i].select(html1);
                    formElements2[i] = doc[i].select(html2);

                    int k;
                    if (i != m - 1)
                        // продукты всегда в наличии
                        k = 40;
                    else {
                        // продукты, которые можно рассмотреть (есть в наличии)
                        k = formElements2[i].size();
                    }
                    for (int j = 0; j < k; j++) {
                        // название и цена каждого продукта
                        products_megamarket.add(new Product());
                        products_megamarket.get(count_megamarket).setName_of_product(formElements1[i].get(j).text() + " - " + formElements2[i].get(j).text() + " (MegaMarket)");
                        products_megamarket.get(count_megamarket).formPrice_of_product(products_megamarket.get(count_megamarket).getName_of_product());

                        Log.d("###", products_megamarket.get(count_megamarket).getName_of_product());
                        Log.d("###", Integer.toString(products_megamarket.get(count_megamarket).getPrice_of_product()));

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
        // html названия продукта и цены
        String html1 = "div.h3.product-title > a";
        String html2 = "span.product-price";

        try {
            // количество страниц с продуктами
            int m = 2;

            // формулирование url с продуктами
            String url[] = new String[m];
            if (typeOfProduct.equals("Milk")) {
                url[0] = "https://fozzyshop.com.ua/300200-moloko?order=product.price.asc";
                m = 1;
                for (int i = 1; i < m; i++) {
                    url[i] = "https://fozzyshop.com.ua/300200-moloko?order=product.price.asc" + "&page=" + Integer.toString(i + 1);
                }
            }
            if (typeOfProduct.equals("Bread")) {
                url[0] = "https://fozzyshop.com.ua/300505-khleb?order=product.price.asc";
                for (int i = 1; i < m; i++) {
                    url[i] = "https://fozzyshop.com.ua/300505-khleb?order=product.price.asc&page=" + (i + 1);
                }
            }
            if (typeOfProduct.equals("Eggs")) {
                url[0] = "https://fozzyshop.com.ua/300212-yajca-kurinye/s-15/kategoriya-yajca_kurinye?order=product.price.asc";
                for (int i = 1; i < m; i++) {
                    url[i] = "https://fozzyshop.com.ua/300212-yajca-kurinye/s-15/kategoriya-yajca_kurinye?page=" + (i + 1) + "&order=product.price.asc";
                }
            }

            Document doc[] = new Document[m];
            Elements formElements1[] = new Elements[m];
            Elements formElements2[] = new Elements[m];

            //String products_fozzy[] = new String[100];

            for (int i = 0; i < m; i++) {

                doc[i] = Jsoup.connect(url[i]).get();
                formElements1[i] = doc[i].select(html1);
                formElements2[i] = doc[i].select(html2);

                // продукты, которые можно рассмотреть (есть в наличии)
                int k = formElements2[i].size();

                // получение массива продуктов с помощью парсинга сайта
                for (int j = 0; j < k; j++) {
                    products_fozzy.add(new Product());
                    products_fozzy.get(count_fozzy).setName_of_product(formElements1[i].get(j).text() + " - " + formElements2[i].get(j).text() + " (Fozzy)");
                    products_fozzy.get(count_fozzy).formPrice_of_product(products_fozzy.get(count_fozzy).getName_of_product());

                    Log.d("###", products_fozzy.get(count_fozzy).getName_of_product());
                    Log.d("###", Integer.toString(products_fozzy.get(count_fozzy).getPrice_of_product()));

                    count_fozzy++;
                }
            }
            z = getString(R.string.products);
        }
        catch (IOException e) {
            z = getString(R.string.problem_with_internet_connection);
        }
    }

    // Метод чтения информации о продуктах из файла
   public void readFromFile(String fileName) {
        try{
            BufferedReader reader = null;
            reader = new BufferedReader(
                    new InputStreamReader(getAssets().open(fileName)));

            String mLine;
            while ((mLine = reader.readLine()) != null) {
                String s = mLine.substring(mLine.lastIndexOf(" - ") - 1 + 4, mLine.lastIndexOf(" грн"));
                String price = s.substring(0, s.lastIndexOf(",")) +  s.substring(s.lastIndexOf(",") + 1, s.length());

                if (fileName.lastIndexOf("novus") != -1)
                {
                    products_novus.get(count_novus).setName_of_product(mLine);
                    products_novus.get(count_novus).setPrice_of_product(Integer.parseInt(price));
                    count_novus++;
                }

                if (fileName.lastIndexOf("fozzy") != -1)
                {
                    products_fozzy.get(count_fozzy).setName_of_product(mLine);
                    products_fozzy.get(count_fozzy).setPrice_of_product(Integer.parseInt(price));
                    count_fozzy++;
                }

                if (fileName.lastIndexOf("megamarket") != -1)
                {
                    products_megamarket.get(count_megamarket).setName_of_product(mLine);
                    products_megamarket.get(count_megamarket).setPrice_of_product(Integer.parseInt(price));
                    count_megamarket++;
                }

                z = getString(R.string.products);
            }
        } catch (IOException e) {
            z = getString(R.string.problem_with_internet_connection);
            e.printStackTrace();
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