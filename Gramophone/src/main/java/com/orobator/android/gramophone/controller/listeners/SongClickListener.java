package com.orobator.android.gramophone.controller.listeners;import android.app.Fragment;import android.util.Log;import android.view.ActionMode;import android.view.Menu;import android.view.MenuInflater;import android.view.MenuItem;import android.view.View;import android.widget.AdapterView;import android.widget.ListAdapter;import android.widget.Toast;import com.orobator.android.gramophone.R;import com.orobator.android.gramophone.model.Song;public class SongClickListener implements AdapterView.OnItemLongClickListener {    private static final String TAG = "SongClickListener";    private ListAdapter mListAdapter;    private Fragment mFragment;    private ActionMode mActionMode;    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {        // Called when the action mode is created; startActionMode() was called        @Override        public boolean onCreateActionMode(ActionMode mode, Menu menu) {            // Inflate a menu resource providing context menu items            MenuInflater inflater = mode.getMenuInflater();            inflater.inflate(R.menu.song_context_menu, menu);            return true;        }        // Called each time the action mode is shown. Always called after onCreateActionMode, but        // may be called multiple times if the mode is invalidated.        @Override        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {            return false; // Return false if nothing is done        }        @Override        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {            switch (item.getItemId()) {                case R.id.action_delete:                    Toast.makeText(mFragment.getActivity(), "Delete from SongClickListener(ActionMode.Callback)", Toast.LENGTH_LONG).show();                    mode.finish(); // Action picked, so close the CAB                    return true;                default:                    return false;            }        }        @Override        public void onDestroyActionMode(ActionMode mode) {            // Called when the user exits the action mode            mActionMode = null;        }    };    public SongClickListener(ListAdapter listAdapter, Fragment fragment) {        mListAdapter = listAdapter;        mFragment = fragment;    }    @Override    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {        Log.d(TAG, "onItemLongClick " + ((Song) mListAdapter.getItem(position)).toString());        if (mActionMode != null) {            return false;        }        // Start the CAB using the ActionMode.Callback defined above        mActionMode = mFragment.getActivity().startActionMode(mActionModeCallback);        view.setSelected(true);        return true;    }}