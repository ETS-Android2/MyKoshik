package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
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

// Activity для выбора продуктов из различных супермаркетов
public class AboutProductsActivity extends AppCompatActivity {
    private LinearLayout linearLayout;

    // Надпись о типе продукта и надпись о количестве продуктов в корзине
    private TextView textview_type_of_product, textview_count_of_products;

    // Поисковая стока
    private EditText plaintext_search_line;

    // Кнопки супермаркетов и кнопка "Далее"
    private Button button_of_novus, button_of_megamarket, button_of_fozzy, button_of_continue;

    // Кнопка корзины с продуктами из списка
    private ImageButton button_of_bag;

    // Границы ID надписей и кнопок продуктов
    private int x, y;

    // Тип продукта
    String type_of_product;

    // Флаги цвета кнопок при нажатии и флаг проверки наличия вывода продуктов
    private boolean f1, f2, f3, flag;

    // ArrayList продуктов из различных супермаркетов и ArrayList продуктов которые изменяються в ходе поиска в поисковой строке
    public ArrayList<Product> products_novus, products_megamarket, products_fozzy, products_novus_megamarket, products_megamarket_fozzy, products_novus_fozzy, products_novus_megamarket_fozzy, products_for_searchbar = new ArrayList<Product>();

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
        button_of_bag = findViewById(R.id.picture_basket);

        button_of_novus.setBackgroundColor(getColor(R.color.colorBlue));
        button_of_megamarket.setBackgroundColor(getColor(R.color.colorBlue));
        button_of_fozzy.setBackgroundColor(getColor(R.color.colorBlue));
        button_of_continue.setBackgroundColor(getColor(R.color.colorGreen));

        textview_count_of_products = findViewById(R.id.textview_count_of_products);

        linearLayout = findViewById(R.id.linearlayout);

        x = 1;
        y = 1;

        products_novus_fozzy = new ArrayList<Product>();
        products_novus_megamarket = new ArrayList<Product>();
        products_megamarket_fozzy = new ArrayList<Product>();
        products_novus_megamarket_fozzy = new ArrayList<Product>();

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

        // Переход в ListOfProductsActivity при нажатии на кнопку корзины продуктов
        button_of_bag.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent mainIntent = new Intent(AboutProductsActivity.this, ListOfProductsActivity.class);
                startActivity(mainIntent);
            }
        });

        products_for_searchbar = new ArrayList<Product>();

        // Добавление обьекта TextWatcher для реагирование на изменения поисковой строки
        plaintext_search_line.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            // Если текст в поисковой строке изменился
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               if (products_for_searchbar.size() != 0) {
                    deleteListOfProducts();

                    viv(products_for_searchbar, plaintext_search_line.getText().toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    // Жизненный цикл для того, чтобы при переходе из ListOfProductsActivity обратно в AboutProductsActivity не сбивались значение количества продуктов
    @Override
    protected void onResume() {
        super.onResume();

        formCountOfProductsInList();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent intent = new Intent(AboutProductsActivity.this, MainActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    // Метод для получения информации из SplashScreenActivity
    public void getInfoFromIntent() {
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");

        type_of_product = args.getString("TypeOfProduct");

        products_novus = (ArrayList<Product>) args.getSerializable("Name1");
        products_megamarket = (ArrayList<Product>) args.getSerializable("Name2");
        products_fozzy = (ArrayList<Product>) args.getSerializable("Name3");
    }

    // Метод для подсчета количества продуктов в файле
    public void formCountOfProductsInList() {
        // Количество продуктов в списке
        int count = 0;

        try {
            FileInputStream fileInput = openFileInput("list_of_products.txt");
            InputStreamReader reader = new InputStreamReader(fileInput);
            BufferedReader buffer = new BufferedReader(reader);
            String mLine;

            // Считывание продуктов из файла со списком и подсчет количества повторений кажлого продукта
            while ((mLine = buffer.readLine()) != null) {
                Product product_example = new Product();

                product_example.formCount_of_product(mLine);
                count = count + product_example.getCount_of_product();
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
        // Изменение цвета кнопок при нажатии на них
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


        // Вывод информации в зависимости от выбраных кнопок
        if (view.getId() == button_of_continue.getId())
        {
            deleteListOfProducts();

            if ((f1 == true) && (f2 == false) && (f3 == false))
            {
                // Novus

                viv(products_novus, plaintext_search_line.getText().toString());
            }

            else
            if ((f2 == true) && (f1 == false) && (f3 == false))
            {
                // MegaMarket

                // Проверка на существование продукта данного типа
                if (type_of_product.equals("Bread"))
                {
                    TextView t = new TextView(getApplicationContext());
                    t.setText('\n' + getString(R.string.problem_not_avaible_product));
                    t.setTextSize(18);
                    t.setId(y);
                    y++;
                    linearLayout.addView(t);
                }
                else
                    viv(products_megamarket, plaintext_search_line.getText().toString());
            }

            else
            if ((f3 == true) && (f1 == false) && (f2 == false))
            {
                // Fozzy

                viv(products_fozzy, plaintext_search_line.getText().toString());
            }

            else
            if ((f1 == true) && (f2 == true) && (f3 == false))
            {
                // Novus + MegaMarket

                if (products_novus_megamarket.size() == 0) {
                    products_novus_megamarket = getInfoAboutTwoShops(products_novus, products_megamarket);
                }

                viv(products_novus_megamarket, plaintext_search_line.getText().toString());
            }

            else
            if ((f1 == true) && (f3 == true) && (f2 == false))
            {
                // Novus + Fozzy

                if (products_novus_fozzy.size() == 0) {
                    products_novus_fozzy = getInfoAboutTwoShops(products_novus, products_fozzy);
                }

                viv(products_novus_fozzy, plaintext_search_line.getText().toString());
            }

            else
            if ((f2 == true) && (f3 == true) && (f1 == false))
            {
                // MegaMarket + Fozzy

                if (products_megamarket_fozzy.size() == 0) {
                    products_megamarket_fozzy = getInfoAboutTwoShops(products_megamarket, products_fozzy);
                }

                viv(products_megamarket_fozzy, plaintext_search_line.getText().toString());
            }

            else
            if ((f1 == true) && (f2 == true) && (f3 == true))
            {
                // Novus + MegaMarket + Fozzy

                if (products_novus_megamarket_fozzy.size() == 0) {
                        if (products_megamarket_fozzy.size() == 0) {
                            products_megamarket_fozzy = getInfoAboutTwoShops(products_megamarket, products_fozzy);
                        }

                        products_novus_megamarket_fozzy = getInfoAboutTwoShops(products_megamarket_fozzy, products_novus);

                        viv(products_novus_megamarket_fozzy, plaintext_search_line.getText().toString());
                }
            }

            else {
                // Если не нажато ни одну кнопку

                TextView t = new TextView(getApplicationContext());
                t.setText(getString(R.string.problem_choose_button));
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

    // Метод формулирования АrrayList<Product> с информацией о 2 магазинах, который возвращает тоже ArrayList с информацией о продуктах из 2 магазинов
    public ArrayList<Product> getInfoAboutTwoShops(ArrayList<Product> s1, ArrayList<Product> s2) {
        int k1 = s1.size();
        int k2 = s2.size();

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

        // Сортировка "Бульбашкой"
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

    // Метод, который регулирует вывод продуктов
    public void viv(ArrayList<Product> products, String string_from_searchbar) {
        flag = false;

        for (int i = 0; i < products.size(); i++)
            addButtonAndTextView(products.get(i).getName_of_product(), string_from_searchbar);

        products_for_searchbar = new ArrayList<Product>();

        for (int i = 0; i < products.size(); i++) {
            products_for_searchbar.add(new Product());
            products_for_searchbar.get(i).setName_of_product(products.get(i).getName_of_product());
        }

        // Если не нашлось совпадений со строкой поиска
        if (flag == false) {
            TextView t = new TextView(getApplicationContext());
            t.setText('\n' + getString(R.string.problem_searchbar_not_found)+ '\n');
            t.setTextSize(18);
            t.setId(y);
            y++;
            linearLayout.addView(t);
        }
    }

    // Метод для удаления кнопок и надписей, чтобы не было повтора ID при создание новых
    public void deleteListOfProducts() {
        for (int i = x; i < y; i++)
        {
            View b = findViewById(i);
            b.setVisibility(View.GONE);
        }

        // "Jбнуление" границ ID
        x = y;
    }

    // Метод для создание разных кнопок и надписей для каждого продукта
    public void addButtonAndTextView(final String product, String string_from_searchbar) {
        // Продукт, который нужно добавить
        String s1 = product.toLowerCase();

        // Строка из строки поиска
        String s2 = string_from_searchbar.toLowerCase();

        if (s1.lastIndexOf(s2) != -1) {
            flag = true;

            int number = (y - x) / 2 + 1;

            TextView t = new TextView(getApplicationContext());
            t.setText('\n' + Integer.toString(number) + ") " + product + '\n');
            t.setTextSize(18);
            t.setId(y);
            y++;
            linearLayout.addView(t);

            Button b = new Button(getApplicationContext());
            b.setText(getString(R.string.button_buy_product) + Integer.toString(number));
            b.setId(y);
            y++;
            b.setBackgroundColor(getColor(R.color.colorOrange));
            linearLayout.addView(b);

            // При нажатии на кнопку "Купить продукт"
            b.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Добавление продукта в файл со списком продутов
                    write(product);

                    // Изменение количества продуктов, который будет выводиться над корзиной с продуктами
                    formCountOfProductsInList();
                }
            });
        }
    }

    // Метод для записи определенного продукта в файл списка продуктов
    public void write(String product) {

        try {
            StringBuffer strBuffer = new StringBuffer();
            String stroka;
            try {
                FileInputStream fileInput = openFileInput("list_of_products.txt");
                InputStreamReader reader = new InputStreamReader(fileInput);
                BufferedReader buffer = new BufferedReader(reader);
                String lines;

                boolean f = false;

                while ((lines = buffer.readLine()) != null) {
                   if (lines.lastIndexOf(product) != -1)
                   {
                       f = true;

                       // Если продукт имеет повторения в списке
                       if (lines.lastIndexOf("X") > lines.lastIndexOf(")"))
                       {
                           String s = lines.substring(lines.lastIndexOf("X") + 1, lines.length());
                           int count = Integer.parseInt(s) + 1;

                           strBuffer.append(product + " X" + Integer.toString(count) + '\n');
                       }
                       else
                           strBuffer.append(lines + " X2" + '\n');
                   }
                    else
                        strBuffer.append(lines + '\n');
                }

                // Если совпадений с продуктом добавления не было
                if (f == false)
                    stroka = product + '\n' + strBuffer.toString();
                else
                    stroka = strBuffer.toString();
            }
            catch (IOException e)
            {
                // Если файл до этого не был создан
                stroka = product + '\n';
            }

            // Помещать данных в файл
            FileOutputStream fileOutput = openFileOutput("list_of_products.txt", MODE_PRIVATE);
            fileOutput.write(stroka.getBytes());
            fileOutput.close();
        }
        catch (FileNotFoundException e )
        {
        }
        catch (IOException e )
        {
        }

        Toast.makeText(AboutProductsActivity.this, getString(R.string.toast_added_product), Toast.LENGTH_LONG).show();
    }

}