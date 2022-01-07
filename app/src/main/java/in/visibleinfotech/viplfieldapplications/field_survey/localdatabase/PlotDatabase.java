package in.visibleinfotech.viplfieldapplications.field_survey.localdatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import in.visibleinfotech.viplfieldapplications.field_survey.models.Plot;

public class PlotDatabase extends SQLiteOpenHelper {

    Context context;

    public PlotDatabase(Context context) {
        super(context, "VIPL2", null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public int getNumOfPlot(String farmerCode) {
        createDB();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query("PLOT_SURVEY", null, "PL_GrowerCode = ?", new String[]{farmerCode}, null, null, null);
        return c.getCount();
    }

    public ArrayList<String> savedFarmers() {
        Set<String> farmerNames = new HashSet<>();
        createDB();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query("PLOT_SURVEY", null, null, null, null, null, null);
        if (c != null && c.moveToFirst()) {
            do {
                String farmerName = c.getString(c.getColumnIndex("PL_GName"));
                String farmerCode = c.getString(c.getColumnIndex("PL_GrowerCode"));
                farmerNames.add(farmerCode + " : " + farmerName);
            } while (c.moveToNext());
        }
        return new ArrayList<>(farmerNames);
    }

    public ArrayList<String> savedPlots(String farmerCode) {
        ArrayList<String> farmerNames = new ArrayList<>();
        farmerNames.add("Select Plot -> ");
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query("PLOT_SURVEY", null, "PL_GrowerCode = ? and PL_Upload = 0", new String[]{farmerCode}, null, null, null);
        if (c != null && c.moveToFirst()) {
            do {
                String plotLand = c.getString(c.getColumnIndex("PL_LandName"));
                String plotNumber = c.getString(c.getColumnIndex("PL_PlotNumber"));
                farmerNames.add(plotNumber + " <-> " + plotLand);
            } while (c.moveToNext());
        }
        return farmerNames;
    }

    public int checkExists(String PL_GrowerCode, String PL_Plotvillcode, String PL_LandName) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query("PLOT_SURVEY", null, "PL_GrowerCode = ? and PL_Plotvillcode = ? and PL_LandName = ?", new String[]{PL_GrowerCode, PL_Plotvillcode, PL_LandName}, null, null, null);
        return c.getCount();
    }

    public int updateUpload(String plot_id) {
        createDB();
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("PL_Upload", 1);
        return db.update("PLOT_SURVEY", values, "PL_PlotNumber = ?", new String[]{plot_id.trim()});

    }

    public int updateCompleted(String plot_id) {
        createDB();
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("PL_Upload", 0);
        return db.update("PLOT_SURVEY", values, "PL_PlotNumber = ?", new String[]{plot_id.trim()});

    }

    public int deletePlot(String plot_id) {
        createDB();
        SQLiteDatabase db = getWritableDatabase();
        return db.delete("PLOT_SURVEY", "PL_PlotNumber = ?", new String[]{plot_id.trim()});
    }

    public ArrayList<Plot> getUploadedPlots() {
        createDB();
        ArrayList<Plot> plots = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query("PLOT_SURVEY", null, " PL_Upload = 1", null, null, null, null);
        if (c != null && c.moveToFirst()) {
            do {
                String PL_SiteCode = c.getString(c.getColumnIndex("PL_SiteCode"));
                String PL_PlotNumber = c.getString(c.getColumnIndex("PL_PlotNumber"));
                String PL_GrowerCode = c.getString(c.getColumnIndex("PL_GrowerCode"));
                String PL_VillageCode = c.getString(c.getColumnIndex("PL_VillageCode"));
                String PL_PlantationDate = c.getString(c.getColumnIndex("PL_PlantationDate"));
                String PL_CropId = c.getString(c.getColumnIndex("PL_CropId"));
                String PL_CaneId = c.getString(c.getColumnIndex("PL_CaneId"));
                String PL_Area = c.getString(c.getColumnIndex("PL_Area"));
                String PL_LandName = c.getString(c.getColumnIndex("PL_LandName"));
                String PL_Enable = c.getString(c.getColumnIndex("PL_Enable"));
                String PL_FieldType = c.getString(c.getColumnIndex("PL_FieldType"));
                String PL_CuttingDate = c.getString(c.getColumnIndex("PL_CuttingDate"));
                String PL_FieldElivation = c.getString(c.getColumnIndex("PL_FieldElivation"));
                String PL_EstTons = c.getString(c.getColumnIndex("PL_EstTons"));
                String PL_ActualArea = c.getString(c.getColumnIndex("PL_ActualArea"));
                String PL_Road = c.getString(c.getColumnIndex("PL_Road"));
                String PL_Enterarea = c.getString(c.getColumnIndex("PL_Enterarea"));
                String PL_EntType = c.getString(c.getColumnIndex("PL_EntType"));
                String PL_HarvMode = c.getString(c.getColumnIndex("PL_HarvMode"));
                String PL_Plotvillcode = c.getString(c.getColumnIndex("PL_Plotvillcode"));
                String PL_GName = c.getString(c.getColumnIndex("PL_GName"));
                String PL_VillName = c.getString(c.getColumnIndex("PL_VillName"));
                String PL_VarName = c.getString(c.getColumnIndex("PL_VarName"));
                String PL_CropName = c.getString(c.getColumnIndex("PL_CropName"));
                String PL_Upload = c.getString(c.getColumnIndex("PL_Upload"));
                String PL_RoadDistance = c.getString(c.getColumnIndex("PL_RoadDistance"));
                String irrigationCode = c.getString(c.getColumnIndex("irrigationCode"));
                String isSoilTested = c.getString(c.getColumnIndex("isSoilTested"));
                String isWaterTested = c.getString(c.getColumnIndex("isWaterTested"));
                String waterCode = c.getString(c.getColumnIndex("waterCode"));
                String waterType = c.getString(c.getColumnIndex("waterType"));
                String supplymode = c.getString(c.getColumnIndex("supplymode"));
                String soilCode = c.getString(c.getColumnIndex("soilCode"));
                String rowDirection = c.getString(c.getColumnIndex("rowDirection"));
                String tressMalching = c.getString(c.getColumnIndex("tressMalching"));
                String kashraNum = c.getString(c.getColumnIndex("khashraNum"));
                String remark = c.getString(c.getColumnIndex("PL_remark"));
                String seed = c.getString(c.getColumnIndex("seed"));
                String interCrop = c.getString(c.getColumnIndex("interCrop"));
                String prevCrop = c.getString(c.getColumnIndex("prevCrop"));
                String dev1Option = c.getString(c.getColumnIndex("dev1Option"));
                String dev2Option = c.getString(c.getColumnIndex("dev2Option"));
                String dev3Option = c.getString(c.getColumnIndex("dev3Option"));
                String dev4Option = c.getString(c.getColumnIndex("dev4Option"));
                String dev5Option = c.getString(c.getColumnIndex("dev5Option"));
                Plot p = new Plot(PL_SiteCode, PL_PlotNumber, PL_GrowerCode, PL_VillageCode, PL_PlantationDate, PL_CropId, PL_CaneId, PL_Area, PL_LandName, PL_Enable, PL_FieldType, PL_FieldElivation, PL_CuttingDate,
                        PL_EstTons, PL_ActualArea, PL_Road, PL_Enterarea, PL_EntType, PL_HarvMode, PL_Plotvillcode, PL_GName, PL_VillName, PL_CropName, PL_VarName, PL_Upload, PL_RoadDistance,
//                        "0", "n", "n", "0", "0", "0", "0", "0", "n");
                        irrigationCode, isSoilTested, isWaterTested, waterCode, waterType, supplymode, soilCode, rowDirection, tressMalching, kashraNum, remark, seed, interCrop, prevCrop, dev1Option, dev2Option, dev3Option, dev4Option, dev5Option);
                plots.add(p);

            } while (c.moveToNext());
        }
        return plots;

    }

    public ArrayList<Plot> getCompletedPlots() {
        createDB();
        ArrayList<Plot> plots = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query("PLOT_SURVEY", null, "PL_PlantationDate != 'null' and PL_Upload = 0", null, null, null, null);
        if (c != null && c.moveToFirst()) {
            do {
                String PL_SiteCode = c.getString(c.getColumnIndex("PL_SiteCode"));
                String PL_PlotNumber = c.getString(c.getColumnIndex("PL_PlotNumber"));
                String PL_GrowerCode = c.getString(c.getColumnIndex("PL_GrowerCode"));
                String PL_VillageCode = c.getString(c.getColumnIndex("PL_VillageCode"));
                String PL_PlantationDate = c.getString(c.getColumnIndex("PL_PlantationDate"));
                String PL_CropId = c.getString(c.getColumnIndex("PL_CropId"));
                String PL_CaneId = c.getString(c.getColumnIndex("PL_CaneId"));
                String PL_Area = c.getString(c.getColumnIndex("PL_Area"));
                String PL_LandName = c.getString(c.getColumnIndex("PL_LandName"));
                String PL_Enable = c.getString(c.getColumnIndex("PL_Enable"));
                String PL_FieldType = c.getString(c.getColumnIndex("PL_FieldType"));
                String PL_CuttingDate = c.getString(c.getColumnIndex("PL_CuttingDate"));
                String PL_FieldElivation = c.getString(c.getColumnIndex("PL_FieldElivation"));
                String PL_EstTons = c.getString(c.getColumnIndex("PL_EstTons"));
                String PL_ActualArea = c.getString(c.getColumnIndex("PL_ActualArea"));
                String PL_Road = c.getString(c.getColumnIndex("PL_Road"));
                String PL_Enterarea = c.getString(c.getColumnIndex("PL_Enterarea"));
                String PL_EntType = c.getString(c.getColumnIndex("PL_EntType"));
                String PL_HarvMode = c.getString(c.getColumnIndex("PL_HarvMode"));
                String PL_Plotvillcode = c.getString(c.getColumnIndex("PL_Plotvillcode"));
                String PL_GName = c.getString(c.getColumnIndex("PL_GName"));
                String PL_VillName = c.getString(c.getColumnIndex("PL_VillName"));
                String PL_VarName = c.getString(c.getColumnIndex("PL_VarName"));
                String PL_CropName = c.getString(c.getColumnIndex("PL_CropName"));
                String PL_Upload = c.getString(c.getColumnIndex("PL_Upload"));
                String PL_RoadDistance = c.getString(c.getColumnIndex("PL_RoadDistance"));
                String irrigationCode = c.getString(c.getColumnIndex("irrigationCode"));
                String isSoilTested = c.getString(c.getColumnIndex("isSoilTested"));
                String isWaterTested = c.getString(c.getColumnIndex("isWaterTested"));
                String waterCode = c.getString(c.getColumnIndex("waterCode"));
                String waterType = c.getString(c.getColumnIndex("waterType"));
                String supplymode = c.getString(c.getColumnIndex("supplymode"));
                String soilCode = c.getString(c.getColumnIndex("soilCode"));
                String rowDirection = c.getString(c.getColumnIndex("rowDirection"));
                String tressMalching = c.getString(c.getColumnIndex("tressMalching"));
                String khashraNum = c.getString(c.getColumnIndex("khashraNum"));
                String remark = c.getString(c.getColumnIndex("PL_remark"));
                String seed = c.getString(c.getColumnIndex("seed"));
                String interCrop = c.getString(c.getColumnIndex("interCrop"));
                String prevCrop = c.getString(c.getColumnIndex("prevCrop"));
                String dev1Option = c.getString(c.getColumnIndex("dev1Option"));
                String dev2Option = c.getString(c.getColumnIndex("dev2Option"));
                String dev3Option = c.getString(c.getColumnIndex("dev3Option"));
                String dev4Option = c.getString(c.getColumnIndex("dev4Option"));
                String dev5Option = c.getString(c.getColumnIndex("dev5Option"));
                Plot p = new Plot(PL_SiteCode, PL_PlotNumber, PL_GrowerCode, PL_VillageCode, PL_PlantationDate, PL_CropId, PL_CaneId, PL_Area, PL_LandName, PL_Enable, PL_FieldType, PL_FieldElivation, PL_CuttingDate,
                        PL_EstTons, PL_ActualArea, PL_Road, PL_Enterarea, PL_EntType, PL_HarvMode, PL_Plotvillcode, PL_GName, PL_VillName, PL_CropName, PL_VarName, PL_Upload, PL_RoadDistance,
//                        "0", "n", "n", "0", "0", "0", "0", "0", "n");
                        irrigationCode, isSoilTested, isWaterTested, waterCode, waterType, supplymode, soilCode, rowDirection, tressMalching, khashraNum, remark, seed, interCrop, prevCrop, dev1Option, dev2Option, dev3Option, dev4Option, dev5Option);
                plots.add(p);

            } while (c.moveToNext());
        }
        return plots;
    }

    public ArrayList<Plot> getCreatedPlots() {
        createDB();
        ArrayList<Plot> plots = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query("PLOT_SURVEY", null, "PL_PlantationDate = 'null'", null, null, null, null);
        if (c != null && c.moveToFirst()) {
            do {
                String PL_SiteCode = c.getString(c.getColumnIndex("PL_SiteCode"));
                String PL_PlotNumber = c.getString(c.getColumnIndex("PL_PlotNumber"));
                String PL_GrowerCode = c.getString(c.getColumnIndex("PL_GrowerCode"));
                String PL_VillageCode = c.getString(c.getColumnIndex("PL_VillageCode"));
                String PL_PlantationDate = c.getString(c.getColumnIndex("PL_PlantationDate"));
                String PL_CropId = c.getString(c.getColumnIndex("PL_CropId"));
                String PL_CaneId = c.getString(c.getColumnIndex("PL_CaneId"));
                String PL_Area = c.getString(c.getColumnIndex("PL_Area"));
                String PL_LandName = c.getString(c.getColumnIndex("PL_LandName"));
                String PL_Enable = c.getString(c.getColumnIndex("PL_Enable"));
                String PL_FieldType = c.getString(c.getColumnIndex("PL_FieldType"));
                String PL_CuttingDate = c.getString(c.getColumnIndex("PL_CuttingDate"));
                String PL_FieldElivation = c.getString(c.getColumnIndex("PL_FieldElivation"));
                String PL_EstTons = c.getString(c.getColumnIndex("PL_EstTons"));
                String PL_ActualArea = c.getString(c.getColumnIndex("PL_ActualArea"));
                String PL_Road = c.getString(c.getColumnIndex("PL_Road"));
                String PL_Enterarea = c.getString(c.getColumnIndex("PL_Enterarea"));
                String PL_EntType = c.getString(c.getColumnIndex("PL_EntType"));
                String PL_HarvMode = c.getString(c.getColumnIndex("PL_HarvMode"));
                String PL_Plotvillcode = c.getString(c.getColumnIndex("PL_Plotvillcode"));
                String PL_GName = c.getString(c.getColumnIndex("PL_GName"));
                String PL_VillName = c.getString(c.getColumnIndex("PL_VillName"));
                String PL_VarName = c.getString(c.getColumnIndex("PL_VarName"));
                String PL_CropName = c.getString(c.getColumnIndex("PL_CropName"));
                String PL_Upload = c.getString(c.getColumnIndex("PL_Upload"));
                String PL_RoadDistance = c.getString(c.getColumnIndex("PL_RoadDistance"));
                String irrigationCode = c.getString(c.getColumnIndex("irrigationCode"));
                String isSoilTested = c.getString(c.getColumnIndex("isSoilTested"));
                String isWaterTested = c.getString(c.getColumnIndex("isWaterTested"));
                String waterCode = c.getString(c.getColumnIndex("waterCode"));
                String waterType = c.getString(c.getColumnIndex("waterType"));
                String supplymode = c.getString(c.getColumnIndex("supplymode"));
                String soilCode = c.getString(c.getColumnIndex("soilCode"));
                String rowDirection = c.getString(c.getColumnIndex("rowDirection"));
                String tressMalching = c.getString(c.getColumnIndex("tressMalching"));
                String kashraNum = c.getString(c.getColumnIndex("khashraNum"));
                String remark = c.getString(c.getColumnIndex("PL_remark"));
                String seed = c.getString(c.getColumnIndex("seed"));
                String interCrop = c.getString(c.getColumnIndex("interCrop"));
                String prevCrop = c.getString(c.getColumnIndex("prevCrop"));
                String dev1Option = c.getString(c.getColumnIndex("dev1Option"));
                String dev2Option = c.getString(c.getColumnIndex("dev2Option"));
                String dev3Option = c.getString(c.getColumnIndex("dev3Option"));
                String dev4Option = c.getString(c.getColumnIndex("dev4Option"));
                String dev5Option = c.getString(c.getColumnIndex("dev5Option"));
                Plot p = new Plot(PL_SiteCode, PL_PlotNumber, PL_GrowerCode, PL_VillageCode, PL_PlantationDate, PL_CropId, PL_CaneId, PL_Area, PL_LandName, PL_Enable, PL_FieldType, PL_FieldElivation, PL_CuttingDate,
                        PL_EstTons, PL_ActualArea, PL_Road, PL_Enterarea, PL_EntType, PL_HarvMode, PL_Plotvillcode, PL_GName, PL_VillName, PL_CropName, PL_VarName, PL_Upload, PL_RoadDistance,
//                        "0", "n", "n", "0", "0", "0", "0", "0", "n");
                        irrigationCode, isSoilTested, isWaterTested, waterCode, waterType, supplymode, soilCode, rowDirection, tressMalching, kashraNum, remark, seed, interCrop, prevCrop, dev1Option, dev2Option, dev3Option, dev4Option, dev5Option);
                plots.add(p);

            } while (c.moveToNext());
        }
        return plots;
    }

    private void createDB() {
        SQLiteDatabase db = getWritableDatabase();
        String query = "CREATE TABLE IF NOT EXISTS PLOT_SURVEY(PL_SiteCode TEXT, PL_PlotNumber TEXT, PL_GrowerCode TEXT, PL_VillageCode TEXT, PL_PlantationDate TEXT," +
                "PL_CropId TEXT, PL_CaneId, PL_Area TEXT, PL_LandName TEXT, PL_Enable TEXT, PL_FieldType TEXT, PL_FieldElivation TEXT," +
                " PL_CuttingDate TEXT, PL_EstTons TEXT, PL_ActualArea TEXT, PL_Road TEXT, PL_Enterarea TEXT, PL_EntType TEXT, PL_HarvMode TEXT, PL_Plotvillcode TEXT," +
                "PL_GName TEXT, PL_VillName TEXT, PL_CropName TEXT, PL_VarName TEXT, PL_Upload TEXT,PL_RoadDistance TEXT,irrigationCode TEXT,isSoilTested TEXT,isWaterTested TEXT," +
                "waterCode TEXT,waterType TEXT,supplymode TEXT,soilCode TEXT,rowDirection TEXT,tressMalching TEXT,khashraNum TEXT,PL_remark TEXT," +
                "seed text,interCrop text,prevCrop text,dev1Option text,dev2Option text,dev3Option text,dev4Option text,dev5Option text);";
        db.execSQL(query);
        db.close();
    }

    public long addPlot(Plot plot) {
        createDB();
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("PL_SiteCode", plot.getPL_SiteCode());

        values.put("PL_PlotNumber", plot.getPL_PlotNumber());
        values.put("PL_GrowerCode", plot.getPL_GrowerCode());
        values.put("PL_VillageCode", plot.getPL_VillageCode());
        values.put("PL_PlantationDate", plot.getPL_PlantationDate());
        values.put("PL_CropId", plot.getPL_CropId());
        values.put("PL_CaneId", plot.getPL_CaneId());
        values.put("PL_Area", plot.getPL_Area());
        values.put("PL_LandName", plot.getPL_LandName());
        values.put("PL_Enable", plot.getPL_Enable());
        values.put("PL_FieldType", plot.getPL_FieldType());
        values.put("PL_FieldElivation", plot.getPL_FieldElivation());
        values.put("PL_HarvMode", plot.getPL_HarvMode());
        values.put("PL_CuttingDate", plot.getPL_CuttingDate());
        values.put("PL_EstTons", plot.getPL_EstTons());
        values.put("PL_ActualArea", plot.getPL_ActualArea());
        values.put("PL_Road", plot.getPL_Road());
        values.put("PL_Enterarea", plot.getPL_Enterarea());
        values.put("PL_EntType", plot.getPL_EntType());
        values.put("PL_Plotvillcode", plot.getPL_Plotvillcode());
        values.put("PL_GName", plot.getPL_GName());
        values.put("PL_VillName", plot.getPL_VillName());
        values.put("PL_CropName", plot.getPL_CropName());
        values.put("PL_VarName", plot.getPL_VarName());
        values.put("PL_Upload", plot.getPL_Upload());
        values.put("PL_RoadDistance", plot.getPL_RoadDistance());
        values.put("irrigationCode", plot.getIrrigationCode());
        values.put("isSoilTested", plot.getIsSoilTested());
        values.put("isWaterTested", plot.getIsWaterTested());
        values.put("waterCode", plot.getWaterCode());
        values.put("waterType", plot.getWaterType());
        values.put("supplymode", plot.getSupplymode());
        values.put("soilCode", plot.getSoilCode());
        values.put("rowDirection", plot.getRowDirection());
        values.put("tressMalching", plot.getTressMalching());
        values.put("khashraNum", plot.getKashraNum());
        values.put("seed", plot.getSeed());
        values.put("interCrop", plot.getInterCrop());
        values.put("prevCrop", plot.getPrevCrop());
        values.put("dev1Option", plot.getDev1Option());
        values.put("dev2Option", plot.getDev2Option());
        values.put("dev3Option", plot.getDev3Option());
        values.put("dev4Option", plot.getDev4Option());
        values.put("dev5Option", plot.getDev5Option());

        return db.insert("PLOT_SURVEY", null, values);
    }

    public void updatePlot(String plot_id, String PL_PlantationDate, String PL_CropId, String PL_CaneId, String PL_Area, String PL_ActualArea, String PL_Enterarea, String PL_CropName, String PL_VarName, String distance,
                           String irrigationCode, String isSoilTested, String isWaterTested, String waterCode, String waterType, String supplymode, String soilCode, String rowDirection, String tressMalching, String fieldElevation, String fieldType, String harvestingType, String remark,
                           String seed, String interCrop, String prevCrop, String dev1Option, String dev2Option, String dev3Option, String dev4Option, String dev5Option) {
        createDB();
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("PL_PlantationDate", PL_PlantationDate);
        values.put("PL_CropId", PL_CropId);
        values.put("PL_CaneId", PL_CaneId);
        values.put("PL_Area", PL_Area);
        values.put("PL_ActualArea", PL_ActualArea);
        values.put("PL_Enterarea", PL_Enterarea);
        values.put("PL_CropName", PL_CropName);
        values.put("PL_VarName", PL_VarName);
        values.put("PL_RoadDistance", distance);
        values.put("PL_Upload", "0");
        values.put("PL_remark", remark);
        if (!fieldType.equals("null")) values.put("PL_FieldType", fieldType);
        if (!fieldElevation.equals("null")) values.put("PL_FieldElivation", fieldElevation);
        if (!harvestingType.equals("null")) values.put("PL_HarvMode", harvestingType);
        if (!irrigationCode.equals("0")) values.put("irrigationCode", irrigationCode);
        if (!isSoilTested.equals("null")) values.put("isSoilTested", isSoilTested);
        if (!isWaterTested.equals("null")) values.put("isWaterTested", isWaterTested);
        if (!waterCode.equals("0")) values.put("waterCode", waterCode);
        if (!waterType.equals("0")) values.put("waterType", waterType);
        if (!supplymode.equals("0")) values.put("supplymode", supplymode);
        if (!soilCode.equals("0")) values.put("soilCode", soilCode);
        if (!rowDirection.equals("0")) values.put("rowDirection", rowDirection);
        if (!tressMalching.equals("null")) values.put("tressMalching", tressMalching);
        if (!seed.equals("null")) values.put("seed", seed);
        if (!interCrop.equals("null")) values.put("interCrop", interCrop);
        if (!prevCrop.equals("null")) values.put("prevCrop", prevCrop);
        if (!dev1Option.equals("null")) values.put("dev1Option", dev1Option);
        if (!dev2Option.equals("null")) values.put("dev2Option", dev2Option);
        if (!dev3Option.equals("null")) values.put("dev3Option", dev3Option);
        if (!dev4Option.equals("null")) values.put("dev4Option", dev4Option);
        if (!dev5Option.equals("null")) values.put("dev5Option", dev5Option);


        int c = db.update("PLOT_SURVEY", values, "PL_PlotNumber = ?", new String[]{plot_id.trim()});
        Toast.makeText(context, "Records Updated = " + c, Toast.LENGTH_SHORT).show();
    }

    public void addPlotCoordinates(String plot_id, ArrayList<LatLng> latLngs) {
        createCordinateTable();
        SQLiteDatabase db = getWritableDatabase();

        for (LatLng latLng : latLngs) {
            ContentValues values = new ContentValues();
            values.put("plot_id", plot_id);
            values.put("lattitude", latLng.latitude);
            values.put("longitudes", latLng.longitude);
            db.insert("PLOT_COORDINATES", null, values);
        }
        Toast.makeText(context, "all coorinates saved successfully", Toast.LENGTH_SHORT).show();
    }

    public int getNumOfCoordinates(String plot_id) {
        createCordinateTable();
        SQLiteDatabase db = getReadableDatabase();
        return db.query("PLOT_COORDINATES", null, "plot_id = ?", new String[]{plot_id}, null, null, null).getCount();
    }

    public int removeCoordinates(String plot_id) {
        createCordinateTable();
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("PL_PlantationDate", "null");
        values.put("PL_CropId", "null");
        values.put("PL_CaneId", "null");
        values.put("PL_Area", "null");
        values.put("PL_ActualArea", "null");
        values.put("PL_Enterarea", "null");
        values.put("PL_CropName", "null");
        values.put("PL_VarName", "null");
        values.put("PL_RoadDistance", "null");
        values.put("PL_Upload", "0");
        db.update("PLOT_SURVEY", values, "PL_PlotNumber = ?", new String[]{plot_id.trim()});

        return db.delete("PLOT_COORDINATES", "plot_id = ?", new String[]{plot_id});
    }

    void createCordinateTable() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("Create table if not exists PLOT_COORDINATES(plot_id text,lattitude text,longitudes text)");
        db.close();
    }

    public ArrayList<LatLng> getCoordinates(String plot_id) {
        createCordinateTable();
        ArrayList<LatLng> latLngs = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query("PLOT_COORDINATES", null, "plot_id = ?", new String[]{plot_id}, null, null, null);
        if (c != null && c.moveToFirst()) {
            do {
                double latti = Double.parseDouble(c.getString(c.getColumnIndex("lattitude")));
                double longi = Double.parseDouble(c.getString(c.getColumnIndex("longitudes")));
                latLngs.add(new LatLng(latti, longi));
            } while (c.moveToNext());
        }
        return latLngs;
    }
}
