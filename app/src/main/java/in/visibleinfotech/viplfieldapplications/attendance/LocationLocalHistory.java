package in.visibleinfotech.viplfieldapplications.attendance;

public class LocationLocalHistory {
    String username, lat, longi, date_time;

    public LocationLocalHistory(String username, String lat, String longi, String date_time) {
        this.username = username;
        this.lat = lat;
        this.longi = longi;
        this.date_time = date_time;
    }

    public String getUsername() {
        return username;
    }

    public String getLat() {
        return lat;
    }

    public String getLongi() {
        return longi;
    }

    public String getDate_time() {
        return date_time;
    }
}
