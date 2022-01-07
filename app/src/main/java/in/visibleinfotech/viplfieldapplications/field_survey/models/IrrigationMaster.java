package in.visibleinfotech.viplfieldapplications.field_survey.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "IrrigationMaster")
public class IrrigationMaster {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "ir_code")
    String ir_code;
    @ColumnInfo(name = "ir_name")
    String ir_name;

    public IrrigationMaster(String ir_code, String ir_name) {
        this.ir_code = ir_code;
        this.ir_name = ir_name;
    }

    public String getIr_code() {
        return ir_code;
    }

    public String getIr_name() {
        return ir_name;
    }
}
