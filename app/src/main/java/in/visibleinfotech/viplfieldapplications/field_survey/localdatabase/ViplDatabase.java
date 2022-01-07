package in.visibleinfotech.viplfieldapplications.field_survey.localdatabase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import in.visibleinfotech.viplfieldapplications.field_survey.models.AccountMaster;
import in.visibleinfotech.viplfieldapplications.field_survey.models.CaneDev1;
import in.visibleinfotech.viplfieldapplications.field_survey.models.CaneDev2;
import in.visibleinfotech.viplfieldapplications.field_survey.models.CaneDev3;
import in.visibleinfotech.viplfieldapplications.field_survey.models.CaneDev4;
import in.visibleinfotech.viplfieldapplications.field_survey.models.CaneDev5;
import in.visibleinfotech.viplfieldapplications.field_survey.models.CaneRowDistanceMaster;
import in.visibleinfotech.viplfieldapplications.field_survey.models.CropType;
import in.visibleinfotech.viplfieldapplications.field_survey.models.FieldElivationTypeMaster;
import in.visibleinfotech.viplfieldapplications.field_survey.models.FieldQualityMaster;
import in.visibleinfotech.viplfieldapplications.field_survey.models.HarvMaster;
import in.visibleinfotech.viplfieldapplications.field_survey.models.Harvester;
import in.visibleinfotech.viplfieldapplications.field_survey.models.InterCrop;
import in.visibleinfotech.viplfieldapplications.field_survey.models.IrrigationMaster;
import in.visibleinfotech.viplfieldapplications.field_survey.models.PlaceMaster;
import in.visibleinfotech.viplfieldapplications.field_survey.models.PlantSeed;
import in.visibleinfotech.viplfieldapplications.field_survey.models.PrevCrop;
import in.visibleinfotech.viplfieldapplications.field_survey.models.RouteMaster;
import in.visibleinfotech.viplfieldapplications.field_survey.models.RowToRowFieldMapDirection;
import in.visibleinfotech.viplfieldapplications.field_survey.models.SoilMaster;
import in.visibleinfotech.viplfieldapplications.field_survey.models.SupplyMaster;
import in.visibleinfotech.viplfieldapplications.field_survey.models.Transporter;
import in.visibleinfotech.viplfieldapplications.field_survey.models.VarietyMaster;
import in.visibleinfotech.viplfieldapplications.field_survey.models.WaterSourceMaster;
import in.visibleinfotech.viplfieldapplications.field_survey.models.WaterType;

@Database(entities = {WaterType.class, WaterSourceMaster.class, VarietyMaster.class, Transporter.class, SupplyMaster.class, SoilMaster.class, RowToRowFieldMapDirection.class, RouteMaster.class, AccountMaster.class, CaneRowDistanceMaster.class, CropType.class, FieldElivationTypeMaster.class, FieldQualityMaster.class, Harvester.class, HarvMaster.class, PlaceMaster.class, IrrigationMaster.class, InterCrop.class, PlantSeed.class, PrevCrop.class, CaneDev1.class, CaneDev2.class, CaneDev3.class, CaneDev4.class, CaneDev5.class}, version = 1)
public abstract class ViplDatabase extends RoomDatabase {
    public abstract InterStateDao interStateDao();

    public abstract PrevCropDao prevCropDao();

    public abstract PlantSeedDao plantSeedDao();

    public abstract Dev1Dao dev1Dao();

    public abstract Dev2Dao dev2Dao();

    public abstract Dev3Dao dev3Dao();

    public abstract Dev4Dao dev4Dao();

    public abstract Dev5Dao dev5Dao();

    public abstract AccountMasterDao accountMasterDao();

    public abstract CaneRowDistanceMasterDao distanceMasterDao();

    public abstract CropTypeDao cropTypeDao();

    public abstract FieldElevationMasterDao elevationMasterDao();

    public abstract FieldQualitiyMasterDao qualitiyMasterDao();

    public abstract HarvesterDao harvesterDao();

    public abstract HarvMasterDao harvMasterDao();

    public abstract IrrigationMasterDao irrigationMasterDao();

    public abstract PlaceMasterDao placeMasterDao();

    public abstract RouteMasterDao routeMasterDao();

    public abstract RowToRowDirectionDao rowDirectionDao();

    public abstract SoilMasterDao soilMasterDao();

    public abstract SupplyMasterDao supplyMasterDao();

    public abstract TransporterDao transporterDao();

    public abstract VarietyMasterDao varietyMasterDao();

    public abstract WaterSourceDao waterSourceDao();

    public abstract WaterTypeDao waterTypeDao();

    private static volatile ViplDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static ViplDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ViplDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ViplDatabase.class, "vipl_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    void clearAllPreviousRecord() {
        accountMasterDao().deleteAll();
        dev1Dao().deleteAll();
        dev2Dao().deleteAll();
        dev3Dao().deleteAll();
        dev4Dao().deleteAll();
        dev5Dao().deleteAll();
        distanceMasterDao().deleteAll();
        cropTypeDao().deleteAll();
        elevationMasterDao().deleteAll();
        qualitiyMasterDao().deleteAll();
        harvesterDao().deleteAll();
        harvMasterDao().deleteAll();
        interStateDao().deleteAll();
        irrigationMasterDao().deleteAll();
        placeMasterDao().deleteAll();
        plantSeedDao().deleteAll();
        prevCropDao().deleteAll();
        routeMasterDao().deleteAll();
        rowDirectionDao().deleteAll();
        soilMasterDao().deleteAll();
        supplyMasterDao().deleteAll();
        transporterDao().deleteAll();
        varietyMasterDao().deleteAll();
        waterSourceDao().deleteAll();
        waterTypeDao().deleteAll();
    }

}
