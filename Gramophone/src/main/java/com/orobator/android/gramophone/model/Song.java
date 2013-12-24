package com.orobator.android.gramophone.model;

import java.io.Serializable;
import java.text.DecimalFormat;

public class Song implements Serializable {
    private int hasArtwork;
    private int discNumber = 0;
    private int discTotal = 0;
    private int trackNumber = 0;
    private int trackCount = 0;
    private int year = 0;
    private int numTracks = 0; // Number of tracks on the album
    private int bitRate = 0; // bits/second
    private int sampleRate = 0;
    private int playCount = 0;
    private int skipCount = 0;
    private int skipOnShuffle = 0;
    private int rating = 0;
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
    private String fileName;
    private String location;

    public Song() {
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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

    public int getNumTracks() {
        return numTracks;
    }

    public void setNumTracks(int numTracks) {
        this.numTracks = numTracks;
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

    public int getDiscTotal() {
        return discTotal;
    }

    public void setDiscTotal(int discTotal) {
        this.discTotal = discTotal;
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

    public boolean hasArtwork() {
        return hasArtwork == 1;
    }

    public void setHasArtwork(boolean hasArtwork) {
        this.hasArtwork = hasArtwork ? 1 : 0;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        if (title == null || artist == null) {
            return fileName;
        }

        return title + " - " + artist;
    }

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
     * displayTime() displays the time in a formatted string
     *
     * @param duration the duration of a song in milliseconds
     * @param precise  if true, the result string will contain milliseconds
     * @return a string in the format of hh:mm:ss(.mmm). Leading zeros are not shown
     */
    public String displayTime(long duration, boolean precise) {
        if (!precise) {
            duration += 500;
        }

        long totalSeconds = duration / 1000;
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

        long displayMinutes = (totalSeconds - displayHours * hour) / minute;
        if (displayMinutes != 0) {
            minutes = Long.toString(displayMinutes);
        } else {
            minutes = "00";
        }

        long displaySeconds = totalSeconds - displayHours * hour - displayMinutes * minute;
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
