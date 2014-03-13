package com.orobator.android.gramophone.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;

import java.io.Serializable;
import java.text.DecimalFormat;

/**
 * Represents a single song on a device.
 */
public class Song implements Serializable {
    public static final long serialVersionUID = 0L;
    public static final String KEY_SONG = "song";
    public static final String KEY_CURSOR_POSITION = "position";

    public static final String KEY_SONG_COLLECTION_TYPE = "song_collection_type";
    public static final String KEY_COLLECTION_TYPE_ALL = "collection_type_all_songs";
    public static final String KEY_COLLECTION_TYPE_ALBUM = "collection_type_album_songs";
    public static final String KEY_COLLECTION_TYPE_ARTIST = "collection_type_artist_songs";
    public static final String KEY_COLLECTION_TYPE_GENRE = "collection_type_genre_songs";

    public static final String KEY_ALBUM = "song_album";
    public static final String KEY_ALBUM_ARTIST = "song_album_artist";
    public static final String KEY_ARTIST = "artist";
    public static final String KEY_FILE_PATH = "file_path";
    public static final String KEY_GENRE = "song_genre";

    /**
     * Default colors for Now Playing screen
     */
    public static final int DEFAULT_BACKGROUND_COLOR = 0xff22596d;
    public static final int DEFAULT_PRIMARY_COLOR = -268633;
    public static final int DEFAULT_SECONDARY_COLOR = -3826113;
    public static final int DEFAULT_DETAIL_COLOR = -4479338;

    private int hasArtwork;
    private int discNumber = 0;
    private int discCount = 0;
    private int trackNumber = 0; // Track number on album
    private int trackCount = 0;  // Total tracks on album
    private int year = 0;
    private int bitRate = 0; // Bits/second
    private int sampleRate = 0;
    private int playCount = 0;
    private int skipCount = 0;
    private int skipOnShuffle = 0;
    private int backgroundColor;
    private int primaryColor;
    private int secondaryColor;
    private int detailColor;
    private int rating = 0; // Has max of 5
    private long songID = 0;
    private long size = 0; // Size of song in bytes
    private long dateModified; // Seconds since Jan 1, 1970
    private long lastPlayed; // Seconds since Jan 1, 1970
    private long duration; // Time in milliseconds
    private String title;
    private String artist;
    private String albumArtist;
    private String album;
    private String equalizerPreset;
    private String genre;
    private String compilationStatus;
    private String composer;
    private String writer;
    private String filePath;

    public Song() {
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getPrimaryColor() {
        return primaryColor;
    }

    public void setPrimaryColor(int primaryColor) {
        this.primaryColor = primaryColor;
    }

    public int getSecondaryColor() {
        return secondaryColor;
    }

    public void setSecondaryColor(int secondaryColor) {
        this.secondaryColor = secondaryColor;
    }

    public int getDetailColor() {
        return detailColor;
    }

    public void setDetailColor(int detailColor) {
        this.detailColor = detailColor;
    }

    public byte[] getArtworkByteArray() {
        if (!hasArtwork()) {
            return null;
        }

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(filePath);

        return retriever.getEmbeddedPicture();
    }

    public boolean hasArtwork() {
        return hasArtwork == 1;
    }

    public Bitmap getArtwork() {
        if (!hasArtwork()) {
            return null;
        }

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(filePath);

        byte albumBytes[] = retriever.getEmbeddedPicture();
        return BitmapFactory.decodeByteArray(albumBytes, 0, albumBytes.length);
    }

    public boolean skipOnShuffle() {
        return skipOnShuffle == 1;
    }

    public void setSkipOnShuffle(boolean skip) {
        skipOnShuffle = skip ? 1 : 0;
    }

    public long getLastPlayed() {
        return lastPlayed;
    }

    public void setLastPlayed(long lastPlayed) {
        this.lastPlayed = lastPlayed;
    }

    public String getEqualizerPreset() {
        return equalizerPreset;
    }

    public void setEqualizerPreset(String equalizerPreset) {
        this.equalizerPreset = equalizerPreset;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void resetSkipCount() {
        skipCount = 0;
    }

    public void incrSkipCount() {
        skipCount++;
    }

    public int getSkipCount() {
        return skipCount;
    }

    public void setSkipCount(int skipCount) {
        this.skipCount = skipCount;
    }

    public int getPlayCount() {
        return playCount;
    }

    public void setPlayCount(int playCount) {
        this.playCount = playCount;
    }

    public void incrPlayCount() {
        playCount++;
    }

    public void resetPlayCount() {
        playCount = 0;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getComposer() {
        return composer;
    }

    public void setComposer(String composer) {
        this.composer = composer;
    }

    public String getCompilationStatus() {
        return compilationStatus;
    }

    public void setCompilationStatus(String compilationStatus) {
        this.compilationStatus = compilationStatus;
    }

    public long getSongID() {
        return songID;
    }

    public void setSongID(long songID) {
        this.songID = songID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbumArtist() {
        return albumArtist;
    }

    public void setAlbumArtist(String albumArtist) {
        this.albumArtist = albumArtist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getDiscNumber() {
        return discNumber;
    }

    public void setDiscNumber(int discNumber) {
        this.discNumber = discNumber;
    }

    public int getDiscCount() {
        return discCount;
    }

    public void setDiscCount(int discCount) {
        this.discCount = discCount;
    }

    public int getTrackNumber() {
        return trackNumber;
    }

    public void setTrackNumber(int trackNumber) {
        this.trackNumber = trackNumber;
    }

    public int getTrackCount() {
        return trackCount;
    }

    public void setTrackCount(int trackCount) {
        this.trackCount = trackCount;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public long getDateModified() {
        return dateModified;
    }

    public void setDateModified(long dateModified) {
        this.dateModified = dateModified;
    }

    public int getBitRate() {
        return bitRate;
    }

    public void setBitRate(int bitRate) {
        this.bitRate = bitRate;
    }

    public int getSampleRate() {
        return sampleRate;
    }

    public void setSampleRate(int sampleRate) {
        this.sampleRate = sampleRate;
    }

    public void setHasArtwork(boolean hasArtwork) {
        this.hasArtwork = hasArtwork ? 1 : 0;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String toString() {
        if (title == null || artist == null) {
            return filePath;
        }

        return title + " - " + artist;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Song)) {
            return false;
        }

        Song other = (Song) o;

        return songID == other.getSongID();
    }

    @Override
    public int hashCode() {
        int hash = 1;
        hash = (hash * 17) + (int) songID;
        hash = (hash * 31) + title.hashCode();
        hash = (hash * 19) + artist.hashCode();
        hash = (hash * 53) + album.hashCode();
        return hash;
    }

    /**
     * displaySize() returns a string detailing how the file size of a song in
     * kilobytes, megabytes, or gigabytes, whichever is most appropriate
     */
    public String displaySize() {
        double kb = 1000;
        double mb = kb * 1000;
        double gb = mb * 1000;

        DecimalFormat df = new DecimalFormat("########.##");

        if (size % gb != size) {
            return df.format(size / gb) + " GB";
        } else if (size % mb != size) {
            return df.format(size / mb) + " MB";
        } else {
            return df.format(size / kb) + " KB";
        }
    }

    /**
     * displayTime(long, boolean) displays the time in a formatted string
     *
     * @param duration the duration of a song in milliseconds
     * @param precise  if true, the result string will contain milliseconds
     *
     * @return a string in the format of hh:mm:ss(.mmm). Leading zeros are not
     * shown
     */
    public String displayTime(long duration, boolean precise) {
        if (!precise) {
            // Round up
            duration += 500;
        }

        final long totalSeconds = duration / 1000;
        int minute = 60;
        int hour = minute * 60;

        String hours = "";
        String minutes;
        String seconds;
        String milliseconds = "";

        long displayHours = totalSeconds / hour;
        if (displayHours != 0) {
            hours = Long.toString(displayHours) + ":";
        }

        long displayMinutes = (totalSeconds - (displayHours * hour)) / minute;
        if (displayMinutes != 0) {
            minutes = Long.toString(displayMinutes);
            if (displayMinutes < 10) {
                minutes = "0" + minutes;
            }
        } else {
            minutes = "0";
        }

        long displaySeconds = totalSeconds - (displayHours * hour) - (displayMinutes * minute);
        if (displaySeconds != 0) {
            seconds = Long.toString(displaySeconds);
            if (displaySeconds < 10) {
                seconds = "0" + seconds;
            }
        } else {
            seconds = "00";
        }

        if (precise) {
            milliseconds = "." + Long.toString(duration % 1000);
        }

        return hours + minutes + ":" + seconds + milliseconds;
    }
}
