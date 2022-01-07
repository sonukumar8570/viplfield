package in.visibleinfotech.viplfieldapplications.field_survey.localdatabase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import in.visibleinfotech.viplfieldapplications.field_survey.models.Harvester;

@Dao
public interface HarvesterDao {
    @Insert()
    void insert(Harvester crop);

    @Query("DELETE FROM harvester")
    void deleteAll();

    @Query("SELECT * FROM harvester ORDER BY harvesterId ASC")
    LiveData<List<Harvester>> getHarvesters();

}
