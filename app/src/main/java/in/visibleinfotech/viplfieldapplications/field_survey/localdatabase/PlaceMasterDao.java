package in.visibleinfotech.viplfieldapplications.field_survey.localdatabase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import in.visibleinfotech.viplfieldapplications.field_survey.models.PlaceMaster;

@Dao
public interface PlaceMasterDao {
    @Insert()
    void insert(PlaceMaster crop);

    @Query("DELETE FROM placemaster")
    void deleteAll();

    @Query("SELECT * FROM PlaceMaster ORDER BY placeId ASC")
    LiveData<List<PlaceMaster>> getPlaceMasters();

    @Query("SELECT placeName FROM PlaceMaster where placeId = :placeId")
    String getPlaceName(String placeId);

}
