package in.visibleinfotech.viplfieldapplications.field_survey.localdatabase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import in.visibleinfotech.viplfieldapplications.field_survey.models.WaterSourceMaster;

@Dao
public interface WaterSourceDao {
    @Insert()
    void insert(WaterSourceMaster crop);

    @Query("DELETE FROM watersourcemaster")
    void deleteAll();

    @Query("SELECT * FROM watersourcemaster ORDER BY w_code ASC")
    LiveData<List<WaterSourceMaster>> getWaterSources();

}
