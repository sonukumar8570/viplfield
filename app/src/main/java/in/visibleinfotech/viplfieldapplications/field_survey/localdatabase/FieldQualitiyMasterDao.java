package in.visibleinfotech.viplfieldapplications.field_survey.localdatabase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import in.visibleinfotech.viplfieldapplications.field_survey.models.CaneDev4;
import in.visibleinfotech.viplfieldapplications.field_survey.models.FieldQualityMaster;

@Dao
public interface FieldQualitiyMasterDao {
    @Insert()
    void insert(FieldQualityMaster crop);

    @Query("DELETE FROM FieldQualityMaster")
    void deleteAll();

    @Query("SELECT * FROM FieldQualityMaster ORDER BY fieldCode ASC")
    LiveData<List<FieldQualityMaster>> getFieldQualities();

}
