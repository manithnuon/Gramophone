package com.orobator.android.gramophone.gui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.orobator.android.gramophone.R;
import com.orobator.android.gramophone.model.Song;

public class SongMetadataFragment extends Fragment {
    private static final String TAG = "SongMetadataFragment";

    private TextView mAlbumTextView;
    private TextView mAlbumArtistTextView;
    private TextView mArtistTextView;
    private TextView mBitrateTextView;
    private TextView mCdTrackNumberTextView;
    private TextView mSampleRateTextView;
    private TextView mCompilationTextView;
    private TextView mComposerTextView;
    private TextView mDateModifiedTextView;
    private TextView mDiscNumberTextView;
    private TextView mDiscCountTextView;
    private TextView mDurationTextView;
    private TextView mGenreTextView;
    private TextView mLocationTextView;
    private TextView mTrackCountTextView;
    private TextView mTrackNumberTextView;
    private TextView mfileNameTextView;
    private TextView mWriterTextView;
    private TextView mNumTracksTextView;
    private TextView mSizeTextView;
    private TextView mTitleTextView;
    private TextView mHasArtworkTextView;
    private TextView mPlayCountTextView;
    private TextView mYearTextView; //TODO make sure you have track num and track count
    private Song mSong;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        mSong = (Song) getActivity().getIntent().getSerializableExtra(SongsFragment.KEY_SONG);
        //TODO Go over fragment arguments to send a song over.

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.song_metadata, parent, false);
        Log.i(TAG, "onCreateView");


        mAlbumTextView = (TextView) view.findViewById(R.id.album_textView);
        String album = getString(R.string.album, mSong.getAlbum());
        mAlbumTextView.setText(album);

        mAlbumArtistTextView = (TextView) view.findViewById(R.id.album_artist_textView);
        String albumArtist = getString(R.string.album_artist, mSong.getAlbumArtist());
        mAlbumArtistTextView.setText(albumArtist);

        mArtistTextView = (TextView) view.findViewById(R.id.artist_textView);
        String artist = getString(R.string.artist, mSong.getArtist());
        mArtistTextView.setText(artist);

        mBitrateTextView = (TextView) view.findViewById(R.id.bitrate_textView);
        String bitrate = getString(R.string.bitrate, mSong.getBitRate());
        mBitrateTextView.setText(bitrate);

        mCdTrackNumberTextView = (TextView) view.findViewById(R.id.track_number_textView);
        String cdTrackNumber = getString(R.string.cd_track_number, mSong.getTrackNumber());
        mCdTrackNumberTextView.setText(cdTrackNumber);

        mSampleRateTextView = (TextView) view.findViewById(R.id.sample_rate_textView);
        String sampleRate = getString(R.string.samplerate, mSong.getSampleRate());
        mSampleRateTextView.setText(sampleRate);

        mCompilationTextView = (TextView) view.findViewById(R.id.compilation_textView);
        String compilation = getString(R.string.compilation, mSong.getCompilationStatus());
        mCompilationTextView.setText(compilation);

        mComposerTextView = (TextView) view.findViewById(R.id.composer_textView);
        String composer = getString(R.string.composer, mSong.getComposer());
        mComposerTextView.setText(composer);

        mDateModifiedTextView = (TextView) view.findViewById(R.id.date_modified_textView);
        String dateModified = getString(R.string.date_modified, mSong.getDateModified().toString());
        mDateModifiedTextView.setText(dateModified);

        mDiscNumberTextView = (TextView) view.findViewById(R.id.disc_number_textView);
        String discNumber = getString(R.string.disc_number, mSong.getDiscNumber());
        mDiscNumberTextView.setText(discNumber);

        mDiscCountTextView = (TextView) view.findViewById(R.id.disc_count_textView);
        String discCount = getString(R.string.disc_count, mSong.getDiscCount());
        mDiscCountTextView.setText(discCount);

        mDurationTextView = (TextView) view.findViewById(R.id.duration_textView);
        String duration = getString(R.string.duration, mSong.displayTime(mSong.getDuration(), true));
        mDurationTextView.setText(duration);

        mGenreTextView = (TextView) view.findViewById(R.id.genre_textView);
        String genre = getString(R.string.genre, mSong.getGenre());
        mGenreTextView.setText(genre);

        mLocationTextView = (TextView) view.findViewById(R.id.location_textView);
        String location = getString(R.string.location, mSong.getLocation());
        mLocationTextView.setText(location);

        mTrackCountTextView = (TextView) view.findViewById(R.id.track_count_textView);
        String trackCount = getString(R.string.cd_track_count, mSong.getTrackCount());
        mTrackCountTextView.setText(trackCount);

        mTrackNumberTextView = (TextView) view.findViewById(R.id.track_number_textView);
        String trackNumber = getString(R.string.cd_track_number, mSong.getTrackNumber());
        mTrackNumberTextView.setText(trackNumber);

        mfileNameTextView = (TextView) view.findViewById(R.id.filename_textView);
        String fileName = getString(R.string.filename, mSong.getFileName());
        mfileNameTextView.setText(fileName);

        mWriterTextView = (TextView) view.findViewById(R.id.writer_textView);
        String writer = getString(R.string.writer, mSong.getWriter());
        mWriterTextView.setText(writer);

        mNumTracksTextView = (TextView) view.findViewById(R.id.num_tracks_textView);
        String numTracks = getString(R.string.num_tracks, mSong.getNumTracks());
        mNumTracksTextView.setText(numTracks);

        mSizeTextView = (TextView) view.findViewById(R.id.size_textView);
        String size = getString(R.string.size, mSong.displaySize());
        mSizeTextView.setText(size);

        mTitleTextView = (TextView) view.findViewById(R.id.title_textView);
        String title = getString(R.string.title, mSong.getTitle());
        mTitleTextView.setText(title);

        mHasArtworkTextView = (TextView) view.findViewById(R.id.has_album_art_textView);
        String hasArtwork = getString(R.string.has_artwork, mSong.hasArtwork() ? "Yes" : "No");
        mHasArtworkTextView.setText(hasArtwork);

        mYearTextView = (TextView) view.findViewById(R.id.year_textView);
        String year = getString(R.string.year, mSong.getYear());
        mYearTextView.setText(year);

        return view;
    }
}
