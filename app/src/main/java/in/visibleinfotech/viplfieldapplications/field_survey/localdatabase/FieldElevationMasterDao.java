package in.visibleinfotech.viplfieldapplications.field_survey.localdatabase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import in.visibleinfotech.viplfieldapplications.field_survey.models.FieldElivationTypeMaster;

@Dao
public interface FieldElevationMasterDao {
    @Insert()
    void insert(FieldElivationTypeMaster crop);

    @Query("DELETE FROM FieldElivationTypeMaster")
    void deleteAll();

    @Query("SELECT * FROM FieldElivationTypeMaster ORDER BY FE_Code ASC")
    LiveData<List<FieldElivationTypeMaster>> getFieldElevations();

}
