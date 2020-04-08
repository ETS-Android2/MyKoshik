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

// Начальная MainActivity
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button button_of_list_of_products;
    private ImageButton button_of_question;
    private TextView button_close_dialog;

    private Dialog dialog;

    private SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        checkFirstRun();

        // Создание диалогового окна с ссылкой на автора
        dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_window);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        button_of_list_of_products = findViewById(R.id.button_of_list_of_products);
        button_of_question = findViewById(R.id.button_of_question);
        button_close_dialog = dialog.findViewById(R.id.button_close_dialog);

        button_of_list_of_products.setOnClickListener(this);
        button_of_question.setOnClickListener(this);
        button_close_dialog.setOnClickListener(this);
    }

    // Метод для нажатия на Button
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_close_dialog :
                // Закрытие диалогового окна
                dialog.dismiss();
                break;

            case R.id.button_of_list_of_products :
                // Переход в ListOfProductsActivity
                Intent intent = new Intent(MainActivity.this, ListOfProductsActivity.class);
                startActivity(intent);
                break;

            case R.id.button_of_question :
                // Показ диалогового окна
                dialog.show();
                break;
        }
    }

    // Метод для формулировки меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Преобразование данных из ресурсов меню в пункты меню на экране
        getMenuInflater().inflate(R.menu.main_menu,menu);

        return true;
    }

    // Метод для выбора одно из типа продуктов в меню
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Идентификатор выбранного пункта меню
        int id = item.getItemId();

        Intent mainIntent;

        // Операции для выбранного пункта меню (Переход к Activity AboutMilk)
        switch (id) {
            case R.id.action_milk:
                mainIntent = new Intent(MainActivity.this, SplashScreenActivity.class);
                mainIntent.putExtra("TypeOfProduct", "Milk");
                startActivity(mainIntent);
                return true;
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