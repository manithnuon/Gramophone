package com.orobator.android.gramophone.view.fragments;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.orobator.android.gramophone.R;
import com.orobator.android.gramophone.model.Library;
import com.orobator.android.gramophone.model.Song;
import com.orobator.android.gramophone.model.SongDatabaseHelper;
import com.orobator.android.gramophone.model.loaders.SQLiteCursorLoader;
import com.orobator.android.gramophone.view.activities.SongMetadataActivity;
import com.orobator.android.gramophone.view.adapters.SongCursorAdapter;

public class SongsFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = "SongsFragment";

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new SongsListCursorLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Library library = Library.getLibrary(getActivity().getApplicationContext());
        // Create an adapter to point at this cursor
        SongCursorAdapter mAdapter = new SongCursorAdapter(getActivity().getApplicationContext(), library.getSongs());
        setListAdapter(mAdapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Stop using the cursor (via the adapter)
        setListAdapter(null);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
        setRetainInstance(true);

        // Initialize the loader to load the list of runs
        getLoaderManager().initLoader(0, null, this);

//        Library library = Library.getLibrary(getActivity().getApplicationContext());

//        long startTime = System.currentTimeMillis();
//        mAdapter = new SongCursorAdapter(getActivity().getApplicationContext(), library.getSongs());
//        setListAdapter(mAdapter);
//        long endTime = System.currentTimeMillis();
//        int songCount = mAdapter.getSize();
//        double timeInSeconds = (endTime - startTime) / 1000.0;
//        Log.i(TAG, "Loaded " + songCount + " songs in " + timeInSeconds + " seconds");
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
        intent.putExtra(SongCursorAdapter.KEY_SONG, song);

        startActivity(intent);
    }

    private static class SongsListCursorLoader extends SQLiteCursorLoader {

        public SongsListCursorLoader(Context context) {
            super(context);
        }

        @Override
        protected Cursor loadCursor() {
            return new SongDatabaseHelper(getContext()).querySongs();
        }
    }

}