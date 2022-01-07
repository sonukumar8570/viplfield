package in.visibleinfotech.viplfieldapplications.field_survey.localdatabase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import in.visibleinfotech.viplfieldapplications.field_survey.models.WaterType;

@Dao
public interface WaterTypeDao {
    @Insert()
    void insert(WaterType crop);

    @Query("DELETE FROM watertype")
    void deleteAll();

    @Query("SELECT * FROM watertype ORDER BY wt_code ASC")
    LiveData<List<WaterType>> getWaterTypes();

}
