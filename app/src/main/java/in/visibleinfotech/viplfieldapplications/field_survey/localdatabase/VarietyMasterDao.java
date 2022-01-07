package in.visibleinfotech.viplfieldapplications.field_survey.localdatabase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import in.visibleinfotech.viplfieldapplications.field_survey.models.VarietyMaster;

@Dao
public interface VarietyMasterDao {
    @Insert()
    void insert(VarietyMaster crop);

    @Query("DELETE FROM VarietyMaster")
    void deleteAll();

    @Query("SELECT * FROM VarietyMaster ORDER BY varietyCode ASC")
    LiveData<List<VarietyMaster>> getVarieties();

}
