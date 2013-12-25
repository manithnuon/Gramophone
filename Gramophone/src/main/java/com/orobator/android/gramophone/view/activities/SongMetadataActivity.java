package com.orobator.android.gramophone.view.activities;

import android.app.Fragment;

import com.orobator.android.gramophone.view.fragments.SongMetadataFragment;

public class SongMetadataActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new SongMetadataFragment();
    }
}
