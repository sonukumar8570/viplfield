package in.visibleinfotech.viplfieldapplications.field_survey.localdatabase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import in.visibleinfotech.viplfieldapplications.field_survey.models.HarvMaster;

@Dao
public interface HarvMasterDao {
    @Insert()
    void insert(HarvMaster crop);

    @Query("DELETE FROM harvmaster")
    void deleteAll();

    @Query("SELECT * FROM harvmaster ORDER BY h_code ASC")
    LiveData<List<HarvMaster>> getHarvMasters();

}
