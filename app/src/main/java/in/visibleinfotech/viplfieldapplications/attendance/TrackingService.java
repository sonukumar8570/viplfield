package in.visibleinfotech.viplfieldapplications.attendance;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.sql.Connection;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import in.visibleinfotech.viplfieldapplications.ConnectionStr;
import in.visibleinfotech.viplfieldapplications.MainConstant;
import in.visibleinfotech.viplfieldapplications.R;

public class TrackingService extends Service implements LocationListener {

    protected BroadcastReceiver stopReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            unregisterReceiver(stopReceiver);

            stopSelf();
        }
    };
    FusedLocationProviderClient client;
    LocationRequest request;

    LocationCallback callback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            new UpdateLocation().execute(locationResult.getLastLocation());
        }

    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        buildNotification();
        requestLocationUpdates();

    }

    private void buildNotification() {
        String stop = "stop";
        registerReceiver(stopReceiver, new IntentFilter(stop));
        PendingIntent broadcastIntent = PendingIntent.getBroadcast(
                this, 0, new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        int notifyID = 1;
        String CHANNEL_ID = "my_channel_01";// The id of the channel.
        CharSequence name = getString(R.string.app_name);// The user-visible name of the channel.
        int importance = NotificationManager.IMPORTANCE_HIGH;
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.createNotificationChannel(mChannel);

// Create a notification and set the notification channel.
            Notification notification = new Notification.Builder(TrackingService.this)
                    .setContentTitle("User Attendance")
                    .setContentText("Your Location is being tracked")
                    .setSmallIcon(R.mipmap.ic_meri_hazri)
                    .setChannelId(CHANNEL_ID)
                    .setContentIntent(broadcastIntent)
                    .build();


// Issue the notification.
            mNotificationManager.notify(notifyID, notification);
            startForeground(1, notification);
        } else {

            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

// Create a notification and set the notification channel.
            Notification notification = new Notification.Builder(TrackingService.this)
                    .setContentTitle("User Attendance")
                    .setContentText("Your Location is being tracked")
                    .setSmallIcon(R.mipmap.ic_meri_hazri)
                    .setContentIntent(broadcastIntent)
                    .build();


// Issue the notification.
            mNotificationManager.notify(notifyID, notification);
            startForeground(1, notification);

        }
    }

    private void requestLocationUpdates() {
        request =  LocationRequest.create();

        request.setInterval(60 * 1000 * 5);
        request.setFastestInterval(60 * 1000 *2);

        //mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //mLocationRequest.setSmallestDisplacement(Utils.SMALLEST_DISPLACEMENT);
        //mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setMaxWaitTime(60 * 1000 * 10);
        client = LocationServices.getFusedLocationProviderClient(this);
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            client.requestLocationUpdates(request, callback, Looper.getMainLooper());
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        new UpdateLocation().execute(location);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
       // client.removeLocationUpdates(callback);
    }

    public class UpdateLocation extends AsyncTask<Location, String, String> {
        String msg = "null";
        boolean saved;

        @Override
        protected String doInBackground(Location... strings) {
            Location l = strings[0];
            try {
                ConnectionStr conStr = new ConnectionStr();
                MainConstant c = new MainConstant(TrackingService.this);
                Connection connect = conStr.connectionclasss(c.getDbUser(), c.getDbPass(), c.getDbName(), c.getIp(), c.getInstance());

                if (connect == null) {

                    return "not connected to internet";
                } else {

                    Statement stmt = connect.createStatement();


                    String query2 = "Insert into VIPL_Location(username,lat,longi,date_time)" +
                            "values('" + c.geteCode() + "','" + l.getLatitude() + "','" + l.getLongitude() + "','" + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()) + "')";
                    Log.d("myname", query2);
                    int i = stmt.executeUpdate(query2);
                    saved = i > 0;
                    msg = "saved";
                }
            } catch (
                    Exception e) {
                LocationDb db=new LocationDb(TrackingService.this);
                db.addNewLocation(l);
                Log.d("myname", e.toString());
            }

            return msg;
        }

    }
}
