package com.orobator.android.gramophone.view.activities;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.orobator.android.gramophone.R;
import com.orobator.android.gramophone.model.Album;
import com.orobator.android.gramophone.model.Song;
import com.orobator.android.gramophone.model.SongDatabaseHelper;
import com.orobator.android.gramophone.model.SongDatabaseHelper.SongCursor;
import com.orobator.android.gramophone.view.fragments.NowPlayingFragment;

public class NowPlayingActivity extends FragmentActivity {
    private static final String TAG = "NowPlayingActivity";
    private ViewPager mViewPager;
    private SongCursor mSongCursor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.nowPlaying_viewPager);
        mViewPager.setOffscreenPageLimit(3);
        setContentView(mViewPager);

        Song song = (Song) getIntent().getSerializableExtra(Song.KEY_SONG);
        int cursorPosition = getIntent().getIntExtra(Song.KEY_CURSOR_POSITION, 0);
        String collectionType = getIntent().getStringExtra(Song.KEY_SONG_COLLECTION_TYPE);
        SongDatabaseHelper helper = new SongDatabaseHelper(this);

        if (collectionType.equals(Song.KEY_COLLECTION_TYPE_ALL)) {
            mSongCursor = helper.querySongs();
        } else if (collectionType.equals(Song.KEY_COLLECTION_TYPE_ALBUMS)) {
            String albumName = getIntent().getStringExtra(Song.KEY_ALBUM);
            String albumArtist = getIntent().getStringExtra(Song.KEY_ALBUM_ARTIST);
            Album album = new Album(albumName, albumArtist);
            mSongCursor = helper.querySongsForAlbum(album);
        } else if (collectionType.equals(Song.KEY_COLLECTION_TYPE_GENRES)) {
            String genre = getIntent().getStringExtra(Song.KEY_GENRE);
            mSongCursor = helper.querySongsForGenre(genre);
        }


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
