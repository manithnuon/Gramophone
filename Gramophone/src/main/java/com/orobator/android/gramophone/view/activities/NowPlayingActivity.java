package com.orobator.android.gramophone.view.activities;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.orobator.android.gramophone.R;
import com.orobator.android.gramophone.model.Song;
import com.orobator.android.gramophone.model.SongDatabaseHelper;
import com.orobator.android.gramophone.model.SongDatabaseHelper.SongCursor;
import com.orobator.android.gramophone.view.fragments.NowPlayingFragment;

public class NowPlayingActivity extends FragmentActivity {
    private ViewPager mViewPager;
    private SongCursor mSongCursor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.nowPlaying_viewPager);
        setContentView(mViewPager);

        Song song = (Song) getIntent().getSerializableExtra(Song.KEY_SONG);
        int cursorPosition = getIntent().getIntExtra(Song.KEY_CURSOR_POSITION, 0);
        mSongCursor = new SongDatabaseHelper(this).querySongs();
//        setTitle(song.getTitle() + " - " + song.getArtist());
        setTitle("");
        int backgroundColor = song.getBackgroundColor();
        getActionBar().setBackgroundDrawable(new ColorDrawable(backgroundColor));

        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public android.support.v4.app.Fragment getItem(int position) {
                mSongCursor.moveToPosition(position);
                Song songNext = mSongCursor.getSong();
                Bundle args = new Bundle();
                args.putSerializable(Song.KEY_SONG, songNext);
                Fragment fragment = new NowPlayingFragment();
                fragment.setArguments(args);
                return fragment;
            }

            @Override
            public int getCount() {
                return mSongCursor.getCount();
            }
        });

        mViewPager.setCurrentItem(cursorPosition);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int position) {
                mSongCursor.moveToPosition(position);
                Song songCurrent = mSongCursor.getSong();
                int backgroundColor = songCurrent.getBackgroundColor();
                getActionBar().setBackgroundDrawable(new ColorDrawable(backgroundColor));
            }

            @Override
            public void onPageScrollStateChanged(int position) {

            }
        });
    }


}
