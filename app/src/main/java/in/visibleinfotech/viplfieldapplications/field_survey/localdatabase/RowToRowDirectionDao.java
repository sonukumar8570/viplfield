package in.visibleinfotech.viplfieldapplications.field_survey.localdatabase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import in.visibleinfotech.viplfieldapplications.field_survey.models.RowToRowFieldMapDirection;

@Dao
public interface RowToRowDirectionDao {
    @Insert()
    void insert(RowToRowFieldMapDirection crop);

    @Query("DELETE FROM RowToRowFieldMapDirection")
    void deleteAll();

    @Query("SELECT * FROM RowToRowFieldMapDirection ORDER BY row_Code ASC")
    LiveData<List<RowToRowFieldMapDirection>> getRowDirections();

}
