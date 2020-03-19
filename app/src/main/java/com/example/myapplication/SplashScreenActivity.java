package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
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
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SplashScreenActivity extends Activity {

    private int[] products_novus_price = new int[1000];
    private int[] products_megamarket_price = new int[1000];
    private int[] products_fozzy_price = new int[1000];

    private int count_novus, count_megamarket, count_fozzy;

    private String[] products_novus = new String[1000];
    private String[] products_megamarket = new String[1000];
    private String[] products_fozzy = new String[1000];

    // Время в милесекундах, в течение которого будет отображаться Splash Screen
    private final int SPLASH_DISPLAY_LENGTH = 500;

    private ImageView image_of_product;

    private TextView name_of_product;

    String z, typeOfProduct;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // тип продукта, который мы получаем с MainActivity
        typeOfProduct = getTypeOfProduct();

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

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        count_novus = 0;
        count_megamarket = 0;
        count_fozzy = 0;
        formProducts();
    }

    // метод лдля получения типа продукта
    public String getTypeOfProduct() {
        Bundle arguments1 = getIntent().getExtras();
        return arguments1.getString("TypeOfProduct");

    }

    // метод для парсинга 3 супермаркетов и отправки названий с ценами продуктов в AboutProduct
    public void formProducts() {
        Log.d("###", "formProducts()");
        // по истечении времени, запускаем активити, а SplashScreenActivity закрываем
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("###", "run()");

                /*getInfoAboutNovus();
                getInfoAboutMegaMarket();
                getInfoAboutFozzy();*/
                //readFromFile();
                //readFromFile("file_MegaMarket_" + typeOfProduct + ".txt");
                //readFromFile("file_Fozzy_" + typeOfProduct + ".txt");

                //readFromFile("file_megamarket_" + typeOfProduct.toLowerCase() + ".txt");
                //readFromFile("file_fozzy_" + typeOfProduct.toLowerCase() + ".txt");

                if (tryInternetConnection() == true)
                {
                    Log.e("###","+ Internet");
                    getInfoAboutNovus();
                    getInfoAboutMegaMarket();
                    getInfoAboutFozzy();
                }
                else
                {
                    Log.e("###","- Internet");
                    readFromFile("filenovus" + typeOfProduct.toLowerCase() + ".txt");
                    readFromFile("filemegamarket" + typeOfProduct.toLowerCase() + ".txt");
                    readFromFile("filefozzy" + typeOfProduct.toLowerCase() + ".txt");
                }

                Intent mainIntent = new Intent(SplashScreenActivity.this, AboutProduct.class);
                mainIntent.putExtra("Name1", products_novus);
                mainIntent.putExtra("Name2", products_megamarket);
                mainIntent.putExtra("Name3", products_fozzy);
                mainIntent.putExtra("Price1", products_novus_price);
                mainIntent.putExtra("Price2", products_megamarket_price);
                mainIntent.putExtra("Price3", products_fozzy_price);
                mainIntent.putExtra("Numbers1", count_novus);
                mainIntent.putExtra("Numbers2", count_megamarket);
                mainIntent.putExtra("Numbers3", count_fozzy);
                mainIntent.putExtra("Stroka", z);
                mainIntent.putExtra("TypeOfProduct", typeOfProduct);
                SplashScreenActivity.this.startActivity(mainIntent);
                SplashScreenActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    // метод для парсинга Novus
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
        String html1, html2, html3, html4;

        // html для названия продукта
        html1 = "div.one-product-name";
        // html для цены в гривнах
        html2 = "span.grivna.price";
        // html для цены в копейках
        html3 = "span.kopeiki";
        // html для количества продуктов
        html4 = "span.search-number-of-items";

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

            // получение информации о продуктах
            for (int i = 0; i < m; i++)
            {
                doc[i] = Jsoup.connect(url[i]).get();
                formElements1[i] = doc[i].select(html1);
                formElements2[i] = doc[i].select(html2);
                formElements3[i] = doc[i].select(html3);
                for (int j = 0; j < formElements1[i].size(); j++) {
                    // название и цена каждого продукта
                    products_novus[count_novus] = formElements1[i].get(j).text() + " - " + formElements2[i].get(j).text() + "," + formElements3[i].get(j).text() + " грн (Novus)";
                    products_novus_price[count_novus] = Integer.parseInt(formElements2[i].get(j).text() + formElements3[i].get(j).text());
                    //str.append(products_novus[count_novus] + '\n');
                    Log.e("Novus", products_novus[count_novus] + " " + Integer.toString(products_novus_price[count_novus]));
                    count_novus++;
                }
            }
        }
        catch (IOException e) {
        }
       // Log.d("####", str.toString());
    }

    // метод для парсинга MegaMarket
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
                        products_megamarket[count_megamarket] = formElements1[i].get(j).text() + " - " + formElements2[i].get(j).text() + " (MegaMarket)";
                        String stroka = formElements2[i].get(j).text();
                        products_megamarket_price[count_megamarket] = Integer.parseInt(stroka.substring(0, stroka.lastIndexOf(",")) + stroka.substring(stroka.lastIndexOf(",") + 1, stroka.length() - 4));

                        Log.e("MegaMarket", products_megamarket[count_megamarket] + " " + Integer.toString(products_megamarket_price[count_megamarket]));
                        count_megamarket++;
                    }
                }
            }
        }
        catch (IOException e) {
        }
    }

    // метод для парсинга Fozzy
    public void getInfoAboutFozzy () {
        Log.e("###", "Fozzy");
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
                Log.e("Fozzys", formElements1[0].get(0).text());
                Log.e("Fozzys", formElements2[0].get(0).text());

                // продукты, которые можно рассмотреть (есть в наличии)
                int k = formElements2[i].size();
                Log.e("Fozzys", Integer.toString(k));

                // получение массива продуктов с помощью парсинга сайта
                for (int j = 0; j < k; j++) {
                    products_fozzy[count_fozzy] = formElements1[i].get(j).text() + " - " + formElements2[i].get(j).text() + " (Fozzy)";
                    String stroka1 = formElements2[i].get(j).text();
                    products_fozzy_price[count_fozzy] = Integer.parseInt(stroka1.substring(0,stroka1.lastIndexOf(",")) + stroka1.substring(stroka1.lastIndexOf(",") + 1,stroka1.length() - 4));
                    Log.e("Fozzys", products_fozzy[count_fozzy]);
                    Log.e("Fozzys", formElements2[i].get(j).text());
                    count_fozzy++;
                }
            }
            z = getString(R.string.products);
        }
        catch (IOException e) {
            z = getString(R.string.problem_with_internet_connection);
        }
    }

    public void readFromFile(String fileName) {
        Log.d("#####", fileName);
        /*BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(getAssets().open("")));
            String mLine;

            while ((mLine = reader.readLine()) != null) {
                /*String stroka1 = mLine.substring(mLine.lastIndexOf(" - ") - 1 + 4, mLine.lastIndexOf(" грн"));
                String price1 = stroka1.substring(0, stroka1.lastIndexOf(",")) +  stroka1.substring(stroka1.lastIndexOf(",") + 1, stroka1.length() - 1);*/
                //Log.d("####",mLine);
                /*if (file_name.lastIndexOf("Fozzy") != -1)
                {
                    products_fozzy[count_fozzy] = mLine;
                    products_fozzy_price[count_fozzy] = Integer.parseInt(price1);
                    count_fozzy++;
                }

                if (file_name.lastIndexOf("Novus") != -1)
                {
                    products_novus[count_novus] = mLine;
                    products_novus_price[count_novus] = Integer.parseInt(price1);
                    count_novus++;
                }

                if (file_name.lastIndexOf("MegaMarket") != -1)
                {
                    products_megamarket[count_megamarket] = mLine;
                    products_megamarket_price[count_megamarket] = Integer.parseInt(price1);
                    count_megamarket++;
                }
            }
        }
        catch (IOException e) {
        }
            */
        try{
            BufferedReader reader = null;
            reader = new BufferedReader(
                    new InputStreamReader(getAssets().open(fileName)));

            String mLine;
            while ((mLine = reader.readLine()) != null) {
                Log.d("####", mLine);
                String stroka1 = mLine.substring(mLine.lastIndexOf(" - ") - 1 + 4, mLine.lastIndexOf(" грн"));
                String price1 = stroka1.substring(0, stroka1.lastIndexOf(",")) +  stroka1.substring(stroka1.lastIndexOf(",") + 1, stroka1.length());

                if (fileName.lastIndexOf("fozzy") != -1)
                {
                    products_fozzy[count_fozzy] = mLine;
                    products_fozzy_price[count_fozzy] = Integer.parseInt(price1);
                    count_fozzy++;
                }
                if (fileName.lastIndexOf("novus") != -1)
                {
                    products_novus[count_novus] = mLine;
                    products_novus_price[count_novus] = Integer.parseInt(price1);
                    count_novus++;
                }
                if (fileName.lastIndexOf("megamarket") != -1)
                {
                    products_megamarket[count_megamarket] = mLine;
                    products_megamarket_price[count_megamarket] = Integer.parseInt(price1);
                    count_megamarket++;
                }

                z = getString(R.string.products);
            }
        } catch (IOException e) {
            z = getString(R.string.problem_with_internet_connection);
            e.printStackTrace();
        }
    }

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