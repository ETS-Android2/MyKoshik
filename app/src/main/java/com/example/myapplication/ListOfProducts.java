package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class ListOfProducts extends AppCompatActivity implements AdapterView.OnItemSelectedListener  {
    // TextView ("Продукты" / "Список продуктов пуст")
    private TextView result;

    private LinearLayout linearLayout;

    // Файл для чтения продуктов
    private FileOutputStream fileOutput;

    private Button button_of_clear;

    // Границы ID Buttons и TextViews продуктов
    private int x,y;

    // Количество продуктов в списке
    private int n, button_znak;

    // Массив продуктов
    private StringBuffer[] product;

    int price_of_product[];

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_products);

        x = 1;
        y = 1;

        n = 0;

        product = new StringBuffer[1000];

        price_of_product = new int[1000];

        result = findViewById(R.id.result2);

        linearLayout = findViewById(R.id.linearlayout2);

        button_of_clear = findViewById(R.id.button_of_clear);
        button_of_clear.setBackgroundColor(getColor(R.color.colorRed));

        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.types_of_sort, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        button_of_clear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Обнуление списка продуктов
                try {

                    fileOutput = openFileOutput("example.txt", MODE_PRIVATE);
                    fileOutput.write("".getBytes());
                    fileOutput.close();
                    Toast.makeText(ListOfProducts.this, "Список продуктов был очищен", Toast.LENGTH_LONG).show();
                    result.setText("Список продуктов пуст");
                } catch (FileNotFoundException e) {
                } catch (IOException e) {
                }

                deleteListOfProducts(true);
            }
        });

        button_znak =  1;

        readInfoFromFile();
        getInfoAboutProductsWithZnak("List");

    }

    // Вспомогательтный метод, который вызывается при нажатии кнопки очистки и удалении одного продукта
    public void pr1() {
        deleteListOfProducts(false);
        switch (button_znak) {
            case 0:
                getInfoAboutProductsWithZnak("List");
                break;
            case 1:
                getInfoAboutProductsWithZnak("SortUp");
                break;
            case 2:
                getInfoAboutProductsWithZnak("SortDown");
                break;
            case 3:
                getInfoAboutProductsWithZnak("Supermarkets");
                break;
        }
        button_znak = (button_znak + 1) % 4;
    }

    // Метод для чтения продуктов
    public void readInfoFromFile() {
        try {
            Log.d("###2","YES");
            FileInputStream fileInput = openFileInput("example.txt");
            InputStreamReader reader = new InputStreamReader(fileInput);
            BufferedReader buffer = new BufferedReader(reader);
            StringBuffer strBuffer = new StringBuffer();
            String lines;
            Log.d("###2","YES");
            n = 0;

            while ((lines = buffer.readLine()) != null) {
                Log.d("###2","Line = "+lines);
                product[n] = new StringBuffer(lines);
                price_of_product[n] = formPriceOfProduct(product[n].toString());
                Log.d("###2",product[n].toString());
                n++;
            }

            if (n == 0) {
                // Если продуктоету
                result.setText("Список продуктов пуст");
            }
        }
        catch (FileNotFoundException e) {
        }
        catch (IOException e) {
        }
    }

    // Метод для формулировки цены продукта
    public int formPriceOfProduct(String product) {
        int pos1, pos2;

        pos1 = product.lastIndexOf("грн");
        pos2 = 0;
        while (!product.substring(pos1-4-pos2,pos1-3-pos2).equals(" ")) {
            pos2++;
        }

        return Integer.parseInt(product.substring(pos1-3-pos2,pos1-4) + product.substring(pos1-3,pos1-1));
    }

    //Метод для создания Button и TextView в зависимости от знака
    public void getInfoAboutProductsWithZnak(String znak) {
        switch (znak) {
            case "List" :
                writeProductsToList(product);
                break;
            case "SortUp" :
                sort("SortUp");
                break;
            case "SortDown" :
                sort("SortDown");
                break;
            case "Supermarkets" :
                sort("Supermarkets");
                break;
        }
    }

    // Метод для сортировки массива продуктов
    public void sort(String typeOfSort) {
        StringBuffer product_s[] = new StringBuffer[n];
        int price_of_product_s[] = new int[n];

        for (int i = 0; i < n; i++)
        {
            product_s[i] = new StringBuffer(product[i]);
            price_of_product_s[i] = price_of_product[i];
        }

        StringBuffer s = new StringBuffer("");

        if ((typeOfSort.equals("SortUp") == true) || (typeOfSort.equals("Supermarkets"))) {
            boolean changes;
            do {
                changes = false;
                for (int i = 0; i < n - 1; i++) {
                    if (price_of_product_s[i] > price_of_product_s[i + 1]) {
                        int _current = price_of_product_s[i];
                        price_of_product_s[i] = price_of_product_s[i + 1];
                        price_of_product_s[i + 1] = _current;
                        changes = true;

                        s.setLength(0);
                        s.append(product_s[i].toString());
                        product_s[i].setLength(0);
                        product_s[i].append(product_s[i + 1].toString());
                        product_s[i + 1].setLength(0);
                        product_s[i + 1].append(s.toString());
                    }
                }

            }
            while (changes);

            StringBuffer product_d[] = new StringBuffer[n];
            if (typeOfSort.equals("Supermarkets"))
            {
                int k = 0;
                for (int i = 0; i < n; i++)
                {
                    if (product_s[i].toString().indexOf("Novus") != -1) {
                        product_d[k] = new StringBuffer(product_s[i].toString());
                        k++;
                    }
                }
                for (int i = 0; i < n; i++)
                {
                    if (product_s[i].toString().indexOf("MegaMarket") != -1) {
                        product_d[k] = new StringBuffer(product_s[i].toString());
                        k++;
                    }
                }
                for (int i = 0; i < n; i++)
                {
                    if (product_s[i].toString().indexOf("Fozzy") != -1) {
                        product_d[k] = new StringBuffer(product_s[i].toString());
                        k++;
                    }
                }
                writeProductsToList(product_d);
            }
            else
                writeProductsToList(product_s);

        }
        else {
            boolean changes;
            do {
                changes = false;
                for (int i = 0; i < n - 1; i++) {
                    if (price_of_product_s[i] < price_of_product_s[i + 1]) {
                        int _current = price_of_product_s[i];
                        price_of_product_s[i] = price_of_product_s[i + 1];
                        price_of_product_s[i + 1] = _current;
                        changes = true;

                        s.setLength(0);
                        s.append(product_s[i].toString());
                        product_s[i].setLength(0);
                        product_s[i].append(product_s[i + 1].toString());
                        product_s[i + 1].setLength(0);
                        product_s[i + 1].append(s.toString());
                    }
                }

            }
            while (changes);
            writeProductsToList(product_s);
        }
    }

    // Метод для создания Button и TextView для каждого продукта
    public void writeProductsToList(StringBuffer s[]) {
        result.setText("Продукты (Кол. = " + Integer.toString(n) + ", Цена = " + Integer.toString(sum() / 100) + "." + Integer.toString(sum() % 100) + ") :");
        for (int i = 0; i < n; i++)
            addButtonAndTextView(s[i].toString(), i+1);
    }

    public int sum() {
        int s = 0;
        for (int i = 0; i < n; i++)
            s = s + price_of_product[i];
        return s;
    }
    // Метод для удаления продукта из файла
    public void deleteListOfProducts(String product) {
        try {
            StringBuffer strBuffer = new StringBuffer();

            FileInputStream fileInput = openFileInput("example.txt");
            InputStreamReader reader = new InputStreamReader(fileInput);
            BufferedReader buffer = new BufferedReader(reader);
            String lines;
            n--;
            boolean f = false;
            while ((lines = buffer.readLine()) != null) {
                // Проверка на равенство продукта который надо удалить и продукта из файла
                Log.d("###dg",Boolean.toString(lines.equals(product)));
                if ((lines.equals(product) == false) || ((f == true) && (lines.equals(product) == true))) {
                    Log.d("gggg",lines);
                    strBuffer.append(lines + '\n');
                }
                else
                    f = true;
            }

            result.setText("Продукты (Кол. = " + Integer.toString(n) + ") :");

            if (n == 0)
                result.setText("Список продуктов пуст");
            Log.d("######",strBuffer.toString());
            // Запись нового файла без удаленного продукта
            fileOutput = openFileOutput("example.txt", MODE_PRIVATE);
            fileOutput.write(strBuffer.toString().getBytes());
            fileOutput.close();
        }
        catch (FileNotFoundException e) {
        }
        catch (IOException e) {
        }
    }

    // Метод для удаления всех Buttons и TextViews при обнулении списка
    public void deleteListOfProducts(boolean f) {
        for (int i = x; i < y; i++)
        {
            View b = findViewById(i);
            b.setVisibility(View.GONE);
        }

        if (f == true) {
            n = 0;
            result.setText("Список продуктов пуст");
        }

        // Делаем типо "обнуление" количества Butons и TextViews
        x = y;
    }

    // Метод для создание Button и TextView каждого продукта
    public void addButtonAndTextView(final String product, int number) {
        TextView t = new TextView(getApplicationContext());
        t.setText('\n' + Integer.toString(number) + ") " + product + '\n');
        t.setId(y);
        y++;
        linearLayout.addView(t);

        final Button b = new Button(getApplicationContext());
        b.setText("Удалить продукт" + " №" + Integer.toString(number));
        b.setId(y);
        y++;
        linearLayout.addView(b);
        b.setBackgroundColor(getColor(R.color.colorOrange));

        // При нажатии на кнопку "Удалить продукт" будет удаляться продукт из файла и по-новому распределение номеров продуктов
        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                deleteListOfProducts(product);
                deleteListOfProducts(false);
                readInfoFromFile();
                pr1();
                Toast.makeText(ListOfProducts.this, "Продукт был удален из списка", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        deleteListOfProducts(false);
        switch (parent.getItemAtPosition(position).toString()) {
            case "По очереди продуктов":
                getInfoAboutProductsWithZnak("List");
                break;
            case "По цене продуктов ↓":
                getInfoAboutProductsWithZnak("SortUp");
                break;
            case "По цене продуктов ↑":
                getInfoAboutProductsWithZnak("SortDown");
                break;
            case "По супермаркетам":
                getInfoAboutProductsWithZnak("Supermarkets");
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
