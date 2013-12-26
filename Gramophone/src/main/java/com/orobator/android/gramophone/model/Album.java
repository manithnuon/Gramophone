package com.orobator.android.gramophone.model;

public class Album {
    public static final String KEY_ALBUM_NAME = "album name";
    public static final String KEY_ALBUM_ARTIST = "album artist";
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
