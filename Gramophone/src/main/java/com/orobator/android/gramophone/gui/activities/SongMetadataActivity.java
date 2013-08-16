package com.orobator.android.gramophone.gui.activities;

import android.support.v4.app.Fragment;

import com.orobator.android.gramophone.gui.fragments.SongMetadataFragment;

public class SongMetadataActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new SongMetadataFragment();
    }
}
