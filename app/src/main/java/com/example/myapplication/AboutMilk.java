package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

public class AboutMilk extends AppCompatActivity {

    private TextView result;
    private Button button_of_novus, button_of_megamarket, button_of_fozzy, button_of_continue;
    private LinearLayout linearLayout;

    private int x, y;

    private int count_novus, count_megamarket, count_fozzy, count_novus_megamarket, count_megamarket_fozzy, count_novus_fozzy, count_novus_megamarket_fozzy;

    private String[] products_novus = new String[1000];
    private String[] products_megamarket = new String[1000];
    private String[] products_fozzy = new String[1000];
    private String[] products_novus_megamarket = new String[1000];
    private String[] products_megamarket_fozzy = new String[1000];
    private String[] products_novus_fozzy =  new String[1000];
    private String[] products_novus_megamarket_fozzy =  new String[1000];


    private int[] products_novus_price = new int[1000];
    private int[] products_megamarket_price = new int[1000];
    private int[] products_fozzy_price = new int[1000];

    private boolean f1, f2, f3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about_milk);

        button_of_novus = findViewById(R.id.button_of_novus);
        button_of_megamarket = findViewById(R.id.button_of_megamarket);
        button_of_fozzy = findViewById(R.id.button_of_fozzy);
        button_of_continue = findViewById(R.id.button_of_continue);

        button_of_novus.setBackgroundColor(getColor(R.color.colorBlue));
        button_of_megamarket.setBackgroundColor(getColor(R.color.colorBlue));
        button_of_fozzy.setBackgroundColor(getColor(R.color.colorBlue));
        button_of_continue.setBackgroundColor(getColor(R.color.colorGreen));

        result = findViewById(R.id.list_of_products_of_shop);

        linearLayout = findViewById(R.id.linearlayout1);

        x = 1;
        y = 1;

        count_novus = 0;
        count_megamarket = 0;
        count_fozzy = 0;

        count_novus_megamarket = 0;
        count_megamarket_fozzy = 0;
        count_novus_fozzy = 0;

        count_novus_megamarket_fozzy = 0;

        f1 = false;
        f2 = false;
        f3 = false;

        Bundle arguments = getIntent().getExtras();
        products_novus = arguments.getStringArray("Name1");
        products_megamarket = arguments.getStringArray("Name2");
        products_fozzy = arguments.getStringArray("Name3");

        products_novus_price = arguments.getIntArray("Price1");
        products_megamarket_price = arguments.getIntArray("Price2");
        products_fozzy_price = arguments.getIntArray("Price3");

        String z = arguments.getString("Stroka");

        result.setText(z);


    }

    public void onClickButtonShop(View view) {

        if (view.getId() == button_of_novus.getId()) {
            if (f1 == false) {
                button_of_novus.setBackgroundColor(getColor(R.color.colorPurple));
            }
            else
            {
                button_of_novus.setBackgroundColor(getColor(R.color.colorBlue));
            }
            f1 = !f1;
        }
        if (view.getId() == button_of_megamarket.getId()) {
            if (f2 == false) {
                button_of_megamarket.setBackgroundColor(getColor(R.color.colorPurple));
            }
            else
            {
                button_of_megamarket.setBackgroundColor(getColor(R.color.colorBlue));
            }
            f2 = !f2;
        }
        if (view.getId() == button_of_fozzy.getId()) {
            if (f3 == false) {
                button_of_fozzy.setBackgroundColor(getColor(R.color.colorPurple));
            }
            else
            {
                button_of_fozzy.setBackgroundColor(getColor(R.color.colorBlue));
            }
            f3 = !f3;
        }

        if (view.getId() == button_of_continue.getId())
        {
            deleteListOfProducts();

            if ((f1 == true) && (f2 == false) && (f3 == false))
            {
                if (count_novus == 0)
                    getInfoAboutNovus();

                viv(products_novus, count_novus);
            }
            else
            if ((f2 == true) && (f1 == false) && (f3 == false))
            {
                if (count_megamarket == 0)
                    getInfoAboutMegaMarket();

                viv(products_megamarket, count_megamarket);
            }
            else
            if ((f3 == true) && (f1 == false) && (f2 == false))
            {
                if (count_fozzy == 0)
                    getInfoAboutFozzy();

                viv(products_fozzy, count_fozzy);
            }
            else
            if ((f1 == true) && (f2 == true) && (f3 == false))
            {
                if (count_novus == 0)
                    getInfoAboutNovus();
                if (count_novus_megamarket == 0)
                    getInfoAboutMegaMarket();


                if (count_novus_megamarket == 0)
                    getInfoAboutTwoShops(products_novus, products_megamarket, count_novus, count_megamarket, products_novus_price, products_megamarket_price, "Novus_MegaMarket");

                viv(products_novus_megamarket, count_novus_megamarket);
            }
            else
            if ((f1 == true) && (f3 == true) && (f2 == false))
            {
                if (count_novus == 0)
                    getInfoAboutNovus();
                if (count_fozzy == 0)
                    getInfoAboutFozzy();

                if (count_novus_fozzy == 0)
                    getInfoAboutTwoShops(products_novus, products_fozzy, count_novus, count_fozzy, products_novus_price, products_fozzy_price, "Novus_Fozzy");

                viv(products_novus_fozzy, count_novus_fozzy);
            }
            else
            if ((f2 == true) && (f3 == true) && (f1 == false))
            {
                if (count_megamarket == 0)
                    getInfoAboutMegaMarket();
                if (count_fozzy == 0)
                    getInfoAboutFozzy();

                if (count_novus_fozzy == 0)
                    getInfoAboutTwoShops(products_megamarket, products_fozzy, count_megamarket, count_fozzy, products_megamarket_price, products_fozzy_price, "MegaMarket_Fozzy");

                viv(products_megamarket_fozzy, count_megamarket_fozzy);
            }
            else
            if ((f1 == true) && (f2 == true) && (f3 == true))
            {
                if (count_novus == 0)
                    getInfoAboutNovus();
                if (count_megamarket == 0)
                    getInfoAboutMegaMarket();
                if (count_fozzy == 0)
                    getInfoAboutFozzy();

                if (count_novus_megamarket_fozzy == 0)
                    getInfoAboutTwoShops(products_novus, products_megamarket, count_novus, count_megamarket, products_novus_price, products_megamarket_price, "Novus_MegaMarket_Fozzy1");

                viv(products_novus_megamarket_fozzy, count_novus_megamarket_fozzy);
            }
            else
                result.setText("Выберите одну кнопку чтобы продолжить");

            f1 = false;
            f2 = false;
            f3 = false;

            button_of_novus.setBackgroundColor(getColor(R.color.colorBlue));
            button_of_megamarket.setBackgroundColor(getColor(R.color.colorBlue));
            button_of_fozzy.setBackgroundColor(getColor(R.color.colorBlue));
        }

    }

    public void getInfoAboutTwoShops(String[] s1, String[] s2, int k1, int k2, int[] a, int[] b, String z) {
        int i = 0;
        int j = 0;
        int l = 0;
        String[] s3 = new String[1000];
        int[] price = new int[1000];
        while ((i < k1) && (j < k2)) {
            if (a[i] < b[j]) {
                s3[l] = s1[i];
                price[l] = a[i];
                i++;
                l++;
            }
            else
            {
                s3[l] = s2[j];
                price[l] = b[j];
                j++;
                l++;
            }
        }
        while (i < k1) {
            s3[l] = s1[i];
            price[l] = a[i];
            i++;
            l++;
        }
        while (j < k2) {
            s3[l] = s2[j];
            price[l] = b[j];
            j++;
            l++;
        }
        switch(z) {
            case "Novus_MegaMarket" :
                for (int k = 0; k<l; k++)
                    products_novus_megamarket[k] = s3[k];
                count_novus_megamarket = l;
                break;
            case "MegaMarket_Fozzy" :
                for (int k = 0; k<l; k++)
                    products_megamarket_fozzy[k] = s3[k];
                count_megamarket_fozzy = l;
                break;
            case "Novus_Fozzy" :
                for (int k = 0; k<l; k++)
                    products_novus_fozzy[k] = s3[k];
                count_novus_fozzy = l;
                break;
            case "Novus_MegaMarket_Fozzy1" :
                getInfoAboutTwoShops(s3, products_fozzy, l, count_fozzy, price, products_fozzy_price, "Novus_MegaMarket_Fozzy2");
                Log.d("#####",Integer.toString(count_novus_megamarket_fozzy));
                break;
            case "Novus_MegaMarket_Fozzy2" :
                for (int k = 0; k<l; k++)
                    products_novus_megamarket_fozzy[k] = s3[k];
                count_novus_megamarket_fozzy = l;
                Log.d("#####",Integer.toString(count_novus_megamarket_fozzy));
                break;
        }
    }

    public void viv(String[] products, int count) {
        for (int i = 0; i<count; i++)
            addButtonAndTextView(products[i], i+1);

    }

    // метод для извлечения и вывода информации о продуктах из Novus
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
                    products_novus[count_novus] = Integer.toString(i * 50 + j + 1) + ") " + formElements1[i].get(j).text() + " - " + formElements2[i].get(j).text() + "," + formElements3[i].get(j).text() + " грн (Novus)";
                    products_novus_price[count_novus] = Integer.parseInt(formElements2[i].get(j).text() + formElements3[i].get(j).text());
                    count_novus++;
                }
            }

            result.setText("Продукты : ");
        }
        catch (IOException e) {
            result.setText("Проверьте соединение с интернетом");
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
                    products_megamarket[count_megamarket] = Integer.toString(i * 40 + j + 1) + ") " + formElements1[i].get(j).text() + " - " + formElements2[i].get(j).text() + " (MegaMarket)";
                    String stroka = formElements2[i].get(j).text();
                    products_megamarket_price[count_megamarket] = Integer.parseInt(stroka.substring(0,stroka.lastIndexOf(",")) + stroka.substring(stroka.lastIndexOf(",") + 1,stroka.length() - 4));
                    count_megamarket++;
                }
            }
            result.setText("Продукты : ");
        }
        catch (IOException e) {
            result.setText("Проверьте соединение с интернетом");
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
                    products_fozzy[count_fozzy] = Integer.toString(i * 24 + j + 1) + ") " + formElements1[i].get(j).text() + " - " + formElements2[i].get(j).text() + " (Fozzy)";
                    String stroka = formElements2[i].get(j).text();
                    products_fozzy_price[count_fozzy] = Integer.parseInt(stroka.substring(0,stroka.lastIndexOf(",")) + stroka.substring(stroka.lastIndexOf(",") + 1,stroka.length() - 4));
                    count_fozzy++;
                }
            }
            result.setText("Продукты : ");
        }
        catch (IOException e) {
            result.setText("Проверьте соединение с интернетом");
        }

    }

    // метод для удаления Buttons и TextViews, чтобы не было повтора ID при создание новых Butons и TextViews
    public void deleteListOfProducts() {
        for (int i = x; i < y; i++)
        {
            View b = findViewById(i);
            b.setVisibility(View.GONE);
        }

        // делаем типо "обнуление" количества Butons и TextViews
        x = y;
    }

    // метод для создание Button и TextView для каждого продукта
    public void addButtonAndTextView(final String product, int number) {
        TextView t = new TextView(getApplicationContext());
        t.setText('\n' + product + '\n');
        t.setId(y);
        y++;
        linearLayout.addView(t);

        Button b = new Button(getApplicationContext());
        b.setText("Купить продукт №" + Integer.toString(number));
        b.setId(y);
        y++;
        linearLayout.addView(b);
        b.setBackgroundColor(getColor(R.color.colorOrange));

        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                write(product.substring(3,product.length()));
            }
        });

    }
    public void write(String shop) {

        try {
            // -> часть из метода read для write
            StringBuffer strBuffer = new StringBuffer();
            String stroka;
            try {
                FileInputStream fileInput = openFileInput("example.txt");
                InputStreamReader reader = new InputStreamReader(fileInput);
                BufferedReader buffer = new BufferedReader(reader);
                String lines;

                while ((lines = buffer.readLine()) != null) {
                    strBuffer.append(lines + '\n');
                }

                stroka = strBuffer.toString() + shop;
            }
            catch (IOException e)
            {
                stroka = shop;
            }
            // класс, который помогает помещать данные в файл
            FileOutputStream fileOutput = openFileOutput("example.txt", MODE_PRIVATE);
            fileOutput.write(stroka.getBytes());
            fileOutput.close();

            Toast.makeText(AboutMilk.this, "Продукт был добавлен в список покупок", Toast.LENGTH_LONG).show();
        }
        catch (FileNotFoundException e )
        {
        }
        catch (IOException e )
        {
        }

    }
}