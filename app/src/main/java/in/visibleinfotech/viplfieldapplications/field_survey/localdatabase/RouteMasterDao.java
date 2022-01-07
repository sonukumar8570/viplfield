package in.visibleinfotech.viplfieldapplications.field_survey.localdatabase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import in.visibleinfotech.viplfieldapplications.field_survey.models.RouteMaster;

@Dao
public interface RouteMasterDao {
    @Insert()
    void insert(RouteMaster crop);

    @Query("DELETE FROM routemaster")
    void deleteAll();

    @Query("SELECT * FROM routemaster where r_place=:id")
    LiveData<List<RouteMaster>> getRoutes(String id);

    @Query("SELECT * FROM routemaster")
    LiveData<List<RouteMaster>> getRoutes();

}
