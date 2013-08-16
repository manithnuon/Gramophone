package com.orobator.android.gramophone.gui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.TextView;

import com.orobator.android.gramophone.model.Song;

public class SongMetadataFragment extends Fragment {

    private TextView mAlbumTextView;
    private TextView mAlbumArtistTextView;
    private TextView mArtistTextView;
    private TextView mBitrateTextView;
    private TextView mCdTrackNumberTextView;
    private TextView mCompilationTextView;
    private TextView mComposerTextView;
    private TextView mDateModifiedTextView;
    private TextView mDiscNumberTextView;
    private TextView mDurationTextView;
    private TextView mGenreTextView;
    private TextView mLocationTextView;
    private TextView mNumTracksTextView;
    private TextView mTitleTextView;
    private TextView mYearTextView; //TODO make sure you have track num and track count
    private Song mSong;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO Go over fragment arguments to send a song over.
    }
}
