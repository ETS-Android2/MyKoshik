package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.paolorotolo.appintro.AppIntroFragment;

// Activity для показа Intro приложения (Вступительная презентация о приложении)
public class AppIntroActivity extends com.github.paolorotolo.appintro.AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Добавление новых слайдов с презентацией приложения
        addSlide(AppIntroFragment.newInstance(getString(R.string.text_welcome_intro), "",
                R.drawable.basket_intro, ContextCompat.getColor(getApplicationContext(), R.color.colorAccent)));
        addSlide(AppIntroFragment.newInstance(getString(R.string.text_list_intro), "",
                R.drawable.checklist_intro, ContextCompat.getColor(getApplicationContext(), R.color.colorPurple)));
        addSlide(AppIntroFragment.newInstance(getString(R.string.text_supermarket1_intro), getString(R.string.text_supermarket2_intro),
                R.drawable.store_intro, ContextCompat.getColor(getApplicationContext(), R.color.colorAccent)));
        addSlide(AppIntroFragment.newInstance(getString(R.string.text_spare_intro), "",
                R.drawable.money_box_intro, ContextCompat.getColor(getApplicationContext(), R.color.colorPurple)));
    }

    // Метод при нажатии кнопки "Done" на последнем слайде
    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);

        // Переход в MainActivity
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }

    // Метод при нажатии кнопки "Skip"
    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);

        // Переход в MainActivity
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }
}
