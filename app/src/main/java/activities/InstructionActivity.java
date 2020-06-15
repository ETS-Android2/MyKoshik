package activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import com.example.myapplication.R;
import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class InstructionActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageSwitcher mImageSwitcher;
    private ImageView mIVBack, mIVForward;

    private final int[] IMAGES = new int[]{R.drawable.instruction_buy_icon, R.drawable.aboutproductactivity_basket, R.drawable.aboutproductactivity_novus, R.drawable.aboutproductsactivity_search_line, R.drawable.aboutproductactivity_button_continue, R.drawable.aboutproductactivity_list,
                                            R.drawable.instruction_list_icon,R.drawable.lisofproductsactivity_delete_list, R.drawable.listofproductsactivity_spinner, R.drawable.listofproductsactivity_list, R.drawable.listofproductsactivity_list, R.drawable.listofproductsactivity_share,
                                            R.drawable.instruction_alarm_icon,R.drawable.alarmactivity_notification, R.drawable.alarmactivity_days_in_week, R.drawable.alarmactivity_add_alarm,
                                            R.drawable.instruction_notifications_icon,R.drawable.notificationsactivity_note, R.drawable.notificationsactivity_delete_note, R.drawable.notificationsactivity_refactor_note, R.drawable.notficationsactivity_share_list};
    private int index = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction);

        mImageSwitcher = (ImageSwitcher) findViewById(R.id.mImageSwitcher);
        mIVBack = (ImageView) findViewById(R.id.mIVBack);
        mIVForward = (ImageView) findViewById(R.id.mIVForward);

        mImageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView mIV = new ImageView(InstructionActivity.this);

                mIV.setScaleType(ImageView.ScaleType.FIT_CENTER);
                mIV.setLayoutParams(new ImageSwitcher.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                return mIV;
            }
        });

        mImageSwitcher.setBackgroundResource(IMAGES[0]);

        mIVBack.setOnClickListener(this);
        mIVForward.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mIVBack :
                if (index != 0) {
                    mImageSwitcher.setBackgroundResource(IMAGES[index - 1]);
                    index = index - 1;
                }
                break;
            case R.id.mIVForward :
                if (index != IMAGES.length - 1) {
                    mImageSwitcher.setBackgroundResource(IMAGES[index + 1]);
                    index = index + 1;
                }
                break;
        }
    }
}