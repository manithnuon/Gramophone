package com.orobator.android.gramophone.view.activities;

import android.app.Fragment;
import android.os.Bundle;

import com.orobator.android.gramophone.model.Song;
import com.orobator.android.gramophone.view.fragments.NowPlayingFragment;

public class NowPlayingActivity extends SingleFragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Song song = (Song) getIntent().getSerializableExtra(Song.KEY_SONG);
        setTitle(song.getTitle() + " - " + song.getArtist());
    }


    @Override
    protected Fragment createFragment() {
        return new NowPlayingFragment();
    }
}
