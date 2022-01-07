package in.visibleinfotech.viplfieldapplications.field_survey.localdatabase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import in.visibleinfotech.viplfieldapplications.field_survey.models.PrevCrop;

@Dao
public interface PrevCropDao {
    @Insert()
    void insert(PrevCrop crop);

    @Query("DELETE FROM prev_crop")
    void deleteAll();

    @Query("SELECT * FROM prev_crop ORDER BY code ASC")
    LiveData<List<PrevCrop>> getAllPrevCrop();

}
