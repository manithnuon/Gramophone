package com.orobator.android.gramophone.model;

import java.io.Serializable;

public class Album implements Serializable {
    public static final long serialVersionUID = 0L;
    public static final String KEY_ALBUM_NAME = "album name key";
    public static final String KEY_ALBUM_ARTIST = "album artist key";
    private String mAlbumName;
    private String mAlbumArtist;

    public Album(String albumName, String albumArtist) {
        mAlbumName = albumName;
        mAlbumArtist = albumArtist;
    }

    public String getAlbumName() {
        return mAlbumName;
    }

    public String getAlbumArtist() {
        return mAlbumArtist;
    }

    @Override
    public String toString() {
        return mAlbumName + " - " + mAlbumArtist;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Album)) {
            return false;
        }

        Album other = (Album) o;
        return other.getAlbumArtist().equals(mAlbumArtist)
                && other.getAlbumName().equals(mAlbumName);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash += 17 * mAlbumName.hashCode();
        hash += 19 * mAlbumArtist.hashCode();
        return hash;
    }

}
