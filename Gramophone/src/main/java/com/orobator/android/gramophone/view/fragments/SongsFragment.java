package com.orobator.android.gramophone.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.orobator.android.gramophone.R;
import com.orobator.android.gramophone.model.Library;
import com.orobator.android.gramophone.model.Song;
import com.orobator.android.gramophone.view.activities.SongMetadataActivity;
import com.orobator.android.gramophone.view.adapters.SongCursorAdapter;

public class SongsFragment extends ListFragment {
    private static final String TAG = "SongsFragment";
    SongCursorAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
        setRetainInstance(true);

        Library library = Library.getLibrary(getActivity().getApplicationContext());

        long startTime = System.currentTimeMillis();
        mAdapter = new SongCursorAdapter(getActivity().getApplicationContext(), library.getSongs());
        setListAdapter(mAdapter);
        long endTime = System.currentTimeMillis();
        int songCount = mAdapter.getSize();
        double timeInSeconds = (endTime - startTime) / 1000.0;
        Log.i(TAG, "Loaded " + songCount + " songs in " + timeInSeconds + " seconds");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_view_songs, parent, false);

        return view;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {

        Song song = (Song) mAdapter.getItem(position);

        Intent intent = new Intent(getActivity(), SongMetadataActivity.class);
        intent.putExtra(SongCursorAdapter.KEY_SONG, song);

        startActivity(intent);
    }

}