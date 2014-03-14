package com.orobator.android.gramophone.model;import android.content.Context;import android.os.Bundle;import java.util.LinkedList;/** * The SongQueue class represents the globally accessible music queue. This * queue is similar to a double ended queue because additions can be made to the * front. However, it is also different from a double ended queue because the * internal order of the queue can be modified and members of the queue can get * skipped. */public class SongQueue {    private static final String TAG = "SongQueue";    static SongDatabaseHelper.SongCursor sSongCursor;    static Context sContext;    private static LinkedList<Song> songQueue;    private static int currentSongPosition = 0;    private SongQueue() {        // Private constructor that prevents instantiation of the class    }    public static LinkedList<Song> getSongQueue() {        return songQueue;    }    /**     * initializeQueue(...) creates a new queue     *     * @param firstSong      The first song to be played     * @param shuffle        Whether or not the queue should be shuffled     * @param collectionType The collection type of the queue being initialized     * @param collectionInfo Describes the collection     */    public static void initializeQueue(Song firstSong, boolean shuffle,                                       String collectionType, Context context,                                       Bundle collectionInfo) {        // Get the cursor for the songs TODO implement shuffling        sContext = context.getApplicationContext();        SongDatabaseHelper helper = new SongDatabaseHelper(sContext);        songQueue = new LinkedList<Song>();        // Populate the queue with minQueueLength songs before and after the firstSong        // If the collection type is album, enqueue entire album.        int minQueueLength = 25;        switch (collectionType) {            case Song.KEY_COLLECTION_TYPE_ALL:                sSongCursor = helper.querySongs();                // Move cursor to song                sSongCursor.moveToFirst();                while (!sSongCursor.getSong().equals(firstSong)) {                    sSongCursor.moveToNext();                }                // Move cursor back by minQueueLength                int offset = sSongCursor.getPosition() - minQueueLength;                if (offset > 0) {                    offset = 0;                }                sSongCursor.move(-minQueueLength);                if (sSongCursor.isBeforeFirst()) {                    sSongCursor.moveToFirst();                }                for (int i = 0; i <= minQueueLength * 2 + offset; i++) {                    if (sSongCursor.isAfterLast()) {                        break;                    }                    songQueue.addLast(sSongCursor.getSong());                    sSongCursor.moveToNext();                }                currentSongPosition = songQueue.indexOf(firstSong);                break;            case Song.KEY_COLLECTION_TYPE_ALBUM:                Album album = (Album) collectionInfo.getSerializable(Song.KEY_ALBUM);                sSongCursor = helper.querySongsForAlbum(album);                sSongCursor.moveToFirst();                while (!sSongCursor.isAfterLast()) {                    songQueue.add(sSongCursor.getSong());                    sSongCursor.moveToNext();                }                currentSongPosition = songQueue.indexOf(firstSong);                break;            case Song.KEY_COLLECTION_TYPE_ARTIST:                String artist = collectionInfo.getString(Song.KEY_ARTIST);                sSongCursor = helper.querySongsForArtist(artist);                // Move cursor to song                sSongCursor.moveToFirst();                while (!sSongCursor.getSong().equals(firstSong)) {                    sSongCursor.moveToNext();                }                // Move cursor back by minQueueLength                offset = sSongCursor.getPosition() - minQueueLength;                if (offset > 0) {                    offset = 0;                }                sSongCursor.move(-minQueueLength);                if (sSongCursor.isBeforeFirst()) {                    sSongCursor.moveToFirst();                }                for (int i = 0; i <= minQueueLength * 2 + offset; i++) {                    if (sSongCursor.isAfterLast()) {                        break;                    }                    songQueue.addLast(sSongCursor.getSong());                    sSongCursor.moveToNext();                }                currentSongPosition = songQueue.indexOf(firstSong);                break;            case Song.KEY_COLLECTION_TYPE_GENRE:                String genre = collectionInfo.getString(Song.KEY_GENRE);                sSongCursor = helper.querySongsForGenre(genre);                // Move cursor to song                sSongCursor.moveToFirst();                while (!sSongCursor.getSong().equals(firstSong)) {                    sSongCursor.moveToNext();                }                // Move cursor back by minQueueLength                offset = sSongCursor.getPosition() - minQueueLength;                if (offset > 0) {                    offset = 0;                }                sSongCursor.move(-minQueueLength);                if (sSongCursor.isBeforeFirst()) {                    sSongCursor.moveToFirst();                }                for (int i = 0; i <= minQueueLength * 2 + offset; i++) {                    if (sSongCursor.isAfterLast()) {                        break;                    }                    songQueue.addLast(sSongCursor.getSong());                    sSongCursor.moveToNext();                }                currentSongPosition = songQueue.indexOf(firstSong);                break;            default:                sSongCursor = null; // Should never happen        }    }    public static void moveToPosition(int position) {        currentSongPosition = position;    }    public static Song getSong(int position) {        try {            return songQueue.get(position);        } catch (IndexOutOfBoundsException e) {            return null;        }    }    public static String displayQueue() {        if (songQueue == null) {            return null;        }        String q = "\n" + "Current pos: " + currentSongPosition + "\n";        for (int i = 0; i < songQueue.size(); i++) {            String row = Integer.toString(i) + ". " + songQueue.get(i) + "\n";            q = q + row;        }        return q;    }    public static int getCurrentSongPosition() {        return currentSongPosition;    }    public static int getQueueSize() {        if (songQueue == null || songQueue.size() == 0) {            return 0;        }        return songQueue.size() - (currentSongPosition + 1);    }    public static int getTotalSize() {        if (songQueue == null) {            return 0;        }        return songQueue.size();    }    /**     * removeFromQueue removes the song with id songId from the queue     */    public static void removeFromQueue(long songId) {        // TODO implement this, update metadata    }    /**     * enqueue adds a song with id songId to the back of the queue     */    public static void enqueue(Song song) {        songQueue.addLast(song);    }    /**     * playNext(Song) adds a song to the front song queue     */    public static void playNext(Song song) {        songQueue.add(currentSongPosition + 1, song);    }    /**     * Adds all songs by the artist to the queue.     */    public static void enqueueArtist(String artist) {        // TODO implement this    }    /**     * Adds all songs in the album to the queue.     */    public static void enqueueAlbum(Album album) {        // TODO: implement this    }    /**     * Adds all songs in the genre to the queue.     */    public static void enqueueGenre(String genre) {        // TODO: implement this    }    /**     * skipToPosition(position) makes position the first place in the queue and     * discards all others before it.     */    public static void skipToPosition(int position) {        // TODO implement this    }}