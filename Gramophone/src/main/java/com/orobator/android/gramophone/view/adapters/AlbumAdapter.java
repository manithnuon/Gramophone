package com.orobator.android.gramophone.view.adapters;import android.support.v4.app.Fragment;import android.view.View;import android.view.ViewGroup;import android.widget.ArrayAdapter;import android.widget.SectionIndexer;import android.widget.TextView;import com.orobator.android.gramophone.R;import com.orobator.android.gramophone.model.Album;import java.util.ArrayList;import java.util.HashMap;import java.util.Vector;public class AlbumAdapter extends ArrayAdapter<Album> implements SectionIndexer {    public static final String KEY_ALBUM_NAME = "album name";    public static final String KEY_ALBUM_ARTIST = "album artist";    private ArrayList<Album> mAlbums;    private Vector<String> mSections;    private HashMap<String, Integer> sectionMap;    private Fragment mFragment;    public AlbumAdapter(ArrayList<Album> albums, Fragment fragment) {        super(fragment.getActivity(), android.R.layout.simple_list_item_1, albums);        mAlbums = albums;        mFragment = fragment;        mSections = new Vector<String>();        sectionMap = new HashMap<String, Integer>();        initializeSections();    }    private void initializeSections() {        for (int i = 0; i < mAlbums.size(); i++) {            String title = mAlbums.get(i).getAlbumName();            if (title.toLowerCase().startsWith("the ")) {                title = title.substring(3);            }            String firstLetter = title.substring(0, 1).toUpperCase();            Integer myInt = Integer.getInteger(firstLetter);            if (myInt != null) {                firstLetter = "123";            }            if (!sectionMap.containsKey(firstLetter)) {                if (startsWithAlphaNum(firstLetter)) {                    sectionMap.put(firstLetter, i);                    mSections.add(firstLetter);                }            }        }    }    private boolean startsWithAlphaNum(String str) {        if (str == null) return false;        // TODO Clean up code with REGEX        return !(str.toLowerCase().startsWith("~")                || str.toLowerCase().startsWith("!")                || str.toLowerCase().startsWith("@")                || str.toLowerCase().startsWith("#")                || str.toLowerCase().startsWith("$")                || str.toLowerCase().startsWith("%")                || str.toLowerCase().startsWith("^")                || str.toLowerCase().startsWith("&")                || str.toLowerCase().startsWith("*")                || str.toLowerCase().startsWith("(")                || str.toLowerCase().startsWith(")")                || str.toLowerCase().startsWith("_")                || str.toLowerCase().startsWith("-")                || str.toLowerCase().startsWith("+")                || str.toLowerCase().startsWith("=")                || str.toLowerCase().startsWith("`")                || str.toLowerCase().startsWith("[")                || str.toLowerCase().startsWith("]")                || str.toLowerCase().startsWith("{")                || str.toLowerCase().startsWith("}")                || str.toLowerCase().startsWith("\\")                || str.toLowerCase().startsWith("|")                || str.toLowerCase().startsWith(":")                || str.toLowerCase().startsWith(";")                || str.toLowerCase().startsWith("'")                || str.toLowerCase().startsWith("\"")                || str.toLowerCase().startsWith("<")                || str.toLowerCase().startsWith(">")                || str.toLowerCase().startsWith(",")                || str.toLowerCase().startsWith(".")                || str.toLowerCase().startsWith("?")                || str.toLowerCase().startsWith("/"));    }    public int getSize() {        return mAlbums.size();    }    @Override    public View getView(int position, View convertView, ViewGroup parent) {        Album album = getItem(position);        if (convertView == null) {            convertView = mFragment.getActivity().getLayoutInflater()                    .inflate(R.layout.list_item_album, null);            ViewHolder holder = new ViewHolder();            holder.albumTitleTextView = (TextView) convertView.findViewById(R.id.albumTitle_textView);            holder.albumArtistTextView = (TextView) convertView.findViewById(R.id.albumArtist_textView);            convertView.setTag(holder);        }        ViewHolder holder = (ViewHolder) convertView.getTag();        holder.albumTitleTextView.setText(album.getAlbumName());        holder.albumArtistTextView.setText(album.getAlbumArtist());        return convertView;    }    @Override    public Object[] getSections() {        return mSections.toArray();    }    @Override    public int getPositionForSection(int section) {        return sectionMap.get(mSections.get(section));    }    @Override    public int getSectionForPosition(int position) {        Album album = getItem(position);        String title = album.getAlbumName();        String firstLetter = title.substring(0, 1);        for (int i = 0; i < mSections.size(); i++) {            if (firstLetter.equals(mSections.get(i))) {                return i;            }        }        return 0;    }    static class ViewHolder {        TextView albumTitleTextView;        TextView albumArtistTextView;    }}