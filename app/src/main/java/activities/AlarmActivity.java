package activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;

public class AlarmActivity extends AppCompatActivity {

    private TimePicker mTimePicker;
    private FloatingActionButton mFABSetAlarm;
    private EditText mETMessage;
    private CheckBox mCBMonday, mCBTuesday, mCBWednesday, mCBThursday, mCBFriday, mCBSaturday, mCBSunday;


    private int hour, minutes;
    private String message;
    private ArrayList<Integer> alarmDays;

    private int REQUEST_CODE_ALARM = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        mTimePicker = (TimePicker) findViewById(R.id.mTimePicker);
        mFABSetAlarm = (FloatingActionButton) findViewById(R.id.mFABSetAlarm);
        mETMessage = (EditText) findViewById(R.id.mETMessage);
        mCBMonday = (CheckBox) findViewById(R.id.mCB1);
        mCBTuesday = (CheckBox) findViewById(R.id.mCB2);
        mCBWednesday = (CheckBox) findViewById(R.id.mCB3);
        mCBThursday = (CheckBox) findViewById(R.id.mCB4);
        mCBFriday = (CheckBox) findViewById(R.id.mCB5);
        mCBSaturday = (CheckBox) findViewById(R.id.mCB6);
        mCBSunday = (CheckBox) findViewById(R.id.mCB7);

        mTimePicker.setIs24HourView(true);

        mFABSetAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAlarm();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Toast.makeText(this, getString(R.string.alarmactivity_added_alarm_toast), Toast.LENGTH_LONG).show();
    }

    private void createAlarm() {
        hour = mTimePicker.getHour();
        minutes = mTimePicker.getMinute();

        message = mETMessage.getText().toString();

        Log.d("AlarmActivity", "Time : " + hour + ":" + minutes);
        Log.d("AlarmActivity", "Message : " + message);

        formAlarmDays();

        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);

        intent.putExtra(AlarmClock.EXTRA_HOUR, hour);
        intent.putExtra(AlarmClock.EXTRA_MINUTES, minutes);
        intent.putExtra(AlarmClock.EXTRA_MESSAGE, message);
        intent.putExtra(AlarmClock.EXTRA_DAYS, alarmDays);

        startActivityForResult(intent, REQUEST_CODE_ALARM);
    }

    private void formAlarmDays() {
        alarmDays = new ArrayList<Integer>();

        if (mCBMonday.isChecked() == true)
            alarmDays.add(Calendar.MONDAY);
        if (mCBTuesday.isChecked() == true)
            alarmDays.add(Calendar.TUESDAY);
        if (mCBWednesday.isChecked() == true)
            alarmDays.add(Calendar.WEDNESDAY);
        if (mCBThursday.isChecked() == true)
            alarmDays.add(Calendar.THURSDAY);
        if (mCBFriday.isChecked() == true)
            alarmDays.add(Calendar.FRIDAY);
        if (mCBSaturday.isChecked() == true)
            alarmDays.add(Calendar.SATURDAY);
        if (mCBSunday.isChecked() == true)
            alarmDays.add(Calendar.SUNDAY);
    }
}