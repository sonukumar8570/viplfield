package in.visibleinfotech.viplfieldapplications.field_survey.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "RouteMaster")
public class RouteMaster {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "r_code")
    String r_code;
    @ColumnInfo(name = "r_name")
    String r_name;
    @ColumnInfo(name = "r_place")
    String r_place;

    public RouteMaster(String r_code, String r_name, String r_place) {
        this.r_code = r_code;
        this.r_name = r_name;
        this.r_place = r_place;
    }

    public String getR_code() {
        return r_code;
    }

    public String getR_name() {
        return r_name;
    }

    public String getR_place() {
        return r_place;
    }
}
