package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

// Начальная Activity
public class MainActivity extends AppCompatActivity {

    private Button button_of_purchase;

    private ImageButton button_of_question;

    private TextView button_close;

    private SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_of_purchase = findViewById(R.id.button_of_list_of_products);

        button_of_purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Переход в ListOfProducts при нажатии на кнопку
                Intent intent = new Intent(MainActivity.this, ListOfProducts.class);
                startActivity(intent);
            }
        });

        checkFirstRun();

        button_of_question = findViewById(R.id.button_of_question);

        button_of_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Показ диалогового окна с ссылкой на сайт при нажатии на знак вопроса ( + Инструкция )
                final Dialog dialog;
                dialog = new Dialog(MainActivity.this);

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.activity_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCancelable(false);

                button_close = dialog.findViewById(R.id.button_close);

                button_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
    }

    // Метод для формулировки меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Преобразование данных из ресурсов меню в пункты меню на экране
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;

    }

    // Метод при выборе одно из типа продуктов в меню
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Получаем идентификатор выбранного пункта меню
        int id = item.getItemId();

        Intent mainIntent;

        // Операции для выбранного пункта меню
        switch (id) {
            case R.id.action_milk:
                mainIntent = new Intent(MainActivity.this, SplashScreenActivity.class);
                mainIntent.putExtra("TypeOfProduct", "Milk");
                startActivity(mainIntent);
                return true;
            // переход к Activity AboutMilk
            case R.id.action_eggs:
                mainIntent = new Intent(MainActivity.this, SplashScreenActivity.class);
                mainIntent.putExtra("TypeOfProduct", "Eggs");
                startActivity(mainIntent);
                return true;
            case R.id.action_bread:
                mainIntent = new Intent(MainActivity.this, SplashScreenActivity.class);
                mainIntent.putExtra("TypeOfProduct", "Bread");
                startActivity(mainIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Метод проверки первого запуска приложения и перехода в ApIntroActivity
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