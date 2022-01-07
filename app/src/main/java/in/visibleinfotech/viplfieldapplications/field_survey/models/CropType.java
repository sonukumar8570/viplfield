package in.visibleinfotech.viplfieldapplications.field_survey.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "crop_type")
public class CropType {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "cropId")
    String cropId;
    @ColumnInfo(name = "cropType")
    String cropType;

    public CropType(String cropId, String cropType) {
        this.cropId = cropId;
        this.cropType = cropType;
    }

    public String getCropId() {
        return cropId;
    }

    public String getCropType() {
        return cropType;
    }
}
