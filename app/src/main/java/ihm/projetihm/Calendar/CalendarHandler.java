package ihm.projetihm.Calendar;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;
import android.util.SparseArray;

import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

public class CalendarHandler {

    private Context context;
    private static SparseArray<Long> EVENTS = new SparseArray<>();

    public CalendarHandler(Context context) {
        this.context = context;
    }

    public boolean addEvent(String title, String content, long dateEvent, int eventDuration, int eventID) {
        ContentResolver cr = context.getContentResolver();
        ContentValues values = new ContentValues();

        values.put(CalendarContract.Events.DTSTART, dateEvent);
        values.put(CalendarContract.Events.DTEND, dateEvent + (eventDuration * 60000));
        values.put(CalendarContract.Events.TITLE, title);
        values.put(CalendarContract.Events.DESCRIPTION, content);
        TimeZone timeZone = TimeZone.getDefault();
        values.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());
        values.put(CalendarContract.Events.CALENDAR_ID, 1);
        values.put(CalendarContract.Events.HAS_ALARM, 1);

        try {
            Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
            String[] segments = uri.toString().split("/");
            EVENTS.put(eventID, Long.parseLong(segments[segments.length - 1]));
            Log.d("INSERTCAL", "inserted " + uri);
            return true;
        } catch (SecurityException e) {
            Log.w("INSERTCAL", "permission denied");
            return false;
        }
    }

    public boolean removeEvent(int eventID) {
        try {
            ContentResolver cr = context.getContentResolver();
            Uri deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, EVENTS.get(eventID));
            EVENTS.remove(eventID);
            int row = cr.delete(deleteUri, null, null);
            Log.d("REMOVECAL", "Row deleted: " + row);
            return true;
        } catch (Exception e) {
            Log.w("REMOVECAL", Arrays.toString(e.getStackTrace()));
            return false;
        }
    }

}
