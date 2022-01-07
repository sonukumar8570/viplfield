package in.visibleinfotech.viplfieldapplications.field_survey.localdatabase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import in.visibleinfotech.viplfieldapplications.field_survey.models.CaneDev3;

@Dao
public interface Dev3Dao {
    @Insert()
    void insert(CaneDev3 crop);

    @Query("DELETE FROM cane_dev3")
    void deleteAll();

    @Query("SELECT * FROM cane_dev3 ORDER BY code ASC")
    LiveData<List<CaneDev3>> getDev3Options();

}
