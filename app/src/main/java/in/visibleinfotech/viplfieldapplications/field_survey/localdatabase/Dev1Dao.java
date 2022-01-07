package in.visibleinfotech.viplfieldapplications.field_survey.localdatabase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import in.visibleinfotech.viplfieldapplications.field_survey.models.CaneDev1;
import in.visibleinfotech.viplfieldapplications.field_survey.models.PlantSeed;

@Dao
public interface Dev1Dao {
    @Insert()
    void insert(CaneDev1 crop);

    @Query("DELETE FROM cane_dev1")
    void deleteAll();

    @Query("SELECT * FROM cane_dev1 ORDER BY code ASC")
    LiveData<List<CaneDev1>> getDev1Options();

}
