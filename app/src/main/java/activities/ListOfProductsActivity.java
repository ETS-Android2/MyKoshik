package activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

import com.example.myapplication.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import classes.Product;

// Activity со списком продуктов
public class ListOfProductsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener  {
    // Надписи о количестве продуктов и надпись о общей стоимости всех прокупок
    private TextView textview_number_of_products, textview_sum_of_product_prices;

    // Кнопка "Корзина"
    private ImageButton button_of_clear;

    private LinearLayout linearLayout;

    private FloatingActionButton mFABShareList;

    // Файл для чтения продуктов
    private FileOutputStream fileOutput;

    // Границы ID кнопок и надписей продуктов + Тип сортировки
    private int x, y, sort_sign;

    // ArrayList с информацией о продуктах в списке
    public ArrayList<Product> products = new ArrayList<Product>();

    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list_of_products);

        textview_number_of_products = findViewById(R.id.textview_count_of_products);
        textview_sum_of_product_prices = findViewById(R.id.textview_sum_of_product_prices);

        linearLayout = findViewById(R.id.linearlayout);
        mFABShareList = findViewById(R.id.mFABShareList);
        button_of_clear = findViewById(R.id.picture_rubbish_bin);

        x = 1;
        y = 1;

        spinner = findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinner_types_of_sort, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        // Обнуление списка при нажатии на кнопку "Корзина"
        button_of_clear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    // Обнуление txt файла со списком продуктов
                    fileOutput = openFileOutput("list_of_products.txt", MODE_PRIVATE);
                    fileOutput.write("".getBytes());
                    fileOutput.close();

                    deleteButtonsAndTextViews();
                }
                catch (FileNotFoundException e) {
                }
                catch (IOException e) {
                }
                finally {
                    products = new ArrayList<Product>();

                    writeInfoInPlate();

                    Toast.makeText(ListOfProductsActivity.this, getString(R.string.toast_cleared_list), Toast.LENGTH_LONG).show();
                }
            }
        });

        // Тип сортировки (1-4)
        sort_sign =  1;

        readInfoFromFile();

        writeInfoInPlate();

        chooseTypeOfSort();

        final Toast toastik = Toast.makeText(this, "Список пустий", Toast.LENGTH_SHORT);

        mFABShareList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (products.size() != 0) {
                    StringBuffer listOfProducts = new StringBuffer(formListOfProducts());

                    shareListOfProducts(listOfProducts);
                }
                else
                    toastik.show();

            }
        });
    }

    // Метод Spinner для нажатия на виды сортировки
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        deleteButtonsAndTextViews();

        switch (parent.getItemAtPosition(position).toString()) {
            case "За чергою продуктів":
                sort_sign = 1;
                writeProductsToList(products);
                break;
            case "За цінами продуктів ↓":
                sort_sign = 2;
                sortProducts("SortUp");
                break;
            case "За цінами продуктів ↑":
                sort_sign = 3;
                sortProducts("SortDown");
                break;
            case "За супермаркетами":
                sort_sign = 4;
                sortProducts("Supermarkets");
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    // Вспомогательтный метод, который для выборки типа сортировки
    public void chooseTypeOfSort() {
        switch (sort_sign) {
            case 1:
                sort_sign = 1;
                writeProductsToList(products);
                break;
            case 2:
                sort_sign = 2;
                sortProducts("SortUp");
                break;
            case 3:
                sort_sign = 3;
                sortProducts("SortDown");
                break;
            case 4:
                sort_sign = 4;
                sortProducts("Supermarkets");
                break;
        }
    }

    // Метод для чтения продуктов из txt файла
    public void readInfoFromFile() {
        try {
            FileInputStream fileInput = openFileInput("list_of_products.txt");
            InputStreamReader reader = new InputStreamReader(fileInput);
            BufferedReader buffer = new BufferedReader(reader);
            String mLine;

            while ((mLine = buffer.readLine()) != null) {
                products.add(new Product());
                products.get(products.size() - 1).setName_of_product(mLine);
                products.get(products.size() - 1).formPrice_of_product2(mLine);
                products.get(products.size() - 1).formCount_of_product(mLine);
            }
        }
        catch (FileNotFoundException e) {
        }
        catch (IOException e) {
        }
    }

    // Метод для сортировки ArrayList с информацией о продуктах
    public void sortProducts(String typeOfSort) {
        ArrayList<Product> products_copy1 = new ArrayList<Product>();

        for (int i = 0; i < products.size(); i++) {
            products_copy1.add(new Product());
            products_copy1.get(i).setName_of_product(products.get(i).getName_of_product());
            products_copy1.get(i).setPrice_of_product(products.get(i).getPrice_of_product());
        }

        // Сортировка "Бульбашка"
        boolean changes = true;

        do {
            changes = false;
            for (int i = 0; i < products_copy1.size() - 1; i++) {
                int price1 = products_copy1.get(i).getPrice_of_product();
                int price2 = products_copy1.get(i + 1).getPrice_of_product();

                if (((price1 > price2) && (typeOfSort.equals("SortUp") || typeOfSort.equals("Supermarkets"))) || ((price1 < price2) && (typeOfSort.equals("SortDown"))))  {
                    changes = true;
                    products_copy1.get(i).setPrice_of_product(price2);
                    products_copy1.get(i + 1).setPrice_of_product(price1);

                    String name1 = products_copy1.get(i).getName_of_product();
                    String name2 = products_copy1.get(i + 1).getName_of_product();

                    products_copy1.get(i).setName_of_product(name2);
                    products_copy1.get(i + 1).setName_of_product(name1);
                }
            }
        }
        while (changes);

        ArrayList<Product> products_copy2 = new ArrayList<Product>();

        // Дополнительная сортировка при выборе сортировки по супермаркетам
        if (typeOfSort.equals("Supermarkets"))
        {
            String shops[] = {"Novus", "MegaMarket", "Fozzy"};

            // Novus -> MegaMarket -> Fozzy
            for (int i = 0; i < shops.length; i++) {
                for (int j = 0; j < products_copy1.size(); j++) {
                    int price = products_copy1.get(j).getPrice_of_product();
                    String name = products_copy1.get(j).getName_of_product();

                    if (name.lastIndexOf(shops[i]) != -1) {
                        products_copy2.add(new Product());
                        products_copy2.get(products_copy2.size() - 1).setName_of_product(name);
                        products_copy2.get(products_copy2.size() - 1).setPrice_of_product(price);
                    }

                }
            }

            writeProductsToList(products_copy2);
        }
        else {
            writeProductsToList(products_copy1);
        }
    }

    // Метод для записи продуктов в ScrollView -> LinearLayout
    public void writeProductsToList(ArrayList<Product> products) {
        for (int i = 0; i < products.size(); i++)
            addButtonAndTextView(products.get(i).getName_of_product());
    }


    // Метод для удаления определенного продукта из файла
    public void changeCountOfProduct(String product) {
        try {
            StringBuffer strBuffer = new StringBuffer();

            FileInputStream fileInput = openFileInput("list_of_products.txt");
            InputStreamReader reader = new InputStreamReader(fileInput);
            BufferedReader buffer = new BufferedReader(reader);
            String mLine;

            boolean f = false;

            while ((mLine = buffer.readLine()) != null) {
                if ((mLine.lastIndexOf(product) != -1) && (f == false)) {
                    f = true;

                    if (mLine.lastIndexOf("X") > mLine.lastIndexOf(")"))
                    {
                        String s1 = mLine.substring(mLine.lastIndexOf("X") + 1, mLine.length());
                        int count = Integer.parseInt(s1);

                        if (count != 2)
                        {
                            String s2 = mLine.substring(0, mLine.lastIndexOf("X") + 1);

                            strBuffer.append(s2 + Integer.toString(count - 1) + '\n');
                        }
                        else
                        {
                            String s2 = mLine.substring(0, mLine.lastIndexOf(")") + 1);

                            strBuffer.append(s2 + '\n');
                        }
                    }
                }
                else
                    strBuffer.append(mLine + '\n');
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

    // Метод для удаления всех кнопок и надписей в LinearLayout
    public void deleteButtonsAndTextViews() {
        for (int i = x; i < y; i++)
        {
            View z = findViewById(i);
            z.setVisibility(View.GONE);
        }

        // "Обнуление" ID
        x = y;
    }

    // Метод для добавления кнопок и надписей в LinearLayout
   public void addButtonAndTextView(final String product) {
        int number = (y - x) / 2 + 1;

        TextView t = new TextView(getApplicationContext());
        t.setText('\n' + Integer.toString(number) + ") " + product + '\n');
        t.setTextSize(18);
        t.setId(y);
        y++;
        linearLayout.addView(t);

        Button b = new Button(getApplicationContext());
        b.setText(getString(R.string.button_delete_product) + Integer.toString(number));
        b.setId(y);
        b.setBackgroundColor(getColor(R.color.colorOrange));
        y++;
        linearLayout.addView(b);

        // Удаление конкретного продукта при нажатии на кнопку
        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                deleteButtonsAndTextViews();

                changeCountOfProduct(product);

                products = new ArrayList<Product>();

                readInfoFromFile();

                chooseTypeOfSort();

                writeInfoInPlate();

                Toast.makeText(ListOfProductsActivity.this, getString(R.string.toast_deleted_product), Toast.LENGTH_LONG).show();
            }
        });
    }

    // Метод, который узнает обшую сумму списка продуктов
    public int getSumOfPrices() {
        int sum = 0;

        for (int i = 0; i < products.size(); i++)
            sum = sum + products.get(i).getPrice_of_product() * products.get(i).getCount_of_product();

        return sum;
    }

    // Метод, который узнает обшее количество продуктов
    public int getCountOfProducts() {
        int count  = 0;

        for (int i = 0; i < products.size(); i++)
            count = count + products.get(i).getCount_of_product();

        return count;
    }

    // Метод, который выводит количество и общую сумму продуктов в списке
    public void writeInfoInPlate() {
        int count = getCountOfProducts();

        textview_number_of_products.setText(getString(R.string.count_of_products_list) + Integer.toString(count));

        String sum_str;

        if (count == 0) {
            sum_str = "0.00";
        }
        else {
            // Формулировка общей суммы продуктов
            int sum_int = getSumOfPrices();

            if (sum_int % 100 < 10) {
                sum_str = Integer.toString(sum_int / 100) + ".0" + Integer.toString(sum_int % 100);
            }
            else {
                sum_str = Integer.toString(sum_int / 100) + "." + Integer.toString(sum_int % 100);
            }
        }
        textview_sum_of_product_prices.setText(getString(R.string.price_of_list) + sum_str + " грн.");
    }

    private StringBuffer formListOfProducts() {
        StringBuffer listOfProducts = new StringBuffer();

        listOfProducts.append("Список продуктів (" + textview_sum_of_product_prices.getText() + ") :" + '\n');

        for (int i = 0; i < products.size(); i++)
            listOfProducts.append(Integer.toString(i + 1) + ") " + products.get(i).getName_of_product() + '\n');

        listOfProducts.append('\n' + "---" + '\n' + "Створено за допомогою додатка \"MyKoshik\"");

        Log.d("###", listOfProducts.toString());

        return listOfProducts;
    }

    private void shareListOfProducts(StringBuffer listOfProducts) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);

        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Список Продуктів");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, listOfProducts.toString());

        startActivity(Intent.createChooser(sharingIntent, "Поділитися за допомогою"));
    }
}
