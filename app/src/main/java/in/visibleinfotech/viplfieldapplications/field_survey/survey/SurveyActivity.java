package in.visibleinfotech.viplfieldapplications.field_survey.survey;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import in.visibleinfotech.viplfieldapplications.MainConstant;
import in.visibleinfotech.viplfieldapplications.R;
import in.visibleinfotech.viplfieldapplications.field_survey.localdatabase.PlotDatabase;
import in.visibleinfotech.viplfieldapplications.field_survey.localdatabase.ViplViewModel;
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
import in.visibleinfotech.viplfieldapplications.field_survey.models.InterCrop;
import in.visibleinfotech.viplfieldapplications.field_survey.models.IrrigationMaster;
import in.visibleinfotech.viplfieldapplications.field_survey.models.PlantSeed;
import in.visibleinfotech.viplfieldapplications.field_survey.models.PrevCrop;
import in.visibleinfotech.viplfieldapplications.field_survey.models.SoilMaster;
import in.visibleinfotech.viplfieldapplications.field_survey.models.SupplyMaster;
import in.visibleinfotech.viplfieldapplications.field_survey.models.VarietyMaster;
import in.visibleinfotech.viplfieldapplications.field_survey.models.WaterSourceMaster;
import in.visibleinfotech.viplfieldapplications.field_survey.models.WaterType;

public class SurveyActivity extends FragmentActivity {


    Spinner caneVarietyActv, cropTypeActv, plotNameSpinner, roadDistanceSpinner;
    TextView dateSelectEt, plotCalculatedAreaET;
    EditText plotEnteredAreaET, remarkEt;
    AppCompatAutoCompleteTextView farmerNameActv;
    String caneId, cropID;
    ArrayList<String> varietyIds;
    ArrayList<String> cropIds;

    String datte = null;
    SharedPreferences preferences;
    private LinearLayout irriLayout, wSourcelayout, wtypeLayout, sModeLayout, roadDistancelayout, fieldLayout, elevationlayout, sTypelayout, rorLayout, harvTypeLayout, sTestedlayout, wtestedlayout, tressMalchingLayout,
            seedLayout, inteLayout, prevLayout, dev1Layout, dev2Layout, dev3Layout, dev4Layout, dev5Layout;

    private Spinner harvtypeActv, farmerFieldElivationActv, irriSpinner, wSourceSpinner, wtypeSpinner, sModeSpinner, fieldSpinner, sTypeSpinner, rorSpinner,
            seedSpinner, interSpinner, prevSpinner, dev1SPinner, dev2Spinner, dev3Spinner, dev4Spinner, dev5Spinner;

    private RadioGroup sTestedGroup, wtestedGroup, tressGroup;
    private String place_id = "null", farmerCode = "null", farmerVillage = "null", plotLand = "null", plotVillage = "null", plotNearestRoad = "null", roadDistance = "null", irrigationCode = "0", isSoilTested = "null",
            isWaterTested = "null", waterCode = "0", waterType = "0", supplymode = "0", fieldType = "null", fieldElevation = "null", soilCode = "0", rorDistance = "0",
            harvestingType = "null", tressMalching = "null", seed = "-1", prevCrop = "-1", interCrop = "-1", dev1Option = "-1", dev2Option = "-1", dev3Option = "-1", dev4Option = "-1", dev5Option = "-1";
    ViplViewModel viplViewModel;

    String imageFilePath = null;
    private boolean image = false;

    //    LinearLayout llmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.survey_activity_survey);
        preferences = getSharedPreferences("Choice", 0);

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    122);
        }
        viplViewModel = ViewModelProviders.of(this).get(ViplViewModel.class);
        try {
            initAndLoad();
            loadPreviousPrefrence();
        } catch (SQLException e) {
            Log.d("myname", e.toString());
            Toast.makeText(this, "Create Plot First", Toast.LENGTH_SHORT).show();
        }


        caneVarietyActv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) caneId = varietyIds.get(position - 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        cropTypeActv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) cropID = cropIds.get(position - 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void loadPreviousPrefrence() {
        if (preferences.getBoolean("irrioption2", false)) {
            irriLayout.setVisibility(View.VISIBLE);
            ArrayList<String> irrigationNames = new ArrayList<>();
            irrigationNames.add("Select Irrigation Type ->");
            final ArrayList<String> irrigationCodes = new ArrayList<>();

            viplViewModel.getIrrigationLiveData().observe(this, new Observer<List<IrrigationMaster>>() {
                @Override
                public void onChanged(List<IrrigationMaster> irrigationMasters) {
                    for (IrrigationMaster master : irrigationMasters) {
                        irrigationNames.add(master.getIr_name());
                        irrigationCodes.add(master.getIr_code());
                    }
                }
            });

            ArrayAdapter ad = new ArrayAdapter(this, R.layout.spinner_layout, R.id.spinnerTv, irrigationNames);
            irriSpinner.setAdapter(ad);
            irriSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position > 0) irrigationCode = irrigationCodes.get(position - 1);
                    else
                        Toast.makeText(SurveyActivity.this, "Nothing is selected", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


        }
        if (preferences.getBoolean("supplyoption2", false)) {
            sModeLayout.setVisibility(View.VISIBLE);
            final ArrayList<String> codes = new ArrayList<>();
            final ArrayList<String> names = new ArrayList<>();
            names.add("Select mode of supply ->");
            viplViewModel.getSupplyLiveData().observe(this, new Observer<List<SupplyMaster>>() {
                @Override
                public void onChanged(List<SupplyMaster> supplyMasters) {
                    for (SupplyMaster supplyMaster : supplyMasters) {
                        names.add(supplyMaster.getM_Name());
                        codes.add(supplyMaster.getM_Code());
                    }
                }
            });

            ArrayAdapter adapter = new ArrayAdapter(this, R.layout.spinner_layout, R.id.spinnerTv, names);
            sModeSpinner.setAdapter(adapter);
            sModeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position > 0) supplymode = codes.get(position - 1);
                    else
                        Toast.makeText(SurveyActivity.this, "nothing is selected", Toast.LENGTH_SHORT).show();

                }


                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


        }
        if (preferences.getBoolean("soiloption2", false)) {
            sTestedlayout.setVisibility(View.VISIBLE);


        }
        if (preferences.getBoolean("waterSourceoption2", false)) {
            wSourcelayout.setVisibility(View.VISIBLE);

            ArrayList<String> waterSources = new ArrayList<>();
            waterSources.add("Select water source ->");
            final ArrayList<String> waterCodes = new ArrayList<>();
            viplViewModel.getWaterSourceLiveData().observe(this, new Observer<List<WaterSourceMaster>>() {
                @Override
                public void onChanged(List<WaterSourceMaster> waterSourceMasters) {
                    for (WaterSourceMaster sourceMaster : waterSourceMasters) {
                        waterSources.add(sourceMaster.getW_watersource());
                        waterCodes.add(sourceMaster.getW_code());
                    }
                }
            });
            ArrayAdapter ad = new ArrayAdapter(this, R.layout.spinner_layout, R.id.spinnerTv, waterSources);
            wSourceSpinner.setAdapter(ad);
            wSourceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position > 0) waterCode = waterCodes.get(position - 1);
                    else
                        Toast.makeText(SurveyActivity.this, "nothing is selected", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        if (preferences.getBoolean("waterTypeoption2", false)) {
            wtypeLayout.setVisibility(View.VISIBLE);

            ArrayList<String> waterNames = new ArrayList<>();
            waterNames.add("Select water type ->");
            final ArrayList<String> waterTypeCodes = new ArrayList<>();
            viplViewModel.getWaterTypeLiveData().observe(this, new Observer<List<WaterType>>() {
                @Override
                public void onChanged(List<WaterType> waterTypes) {
                    for (WaterType type : waterTypes) {
                        waterNames.add(type.getWt_name());
                        waterTypeCodes.add(type.getWt_code());
                    }
                }
            });
            ArrayAdapter ad = new ArrayAdapter(this, R.layout.spinner_layout, R.id.spinnerTv, waterNames);
            wtypeSpinner.setAdapter(ad);
            wtypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position > 0) waterType = waterTypeCodes.get(position - 1);
                    else
                        Toast.makeText(SurveyActivity.this, "Nothing is selected", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }
        if (preferences.getBoolean("waterTestedoption2", false)) {
            wtestedlayout.setVisibility(View.VISIBLE);

        }
        if (preferences.getBoolean("fieldoption2", false)) {
            fieldLayout.setVisibility(View.VISIBLE);

            final ArrayList<String> fieldMasterList = new ArrayList<>();
            fieldMasterList.add("Select Field type ->");
            viplViewModel.getFieldQualityLiveData().observe(this, new Observer<List<FieldQualityMaster>>() {
                @Override
                public void onChanged(List<FieldQualityMaster> fieldQualityMasters) {
                    for (FieldQualityMaster master : fieldQualityMasters) {
                        fieldMasterList.add(master.getFieldName());
                    }
                }
            });
            ArrayAdapter fieldAdapter = new ArrayAdapter(this, R.layout.spinner_layout, R.id.spinnerTv, fieldMasterList);
            fieldSpinner.setAdapter(fieldAdapter);
            fieldSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    fieldType = fieldMasterList.get(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


        }
        if (preferences.getBoolean("elevationoption2", false)) {
            elevationlayout.setVisibility(View.VISIBLE);
            final ArrayList<String> fieldMasterList = new ArrayList<>();
            fieldMasterList.add("Select field elevation ->");
            viplViewModel.getFieldElevationLiveData().observe(this, new Observer<List<FieldElivationTypeMaster>>() {
                @Override
                public void onChanged(List<FieldElivationTypeMaster> fieldElivationTypeMasters) {
                    for (FieldElivationTypeMaster master : fieldElivationTypeMasters) {
                        fieldMasterList.add(master.getFE_Name());
                    }
                }
            });
            ArrayAdapter fieldAdapter = new ArrayAdapter(this, R.layout.spinner_layout, R.id.spinnerTv, fieldMasterList);
            farmerFieldElivationActv.setAdapter(fieldAdapter);
            farmerFieldElivationActv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    fieldElevation = fieldMasterList.get(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }
        if (preferences.getBoolean("harvoption2", false)) {
            harvTypeLayout.setVisibility(View.VISIBLE);
            final ArrayList<String> harvList = new ArrayList<>();
            harvList.add("Select harvesting type ->");
            viplViewModel.getHarvMasterLiveData().observe(this, new Observer<List<HarvMaster>>() {
                @Override
                public void onChanged(List<HarvMaster> harvMasters) {
                    for (HarvMaster master : harvMasters) {
                        harvList.add(master.getH_name());
                    }
                }
            });
            ArrayAdapter harvArrayAdapter = new ArrayAdapter(this, R.layout.spinner_layout, R.id.spinnerTv, harvList);
            harvtypeActv.setAdapter(harvArrayAdapter);
            harvtypeActv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    harvestingType = harvList.get(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


        }
        if (preferences.getBoolean("rowoption2", false)) {
            rorLayout.setVisibility(View.VISIBLE);
            final ArrayList<String> directions = new ArrayList<>();
            final ArrayList<String> codes = new ArrayList<>();
            directions.add("Select Row to Row Distance ->");

            viplViewModel.getCaneRowLiveData().observe(this, new Observer<List<CaneRowDistanceMaster>>() {
                @Override
                public void onChanged(List<CaneRowDistanceMaster> caneRowDistanceMasters) {
                    for (CaneRowDistanceMaster dirction : caneRowDistanceMasters) {
                        directions.add(dirction.getCane_RowFit());
                        codes.add(dirction.getCane_RowCode());
                    }
                }
            });
            ArrayAdapter harvArrayAdapter = new ArrayAdapter(this, R.layout.spinner_layout, R.id.spinnerTv, directions);
            rorSpinner.setAdapter(harvArrayAdapter);
            rorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position > 0) rorDistance = directions.get(position - 1);
                    else
                        Toast.makeText(SurveyActivity.this, "nothing is selected", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }
       /* RouteMasterDb routeMasterDb = new RouteMasterDb(this);
        ArrayList<RouteMaster> routeMasters =routeMasterDb.getRoute();
        if (preferences.getBoolean("nearestRoadoption1", false)) {
            nearestRoadLayout.setVisibility(View.VISIBLE);
            nearestRoadSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    plotNearestRoad = arrayList.get(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }*/
        if (preferences.getBoolean("roadDistanceoption2", false)) {
            roadDistancelayout.setVisibility(View.VISIBLE);
            final ArrayList<String> roadDistances = new ArrayList<>();
            roadDistances.add("Select Distance from Road -> ");
            roadDistances.add("0 - 50 ");
            roadDistances.add("50 - 200");
            roadDistances.add("200 - 500 ");
            roadDistances.add("500 - 1000 ");
            ArrayAdapter roadDistanceAdapter = new ArrayAdapter(this, R.layout.spinner_layout, R.id.spinnerTv, roadDistances);
            roadDistanceSpinner.setAdapter(roadDistanceAdapter);
            roadDistanceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    roadDistance = roadDistances.get(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        if (preferences.getBoolean("seedoption2", false)) {
            seedLayout.setVisibility(View.VISIBLE);

            ArrayList<String> seedOptions = new ArrayList<>();
            seedOptions.add("Select Plant Seed ->");
            viplViewModel.getSeedLiveData().observe(this, new Observer<List<PlantSeed>>() {
                @Override
                public void onChanged(List<PlantSeed> plantSeeds) {
                    for (PlantSeed plantSeed : plantSeeds) {
                        seedOptions.add(plantSeed.getCropCode() + " - " + plantSeed.getCropName());
                    }
                }
            });
            ArrayAdapter ad = new ArrayAdapter(this, R.layout.spinner_layout, R.id.spinnerTv, seedOptions);
            seedSpinner.setAdapter(ad);

        }
        if (preferences.getBoolean("prevoption2", false)) {
            prevLayout.setVisibility(View.VISIBLE);

            ArrayList<String> prevCropsList = new ArrayList<>();
            prevCropsList.add("Select Previous Crop ->");
            viplViewModel.getPrevLiveData().observe(this, new Observer<List<PrevCrop>>() {
                @Override
                public void onChanged(List<PrevCrop> prevCrops) {

                    for (PrevCrop prevCrop : prevCrops) {
                        prevCropsList.add(prevCrop.getCropCode() + " - " + prevCrop.getCropName());
                    }
                }
            });
            ArrayAdapter ad = new ArrayAdapter(this, R.layout.spinner_layout, R.id.spinnerTv, prevCropsList);
            prevSpinner.setAdapter(ad);
        }
        if (preferences.getBoolean("interoption2", false)) {
            inteLayout.setVisibility(View.VISIBLE);

            ArrayList<String> interCropsList = new ArrayList<>();
            interCropsList.add("Select Inter crop ->");
            viplViewModel.getInterCropLiveData().observe(this, new Observer<List<InterCrop>>() {
                @Override
                public void onChanged(List<InterCrop> interCrops) {
                    for (InterCrop prevCrop : interCrops) {
                        interCropsList.add(prevCrop.getCropCode() + " - " + prevCrop.getCropName());
                    }
                }
            });
            ArrayAdapter ad = new ArrayAdapter(this, R.layout.spinner_layout, R.id.spinnerTv, interCropsList);
            interSpinner.setAdapter(ad);
        }
        if (preferences.getBoolean("dev1option2", false)) {
            dev1Layout.setVisibility(View.VISIBLE);

            ArrayList<String> devList = new ArrayList<>();
            devList.add("Select Option ->");
            viplViewModel.getCane1LiveData().observe(this, new Observer<List<CaneDev1>>() {
                @Override
                public void onChanged(List<CaneDev1> caneDev1s) {
                    for (CaneDev1 prevCrop : caneDev1s) {
                        devList.add(prevCrop.getCropCode() + " - " + prevCrop.getCropName());
                    }
                }
            });

            ArrayAdapter ad = new ArrayAdapter(this, R.layout.spinner_layout, R.id.spinnerTv, devList);
            dev1SPinner.setAdapter(ad);
        }
        if (preferences.getBoolean("dev2option2", false)) {
            dev2Layout.setVisibility(View.VISIBLE);

            ArrayList<String> devList = new ArrayList<>();
            devList.add("Select Option ->");
            viplViewModel.getCane2LiveData().observe(this, new Observer<List<CaneDev2>>() {
                @Override
                public void onChanged(List<CaneDev2> caneDev2s) {
                    for (CaneDev2 prevCrop : caneDev2s) {
                        devList.add(prevCrop.getCropCode() + " - " + prevCrop.getCropName());
                    }
                }
            });
            ArrayAdapter ad = new ArrayAdapter(this, R.layout.spinner_layout, R.id.spinnerTv, devList);
            dev2Spinner.setAdapter(ad);
        }
        if (preferences.getBoolean("dev3option2", false)) {
            dev3Layout.setVisibility(View.VISIBLE);

            ArrayList<String> devList = new ArrayList<>();
            devList.add("Select Option ->");
            viplViewModel.getCane3LiveData().observe(this, new Observer<List<CaneDev3>>() {
                @Override
                public void onChanged(List<CaneDev3> caneDev3s) {
                    for (CaneDev3 prevCrop : caneDev3s) {
                        devList.add(prevCrop.getCropCode() + " - " + prevCrop.getCropName());
                    }
                }
            });
            ArrayAdapter ad = new ArrayAdapter(this, R.layout.spinner_layout, R.id.spinnerTv, devList);
            dev3Spinner.setAdapter(ad);
        }
        if (preferences.getBoolean("dev4option2", false)) {
            dev4Layout.setVisibility(View.VISIBLE);

            ArrayList<String> devList = new ArrayList<>();
            devList.add("Select Option ->");
            viplViewModel.getCane4LiveData().observe(this, new Observer<List<CaneDev4>>() {
                @Override
                public void onChanged(List<CaneDev4> caneDev4s) {
                    for (CaneDev4 prevCrop : caneDev4s) {
                        devList.add(prevCrop.getCropCode() + " - " + prevCrop.getCropName());
                    }
                }
            });
            ArrayAdapter ad = new ArrayAdapter(this, R.layout.spinner_layout, R.id.spinnerTv, devList);
            dev4Spinner.setAdapter(ad);
        }
        if (preferences.getBoolean("dev5option2", false)) {
            dev5Layout.setVisibility(View.VISIBLE);

            ArrayList<String> devList = new ArrayList<>();
            devList.add("Select Option ->");
            viplViewModel.getCane5LiveData().observe(this, new Observer<List<CaneDev5>>() {
                @Override
                public void onChanged(List<CaneDev5> caneDev5s) {
                    for (CaneDev5 prevCrop : caneDev5s) {
                        devList.add(prevCrop.getCropCode() + " - " + prevCrop.getCropName());
                    }
                }
            });
            ArrayAdapter ad = new ArrayAdapter(this, R.layout.spinner_layout, R.id.spinnerTv, devList);
            dev5Spinner.setAdapter(ad);
        }
        if (preferences.getBoolean("tressoption2", false)) {
            tressMalchingLayout.setVisibility(View.VISIBLE);

        }
        if (preferences.getBoolean("soilTypeoption2", false)) {
            sTypelayout.setVisibility(View.VISIBLE);

            ArrayList<String> soilNames = new ArrayList<>();
            soilNames.add("Select soil Type ->");
            final ArrayList<String> soilCodes = new ArrayList<>();
            viplViewModel.getSoilLiveData().observe(this, new Observer<List<SoilMaster>>() {
                @Override
                public void onChanged(List<SoilMaster> soilMasters) {
                    for (SoilMaster soilMaster : soilMasters) {
                        soilNames.add(soilMaster.getS_name());
                        soilCodes.add(soilMaster.getS_code());
                    }
                }
            });

            ArrayAdapter ad = new ArrayAdapter(this, R.layout.spinner_layout, R.id.spinnerTv, soilNames);
            sTypeSpinner.setAdapter(ad);
            sTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position > 0) soilCode = soilCodes.get(position - 1);
                    else
                        Toast.makeText(SurveyActivity.this, "Nothing is selected", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }


    }


    private void initAndLoad() throws SQLException {
        caneVarietyActv = findViewById(R.id.caneVariety);
        plotNameSpinner = findViewById(R.id.plotName2);
        cropTypeActv = findViewById(R.id.cropType);
        dateSelectEt = findViewById(R.id.date);
        farmerNameActv = findViewById(R.id.spinnerFarmer2);
        plotEnteredAreaET = findViewById(R.id.createarea);
        remarkEt = findViewById(R.id.remarkEt);
        plotCalculatedAreaET = findViewById(R.id.calculatedarea);
        roadDistancelayout = findViewById(R.id.roadDistancelayout2);

        seedSpinner = findViewById(R.id.seedSpinner2);
        interSpinner = findViewById(R.id.interSpinner2);
        prevSpinner = findViewById(R.id.prevSpinner2);
        dev1SPinner = findViewById(R.id.dev1Spinner2);
        dev2Spinner = findViewById(R.id.dev2Spinner2);
        dev3Spinner = findViewById(R.id.dev3Spinner2);
        dev4Spinner = findViewById(R.id.dev4Spinner2);
        dev5Spinner = findViewById(R.id.dev5Spinner2);
        seedLayout = findViewById(R.id.seedLayout2);
        inteLayout = findViewById(R.id.interLayout2);
        prevLayout = findViewById(R.id.prevLayout2);
        dev1Layout = findViewById(R.id.dev1Layout2);
        dev2Layout = findViewById(R.id.dev2Layout2);
        dev3Layout = findViewById(R.id.dev3Layout2);
        dev4Layout = findViewById(R.id.dev4Layout2);
        dev5Layout = findViewById(R.id.dev5Layout2);

        harvtypeActv = findViewById(R.id.harvtype2);
        farmerFieldElivationActv = findViewById(R.id.farmerFieldElivation2);


        irriLayout = findViewById(R.id.irrigationLayout2);
        wSourcelayout = findViewById(R.id.waterSourceLayout2);
        wtypeLayout = findViewById(R.id.waterTypeLayout2);
        sModeLayout = findViewById(R.id.supplyModelayout2);
        fieldLayout = findViewById(R.id.fieldLayout2);
        elevationlayout = findViewById(R.id.elevationlayout2);
        sTypelayout = findViewById(R.id.soiltypeLayout2);
        rorLayout = findViewById(R.id.rorLayout2);
        harvTypeLayout = findViewById(R.id.harvLayout2);
        sTestedlayout = findViewById(R.id.soilTestesdLayout2);
        wtestedlayout = findViewById(R.id.waterTestesdLayout2);
        tressMalchingLayout = findViewById(R.id.tressMalchingLayout2);

        irriSpinner = findViewById(R.id.irrigationSpinner2);
        wSourceSpinner = findViewById(R.id.waterSourceSpinner2);
        wtypeSpinner = findViewById(R.id.waterTypeSpinner2);
        sModeSpinner = findViewById(R.id.supplyModeSpinner2);
        fieldSpinner = findViewById(R.id.fieldTypeSpinner2);
        sTypeSpinner = findViewById(R.id.soiltypeSpinner2);
        rorSpinner = findViewById(R.id.rorSpinner2);
        sTestedGroup = findViewById(R.id.sTestedGroup2);
        wtestedGroup = findViewById(R.id.wtestedGroup2);
        tressGroup = findViewById(R.id.tressGroupCreate2);
        roadDistanceSpinner = findViewById(R.id.roadDistance2);

        dateSelectEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(SurveyActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String m = "" + (month + 1);
                        if (m.length() < 2) {
                            m = "0" + m;
                        }

                        dateSelectEt.setText(dayOfMonth + "-" + m + "-" + year);
                        datte = year + "-" + m + "-" + dayOfMonth;
                    }
                }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        final PlotDatabase plotDatabase = new PlotDatabase(this);
        ArrayAdapter farmerNamesAdapter = new ArrayAdapter(this, R.layout.spinner_layout, R.id.spinnerTv, plotDatabase.savedFarmers());
        farmerNameActv.setAdapter(farmerNamesAdapter);
        farmerNameActv.setThreshold(1);

        farmerNameActv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String farmer = farmerNameActv.getText().toString();
                String farmerCode = farmer.split(":")[0].trim();
                ArrayAdapter adapter = new ArrayAdapter(SurveyActivity.this, R.layout.spinner_layout, R.id.spinnerTv, plotDatabase.savedPlots(farmerCode));
                plotNameSpinner.setAdapter(adapter);
            }
        });


        ArrayList<String> varietyList = new ArrayList<>();
        varietyIds = new ArrayList<>();

        varietyList.add("Select cane Variety -> ");
        viplViewModel.getVarietyLiveData().observe(this, new Observer<List<VarietyMaster>>() {
            @Override
            public void onChanged(List<VarietyMaster> varietyMasters) {
                for (VarietyMaster master : varietyMasters) {
                    varietyList.add(master.getVarietyName());
                    varietyIds.add(master.getId());
                }
            }
        });

        ArrayAdapter varietyNameAdapter = new ArrayAdapter(this, R.layout.spinner_layout, R.id.spinnerTv, varietyList);
        caneVarietyActv.setAdapter(varietyNameAdapter);


        ArrayList<String> cropList = new ArrayList<>();
        cropIds = new ArrayList<>();
        cropList.add("Select Crop Type -> ");
        viplViewModel.getCropTypeLiveData().observe(this, new Observer<List<CropType>>() {
            @Override
            public void onChanged(List<CropType> cropTypes) {
                for (CropType master : cropTypes) {
                    cropList.add(master.getCropType());
                    cropIds.add(master.getCropId());
                }
                ArrayAdapter cropTypeAdapter = new ArrayAdapter(SurveyActivity.this, R.layout.spinner_layout, R.id.spinnerTv, cropList);
                cropTypeActv.setAdapter(cropTypeAdapter);
            }
        });


    }

    public void setCordinates(View view) {
        String farmerName = farmerNameActv.getText().toString();
        String plotnumber = "";
        if (plotNameSpinner != null && plotNameSpinner.getSelectedItem() != null) {
            plotnumber = plotNameSpinner.getSelectedItem().toString();
        } else {
            Toast.makeText(this, "select plot first", Toast.LENGTH_SHORT).show();
            return;
        }
        String plantDate = dateSelectEt.getText().toString();
        String canevariety = caneVarietyActv.getSelectedItem().toString();
        String cropType = cropTypeActv.getSelectedItem().toString();
        String plotEnteredArea = plotEnteredAreaET.getText().toString();


        if (farmerName.isEmpty()) farmerNameActv.setError("Fill this field");
        else if (plantDate.isEmpty()) dateSelectEt.setError("Fill this field");
        else if (plotEnteredArea.isEmpty()) plotEnteredAreaET.setError("Fill this field");
        else if (plotnumber.startsWith("Select") || canevariety.startsWith("Select cane") || cropType.startsWith("Select Crop")
                || plotEnteredArea.isEmpty()) {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Plot Name Missing")
                    .setMessage("Please fill all the fields first")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create();
            dialog.show();
        } else {

            Intent i = new Intent(this, MapsActivity.class);
            String plotnumbe = plotNameSpinner.getSelectedItem().toString();
            i.putExtra("plot_id", plotnumbe.split("<->")[0].trim());
            startActivityForResult(i, 2);
        }
    }

    public void submitSurvey(View view) {
        String farmerName = farmerNameActv.getText().toString();
        if (farmerName.isEmpty()) {
            farmerNameActv.setError("Fill this field");
            Toast.makeText(this, "Farmer name missing", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!image) {
            Toast.makeText(this, "Capture image First", Toast.LENGTH_SHORT).show();
            return;
        }
        if (preferences.getBoolean("soiloption2", false) && sTestedGroup.getCheckedRadioButtonId() != -1) {
            isSoilTested = ((RadioButton) findViewById(sTestedGroup.getCheckedRadioButtonId())).getText().toString();
        }
        if (preferences.getBoolean("soiloption2", false) && isSoilTested.equals("null")) {
            ((RadioButton) sTestedGroup.getChildAt(1)).setError("Select one option");
            Toast.makeText(this, "select soil", Toast.LENGTH_SHORT).show();
            return;

        }
        if (preferences.getBoolean("waterTestedoption2", false) && wtestedGroup.getCheckedRadioButtonId() != -1) {
            isWaterTested = ((RadioButton) findViewById(wtestedGroup.getCheckedRadioButtonId())).getText().toString();
        }
        if (preferences.getBoolean("waterTestedoption2", false) && isWaterTested.equals("null")) {
            ((RadioButton) wtestedGroup.getChildAt(1)).setError("Select one option");

            return;
        }
        if (preferences.getBoolean("tressoption2", false) && tressGroup.getCheckedRadioButtonId() != -1) {
            tressMalching = ((RadioButton) findViewById(tressGroup.getCheckedRadioButtonId())).getText().toString();

        }
        if (preferences.getBoolean("tressoption2", false) && tressMalching.equals("null")) {
            ((RadioButton) tressGroup.getChildAt(1)).setError("Select one option");
            return;
        }

        if (preferences.getBoolean("irrioption2", false) && irrigationCode.equals("0")) {
            ((TextView) irriLayout.getChildAt(0)).setError("Select One option");
            Toast.makeText(this, "Select Irrigation", Toast.LENGTH_SHORT).show();
            return;
        }
        if (preferences.getBoolean("waterSourceoption2", false) && waterCode.equals("0")) {
            ((TextView) wSourcelayout.getChildAt(0)).setError("Select One option");
            Toast.makeText(this, "Select water source", Toast.LENGTH_SHORT).show();
            return;
        }
        if (preferences.getBoolean("waterTypeoption2", false) && waterType.equals("0")) {
            ((TextView) wtypeLayout.getChildAt(0)).setError("Select One option");
            Toast.makeText(this, "Select water type", Toast.LENGTH_SHORT).show();
            return;
        }
        if (preferences.getBoolean("supplyoption2", false) && supplymode.equals("0")) {
            ((TextView) sModeLayout.getChildAt(0)).setError("Select One option");
            Toast.makeText(this, "Select Mode", Toast.LENGTH_SHORT).show();
            return;
        }
        if (preferences.getBoolean("fieldoption2", false) && fieldType.equals("null")) {
            ((TextView) fieldLayout.getChildAt(0)).setError("Select One option");
            Toast.makeText(this, "Select Field", Toast.LENGTH_SHORT).show();
            return;
        }
        if (preferences.getBoolean("elevationoption2", false) && fieldElevation.equals("null")) {
            ((TextView) elevationlayout.getChildAt(0)).setError("Select One option");
            Toast.makeText(this, "Select Field Elevation", Toast.LENGTH_SHORT).show();
            return;
        }
        if (preferences.getBoolean("soilTypeoption2", false) && soilCode.equals("0")) {
            ((TextView) sTypelayout.getChildAt(0)).setError("Select One option");
            Toast.makeText(this, "Select Soil type", Toast.LENGTH_SHORT).show();
            return;
        }
        if (preferences.getBoolean("rowoption2", false) && rorDistance.equals("0")) {
            ((TextView) rorLayout.getChildAt(0)).setError("Select One option");
            Toast.makeText(this, "Select row distance", Toast.LENGTH_SHORT).show();
            return;
        }
        if (preferences.getBoolean("harvoption2", false) && harvestingType.equals("null")) {
            ((TextView) harvTypeLayout.getChildAt(0)).setError("Select One option");
            Toast.makeText(this, "Select Harvesting Type", Toast.LENGTH_SHORT).show();
            return;
        }
        if (preferences.getBoolean("seedoption2", false)) {
            if (seedSpinner.getSelectedItem().toString().startsWith("Select")) {
                Toast.makeText(this, "Please select seed option", Toast.LENGTH_SHORT).show();
                return;
            }
            seed = seedSpinner.getSelectedItem().toString().split("-")[0].trim();
        }
        if (preferences.getBoolean("prevoption2", false)) {
            if (prevSpinner.getSelectedItem().toString().startsWith("Select")) {
                Toast.makeText(this, "Select previous crop", Toast.LENGTH_SHORT).show();
                return;
            }
            prevCrop = prevSpinner.getSelectedItem().toString().split("-")[0].trim();
        }
        if (preferences.getBoolean("interoption2", false)) {
            if (interSpinner.getSelectedItem().toString().startsWith("Select")) {
                Toast.makeText(this, "Select inter crop", Toast.LENGTH_SHORT).show();
                return;
            }
            interCrop = interSpinner.getSelectedItem().toString().split("-")[0].trim();
        }
        if (preferences.getBoolean("dev1option2", false)) {
            if (dev1SPinner.getSelectedItem().toString().startsWith("Select")) {
                Toast.makeText(this, "Select Cane 1 development", Toast.LENGTH_SHORT).show();
                return;
            }
            dev1Option = dev1SPinner.getSelectedItem().toString().split("-")[0].trim();
        }
        if (preferences.getBoolean("dev2option2", false)) {
            if (dev2Spinner.getSelectedItem().toString().startsWith("Select")) {
                Toast.makeText(this, "Select Cane 2 development", Toast.LENGTH_SHORT).show();
                return;
            }
            dev2Option = dev2Spinner.getSelectedItem().toString().split("-")[0].trim();
        }
        if (preferences.getBoolean("dev3option2", false)) {
            if (dev3Spinner.getSelectedItem().toString().startsWith("Select")) {
                Toast.makeText(this, "Select Cane 3 development", Toast.LENGTH_SHORT).show();
                return;
            }
            dev3Option = dev3Spinner.getSelectedItem().toString().split("-")[0].trim();
        }
        if (preferences.getBoolean("dev4option2", false)) {
            if (dev4Spinner.getSelectedItem().toString().startsWith("Select")) {
                Toast.makeText(this, "Select Cane 4 development", Toast.LENGTH_SHORT).show();
                return;
            }
            dev4Option = dev4Spinner.getSelectedItem().toString().split("-")[0].trim();

        }
        if (preferences.getBoolean("dev5option2", false)) {
            if (dev5Spinner.getSelectedItem().toString().startsWith("Select")) {
                Toast.makeText(this, "Select Cane 5 development", Toast.LENGTH_SHORT).show();
                return;
            }
            dev5Option = dev3Spinner.getSelectedItem().toString().split("-")[0].trim();
        }


        String plotnumber = plotNameSpinner.getSelectedItem().toString();
        String plantDate = dateSelectEt.getText().toString();
        String plotEnteredArea = plotEnteredAreaET.getText().toString();
        String calArea = plotCalculatedAreaET.getText().toString();
        String remark = remarkEt.getText().toString();

        if (plantDate.isEmpty()) dateSelectEt.setError("Fill this field");
        else if (plotEnteredArea.isEmpty()) plotEnteredAreaET.setError("Fill this field");
        else if (plotnumber.startsWith("Select") || caneId.startsWith("Select cane") || cropID.startsWith("Select Crop") || roadDistance.startsWith("Select Distance ->")
                || plotEnteredArea.isEmpty()) {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Detail Missing")
                    .setMessage("Please fill all the fields first")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create();
            dialog.show();
        } else if (Double.parseDouble(calArea) == 0.0) {
            plotCalculatedAreaET.setError("Area cannot be = 0.0");
        } else {
            PlotDatabase pd = new PlotDatabase(this);
            String plot_id = plotnumber.split("<->")[0].trim();
            Log.d("myname", plot_id);


            pd.updatePlot(plot_id, datte, cropID, caneId, calArea, plotEnteredArea, plotCalculatedAreaET.getText().toString(),
                    cropTypeActv.getSelectedItem().toString(), caneVarietyActv.getSelectedItem().toString(), roadDistance, irrigationCode, isSoilTested,
                    isWaterTested, waterCode, waterType, supplymode, soilCode, rorDistance, tressMalching, fieldElevation, fieldType, harvestingType, remark,
                    seed, interCrop, prevCrop, dev1Option, dev2Option, dev3Option, dev4Option, dev5Option);

            Log.d("myname", "roadDistance" + roadDistance + "\nirrigationCode" + irrigationCode + "\nisSoilTested" + isSoilTested + "\nisWaterTested" + isWaterTested
                    + "\nwaterCode" + waterCode + "\nwaterType" + waterType + "\nsupplymode" + supplymode + "\nsoilCode" + soilCode + "\nrorDistance" + rorDistance + "\ntressMalching" + tressMalching);
            finish();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (data != null) {
                double area_in_meter = data.getDoubleExtra("MESSAGE", 0.0);
                String in_acre=new DecimalFormat("##.###").format(area_in_meter * 0.000247105);

                plotCalculatedAreaET.setText(in_acre);
                MainConstant constant=new MainConstant(SurveyActivity.this);
                if (constant.getSurveyArea()==1){
                    String in_hector=new DecimalFormat("##.###").format(area_in_meter / 10000);
                    plotCalculatedAreaET.setText(in_hector);
                }
            } else {

                plotCalculatedAreaET.setText("" + 0.0);
            }
        }

        if (requestCode == 100 && resultCode == RESULT_OK) {
            image = true;

            Glide.with(this).load(android.R.drawable.stat_sys_download_done).into((ImageView) findViewById(R.id.surveyImage));
        } else {
            image = false;
            Glide.with(this).load(android.R.drawable.ic_menu_camera).into((ImageView) findViewById(R.id.surveyImage));
        }

    }

    @Override
    public void onBackPressed() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Alert")
                .setMessage("Survey is not submitted  yet....")
                .setPositiveButton("Submit now", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Submit later", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SurveyActivity.super.onBackPressed();
                    }
                })
                .create();
        dialog.show();
    }

    public void captureImage(View view) {
        String plotnumber = "";
        if (plotNameSpinner != null && plotNameSpinner.getSelectedItem() != null) {
            plotnumber = plotNameSpinner.getSelectedItem().toString();
        } else {
            Toast.makeText(this, "select plot first", Toast.LENGTH_SHORT).show();
            return;
        }
        openCameraIntent(plotnumber);

    }

    private void openCameraIntent(String plotNumber) {
        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getPackageManager()) != null) {
            //Create a file to store the image
            File photoFile = null;
            try {
                photoFile = createImageFile(plotNumber);
            } catch (IOException ex) {
                Log.d("myname", ex.getMessage());
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "in.visibleinfotech.viplfieldapplications.provider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(pictureIntent, 100);
            }
        }
    }

    private File createImageFile(String plotNumber) throws IOException {

        String imageFileName = "Sur_" + plotNumber + "_";
        File storageDir =
                getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        imageFilePath = image.getAbsolutePath();
        return image;
    }
}
