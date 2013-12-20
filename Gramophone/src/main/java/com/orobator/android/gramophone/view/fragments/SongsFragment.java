package com.orobator.android.gramophone.view.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;

import com.orobator.android.gramophone.R;
import com.orobator.android.gramophone.model.Library;
import com.orobator.android.gramophone.model.Song;
import com.orobator.android.gramophone.view.activities.SongMetadataActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

public class SongsFragment extends ListFragment {
    public static final String KEY_SONG = "song";
    private static final String TAG = "SongsFragment";
    SongAdapter mAdapter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.i(TAG, "onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
        setRetainInstance(true);

//        getActivity();

        Library library = Library.getLibrary(getActivity().getApplicationContext());

        long startTime = System.currentTimeMillis();
        mAdapter = new SongAdapter(library.getSongs());
        setListAdapter(mAdapter);
        long endTime = System.currentTimeMillis();
        int songCount = mAdapter.mSongs.size();
        double timeInSeconds = (endTime - startTime) / 1000.0;
        Toast toast = Toast.makeText(getActivity(), "Loaded " + songCount +
                " songs in " + timeInSeconds + " seconds", Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_view_songs, parent, false);

        Log.i(TAG, "onCreateView");

        return view;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {

        Song song = (Song) getListAdapter().getItem(position);

        Intent intent = new Intent(getActivity(), SongMetadataActivity.class);
        intent.putExtra(KEY_SONG, song);

        startActivity(intent);
    }

    static class NoAlbumArtViewHolder {
        TextView songTitleTextView;
        TextView songArtistTextView;
    }

    static class NoArtistOrTitleViewHolder {
        TextView fileName;
    }

    private class SongAdapter extends ArrayAdapter<Song> implements SectionIndexer {
        protected static final int VIEW_TYPE_NO_ALBUM_ART = 0;
        protected static final int VIEW_TYPE_NO_ARTIST_OR_TITLE = 1;
        protected static final int VIEW_TYPE_HAS_ALBUM_ART = 2;
        protected static final int VIEW_TYPE_COUNT = VIEW_TYPE_HAS_ALBUM_ART + 1;
        private ArrayList<Song> mSongs;
        private Vector<String> mSections;
        private HashMap<String, Integer> sectionMap;

        public SongAdapter(ArrayList<Song> songs) {
            super(getActivity(), android.R.layout.simple_list_item_1, songs);
            mSongs = songs;
            mSections = new Vector<String>();
            sectionMap = new HashMap<String, Integer>();
            initializeSections();
        }

        private void initializeSections() {
            for (int i = 0; i < mSongs.size(); i++) {
                String title = mSongs.get(i).getTitle();
                if (title.toLowerCase().startsWith("the ")) {
                    title = title.substring(3);
                }
                String firstLetter = title.substring(0, 1).toUpperCase();
                Integer myInt = Integer.getInteger(firstLetter);
                if (myInt != null) {
                    firstLetter = "123";
                }
                if (!sectionMap.containsKey(firstLetter)) {
                    if (startsWithAlphaNum(firstLetter)) {
                        sectionMap.put(firstLetter, i);
                        mSections.add(firstLetter);
                    }
                }
            }
        }

        private boolean startsWithAlphaNum(String str) {
            if (str == null) return false;

            // TODO Clean up code with REGEX
            return !(str.toLowerCase().startsWith("~")
                    || str.toLowerCase().startsWith("!")
                    || str.toLowerCase().startsWith("@")
                    || str.toLowerCase().startsWith("#")
                    || str.toLowerCase().startsWith("$")
                    || str.toLowerCase().startsWith("%")
                    || str.toLowerCase().startsWith("^")
                    || str.toLowerCase().startsWith("&")
                    || str.toLowerCase().startsWith("*")
                    || str.toLowerCase().startsWith("(")
                    || str.toLowerCase().startsWith(")")
                    || str.toLowerCase().startsWith("_")
                    || str.toLowerCase().startsWith("-")
                    || str.toLowerCase().startsWith("+")
                    || str.toLowerCase().startsWith("=")
                    || str.toLowerCase().startsWith("`")
                    || str.toLowerCase().startsWith("[")
                    || str.toLowerCase().startsWith("]")
                    || str.toLowerCase().startsWith("{")
                    || str.toLowerCase().startsWith("}")
                    || str.toLowerCase().startsWith("\\")
                    || str.toLowerCase().startsWith("|")
                    || str.toLowerCase().startsWith(":")
                    || str.toLowerCase().startsWith(";")
                    || str.toLowerCase().startsWith("'")
                    || str.toLowerCase().startsWith("\"")
                    || str.toLowerCase().startsWith("<")
                    || str.toLowerCase().startsWith(">")
                    || str.toLowerCase().startsWith(",")
                    || str.toLowerCase().startsWith(".")
                    || str.toLowerCase().startsWith("?")
                    || str.toLowerCase().startsWith("/"));
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Song song = getItem(position);

            int type = getItemViewType(position);

            switch (type) {
                case VIEW_TYPE_HAS_ALBUM_ART:
                case VIEW_TYPE_NO_ALBUM_ART:
                    if (convertView == null) {
                        convertView = getActivity().getLayoutInflater()
                                .inflate(R.layout.list_item_no_album_art, null);
                        NoAlbumArtViewHolder holder = new NoAlbumArtViewHolder();
                        holder.songTitleTextView = (TextView) convertView.findViewById(R.id.songTitle_TextView);
                        holder.songArtistTextView = (TextView) convertView.findViewById(R.id.songArtist_TextView);
                        convertView.setTag(holder);
                    }

                    NoAlbumArtViewHolder holder = (NoAlbumArtViewHolder) convertView.getTag();
                    holder.songTitleTextView.setText(song.getTitle());
                    holder.songArtistTextView.setText(song.getArtist());
                    break;

                case VIEW_TYPE_NO_ARTIST_OR_TITLE:
                    if (convertView == null) { // if we weren't given a view, inflate one
                        convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_no_title_or_artist, null);
                        NoArtistOrTitleViewHolder holder1 = new NoArtistOrTitleViewHolder();
                        holder1.fileName = (TextView) convertView.findViewById(R.id.fileName_TextView);
                        convertView.setTag(holder1);
                    }

                    NoArtistOrTitleViewHolder holder1 = (NoArtistOrTitleViewHolder) convertView.getTag();
                    holder1.fileName.setText(song.getFileName());
                    break;
            }

            return convertView;
        }

        @Override
        public int getItemViewType(int position) {
            Song song = getItem(position);

            if (song.getArtist() == null || song.getTitle() == null) {
                return VIEW_TYPE_NO_ARTIST_OR_TITLE;
            }

            if (song.hasArtwork()) {
                return VIEW_TYPE_HAS_ALBUM_ART;
            } else {
                return VIEW_TYPE_NO_ALBUM_ART;
            }
        }

        @Override
        public int getViewTypeCount() {
            return VIEW_TYPE_COUNT;
        }

        @Override
        public Object[] getSections() {
            return mSections.toArray();
        }

        @Override
        public int getPositionForSection(int section) {
            return sectionMap.get(mSections.get(section));
        }

        @Override
        public int getSectionForPosition(int position) {
            Song song = getItem(position);
            String title = song.getTitle();
            String firstLetter = title.substring(0, 1);
            for (int i = 0; i < mSections.size(); i++) {
                if (firstLetter.equals(mSections.get(i))) {
                    return i;
                }
            }
            return 0;
        }
    }
}