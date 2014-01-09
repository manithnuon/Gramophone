package com.orobator.android.gramophone.view.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
    private static final String PREVIOUS_VALUE_ARTIST = "artist";
    private static final String PREVIOUS_VALUE_ALBUM_ARTIST = "album artist";
    private static final String PREVIOUS_VALUE_ALBUM = "album";
    private static final String PREVIOUS_VALUE_GENRE = "genre";
    private static final String PREVIOUS_VALUE_HAS_ARTWORK = "has artwork";
    private static final String PREVIOUS_VALUE_COMPILATION = "compilation";
    private static final String PREVIOUS_VALUE_COMPOSER = "composer";
    private static final String PREVIOUS_VALUE_DISC_NUM = "disc num";
    private static final String PREVIOUS_VALUE_DISC_COUNT = "disc count";
    private static final String PREVIOUS_VALUE_LOCATION = "location";
    private static final String PREVIOUS_VALUE_PLAY_COUNT = "play count";
    private static final String PREVIOUS_VALUE_RATING = "rating";
    private static final String PREVIOUS_VALUE_SKIP_COUNT = "skip count";
    private static final String PREVIOUS_VALUE_TRACK_NUM = "track num";
    private static final String PREVIOUS_VALUE_TRACK_COUNT = "track count";
    private static final String PREVIOUS_VALUE_WRITER = "writer";
    private static final String PREVIOUS_VALUE_YEAR = "year";
    private ListView mMetadataListView;
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

        mMetadataListView = (ListView) view.findViewById(R.id.metadata_listView);
        mMetadataListView.setAdapter(new MetadataListViewAdapter());

        return view;
    }

    class MetadataListViewAdapter extends BaseAdapter {
        // TODO add skip on shuffle
        // TODO add Equalizer Preset
        private static final int VIEW_TYPE_EDIT = 0;
        private static final int VIEW_TYPE_NO_EDIT = 1;
        private static final int VIEW_TYPE_COUNT = 2;
        private static final int TITLE = 0;
        private static final int ARTIST = 1;
        private static final int ALBUM_ARTIST = 2;
        private static final int ALBUM = 3;
        private static final int GENRE = 4;
        private static final int HAS_ARTWORK = 5;
        private static final int BITRATE = 6;
        private static final int COMPILATION = 7;
        private static final int COMPOSER = 8;
        private static final int DATE_MODIFIED = 9;
        private static final int DISC_NUM = 10;
        private static final int DISC_COUNT = 11;
        private static final int DURATION = 12;
        private static final int LOCATION = 13;
        private static final int PLAY_COUNT = 14;
        private static final int RATING = 15;
        private static final int SAMPLE_RATE = 16;
        private static final int SIZE = 17;
        private static final int SKIP_COUNT = 18;
        private static final int TRACK_NUM = 19;
        private static final int TRACK_COUNT = 20;
        private static final int WRITER = 21;
        private static final int YEAR = 22;

        @Override
        public int getCount() {
            return 23;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return (long) position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                if (getItemViewType(position) == VIEW_TYPE_EDIT) {
                    convertView = inflater.inflate(R.layout.list_item_song_metadata, null, false);
                } else {
                    convertView = inflater.inflate(R.layout.list_item_song_metadata_no_edit, null, false);
                }
            }

            TextView metadataCategoryTextView = (TextView) convertView.findViewById(R.id.metadataCategory_TextView);

            if (getItemViewType(position) == VIEW_TYPE_NO_EDIT) {
                switch (position) {
                    case BITRATE:
                        String bitrate = getString(R.string.bitrate, mSong.getBitRate());
                        metadataCategoryTextView.setText(bitrate);
                        break;
                    case DATE_MODIFIED:
                        String dateModified = getString(R.string.date_modified, mSong.getDateModified());
                        metadataCategoryTextView.setText(dateModified);
                        break;
                    case DURATION:
                        String duration = getString(R.string.duration, mSong.displayTime(mSong.getDuration(), true));
                        metadataCategoryTextView.setText(duration);
                        break;
                    case SAMPLE_RATE:
                        String sampleRate = getString(R.string.samplerate, mSong.getSampleRate());
                        metadataCategoryTextView.setText(sampleRate);
                        break;
                    case SIZE:
                        String size = getString(R.string.size, mSong.displaySize());
                        metadataCategoryTextView.setText(size);
                        break;
                    default:
                }
                return convertView;
            }

            Button editButton = (Button) convertView.findViewById(R.id.metadataEdit_Button);
            switch (position) {
                case TITLE:
                    String title = getString(R.string.title, mSong.getTitle());
                    metadataCategoryTextView.setText(title);
                    editButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DialogFragment fragment = new EditDialogFragment();
                            Bundle args = new Bundle();
                            args.putString(PREVIOUS_VALUE, PREVIOUS_VALUE_TITLE);
                            fragment.setArguments(args);
                            fragment.show(getFragmentManager(), "Edit Title");
                        }
                    });
                    break;
                case ARTIST:
                    String artist = getString(R.string.artist, mSong.getArtist());
                    metadataCategoryTextView.setText(artist);
                    editButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DialogFragment fragment = new EditDialogFragment();
                            Bundle args = new Bundle();
                            args.putString(PREVIOUS_VALUE, PREVIOUS_VALUE_ARTIST);
                            fragment.setArguments(args);
                            fragment.show(getFragmentManager(), "Edit Artist");
                        }
                    });
                    break;
                case ALBUM_ARTIST:
                    String albumArtist = getString(R.string.album_artist, mSong.getAlbumArtist());
                    metadataCategoryTextView.setText(albumArtist);
                    editButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DialogFragment fragment = new EditDialogFragment();
                            Bundle args = new Bundle();
                            args.putString(PREVIOUS_VALUE, PREVIOUS_VALUE_ALBUM_ARTIST);
                            fragment.setArguments(args);
                            fragment.show(getFragmentManager(), "Edit Album Artist");
                        }
                    });
                    break;
                case ALBUM:
                    String album = getString(R.string.album, mSong.getAlbum());
                    metadataCategoryTextView.setText(album);
                    editButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DialogFragment fragment = new EditDialogFragment();
                            Bundle args = new Bundle();
                            args.putString(PREVIOUS_VALUE, PREVIOUS_VALUE_ALBUM);
                            fragment.setArguments(args);
                            fragment.show(getFragmentManager(), "Edit Artist");
                        }
                    });
                    break;
                case GENRE:
                    String genre = getString(R.string.genre, mSong.getGenre());
                    metadataCategoryTextView.setText(genre);
                    editButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DialogFragment fragment = new EditDialogFragment();
                            Bundle args = new Bundle();
                            args.putString(PREVIOUS_VALUE, PREVIOUS_VALUE_GENRE);
                            fragment.setArguments(args);
                            fragment.show(getFragmentManager(), "Edit Artist");
                        }
                    });
                    break;
                case HAS_ARTWORK:
                    String hasArtwork = getString(R.string.has_artwork, mSong.hasArtwork() ? "Yes" : "No");
                    metadataCategoryTextView.setText(hasArtwork);
                    editButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DialogFragment fragment = new EditDialogFragment();
                            Bundle args = new Bundle();
                            args.putString(PREVIOUS_VALUE, PREVIOUS_VALUE_HAS_ARTWORK);
                            fragment.setArguments(args);
                            fragment.show(getFragmentManager(), "Edit Artwork");
                        }
                    });
                    break;
                case COMPILATION:
                    String compilation = getString(R.string.compilation, mSong.getCompilationStatus());
                    metadataCategoryTextView.setText(compilation);
                    editButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DialogFragment fragment = new EditDialogFragment();
                            Bundle args = new Bundle();
                            args.putString(PREVIOUS_VALUE, PREVIOUS_VALUE_COMPILATION);
                            fragment.setArguments(args);
                            fragment.show(getFragmentManager(), "Edit Compilation");
                        }
                    });
                    break;
                case COMPOSER:
                    String composer = getString(R.string.composer, mSong.getComposer());
                    metadataCategoryTextView.setText(composer);
                    editButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DialogFragment fragment = new EditDialogFragment();
                            Bundle args = new Bundle();
                            args.putString(PREVIOUS_VALUE, PREVIOUS_VALUE_COMPOSER);
                            fragment.setArguments(args);
                            fragment.show(getFragmentManager(), "Edit Composer");
                        }
                    });
                    break;
                case DISC_NUM:
                    String discNumber = getString(R.string.disc_number, mSong.getDiscNumber());
                    metadataCategoryTextView.setText(discNumber);
                    editButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DialogFragment fragment = new EditDialogFragment();
                            Bundle args = new Bundle();
                            args.putString(PREVIOUS_VALUE, PREVIOUS_VALUE_DISC_NUM);
                            fragment.setArguments(args);
                            fragment.show(getFragmentManager(), "Edit Disc Number");
                        }
                    });
                    break;
                case DISC_COUNT:
                    String discCount = getString(R.string.disc_count, mSong.getDiscCount());
                    metadataCategoryTextView.setText(discCount);
                    editButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DialogFragment fragment = new EditDialogFragment();
                            Bundle args = new Bundle();
                            args.putString(PREVIOUS_VALUE, PREVIOUS_VALUE_DISC_COUNT);
                            fragment.setArguments(args);
                            fragment.show(getFragmentManager(), "Edit Total Discs");
                        }
                    });
                    break;
                case LOCATION:
                    String location = getString(R.string.filePath, mSong.getFilePath());
                    metadataCategoryTextView.setText(location);
                    editButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DialogFragment fragment = new EditDialogFragment();
                            Bundle args = new Bundle();
                            args.putString(PREVIOUS_VALUE, PREVIOUS_VALUE_LOCATION);
                            fragment.setArguments(args);
                            fragment.show(getFragmentManager(), "Edit Location");
                        }
                    });
                    break;
                case PLAY_COUNT:
                    String playCount = getString(R.string.play_count, mSong.getPlayCount());
                    metadataCategoryTextView.setText(playCount);
                    editButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DialogFragment fragment = new EditDialogFragment();
                            Bundle args = new Bundle();
                            args.putString(PREVIOUS_VALUE, PREVIOUS_VALUE_PLAY_COUNT);
                            fragment.setArguments(args);
                            fragment.show(getFragmentManager(), "Reset Play Count");
                        }
                    });
                    break;
                case RATING:
                    String rating = getString(R.string.rating, mSong.getRating());
                    metadataCategoryTextView.setText(rating);
                    editButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DialogFragment fragment = new EditDialogFragment();
                            Bundle args = new Bundle();
                            args.putString(PREVIOUS_VALUE, PREVIOUS_VALUE_RATING);
                            fragment.setArguments(args);
                            fragment.show(getFragmentManager(), "Edit Rating");
                        }
                    });
                    break;
                case SKIP_COUNT:
                    String skipCount = getString(R.string.skip_count, mSong.getSkipCount());
                    metadataCategoryTextView.setText(skipCount);
                    editButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DialogFragment fragment = new EditDialogFragment();
                            Bundle args = new Bundle();
                            args.putString(PREVIOUS_VALUE, PREVIOUS_VALUE_SKIP_COUNT);
                            fragment.setArguments(args);
                            fragment.show(getFragmentManager(), "Reset Skip Count");
                        }
                    });
                    break;
                case TRACK_NUM:
                    String albumTrackNum = getString(R.string.album_track_number, mSong.getTrackNumber());
                    metadataCategoryTextView.setText(albumTrackNum);
                    editButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DialogFragment fragment = new EditDialogFragment();
                            Bundle args = new Bundle();
                            args.putString(PREVIOUS_VALUE, PREVIOUS_VALUE_TRACK_NUM);
                            fragment.setArguments(args);
                            fragment.show(getFragmentManager(), "Edit Album Track Number");
                        }
                    });
                    break;
                case TRACK_COUNT:
                    String albumTrackCount = getString(R.string.album_track_count, mSong.getTrackCount());
                    metadataCategoryTextView.setText(albumTrackCount);
                    editButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DialogFragment fragment = new EditDialogFragment();
                            Bundle args = new Bundle();
                            args.putString(PREVIOUS_VALUE, PREVIOUS_VALUE_TRACK_COUNT);
                            fragment.setArguments(args);
                            fragment.show(getFragmentManager(), "Edit Album Track Count");
                        }
                    });
                    break;
                case WRITER:
                    String writer = getString(R.string.writer, mSong.getWriter());
                    metadataCategoryTextView.setText(writer);
                    editButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DialogFragment fragment = new EditDialogFragment();
                            Bundle args = new Bundle();
                            args.putString(PREVIOUS_VALUE, PREVIOUS_VALUE_WRITER);
                            fragment.setArguments(args);
                            fragment.show(getFragmentManager(), "Edit Writer");
                        }
                    });
                    break;
                case YEAR:
                    String year = getString(R.string.year, mSong.getYear());
                    metadataCategoryTextView.setText(year);
                    editButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DialogFragment fragment = new EditDialogFragment();
                            Bundle args = new Bundle();
                            args.putString(PREVIOUS_VALUE, PREVIOUS_VALUE_YEAR);
                            fragment.setArguments(args);
                            fragment.show(getFragmentManager(), "Edit Year");
                        }
                    });
                    break;
                default:

            }

            return convertView;
        }

        @Override
        public int getItemViewType(int position) {
            switch (position) {
                case BITRATE:
                case DATE_MODIFIED:
                case DURATION:
                case SAMPLE_RATE:
                case SIZE:
                    return VIEW_TYPE_NO_EDIT;
                default:
                    return VIEW_TYPE_EDIT;
            }
        }

        @Override
        public int getViewTypeCount() {
            return VIEW_TYPE_COUNT;
        }

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
                    builder.setPositiveButton(R.string.confirm_metadata_edit, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String newTitle = mEditText.getText().toString();
                            mSong.setTitle(newTitle);
                            ((BaseAdapter) mMetadataListView.getAdapter()).notifyDataSetChanged();
                            mHelper.updateSongMetadata(mSong.getSongID(), SongDatabaseHelper.UPDATE_TITLE, newTitle, mSong.getFilePath());
                        }
                    });
                    break;
                case PREVIOUS_VALUE_ARTIST:
                    mEditText.setText(mSong.getArtist());
                    builder.setPositiveButton(R.string.confirm_metadata_edit, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String newArtist = mEditText.getText().toString();
                            mSong.setArtist(newArtist);
                            ((BaseAdapter) mMetadataListView.getAdapter()).notifyDataSetChanged();
                            mHelper.updateSongMetadata(mSong.getSongID(), SongDatabaseHelper.UPDATE_ARTIST, newArtist, mSong.getFilePath());
                        }
                    });
                    break;
                case PREVIOUS_VALUE_ALBUM_ARTIST:
                    mEditText.setText(mSong.getAlbumArtist());
                    builder.setPositiveButton(R.string.confirm_metadata_edit, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String newAlbumArtist = mEditText.getText().toString();
                            mSong.setAlbumArtist(newAlbumArtist);
                            ((BaseAdapter) mMetadataListView.getAdapter()).notifyDataSetChanged();
                            mHelper.updateSongMetadata(mSong.getSongID(), SongDatabaseHelper.UPDATE_ALBUM_ARTIST, newAlbumArtist, mSong.getFilePath());
                        }
                    });
                    break;
                case PREVIOUS_VALUE_ALBUM:
                    mEditText.setText(mSong.getAlbum());
                    builder.setPositiveButton(R.string.confirm_metadata_edit, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String newAlbum = mEditText.getText().toString();
                            mSong.setAlbum(newAlbum);
                            ((BaseAdapter) mMetadataListView.getAdapter()).notifyDataSetChanged();
                            mHelper.updateSongMetadata(mSong.getSongID(), SongDatabaseHelper.UPDATE_ALBUM, newAlbum, mSong.getFilePath());
                        }
                    });
                    break;
                case PREVIOUS_VALUE_GENRE:
                    mEditText.setText(mSong.getGenre());
                    builder.setPositiveButton(R.string.confirm_metadata_edit, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String newGenre = mEditText.getText().toString();
                            mSong.setGenre(newGenre);
                            ((BaseAdapter) mMetadataListView.getAdapter()).notifyDataSetChanged();
                            mHelper.updateSongMetadata(mSong.getSongID(), SongDatabaseHelper.UPDATE_GENRE, newGenre, mSong.getFilePath());
                        }
                    });
                    break;
                case PREVIOUS_VALUE_HAS_ARTWORK:
                    // TODO this doesn't really make sense now does it?
                    mEditText.setText(mSong.hasArtwork() ? "Yes" : "No");
                    builder.setPositiveButton(R.string.confirm_metadata_edit, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing for now.
                            Toast.makeText(getActivity(), "Coming Soon", Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;
                case PREVIOUS_VALUE_COMPILATION:
                    mEditText.setText(mSong.getCompilationStatus());
                    builder.setPositiveButton(R.string.confirm_metadata_edit, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String newCompilation = mEditText.getText().toString();
                            mSong.setCompilationStatus(newCompilation);
                            ((BaseAdapter) mMetadataListView.getAdapter()).notifyDataSetChanged();
                            mHelper.updateSongMetadata(mSong.getSongID(), SongDatabaseHelper.UPDATE_COMPILATION, newCompilation, mSong.getFilePath());
                        }
                    });
                    break;
                case PREVIOUS_VALUE_COMPOSER:
                    mEditText.setText(mSong.getComposer());
                    builder.setPositiveButton(R.string.confirm_metadata_edit, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String newComposer = mEditText.getText().toString();
                            mSong.setComposer(newComposer);
                            ((BaseAdapter) mMetadataListView.getAdapter()).notifyDataSetChanged();
                            mHelper.updateSongMetadata(mSong.getSongID(), SongDatabaseHelper.UPDATE_COMPOSER, newComposer, mSong.getFilePath());
                        }
                    });
                    break;
                case PREVIOUS_VALUE_DISC_NUM:
                    mEditText.setText(Integer.toString(mSong.getDiscNumber()));
                    mEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                    builder.setPositiveButton(R.string.confirm_metadata_edit, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String newDiscNum = mEditText.getText().toString();
                            mSong.setDiscNumber(Integer.parseInt(newDiscNum));
                            ((BaseAdapter) mMetadataListView.getAdapter()).notifyDataSetChanged();
                            mHelper.updateSongMetadata(mSong.getSongID(), SongDatabaseHelper.UPDATE_DISC_NUM, newDiscNum, mSong.getFilePath());
                        }
                    });
                    break;
                case PREVIOUS_VALUE_DISC_COUNT:
                    mEditText.setText(Integer.toString(mSong.getDiscCount()));
                    mEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                    builder.setPositiveButton(R.string.confirm_metadata_edit, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String newDiscCount = mEditText.getText().toString();
                            mSong.setDiscCount(Integer.parseInt(newDiscCount));
                            ((BaseAdapter) mMetadataListView.getAdapter()).notifyDataSetChanged();
                            mHelper.updateSongMetadata(mSong.getSongID(), SongDatabaseHelper.UPDATE_DISC_COUNT, newDiscCount, mSong.getFilePath());
                        }
                    });
                    break;
                case PREVIOUS_VALUE_LOCATION:
                    mEditText.setText(mSong.getFilePath());
                    builder.setPositiveButton(R.string.confirm_metadata_edit, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing for now.
                            Toast.makeText(getActivity(), "Coming Soon", Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;
                case PREVIOUS_VALUE_PLAY_COUNT:
                    mEditText.setText(Integer.toString(mSong.getPlayCount()));
                    mEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                    builder.setPositiveButton(R.string.confirm_metadata_edit, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO this doesn't make sense either. Should be reset play count
                            String newPlayCount = mEditText.getText().toString();
                            mSong.resetPlayCount();
                            ((BaseAdapter) mMetadataListView.getAdapter()).notifyDataSetChanged();
                            mHelper.updateSongMetadata(mSong.getSongID(), SongDatabaseHelper.UPDATE_PLAY_COUNT, newPlayCount, mSong.getFilePath());
                        }
                    });
                    break;
                case PREVIOUS_VALUE_RATING:
                    mEditText.setText(Integer.toString(mSong.getRating()));
                    mEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                    builder.setPositiveButton(R.string.confirm_metadata_edit, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String newRating = mEditText.getText().toString();
                            mSong.setRating(Integer.parseInt(newRating));
                            ((BaseAdapter) mMetadataListView.getAdapter()).notifyDataSetChanged();
                            mHelper.updateSongMetadata(mSong.getSongID(), SongDatabaseHelper.UPDATE_RATING, newRating, mSong.getFilePath());
                        }
                    });
                    break;
                case PREVIOUS_VALUE_SKIP_COUNT:
                    mEditText.setText(Integer.toString(mSong.getSkipCount()));
                    mEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                    builder.setPositiveButton(R.string.confirm_metadata_edit, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String newSkipCount = mEditText.getText().toString();
                            mSong.resetSkipCount();
                            ((BaseAdapter) mMetadataListView.getAdapter()).notifyDataSetChanged();
                            mHelper.updateSongMetadata(mSong.getSongID(), SongDatabaseHelper.UPDATE_SKIP_COUNT, newSkipCount, mSong.getFilePath());
                        }
                    });
                    break;
                case PREVIOUS_VALUE_TRACK_NUM:
                    mEditText.setText(Integer.toString(mSong.getTrackNumber()));
                    mEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                    builder.setPositiveButton(R.string.confirm_metadata_edit, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String newTrackNum = mEditText.getText().toString();
                            mSong.setTrackNumber(Integer.parseInt(newTrackNum));
                            ((BaseAdapter) mMetadataListView.getAdapter()).notifyDataSetChanged();
                            mHelper.updateSongMetadata(mSong.getSongID(), SongDatabaseHelper.UPDATE_TRACK_NUM, newTrackNum, mSong.getFilePath());
                        }
                    });
                    break;
                case PREVIOUS_VALUE_TRACK_COUNT:
                    mEditText.setText(Integer.toString(mSong.getTrackCount()));
                    mEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                    builder.setPositiveButton(R.string.confirm_metadata_edit, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String newTrackCount = mEditText.getText().toString();
                            mSong.setTrackCount(Integer.parseInt(newTrackCount));
                            ((BaseAdapter) mMetadataListView.getAdapter()).notifyDataSetChanged();
                            mHelper.updateSongMetadata(mSong.getSongID(), SongDatabaseHelper.UPDATE_TRACK_COUNT, newTrackCount, mSong.getFilePath());
                        }
                    });
                    break;
                case PREVIOUS_VALUE_WRITER:
                    mEditText.setText(mSong.getWriter());
                    builder.setPositiveButton(R.string.confirm_metadata_edit, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String newWriter = mEditText.getText().toString();
                            mSong.setWriter(newWriter);
                            ((BaseAdapter) mMetadataListView.getAdapter()).notifyDataSetChanged();
                            mHelper.updateSongMetadata(mSong.getSongID(), SongDatabaseHelper.UPDATE_WRITER, newWriter, mSong.getFilePath());
                        }
                    });
                    break;
                case PREVIOUS_VALUE_YEAR:
                    mEditText.setText(Integer.toString(mSong.getYear()));
                    mEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                    builder.setPositiveButton(R.string.confirm_metadata_edit, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String newYear = mEditText.getText().toString();
                            mSong.setYear(Integer.parseInt(newYear));
                            ((BaseAdapter) mMetadataListView.getAdapter()).notifyDataSetChanged();
                            mHelper.updateSongMetadata(mSong.getSongID(), SongDatabaseHelper.UPDATE_YEAR, newYear, mSong.getFilePath());
                        }
                    });
                    break;
                default:
            }

            builder.setTitle(getTag())
                    .setView(dialogView)
                    .setNegativeButton(R.string.cancel_metadata_edit, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing
                        }
                    });

            return builder.create();


        }

    }
}
