package in.visibleinfotech.viplfieldapplications.field_survey.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "VarietyMaster")
public class VarietyMaster {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "varietyCode")
    String id;
    @ColumnInfo(name = "varietyName")
    String varietyName;

    public VarietyMaster(String id, String varietyName) {
        this.id = id;
        this.varietyName = varietyName;
    }

    public String getId() {
        return id;
    }

    public String getVarietyName() {
        return varietyName;
    }
}
