package com.orobator.android.gramophone.model;

import java.util.Date;

public class Song {
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
    private long size = 0; //Size of song in bytes
    private String duration; //TODO units?
    private int discNumber = 0;
    private int discCount = 0;
    private int trackNumber = 0; //TODO hook up the metadata display
    private int trackCount = 0;  //TODO Change the list display of songs to Song in bold with artist underneath
    private int year = 0;
    private int numTracks = 0; //TODO is this the number of tracks in the containing album?
    private Date dateModified;
    private int bitRate = 0; //bits/sec
    private int sampleRate = 0;
    private String location;
    private int playCount = 0;

    public Song(String location) {
        this.location = location;
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

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
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

    public boolean isHasArtwork() {
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
}
