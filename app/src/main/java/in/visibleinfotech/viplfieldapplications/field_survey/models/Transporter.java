package in.visibleinfotech.viplfieldapplications.field_survey.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "transporter")
public class Transporter {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "transporterId")
    private String transporterId;
    @ColumnInfo(name = "transporterName")
    String transporterName;

    public Transporter(String transporterId, String transporterName) {
        this.transporterId = transporterId;
        this.transporterName = transporterName;
    }

    public String getTransporterId() {
        return transporterId;
    }

    public String getTransporterName() {
        return transporterName;
    }
}
