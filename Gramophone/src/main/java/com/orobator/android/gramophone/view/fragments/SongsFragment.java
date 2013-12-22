package com.orobator.android.gramophone.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.orobator.android.gramophone.R;
import com.orobator.android.gramophone.model.Library;
import com.orobator.android.gramophone.model.Song;
import com.orobator.android.gramophone.view.activities.SongMetadataActivity;
import com.orobator.android.gramophone.view.adapters.SongAdapter;

public class SongsFragment extends ListFragment {
    private static final String TAG = "SongsFragment";
    SongAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
        setRetainInstance(true);

        Library library = Library.getLibrary(getActivity().getApplicationContext());

        long startTime = System.currentTimeMillis();
        mAdapter = new SongAdapter(library.getSongs(), this);
        setListAdapter(mAdapter);
        long endTime = System.currentTimeMillis();
        int songCount = mAdapter.getSize();
        double timeInSeconds = (endTime - startTime) / 1000.0;
        Toast toast = Toast.makeText(getActivity(), "Loaded " + songCount +
                " songs in " + timeInSeconds + " seconds", Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_view_songs, parent, false);

        return view;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {

        Song song = (Song) getListAdapter().getItem(position);

        Intent intent = new Intent(getActivity(), SongMetadataActivity.class);
        intent.putExtra(SongAdapter.KEY_SONG, song);

        startActivity(intent);
    }

}