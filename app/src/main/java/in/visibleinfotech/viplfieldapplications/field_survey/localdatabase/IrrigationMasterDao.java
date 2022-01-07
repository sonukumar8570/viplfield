package in.visibleinfotech.viplfieldapplications.field_survey.localdatabase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import in.visibleinfotech.viplfieldapplications.field_survey.models.IrrigationMaster;

@Dao
public interface IrrigationMasterDao {
    @Insert()
    void insert(IrrigationMaster crop);

    @Query("DELETE FROM irrigationmaster")
    void deleteAll();

    @Query("SELECT * FROM irrigationmaster ORDER BY ir_code ASC")
    LiveData<List<IrrigationMaster>> getIrrigationMasters();

}
