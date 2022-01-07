package in.visibleinfotech.viplfieldapplications.attendance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import in.visibleinfotech.viplfieldapplications.MainConstant;
import in.visibleinfotech.viplfieldapplications.field_survey.models.Harvester;

public class LocationDb extends SQLiteOpenHelper {
    Context context;
    MainConstant c;

    public LocationDb(Context context) {
        super(context, "VIPL", null, 1);
        this.context = context;

        c = new MainConstant(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addNewLocation(Location location) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("CREATE TABLE IF NOT EXISTS Vipl_location(username TEXT,lat TEXT,longi TEXT,date_time TEXT)");

        long k = 0;

        ContentValues values = new ContentValues();
        values.put("username", c.geteCode());
        values.put("lat",location.getLatitude());
        values.put("longi", location.getLongitude());
        values.put("date_time",new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()) );

        k = database.insert("Vipl_location", null, values);

        Log.d("myname", "" + k);
        return true;
    }

    public ArrayList<LocationLocalHistory> getLocations() {
        ArrayList<LocationLocalHistory> localHistories = new ArrayList<>();
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("CREATE TABLE IF NOT EXISTS Vipl_location(username TEXT,lat TEXT,longi TEXT,date_time TEXT)");
        database.close();
        database = this.getReadableDatabase();
        Cursor cursor = database.query("Vipl_location", null, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String username = cursor.getString(cursor.getColumnIndex("username"));
                String lat = cursor.getString(cursor.getColumnIndex("lat"));
                String longi = cursor.getString(cursor.getColumnIndex("longi"));
                String date_time = cursor.getString(cursor.getColumnIndex("date_time"));

                LocationLocalHistory waterType = new LocationLocalHistory(username,lat,longi,date_time);
                localHistories.add(waterType);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return localHistories;
    }

    public void clearLocationHistory() {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("CREATE TABLE IF NOT EXISTS Vipl_location(username TEXT,lat TEXT,longi TEXT,date_time TEXT)");
        database.delete("Vipl_location", null, null);
    }
}
