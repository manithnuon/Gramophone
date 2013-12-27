package com.orobator.android.gramophone.view.adapters;import android.content.Context;import android.database.Cursor;import android.view.LayoutInflater;import android.view.View;import android.view.ViewGroup;import android.widget.CursorAdapter;import android.widget.SectionIndexer;import android.widget.TextView;import com.orobator.android.gramophone.R;import com.orobator.android.gramophone.model.Song;import com.orobator.android.gramophone.model.SongDatabaseHelper;import java.util.ArrayList;import java.util.HashMap;import java.util.Vector;public class SongCursorAdapter extends CursorAdapter implements SectionIndexer {    public static final String KEY_SONG = "song";    private static final String TAG = "SongCursorAdapter";    private Vector<String> mSections;    private HashMap<String, Integer> sectionMap;    private SongDatabaseHelper.SongCursor mSongCursor;    private ArrayList<Song> mSongs;    private Context mContext;    public SongCursorAdapter(Context context, SongDatabaseHelper.SongCursor cursor) {        super(context, cursor, 0);        mContext = context;        mSongCursor = cursor;        mSections = new Vector<String>();        sectionMap = new HashMap<String, Integer>();        mSongs = new ArrayList<Song>();        initializeSections();    }    /**     * Creates the sections for the SectionIndexer to use     */    private void initializeSections() {        mSongCursor.moveToFirst();        while (!mSongCursor.isAfterLast()) {            mSongs.add(mSongCursor.getSong());            String title = mSongCursor.getSong().getTitle();            String firstLetter = title.substring(0, 1).toUpperCase();            Integer myInt = Integer.getInteger(firstLetter);            if (myInt != null) {                firstLetter = "123";            }            if (!sectionMap.containsKey(firstLetter)) {                if (startsWithAlphaNum(firstLetter)) {                    sectionMap.put(firstLetter, mSongCursor.getPosition());                    mSections.add(firstLetter);                }            }            mSongCursor.moveToNext();        }    }    private boolean startsWithAlphaNum(String str) {        if (str == null) return false;        // TODO Clean up code with REGEX        return !(str.toLowerCase().startsWith("~")                || str.toLowerCase().startsWith("!")                || str.toLowerCase().startsWith("@")                || str.toLowerCase().startsWith("#")                || str.toLowerCase().startsWith("$")                || str.toLowerCase().startsWith("%")                || str.toLowerCase().startsWith("^")                || str.toLowerCase().startsWith("&")                || str.toLowerCase().startsWith("*")                || str.toLowerCase().startsWith("(")                || str.toLowerCase().startsWith(")")                || str.toLowerCase().startsWith("_")                || str.toLowerCase().startsWith("-")                || str.toLowerCase().startsWith("+")                || str.toLowerCase().startsWith("=")                || str.toLowerCase().startsWith("`")                || str.toLowerCase().startsWith("[")                || str.toLowerCase().startsWith("]")                || str.toLowerCase().startsWith("{")                || str.toLowerCase().startsWith("}")                || str.toLowerCase().startsWith("\\")                || str.toLowerCase().startsWith("|")                || str.toLowerCase().startsWith(":")                || str.toLowerCase().startsWith(";")                || str.toLowerCase().startsWith("'")                || str.toLowerCase().startsWith("\"")                || str.toLowerCase().startsWith("<")                || str.toLowerCase().startsWith(">")                || str.toLowerCase().startsWith(",")                || str.toLowerCase().startsWith(".")                || str.toLowerCase().startsWith("?")                || str.toLowerCase().startsWith("/"));    }    @Override    public Object getItem(int position) {        return mSongs.get(position);    }    @Override    public View getView(int position, View convertView, ViewGroup parent) {        Song song = mSongs.get(position);        if (convertView == null) {            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);            convertView = inflater.inflate(R.layout.list_item_no_album_art, null);            ViewHolder holder = new ViewHolder();            holder.titleTextView = (TextView) convertView.findViewById(R.id.songTitle_TextView);            holder.artistTextView = (TextView) convertView.findViewById(R.id.songArtist_TextView);            convertView.setTag(holder);        }        ViewHolder holder = (ViewHolder) convertView.getTag();        holder.titleTextView.setText(song.getTitle());        holder.artistTextView.setText(song.getArtist());        return convertView;    }    @Override    public View newView(Context context, Cursor cursor, ViewGroup parent) {        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);        return inflater.inflate(R.layout.list_item_no_album_art, parent, false);    }    @Override    public void bindView(View view, Context context, Cursor cursor) {        Song song = mSongCursor.getSong();        TextView songTitleTextView = (TextView) view.findViewById(R.id.songTitle_TextView);        TextView songArtistTextView = (TextView) view.findViewById(R.id.songArtist_TextView);        songTitleTextView.setText(song.getTitle());        songArtistTextView.setText(song.getArtist());    }    public int getSize() {        return mSongCursor.getCount();    }    @Override    public Object[] getSections() {        return mSections.toArray();    }    @Override    public int getPositionForSection(int sectionIndex) {        return sectionMap.get(mSections.get(sectionIndex));    }    @Override    public int getSectionForPosition(int position) {        Song song = mSongs.get(position);        String title = song.getTitle();        String firstLetter = title.substring(0, 1).toUpperCase();        for (int i = 0; i < mSections.size(); i++) {            if (firstLetter.equals(mSections.get(i))) {                return i;            }        }        return 0;    }    /**     * ViewHolder is a class to cache calls to findViewById() for performance     * increases     */    static class ViewHolder {        TextView titleTextView;        TextView artistTextView;    }}