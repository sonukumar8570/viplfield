package in.visibleinfotech.viplfieldapplications;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionStr {

    @SuppressLint("NewApi")
    public Connection connectionclasss(String user, String password, String database, String server, String instance) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL = null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            if (instance != null) {
                ConnectionURL = "jdbc:jtds:sqlserver://" + server + ";instance=" + instance + ";"
                        + "databaseName=" + database + ";";

            } else {
                ConnectionURL = "jdbc:jtds:sqlserver://" + server + ";"
                        + "databaseName=" + database + ";";
            }
            connection = DriverManager.getConnection(ConnectionURL, user, password);

        } catch (SQLException se) {
            Log.e("myname : ", "" + se);
        } catch (Exception e) {
            Log.e("myname : ", "" + e);
        }
        return connection;
    }
}
