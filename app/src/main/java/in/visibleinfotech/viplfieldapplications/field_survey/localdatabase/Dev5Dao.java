package in.visibleinfotech.viplfieldapplications.field_survey.localdatabase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import in.visibleinfotech.viplfieldapplications.field_survey.models.CaneDev5;

@Dao
public interface Dev5Dao {
    @Insert()
    void insert(CaneDev5 crop);

    @Query("DELETE FROM cane_dev5")
    void deleteAll();

    @Query("SELECT * FROM cane_dev1 ORDER BY code ASC")
    LiveData<List<CaneDev5>> getDev5Options();

}
