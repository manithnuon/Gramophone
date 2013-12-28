package com.orobator.android.gramophone.view.fragments;import android.app.FragmentManager;import android.app.ListFragment;import android.app.LoaderManager;import android.content.Loader;import android.database.Cursor;import android.os.Bundle;import android.view.LayoutInflater;import android.view.View;import android.view.ViewGroup;import android.widget.CursorAdapter;import android.widget.ListView;import com.orobator.android.gramophone.R;import com.orobator.android.gramophone.model.Album;import com.orobator.android.gramophone.model.SongDatabaseHelper.AlbumCursor;import com.orobator.android.gramophone.model.loaders.AlbumCursorLoader;import com.orobator.android.gramophone.view.adapters.AlbumCursorAdapter;/** * Fragment used to display all of the albums on the device */public class AlbumsFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {    private static final String TAG = "AlbumsFragment";    @Override    public Loader<Cursor> onCreateLoader(int id, Bundle args) {        return new AlbumCursorLoader(getActivity(), id, args);    }    @Override    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {        AlbumCursor albumCursor = (AlbumCursor) cursor;        AlbumCursorAdapter mAdapter = new AlbumCursorAdapter(getActivity().getApplicationContext(), albumCursor);        setListAdapter(mAdapter);    }    @Override    public void onLoaderReset(Loader<Cursor> loader) {        setListAdapter(null);    }    @Override    public void onCreate(Bundle savedInstanceState) {        super.onCreate(savedInstanceState);        setRetainInstance(true);        getLoaderManager().initLoader(AlbumCursorLoader.ALL_ALBUMS_ID, null, this);    }    @Override    public void onDestroy() {        CursorAdapter cursorAdapter = (CursorAdapter) getListAdapter();        Cursor cursor = cursorAdapter.getCursor();        if (cursor != null) {            cursor.close();        }        super.onDestroy();    }    @Override    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {        View view = inflater.inflate(R.layout.list_view_albums, parent, false);        if (view != null) {            ListView listView = (ListView) view.findViewById(android.R.id.list);            listView.setFastScrollAlwaysVisible(true);        }        return view;    }    @Override    public void onListItemClick(ListView listView, View view, int position, long id) {        Album album = (Album) getListAdapter().getItem(position);        Bundle args = new Bundle();        args.putString(Album.KEY_ALBUM_NAME, album.getAlbumName());        args.putString(Album.KEY_ALBUM_ARTIST, album.getAlbumArtist());        AlbumSongsFragment albumSongsFragment = new AlbumSongsFragment();        albumSongsFragment.setArguments(args);        FragmentManager fm = getActivity().getFragmentManager();        fm.beginTransaction()                .replace(R.id.content_frame, albumSongsFragment)                .addToBackStack(album.getAlbumName())                .commit();    }}