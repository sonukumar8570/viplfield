package in.visibleinfotech.viplfieldapplications.field_survey.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "HarvMaster")
public class HarvMaster {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "h_code")
    String h_code;
    @ColumnInfo(name = "h_name")
    String h_name;

    public HarvMaster(String h_code, String h_name) {
        this.h_code = h_code;
        this.h_name = h_name;
    }

    public String getH_code() {
        return h_code;
    }

    public String getH_name() {
        return h_name;
    }
}
