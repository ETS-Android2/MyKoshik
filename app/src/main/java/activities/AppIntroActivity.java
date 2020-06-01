package activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class AppIntroActivity extends com.github.paolorotolo.appintro.AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(AppIntroFragment.newInstance(getString(R.string.appintroactivity_welcome), "",
                R.drawable.basket_intro, ContextCompat.getColor(getApplicationContext(), R.color.colorAccent)));
        addSlide(AppIntroFragment.newInstance(getString(R.string.appintroactivity_product_list), "",
                R.drawable.checklist_intro, ContextCompat.getColor(getApplicationContext(), R.color.colorPurple)));
        addSlide(AppIntroFragment.newInstance(getString(R.string.appintroactivity_supermarket1), getString(R.string.appintroactivity_supermarket2),
                R.drawable.store_intro, ContextCompat.getColor(getApplicationContext(), R.color.colorAccent)));
        addSlide(AppIntroFragment.newInstance(getString(R.string.appintroactivity_spare), "",
                R.drawable.money_box_intro, ContextCompat.getColor(getApplicationContext(), R.color.colorPurple)));

        setSkipText(getString(R.string.appintroactivity_skip));
        setDoneText(getString(R.string.appintroactivity_next));
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);

        Intent intent = new Intent(getApplicationContext(),MainActivity.class);

        startActivity(intent);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);

        Intent intent = new Intent(getApplicationContext(),MainActivity.class);

        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
