package in.visibleinfotech.viplfieldapplications.field_survey.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "CaneRowDistanceMaster")
public class CaneRowDistanceMaster {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "Cane_RowCode")
    private String cane_RowCode;
    @ColumnInfo(name = "Cane_RowFit")
    String cane_RowFit;

    public CaneRowDistanceMaster(@NonNull String cane_RowCode, String cane_RowFit) {
        this.cane_RowCode = cane_RowCode;
        this.cane_RowFit = cane_RowFit;
    }

    public String getCane_RowCode() {
        return cane_RowCode;
    }

    public String getCane_RowFit() {
        return cane_RowFit;
    }
}
