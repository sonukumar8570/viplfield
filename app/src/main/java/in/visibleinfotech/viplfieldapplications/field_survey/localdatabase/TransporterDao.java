package in.visibleinfotech.viplfieldapplications.field_survey.localdatabase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import in.visibleinfotech.viplfieldapplications.field_survey.models.Transporter;

@Dao
public interface TransporterDao {
    @Insert()
    void insert(Transporter crop);

    @Query("DELETE FROM transporter")
    void deleteAll();

    @Query("SELECT * FROM transporter ORDER BY transporterId ASC")
    LiveData<List<Transporter>> getTransporters();

}
