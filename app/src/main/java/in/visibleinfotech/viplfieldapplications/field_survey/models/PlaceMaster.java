package in.visibleinfotech.viplfieldapplications.field_survey.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "PlaceMaster")
public class PlaceMaster {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "placeId")
    String placeId;
    @ColumnInfo(name = "placeName")
    String placeName;

    public PlaceMaster(@NonNull String placeId, String placeName) {
        this.placeId = placeId;
        this.placeName = placeName;
    }

    @NonNull
    public String getPlaceId() {
        return placeId;
    }

    public String getPlaceName() {
        return placeName;
    }


}
