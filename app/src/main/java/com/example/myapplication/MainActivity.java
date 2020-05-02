package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import me.toptas.fancyshowcase.FancyShowCaseQueue;
import me.toptas.fancyshowcase.FancyShowCaseView;
import me.toptas.fancyshowcase.FocusShape;

// Начальная MainActivity
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // Кнопка списка продуктов и кнопка открытия Spinner с типами продуктов
    private Button button_of_list_of_products, button_choose_product;

    // Кнопка с информацией и ссылкой на сайт автора
    private ImageButton button_open_dialog;

    // Кнопка закрытия диалогового окна
    private TextView button_close_dialog;

    // Диалоговое окно
    private Dialog dialog;

    // ArrayList типов продуктов
    private ArrayList<String> items = new ArrayList<String>();

    // Spinner из библиотеки
    private SpinnerDialog spinnerDialog;

    private SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Создание диалогового окна с ссылкой на автора
        dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_window_site);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        button_of_list_of_products = findViewById(R.id.button_of_list_of_products);
        button_choose_product = findViewById(R.id.button_choose_product);
        button_open_dialog = findViewById(R.id.button_open_dialog);
        button_close_dialog = dialog.findViewById(R.id.button_close_dialog);

        button_of_list_of_products.setOnClickListener(this);
        button_choose_product.setOnClickListener(this);
        button_open_dialog.setOnClickListener(this);
        button_close_dialog.setOnClickListener(this);

        initItems();

        spinnerDialog = new SpinnerDialog(this, items, "Оберіть один продукт :", "Закрити");

        // Создание Spinner из библиотеки с выбором продуктов
        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                Intent mainIntent;

                // Переадресаия в SplashScreenActivity в зависимости от выбраного типа продуктов
                switch (items.get(position)) {
                    case "Молоко" :
                        mainIntent = new Intent(MainActivity.this, SplashScreenActivity.class);
                        mainIntent.putExtra("TypeOfProduct", "Milk");
                        startActivity(mainIntent);
                        break;
                    case "Яйця" :
                        mainIntent = new Intent(MainActivity.this, SplashScreenActivity.class);
                        mainIntent.putExtra("TypeOfProduct", "Eggs");
                        startActivity(mainIntent);
                        break;
                    case "Хліб" :
                        mainIntent = new Intent(MainActivity.this, SplashScreenActivity.class);
                        mainIntent.putExtra("TypeOfProduct", "Bread");
                        startActivity(mainIntent);
                        break;
                }
            }
        });

        /*
            В зависимости от номера вхождения в MainActivity:
             1 - Переход в AppIntroAcitvity
             2 - Показ инструкции
         */

        switch (checkFirstRun()) {
            case 1 :
                Intent intent = new Intent(MainActivity.this, AppIntroActivity.class);
                startActivity(intent);
                break;
            case 2:
                makeInstruction();
                break;
        }
    }

    // Метод, который вызываеться при нажатии на обьект
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_open_dialog:
                dialog.show();
                break;
            case R.id.button_close_dialog :
                dialog.dismiss();
                break;
            case R.id.button_of_list_of_products :
                Intent intent = new Intent(MainActivity.this, ListOfProductsActivity.class);
                startActivity(intent);
                break;
            case R.id.button_choose_product:
                spinnerDialog.showSpinerDialog();
                break;
        }
    }

    // Метод для инициализирования предмето в Spinner
    public void initItems() {
        for (int i = 0; i < getResources().getStringArray(R.array.spinner_types_of_product).length; i++)
            items.add(getResources().getStringArray(R.array.spinner_types_of_product)[i]);
    }

    // Метод для проверки первого запуска приложения
    public int checkFirstRun() {
        prefs = getSharedPreferences("com.mycompany.myAppName", MODE_PRIVATE);

        int entrance;

        // В зависимости от номера вхождения в MainActivity возващаеться значение
        if (prefs.getBoolean("firstrun", true))
        {
            entrance = 1;

            writeToDateFile("FirstEntrance");

            prefs.edit().putBoolean("firstrun", false).commit();
        }
        else
        {
            String entry = readFromDateFile();

            if (entry.equals("FirstEntrance") == true)
            {
                writeToDateFile("SecondEntrance");

                entrance = 2;
            }
            else
                entrance = 3; // И больше
        }

        return entrance;
    }

    // Метод записи номера вхождения в MainActivity в файл
    public void writeToDateFile(String entry) {
        try {
            FileOutputStream fileOutput = openFileOutput("file_entrance.txt", MODE_PRIVATE);
            fileOutput.write(entry.getBytes());
            fileOutput.close();
        }
        catch (FileNotFoundException e) {
        }
        catch (IOException e) {
        }
    }

    // Метод считывания номера вхождения в MainActivity с файла
    public String readFromDateFile() {
        String entry = null;

        try {
            FileInputStream fileInput = openFileInput("file_entrance.txt");
            InputStreamReader reader = new InputStreamReader(fileInput);
            BufferedReader buffer = new BufferedReader(reader);
            String mLine;

            while ((mLine = buffer.readLine()) != null) {
                entry = mLine;
            }
        }
        catch (FileNotFoundException e) {
        }
        catch (IOException e) {
        }
        finally {
            return entry;
        }
    }

    // Инструкция о пользовании с помощью FancyShowCaseView
    public void makeInstruction() {
        final FancyShowCaseView fancyShowCaseView1 = new FancyShowCaseView.Builder(MainActivity.this)
                .title(getString(R.string.instruction_text1))
                .enableAutoTextPosition()
                .focusOn(button_choose_product)
                .focusShape(FocusShape.ROUNDED_RECTANGLE)
                .build();

        final FancyShowCaseView fancyShowCaseView2 = new FancyShowCaseView.Builder(MainActivity.this)
                .title(getString(R.string.instruction_text2))
                .enableAutoTextPosition()
                .focusOn(button_of_list_of_products)
                .focusShape(FocusShape.ROUNDED_RECTANGLE)
                .build();

        final FancyShowCaseView fancyShowCaseView3 = new FancyShowCaseView.Builder(MainActivity.this)
                .title(getString(R.string.instruction_text3))
                .enableAutoTextPosition()
                .focusOn(button_open_dialog)
                .build();

        FancyShowCaseQueue mQueue = new FancyShowCaseQueue()
                .add(fancyShowCaseView1)
                .add(fancyShowCaseView2)
                .add(fancyShowCaseView3);

        mQueue.show();
    }
}