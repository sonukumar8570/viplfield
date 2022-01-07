package in.visibleinfotech.viplfieldapplications.field_survey.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "harvester")
public class Harvester {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "harvesterId")
    private String harvesterId;
    @ColumnInfo(name = "harvesterName")
    String harvesterName;

    public Harvester(String harvesterId, String harvesterName) {
        this.harvesterId = harvesterId;
        this.harvesterName = harvesterName;
    }

    public String getHarvesterId() {
        return harvesterId;
    }

    public String getHarvesterName() {
        return harvesterName;
    }
}
