package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.AttributedCharacterIterator;
import java.util.ArrayList;
import java.util.jar.Attributes;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import me.toptas.fancyshowcase.FancyShowCaseView;

// Начальная MainActivity
public class MainActivity extends AppCompatActivity {

    // Кнопка списка продуктов
    private Button button_of_list_of_products, button_of_shops;

    // Кнопка с информацией и ссылкой на сайт автора
    private ImageButton button_of_question;

    // Кнопка закрытия диалогового окна
    private TextView button_close_dialog;

    // Диалоговое окно
    private Dialog dialog;

    private ArrayList<String> items = new ArrayList<String>();

    private SpinnerDialog spinnerDialog;

    private SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_ACTION_BAR);

        setContentView(R.layout.activity_main);

        checkFirstRun();

        // Создание диалогового окна с ссылкой на автора
        dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_window_site);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        button_of_list_of_products = findViewById(R.id.button_of_list_of_products);
        button_of_shops = findViewById(R.id.button_chose_product);
        button_of_question = findViewById(R.id.button_of_question);
        button_close_dialog = dialog.findViewById(R.id.button_close_dialog);

        button_of_list_of_products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListOfProductsActivity.class);
                startActivity(intent);
            }
        });
        button_of_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
        button_close_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        initItems();

        spinnerDialog = new SpinnerDialog(this, items, "Оберіть одн продукт :");
        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                Intent mainIntent;
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

        button_of_shops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerDialog.showSpinerDialog();
            }
        });
    }

    public void initItems() {
        for (int i = 0; i < getResources().getStringArray(R.array.spinner_types_of_product).length; i++)
            items.add(getResources().getStringArray(R.array.spinner_types_of_product)[i]);
    }

    // Метод для проверки первого запуска приложения
    public void checkFirstRun() {
        prefs = getSharedPreferences("com.mycompany.myAppName", MODE_PRIVATE);

        if (prefs.getBoolean("firstrun", true))
        {
            Intent intent = new Intent(MainActivity.this, AppIntroActivity.class);
            startActivity(intent);

            prefs.edit().putBoolean("firstrun", false).commit();
        }
    }
}