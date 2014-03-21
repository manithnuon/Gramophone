package com.orobator.android.gramophone.model;import android.content.Context;import android.os.Bundle;import android.util.Log;import java.util.LinkedList;/** * The SongQueue class represents the globally accessible music queue. This * queue is similar to a double ended queue because additions can be made to the * front. However, it is also different from a double ended queue because the * internal order of the queue can be modified and members of the queue can get * skipped. */public class SongQueue {    private static final String TAG = "SongQueue";    private static SongDatabaseHelper sDatabaseHelper;    private static SongDatabaseHelper.SongCursor sSongCursor;    private static Context sContext;    private static LinkedList<Song> songQueue;    private static String sCollectionType;    private static String sCollectionDetail;    private static int currentSongQueuePosition = -1;    private static int minQueueLength = 25;    private static int cursorEndPosition;    private static boolean cursorMovedAfterLast = false;    private SongQueue() {        // Private constructor that prevents instantiation of the class    }    public static void clearQueue() {        songQueue = null;        sSongCursor = null;        cursorMovedAfterLast = false;        currentSongQueuePosition = -1;        // TODO: Potentially update now playing notification    }    public static String displayQueue() {        if (songQueue == null) {            return "SongQueue is null";        }        String q = "\n" + "Current pos: " + currentSongQueuePosition + "\n";        for (int i = 0; i < songQueue.size(); i++) {            String row = Integer.toString(i) + ". " + songQueue.get(i) + "\n";            q = q + row;        }        return q;    }    /**     * enqueue adds a song to the back of the queue     */    public static void enqueue(Song song) {        if (songQueue == null) {            songQueue = new LinkedList<Song>();        }        // TODO: Potentially update now playing notification        // TODO: will probably have to notify mediaplayer        songQueue.addLast(song);    }    /**     * Adds all songs in the album to the queue.     */    public static void enqueueAlbum(Album album) {        // TODO: implement this        // TODO: Potentially update now playing notification    }    /**     * Adds all songs by the artist to the queue.     */    public static void enqueueArtist(String artist) {        // TODO implement this        // TODO: Potentially update now playing notification    }    /**     * Adds all songs in the genre to the queue.     */    public static void enqueueGenre(String genre) {        // TODO: implement this        // TODO: Potentially update now playing notification    }    public static int getCurrentSongQueuePosition() {        return currentSongQueuePosition;    }    public static int getQueueSize() {        if (songQueue == null || songQueue.size() == 0) {            return 0;        }        return songQueue.size() - (currentSongQueuePosition + 1);    }    public static Song getSong(int position) {        try {            return songQueue.get(position);        } catch (IndexOutOfBoundsException e) {            return null;        }    }    public static LinkedList<Song> getSongQueue() {        return songQueue;    }    public static int getTotalSize() {        if (songQueue == null) {            return 0;        }        return songQueue.size();    }    /**     * initializeQueue(...) creates a new queue     *     * @param firstSong      The first song to be played     * @param shuffle        Whether or not the queue should be shuffled     * @param collectionType The collection type of the queue being initialized     * @param collectionInfo Describes the collection     */    public static void initializeQueue(Song firstSong, boolean shuffle,                                       String collectionType, Context context,                                       Bundle collectionInfo) {        // Get the cursor for the songs TODO implement shuffling: just use song ids to shuffle, get songs by id        sContext = context.getApplicationContext();        sDatabaseHelper = new SongDatabaseHelper(sContext);        songQueue = new LinkedList<Song>();        sCollectionType = collectionType;        // Populate the queue with minQueueLength songs before and after the firstSong        // If the collection type is album, enqueue entire album.        switch (collectionType) {            case Song.KEY_COLLECTION_TYPE_ALL:                sSongCursor = sDatabaseHelper.querySongs();                // Move cursor to song                sSongCursor.moveToFirst();                while (!sSongCursor.getSong().equals(firstSong)) {                    sSongCursor.moveToNext();                }                // Move cursor back by minQueueLength                int offset = sSongCursor.getPosition() - minQueueLength;                if (offset > 0) {                    offset = 0;                }                sSongCursor.move(-minQueueLength);                if (sSongCursor.isBeforeFirst()) {                    sSongCursor.moveToFirst();                }                for (int i = 0; i <= minQueueLength * 2 + offset; i++) {                    if (sSongCursor.isAfterLast()) {                        cursorMovedAfterLast = true;                        break;                    }                    songQueue.addLast(sSongCursor.getSong());                    sSongCursor.moveToNext();                }                currentSongQueuePosition = songQueue.indexOf(firstSong);                break;            case Song.KEY_COLLECTION_TYPE_ALBUM:                Album album = (Album) collectionInfo.getSerializable(Song.KEY_ALBUM);                sSongCursor = sDatabaseHelper.querySongsForAlbum(album);                sSongCursor.moveToFirst();                while (!sSongCursor.isAfterLast()) {                    songQueue.add(sSongCursor.getSong());                    sSongCursor.moveToNext();                }                currentSongQueuePosition = songQueue.indexOf(firstSong);                break;            case Song.KEY_COLLECTION_TYPE_ARTIST:                String artist = collectionInfo.getString(Song.KEY_ARTIST);                sCollectionDetail = artist;                sSongCursor = sDatabaseHelper.querySongsForArtist(artist);                // Move cursor to song                sSongCursor.moveToFirst();                while (!sSongCursor.getSong().equals(firstSong)) {                    sSongCursor.moveToNext();                }                // Move cursor back by minQueueLength                offset = sSongCursor.getPosition() - minQueueLength;                if (offset > 0) {                    offset = 0;                }                sSongCursor.move(-minQueueLength);                if (sSongCursor.isBeforeFirst()) {                    sSongCursor.moveToFirst();                }                for (int i = 0; i <= minQueueLength * 2 + offset; i++) {                    if (sSongCursor.isAfterLast()) {                        cursorMovedAfterLast = true;                        break;                    }                    songQueue.addLast(sSongCursor.getSong());                    sSongCursor.moveToNext();                }                currentSongQueuePosition = songQueue.indexOf(firstSong);                break;            case Song.KEY_COLLECTION_TYPE_GENRE:                String genre = collectionInfo.getString(Song.KEY_GENRE);                sCollectionDetail = genre;                sSongCursor = sDatabaseHelper.querySongsForGenre(genre);                // Move cursor to song                sSongCursor.moveToFirst();                while (!sSongCursor.getSong().equals(firstSong)) {                    sSongCursor.moveToNext();                }                // Move cursor back by minQueueLength                offset = sSongCursor.getPosition() - minQueueLength;                if (offset > 0) {                    offset = 0;                }                sSongCursor.move(-minQueueLength);                if (sSongCursor.isBeforeFirst()) {                    sSongCursor.moveToFirst();                }                for (int i = 0; i <= minQueueLength * 2 + offset; i++) {                    if (sSongCursor.isAfterLast()) {                        cursorMovedAfterLast = true;                        break;                    }                    songQueue.addLast(sSongCursor.getSong());                    sSongCursor.moveToNext();                }                currentSongQueuePosition = songQueue.indexOf(firstSong);                break;            default:                sSongCursor = null; // Should never happen        }        cursorEndPosition = sSongCursor.getPosition();    }    /**     * moveToPosition(int) moves the queue's current position to the specified     * position. The queue is populated with songs from the initial collection     * if the queue size is less than minQueue. If all the songs from the     * initial collection have been enqueued, the queue is not modified.     */    public static void moveToPosition(int position) {        currentSongQueuePosition = position;        // All the songs have been enqueued        if (cursorMovedAfterLast) {            return;        }        // TODO: What if the user is moving backwards in a collection (no shuffle)?        // Pull in songs from initial collection        if (getQueueSize() < minQueueLength) {            switch (sCollectionType) {                case Song.KEY_COLLECTION_TYPE_ALL:                    sSongCursor = sDatabaseHelper.querySongs();                    sSongCursor.moveToPosition(cursorEndPosition);                    if (sSongCursor.isAfterLast()) {                        // TODO: Could optimize this by just saving the cursor length and not querying database                        cursorMovedAfterLast = true;                        return;                    }                    enqueue(sSongCursor.getSong());                    cursorEndPosition++;                    break;                case Song.KEY_COLLECTION_TYPE_ALBUM:                    // The entire album was enqueued. Do nothing                    break;                case Song.KEY_COLLECTION_TYPE_ARTIST:                    sSongCursor = sDatabaseHelper.querySongsForArtist(sCollectionDetail);                    sSongCursor.moveToPosition(cursorEndPosition);                    if (sSongCursor.isAfterLast()) {                        cursorMovedAfterLast = true;                        return;                    }                    enqueue(sSongCursor.getSong());                    cursorEndPosition++;                    break;                case Song.KEY_COLLECTION_TYPE_GENRE:                    sSongCursor = sDatabaseHelper.querySongsForGenre(sCollectionDetail);                    sSongCursor.moveToPosition(cursorEndPosition);                    if (sSongCursor.isAfterLast()) {                        cursorMovedAfterLast = true;                        return;                    }                    enqueue(sSongCursor.getSong());                    cursorEndPosition++;                    break;                default:                    assert false;                    break; // Should never happen            }        }    }    /**     * playNext(Song) adds a song to the front song queue     */    public static void playNext(Song song) {        Log.d(TAG, "playNext(" + song.toString() + ")");        if (songQueue == null) {            Log.d(TAG, "in playNext(), queue was null");            songQueue = new LinkedList<Song>();        }        songQueue.add(currentSongQueuePosition + 1, song);        Log.d(TAG, "New songQueue length is " + songQueue.size());        // TODO: Potentially update now playing notification        // TODO: will probably have to notify mediaplayer    }    /**     * removeFromQueue removes the song with id songId from the queue     */    public static void removeFromQueue(long songId) {        // TODO implement this, update metadata        // TODO: Potentially update now playing notification    }}