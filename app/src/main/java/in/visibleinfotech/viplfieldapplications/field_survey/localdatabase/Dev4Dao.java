package in.visibleinfotech.viplfieldapplications.field_survey.localdatabase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import in.visibleinfotech.viplfieldapplications.field_survey.models.CaneDev4;

@Dao
public interface Dev4Dao {
    @Insert()
    void insert(CaneDev4 crop);

    @Query("DELETE FROM cane_dev4")
    void deleteAll();

    @Query("SELECT * FROM cane_dev4 ORDER BY code ASC")
    LiveData<List<CaneDev4>> getDev4Options();

}
