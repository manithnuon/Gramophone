package com.orobator.android.gramophone.view.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.orobator.android.gramophone.R;
import com.orobator.android.gramophone.model.Song;
import com.orobator.android.gramophone.model.SongDatabaseHelper;

/**
 * This fragment displays metadata for a selected song.
 */
public class SongMetadataFragment extends Fragment {
    private static final String TAG = "SongMetadataFragment";
    private static final String PREVIOUS_VALUE = "previous value";
    private static final String PREVIOUS_VALUE_TITLE = "title";
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
    private TextView mRatingTextView;
    private TextView mSizeTextView;
    private TextView mTitleTextView;
    private TextView mHasArtworkTextView;
    private TextView mPlayCountTextView;
    private TextView mYearTextView;
    private Button mEditTitleButton;
    private Song mSong;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        mSong = (Song) getActivity().getIntent().getSerializableExtra(Song.KEY_SONG);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_song_metadata, parent, false);
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
        String dateModified = getString(R.string.date_modified, mSong.getDateModified());
        mDateModifiedTextView.setText(dateModified);

        mDiscNumberTextView = (TextView) view.findViewById(R.id.disc_number_textView);
        String discNumber = getString(R.string.disc_number, mSong.getDiscNumber());
        mDiscNumberTextView.setText(discNumber);

        mDiscCountTextView = (TextView) view.findViewById(R.id.disc_count_textView);
        String discCount = getString(R.string.disc_count, mSong.getDiscTotal());
        mDiscCountTextView.setText(discCount);

        mDurationTextView = (TextView) view.findViewById(R.id.duration_textView);
        String duration = getString(R.string.duration, mSong.displayTime(mSong.getDuration(), true));
        mDurationTextView.setText(duration);

        mGenreTextView = (TextView) view.findViewById(R.id.genre_textView);
        String genre = getString(R.string.genre, mSong.getGenre());
        mGenreTextView.setText(genre);

        mLocationTextView = (TextView) view.findViewById(R.id.filename_textView);
        String location = getString(R.string.filePath, mSong.getFilePath());
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

        mPlayCountTextView = (TextView) view.findViewById(R.id.play_count_textView);
        String playCount = getString(R.string.play_count, mSong.getPlayCount());
        mPlayCountTextView.setText(playCount);

        mRatingTextView = (TextView) view.findViewById(R.id.rating_textView);
        String rating = getString(R.string.rating, mSong.getRating());
        mRatingTextView.setText(rating);

        mEditTitleButton = (Button) view.findViewById(R.id.editTitle_button);
        mEditTitleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment fragment = new EditDialogFragment();
                Bundle args = new Bundle();
                args.putString(PREVIOUS_VALUE, PREVIOUS_VALUE_TITLE);
                fragment.setArguments(args);
                fragment.show(getFragmentManager(), "Edit Title");
            }
        });

        return view;
    }

    class EditDialogFragment extends DialogFragment {
        EditText mEditText;
        SongDatabaseHelper mHelper;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            mHelper = new SongDatabaseHelper(getActivity().getApplicationContext());
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            // Get the layout inflater
            LayoutInflater inflater = getActivity().getLayoutInflater();

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout

            View dialogView = inflater.inflate(R.layout.dialog_fragment_edit_metadata, null);
            mEditText = (EditText) dialogView.findViewById(R.id.metadata_editText);

            switch (getArguments().getString(PREVIOUS_VALUE)) {
                case PREVIOUS_VALUE_TITLE:
                    mEditText.setText(mSong.getTitle());
                    break;
                default:
            }

            builder.setTitle(getTag())
                    .setView(dialogView)
                    .setPositiveButton(R.string.confirm_metadata_edit, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String newTitle = mEditText.getText().toString();
                            mTitleTextView.setText(newTitle);
                            mSong.setTitle(newTitle);
                            mHelper.updateSongMetadata(mSong.getSongID(), SongDatabaseHelper.UPDATE_TITLE, newTitle, mSong.getFilePath());
                        }
                    }).setNegativeButton(R.string.cancel_metadata_edit, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            return builder.create();


        }

    }
}
