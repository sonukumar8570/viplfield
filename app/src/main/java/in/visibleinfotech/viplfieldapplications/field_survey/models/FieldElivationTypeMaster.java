package in.visibleinfotech.viplfieldapplications.field_survey.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "FieldElivationTypeMaster")
public class FieldElivationTypeMaster {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "FE_Code")
    String FE_Code;
    @ColumnInfo(name = "FE_Name")
    String FE_Name;

    public FieldElivationTypeMaster(String FE_Code, String FE_Name) {
        this.FE_Code = FE_Code;
        this.FE_Name = FE_Name;
    }

    public String getFE_Code() {
        return FE_Code;
    }

    public String getFE_Name() {
        return FE_Name;
    }

}
