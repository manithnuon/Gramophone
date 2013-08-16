package com.orobator.android.gramophone.gui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.orobator.android.gramophone.R;
import com.orobator.android.gramophone.model.Library;
import com.orobator.android.gramophone.model.Song;

import java.util.ArrayList;

public class SongsFragment extends ListFragment {
    private static final String TAG = "SongsFragment";
    private String[] supportedFormats;

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

        getActivity();

        Library library = Library.getLibrary(getActivity());

        SongAdapter adapter = new SongAdapter(library.getSongs());
        setListAdapter(adapter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "onActivityCreated");

        //String to be displayed if library is empty
        setEmptyText(getString(R.string.no_songs_found));

    }

    @Override
    public void onStart() {
        super.onStart();

        Log.i(TAG, "onStart");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, parent, savedInstanceState);
        Log.i(TAG, "onCreateView");

        return view;
    }

    private class SongAdapter extends ArrayAdapter<Song> {

        public SongAdapter(ArrayList<Song> songs) {
            super(getActivity(), android.R.layout.simple_list_item_1, songs);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            // if we weren't given a view, inflate one
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(android.R.layout.simple_list_item_1, null);
            }

            // configure the view for this Crime
            Song song = getItem(position);

            TextView songView = (TextView) convertView;
            songView.setText(song.getLocation()); //TODO setText may produce NullPointerException. Figure out why and then prevent it from happening
            return songView;
        }

    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        //TODO start an intent to metaata fragment here

        String title = ((Song) getListAdapter().getItem(position)).getTitle();
        String artist = ((Song) getListAdapter().getItem(position)).getArtist();
        Toast titleToast = Toast.makeText(getActivity(), title + " - " + artist, Toast.LENGTH_SHORT);
        titleToast.show();
    }
}
