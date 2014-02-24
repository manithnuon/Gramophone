package com.orobator.android.gramophone.view.fragments;import android.app.Fragment;import android.content.Intent;import android.graphics.PorterDuff;import android.graphics.drawable.ColorDrawable;import android.graphics.drawable.Drawable;import android.net.Uri;import android.os.Build;import android.os.Bundle;import android.os.Handler;import android.support.v4.app.FragmentActivity;import android.support.v4.app.FragmentManager;import android.support.v4.app.FragmentStatePagerAdapter;import android.support.v4.view.ViewPager;import android.util.DisplayMetrics;import android.view.LayoutInflater;import android.view.View;import android.view.ViewGroup;import android.view.ViewTreeObserver;import android.widget.RelativeLayout;import android.widget.SeekBar;import android.widget.TextView;import com.orobator.android.gramophone.R;import com.orobator.android.gramophone.controller.services.MusicPlayerService;import com.orobator.android.gramophone.model.Album;import com.orobator.android.gramophone.model.Song;import com.orobator.android.gramophone.model.SongDatabaseHelper;import com.readystatesoftware.systembartint.SystemBarTintManager;import java.io.File;public class NowPlayingFragment extends Fragment {    private static final String TAG = "NowPlayingFragment";    private Song mSong;    private MusicPlayerService.SeekBarUpdaterThread mSeekBarUpdaterThread;    private ViewPager mViewPager;    private SongDatabaseHelper.SongCursor mSongCursor;    private SystemBarTintManager mTintManager;    @Override    public void onCreate(Bundle savedInstanceState) {        super.onCreate(savedInstanceState);        mSong = (Song) getActivity().getIntent().getSerializableExtra(Song.KEY_SONG);        mTintManager = new SystemBarTintManager(getActivity());        playSong(mSong);        initializeSongCursor(); // TODO: Depend on SongQueue for this        setActionBarColors(mSong);        initializeSeekBarUpdaterThread();    }    private void playSong(Song song) {        Uri songUri = Uri.fromFile(new File(song.getFilePath()));        Intent intent = new Intent(MusicPlayerService.ACTION_PLAY, songUri, getActivity().getApplicationContext(), MusicPlayerService.class);        intent.putExtra(Song.KEY_SONG, song);        getActivity().startService(intent);    }    private void initializeSongCursor() {        SongDatabaseHelper helper = new SongDatabaseHelper(getActivity());        String collectionType = getActivity().getIntent().getStringExtra(Song.KEY_SONG_COLLECTION_TYPE);        if (collectionType.equals(Song.KEY_COLLECTION_TYPE_ALL)) {            mSongCursor = helper.querySongs();        } else if (collectionType.equals(Song.KEY_COLLECTION_TYPE_ALBUMS)) {            String albumName = getActivity().getIntent().getStringExtra(Song.KEY_ALBUM);            String albumArtist = getActivity().getIntent().getStringExtra(Song.KEY_ALBUM_ARTIST);            Album album = new Album(albumName, albumArtist);            mSongCursor = helper.querySongsForAlbum(album);        } else if (collectionType.equals(Song.KEY_COLLECTION_TYPE_GENRES)) {            String genre = getActivity().getIntent().getStringExtra(Song.KEY_GENRE);            mSongCursor = helper.querySongsForGenre(genre);        }    }    @Override    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {        View view = inflater.inflate(R.layout.fragment_now_playing, parent, false);        view.setBackgroundColor(mSong.getBackgroundColor());        final TextView songTitleTextView = (TextView) view.findViewById(R.id.nowPlayingTitle_textView);        songTitleTextView.setText(mSong.getTitle());        songTitleTextView.setTextColor(mSong.getPrimaryColor());        songTitleTextView.setSelected(true); // Enables marquee scrolling        final TextView artistAlbumTextView = (TextView) view.findViewById(R.id.nowPlayingArtistAlbum_textView);        artistAlbumTextView.setText(mSong.getArtist() + " - " + mSong.getAlbum());        artistAlbumTextView.setTextColor(mSong.getSecondaryColor());        artistAlbumTextView.setSelected(true); // Enables marquee scrolling        final TextView timePlayedTextView = (TextView) view.findViewById(R.id.nowPlayingTimePlayed_textView);        timePlayedTextView.setTextColor(mSong.getPrimaryColor());        final TextView timeLeftTextView = (TextView) view.findViewById(R.id.nowPlayingTimeLeft_textView);        timeLeftTextView.setText(mSong.displayTime(mSong.getDuration(), false));        timeLeftTextView.setTextColor(mSong.getPrimaryColor());        final SeekBar songSeekbar = (SeekBar) view.findViewById(R.id.nowPlaying_seekBar);        songSeekbar.setMax((int) mSong.getDuration());        songSeekbar.getProgressDrawable().setColorFilter(mSong.getSecondaryColor(), PorterDuff.Mode.SRC_ATOP);        songSeekbar.getThumb().setColorFilter(mSong.getPrimaryColor(), PorterDuff.Mode.SRC_ATOP);        songSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {            @Override            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {                timePlayedTextView.setText(mSong.displayTime(progress, false));                timeLeftTextView.setText(mSong.displayTime((mSong.getDuration() - progress), false));                if (fromUser) {                    Intent seekToIntent = new Intent(MusicPlayerService.ACTION_SEEK_TO);                    seekToIntent.setClass(getActivity().getApplicationContext(), MusicPlayerService.class);                    seekToIntent.putExtra(MusicPlayerService.KEY_SEEK_TO, progress);                    getActivity().startService(seekToIntent);                }            }            @Override            public void onStartTrackingTouch(SeekBar seekBar) {            }            @Override            public void onStopTrackingTouch(SeekBar seekBar) {            }        });        mSeekBarUpdaterThread.setSeekBar(songSeekbar);        mSeekBarUpdaterThread.setListener(new MusicPlayerService.SeekBarUpdaterThread.Listener() {            @Override            public void onUpdateReceived(SeekBar seekBar, int progress) {                seekBar.setProgress(progress);            }        });        mViewPager = (ViewPager) view.findViewById(R.id.nowPlayingAlbum_viewPager);        mViewPager.setOffscreenPageLimit(3);        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {            SystemBarTintManager.SystemBarConfig config = mTintManager.getConfig();            int left = mViewPager.getPaddingLeft();            int top = config.getStatusBarHeight() + config.getActionBarHeight();            int right = mViewPager.getPaddingRight();            int bottom = mViewPager.getPaddingBottom();            mViewPager.setPadding(left, top, right, bottom);        }        FragmentManager fm = ((FragmentActivity) getActivity()).getSupportFragmentManager();        // TODO Make artist/title scroll when song is playing        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {            @Override            public android.support.v4.app.Fragment getItem(int position) {                mSongCursor.moveToPosition(position);                Song songNext = mSongCursor.getSong();                Bundle args = new Bundle();                args.putSerializable(Song.KEY_SONG, songNext);                android.support.v4.app.Fragment fragment = new NowPlayingAlbumArtFragment();                fragment.setArguments(args);                return fragment;            }            @Override            public int getCount() {                return mSongCursor.getCount();            }        });        int cursorPosition = getActivity().getIntent().getIntExtra(Song.KEY_CURSOR_POSITION, 0);        mViewPager.setCurrentItem(cursorPosition);        final Fragment fragment = this;        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {            @Override            public void onPageScrolled(int i, float v, int i2) {                // TODO: Fade colors to next color            }            @Override            public void onPageSelected(int position) {                mSongCursor.moveToPosition(position);                Song songCurrent = mSongCursor.getSong();                mSong = songCurrent;                setActionBarColors(mSong);                fragment.getView().setBackgroundColor(mSong.getBackgroundColor());                // Update colors and TextViews to match the currently playing song.                songTitleTextView.setText(mSong.getTitle());                songTitleTextView.setTextColor(mSong.getPrimaryColor());                artistAlbumTextView.setText(mSong.getArtist() + " - " + mSong.getAlbum());                artistAlbumTextView.setTextColor(mSong.getSecondaryColor());                timePlayedTextView.setTextColor(mSong.getPrimaryColor());                timePlayedTextView.setText(getString(R.string.time_zero));                timeLeftTextView.setText(mSong.displayTime(mSong.getDuration(), false));                timeLeftTextView.setTextColor(mSong.getPrimaryColor());                songSeekbar.getProgressDrawable().setColorFilter(mSong.getSecondaryColor(), PorterDuff.Mode.SRC_ATOP);                songSeekbar.getThumb().setColorFilter(mSong.getPrimaryColor(), PorterDuff.Mode.SRC_ATOP);                mTintManager.setStatusBarTintColor(mSong.getBackgroundColor());            }            @Override            public void onPageScrollStateChanged(int position) {            }        });        return view;    }    @Override    public void onDestroyView() {        super.onDestroyView();        // Makes sure handler thread dies when fragment is rotated        mSeekBarUpdaterThread.quit();    }    private void setActionBarColors(Song song) {        getActivity().setTitle("");        int backgroundColor = song.getBackgroundColor();        getActivity().getActionBar().setBackgroundDrawable(new ColorDrawable(backgroundColor));        final Drawable launcherIcon = getResources().getDrawable(R.drawable.ic_launcher);        launcherIcon.setColorFilter(song.getPrimaryColor(), PorterDuff.Mode.SRC_ATOP);        getActivity().getActionBar().setIcon(launcherIcon);        final Drawable upButton = getResources().getDrawable(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_holo_light);        upButton.setAlpha(0xff);        upButton.setColorFilter(song.getSecondaryColor(), PorterDuff.Mode.SRC_ATOP);        getActivity().getActionBar().setHomeAsUpIndicator(upButton);    }    private void initializeSeekBarUpdaterThread() {        mSeekBarUpdaterThread = new MusicPlayerService.SeekBarUpdaterThread(new Handler());        mSeekBarUpdaterThread.start();        mSeekBarUpdaterThread.getLooper();    }    @Override    public void onResume() {        super.onResume();        // Wrap content for a view pager doesn't make sense, so width has to be        // set when layout is being created        ViewTreeObserver observer = mViewPager.getViewTreeObserver();        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {            @Override            public void onGlobalLayout() {                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(                        RelativeLayout.LayoutParams.MATCH_PARENT,                        RelativeLayout.LayoutParams.WRAP_CONTENT);                DisplayMetrics metrics = new DisplayMetrics();                getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);                int viewPagerWidth = metrics.widthPixels;                SystemBarTintManager.SystemBarConfig config = mTintManager.getConfig();                int viewPagerHeight = metrics.widthPixels + config.getActionBarHeight() + config.getStatusBarHeight();                layoutParams.width = viewPagerWidth;                layoutParams.height = viewPagerHeight;                mViewPager.setLayoutParams(layoutParams);                mViewPager.getViewTreeObserver().removeOnGlobalLayoutListener(this);            }        });    }}