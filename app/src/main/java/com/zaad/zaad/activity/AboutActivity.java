package com.zaad.zaad.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;

import com.danielstone.materialaboutlibrary.MaterialAboutActivity;
import com.danielstone.materialaboutlibrary.model.MaterialAboutCard;
import com.danielstone.materialaboutlibrary.model.MaterialAboutList;
import com.zaad.zaad.R;
import com.zaad.zaad.about.AboutList;

public class AboutActivity extends MaterialAboutActivity {

    protected int colorIcon = R.color.mal_color_icon_light_theme;

    @NonNull
    @Override
    protected MaterialAboutList getMaterialAboutList(@NonNull Context context) {
        return AboutList.createMaterialAboutList(context, colorIcon,3);
    }

    @Nullable
    @Override
    protected CharSequence getActivityTitle() {
        return getString(R.string.app_name);
    }
}