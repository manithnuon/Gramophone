package com.orobator.android.gramophone.view.adapters;import android.content.Context;import android.database.Cursor;import android.support.v4.widget.CursorAdapter;import android.view.LayoutInflater;import android.view.View;import android.view.ViewGroup;import android.widget.SectionIndexer;import android.widget.TextView;import com.orobator.android.gramophone.R;import com.orobator.android.gramophone.model.Song;import com.orobator.android.gramophone.model.SongDatabaseHelper;import java.util.HashMap;import java.util.Vector;public class SongCursorAdapter extends CursorAdapter implements SectionIndexer {    public static final String KEY_SONG = "song";    private static final String TAG = "SongCursorAdapter";    private Vector<String> mSections;    private HashMap<String, Integer> sectionMap;    private SongDatabaseHelper.SongCursor mSongCursor;    public SongCursorAdapter(Context context, SongDatabaseHelper.SongCursor cursor) {        super(context, cursor, 0);        mSongCursor = cursor;        mSections = new Vector<String>();        sectionMap = new HashMap<String, Integer>();        initializeSections();    }    private void initializeSections() {        mSongCursor.moveToFirst();        while (!mSongCursor.isAfterLast()) {            String title = mSongCursor.getSong().getTitle();            String firstLetter = title.substring(0, 1).toUpperCase();            Integer myInt = Integer.getInteger(firstLetter);            if (myInt != null) {                firstLetter = "123";            }            if (!sectionMap.containsKey(firstLetter)) {                if (startsWithAlphaNum(firstLetter)) {                    sectionMap.put(firstLetter, mSongCursor.getPosition());                    mSections.add(firstLetter);                }            }            mSongCursor.moveToNext();        }    }    private boolean startsWithAlphaNum(String str) {        if (str == null) return false;        // TODO Clean up code with REGEX        return !(str.toLowerCase().startsWith("~")                || str.toLowerCase().startsWith("!")                || str.toLowerCase().startsWith("@")                || str.toLowerCase().startsWith("#")                || str.toLowerCase().startsWith("$")                || str.toLowerCase().startsWith("%")                || str.toLowerCase().startsWith("^")                || str.toLowerCase().startsWith("&")                || str.toLowerCase().startsWith("*")                || str.toLowerCase().startsWith("(")                || str.toLowerCase().startsWith(")")                || str.toLowerCase().startsWith("_")                || str.toLowerCase().startsWith("-")                || str.toLowerCase().startsWith("+")                || str.toLowerCase().startsWith("=")                || str.toLowerCase().startsWith("`")                || str.toLowerCase().startsWith("[")                || str.toLowerCase().startsWith("]")                || str.toLowerCase().startsWith("{")                || str.toLowerCase().startsWith("}")                || str.toLowerCase().startsWith("\\")                || str.toLowerCase().startsWith("|")                || str.toLowerCase().startsWith(":")                || str.toLowerCase().startsWith(";")                || str.toLowerCase().startsWith("'")                || str.toLowerCase().startsWith("\"")                || str.toLowerCase().startsWith("<")                || str.toLowerCase().startsWith(">")                || str.toLowerCase().startsWith(",")                || str.toLowerCase().startsWith(".")                || str.toLowerCase().startsWith("?")                || str.toLowerCase().startsWith("/"));    }    @Override    public Object getItem(int position) {        mSongCursor.moveToPosition(position);        return mSongCursor.getSong();    }    @Override    public View getView(int position, View convertView, ViewGroup parent) {        mSongCursor.moveToPosition(position);        Song song = mSongCursor.getSong();        if (convertView == null) {            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);            convertView = inflater.inflate(R.layout.list_item_no_album_art, null);            ViewHolder holder = new ViewHolder();            holder.titleTextView = (TextView) convertView.findViewById(R.id.songTitle_TextView);            holder.artistTextView = (TextView) convertView.findViewById(R.id.songArtist_TextView);            convertView.setTag(holder);        }        ViewHolder holder = (ViewHolder) convertView.getTag();        holder.titleTextView.setText(song.getTitle());        holder.artistTextView.setText(song.getArtist());        return convertView;    }    @Override    public View newView(Context context, Cursor cursor, ViewGroup parent) {        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);        return inflater.inflate(R.layout.list_item_no_album_art, parent, false);    }    @Override    public void bindView(View view, Context context, Cursor cursor) {        Song song = mSongCursor.getSong();        TextView songTitleTextView = (TextView) view.findViewById(R.id.songTitle_TextView);        TextView songArtistTextView = (TextView) view.findViewById(R.id.songArtist_TextView);        songTitleTextView.setText(song.getTitle());        songArtistTextView.setText(song.getArtist());    }    public int getSize() {        return mSongCursor.getCount();    }    @Override    public Object[] getSections() {        return mSections.toArray();    }    @Override    public int getPositionForSection(int sectionIndex) {        return sectionMap.get(mSections.get(sectionIndex));    }    @Override    public int getSectionForPosition(int position) {        mSongCursor.moveToPosition(position);        Song song = mSongCursor.getSong();        String title = song.getTitle();        String firstLetter = title.substring(0, 1).toUpperCase();        for (int i = 0; i < mSections.size(); i++) {            if (firstLetter.equals(mSections.get(i))) {                return i;            }        }        return 0;    }    static class ViewHolder {        TextView titleTextView;        TextView artistTextView;    }}