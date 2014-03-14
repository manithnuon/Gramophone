package com.orobator.android.gramophone.view.activities;

import android.app.Fragment;

import com.orobator.android.gramophone.view.fragments.SettingsFragment;

public class SettingsActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new SettingsFragment();
    }
}
