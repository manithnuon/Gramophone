package com.orobator.android.gramophone.view.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.orobator.android.gramophone.R;
import com.orobator.android.gramophone.view.fragments.NowPlayingFragment;

public class NowPlayingActivity extends FragmentActivity {
    private static final String TAG = "NowPlayingActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        FragmentManager manager = getFragmentManager();
        Fragment fragment = new NowPlayingFragment();

        manager.beginTransaction()
                .add(R.id.fragmentContainer, fragment)
                .commit();
    }


}
