package in.visibleinfotech.viplfieldapplications.field_survey.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "RowToRowFieldMapDirection")
public class RowToRowFieldMapDirection {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "row_Code")
    String ROW_Code;
    @ColumnInfo(name = "row_name")
    String ROW_Name;

    public RowToRowFieldMapDirection(String ROW_Code, String ROW_Name) {
        this.ROW_Code = ROW_Code;
        this.ROW_Name = ROW_Name;
    }

    public String getROW_Code() {
        return ROW_Code;
    }

    public String getROW_Name() {
        return ROW_Name;
    }
}
