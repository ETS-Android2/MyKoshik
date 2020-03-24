package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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

public class AboutProduct extends AppCompatActivity {

    private TextView result, typeOfProduct, count_of_products;
    private Button button_of_novus, button_of_megamarket, button_of_fozzy, button_of_continue;
    private LinearLayout linearLayout;
    private ImageButton button_of_bag;

    private ImageView photo;

    private int x, y, count_of_products_in_list;

    private int count_novus, count_megamarket, count_fozzy, count_novus_megamarket, count_megamarket_fozzy, count_novus_fozzy, count_novus_megamarket_fozzy;

    public ArrayList<Product> products_novus, products_megamarket, products_fozzy, products_novus_megamarket, products_megamarket_fozzy, products_novus_fozzy, products_novus_megamarket_fozzy = new ArrayList<Product>();

    String z, type;

    private boolean f1, f2, f3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_product);

        typeOfProduct = findViewById(R.id.textview_name_of_product);

        button_of_novus = findViewById(R.id.button_of_novus);
        button_of_megamarket = findViewById(R.id.button_of_megamarket);
        button_of_fozzy = findViewById(R.id.button_of_fozzy);
        button_of_continue = findViewById(R.id.button_of_continue);

        button_of_novus.setBackgroundColor(getColor(R.color.colorBlue));
        button_of_megamarket.setBackgroundColor(getColor(R.color.colorBlue));
        button_of_fozzy.setBackgroundColor(getColor(R.color.colorBlue));
        button_of_continue.setBackgroundColor(getColor(R.color.colorGreen));
        button_of_bag = findViewById(R.id.imageButton2);

        result = findViewById(R.id.list_of_products_of_shop);

        count_of_products = findViewById(R.id.textView2);

        linearLayout = findViewById(R.id.linearlayout1);

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

        typeOfProduct.setText(getString(R.string.action_milk));

        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");

        products_novus = (ArrayList<Product>) args.getSerializable("Name1");
        products_megamarket = (ArrayList<Product>) args.getSerializable("Name2");
        products_fozzy = (ArrayList<Product>) args.getSerializable("Name3");

        count_novus = args.getInt("Numbers1");
        count_megamarket = args.getInt("Numbers2");
        count_fozzy = args.getInt("Numbers3");

        type = args.getString("TypeOfProduct");

        switch (type) {
            case "Milk" :
                typeOfProduct.setText(getString(R.string.action_milk));
                break;
            case "Bread" :
                typeOfProduct.setText(getString(R.string.action_bread));
                break;
            case "Eggs" :
                typeOfProduct.setText(getString(R.string.action_eggs));
                break;
        }

        button_of_bag.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Переход в ListOfProducts
                Intent mainIntent = new Intent(AboutProduct.this, ListOfProducts .class);
                startActivity(mainIntent);
            }
        });
    }

    // Жизненный цикл для того, чтобы при переходе из ListOfProducts обратно в AboutProduct не сбивались значения количества продуктов
    @Override
    protected void onResume() {
        super.onResume();

        countProduct();
    }

    // Метод для подсчитывания количества продуктов в файле
    public void countProduct() {
        int c = 0;

        try {
            FileInputStream fileInput = openFileInput("list_of_products.txt");
            InputStreamReader reader = new InputStreamReader(fileInput);
            BufferedReader buffer = new BufferedReader(reader);
            String lines;

            while ((lines = buffer.readLine()) != null) {
                c++;
            }
        }
        catch (IOException e) {
        }
        finally
        {
            if (c > 99)
                count_of_products.setText("99+");
            else
                count_of_products.setText(Integer.toString(c));
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
            result.setText("");

            if ((f1 == true) && (f2 == false) && (f3 == false))
            {
                viv(products_novus, count_novus);
            }
            else
            if ((f2 == true) && (f1 == false) && (f3 == false))
            {
                if (type.equals("Bread"))
                    result.setText(getString(R.string.not_avaible_product));
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
            else
                result.setText("Выберите одну кнопку чтобы продолжить");

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

        for (int i = 0; i < k1; i++)
            s3.add(s1.get(i));

        for (int i = 0; i < k2; i++)
            s3.add(s2.get(i));

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
        for (int i = 0; i<count; i++)
            addButtonAndTextView(products.get(i).getName_of_product(), i+1);

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
        TextView t = new TextView(getApplicationContext());
        t.setText('\n' + Integer.toString(number) + ") " + product + '\n');
        t.setTextSize(18);
        t.setId(y);
        y++;
        linearLayout.addView(t);

        Button b = new Button(getApplicationContext());
        b.setText(getString(R.string.buy_product_with_number) + Integer.toString(number));
        b.setId(y);
        y++;
        linearLayout.addView(b);
        b.setBackgroundColor(getColor(R.color.colorOrange));
        //b.setBackground(getDrawable(R.drawable.rounded_button));

        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                write(product);
                countProduct();
            }
        });

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

            Toast.makeText(AboutProduct.this, "Продукт был добавлен в список покупок", Toast.LENGTH_LONG).show();
        }
        catch (FileNotFoundException e )
        {
        }
        catch (IOException e )
        {
        }

    }
}