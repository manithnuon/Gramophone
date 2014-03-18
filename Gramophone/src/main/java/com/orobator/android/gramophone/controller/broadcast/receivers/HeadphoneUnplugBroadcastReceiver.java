package com.orobator.android.gramophone.controller.broadcast.receivers;import android.content.BroadcastReceiver;import android.content.Context;import android.content.Intent;import android.content.SharedPreferences;import android.media.AudioManager;import android.preference.PreferenceManager;import com.orobator.android.gramophone.controller.services.MusicPlayerService;public class HeadphoneUnplugBroadcastReceiver extends BroadcastReceiver {    @Override    public void onReceive(Context context, Intent intent) {        if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intent.getAction())) {            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);            boolean pauseOnUnplug = preferences.getBoolean("pref_key_pause_music_when_unplugging_headphones", true);            if (pauseOnUnplug) {                Intent pauseIntent = new Intent(context.getApplicationContext(), MusicPlayerService.class);                pauseIntent.setAction(MusicPlayerService.ACTION_PAUSE);                context.startService(pauseIntent);            }        }    }}