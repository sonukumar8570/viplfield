package in.visibleinfotech.viplfieldapplications.field_survey.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "SupplyMaster")
public class SupplyMaster {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "m_code")
    String m_Code;
    @ColumnInfo(name = "m_name")
    String m_Name;

    public SupplyMaster(String m_Code, String m_Name) {
        this.m_Code = m_Code;
        this.m_Name = m_Name;
    }

    public String getM_Code() {
        return m_Code;
    }

    public String getM_Name() {
        return m_Name;
    }
}
