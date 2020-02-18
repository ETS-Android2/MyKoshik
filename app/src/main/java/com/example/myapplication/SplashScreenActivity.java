package com.example.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import android.os.StrictMode;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class SplashScreenActivity extends Activity {

    private int[] products_novus_price = new int[1000];
    private int[] products_megamarket_price = new int[1000];
    private int[] products_fozzy_price = new int[1000];

    private int count_novus, count_megamarket, count_fozzy, count_novus_megamarket, count_megamarket_fozzy, count_novus_fozzy, count_novus_megamarket_fozzy;

    private String[] products_novus = new String[1000];
    private String[] products_megamarket = new String[1000];
    private String[] products_fozzy = new String[1000];

    // Время в милесекундах, в течение которого будет отображаться Splash Screen
    private final int SPLASH_DISPLAY_LENGTH = 1000;

    String z;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        Toast.makeText(SplashScreenActivity.this, "Загрузка продуктов. Ожидайте пожалуйста", Toast.LENGTH_LONG).show();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getInfoAboutNovus();
                getInfoAboutMegaMarket();
                getInfoAboutFozzy();

                // По истечении времени, запускаем главный активити, а Splash Screen закрываем
                Intent mainIntent = new Intent(SplashScreenActivity.this, AboutMilk.class);
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
                SplashScreenActivity.this.startActivity(mainIntent);
                SplashScreenActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
    public void getInfoAboutNovus () {
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
            Document doc1 = Jsoup.connect("https://old.novus.zakaz.ua/ru/milk/?&sort=up").get();
            Elements formElements4 = doc1.select(html4);

            // количество продуктов
            int n = 1;
            //int n = Integer.parseInt(formElements4.text().substring(formElements4.text().indexOf("(")+1,formElements4.text().indexOf(" ")));
            // количество страниц с продуктами
            int m;
            // высчитывание количества страниц
            if (n % 50 != 0)
                m = n / 50 + 1;
            else
                m = n / 50;

            // формулирование url с продуктами
            String url[] = new String [m];
            url[0] = "https://old.novus.zakaz.ua/ru/milk/?&sort=up";
            for (int i = 1; i < m; i++)
            {
                url[i] = "https://old.novus.zakaz.ua/ru/milk/?&sort=up&page=" + (i+1);
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
                    count_novus++;
                }
            }
        }
        catch (IOException e) {
        }
    }

    // метод для извлечения и вывода информации о продуктах из MegaMarket
    public void getInfoAboutMegaMarket () {
        String html1, html2;

        html1 = "li.product > div.product_info > h3";
        html2 = "div.price";

        try {
            // количество страниц с продуктами
            int m = 2;

            // формулирование url с продуктами
            String url[] = new String[m];
            url[0] = "https://megamarket.ua/catalog/moloko?sort=price";
            for (int i = 1; i < m; i++) {
                url[i] = "https://megamarket.ua/catalog/moloko?sort=price&page=" + (i + 1);
            }

            Document doc[] = new Document[m];
            Elements formElements1[] = new Elements[m];
            Elements formElements2[] = new Elements[m];
            for (int i = 0; i < m; i++) {

                doc[i] = Jsoup.connect(url[i]).get();
                formElements1[i] = doc[i].select(html1);
                formElements2[i] = doc[i].select(html2);

                int z;
                if (i != m-1)
                    // продукты всегда в наличии
                    z = 40;
                else {
                    // продукты, которые можно рассмотреть (есть в наличии)
                    z = formElements2[i].size();
                }
                for (int j = 0; j < z; j++) {
                    // название и цена каждого продукта
                    products_megamarket[count_megamarket] = formElements1[i].get(j).text() + " - " + formElements2[i].get(j).text() + " (MegaMarket)";
                    String stroka = formElements2[i].get(j).text();
                    products_megamarket_price[count_megamarket] = Integer.parseInt(stroka.substring(0,stroka.lastIndexOf(",")) + stroka.substring(stroka.lastIndexOf(",") + 1,stroka.length() - 4));
                    count_megamarket++;
                }
            }
        }
        catch (IOException e) {
        }
    }
    // метод для извлечения и вывода информации о продуктах из Fozzy
    public void getInfoAboutFozzy () {
        // html названия продукта и цены
        String html1 = "h3.h3.product-title > a";
        String html2 = "span.product-price";

        try {
            // количество страниц с продуктами
            int m = 2;

            // формулирование url с продуктами
            String url[] = new String[m];
            url[0] = "https://fozzyshop.com.ua/300200-moloko/s-15/kategoriya-moloko?order=product.price.asc";
            for (int i = 1; i < m; i++) {
                url[i] = "https://fozzyshop.com.ua/300200-moloko/s-15/kategoriya-moloko?page=" + (i+1) + "&order=product.price.asc";
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
                int z = formElements2[i].size();

                // получение массива продуктов с помощью парсинга сайта
                for (int j = 0; j < z; j++) {
                    products_fozzy[count_fozzy] = formElements1[i].get(j).text() + " - " + formElements2[i].get(j).text() + " (Fozzy)";
                    String stroka = formElements2[i].get(j).text();
                    products_fozzy_price[count_fozzy] = Integer.parseInt(stroka.substring(0,stroka.lastIndexOf(",")) + stroka.substring(stroka.lastIndexOf(",") + 1,stroka.length() - 4));
                    count_fozzy++;
                }
            }
            z = "Продукты :";
        }
        catch (IOException e) {
            z = "Проверьте соединение с интернетом";
        }

    }

}
