package in.visibleinfotech.viplfieldapplications.field_survey.localdatabase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import in.visibleinfotech.viplfieldapplications.field_survey.models.CaneDev2;

@Dao
public interface Dev2Dao {
    @Insert()
    void insert(CaneDev2 crop);

    @Query("DELETE FROM cane_dev2")
    void deleteAll();

    @Query("SELECT * FROM cane_dev2 ORDER BY code ASC")
    LiveData<List<CaneDev2>> getDev2Options();

}
