package com.zaad.zaad.activity;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;

import com.github.appintro.AppIntro;
import com.github.appintro.AppIntroFragment;
import com.zaad.zaad.R;

public class AppIntroActivity extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setColorSkipButton(R.color.white);
        setNextArrowColor(R.color.colorPrimary);
        setColorDoneText(R.color.colorPrimary);

        addSlide(AppIntroFragment.createInstance("",
                "",
                0,
                0,0,0,
                0,0,
                R.drawable.intro_1
        ));

        addSlide(AppIntroFragment.createInstance("",
                "",
                0,
                0,0,0,
                0,0,
                R.drawable.intro_2
        ));

        addSlide(AppIntroFragment.createInstance("",
                "",
                0,
                0,0,0,
                0,0,
                R.drawable.intro_3
        ));
    }

    @Override
    protected void onSkipPressed(@Nullable Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        Intent intent = new Intent(AppIntroActivity.this, SignupHomeActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDonePressed(@Nullable Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        Intent intent = new Intent(AppIntroActivity.this, SignupHomeActivity.class);
        startActivity(intent);
    }
}
