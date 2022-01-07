package in.visibleinfotech.viplfieldapplications.field_survey.localdatabase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import in.visibleinfotech.viplfieldapplications.field_survey.models.SoilMaster;

@Dao
public interface SoilMasterDao {
    @Insert()
    void insert(SoilMaster crop);

    @Query("DELETE FROM soilmaster")
    void deleteAll();

    @Query("SELECT * FROM SoilMaster ORDER BY s_code ASC")
    LiveData<List<SoilMaster>> getSoils();

}
