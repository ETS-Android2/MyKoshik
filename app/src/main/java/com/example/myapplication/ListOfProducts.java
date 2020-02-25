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
    // TextView ("Продукты" / "Список продуктов пуст")
    private TextView result;

    private LinearLayout linearLayout;

    // Файл для чтения продуктов
    private FileOutputStream fileOutput;

    private Button button_of_clear, button_of_sort;

    // Границы ID Buttons и TextViews продуктов
    private int x,y;

    // Количество продуктов в списке
    private int n, button_znak;

    // Массив продуктов
    private StringBuffer[] product;

    int price_of_product[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        button_of_sort = findViewById(R.id.button_of_sort);
        button_znak =  1;

        readInfoFromFile();
        getInfoAboutProductsWithZnak("List");

        button_of_sort.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                deleteListOfProducts(false);
                switch (button_znak) {
                    case 0 :
                        button_of_sort.setText("Сортировка по очереди");
                        getInfoAboutProductsWithZnak("List");
                        break;
                    case 1 :
                        button_of_sort.setText("Сортировка по возрастанию");
                        getInfoAboutProductsWithZnak("SortUp");
                        break;
                    case 2:
                        button_of_sort.setText("Сортировка по убыванию");
                        getInfoAboutProductsWithZnak("SortDown");
                        break;
                }
                button_znak = (button_znak + 1) % 3;
            }
        });

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
        }
    }

    // Метод для сортировки массива продуктов по ценам
    public void sort(String typeOfSort) {
        StringBuffer product_s[] = new StringBuffer[1000];
        int price_of_product_s[] = new int[1000];

        for (int i = 0; i < n; i++)
        {
            product_s[i] = new StringBuffer(product[i]);
            price_of_product_s[i] = price_of_product[i];
        }

        StringBuffer s = new StringBuffer("");

        if (typeOfSort.equals("SortUp") == true) {
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
        }
        writeProductsToList(product_s);
    }

    // Метод для создания Button и TextView для каждого продукта
    public void writeProductsToList(StringBuffer s[]) {
        result.setText("Продукты (Кол. = " + Integer.toString(n) + ") :");
        for (int i = 0; i < n; i++)
            addButtonAndTextView(s[i].toString(), i+1);
    }

    // Метод для удаления Buttons и TextViews, чтобы не было повтора ID при создание новых
    public void deleteListOfProducts(int id, String product) {
        View b = findViewById(id);
        b.setVisibility(View.GONE);

        View t = findViewById(id - 1);
        t.setVisibility(View.GONE);

        try {
            StringBuffer strBuffer1 = new StringBuffer();
            StringBuffer strBuffer2 = new StringBuffer();

            FileInputStream fileInput = openFileInput("example.txt");
            InputStreamReader reader = new InputStreamReader(fileInput);
            BufferedReader buffer = new BufferedReader(reader);
            String lines;
            n--;
            boolean f = false;
            while ((lines = buffer.readLine()) != null) {
                // Проверка на равенство продукта который надо удалить и продукта из файла
                Log.d("###dg",lines);
                Log.d("###dg",product);
                Log.d("###dg",Boolean.toString(lines.equals(product)));
                if ((lines.equals(product) == false) || ((f == true) && (lines.equals(product) == true))) {
                    Log.d("gggg",lines);
                    strBuffer1.append(lines + '\n');
                }
                else
                    f = true;
            }

            result.setText("Продукты (Кол. = " + Integer.toString(n) + ") :");

            if (n == 0)
                result.setText("Список продуктов пуст");
            Log.d("######",strBuffer1.toString());
            // Запись нового файла без удаленного продукта
            fileOutput = openFileOutput("example.txt", MODE_PRIVATE);
            fileOutput.write(strBuffer1.toString().getBytes());
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
        b.setBackgroundColor(getColor(R.color.colorRed));

        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                deleteListOfProducts(b.getId(), product);
                readInfoFromFile();
                Toast.makeText(ListOfProducts.this, "Продукт был удален из списка", Toast.LENGTH_LONG).show();
            }
        });

    }
}
