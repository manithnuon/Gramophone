package com.orobator.android.gramophone.view.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.orobator.android.gramophone.R;
import com.orobator.android.gramophone.view.fragments.AlbumsFragment;
import com.orobator.android.gramophone.view.fragments.ArtistsFragment;
import com.orobator.android.gramophone.view.fragments.GenresFragment;
import com.orobator.android.gramophone.view.fragments.SongQueueFragment;
import com.orobator.android.gramophone.view.fragments.SongsFragment;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.ArrayList;
import java.util.List;

/**
 * MusicActivity is the startup Activity and provides views and navigation to
 * the top level fragments.
 */
public class MusicActivity extends Activity {
    private static final String TAG = "MusicActivity";
    private static final int SONGS_FRAGMENT = 0;
    private static final int ALBUMS_FRAGMENT = 1;
    private static final int ARTISTS_FRAGMENT = 2;
    private static final int GENRES_FRAGMENT = 3;
    private static final int QUEUE_FRAGMENT = 4;
    private static final int PLAYLISTS_FRAGMENT = 5;
    private static final int SETTINGS_FRAGMENT = 6;
    private static String PREVIOUS_TOP_BACK_STACK_ENTRY = "com.orobator.android.gramophone.previousTopBackStackEntryName";
    private static int CURRENT_FRAGMENT = -1;
    private String[] nav_items;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerList;
    private CharSequence mTitle;
    private CharSequence mDrawerTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Crashlytics.start(this);

        setContentView(R.layout.nav_drawer);

        setTranslucentStatusAndNavigationBar(true);

        // create our manager instance after the content view is set
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        // enable navigation bar tint
        tintManager.setNavigationBarTintEnabled(true);

        tintManager.setStatusBarTintColor(getResources().getColor(R.color.belize_hole));
        tintManager.setNavigationBarTintColor(0x00000000);

        mTitle = mDrawerTitle = getTitle();

        nav_items = getResources().getStringArray(R.array.nav_drawer_menu_items);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                   /* host activity */
                mDrawerLayout,          /* DrawerLayout object */
                R.drawable.ic_drawer,   /* nav drawer icon to replace "Up" caret */
                R.string.drawer_open,   /* "open drawer" description */
                R.string.drawer_close   /* "close drawer" description */
        ) {

            /**
             * Called when a drawer has settled in a completely open state
             */
            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // Creates a call to onPrepareOptionsMenu()
            }

            /**
             * Called when a drawer has settled in a completely closed state
             */
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // Creates a call to onPrepareOptionsMenu()
            }
        };

        // Set the drawerToggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Add padding to the tops of views so that they don't start under the
        // status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
            int left = mDrawerList.getPaddingLeft();
            int top = config.getActionBarHeight() + config.getStatusBarHeight();
            int right = mDrawerList.getPaddingRight();
            int bottom = mDrawerList.getPaddingBottom();
            mDrawerList.setPadding(left, top, right, bottom);

            View contentFrame = findViewById(R.id.content_frame);
            contentFrame.setPadding(left, top, right, bottom);
        }

        List<String> items = new ArrayList<String>();
        for (String string : nav_items) {
            items.add(string);
        }

        mDrawerList.setAdapter(new CustomListAdapter(this,
                R.layout.nav_drawer_list_item, items,
                "fonts/robotocondensed_light.ttf"));

        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // Set the FragmentManager's onBackStackChangedListener
        final FragmentManager fm = getFragmentManager();
        fm.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (fm.getBackStackEntryCount() == 0) {
                    setTitle("Songs");
                    mDrawerList.setItemChecked(SONGS_FRAGMENT, true);
                    CURRENT_FRAGMENT = SONGS_FRAGMENT;
                    return;
                }
                int top = fm.getBackStackEntryCount() - 1;

                FragmentManager.BackStackEntry topEntry = fm.getBackStackEntryAt(top);
                String topName = topEntry.getName();

                switch (topName) { // TODO: Sometimes this is null. Figure out why.
                    case "com.orobator.android.gramophone.Songs":
                        topName = "Songs";
                        CURRENT_FRAGMENT = SONGS_FRAGMENT;
                        break;
                    case "com.orobator.android.gramophone.Albums":
                        topName = "Albums";
                        CURRENT_FRAGMENT = ALBUMS_FRAGMENT;
                        break;
                    case "com.orobator.android.gramophone.Artists":
                        topName = "Artists";
                        CURRENT_FRAGMENT = ARTISTS_FRAGMENT;
                        break;
                    case "com.orobator.android.gramophone.Genres":
                        topName = "Genres";
                        CURRENT_FRAGMENT = GENRES_FRAGMENT;
                        break;
                    case "com.orobator.android.gramophone.Queue":
                        topName = "Queue";
                        CURRENT_FRAGMENT = QUEUE_FRAGMENT;
                        break;
                    case "com.orobator.android.gramophone.Playlists":
                        topName = "Playlists";
                        CURRENT_FRAGMENT = PLAYLISTS_FRAGMENT;
                        break;
                    default:
                }
                setTitle(topName);
                mDrawerList.setItemChecked(CURRENT_FRAGMENT, true);
            }
        });

        // Create initial view
        if (CURRENT_FRAGMENT == -1) {
            selectItem(0);
        } else {
            // A view already exists
            selectItem(CURRENT_FRAGMENT);
            setTitle(nav_items[CURRENT_FRAGMENT]);
        }

    }

    @TargetApi(19)
    private void setTranslucentStatusAndNavigationBar(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int status = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        final int navbar = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
        if (on) {
            winParams.flags |= status;
            winParams.flags |= navbar;

        } else {
            winParams.flags &= ~status;
            winParams.flags &= ~navbar;
        }
        win.setAttributes(winParams);
    }

    /**
     * Swaps fragments in the main content view *
     */
    private void selectItem(int position) {
        // Create a new fragment based on the new position
        Fragment fragment;
        String fragmentName = null;
        int oldCURRENT_FRAGMENT = CURRENT_FRAGMENT;

        switch (position) {
            case SONGS_FRAGMENT:
                fragment = new SongsFragment();
                CURRENT_FRAGMENT = SONGS_FRAGMENT;
                fragmentName = "com.orobator.android.gramophone.Songs";
                break;
            case ALBUMS_FRAGMENT:
                fragment = new AlbumsFragment();
                CURRENT_FRAGMENT = ALBUMS_FRAGMENT;
                fragmentName = "com.orobator.android.gramophone.Albums";
                break;
            case ARTISTS_FRAGMENT:
                fragment = new ArtistsFragment();
                CURRENT_FRAGMENT = ARTISTS_FRAGMENT;
                fragmentName = "com.orobator.android.gramophone.Artists";
                break;
            case GENRES_FRAGMENT:
                fragment = new GenresFragment();
                CURRENT_FRAGMENT = GENRES_FRAGMENT;
                fragmentName = "com.orobator.android.gramophone.Genres";
                break;
            case QUEUE_FRAGMENT:
                fragment = new SongQueueFragment();
                CURRENT_FRAGMENT = QUEUE_FRAGMENT;
                fragmentName = "com.orobator.android.gramophone.Queue";
                break;
            case PLAYLISTS_FRAGMENT:
                fragment = new SongsFragment();
                CURRENT_FRAGMENT = PLAYLISTS_FRAGMENT;
                fragmentName = "com.orobator.android.gramophone.Playlists";
                break;
            case SETTINGS_FRAGMENT:
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
                return;
            default:
                fragment = new SongsFragment();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        if (oldCURRENT_FRAGMENT != -1) {
            transaction.addToBackStack(fragmentName);
        }
        transaction.commit();

        // Update ActionBar title
        mTitle = nav_items[position];

        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        FragmentManager fm = getFragmentManager();

        if (fm.getBackStackEntryCount() == 0) {
            return;
        }

        String prevTopName = savedInstanceState.getString(PREVIOUS_TOP_BACK_STACK_ENTRY);
        fm.popBackStack(prevTopName, 0);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred
        mDrawerToggle.syncState();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        FragmentManager fm = getFragmentManager();

        if (fm.getBackStackEntryCount() == 0) {
            return;
        }

        int top = fm.getBackStackEntryCount() - 1;

        FragmentManager.BackStackEntry topEntry = fm.getBackStackEntryAt(top);
        String topName = topEntry.getName();

        outState.putString(PREVIOUS_TOP_BACK_STACK_ENTRY, topName);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.music, menu);

        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
//        menu.findItem(R.id.action_sort_by_artist).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled an app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.action_search:
                Toast toast1 = Toast.makeText(this, "Search", Toast.LENGTH_SHORT);
                toast1.show();
                return true;
            case R.id.action_shuffle:
                Toast toast4 = Toast.makeText(this, "Shuffle Songs", Toast.LENGTH_SHORT);
                toast4.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private class CustomListAdapter extends ArrayAdapter<String> {

        private static final int VIEW_TYPE_NAVIGATION = 0;
        private static final int VIEW_TYPE_SETTINGS = 1;
        Context mContext;
        int layoutResourceId;
        List<String> items;
        Typeface tf;

        public CustomListAdapter(Context context, int layoutResourceId, List<String> items, String FONT) {
            super(context, layoutResourceId, items);
            this.mContext = context;
            this.layoutResourceId = layoutResourceId;
            this.items = items;
            try {
                this.tf = Typeface.createFromAsset(context.getAssets(), FONT);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            switch (getItemViewType(position)) {
                case VIEW_TYPE_NAVIGATION:
                    View listItemView = inflater.inflate(R.layout.nav_drawer_list_item, null, true);
                    TextView textView = (TextView) listItemView.findViewById(R.id.nav_drawer_list_item);
                    textView.setText(items.get(position));
                    if (tf != null) {
                        textView.setTypeface(tf);
                    }
                    return listItemView;
                case VIEW_TYPE_SETTINGS:
                    listItemView = inflater.inflate(R.layout.nav_drawer_settings_list_item, null, true);
                    textView = (TextView) listItemView.findViewById(R.id.settings_textView);
                    textView.setText(items.get(position));
                    if (tf != null) {
                        textView.setTypeface(tf);
                    }
                    return listItemView;
                default:
                    return null;
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position <= 5) {
                return VIEW_TYPE_NAVIGATION;
            }

            return VIEW_TYPE_SETTINGS;
        }

    }
}
