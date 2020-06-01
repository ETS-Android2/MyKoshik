package activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication.R;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private CardView mCVBuy, mCVKoshik, mCVNotes, mCVStatistics, mCVResources;

    private Dialog mDialogWindowResources;

    private SpinnerDialog mSpinnerDialog;

    private ImageView mIVCloseDialog;

    private ArrayList<String> spinnerItems = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        if (checkFirstRun() == true) {
            Intent intent = new Intent(this, AppIntroActivity.class);
            startActivity(intent);
        }

        mCVBuy = (CardView) findViewById(R.id.mCVBuy);
        mCVKoshik = (CardView) findViewById(R.id.mCVKoshik);
        mCVNotes = (CardView) findViewById(R.id.mCVNotes);
        mCVStatistics = (CardView) findViewById(R.id.mCVStatistics);
        mCVResources = (CardView) findViewById(R.id.mCVResources);

        mCVBuy.setOnClickListener(this);
        mCVKoshik.setOnClickListener(this);
        mCVNotes.setOnClickListener(this);
        mCVStatistics.setOnClickListener(this);
        mCVResources.setOnClickListener(this);

        initSpinnerItems();

        mSpinnerDialog = new SpinnerDialog(this, spinnerItems, "Оберіть один продукт :", "Закрити");

        mSpinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                Intent mainIntent;

                switch (spinnerItems.get(position)) {
                    case "Молоко" :
                        mainIntent = new Intent(MainActivity.this, SplashScreenActivity.class);
                        mainIntent.putExtra("TypeOfProduct", "Milk");
                        startActivity(mainIntent);
                        break;
                    case "Яйця" :
                        mainIntent = new Intent(MainActivity.this, SplashScreenActivity.class);
                        mainIntent.putExtra("TypeOfProduct", "Eggs");
                        startActivity(mainIntent);
                        break;
                    case "Хліб" :
                        mainIntent = new Intent(MainActivity.this, SplashScreenActivity.class);
                        mainIntent.putExtra("TypeOfProduct", "Bread");
                        startActivity(mainIntent);
                        break;
                }
            }
        });

        mDialogWindowResources = new Dialog(MainActivity.this);
        mDialogWindowResources.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialogWindowResources.setContentView(R.layout.dialog_box);
        mDialogWindowResources.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialogWindowResources.setCancelable(false);

        mIVCloseDialog = (ImageView) mDialogWindowResources.findViewById(R.id.mIVCloseDialog);

        mIVCloseDialog.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.mCVBuy :
                mSpinnerDialog.showSpinerDialog();
                break;
            case R.id.mCVKoshik :
                intent = new Intent(this, ListOfProductsActivity.class);
                startActivity(intent);
                break;
            case R.id.mCVResources :
                mDialogWindowResources.show();
                break;
            case R.id.mCVNotes :
                intent = new Intent(this, NotesActivity.class);
                startActivity(intent);
                break;
            case R.id.mIVCloseDialog :
                mDialogWindowResources.dismiss();
                break;
            default :
                Toast.makeText(this, getString(R.string.mainactivity_unavailable_task), Toast.LENGTH_LONG).show();
                break;
        }
    }

    public void initSpinnerItems() {
        for (int i = 0; i < getResources().getStringArray(R.array.mainactivity_types_of_product).length; i++)
            spinnerItems.add(getResources().getStringArray(R.array.mainactivity_types_of_product)[i]);
    }

    public boolean checkFirstRun() {
        boolean entrance;

        if (readDateFromFile().equals("Entered") == true) {
            entrance = false;

            Log.d("MainActivity", "Not First Entry");
        }
        else {
            writeDateToFile("Entered");

            entrance = true;

            Log.d("MainActivity", "First Entery");
        }

        return entrance;
    }

    public void writeDateToFile(String date) {
        try {
            FileOutputStream fileOutput = openFileOutput("file_entrance.txt", MODE_PRIVATE);
            fileOutput.write(date.getBytes());
            fileOutput.close();
        }
        catch (FileNotFoundException e) {
        }
        catch (IOException e) {
        }
    }

    public String readDateFromFile() {
        StringBuffer entry = new StringBuffer();

        try {
            FileInputStream fileInput = openFileInput("file_entrance.txt");
            InputStreamReader reader = new InputStreamReader(fileInput);
            BufferedReader buffer = new BufferedReader(reader);
            String mLine;

            while ((mLine = buffer.readLine()) != null) {
                entry.append(mLine);
            }
        }
        catch (FileNotFoundException e) {
        }
        catch (IOException e) {
        }
        finally {
            return entry.toString();
        }
    }
}