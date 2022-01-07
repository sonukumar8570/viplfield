package in.visibleinfotech.viplfieldapplications.field_survey.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "FieldQualityMaster")
public class FieldQualityMaster {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "fieldCode")
    String fieldCode;
    @ColumnInfo(name = "fieldName")
    String fieldName;

    public FieldQualityMaster(String fieldCode, String fieldName) {
        this.fieldCode = fieldCode;
        this.fieldName = fieldName;
    }

    public String getFieldCode() {
        return fieldCode;
    }

    public String getFieldName() {
        return fieldName;
    }
}
