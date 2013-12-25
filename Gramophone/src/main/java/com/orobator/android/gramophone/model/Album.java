package com.orobator.android.gramophone.model;

public class Album {
    private String mAlbumName;
    private String mAlbumArtist;

    public String getAlbumName() {
        return mAlbumName;
    }

    public void setAlbumName(String albumName) {
        mAlbumName = albumName;
    }

    public String getAlbumArtist() {
        return mAlbumArtist;
    }

    public void setAlbumArtist(String albumArtist) {
        mAlbumArtist = albumArtist;
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
        hash += mAlbumName.hashCode();
        hash += mAlbumArtist.hashCode();
        return hash;
    }

}
