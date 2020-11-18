package activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import classes.Product;

// Activity для показа SplashScreen (Экран загрузки) и загрузки продуктов
public class SplashScreenActivity extends AppCompatActivity {
    // Картинка с продуктом
    private ImageView picture_image_of_product;

    // Надпись с типом продукта и надпись о обновлении данных
    private TextView textview_name_of_product, textview_update;

    // Кнопка "Да" и "Нет" для диалогового окна
    private Button button_yes, button_no;

    private Dialog dialog;

    private ProgressBar progressBar;

    // Тип продукта
    String typeOfProduct;

    private boolean internetConection = true;

    // ArrayList продуктов из разных супермаркетов
    public ArrayList<Product> products_novus = new ArrayList<Product>();
    public ArrayList<Product> products_megamarket = new ArrayList<Product>();
    public ArrayList<Product> products_fozzy = new ArrayList<Product>();

    // Время в милесекундах, в течение которого или больше будет отображаться SplashScreen
    private final int SPLASH_DISPLAY_LENGTH = 500;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        typeOfProduct = getTypeOfProduct();

        formSplashScreen();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        dialog = new Dialog(SplashScreenActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_window_splashscreen);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        textview_update = dialog.findViewById(R.id.textview_version);
        button_yes = dialog.findViewById(R.id.button_yes);
        button_no = dialog.findViewById(R.id.button_no);

        textview_update.setText(getString(R.string.dialog_text1_splashscreen) + getString(R.string.dialog_text2_splashscreen) + getCurrentDateFromFile());

        button_yes.setBackgroundColor(getColor(R.color.colorGreen));
        button_no.setBackgroundColor(getColor(R.color.colorRed));

        dialog.show();

        button_yes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();

                progressBar.setVisibility(View.VISIBLE);

                // Проверка на подключение к интернету
                if (tryInternetConnection() == true)
                    formProducts(true);
                else {
                    Toast.makeText(SplashScreenActivity.this, getString(R.string.problem_internet_connection), Toast.LENGTH_LONG).show();

                    formProducts(false);
                }
            }
        });

        button_no.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();

                progressBar.setVisibility(View.VISIBLE);

                formProducts(false);
            }
        });
    }

    // Метод, котрый формирует SplashScreen в зависимости от типа продукта
    public void formSplashScreen() {
        setContentView(R.layout.activity_splash_screen);

        progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(View.INVISIBLE);

        textview_name_of_product = findViewById(R.id.texview_typeOfProduct);
        picture_image_of_product = findViewById(R.id.picture_splash_screen);

        // Изменение картинки и текста в зависимости от типа продукта
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
        Bundle arguments = getIntent().getExtras();

        return arguments.getString("TypeOfProduct");
    }

    // Метод формулирования продуктов и отправки их в AboutProductsActivity
    public void formProducts(final boolean flag) {
        // По истечении времени, запускаем AboutProductsActivity, а SplashScreenActivity закрываем
       new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /*
                    TRUE - Переход к парсингу сайтов
                    FALSE - Считывание информации с файов
                 */
                if (flag == true)
                {
                    getInfoAboutNovus();
                    if (typeOfProduct.equals("Bread") == false)
                        getInfoAboutMegaMarket();
                    getInfoAboutFozzy();

                    if (internetConection == false)
                        Toast.makeText(SplashScreenActivity.this, getString(R.string.problem_internet_connection), Toast.LENGTH_LONG).show();

                }
                else {
                    readFromFile("filenovus" + typeOfProduct.toLowerCase() + ".txt", false);
                    readFromFile("filemegamarket" + typeOfProduct.toLowerCase() + ".txt", false);
                    readFromFile("filefozzy" + typeOfProduct.toLowerCase() + ".txt", false);

                    // Если еще данные не обновлялись
                    if (products_novus.size() == 0)
                        readFromFile("filenovus" + typeOfProduct.toLowerCase() + ".txt", true);
                    if ((products_megamarket.size() == 0) && (typeOfProduct.equals("Bread") == false))
                        readFromFile("filemegamarket" + typeOfProduct.toLowerCase() + ".txt", true);
                    if (products_fozzy.size() == 0)
                        readFromFile("filefozzy" + typeOfProduct.toLowerCase() + ".txt", true);
                }

                // Передача данных о продуктах в AboutProductsActivity
                sendInfoToActivity();

            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    // Метод для передачи данных о продуктах в другую активность
    public void sendInfoToActivity() {
        Intent intent = new Intent(SplashScreenActivity.this, AboutProductsActivity.class);
        Bundle args = new Bundle();

        args.putSerializable("ArrayListNovus", (Serializable) products_novus);
        args.putSerializable("ArrayListMegaMarket", (Serializable) products_megamarket);
        args.putSerializable("ArrayListFozzy", (Serializable) products_fozzy);

        args.putString("TypeOfProduct", typeOfProduct);

        intent.putExtra("Bundle",args);

        startActivity(intent);
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
        html1 = "span.jsx-2300924169.product-tile__title";

        // html для цены продукта
        html2 = "span.jsx-2618301845.Price__value_caption";

        try {
            // Количество просматриваемых страниц сайта
            int n = 2;

            // url сайта
            String url[] = new String [n];

            // Формулировка url с продуктами
            url[0] = "https://novus.zakaz.ua/uk/categories/" + html_sign + "/?sort=price_asc";
            for (int i = 1; i < n; i++)
            {
                url[i] = url[0] + "&page=" + (i+1);
            }

            Document doc[] = new Document[1000];
            Elements formElements1[] = new Elements[1000];
            Elements formElements2[] = new Elements[1000];

            StringBuilder strBuffer = new StringBuilder();
            strBuffer.append(getCurrentDate() + '\n');

            // Получение информации о продуктах
            for (int i = 0; i < n; i++)
            {
                doc[i] = Jsoup.connect(url[i]).get();
                formElements1[i] = doc[i].select(html1);
                formElements2[i] = doc[i].select(html2);

                for (int j = 0; j < formElements1[i].size(); j++) {
                    products_novus.add(new Product());
                    products_novus.get(products_novus.size() - 1).formPrice_of_product(formElements2[i].get(j).text());

                    int price = products_novus.get(products_novus.size() - 1).getPrice_of_product();

                    products_novus.get(products_novus.size() - 1).setName_of_product(formElements1[i].get(j).text() + " - " + Integer.toString(price / 100) + "," + Integer.toString(price - (price / 100) * 100) + " грн " + "(Novus)");

                    strBuffer.append(products_novus.get(products_novus.size() - 1).getName_of_product() + '\n');

                    Log.d("###", products_novus.get(products_novus.size() - 1).getName_of_product());
                }
            }

            writeToFile("filenovus" + typeOfProduct.toLowerCase() + ".txt", strBuffer);
        }
        catch (Exception e) {
            Log.d("###", "Error (Parsing) : " + e.getMessage());
            products_novus = new ArrayList<Product>();

            // Если не удалось распарсить сайт
            readFromFile("filenovus" + typeOfProduct.toLowerCase() + ".txt", false);

            if (products_novus.size() == 0)
                readFromFile("filenovus" + typeOfProduct.toLowerCase() + ".txt", true);
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
            int n = 2;

            if (typeOfProduct.equals("Eggs"))
                n = 1;

            String url[] = new String[n];

            StringBuilder strBuffer = new StringBuilder();
            strBuffer.append(getCurrentDate() + '\n');

            // Проверка на тип продукта (Не хлеб)
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
                        products_megamarket.get(products_megamarket.size() - 1).formPrice_of_product(formElements2[i].get(j).text());

                        int price = products_megamarket.get(products_megamarket.size() - 1).getPrice_of_product();

                        products_megamarket.get(products_megamarket.size() - 1).setName_of_product(formElements1[i].get(j).text() + " - " + Integer.toString(price / 100) + "," + Integer.toString(price - (price / 100) * 100) + " грн" +  " (MegaMarket)");

                        strBuffer.append(products_megamarket.get(products_megamarket.size() - 1).getName_of_product() + '\n');

                        Log.d("###", products_megamarket.get(products_megamarket.size() - 1).getName_of_product());
                }
                }
            }
            if (typeOfProduct.equals("Bread") == false)
                writeToFile("filemegamarket" + typeOfProduct.toLowerCase() + ".txt", strBuffer);
        }
        catch (IOException e) {
            products_megamarket = new ArrayList<Product>();

            readFromFile("filemegamarket" + typeOfProduct.toLowerCase() + ".txt", false);

            if ((products_megamarket.size() == 0) && (typeOfProduct.equals("Bread") == false))
                readFromFile("filemegamarket" + typeOfProduct.toLowerCase() + ".txt", true);
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

                    url[0] = "https://fozzyshop.ua/ru/300200-moloko?order=product.price.asc";
                    break;
                case "Bread" :
                    url[0] = "https://fozzyshop.ua/ru/300505-khleb?order=product.price.asc";
                    break;
                case "Eggs" :
                    url[0] = "https://fozzyshop.ua/ru/300212-yajca-kurinye/s-15/kategoriya-yajca_kurinye?order=product.price.asc";
                    break;
            }

            for (int i = 1; i < n; i++) {
                url[i] = url[0] + "&page=" + Integer.toString(i + 1);
            }

            Document doc[] = new Document[1000];
            Elements formElements1[] = new Elements[1000];
            Elements formElements2[] = new Elements[1000];

            StringBuilder strBuffer = new StringBuilder();
            strBuffer.append(getCurrentDate() + '\n');

            for (int i = 0; i < n; i++) {

                doc[i] = Jsoup.connect(url[i]).get();
                formElements1[i] = doc[i].select(html1);
                formElements2[i] = doc[i].select(html2);

                for (int j = 0; j < formElements2[i].size(); j++) {
                    products_fozzy.add(new Product());
                    products_fozzy.get(products_fozzy.size() - 1).formPrice_of_product(formElements2[i].get(j).text());

                    int price = products_fozzy.get(products_fozzy.size() - 1).getPrice_of_product();

                    products_fozzy.get(products_fozzy.size() - 1).setName_of_product(formElements1[i].get(j).text() + " - " + Integer.toString(price / 100) + "," + Integer.toString(price - (price / 100) * 100) + " грн" + " (Fozzy)");

                    strBuffer.append(products_fozzy.get(products_fozzy.size() - 1).getName_of_product() + '\n');

                    Log.d("###", products_fozzy.get(products_fozzy.size() - 1).getName_of_product());
                }
            }

            writeToFile("filefozzy" + typeOfProduct.toLowerCase() + ".txt", strBuffer);
        }
        catch (IOException e) {
            internetConection = false;
            products_fozzy = new ArrayList<Product>();

            readFromFile("filefozzy" + typeOfProduct.toLowerCase() + ".txt", false);

            if (products_fozzy.size() == 0)
                readFromFile("filefozzy" + typeOfProduct.toLowerCase() + ".txt", true);
        }
    }

    // Метод для получение настоящей даты (День и месяц)
    public String getCurrentDate() {
        // Текущее время
        Date currentDate = new Date();
        // Форматирование времени как "день.месяц"
        DateFormat dateFormat = new SimpleDateFormat("dd.MM", Locale.getDefault());
        String dateText = dateFormat.format(currentDate);

        return  dateText;
    }

    // Метод получение даты из файла
    public String getCurrentDateFromFile() {
        String dateText = null;

        try {
            BufferedReader buffer = null;
            buffer = new BufferedReader(new InputStreamReader(openFileInput("filenovus" + typeOfProduct.toLowerCase() + ".txt")));

            String mLine;

            boolean firstTime = false;

            while (((mLine = buffer.readLine()) != null) && (firstTime == false)) {
                dateText = mLine;

                firstTime = true;
            }
        }
        catch (FileNotFoundException e) {
            dateText = getString(R.string.dialog_inline_version);
        }
        catch (IOException e) {
        }

        return dateText;
    }

    // Метод записи
    public void writeToFile(String fileName, StringBuilder result) {
        FileOutputStream fileOutput;
        try {
            fileOutput = openFileOutput(fileName, MODE_PRIVATE);
            fileOutput.write(result.toString().getBytes());
            fileOutput.close();
        }
        catch (IOException e) {
        }
    }

    // Метод для чтения информации о продуктах из файла при отсутсвие интернета
   public void readFromFile(String fileName, boolean isFileFromAssets) {

        StringBuilder strBufferNovus, strBufferMegaMarket, strBufferFozzy;

        strBufferNovus = new StringBuilder();
        strBufferMegaMarket = new StringBuilder();
        strBufferFozzy = new StringBuilder();

        if (isFileFromAssets == true) {
            strBufferNovus.append(getCurrentDateFromFile() + '\n');
            strBufferMegaMarket.append(getCurrentDateFromFile() + '\n');
            strBufferFozzy.append(getCurrentDateFromFile() + '\n');
        }

        try{
            BufferedReader buffer = null;
            if (isFileFromAssets == true) {
                buffer = new BufferedReader(new InputStreamReader(getAssets().open(fileName)));
            }
            else
                buffer = new BufferedReader(new InputStreamReader(openFileInput(fileName)));

            String mLine;

            boolean firstTime = false;

            while ((mLine = buffer.readLine()) != null) {
                if (firstTime == true) {
                    // Выбор к какой сети супермаркетов относить прочитаный продукт из файла
                    if (fileName.lastIndexOf("novus") != -1) {
                        products_novus.add(new Product());
                        products_novus.get(products_novus.size() - 1).setName_of_product(mLine);
                        products_novus.get(products_novus.size() - 1).formPrice_of_product(mLine);

                        strBufferNovus.append(mLine + '\n');
                    }

                    if (fileName.lastIndexOf("megamarket") != -1) {
                        products_megamarket.add(new Product());
                        products_megamarket.get(products_megamarket.size() - 1).setName_of_product(mLine);
                        products_megamarket.get(products_megamarket.size() - 1).formPrice_of_product(mLine);

                        strBufferMegaMarket.append(mLine + '\n');
                    }

                    if (fileName.lastIndexOf("fozzy") != -1) {
                        products_fozzy.add(new Product());
                        products_fozzy.get(products_fozzy.size() - 1).setName_of_product(mLine);
                        products_fozzy.get(products_fozzy.size() - 1).formPrice_of_product(mLine);

                        strBufferFozzy.append(mLine + '\n');
                    }
                }

                firstTime = true;
            }

            // Если файл из Assets
            if (isFileFromAssets == true) {
                if (fileName.lastIndexOf("novus") != -1)
                    writeToFile("filenovus" + typeOfProduct.toLowerCase() + ".txt", new StringBuilder(strBufferNovus.toString()));
                if ((typeOfProduct.equals("Bread") == false) && (fileName.lastIndexOf("megamarket") != -1))
                    writeToFile("filemegamarket" + typeOfProduct.toLowerCase() + ".txt", new StringBuilder(strBufferMegaMarket.toString()));
                if (fileName.lastIndexOf("fozzy") != -1)
                    writeToFile("filefozzy" + typeOfProduct.toLowerCase() + ".txt", new StringBuilder(strBufferFozzy.toString()));
            }
        }
        catch (IOException e) {
        }
    }

    // Метод для проверки соединение с интернетом
    public boolean tryInternetConnection() {
        String cs = Context.CONNECTIVITY_SERVICE;
        ConnectivityManager cm = (ConnectivityManager) getSystemService(cs);

        if (cm.getActiveNetworkInfo() == null)
            return false;
        else
            return true;
    }

}