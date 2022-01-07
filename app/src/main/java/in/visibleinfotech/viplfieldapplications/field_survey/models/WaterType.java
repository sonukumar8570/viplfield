package in.visibleinfotech.viplfieldapplications.field_survey.models;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "WaterType")
public class WaterType {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "wt_code")
    String wt_code;
    @ColumnInfo(name = "wt_name")
    String wt_name;

    public WaterType(String wt_code, String wt_name) {
        this.wt_code = wt_code;
        this.wt_name = wt_name;
    }

    public String getWt_code() {
        return wt_code;
    }

    public String getWt_name() {
        return wt_name;
    }
}
