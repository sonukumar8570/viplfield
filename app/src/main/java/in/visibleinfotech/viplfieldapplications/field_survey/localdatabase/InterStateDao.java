package in.visibleinfotech.viplfieldapplications.field_survey.localdatabase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import in.visibleinfotech.viplfieldapplications.field_survey.models.InterCrop;

@Dao
public interface InterStateDao {
    @Insert()
    void insert(InterCrop crop);

    @Query("DELETE FROM inter_crop")
    void deleteAll();

    @Query("SELECT * FROM inter_crop ORDER BY code ASC")
    LiveData<List<InterCrop>> getAllInterCrops();

}
