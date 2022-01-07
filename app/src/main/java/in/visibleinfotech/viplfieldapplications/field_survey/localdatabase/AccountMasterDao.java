package in.visibleinfotech.viplfieldapplications.field_survey.localdatabase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import in.visibleinfotech.viplfieldapplications.field_survey.models.AccountMaster;

@Dao
public interface AccountMasterDao {
    @Insert()
    void insert(AccountMaster accountMaster);

    @Query("DELETE FROM account_master")
    void deleteAll();

    @Query("SELECT * FROM account_master where placeID = :placeID ORDER BY accountId ASC")
    LiveData<List<AccountMaster>> getAccountMasters(String placeID);

}
