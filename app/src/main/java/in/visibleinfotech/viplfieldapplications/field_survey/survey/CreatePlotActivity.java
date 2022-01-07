package in.visibleinfotech.viplfieldapplications.field_survey.survey;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import in.visibleinfotech.viplfieldapplications.MainConstant;
import in.visibleinfotech.viplfieldapplications.R;
import in.visibleinfotech.viplfieldapplications.field_survey.localdatabase.PlotDatabase;
import in.visibleinfotech.viplfieldapplications.field_survey.localdatabase.ViplViewModel;
import in.visibleinfotech.viplfieldapplications.field_survey.models.AccountMaster;
import in.visibleinfotech.viplfieldapplications.field_survey.models.CaneDev1;
import in.visibleinfotech.viplfieldapplications.field_survey.models.CaneDev2;
import in.visibleinfotech.viplfieldapplications.field_survey.models.CaneDev3;
import in.visibleinfotech.viplfieldapplications.field_survey.models.CaneDev4;
import in.visibleinfotech.viplfieldapplications.field_survey.models.CaneDev5;
import in.visibleinfotech.viplfieldapplications.field_survey.models.CaneRowDistanceMaster;
import in.visibleinfotech.viplfieldapplications.field_survey.models.FieldElivationTypeMaster;
import in.visibleinfotech.viplfieldapplications.field_survey.models.FieldQualityMaster;
import in.visibleinfotech.viplfieldapplications.field_survey.models.HarvMaster;
import in.visibleinfotech.viplfieldapplications.field_survey.models.InterCrop;
import in.visibleinfotech.viplfieldapplications.field_survey.models.IrrigationMaster;
import in.visibleinfotech.viplfieldapplications.field_survey.models.PlaceMaster;
import in.visibleinfotech.viplfieldapplications.field_survey.models.PlantSeed;
import in.visibleinfotech.viplfieldapplications.field_survey.models.Plot;
import in.visibleinfotech.viplfieldapplications.field_survey.models.PrevCrop;
import in.visibleinfotech.viplfieldapplications.field_survey.models.RouteMaster;
import in.visibleinfotech.viplfieldapplications.field_survey.models.SoilMaster;
import in.visibleinfotech.viplfieldapplications.field_survey.models.SupplyMaster;
import in.visibleinfotech.viplfieldapplications.field_survey.models.WaterSourceMaster;
import in.visibleinfotech.viplfieldapplications.field_survey.models.WaterType;

public class CreatePlotActivity extends AppCompatActivity {

    AppCompatAutoCompleteTextView farmerNameActv, villageNameSpinner,farmerVillageActv;
    TextView farmercodeActv;
    EditText plotNameET, khasraNumEt;
    ArrayList<String> placeNameList = new ArrayList<>();
    SharedPreferences preferences;
    private LinearLayout irriLayout, wSourcelayout, wtypeLayout, sModeLayout, fieldLayout, elevationlayout, nearestRoadLayout, roadDistanceLayout, sTypelayout, rorLayout, harvTypeLayout, sTestedlayout, wtestedlayout, tressMalchingLayout,
            seedLayout, inteLayout, prevLayout, dev1Layout, dev2Layout, dev3Layout, dev4Layout, dev5Layout;
    private Spinner harvtypeActv, farmerFieldElivationActv, nearestRoadSpinner, roadDistanceSpinner, irriSpinner, wSourceSpinner, wtypeSpinner, sModeSpinner, fieldSpinner, sTypeSpinner, rorSpinner,
            seedSpinner, interSpinner, prevSpinner, dev1SPinner, dev2Spinner, dev3Spinner, dev4Spinner, dev5Spinner;
    private RadioGroup sTestedGroup, wtestedGroup, tressGroup;
    private String kashraNum = "null", place_id = "null", farmerCode = "null", farmerVillage = "null", plotLand = "null", plotVillage = "null", plotNearestRoad = "null", roadDitance = "null", irrigationCode = "0", isSoilTested = "null",
            isWaterTested = "null", waterCode = "0", waterType = "0", supplymode = "0", fieldType = "null", fieldElevation = "null", soilCode = "0", rorDistance = "0",
            harvestingType = "null", tressMalching = "null", seed = "-1", prevCrop = "-1", interCrop = "-1", dev1Option = "-1", dev2Option = "-1", dev3Option = "-1", dev4Option = "-1", dev5Option = "-1";

    MainConstant constant;

    ViplViewModel viplViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.survey_activity_create_plot);
        preferences = getSharedPreferences("Choice", 0);
        viplViewModel = ViewModelProviders.of(this).get(ViplViewModel.class);
        constant = new MainConstant(this);
        try {
            initializeAndLoad();
            loadPreviousPrefrence();

        } catch (Exception e) {
            Toast.makeText(this, "import data first", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadPreviousPrefrence() {
        if (preferences.getBoolean("irrioption1", false)) {
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
                        Toast.makeText(CreatePlotActivity.this, "Nothing is selected", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


        }
        if (preferences.getBoolean("supplyoption1", false)) {
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
                        Toast.makeText(CreatePlotActivity.this, "nothing is selected", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


        }
        if (preferences.getBoolean("soiloption1", false)) {
            sTestedlayout.setVisibility(View.VISIBLE);


        }
        if (preferences.getBoolean("waterSourceoption1", false)) {
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
                        Toast.makeText(CreatePlotActivity.this, "nothing is selected", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        if (preferences.getBoolean("seedoption1", false)) {
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
        if (preferences.getBoolean("prevoption1", false)) {
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
        if (preferences.getBoolean("interoption1", false)) {
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
        if (preferences.getBoolean("dev1option1", false)) {
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
        if (preferences.getBoolean("dev2option1", false)) {
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
        if (preferences.getBoolean("dev3option1", false)) {
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
        if (preferences.getBoolean("dev4option1", false)) {
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
        if (preferences.getBoolean("dev5soption1", false)) {
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


        if (preferences.getBoolean("waterTypeoption1", false)) {
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
                        Toast.makeText(CreatePlotActivity.this, "Nothing is selected", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }
        if (preferences.getBoolean("waterTestedoption1", false)) {
            wtestedlayout.setVisibility(View.VISIBLE);

        }
        if (preferences.getBoolean("fieldoption1", false)) {
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


        if (preferences.getBoolean("elevationoption1", false)) {
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
        if (preferences.getBoolean("harvoption1", false)) {
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
        if (preferences.getBoolean("rowoption1", false)) {
            rorLayout.setVisibility(View.VISIBLE);
            final ArrayList<String> directions = new ArrayList<>();
            final ArrayList<String> codes = new ArrayList<>();
            directions.add("Select Row to Row distance ->");

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
                        Toast.makeText(CreatePlotActivity.this, "nothing is selected", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }
        if (preferences.getBoolean("tressoption1", false)) {
            tressMalchingLayout.setVisibility(View.VISIBLE);

        }
        if (preferences.getBoolean("soilTypeoption1", false)) {
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
                        Toast.makeText(CreatePlotActivity.this, "Nothing is selected", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }


    }

    public void saveDetails(View view) {
        String farmer = farmerNameActv.getText().toString();
        String farmerName = farmer.split(":")[1].trim();
        farmerCode = farmer.split(":")[0].trim();
        plotLand = plotNameET.getText().toString();
        kashraNum = khasraNumEt.getText().toString();
        String plotAddress = villageNameSpinner.getText().toString();
        try {
            plotVillage = plotAddress.split("-")[1].trim();
        }
        catch (Exception e){
            Toast.makeText(this, "plzz select village from dropdown only", Toast.LENGTH_SHORT).show();
            return;
        }

        if (farmerName.isEmpty()) {
            farmerNameActv.setError("Farmer name missing");
            return;
        }
        if (farmerCode.equals("null")) {
            farmerNameActv.setError("Farmer not found");
            return;
        }
        if (place_id.equals("null")) {
            farmerNameActv.setError("Village not found");
            return;
        }
        if (plotLand.isEmpty()) {
            plotNameET.setError("Input missing");
            return;
        }
        if (plotVillage.isEmpty()) {
            villageNameSpinner.setError("Input missing");
            return;
        }

        if (preferences.getBoolean("soiloption1", false) && sTestedGroup.getCheckedRadioButtonId() != -1) {
            isSoilTested = ((RadioButton) findViewById(sTestedGroup.getCheckedRadioButtonId())).getText().toString();
        }
        if (preferences.getBoolean("soiloption1", false) && isSoilTested.equals("null")) {
            ((RadioButton) sTestedGroup.getChildAt(1)).setError("Select one option");
            return;

        }
        if (preferences.getBoolean("waterTestedoption1", false) && wtestedGroup.getCheckedRadioButtonId() != -1) {
            isWaterTested = ((RadioButton) findViewById(wtestedGroup.getCheckedRadioButtonId())).getText().toString();
        }
        if (preferences.getBoolean("waterTestedoption1", false) && isWaterTested.equals("null")) {
            ((RadioButton) wtestedGroup.getChildAt(1)).setError("Select one option");
            return;
        }
        if (preferences.getBoolean("tressoption1", false) && tressGroup.getCheckedRadioButtonId() != -1) {
            tressMalching = ((RadioButton) findViewById(tressGroup.getCheckedRadioButtonId())).getText().toString();

        }

        if (preferences.getBoolean("seedoption1", false)) {
            if (seedSpinner.getSelectedItem().toString().startsWith("Select")){
                Toast.makeText(this, "Please select seed option", Toast.LENGTH_SHORT).show();
                return;
            }
            seed = seedSpinner.getSelectedItem().toString().split("-")[0];
        }
        if (preferences.getBoolean("prevoption1", false)) {
            if(prevSpinner.getSelectedItem().toString().startsWith("Select")){
                Toast.makeText(this, "Select previous crop", Toast.LENGTH_SHORT).show();
                return;
            }
            prevCrop = prevSpinner.getSelectedItem().toString().split("-")[0];
        }
        if (preferences.getBoolean("interoption1", false)) {
            if(interSpinner.getSelectedItem().toString().startsWith("Select")){
                Toast.makeText(this, "Select inter crop", Toast.LENGTH_SHORT).show();
                return;
            }
            interCrop = interSpinner.getSelectedItem().toString().split("-")[0];
        }
        if (preferences.getBoolean("dev1option1", false)) {
            if(dev1SPinner.getSelectedItem().toString().startsWith("Select")){
                Toast.makeText(this, "Select Cane 1 development", Toast.LENGTH_SHORT).show();
                return;
            }
            dev1Option = dev1SPinner.getSelectedItem().toString().split("-")[0];
        }
        if (preferences.getBoolean("dev2option1", false)) {
            if(dev2Spinner.getSelectedItem().toString().startsWith("Select")){
                Toast.makeText(this, "Select Cane 2 development", Toast.LENGTH_SHORT).show();
                return;
            }
            dev2Option = dev2Spinner.getSelectedItem().toString().split("-")[0];
        }
        if (preferences.getBoolean("dev3option1", false)) {
            if(dev3Spinner.getSelectedItem().toString().startsWith("Select")){
                Toast.makeText(this, "Select Cane 3 development", Toast.LENGTH_SHORT).show();
                return;
            }
            dev3Option = dev3Spinner.getSelectedItem().toString().split("-")[0];
        }
        if (preferences.getBoolean("dev4option1", false)) {
            if(dev4Spinner.getSelectedItem().toString().startsWith("Select")){
                Toast.makeText(this, "Select Cane 4 development", Toast.LENGTH_SHORT).show();
                return;
            }
            dev4Option = dev4Spinner.getSelectedItem().toString().split("-")[0];

        }
        if (preferences.getBoolean("dev5soption1", false)) {
            if(dev5Spinner.getSelectedItem().toString().startsWith("Select")){
                Toast.makeText(this, "Select Cane 5 development", Toast.LENGTH_SHORT).show();
                return;
            }
            dev5Option = dev3Spinner.getSelectedItem().toString().split("-")[0];
        }


        if (preferences.getBoolean("irrioption1", false) && irrigationCode.equals("0")) {
            ((TextView) irriLayout.getChildAt(0)).setError("Select One option");
            return;
        }
        if (preferences.getBoolean("waterSourceoption1", false) && waterCode.equals("0")) {
            ((TextView) wSourcelayout.getChildAt(0)).setError("Select One option");
            return;
        }
        if (preferences.getBoolean("waterTypeoption1", false) && waterType.equals("0")) {
            ((TextView) wtypeLayout.getChildAt(0)).setError("Select One option");
            return;
        }
        if (preferences.getBoolean("supplyoption1", false) && supplymode.equals("0")) {
            ((TextView) sModeLayout.getChildAt(0)).setError("Select One option");
            return;
        }
        if (preferences.getBoolean("fieldoption1", false) && fieldType.equals("null")) {
            ((TextView) fieldLayout.getChildAt(0)).setError("Select One option");
            return;
        }
        if (preferences.getBoolean("elevationoption1", false) && fieldElevation.equals("null")) {
            ((TextView) elevationlayout.getChildAt(0)).setError("Select One option");
            return;
        }
        if (preferences.getBoolean("soilTypeoption1", false) && soilCode.equals("0")) {
            ((TextView) sTypelayout.getChildAt(0)).setError("Select One option");
            return;
        }
        if (preferences.getBoolean("rowoption1", false) && rorDistance.equals("0")) {
            ((TextView) rorLayout.getChildAt(0)).setError("Select One option");
            return;
        }
        if (preferences.getBoolean("harvoption1", false) && harvestingType.equals("null")) {
            ((TextView) harvTypeLayout.getChildAt(0)).setError("Select One option");
            return;
        }

        SharedPreferences sharedPreferences = getSharedPreferences("userrecord", 0);
        String zoneCode = sharedPreferences.getString("zonecode", "null");

        String siteCode = zoneCode.split("-")[0];

        PlotDatabase plotDatabase = new PlotDatabase(this);
        String plotNumber = farmerCode + "-" + (plotDatabase.getNumOfPlot(farmerCode) + 1) + "-" + constant.geteCode();


        String plot_vill_code = villageNameSpinner.getText().toString().split("-")[0].trim();
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);

        if (plotDatabase.checkExists(farmerCode, plot_vill_code, plotLand) > 0) {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Alert")
                    .setMessage("This field is already created.")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create();
            dialog.show();
        } else {
            Plot plot = new Plot(siteCode, plotNumber, farmerCode, place_id, "null", "null", "null", "null", plotLand, "1", fieldType,
                    fieldElevation, formattedDate, "0", "null", plotNearestRoad, "null", "A", harvestingType, plot_vill_code, farmerName, farmerVillage, "null", "null",
                    "0", roadDitance, irrigationCode, isSoilTested, isWaterTested, waterCode, waterType, supplymode, soilCode, rorDistance, tressMalching, kashraNum, null,
                    seed, interCrop, prevCrop, dev1Option, dev2Option, dev3Option, dev4Option, dev5Option);
            Log.d("myname", "roadDistance" + roadDitance + "\nirrigationCode" + irrigationCode + "\nisSoilTested" + isSoilTested + "\nisWaterTested" + isWaterTested
                    + "\nwaterCode" + waterCode + "\nwaterType" + waterType + "\nsupplymode" + supplymode + "\nsoilCode" + soilCode + "\nrorDistance" + rorDistance + "\ntressMalching" + tressMalching);
            long n = plotDatabase.addPlot(plot);
            Toast.makeText(this, "Total plot " + n, Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    private void initializeAndLoad() {
        farmercodeActv = findViewById(R.id.farmercode);
        khasraNumEt = findViewById(R.id.khasraNumberEt);
        plotNameET = findViewById(R.id.plotName);
        farmerNameActv = findViewById(R.id.spinnerFarmer);
        villageNameSpinner = findViewById(R.id.spinnerVillage);
        farmerVillageActv = findViewById(R.id.farmerVillageACTV);

        harvtypeActv = findViewById(R.id.harvtype);
        farmerFieldElivationActv = findViewById(R.id.farmerFieldElivation);
        nearestRoadSpinner = findViewById(R.id.rowType);
        roadDistanceSpinner = findViewById(R.id.roadDistanceSpinenr);
        seedSpinner = findViewById(R.id.seedSpinner1);
        interSpinner = findViewById(R.id.interSpinner1);
        prevSpinner = findViewById(R.id.prevSpinner1);
        dev1SPinner = findViewById(R.id.dev1Spinner1);
        dev2Spinner = findViewById(R.id.dev2Spinner1);
        dev3Spinner = findViewById(R.id.dev3Spinner1);
        dev4Spinner = findViewById(R.id.dev4Spinner1);
        dev5Spinner = findViewById(R.id.dev5Spinner1);

        irriLayout = findViewById(R.id.irrigationLayout);
        wSourcelayout = findViewById(R.id.waterSourceLayout);
        wtypeLayout = findViewById(R.id.waterTypeLayout);
        sModeLayout = findViewById(R.id.supplyModelayout);
        fieldLayout = findViewById(R.id.fieldLayout);
        elevationlayout = findViewById(R.id.elevationlayout);
        sTypelayout = findViewById(R.id.soiltypeLayout);
        rorLayout = findViewById(R.id.rorLayout);
        harvTypeLayout = findViewById(R.id.harvLayout);
        sTestedlayout = findViewById(R.id.soilTestesdLayout);
        wtestedlayout = findViewById(R.id.waterTestesdLayout);
        tressMalchingLayout = findViewById(R.id.tressMalchingLayout);
        nearestRoadLayout = findViewById(R.id.nearestRoadLayout);
        roadDistanceLayout = findViewById(R.id.roadDistancelayout);
        seedLayout = findViewById(R.id.seedLayout1);
        inteLayout = findViewById(R.id.interLayout1);
        prevLayout = findViewById(R.id.prevLayout1);
        dev1Layout = findViewById(R.id.dev1Layout1);
        dev2Layout = findViewById(R.id.dev2Layout1);
        dev3Layout = findViewById(R.id.dev3Layout1);
        dev4Layout = findViewById(R.id.dev4Layout1);
        dev5Layout = findViewById(R.id.dev5Layout1);

        irriSpinner = findViewById(R.id.irrigationSpinner);
        wSourceSpinner = findViewById(R.id.waterSourceSpinner);
        wtypeSpinner = findViewById(R.id.waterTypeSpinner);
        sModeSpinner = findViewById(R.id.supplyModeSpinner);
        fieldSpinner = findViewById(R.id.fieldTypeSpinner);
        sTypeSpinner = findViewById(R.id.soiltypeSpinner);
        rorSpinner = findViewById(R.id.rorSpinner);
        sTestedGroup = findViewById(R.id.sTestedGroup);
        wtestedGroup = findViewById(R.id.wtestedGroup);
        tressGroup = findViewById(R.id.tressGroupCreate);

        final ArrayList<String> farNameList = new ArrayList<>();
        final ArrayList<String> place_ids = new ArrayList<>();




        placeNameList = new ArrayList<>();
        viplViewModel.getPlaceLiveData().observe(this, new Observer<List<PlaceMaster>>() {
            @Override
            public void onChanged(List<PlaceMaster> placeMasters) {
                for (PlaceMaster master : placeMasters) {
                    placeNameList.add(master.getPlaceId() + " - " + master.getPlaceName());
                }
            }
        });

        ArrayAdapter placeNameArrayAdapter = new ArrayAdapter(this, R.layout.spinner_layout, R.id.spinnerTv, placeNameList);
        villageNameSpinner.setAdapter(placeNameArrayAdapter);
        farmerVillageActv.setAdapter(placeNameArrayAdapter);
        villageNameSpinner.setThreshold(1);
        farmerVillageActv.setThreshold(1);
        final ArrayList<String> arrayList = new ArrayList<>();
        villageNameSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String p_id = villageNameSpinner.getText().toString().split("-")[0].trim();

                viplViewModel.getAccountLiveData(p_id).observe(CreatePlotActivity.this, new Observer<List<AccountMaster>>() {
                    @Override
                    public void onChanged(List<AccountMaster> accountMasters) {
                        for (AccountMaster master : accountMasters) {
                            farNameList.add(master.getAccountId() + " : " + master.getAccountName());
                            place_ids.add(master.getPlaceId());
                            ArrayAdapter farmerNameAdapter = new ArrayAdapter(CreatePlotActivity.this, R.layout.spinner_layout, R.id.spinnerTv, farNameList);
                            farmerNameActv.setThreshold(1);
                            farmerNameActv.setAdapter(farmerNameAdapter);
                        }

                    }
                });


                arrayList.add("Select road -> ");
                viplViewModel.getRoutesLiveData(p_id).observe(CreatePlotActivity.this, new Observer<List<RouteMaster>>() {
                    @Override
                    public void onChanged(List<RouteMaster> routeMasters) {
                        for (RouteMaster routeMaster : routeMasters) {
                            String route = routeMaster.getR_code() + " <-> " + routeMaster.getR_name();
                            arrayList.add(route);
                        }
                    }
                });

                ArrayAdapter rowAdapter = new ArrayAdapter(CreatePlotActivity.this, R.layout.spinner_layout, R.id.spinnerTv, arrayList);
                nearestRoadSpinner.setAdapter(rowAdapter);

            }
        });
        farmerVillageActv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String p_id = farmerVillageActv.getText().toString().split("-")[0].trim();

                viplViewModel.getAccountLiveData(p_id).observe(CreatePlotActivity.this, new Observer<List<AccountMaster>>() {
                    @Override
                    public void onChanged(List<AccountMaster> accountMasters) {
                        for (AccountMaster master : accountMasters) {
                            farNameList.add(master.getAccountId() + " : " + master.getAccountName());
                            place_ids.add(master.getPlaceId());
                            ArrayAdapter farmerNameAdapter = new ArrayAdapter(CreatePlotActivity.this, R.layout.spinner_layout, R.id.spinnerTv, farNameList);
                            farmerNameActv.setThreshold(1);
                            farmerNameActv.setAdapter(farmerNameAdapter);
                        }
                    }
                });

                farmerNameActv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String nnn = farmerNameActv.getText().toString();
                        int index = farNameList.indexOf(nnn);
                        place_id = place_ids.get(index);
                        viplViewModel.getPlaceLiveData().observe(CreatePlotActivity.this, new Observer<List<PlaceMaster>>() {
                            @Override
                            public void onChanged(List<PlaceMaster> placeMasters) {

                                for (PlaceMaster placeMaster : placeMasters) {
                                    if (placeMaster.getPlaceId().equals(place_id)) {
                                        farmerVillage = placeMaster.getPlaceName();
                                        farmercodeActv.setText("Village = " + farmerVillage);
                                        break;
                                    }
                                }
                            }
                        });
                    }
                });
            }
        });
        if (preferences.getBoolean("nearestRoadoption", false)) {
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
        }
        if (preferences.getBoolean("roadDistanceoption1", false)) {
            roadDistanceLayout.setVisibility(View.VISIBLE);
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
                    roadDitance = roadDistances.get(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }



      /*  RowToRowFieldMapDirctionDb rowToRowFieldMapDirctionDb = new RowToRowFieldMapDirctionDb(this);
        ArrayList<RowToRowFieldMapDirection> rowToRowFieldMapDirections = rowToRowFieldMapDirctionDb.getRowToRowFieldMapDirction();
        ArrayList<String> rowTypeList = new ArrayList<>();
        rowTypeList.add("Select Road  -> ");
        rowTypeList.add("Main Road");
        rowTypeList.add("Sub Road");
        rowTypeList.add("No Road");*/
//        for (RowToRowFieldMapDirection master : rowToRowFieldMapDirections) {
//            rowTypeList.add(master.getROW_Name());
//        }

    }


}
