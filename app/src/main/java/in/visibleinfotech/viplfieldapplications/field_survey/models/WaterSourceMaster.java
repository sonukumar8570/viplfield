package in.visibleinfotech.viplfieldapplications.field_survey.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "WaterSourceMaster")
public class WaterSourceMaster {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "w_code")
    String w_code;
    @ColumnInfo(name = "waterSource")
    String w_watersource;

    public WaterSourceMaster(String w_code, String w_watersource) {
        this.w_code = w_code;
        this.w_watersource = w_watersource;
    }

    public String getW_code() {
        return w_code;
    }

    public String getW_watersource() {
        return w_watersource;
    }
}
