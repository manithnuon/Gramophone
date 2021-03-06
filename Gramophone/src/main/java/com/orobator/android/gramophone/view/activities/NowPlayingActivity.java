package com.orobator.android.gramophone.view.activities;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.view.WindowManager;

import com.orobator.android.gramophone.R;
import com.orobator.android.gramophone.view.fragments.NowPlayingFragment;
import com.readystatesoftware.systembartint.SystemBarTintManager;

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

        setTranslucentStatusAndNavigationBar(true);
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setNavigationBarTintColor(0x00ffffff);
        tintManager.setStatusBarTintColor(0x00ffffff);
    }

    @TargetApi(19)
    private void setTranslucentStatusAndNavigationBar(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int status = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        final int navbar = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
        if (on) {
            winParams.flags |= status;
            winParams.flags |= navbar;

        } else {
            winParams.flags &= ~status;
            winParams.flags &= ~navbar;
        }
        win.setAttributes(winParams);
    }


}
