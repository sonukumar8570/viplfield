package in.visibleinfotech.viplfieldapplications.field_survey.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "inter_crop")
public class InterCrop {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "code")
    String cropCode;
    @ColumnInfo(name = "name")
    String cropName;

    public InterCrop(@NonNull String cropCode, String cropName) {
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
