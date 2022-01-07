package in.visibleinfotech.viplfieldapplications.field_survey.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cane_dev2")
public class CaneDev2 {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "code")
    String cropCode;
    @ColumnInfo(name = "name")
    String cropName;

    public CaneDev2(@NonNull String cropCode, String cropName) {
        this.cropCode = cropCode;
        this.cropName = cropName;
    }

    @NonNull
    public String getCropCode() {
        return cropCode;
    }

    public String getCropName() {
        return cropName;
    }
}
