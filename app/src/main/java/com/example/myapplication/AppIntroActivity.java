package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

// Activity для Intro приложения (Вступительная презентация приложения)
public class AppIntroActivity extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Добавление новых слайдов с презентацией приложения
        addSlide(AppIntroFragment.newInstance(getString(R.string.welcome_intro), "",
                R.drawable.basket_intro, ContextCompat.getColor(getApplicationContext(), R.color.colorAccent)));
        addSlide(AppIntroFragment.newInstance(getString(R.string.list_intro), "",
                R.drawable.checklist_intro, ContextCompat.getColor(getApplicationContext(), R.color.colorPurple)));
        addSlide(AppIntroFragment.newInstance(getString(R.string.supermarket1_intro), getString(R.string.supermarket2_intro),
                R.drawable.store_intro, ContextCompat.getColor(getApplicationContext(), R.color.colorAccent)));
        addSlide(AppIntroFragment.newInstance(getString(R.string.spare_intro), "",
                R.drawable.money_box_intro, ContextCompat.getColor(getApplicationContext(), R.color.colorPurple)));
    }

    // Метод при нажатии кнопки "Done" на последнем слайде
    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);

        // Переадресация в MainActivity
        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }

    // Метод при нажатии кнопки "Skip"
    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);

        // Переадресация в MainActivity
        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }
}
