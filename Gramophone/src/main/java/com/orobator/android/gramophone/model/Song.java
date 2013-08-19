package com.orobator.android.gramophone.model;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Date;

public class Song implements Serializable {
    boolean hasArtwork;
    private long trackID = 0;
    private String title;
    private String artist;
    private String albumArtist;
    private String album;
    private String genre;
    private String compilationStatus;
    private String composer;
    private String writer;
    private String fileName;
    private long size = 0; //Size of song in bytes
    private long duration; //Time in milliseconds
    private int discNumber = 0;
    private int discCount = 0;
    private int trackNumber = 0;
    private int trackCount = 0;  //TODO Change the list display of songs to Song in bold with artist underneath
    private int year = 0;
    private int numTracks = 0; //TODO is this the number of tracks in the containing album?
    private Date dateModified;
    private int bitRate = 0; //bits per second
    private int sampleRate = 0;
    private String location;
    private int playCount = 0;

    public Song(String location) {
        this.location = location;
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

    public long getTrackID() {
        return trackID;
    }

    public void setTrackID(long trackID) {
        this.trackID = trackID;
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

    public int getDiscCount() { //TODO do i need this? Should it be renamed?
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

    public int getTrackCount() {//TODO do i need this? Should it be renamed?
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

    public Date getDateModified() {
        return dateModified;
    }

    public void setDateModified(Date dateModified) {
        this.dateModified = dateModified;
    }

    public int getBitRate() {
        return bitRate;
    }

    public void setBitRate(int bitRate) {
        this.bitRate = bitRate;
    }

    public int getSampleRate() { //TODO do i need this? Should it be renamed?
        return sampleRate;
    }

    public void setSampleRate(int sampleRate) {
        this.sampleRate = sampleRate;
    }

    public boolean hasArtwork() {
        return hasArtwork;
    }

    public void setHasArtwork(boolean hasArtwork) {
        this.hasArtwork = hasArtwork;
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
        String minutes = "";
        String seconds = "";
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
