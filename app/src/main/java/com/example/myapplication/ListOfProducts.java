package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

public class ListOfProducts extends AppCompatActivity implements AdapterView.OnItemSelectedListener  {

    // TextView ("Продукты" / "Список продуктов пуст")
    private TextView tnumber_of_products, tsum_of_product_prices;

    private LinearLayout linearLayout;

    // Файл для чтения продуктов
    private FileOutputStream fileOutput;

    private ImageButton button_of_clear;

    // Границы ID Buttons и TextViews продуктов
    private int x,y;

    // Количество продуктов в списке
    private int n, button_znak;

    // Массив продуктов
    private StringBuffer[] name_of_product;

    int price_of_product[];

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_products);

        x = 1;
        y = 1;

        n = 0;

        name_of_product = new StringBuffer[1000];

        price_of_product = new int[1000];

        tnumber_of_products = findViewById(R.id.tnumber_of_products);
        tsum_of_product_prices = findViewById(R.id.tsum_of_product_prices);

        linearLayout = findViewById(R.id.linearlayout2);

        button_of_clear = findViewById(R.id.button_of_clear);

        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.types_of_sort, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        button_of_clear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Обнуление списка продуктов
                try {

                    fileOutput = openFileOutput("list_of_products.txt", MODE_PRIVATE);
                    fileOutput.write("".getBytes());
                    fileOutput.close();
                    Toast.makeText(ListOfProducts.this, getString(R.string.cleared_list), Toast.LENGTH_LONG).show();
                    tnumber_of_products.setText(getString(R.string.empty_list));
                    tsum_of_product_prices.setText("");
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


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        deleteListOfProducts(false);
        switch (parent.getItemAtPosition(position).toString()) {
            case "За чергою продуктів":
                getInfoAboutProductsWithZnak("List");
                break;
            case "За цінами продуктів ↓":
                getInfoAboutProductsWithZnak("SortUp");
                break;
            case "За цінами продуктів ↑":
                getInfoAboutProductsWithZnak("SortDown");
                break;
            case "За супермаркетами":
                getInfoAboutProductsWithZnak("Supermarkets");
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    // Вспомогательтный метод, который вызывается при нажатии кнопки очистки и удалении одного продукта
    public void pr1() {
        deleteListOfProducts(false);
        switch (button_znak) {
            case 0:
                writeProductsToList(name_of_product);
                break;
            case 1:
                sort("SortUp");
                break;
            case 2:
                sort("SortDown");
                break;
            case 3:
                sort("Supermarkets");
                break;
        }
        button_znak = (button_znak + 1) % 4;
    }

    // Метод для чтения продуктов
    public void readInfoFromFile() {
        try {
            FileInputStream fileInput = openFileInput("list_of_products.txt");
            InputStreamReader reader = new InputStreamReader(fileInput);
            BufferedReader buffer = new BufferedReader(reader);
            StringBuffer strBuffer = new StringBuffer();
            String lines;
            n = 0;

            while ((lines = buffer.readLine()) != null) {
                name_of_product[n] = new StringBuffer(lines);
                price_of_product[n] = formPriceOfProduct(name_of_product[n].toString());
                n++;
            }

            if (n == 0) {
                // Если продуктоету
                tnumber_of_products.setText(getString(R.string.empty_list));
                tsum_of_product_prices.setText("");
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
                writeProductsToList(name_of_product);
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
            product_s[i] = new StringBuffer(name_of_product[i]);
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
        if (n == 0)
        {
            tnumber_of_products.setText(getString(R.string.empty_list));
            tsum_of_product_prices.setText("");
        }
        else
        {
            tnumber_of_products.setText("Кількість продуктів = " + Integer.toString(n));
            tsum_of_product_prices.setText("Ціна кошика = " + Integer.toString(sum() / 100) + "." + Integer.toString(sum() % 100) + " грн.");
        }

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

            FileInputStream fileInput = openFileInput("list_of_products.txt");
            InputStreamReader reader = new InputStreamReader(fileInput);
            BufferedReader buffer = new BufferedReader(reader);
            String lines;
            n--;
            boolean f = false;
            while ((lines = buffer.readLine()) != null) {
                // Проверка на равенство продукта который надо удалить и продукта из файла
                if ((lines.equals(product) == false) || ((f == true) && (lines.equals(product) == true))) {
                    strBuffer.append(lines + '\n');
                }
                else
                    f = true;
            }

            tnumber_of_products.setText("Кількість продуктів у кошику = " + Integer.toString(n));
            tsum_of_product_prices.setText("Ціна кошика = " + Integer.toString(sum() / 100) + "." + Integer.toString(sum() % 100) + " грн.");

            if (n == 0)
            {
                tnumber_of_products.setText(getString(R.string.empty_list));
                tsum_of_product_prices.setText("");
            }
            // Запись нового файла без удаленного продукта
            fileOutput = openFileOutput("list_of_products.txt", MODE_PRIVATE);
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
            tnumber_of_products.setText(getString(R.string.empty_list));
            tsum_of_product_prices.setText("");
        }

        // Делаем типо "обнуление" количества Butons и TextViews
        x = y;
    }

    // Метод для создание Button и TextView каждого продукта
    public void addButtonAndTextView(final String product, int number) {
        TextView t = new TextView(getApplicationContext());
        t.setText('\n' + Integer.toString(number) + ") " + product + '\n');
        t.setTextSize(20);
        t.setId(y);
        y++;
        linearLayout.addView(t);

        final Button b = new Button(getApplicationContext());
        b.setText(getString(R.string.delete_product) + " №" + Integer.toString(number));
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
                Toast.makeText(ListOfProducts.this, getString(R.string.deleted_product), Toast.LENGTH_LONG).show();
            }
        });

    }
}
