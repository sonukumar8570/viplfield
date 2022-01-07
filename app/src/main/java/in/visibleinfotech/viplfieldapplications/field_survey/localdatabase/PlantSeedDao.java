package in.visibleinfotech.viplfieldapplications.field_survey.localdatabase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import in.visibleinfotech.viplfieldapplications.field_survey.models.PlantSeed;

@Dao
public interface PlantSeedDao {
    @Insert()
    void insert(PlantSeed crop);

    @Query("DELETE FROM plant_seed")
    void deleteAll();

    @Query("SELECT * FROM inter_crop ORDER BY code ASC")
    LiveData<List<PlantSeed>> getAllPlantSeeds();

}
