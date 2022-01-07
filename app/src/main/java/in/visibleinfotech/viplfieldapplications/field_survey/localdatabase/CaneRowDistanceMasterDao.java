package in.visibleinfotech.viplfieldapplications.field_survey.localdatabase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import in.visibleinfotech.viplfieldapplications.field_survey.models.CaneRowDistanceMaster;

@Dao
public interface CaneRowDistanceMasterDao {
    @Insert()
    void insert(CaneRowDistanceMaster distanceMaster);

    @Query("DELETE FROM canerowdistancemaster")
    void deleteAll();

    @Query("SELECT * FROM canerowdistancemaster ORDER BY Cane_RowCode ASC")
    LiveData<List<CaneRowDistanceMaster>> getCaneRows();

}
