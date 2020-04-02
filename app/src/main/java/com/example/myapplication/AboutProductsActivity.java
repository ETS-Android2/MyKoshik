package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

// Activity для выбора продуктов
public class AboutProductsActivity extends AppCompatActivity {
    private LinearLayout linearLayout;
    // Надпись с типом продукта и надпись о количестве продуктов в корзине
    private TextView textview_type_of_product, textview_count_of_products;
    // Поисковая стока
    private EditText plaintext_search_line;
    // Кнопки супермаркетов и кнопка "Далее"
    private Button button_of_novus, button_of_megamarket, button_of_fozzy, button_of_continue;
    // Кнопка корзины
    private ImageButton button_of_bag;

    // Границы ID
    private int x, y;
    // Количество продуктов в различных комбинациях выводов
    private int count_novus, count_megamarket, count_fozzy, count_novus_megamarket, count_megamarket_fozzy, count_novus_fozzy, count_novus_megamarket_fozzy;

    // Тип продукта
    String type_of_product;

    // Флаги цвета кнопок и флаг проверки вывода
    private boolean f1, f2, f3, flag;

    // ArrayList продуктов из различных супермаркетов
    public ArrayList<Product> products_novus, products_megamarket, products_fozzy, products_novus_megamarket, products_megamarket_fozzy, products_novus_fozzy, products_novus_megamarket_fozzy = new ArrayList<Product>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about_products);

        textview_type_of_product = findViewById(R.id.textview_type_of_product);

        plaintext_search_line = findViewById(R.id.plaintext_search_line);

        button_of_novus = findViewById(R.id.button_of_novus);
        button_of_megamarket = findViewById(R.id.button_of_megamarket);
        button_of_fozzy = findViewById(R.id.button_of_fozzy);
        button_of_continue = findViewById(R.id.button_of_continue);

        button_of_novus.setBackgroundColor(getColor(R.color.colorBlue));
        button_of_megamarket.setBackgroundColor(getColor(R.color.colorBlue));
        button_of_fozzy.setBackgroundColor(getColor(R.color.colorBlue));
        button_of_continue.setBackgroundColor(getColor(R.color.colorGreen));
        button_of_bag = findViewById(R.id.picture_basket);

        textview_count_of_products = findViewById(R.id.textview_count_of_products);

        linearLayout = findViewById(R.id.LinearLayout);

        x = 1;
        y = 1;

        count_novus = 0;
        count_megamarket = 0;
        count_fozzy = 0;

        count_novus_megamarket = 0;
        count_megamarket_fozzy = 0;
        count_novus_fozzy = 0;

        count_novus_megamarket_fozzy = 0;

        f1 = false;
        f2 = false;
        f3 = false;

        getInfoFromIntent();

        switch (type_of_product) {
            case "Milk" :
                textview_type_of_product.setText(getString(R.string.text_milk));
                break;
            case "Bread" :
                textview_type_of_product.setText(getString(R.string.text_bread));
                break;
            case "Eggs" :
                textview_type_of_product.setText(getString(R.string.text_eggs));
                break;
        }

        button_of_bag.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Переход в ListOfProductsActivity
                Intent mainIntent = new Intent(AboutProductsActivity.this, ListOfProductsActivity.class);
                startActivity(mainIntent);
            }
        });
    }

    // Жизненный цикл для того, чтобы при переходе из ListOfProductsActivity обратно в AboutProductsActivity не сбивались значения количества продуктов
    @Override
    protected void onResume() {
        super.onResume();

        formCountOfProductsInList();
    }

    // Метод для получения информации из SplashScreenActivity
    public void getInfoFromIntent() {
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");

        type_of_product = args.getString("TypeOfProduct");

        products_novus = (ArrayList<Product>) args.getSerializable("Name1");
        if (type_of_product.equals("Bread") == false)
            products_megamarket = (ArrayList<Product>) args.getSerializable("Name2");
        products_fozzy = (ArrayList<Product>) args.getSerializable("Name3");

        count_novus = products_novus.size();
        if (type_of_product.equals("Bread") == false)
            count_megamarket = products_megamarket.size();
        count_fozzy = products_fozzy.size();
    }

    // Метод для подсчитывания количества продуктов в файле
    public void formCountOfProductsInList() {
        int count = 0;

        try {
            FileInputStream fileInput = openFileInput("list_of_products.txt");
            InputStreamReader reader = new InputStreamReader(fileInput);
            BufferedReader buffer = new BufferedReader(reader);
            String mLine;

            while ((mLine = buffer.readLine()) != null) {
                count++;
            }
        }
        catch (IOException e) {
        }
        finally
        {
            if (count > 99)
                textview_count_of_products.setText("99+");
            else
                textview_count_of_products.setText(Integer.toString(count));
        }
    }

    // Метод обработки нажатия кнопок с супермаркетами
    public void onClickButtonShop(View view) {

        if (view.getId() == button_of_novus.getId()) {
            if (f1 == false) {
                button_of_novus.setBackgroundColor(getColor(R.color.colorPurple));
            }
            else
            {
                button_of_novus.setBackgroundColor(getColor(R.color.colorBlue));
            }
            f1 = !f1;
        }
        if (view.getId() == button_of_megamarket.getId()) {
            if (f2 == false) {
                button_of_megamarket.setBackgroundColor(getColor(R.color.colorPurple));
            }
            else
            {
                button_of_megamarket.setBackgroundColor(getColor(R.color.colorBlue));
            }
            f2 = !f2;
        }
        if (view.getId() == button_of_fozzy.getId()) {
            if (f3 == false) {
                button_of_fozzy.setBackgroundColor(getColor(R.color.colorPurple));
            }
            else
            {
                button_of_fozzy.setBackgroundColor(getColor(R.color.colorBlue));
            }
            f3 = !f3;
        }

        if (view.getId() == button_of_continue.getId())
        {
            deleteListOfProducts();

            if ((f1 == true) && (f2 == false) && (f3 == false))
            {
                viv(products_novus, count_novus);
            }
            else
            if ((f2 == true) && (f1 == false) && (f3 == false))
            {
                if (type_of_product.equals("Bread"))
                {
                    TextView t = new TextView(getApplicationContext());
                    t.setText('\n' + getString(R.string.text_not_avaible_product));
                    t.setTextSize(18);
                    t.setId(y);
                    y++;
                    linearLayout.addView(t);
                }
                else
                    viv(products_megamarket, count_megamarket);
            }
            else
            if ((f3 == true) && (f1 == false) && (f2 == false))
            {
                viv(products_fozzy, count_fozzy);
            }
            else
            if ((f1 == true) && (f2 == true) && (f3 == false))
            {
                if (count_novus_megamarket == 0) {
                    products_novus_megamarket = getInfoAboutTwoShops(products_novus, products_megamarket, count_novus, count_megamarket);
                    count_novus_megamarket = count_novus + count_megamarket;
                }

                viv(products_novus_megamarket, count_novus_megamarket);
            }
            else
            if ((f1 == true) && (f3 == true) && (f2 == false))
            {
                if (count_novus_fozzy == 0) {
                    products_novus_fozzy = getInfoAboutTwoShops(products_novus, products_fozzy, count_novus, count_fozzy);
                    count_novus_fozzy = count_novus + count_fozzy;
                }

                viv(products_novus_fozzy, count_novus_fozzy);
            }
            else
            if ((f2 == true) && (f3 == true) && (f1 == false))
            {
                if (count_megamarket_fozzy == 0) {
                    products_megamarket_fozzy = getInfoAboutTwoShops(products_megamarket, products_fozzy, count_megamarket, count_fozzy);
                    count_megamarket_fozzy = count_megamarket + count_fozzy;
                }

                viv(products_megamarket_fozzy, count_megamarket_fozzy);
            }
            else
            if ((f1 == true) && (f2 == true) && (f3 == true))
            {
                if (count_novus_megamarket_fozzy == 0) {
                    if (count_megamarket_fozzy == 0) {
                        products_megamarket_fozzy = getInfoAboutTwoShops(products_megamarket, products_fozzy, count_megamarket, count_fozzy);
                        count_megamarket_fozzy = count_megamarket + count_fozzy;
                    }

                    products_novus_megamarket_fozzy = getInfoAboutTwoShops(products_megamarket_fozzy, products_novus, count_megamarket_fozzy, count_novus);
                    count_novus_megamarket_fozzy = count_megamarket_fozzy + count_novus;
                }

                viv(products_novus_megamarket_fozzy, count_novus_megamarket_fozzy);

            }
            else {
                TextView t = new TextView(getApplicationContext());
                t.setText('\n' + "Оберіть одну кнопку аби продовжити");
                t.setTextSize(18);
                t.setId(y);
                y++;
                linearLayout.addView(t);
            }

            f1 = false;
            f2 = false;
            f3 = false;

            button_of_novus.setBackgroundColor(getColor(R.color.colorBlue));
            button_of_megamarket.setBackgroundColor(getColor(R.color.colorBlue));
            button_of_fozzy.setBackgroundColor(getColor(R.color.colorBlue));
        }

    }

    // Метод формулирования массива ArrayList<Product> с информацией о 2 магазинах
    public ArrayList<Product> getInfoAboutTwoShops(ArrayList<Product> s1, ArrayList<Product> s2, int k1, int k2) {
        ArrayList<Product> s3 = new ArrayList<Product>();

        for (int i = 0; i < k1; i++) {
            s3.add(new Product());
            s3.get(i).setName_of_product(s1.get(i).getName_of_product());
            s3.get(i).setPrice_of_product(s1.get(i).getPrice_of_product());
        }

        for (int i = 0; i < k2; i++) {
            s3.add(new Product());
            s3.get(i+k1).setName_of_product(s2.get(i).getName_of_product());
            s3.get(i+k1).setPrice_of_product(s2.get(i).getPrice_of_product());
        }

        for (int i = 1; i < k1 + k2; i++)
            for (int j = i; j >= 1; j--) {
                if (s3.get(j).getPrice_of_product() < s3.get(j-1).getPrice_of_product()) {
                    int price;
                    String name;

                    price = s3.get(j).getPrice_of_product();
                    s3.get(j).setPrice_of_product(s3.get(j-1).getPrice_of_product());
                    s3.get(j-1).setPrice_of_product(price);

                    name = s3.get(j).getName_of_product();
                    s3.get(j).setName_of_product(s3.get(j-1).getName_of_product());
                    s3.get(j-1).setName_of_product(name);
                }
            }

        return s3;
    }

    // Метод, который "выводит" продукты
    public void viv(ArrayList<Product> products, int count) {
        flag = false;
        for (int i = 0; i<count; i++)
            addButtonAndTextView(products.get(i).getName_of_product(), i+1);

        // Если не было совпадений со строкой поиска
        if (flag == false) {
            TextView t = new TextView(getApplicationContext());
            t.setText('\n' + "Даний продукт не знайдено");
            t.setTextSize(18);
            t.setId(y);
            y++;
            linearLayout.addView(t);
        }

    }

    // Метод для удаления Buttons и TextViews, чтобы не было повтора ID при создание новых Butons и TextViews
    public void deleteListOfProducts() {
        for (int i = x; i < y; i++)
        {
            View b = findViewById(i);
            b.setVisibility(View.GONE);
        }

        // Делаем типо "обнуление" количества Butons и TextViews
        x = y;
    }

    // Метод для создание разных Button и TextView для каждого продукта
    public void addButtonAndTextView(final String product, int number) {
        String s1 = product.toLowerCase();
        String s2 = plaintext_search_line.getText().toString().toLowerCase();

        if (s1.lastIndexOf(s2) != -1) {
            flag = true;
            TextView t = new TextView(getApplicationContext());
            t.setText('\n' + Integer.toString((y - x) / 2 + 1) + ") " + product + '\n');
            t.setTextSize(18);
            t.setId(y);
            y++;
            linearLayout.addView(t);

            Button b = new Button(getApplicationContext());
            b.setText(getString(R.string.button_buy_product) + Integer.toString((y - 1 - x) / 2 + 1));
            b.setId(y);
            y++;
            linearLayout.addView(b);
            b.setBackgroundColor(getColor(R.color.colorOrange));
            //b.setBackground(getDrawable(R.drawable.rounded_button));

            b.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    write(product);
                    formCountOfProductsInList();
                }
            });
        }

    }

    // Метод для записи определенного продукта в файл списка продуктов
    public void write(String shop) {

        try {
            StringBuffer strBuffer = new StringBuffer();
            String stroka;
            try {
                FileInputStream fileInput = openFileInput("list_of_products.txt");
                InputStreamReader reader = new InputStreamReader(fileInput);
                BufferedReader buffer = new BufferedReader(reader);
                String lines;

                while ((lines = buffer.readLine()) != null) {
                    strBuffer.append(lines + '\n');
                }

                stroka = shop + '\n' + strBuffer.toString();
            }
            catch (IOException e)
            {
                stroka = shop + '\n';
            }

            // Класс, который помогает помещать данные в файл
            FileOutputStream fileOutput = openFileOutput("list_of_products.txt", MODE_PRIVATE);
            fileOutput.write(stroka.getBytes());
            fileOutput.close();

            Toast.makeText(AboutProductsActivity.this, "Продукт был добавлен в список покупок", Toast.LENGTH_LONG).show();
        }
        catch (FileNotFoundException e )
        {
        }
        catch (IOException e )
        {
        }

    }
}