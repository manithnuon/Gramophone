package com.orobator.android.gramophone.view.fragments;import android.app.FragmentManager;import android.app.ListFragment;import android.app.LoaderManager;import android.content.Context;import android.content.Loader;import android.database.Cursor;import android.os.Bundle;import android.view.LayoutInflater;import android.view.View;import android.view.ViewGroup;import android.widget.CursorAdapter;import android.widget.ListView;import com.orobator.android.gramophone.R;import com.orobator.android.gramophone.model.Album;import com.orobator.android.gramophone.model.SongDatabaseHelper;import com.orobator.android.gramophone.model.SongDatabaseHelper.ArtistCursor;import com.orobator.android.gramophone.model.loaders.SQLiteCursorLoader;import com.orobator.android.gramophone.view.adapters.ArtistCursorAdapter;/** * Fragment for displaying all of the artists on the device */public class ArtistsFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {    private static final String TAG = "ArtistsFragment";    @Override    public Loader<Cursor> onCreateLoader(int id, Bundle args) {        return new ArtistsListCursorLoader(getActivity());    }    @Override    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {        ArtistCursor artistCursor = (ArtistCursor) cursor;        ArtistCursorAdapter adapter = new ArtistCursorAdapter(getActivity().getApplicationContext(), artistCursor);        setListAdapter(adapter);        adapter.notifyDataSetChanged();    }    @Override    public void onLoaderReset(Loader<Cursor> loader) {        setListAdapter(null);    }    @Override    public void onCreate(Bundle savedInstanceState) {        super.onCreate(savedInstanceState);        setRetainInstance(true);        getLoaderManager().initLoader(0, null, this);    }    @Override    public void onDestroy() {        CursorAdapter cursorAdapter = (CursorAdapter) getListAdapter();        Cursor cursor = cursorAdapter.getCursor();        if (cursor != null) {            cursor.close();        }        super.onDestroy();    }    @Override    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {        return inflater.inflate(R.layout.list_view_artists, parent, false);    }    @Override    public void onListItemClick(ListView listView, View view, int position, long id) {        String artist = (String) getListAdapter().getItem(position);        Bundle args = new Bundle();        args.putString(Album.KEY_ALBUM_ARTIST, artist);        ArtistsAlbumsFragment fragment = new ArtistsAlbumsFragment();        fragment.setArguments(args);        FragmentManager fm = getActivity().getFragmentManager();        fm.beginTransaction()                .replace(R.id.content_frame, fragment)                .addToBackStack(artist)                .commit();    }    private static class ArtistsListCursorLoader extends SQLiteCursorLoader {        public ArtistsListCursorLoader(Context context) {            super(context);        }        @Override        protected Cursor loadCursor() {            return new SongDatabaseHelper(getContext()).queryArtists();        }    }}