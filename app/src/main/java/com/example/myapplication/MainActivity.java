package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    private Button button_of_purchase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_of_purchase = findViewById(R.id.button_of_purchase);

        button_of_purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListOfProducts.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // преобразование данных из ресурсов меню в пункты меню на экране
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // получим идентификатор выбранного пункта меню
        int id = item.getItemId();
        Intent mainIntent;
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
        // операции для выбранного пункта меню
    }
}