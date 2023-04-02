package com.zaad.zaad.about;

import android.content.Context;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.danielstone.materialaboutlibrary.model.MaterialAboutList;

public class AboutLicenseList extends AboutList {

    @NonNull
    @Override
    protected MaterialAboutList getMaterialAboutList(@NonNull final Context c) {
        return AboutList.createMaterialAboutLicenseList(c,3);
    }

    @Override
    protected CharSequence getActivityTitle() {
        return getString(com.danielstone.materialaboutlibrary.R.string.mal_title_licenses);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return false;
        }
    }
}

