package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class ListOfProducts extends AppCompatActivity {

    private TextView result;
    private LinearLayout linearLayout;

    private FileOutputStream fileOutput;

    private Button button_of_clear;

    private int x,y;

    private int n;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_products);

        result = findViewById(R.id.result2);

        linearLayout = findViewById(R.id.linearlayout2);

        button_of_clear = findViewById(R.id.button_of_clear);
        button_of_clear.setBackgroundColor(getColor(R.color.colorRed));

        button_of_clear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    fileOutput = openFileOutput("example.txt", MODE_PRIVATE);
                    fileOutput.write("".getBytes());
                    fileOutput.close();
                    Toast.makeText(ListOfProducts.this, "Список продуктов был очищен", Toast.LENGTH_LONG).show();
                    result.setText("Список продуктов пуст");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                deleteListOfProducts();
            }
        });

        x = 1;
        y = 1;

        n = 0;

        result.setText("Продукты :");

        read();
    }
    public void read() {
        try {
            FileInputStream fileInput = openFileInput("example.txt");
            InputStreamReader reader = new InputStreamReader(fileInput);
            BufferedReader buffer = new BufferedReader(reader);
            StringBuffer strBuffer = new StringBuffer();
            String lines;

            n = 0;

            while ((lines = buffer.readLine()) != null) {
                n++;
                addButtonAndTextView(lines);
            }
            if (n == 0)
                result.setText("Список продуктов пуст");
        }
        catch (FileNotFoundException e) {
            result.setText("Список продуктов пуст");
        }
        catch (IOException e) {
            result.setText("Список продуктов пуст");

        }
    }

    // метод для удаления Buttons и TextViews, чтобы не было повтора ID при создание новых Butons и TextViews
    public void deleteListOfProducts(int id, String product) {
        View b = findViewById(id);
        b.setVisibility(View.GONE);

        View t = findViewById(id-1);
        t.setVisibility(View.GONE);

        try {
            StringBuffer strBuffer1 = new StringBuffer();
            StringBuffer strBuffer2 = new StringBuffer();

            FileInputStream fileInput = openFileInput("example.txt");
            InputStreamReader reader = new InputStreamReader(fileInput);
            BufferedReader buffer = new BufferedReader(reader);
            String lines;
            n--;
            while ((lines = buffer.readLine()) != null) {
                if (product.lastIndexOf(lines) == -1) {
                    strBuffer1.append(lines + '\n');
                }
                strBuffer2.append(lines + '\n');
            }

            if (n == 0)
                result.setText("Список продуктов пуст");

            fileOutput = openFileOutput("example.txt", MODE_PRIVATE);
            fileOutput.write(strBuffer1.toString().getBytes());
            fileOutput.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // метод для создание Button и TextView для каждого продукта
    public void addButtonAndTextView(final String product) {
        TextView t = new TextView(getApplicationContext());
        t.setText('\n' + product + '\n');
        t.setId(y);
        y++;
        linearLayout.addView(t);

        final Button b = new Button(getApplicationContext());
        b.setText("Удалить продукт");
        b.setId(y);
        y++;
        linearLayout.addView(b);
        b.setBackgroundColor(getColor(R.color.colorRed));

        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // допили пропуск
                deleteListOfProducts(b.getId(), product);
                Toast.makeText(ListOfProducts.this, "Продукт был удален из списка", Toast.LENGTH_LONG).show();
            }
        });

    }

    // метод для удаления Buttons и TextViews, чтобы не было повтора ID при создание новых Butons и TextViews
    public void deleteListOfProducts() {
        for (int i = x; i < y; i++)
        {
            View b = findViewById(i);
            b.setVisibility(View.GONE);
            n--;
        }

        if (n == 0)
            result.setText("Список продуктов пуст");

        // делаем типо "обнуление" количества Butons и TextViews
        x = y;
    }
}
