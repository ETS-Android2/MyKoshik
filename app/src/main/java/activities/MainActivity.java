package activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

    private CardView mCVBuy, mCVKoshik, mCVNotes, mCVAlarm, mCVResources, mCVHelp, mCVLetter, mCVInstruction;

    private Dialog mDWResources, mDWHelp;
    private ImageView mIVCloseDialog, mIVCloseDialogHelp;

    private SpinnerDialog mSpinnerDialog;

    private ArrayList<String> spinnerItems = new ArrayList<String>();


    private final String SUPPORT_SERVICE_EMAIL_ADDRESS = "mykoshik_support@ukr.net";

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
        mCVAlarm = (CardView) findViewById(R.id.mCVAlarm);
        mCVResources = (CardView) findViewById(R.id.mCVResources);
        mCVHelp = (CardView) findViewById(R.id.mCVHelp);

        mCVBuy.setOnClickListener(this);
        mCVKoshik.setOnClickListener(this);
        mCVNotes.setOnClickListener(this);
        mCVAlarm.setOnClickListener(this);
        mCVResources.setOnClickListener(this);
        mCVHelp.setOnClickListener(this);

        initSpinnerItems();

        mSpinnerDialog = new SpinnerDialog(this, spinnerItems, getString(R.string.mainactivity_choose_one_product), getString(R.string.mainactivity_close_dialog));

        mSpinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                Intent mainIntent = null;

                switch (spinnerItems.get(position)) {
                    case "Молоко" :
                        mainIntent = new Intent(MainActivity.this, SplashScreenActivity.class);
                        mainIntent.putExtra("TypeOfProduct", "Milk");

                        Log.d("MainActivity", "Milk");
                        break;
                    case "Яйця" :
                        mainIntent = new Intent(MainActivity.this, SplashScreenActivity.class);
                        mainIntent.putExtra("TypeOfProduct", "Eggs");

                        Log.d("MainActivity", "Eggs");
                        break;
                    case "Хліб" :
                        mainIntent = new Intent(MainActivity.this, SplashScreenActivity.class);
                        mainIntent.putExtra("TypeOfProduct", "Bread");

                        Log.d("MainActivity", "Bread");
                        break;
                }

                startActivity(mainIntent);
            }
        });

        mDWResources = new Dialog(MainActivity.this);
        mDWResources.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDWResources.setContentView(R.layout.dialog_window_resources);
        mDWResources.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDWResources.setCancelable(false);

        mIVCloseDialog = (ImageView) mDWResources.findViewById(R.id.mIVCloseDialogRes);


        mDWHelp = new Dialog(MainActivity.this);
        mDWHelp.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDWHelp.setContentView(R.layout.dialog_window_help);
        mDWHelp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDWHelp.setCancelable(false);

        mIVCloseDialog.setOnClickListener(this);

        mIVCloseDialogHelp = (ImageView) mDWHelp.findViewById(R.id.mIVCloseDialogHelp);
        mCVInstruction = (CardView) mDWHelp.findViewById(R.id.mCVInstruction);
        mCVLetter = (CardView) mDWHelp.findViewById(R.id.mCVLetter);

        mCVLetter.setOnClickListener(this);
        mCVInstruction.setOnClickListener(this);
        mIVCloseDialogHelp.setOnClickListener(this);
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
                mDWResources.show();
                break;
            case R.id.mCVNotes :
                intent = new Intent(this, NotesActivity.class);
                startActivity(intent);
                break;
            case R.id.mCVAlarm :
                intent = new Intent(this, AlarmActivity.class);
                startActivity(intent);
                break;
            case R.id.mIVCloseDialogRes :
                mDWResources.dismiss();
                break;
            case R.id.mCVHelp :
                mDWHelp.show();
                break;
            case R.id.mIVCloseDialogHelp :
                mDWHelp.dismiss();
                break;
            case R.id.mCVLetter :
                writeLetter();
                break;
            case R.id.mCVInstruction :
                intent = new Intent(this, InstructionActivity.class);
                startActivity(intent);
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

            Log.d("MainActivity", "First Entry");
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

    private void writeLetter() {
        String emailAddresses[] = new String[]{SUPPORT_SERVICE_EMAIL_ADDRESS};

        Intent intent = new Intent(Intent.ACTION_SENDTO);

        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, emailAddresses);

        startActivity(intent);
    }
}