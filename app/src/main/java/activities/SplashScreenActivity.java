package activities;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import classes.Product;

public class SplashScreenActivity extends AppCompatActivity {
    private ImageView picture_image_of_product;
    private TextView textview_name_of_product, textview_update;
    private Button button_yes, button_no;
    private Dialog dialog;
    private ProgressBar progressBar;

    String typeOfProduct;

    public ArrayList<Product> products_novus = new ArrayList<Product>();
    public ArrayList<Product> products_megamarket = new ArrayList<Product>();
    public ArrayList<Product> products_fozzy = new ArrayList<Product>();

    private final int SPLASH_DISPLAY_LENGTH = 500;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        typeOfProduct = getTypeOfProduct();

        formSplashScreen();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        dialog = new Dialog(SplashScreenActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_window_splashscreen);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        textview_update = dialog.findViewById(R.id.textview_version);
        button_yes = dialog.findViewById(R.id.button_yes);
        button_no = dialog.findViewById(R.id.button_no);

        textview_update.setText(getString(R.string.dialog_text1_splashscreen) + getString(R.string.dialog_text2_splashscreen) + getCurrentDateFromFile());

        button_yes.setBackgroundColor(getColor(R.color.colorGreen));
        button_no.setBackgroundColor(getColor(R.color.colorRed));

        dialog.show();

        button_yes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
                progressBar.setVisibility(View.VISIBLE);

                formProducts(true);
            }
        });

        button_no.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
                progressBar.setVisibility(View.VISIBLE);

                formProducts(false);
            }
        });
    }

    // FORMING SPLASH SCREEN
    public void formSplashScreen() {
        setContentView(R.layout.activity_splash_screen);

        progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(View.INVISIBLE);

        textview_name_of_product = findViewById(R.id.texview_typeOfProduct);
        picture_image_of_product = findViewById(R.id.picture_splash_screen);

        switch (typeOfProduct) {
            case "Milk" :
                textview_name_of_product.setText(getString(R.string.text_milk));
                picture_image_of_product.setImageResource(R.drawable.milk);
                break;
            case "Bread" :
                textview_name_of_product.setText(getString(R.string.text_bread));
                picture_image_of_product.setImageResource(R.drawable.bread);
                break;
            case "Eggs" :
                textview_name_of_product.setText(getString(R.string.text_eggs));
                picture_image_of_product.setImageResource(R.drawable.eggs);
                break;
        }

        picture_image_of_product.getLayoutParams().height = 760;
        picture_image_of_product.getLayoutParams().width = 800;
        picture_image_of_product.setScaleType(ImageView.ScaleType.FIT_XY);
    }

    // GETTING TYPE OF PRODUCT FROM ANOTHER ACTIVITY
    public String getTypeOfProduct() {
        Bundle arguments = getIntent().getExtras();

        return arguments.getString("TypeOfProduct");
    }

    // FORMING INFO ABOUT ALL PRODUCTS
    public void formProducts(final boolean isUpdate) {
       new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /*
                    TRUE - GETTING INFO FROM PARSING
                    FALSE - READING INFO FROM FILE
                 */
                if ((isUpdate == true) && (tryInternetConnection())) {
                        getInfoAboutNovus();
                        if (typeOfProduct.equals("Bread") == false)
                            getInfoAboutMegaMarket();
                        getInfoAboutFozzy();
                }
                else {
                    if ((tryInternetConnection() == false) && (isUpdate == true))
                        Toast.makeText(SplashScreenActivity.this, getString(R.string.problem_internet_connection), Toast.LENGTH_LONG).show();

                        readFromFile("filenovus" + typeOfProduct.toLowerCase() + ".txt", false);
                        if (typeOfProduct.equals("Bread") == false)
                            readFromFile("filemegamarket" + typeOfProduct.toLowerCase() + ".txt", false);
                        readFromFile("filefozzy" + typeOfProduct.toLowerCase() + ".txt", false);
                }

                sendInfoToActivity();

            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    // SENDING INFO TO ANOTHER ACTIVITY
    public void sendInfoToActivity() {
        Intent intent = new Intent(SplashScreenActivity.this, AboutProductsActivity.class);
        Bundle args = new Bundle();

        args.putSerializable("ArrayListNovus", (Serializable) products_novus);
        args.putSerializable("ArrayListMegaMarket", (Serializable) products_megamarket);
        args.putSerializable("ArrayListFozzy", (Serializable) products_fozzy);
        args.putString("TypeOfProduct", typeOfProduct);

        intent.putExtra("Bundle",args);
        startActivity(intent);
    }

    // PARSING NOVUS
    public void getInfoAboutNovus () {
        String html_sign;
        switch (typeOfProduct) {
            case "Milk" :
                html_sign = "milk";
                break;
            case "Bread" :
                html_sign = "bread";
                break;
            case "Eggs" :
                html_sign = "eggs";
                break;
            default:
                html_sign = "";
                break;
        }

        String html1 = "span.jsx-3203167493.product-tile__title";
        String html2 = "span.jsx-2618301845.Price__value_caption";

        try {
            int n = 2;

            String url[] = new String [n];
            url[0] = "https://novus.zakaz.ua/uk/categories/" + html_sign + "/?sort=price_asc";
            for (int i = 1; i < n; i++)
            {
                url[i] = url[0] + "&page=" + (i+1);
            }

            Document doc[] = new Document[1000];
            Elements formElements1[] = new Elements[1000];
            Elements formElements2[] = new Elements[1000];

            StringBuilder strBuffer = new StringBuilder("");

            for (int i = 0; i < n; i++)
            {
                doc[i] = Jsoup.connect(url[i]).get();
                formElements1[i] = doc[i].select(html1);
                formElements2[i] = doc[i].select(html2);

                for (int j = 0; j < formElements1[i].size(); j++) {
                    products_novus.add(new Product());
                    products_novus.get(products_novus.size() - 1).formPrice_of_product(formElements2[i].get(j).text());

                    int price = products_novus.get(products_novus.size() - 1).getPrice_of_product();

                    products_novus.get(products_novus.size() - 1).setName_of_product(formElements1[i].get(j).text() + " - " + Integer.toString(price / 100) + "," + Integer.toString(price - (price / 100) * 100) + " грн " + "(Novus)");

                    strBuffer.append(products_novus.get(products_novus.size() - 1).getName_of_product() + '\n');

                    Log.d("###","NOVUS : " + products_novus.get(products_novus.size() - 1).getName_of_product());
                }
            }

            if (formElements1[0].size() == 0) {
                Log.d("###", "Error (Parsing - NOVUS) : " + "0 Size");
                products_novus = new ArrayList<Product>();

                readFromFile("filenovus" + typeOfProduct.toLowerCase() + ".txt", false);
            }
            else {
                writeToFile("filenovus" + typeOfProduct.toLowerCase() + ".txt", new StringBuilder(strBuffer.toString()));

                setDate(getCurrentDate());
            }
        }
        catch (Exception e) {
            Log.d("###", "Error (Parsing - NOVUS) : " + e.getMessage());
            products_novus = new ArrayList<Product>();

            readFromFile("filenovus" + typeOfProduct.toLowerCase() + ".txt", false);
        }
    }

    // PARSING MEGAMARKET
    public void getInfoAboutMegaMarket () {
        String html_sign;

        switch (typeOfProduct) {
            case "Milk" :
                html_sign = "moloko";
                break;
            case "Eggs" :
                html_sign = "yajtsya";
                break;
            default:
                html_sign = "";
                break;
        }

        String html1, html2;

        html1 = "li.product > div.product_info > h3";
        html2 = "div.price";

        try {
            int n = 2;

            if (typeOfProduct.equals("Eggs"))
                n = 1;

            String url[] = new String[n];

            StringBuilder strBuffer = new StringBuilder("");

            // Проверка на тип продукта (Не хлеб)
            if (html_sign != "") {
                url[0] = "https://megamarket.ua/catalog/" + html_sign + "?sort=price";
                for (int i = 1; i < n; i++) {
                    url[i] = url[0] + "&page=" + (i + 1);
                }

                Document doc[] = new Document[n];
                Elements formElements1[] = new Elements[n];
                Elements formElements2[] = new Elements[n];

                for (int i = 0; i < n; i++) {

                    doc[i] = Jsoup.connect(url[i]).get();
                    formElements1[i] = doc[i].select(html1);
                    formElements2[i] = doc[i].select(html2);

                    for (int j = 0; j < formElements2[i].size(); j++) {
                        products_megamarket.add(new Product());
                        products_megamarket.get(products_megamarket.size() - 1).formPrice_of_product(formElements2[i].get(j).text());

                        int price = products_megamarket.get(products_megamarket.size() - 1).getPrice_of_product();

                        products_megamarket.get(products_megamarket.size() - 1).setName_of_product(formElements1[i].get(j).text() + " - " + Integer.toString(price / 100) + "," + Integer.toString(price - (price / 100) * 100) + " грн" +  " (MegaMarket)");

                        strBuffer.append(products_megamarket.get(products_megamarket.size() - 1).getName_of_product() + '\n');

                        Log.d("###", "MEGAMARKET : " + products_megamarket.get(products_megamarket.size() - 1).getName_of_product());
                    }
                }
            }
            
            writeToFile("filemegamarket" + typeOfProduct.toLowerCase() + ".txt", new StringBuilder(strBuffer.toString()));
        }
        catch (Exception e) {
            Log.d("###", "Error (Parsing - MEGAMARKET) : " + e.getMessage());
            products_megamarket = new ArrayList<Product>();

            readFromFile("filemegamarket" + typeOfProduct.toLowerCase() + ".txt", false);
        }
    }

    // PARSING FOZZY
    public void getInfoAboutFozzy () {
        readFromFile("filefozzy" + typeOfProduct.toLowerCase() + ".txt", false);
    }

    // GETTING DATE NOW
    public String getCurrentDate() {
        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd.MM", Locale.getDefault());
        String dateText = dateFormat.format(currentDate);

        return  dateText;
    }

    // GETTING DATA FROM SHARED PREFERENCES
    // IF THERE IS NO FILE, DATA IS - 10.03
    public String getCurrentDateFromFile() {
        SharedPreferences sPref = getPreferences(MODE_PRIVATE);
        String dateText = sPref.getString("update_date" + typeOfProduct, "10.03");

        return dateText;
    }

    public void setDate(String date) {
        SharedPreferences sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString("update_date" + typeOfProduct, date);
        ed.commit();
    }

    // WRITING DATA TO FILE
    public void writeToFile(String fileName, StringBuilder result) {
        try {
            FileOutputStream fileOutput = openFileOutput(fileName, MODE_PRIVATE);
            fileOutput.write(result.toString().getBytes());
            fileOutput.close();
        }
        catch (Exception e) {
        }
    }

    // READING INFORMATION FROM FILE
   public void readFromFile(String fileName, boolean isFileFromAssets) {
        Log.d("###", "READ : " + fileName + " " + isFileFromAssets);

        StringBuilder strBufferNovus = new StringBuilder();
        StringBuilder strBufferMegaMarket = new StringBuilder();
        StringBuilder strBufferFozzy = new StringBuilder();

        try{
            BufferedReader buffer = null;
            if (isFileFromAssets) {
                buffer = new BufferedReader(new InputStreamReader(getAssets().open(fileName)));
            }
            else
                buffer = new BufferedReader(new InputStreamReader(openFileInput(fileName)));

            String mLine;

            while ((mLine = buffer.readLine()) != null) {
                    if (fileName.lastIndexOf("novus") != -1) {
                        products_novus.add(new Product());
                        products_novus.get(products_novus.size() - 1).setName_of_product(mLine);
                        products_novus.get(products_novus.size() - 1).formPrice_of_product2(mLine);

                        strBufferNovus.append(mLine + '\n');
                    }

                    if (fileName.lastIndexOf("megamarket") != -1) {
                        products_megamarket.add(new Product());
                        products_megamarket.get(products_megamarket.size() - 1).setName_of_product(mLine);
                        products_megamarket.get(products_megamarket.size() - 1).formPrice_of_product2(mLine);

                        strBufferMegaMarket.append(mLine + '\n');
                    }

                    if (fileName.lastIndexOf("fozzy") != -1) {
                        products_fozzy.add(new Product());
                        products_fozzy.get(products_fozzy.size() - 1).setName_of_product(mLine);
                        products_fozzy.get(products_fozzy.size() - 1).formPrice_of_product2(mLine);

                        strBufferFozzy.append(mLine + '\n');
                    }
            }
            if (isFileFromAssets) {
                    writeToFile(fileName, new StringBuilder(strBufferNovus.toString() + strBufferFozzy.toString() + strBufferMegaMarket.toString()));
            }

            Log.d("###", strBufferNovus.toString());
        }
        catch (Exception e) {
            Log.d("###", "Error (Reading) : " + e.getMessage());
            if (fileName.lastIndexOf("novus") != -1)
                products_novus = new ArrayList<Product>();
            else
                if (fileName.lastIndexOf("megamarket") != -1)
                    products_megamarket = new ArrayList<Product>();
                else
                    products_fozzy = new ArrayList<Product>();

            readFromFile(fileName, true);
        }
    }

    // TRYING INTERNET CONNECTION
    public boolean tryInternetConnection() {
        String cs = Context.CONNECTIVITY_SERVICE;
        ConnectivityManager cm = (ConnectivityManager) getSystemService(cs);

        if (cm.getActiveNetworkInfo() == null)
            return false;
        else
            return true;
    }

}