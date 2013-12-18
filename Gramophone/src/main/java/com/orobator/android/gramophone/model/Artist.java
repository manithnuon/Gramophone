package com.orobator.android.gramophone.model;

import java.util.ArrayList;

public class Artist {
    private String name;
    private ArrayList<Album> mAlbums;

    public ArrayList<Album> getAlbums() {
        return mAlbums;
    }

    public void setAlbums(ArrayList<Album> albums) {
        mAlbums = albums;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Artist)) {
            return false;
        }

        Artist other = (Artist) o;
        return other.getName().equals(name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
