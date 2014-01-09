package com.orobator.android.gramophone.view.fragments;import android.content.Intent;import android.graphics.Bitmap;import android.graphics.PorterDuff;import android.graphics.drawable.BitmapDrawable;import android.os.Bundle;import android.os.Handler;import android.support.v4.app.Fragment;import android.util.DisplayMetrics;import android.util.Log;import android.view.LayoutInflater;import android.view.View;import android.view.ViewGroup;import android.widget.ImageButton;import android.widget.SeekBar;import android.widget.TextView;import android.widget.Toast;import com.orobator.android.gramophone.R;import com.orobator.android.gramophone.controller.services.MusicPlayerService;import com.orobator.android.gramophone.model.Song;public class NowPlayingFragment extends Fragment {    private static final String TAG = "NowPlayingFragment";    private Bitmap mAlbumCover;    private Song mSong;    private MusicPlayerService.SeekBarUpdaterThread mSeekBarUpdaterThread;    @Override    public void onCreate(Bundle savedInstanceState) {        super.onCreate(savedInstanceState);        Bundle args = getArguments();        mSong = (Song) args.getSerializable(Song.KEY_SONG);        mSeekBarUpdaterThread = new MusicPlayerService.SeekBarUpdaterThread(new Handler());        mSeekBarUpdaterThread.start();        mSeekBarUpdaterThread.getLooper();    }    @Override    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {        View view = inflater.inflate(R.layout.fragment_now_playing, parent, false);        DisplayMetrics metrics = new DisplayMetrics();        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);        if (view != null) {            view.setBackgroundColor(mSong.getBackgroundColor());        }        ImageButton albumArtImageButton = (ImageButton) view.findViewById(R.id.nowPlayingAlbumArt_imageView);        albumArtImageButton.setMinimumHeight(metrics.widthPixels);        albumArtImageButton.setMaxHeight(metrics.widthPixels);        albumArtImageButton.setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View v) {                Intent playbackToggleIntent = new Intent(MusicPlayerService.ACTION_TOGGLE_PLAYBACK);                playbackToggleIntent.setClass(getActivity().getApplicationContext(), MusicPlayerService.class);                getActivity().startService(playbackToggleIntent);            }        });        if (mSong.hasArtwork()) {            mAlbumCover = mSong.getArtwork();        } else {            Log.d(TAG, mSong.getTitle() + " - " + mSong.getArtist() + " has no album art");            mAlbumCover = ((BitmapDrawable) getResources().getDrawable(R.drawable.replacement_album_art)).getBitmap();        }        double scaleFactor = (metrics.widthPixels * 1.0) / mAlbumCover.getHeight();        Double scaledWidth = new Double(scaleFactor * mAlbumCover.getWidth());        int dstHeight = metrics.widthPixels;        int dstWidth = scaledWidth.intValue();        mAlbumCover = Bitmap.createScaledBitmap(mAlbumCover, dstWidth, dstHeight, true);        albumArtImageButton.setImageBitmap(mAlbumCover);        TextView songTitleTextView = (TextView) view.findViewById(R.id.nowPlayingTitle_textView);        songTitleTextView.setText(mSong.getTitle());        songTitleTextView.setTextColor(mSong.getPrimaryColor());        songTitleTextView.setSelected(true);        TextView artistAlbumTextView = (TextView) view.findViewById(R.id.nowPlayingArtistAlbum_textView);        artistAlbumTextView.setText(mSong.getArtist() + " - " + mSong.getAlbum());        artistAlbumTextView.setTextColor(mSong.getSecondaryColor());        artistAlbumTextView.setSelected(true);        final TextView timePlayedTextView = (TextView) view.findViewById(R.id.nowPlayingTimePlayed_textView);        timePlayedTextView.setTextColor(mSong.getPrimaryColor());        timePlayedTextView.setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View v) {                Toast toast = Toast.makeText(getActivity(), "timePlayedTextView", Toast.LENGTH_SHORT);                toast.show();            }        });        final TextView timeLeftTextView = (TextView) view.findViewById(R.id.nowPlayingTimeLeft_textView);        timeLeftTextView.setText(mSong.displayTime(mSong.getDuration(), false));        timeLeftTextView.setTextColor(mSong.getPrimaryColor());        timeLeftTextView.setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View v) {                Toast toast = Toast.makeText(getActivity(), "timeLeftTextView", Toast.LENGTH_SHORT);                toast.show();            }        });        final SeekBar songSeekbar = (SeekBar) view.findViewById(R.id.nowPlaying_seekBar);        songSeekbar.setMax((int) mSong.getDuration());        songSeekbar.getProgressDrawable().setColorFilter(mSong.getSecondaryColor(), PorterDuff.Mode.SRC_ATOP);        songSeekbar.getThumb().setColorFilter(mSong.getPrimaryColor(), PorterDuff.Mode.SRC_ATOP);        songSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {            @Override            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {                timePlayedTextView.setText(mSong.displayTime(progress, false));                timeLeftTextView.setText(mSong.displayTime((mSong.getDuration() - progress), false));                if (fromUser) {                    Intent seekToIntent = new Intent(MusicPlayerService.ACTION_SEEK_TO);                    seekToIntent.setClass(getActivity().getApplicationContext(), MusicPlayerService.class);                    seekToIntent.putExtra(MusicPlayerService.KEY_SEEK_TO, (int) progress);                    getActivity().startService(seekToIntent);                }            }            @Override            public void onStartTrackingTouch(SeekBar seekBar) {            }            @Override            public void onStopTrackingTouch(SeekBar seekBar) {            }        });        mSeekBarUpdaterThread.setSeekBar(songSeekbar);        mSeekBarUpdaterThread.setListener(new MusicPlayerService.SeekBarUpdaterThread.Listener() {            @Override            public void onUpdateReceived(SeekBar seekBar, int progress) {                if (isVisible()) {                    seekBar.setProgress(progress);                }            }        });        return view;    }    @Override    public void onDestroyView() {        super.onDestroyView();        // Makes sure handler thread dies when fragment is rotated        mSeekBarUpdaterThread.quit();    }    @Override    public void onStart() {        super.onStart();        if (MusicPlayerService.getCurrentSongId() != -1                && mSong.getSongID() == MusicPlayerService.getCurrentSongId()) {            setUserVisibleHint(true);        } else {            setUserVisibleHint(false);        }    }}