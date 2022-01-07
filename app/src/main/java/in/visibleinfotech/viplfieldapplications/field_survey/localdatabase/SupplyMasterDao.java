package in.visibleinfotech.viplfieldapplications.field_survey.localdatabase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import in.visibleinfotech.viplfieldapplications.field_survey.models.SupplyMaster;

@Dao
public interface SupplyMasterDao {
    @Insert()
    void insert(SupplyMaster crop);

    @Query("DELETE FROM supplymaster")
    void deleteAll();

    @Query("SELECT * FROM supplymaster ORDER BY m_code ASC")
    LiveData<List<SupplyMaster>> getSupplyOptions();

}
