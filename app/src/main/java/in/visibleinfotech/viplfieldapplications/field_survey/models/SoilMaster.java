package in.visibleinfotech.viplfieldapplications.field_survey.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "SoilMaster")
public class SoilMaster {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "s_code")
    String s_code;
    @ColumnInfo(name = "s_name")
    String s_name;

    public SoilMaster(String s_code, String s_name) {
        this.s_code = s_code;
        this.s_name = s_name;
    }

    public String getS_code() {
        return s_code;
    }

    public String getS_name() {
        return s_name;
    }
}
