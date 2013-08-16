package com.orobator.android.gramophone.gui.activities;

import android.support.v4.app.Fragment;
import android.util.Log;

import com.orobator.android.gramophone.gui.fragments.SongsFragment;

public class MusicActivity extends SingleFragmentActivity {
    private static final String TAG = "MusicActivity";

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
    }


    @Override
    protected Fragment createFragment() {
        return new SongsFragment();
    }
}
