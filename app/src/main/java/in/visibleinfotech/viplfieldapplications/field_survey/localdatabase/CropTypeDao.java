package in.visibleinfotech.viplfieldapplications.field_survey.localdatabase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import in.visibleinfotech.viplfieldapplications.field_survey.models.CropType;

@Dao
public interface CropTypeDao {
    @Insert()
    void insert(CropType cropType);

    @Query("DELETE FROM crop_type")
    void deleteAll();

    @Query("SELECT * FROM crop_type ORDER BY cropId ASC")
    LiveData<List<CropType>> getCropTypes();

}
