package com.orobator.android.gramophone.view.fragments;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.fortysevendeg.android.swipelistview.SwipeListView;
import com.orobator.android.gramophone.R;
import com.orobator.android.gramophone.controller.listeners.SongClickListener;
import com.orobator.android.gramophone.controller.listeners.SongSwipeViewListener;
import com.orobator.android.gramophone.model.SongDatabaseHelper.SongCursor;
import com.orobator.android.gramophone.model.loaders.SongCursorLoader;
import com.orobator.android.gramophone.view.adapters.DummyListAdapter;
import com.orobator.android.gramophone.view.adapters.SongCursorAdapter;

/**
 * Fragment used to display all of the songs on the device.
 */
public class SongsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = "SongsFragment";
    private SwipeListView mSwipeListView;

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new SongCursorLoader(getActivity(), id, args);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        SongCursor songCursor = (SongCursor) cursor;
        SongCursorAdapter adapter = new SongCursorAdapter(getActivity().getApplicationContext(), songCursor, this);
        SongClickListener clickListener = new SongClickListener(adapter, this);
        mSwipeListView.setAdapter(adapter);
        mSwipeListView.setOnItemClickListener(clickListener);
        mSwipeListView.setOnItemLongClickListener(clickListener);
        mSwipeListView.setSwipeListViewListener(new SongSwipeViewListener(adapter, this));
        mSwipeListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                // Here you can do something when items are selected/de-selected,
                // such as update the title in the CAB
                // TODO update title to reflect selection count
                // TODO create data structure to manage what has been selected
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.song_context_menu, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                // Here you can perform updates to the CAB due to
                // an invalidate() request
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_delete:
                        // deleteSelectedItems();
                        Toast.makeText(getActivity(), "Delete from MultiChoiceModeListener", Toast.LENGTH_LONG).show();
                        mode.finish(); // Action picked, so close the CAB
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                // Here you can make any necessary updates to the activity when
                // the CAB is removed. By default, selected items are deselected/unchecked.
            }
        });
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Stop using the cursor (via the adapter)
        mSwipeListView.setAdapter(new DummyListAdapter());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);


        // Initialize the loader to load the list of songs 
        getLoaderManager().initLoader(SongCursorLoader.ALL_SONGS_ID, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_view_songs, parent, false);

        if (view != null) {
            mSwipeListView = (SwipeListView) view.findViewById(R.id.list_view_songs);

        }

        LinearLayout emptyView = (LinearLayout) inflater.inflate(R.layout.list_view_songs_empty, parent, false);

        // TODO get this working.
        mSwipeListView.setEmptyView(emptyView);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSwipeListView.setSelector(R.drawable.song_list_view_selector);
    }

}
