package in.visibleinfotech.viplfieldapplications.field_survey.localdatabase;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import java.util.List;

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

public class ViplViewModel extends AndroidViewModel {
    private ViplDatabase viplDatabase;
    MutableLiveData<String> villageIdLiveData = new MutableLiveData<>();
    private LiveData<List<CaneDev4>> cane4LiveData;
    private LiveData<List<CaneDev5>> cane5LiveData;
    private LiveData<List<CaneRowDistanceMaster>> caneRowLiveData;
    private LiveData<List<CropType>> cropTypeLiveData;
    private LiveData<List<FieldElivationTypeMaster>> fieldElevationLiveData;
    private LiveData<List<FieldQualityMaster>> fieldQualityLiveData;
    private LiveData<List<Harvester>> harvesterLiveData;
    private LiveData<List<HarvMaster>> harvMasterLiveData;
    private LiveData<List<InterCrop>> interCropLiveData;
    private LiveData<List<IrrigationMaster>> irrigationLiveData;
    private LiveData<List<PlaceMaster>> placeLiveData;
    private LiveData<List<PlantSeed>> seedLiveData;
    private LiveData<List<PrevCrop>> prevLiveData;
    private LiveData<List<RowToRowFieldMapDirection>> fieldDirectionLiveData;
    private LiveData<List<SoilMaster>> soilLiveData;
    private LiveData<List<SupplyMaster>> supplyLiveData;
    private LiveData<List<Transporter>> transporterLiveData;
    private LiveData<List<VarietyMaster>> varietyLiveData;
    private LiveData<List<WaterSourceMaster>> waterSourceLiveData;
    private LiveData<List<WaterType>> waterTypeLiveData;

    public ViplViewModel(@NonNull Application application) {
        super(application);
        viplDatabase = ViplDatabase.getDatabase(application);
        cane4LiveData = viplDatabase.dev4Dao().getDev4Options();
        cane5LiveData = viplDatabase.dev5Dao().getDev5Options();
        caneRowLiveData = viplDatabase.distanceMasterDao().getCaneRows();
        cropTypeLiveData = viplDatabase.cropTypeDao().getCropTypes();
        fieldElevationLiveData = viplDatabase.elevationMasterDao().getFieldElevations();
        fieldQualityLiveData = viplDatabase.qualitiyMasterDao().getFieldQualities();
        harvesterLiveData = viplDatabase.harvesterDao().getHarvesters();
        harvMasterLiveData = viplDatabase.harvMasterDao().getHarvMasters();
        interCropLiveData = viplDatabase.interStateDao().getAllInterCrops();
        irrigationLiveData = viplDatabase.irrigationMasterDao().getIrrigationMasters();
        placeLiveData = viplDatabase.placeMasterDao().getPlaceMasters();
        seedLiveData = viplDatabase.plantSeedDao().getAllPlantSeeds();
        prevLiveData = viplDatabase.prevCropDao().getAllPrevCrop();

        fieldDirectionLiveData = viplDatabase.rowDirectionDao().getRowDirections();
        soilLiveData = viplDatabase.soilMasterDao().getSoils();
        supplyLiveData = viplDatabase.supplyMasterDao().getSupplyOptions();
        transporterLiveData = viplDatabase.transporterDao().getTransporters();
        varietyLiveData = viplDatabase.varietyMasterDao().getVarieties();
        waterSourceLiveData = viplDatabase.waterSourceDao().getWaterSources();
        waterTypeLiveData = viplDatabase.waterTypeDao().getWaterTypes();

    }



    public LiveData<List<CaneDev1>> getCane1LiveData() {
        return viplDatabase.dev1Dao().getDev1Options();
    }

    public LiveData<List<CaneDev2>> getCane2LiveData() {
        return viplDatabase.dev2Dao().getDev2Options();
    }

    public LiveData<List<CaneDev3>> getCane3LiveData() {
       return viplDatabase.dev3Dao().getDev3Options();
    }

    public LiveData<List<CaneDev4>> getCane4LiveData() {
        return cane4LiveData;
    }

    public LiveData<List<CaneDev5>> getCane5LiveData() {
        return cane5LiveData;
    }

    public LiveData<List<CaneRowDistanceMaster>> getCaneRowLiveData() {
        return caneRowLiveData;
    }

    public LiveData<List<CropType>> getCropTypeLiveData() {
        return cropTypeLiveData;
    }

    public LiveData<List<FieldElivationTypeMaster>> getFieldElevationLiveData() {
        return fieldElevationLiveData;
    }

    public LiveData<List<FieldQualityMaster>> getFieldQualityLiveData() {
        return fieldQualityLiveData;
    }

    public LiveData<List<Harvester>> getHarvesterLiveData() {
        return harvesterLiveData;
    }

    public LiveData<List<HarvMaster>> getHarvMasterLiveData() {
        return harvMasterLiveData;
    }

    public LiveData<List<InterCrop>> getInterCropLiveData() {
        return interCropLiveData;
    }

    public LiveData<List<IrrigationMaster>> getIrrigationLiveData() {
        return irrigationLiveData;
    }

    public LiveData<List<PlaceMaster>> getPlaceLiveData() {
        return placeLiveData;
    }

    public LiveData<List<PlantSeed>> getSeedLiveData() {
        return seedLiveData;
    }

    public LiveData<List<PrevCrop>> getPrevLiveData() {
        return prevLiveData;
    }

    public LiveData<List<RouteMaster>> getRoutesLiveData(String id) {
        return viplDatabase.routeMasterDao().getRoutes(id);
    }

    public LiveData<List<AccountMaster>> getAccountLiveData(String villageId) {
         return  viplDatabase.accountMasterDao().getAccountMasters(villageId);
    }

    public LiveData<List<RowToRowFieldMapDirection>> getFieldDirectionLiveData() {
        return fieldDirectionLiveData;
    }

    public LiveData<List<SoilMaster>> getSoilLiveData() {
        return soilLiveData;
    }

    public LiveData<List<SupplyMaster>> getSupplyLiveData() {
        return supplyLiveData;
    }

    public LiveData<List<Transporter>> getTransporterLiveData() {
        return transporterLiveData;
    }

    public LiveData<List<VarietyMaster>> getVarietyLiveData() {
        return varietyLiveData;
    }

    public LiveData<List<WaterSourceMaster>> getWaterSourceLiveData() {
        return waterSourceLiveData;
    }

    public LiveData<List<WaterType>> getWaterTypeLiveData() {
        return waterTypeLiveData;
    }
}
