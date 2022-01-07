package in.visibleinfotech.viplfieldapplications.field_survey;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import in.visibleinfotech.viplfieldapplications.R;


public class AdvanceSetting extends AppCompatActivity {
    LinearLayout layout;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Switch irrigationSwitch, soilSwitch, waterTestedSwitch, waterSourceSwitch, waterTypeSwitch, supplySwitch,
            fieldSwitch, elevationSwitch, soilTypeSwitch, rorSwitch, harvSwitch, tressSwitch, nearestRoadSwitch, roadDistanceSwitch,
            seedSwitch, interSwitch, prevSwitch, dev1Switch, dev2Switch, dev3Switch, dev4Switch, dev5Switch;
    RadioGroup irrigationGroup, soilGroup, waterTestedGroup, waterSourceGroup, waterTypeGroup, supplyGroup, fieldGroup,
            elevationGroup, soilTypeGroup, rorGroup, harvGroup, tressGroup, roadDistanceGroup,
            seedGroup, interGroup, prevGroup, dev1Group, dev2Group, dev3Group, dev4Group, dev5Group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.survey_activity_advance_setting);
        layout = findViewById(R.id.ll1);
        init();
        loadPreviousPrefrence();

        irrigationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                for (int i = 0; i < irrigationGroup.getChildCount(); i++) {
                    irrigationGroup.getChildAt(i).setEnabled(isChecked);
                }
            }
        });
        soilSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                for (int i = 0; i < soilGroup.getChildCount(); i++) {
                    soilGroup.getChildAt(i).setEnabled(isChecked);
                }
            }
        });
        waterSourceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                for (int i = 0; i < waterSourceGroup.getChildCount(); i++) {
                    waterSourceGroup.getChildAt(i).setEnabled(isChecked);
                }
            }
        });
        waterTestedSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                for (int i = 0; i < waterTestedGroup.getChildCount(); i++) {
                    waterTestedGroup.getChildAt(i).setEnabled(isChecked);
                }
            }
        });
        waterTypeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                for (int i = 0; i < waterTypeGroup.getChildCount(); i++) {
                    waterTypeGroup.getChildAt(i).setEnabled(isChecked);
                }
            }
        });
        harvSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                for (int i = 0; i < harvGroup.getChildCount(); i++) {
                    harvGroup.getChildAt(i).setEnabled(isChecked);
                }
            }
        });
        rorSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                for (int i = 0; i < rorGroup.getChildCount(); i++) {
                    rorGroup.getChildAt(i).setEnabled(isChecked);
                }
            }
        });
        tressSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                for (int i = 0; i < tressGroup.getChildCount(); i++) {
                    tressGroup.getChildAt(i).setEnabled(isChecked);
                }
            }
        });
        supplySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                for (int i = 0; i < supplyGroup.getChildCount(); i++) {
                    supplyGroup.getChildAt(i).setEnabled(isChecked);
                }
            }
        });
        soilTypeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                for (int i = 0; i < soilTypeGroup.getChildCount(); i++) {
                    soilTypeGroup.getChildAt(i).setEnabled(isChecked);
                }
            }
        });
        fieldSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                for (int i = 0; i < fieldGroup.getChildCount(); i++) {
                    fieldGroup.getChildAt(i).setEnabled(isChecked);
                }
            }
        });
        elevationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                for (int i = 0; i < elevationGroup.getChildCount(); i++) {
                    elevationGroup.getChildAt(i).setEnabled(isChecked);
                }
            }
        });

        roadDistanceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                for (int i = 0; i < roadDistanceGroup.getChildCount(); i++) {
                    roadDistanceGroup.getChildAt(i).setEnabled(isChecked);
                }
            }
        });
        seedSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                for (int i = 0; i < seedGroup.getChildCount(); i++) {
                    seedGroup.getChildAt(i).setEnabled(isChecked);
                }
            }
        });
        interSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                for (int i = 0; i < interGroup.getChildCount(); i++) {
                    interGroup.getChildAt(i).setEnabled(isChecked);
                }
            }
        });
        prevSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                for (int i = 0; i < prevGroup.getChildCount(); i++) {
                    prevGroup.getChildAt(i).setEnabled(isChecked);
                }
            }
        });
        dev1Switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                for (int i = 0; i < dev1Group.getChildCount(); i++) {
                    dev1Group.getChildAt(i).setEnabled(isChecked);
                }
            }
        });
        dev2Switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                for (int i = 0; i < dev2Group.getChildCount(); i++) {
                    dev2Group.getChildAt(i).setEnabled(isChecked);
                }
            }
        });
        dev3Switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                for (int i = 0; i < dev3Group.getChildCount(); i++) {
                    dev3Group.getChildAt(i).setEnabled(isChecked);
                }
            }
        });
        dev4Switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                for (int i = 0; i < dev4Group.getChildCount(); i++) {
                    dev4Group.getChildAt(i).setEnabled(isChecked);
                }
            }
        });
        dev5Switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                for (int i = 0; i < dev5Group.getChildCount(); i++) {
                    dev5Group.getChildAt(i).setEnabled(isChecked);
                }
            }
        });

    }

    private void loadPreviousPrefrence() {
        if (preferences.getBoolean("irrioption1", false) || preferences.getBoolean("irrioption2", false)) {
            irrigationSwitch.setChecked(true);
            ((RadioButton) findViewById(R.id.irrioption1)).setEnabled(true);
            ((RadioButton) findViewById(R.id.irrioption1)).setChecked(preferences.getBoolean("irrioption1", false));
            ((RadioButton) findViewById(R.id.irrioption2)).setEnabled(true);
            ((RadioButton) findViewById(R.id.irrioption2)).setChecked(preferences.getBoolean("irrioption2", false));
        }
        if (preferences.getBoolean("supplyoption1", false) || preferences.getBoolean("supplyoption2", false)) {
            supplySwitch.setChecked(true);
            ((RadioButton) findViewById(R.id.supplyoption1)).setEnabled(true);
            ((RadioButton) findViewById(R.id.supplyoption1)).setChecked(preferences.getBoolean("supplyoption1", false));
            ((RadioButton) findViewById(R.id.supplyoption2)).setEnabled(true);
            ((RadioButton) findViewById(R.id.supplyoption2)).setChecked(preferences.getBoolean("supplyoption2", false));
        }
        if (preferences.getBoolean("soiloption1", false) || preferences.getBoolean("soiloption2", false)) {
            soilSwitch.setChecked(true);
            ((RadioButton) findViewById(R.id.soiloption1)).setEnabled(true);
            ((RadioButton) findViewById(R.id.soiloption1)).setChecked(preferences.getBoolean("soiloption1", false));
            ((RadioButton) findViewById(R.id.soiloption2)).setEnabled(true);
            ((RadioButton) findViewById(R.id.soiloption2)).setChecked(preferences.getBoolean("soiloption2", false));
        }
        if (preferences.getBoolean("waterSourceoption1", false) || preferences.getBoolean("waterSourceoption2", false)) {
            waterSourceSwitch.setChecked(true);
            ((RadioButton) findViewById(R.id.waterSourceoption1)).setEnabled(true);
            ((RadioButton) findViewById(R.id.waterSourceoption1)).setChecked(preferences.getBoolean("waterSourceoption1", false));
            ((RadioButton) findViewById(R.id.waterSourceoption2)).setEnabled(true);
            ((RadioButton) findViewById(R.id.waterSourceoption2)).setChecked(preferences.getBoolean("waterSourceoption2", false));
        }
        if (preferences.getBoolean("waterTypeoption1", false) || preferences.getBoolean("waterTypeoption2", false)) {
            waterTypeSwitch.setChecked(true);
            ((RadioButton) findViewById(R.id.waterTypeoption1)).setEnabled(true);
            ((RadioButton) findViewById(R.id.waterTypeoption1)).setChecked(preferences.getBoolean("waterTypeoption1", false));
            ((RadioButton) findViewById(R.id.waterTypeoption2)).setEnabled(true);
            ((RadioButton) findViewById(R.id.waterTypeoption2)).setChecked(preferences.getBoolean("waterTypeoption2", false));
        }
        if (preferences.getBoolean("waterTestedoption1", false) || preferences.getBoolean("waterTestedoption2", false)) {
            waterTestedSwitch.setChecked(true);
            ((RadioButton) findViewById(R.id.waterTestedoption1)).setEnabled(true);
            ((RadioButton) findViewById(R.id.waterTestedoption1)).setChecked(preferences.getBoolean("waterTestedoption1", false));
            ((RadioButton) findViewById(R.id.waterTestedoption2)).setEnabled(true);
            ((RadioButton) findViewById(R.id.waterTestedoption2)).setChecked(preferences.getBoolean("waterTestedoption2", false));
        }
        if (preferences.getBoolean("fieldoption1", false) || preferences.getBoolean("fieldoption2", false)) {
            fieldSwitch.setChecked(true);
            ((RadioButton) findViewById(R.id.fieldoption1)).setEnabled(true);
            ((RadioButton) findViewById(R.id.fieldoption1)).setChecked(preferences.getBoolean("fieldoption1", false));
            ((RadioButton) findViewById(R.id.fieldoption2)).setEnabled(true);
            ((RadioButton) findViewById(R.id.fieldoption2)).setChecked(preferences.getBoolean("fieldoption2", false));
        }
        if (preferences.getBoolean("elevationoption1", false) || preferences.getBoolean("elevationoption2", false)) {
            elevationSwitch.setChecked(true);
            ((RadioButton) findViewById(R.id.elevationoption1)).setEnabled(true);
            ((RadioButton) findViewById(R.id.elevationoption1)).setChecked(preferences.getBoolean("elevationoption1", false));
            ((RadioButton) findViewById(R.id.elevationoption2)).setEnabled(true);
            ((RadioButton) findViewById(R.id.elevationoption2)).setChecked(preferences.getBoolean("elevationoption2", false));
        }
        if (preferences.getBoolean("harvoption1", false) || preferences.getBoolean("harvoption2", false)) {
            harvSwitch.setChecked(true);
            ((RadioButton) findViewById(R.id.harvoption1)).setEnabled(true);
            ((RadioButton) findViewById(R.id.harvoption1)).setChecked(preferences.getBoolean("harvoption1", false));
            ((RadioButton) findViewById(R.id.harvoption2)).setEnabled(true);
            ((RadioButton) findViewById(R.id.harvoption2)).setChecked(preferences.getBoolean("harvoption2", false));
        }
        if (preferences.getBoolean("rowoption1", false) || preferences.getBoolean("rowoption2", false)) {
            rorSwitch.setChecked(true);
            ((RadioButton) findViewById(R.id.rowoption1)).setEnabled(true);
            ((RadioButton) findViewById(R.id.rowoption1)).setChecked(preferences.getBoolean("rowoption1", false));
            ((RadioButton) findViewById(R.id.rowoption2)).setEnabled(true);
            ((RadioButton) findViewById(R.id.rowoption2)).setChecked(preferences.getBoolean("rowoption2", false));
        }
        if (preferences.getBoolean("tressoption1", false) || preferences.getBoolean("tressoption2", false)) {
            tressSwitch.setChecked(true);
            ((RadioButton) findViewById(R.id.tressoption1)).setEnabled(true);
            ((RadioButton) findViewById(R.id.tressoption1)).setChecked(preferences.getBoolean("tressoption1", false));
            ((RadioButton) findViewById(R.id.tressoption2)).setEnabled(true);
            ((RadioButton) findViewById(R.id.tressoption2)).setChecked(preferences.getBoolean("tressoption2", false));
        }
        if (preferences.getBoolean("soilTypeoption1", false) || preferences.getBoolean("soilTypeoption2", false)) {
            soilTypeSwitch.setChecked(true);
            ((RadioButton) findViewById(R.id.soilTypeoption1)).setEnabled(true);
            ((RadioButton) findViewById(R.id.soilTypeoption1)).setChecked(preferences.getBoolean("soilTypeoption1", false));
            ((RadioButton) findViewById(R.id.soilTypeoption2)).setEnabled(true);
            ((RadioButton) findViewById(R.id.soilTypeoption2)).setChecked(preferences.getBoolean("soilTypeoption2", false));
        }
        if (preferences.getBoolean("nearestRoadoption", false)) {
            nearestRoadSwitch.setChecked(true);
        }
        if (preferences.getBoolean("roadDistanceoption1", false) || preferences.getBoolean("roadDistanceoption2", false)) {
            roadDistanceSwitch.setChecked(true);
            ((RadioButton) findViewById(R.id.roadDistanceoption1)).setEnabled(true);
            ((RadioButton) findViewById(R.id.roadDistanceoption1)).setChecked(preferences.getBoolean("roadDistanceoption1", false));
            ((RadioButton) findViewById(R.id.roadDistanceoption2)).setEnabled(true);
            ((RadioButton) findViewById(R.id.roadDistanceoption2)).setChecked(preferences.getBoolean("roadDistanceoption2", false));
        }
        if (preferences.getBoolean("seedoption1", false) || preferences.getBoolean("seedoption2", false)) {
            seedSwitch.setChecked(true);
            ((RadioButton) findViewById(R.id.seedoption1)).setEnabled(true);
            ((RadioButton) findViewById(R.id.seedoption1)).setChecked(preferences.getBoolean("seedoption1", false));
            ((RadioButton) findViewById(R.id.seedoption2)).setEnabled(true);
            ((RadioButton) findViewById(R.id.seedoption2)).setChecked(preferences.getBoolean("seedoption2", false));
        }
        if (preferences.getBoolean("prevoption1", false) || preferences.getBoolean("prevoption2", false)) {
            prevSwitch.setChecked(true);
            ((RadioButton) findViewById(R.id.prevoption1)).setEnabled(true);
            ((RadioButton) findViewById(R.id.prevoption1)).setChecked(preferences.getBoolean("prevoption1", false));
            ((RadioButton) findViewById(R.id.prevoption2)).setEnabled(true);
            ((RadioButton) findViewById(R.id.prevoption2)).setChecked(preferences.getBoolean("prevoption2", false));
        }
        if (preferences.getBoolean("interoption1", false) || preferences.getBoolean("interoption2", false)) {
            interSwitch.setChecked(true);
            ((RadioButton) findViewById(R.id.interoption1)).setEnabled(true);
            ((RadioButton) findViewById(R.id.interoption1)).setChecked(preferences.getBoolean("interoption1", false));
            ((RadioButton) findViewById(R.id.interoption2)).setEnabled(true);
            ((RadioButton) findViewById(R.id.interoption2)).setChecked(preferences.getBoolean("interoption2", false));
        }
        if (preferences.getBoolean("dev1option1", false) || preferences.getBoolean("dev1option2", false)) {
            dev1Switch.setChecked(true);
            ((RadioButton) findViewById(R.id.dev1option1)).setEnabled(true);
            ((RadioButton) findViewById(R.id.dev1option1)).setChecked(preferences.getBoolean("dev1option1", false));
            ((RadioButton) findViewById(R.id.dev1option2)).setEnabled(true);
            ((RadioButton) findViewById(R.id.dev1option2)).setChecked(preferences.getBoolean("dev1option2", false));
        }
        if (preferences.getBoolean("dev2option1", false) || preferences.getBoolean("dev2option2", false)) {
            dev2Switch.setChecked(true);
            ((RadioButton) findViewById(R.id.dev2option1)).setEnabled(true);
            ((RadioButton) findViewById(R.id.dev2option1)).setChecked(preferences.getBoolean("dev2option1", false));
            ((RadioButton) findViewById(R.id.dev2option2)).setEnabled(true);
            ((RadioButton) findViewById(R.id.dev2option2)).setChecked(preferences.getBoolean("dev2option2", false));
        }
        if (preferences.getBoolean("dev3option1", false) || preferences.getBoolean("dev3option2", false)) {
            dev3Switch.setChecked(true);
            ((RadioButton) findViewById(R.id.dev3option1)).setEnabled(true);
            ((RadioButton) findViewById(R.id.dev3option1)).setChecked(preferences.getBoolean("dev3option1", false));
            ((RadioButton) findViewById(R.id.dev3option2)).setEnabled(true);
            ((RadioButton) findViewById(R.id.dev3option2)).setChecked(preferences.getBoolean("dev3option2", false));
        }
        if (preferences.getBoolean("dev4option1", false) || preferences.getBoolean("dev4option2", false)) {
            dev4Switch.setChecked(true);
            ((RadioButton) findViewById(R.id.dev4option1)).setEnabled(true);
            ((RadioButton) findViewById(R.id.dev4option1)).setChecked(preferences.getBoolean("dev4option1", false));
            ((RadioButton) findViewById(R.id.dev4option2)).setEnabled(true);
            ((RadioButton) findViewById(R.id.dev4option2)).setChecked(preferences.getBoolean("dev4option2", false));
        }
        if (preferences.getBoolean("dev5soption1", false) || preferences.getBoolean("dev5option2", false)) {
            dev5Switch.setChecked(true);
            ((RadioButton) findViewById(R.id.dev5soption1)).setEnabled(true);
            ((RadioButton) findViewById(R.id.dev5soption1)).setChecked(preferences.getBoolean("dev5soption1", false));
            ((RadioButton) findViewById(R.id.dev5option2)).setEnabled(true);
            ((RadioButton) findViewById(R.id.dev5option2)).setChecked(preferences.getBoolean("dev5option2", false));
        }


    }

    @Override
    public void onBackPressed() {
        editor.clear().apply();
        if (irrigationSwitch.isChecked()) {
            if (irrigationGroup.getCheckedRadioButtonId() != -1) {
                if (irrigationGroup.getCheckedRadioButtonId() == R.id.irrioption1) {
                    editor.putBoolean("irrioption1", true).commit();
                } else {
                    editor.putBoolean("irrioption2", true).commit();

                }
            } else {
                ((RadioButton) irrigationGroup.getChildAt(1)).setError("Select Option");
            }
        } else {
            editor.putBoolean("irrioption1", false).commit();
            editor.putBoolean("irrioption2", false).commit();
        }
        if (supplySwitch.isChecked()) {
            if (supplyGroup.getCheckedRadioButtonId() != -1) {
                if (supplyGroup.getCheckedRadioButtonId() == R.id.supplyoption1) {
                    editor.putBoolean("supplyoption1", true).commit();
                } else {
                    editor.putBoolean("supplyoption2", true).commit();
                }
            } else {
                ((RadioButton) supplyGroup.getChildAt(1)).setError("Select Option");
                return;
            }
        } else {
            editor.putBoolean("supplyoption1", false).commit();
            editor.putBoolean("supplyoption2", false).commit();
        }

        if (soilSwitch.isChecked()) {
            if (soilGroup.getCheckedRadioButtonId() != -1) {
                if (soilGroup.getCheckedRadioButtonId() == R.id.soiloption1) {
                    editor.putBoolean("soiloption1", true).commit();
                } else {
                    editor.putBoolean("soiloption2", true).commit();
                }
            } else {
                ((RadioButton) soilGroup.getChildAt(1)).setError("Select Option");
                return;
            }
        } else {
            editor.putBoolean("soiloption1", false).commit();
            editor.putBoolean("soiloption2", false).commit();
        }
        if (waterSourceSwitch.isChecked()) {
            if (waterSourceGroup.getCheckedRadioButtonId() != -1) {
                if (waterSourceGroup.getCheckedRadioButtonId() == R.id.waterSourceoption1) {
                    editor.putBoolean("waterSourceoption1", true).commit();
                } else {
                    editor.putBoolean("waterSourceoption2", true).commit();
                }

            } else {
                ((RadioButton) waterSourceGroup.getChildAt(1)).setError("Select Option");
                return;
            }
        } else {
            editor.putBoolean("waterSourceoption1", false).commit();
            editor.putBoolean("waterSourceoption2", false).commit();
        }
        if (waterTypeSwitch.isChecked()) {
            if (waterTypeGroup.getCheckedRadioButtonId() != -1) {
                if (waterTypeGroup.getCheckedRadioButtonId() == R.id.waterTypeoption1) {
                    editor.putBoolean("waterTypeoption1", true).commit();
                } else {
                    editor.putBoolean("waterTypeoption2", true).commit();
                }
            } else {
                ((RadioButton) waterTypeGroup.getChildAt(1)).setError("Select option");
                return;
            }
        } else {
            editor.putBoolean("waterTypeoption1", false).commit();
            editor.putBoolean("waterTypeoption2", false).commit();
        }
        if (waterTestedSwitch.isChecked()) {
            if (waterTestedGroup.getCheckedRadioButtonId() != -1) {
                if (waterTestedGroup.getCheckedRadioButtonId() == R.id.waterTestedoption1) {
                    editor.putBoolean("waterTestedoption1", true).commit();
                } else {
                    editor.putBoolean("waterTestedoption2", true).commit();
                }
            } else {
                ((RadioButton) waterTestedGroup.getChildAt(1)).setError("Select Option");
                return;
            }
        } else {
            editor.putBoolean("waterTestedoption1", false).commit();
            editor.putBoolean("waterTestedoption2", false).commit();
        }
        if (fieldSwitch.isChecked()) {
            if (fieldGroup.getCheckedRadioButtonId() != -1) {
                if (fieldGroup.getCheckedRadioButtonId() == R.id.fieldoption1) {
                    editor.putBoolean("fieldoption1", true).commit();
                } else {
                    editor.putBoolean("fieldoption2", true).commit();
                }
            } else {
                ((RadioButton) fieldGroup.getChildAt(1)).setError("Select Option");
                return;
            }
        } else {
            editor.putBoolean("fieldoption1", false).commit();
            editor.putBoolean("fieldoption2", false).commit();
        }
        if (elevationSwitch.isChecked()) {
            if (elevationGroup.getCheckedRadioButtonId() != -1) {
                if (elevationGroup.getCheckedRadioButtonId() == R.id.elevationoption1) {
                    editor.putBoolean("elevationoption1", true).commit();
                } else {
                    editor.putBoolean("elevationoption2", true).commit();
                }
            } else {
                ((RadioButton) elevationGroup.getChildAt(1)).setError("Select Option");
                return;
            }
        } else {
            editor.putBoolean("elevationoption1", false).commit();
            editor.putBoolean("elevationoption2", false).commit();
        }
        if (soilTypeSwitch.isChecked()) {
            if (soilTypeGroup.getCheckedRadioButtonId() != -1) {
                if (soilTypeGroup.getCheckedRadioButtonId() == R.id.soilTypeoption1) {
                    editor.putBoolean("soilTypeoption1", true).commit();
                } else {
                    editor.putBoolean("soilTypeoption2", true).commit();
                }
            } else {
                ((RadioButton) soilTypeGroup.getChildAt(1)).setError("Select this file");
                return;
            }
        } else {
            editor.putBoolean("soilTypeoption1", false).commit();
            editor.putBoolean("soilTypeoption2", false).commit();
        }
        if (rorSwitch.isChecked()) {
            if (rorGroup.getCheckedRadioButtonId() != -1) {
                if (rorGroup.getCheckedRadioButtonId() == R.id.rowoption1) {
                    editor.putBoolean("rowoption1", true).commit();
                } else {
                    editor.putBoolean("rowoption2", true).commit();
                }
            } else {
                ((RadioButton) rorGroup.getChildAt(1)).setError("Select Option");
                return;
            }
        } else {
            editor.putBoolean("rowoption1", false).commit();
            editor.putBoolean("rowoption2", false).commit();
        }
        if (harvSwitch.isChecked()) {
            if (harvGroup.getCheckedRadioButtonId() != -1) {
                if (harvGroup.getCheckedRadioButtonId() == R.id.harvoption1) {
                    editor.putBoolean("harvoption1", true).commit();
                } else {
                    editor.putBoolean("harvoption2", true).commit();
                }
            } else {
                ((RadioButton) harvGroup.getChildAt(1)).setError("Select option");
                return;
            }
        } else {
            editor.putBoolean("harvoption1", false).commit();
            editor.putBoolean("harvoption2", false).commit();
        }
        if (tressSwitch.isChecked()) {
            if (tressGroup.getCheckedRadioButtonId() != -1) {
                if (tressGroup.getCheckedRadioButtonId() == R.id.tressoption1) {
                    editor.putBoolean("tressoption1", true).commit();
                } else {
                    editor.putBoolean("tressoption2", true).commit();
                }
            } else {
                ((RadioButton) tressGroup.getChildAt(1)).setError("Select option");
                return;
            }
        } else {
            editor.putBoolean("tressoption1", false).commit();
            editor.putBoolean("tressoption2", false).commit();
        }

        if (nearestRoadSwitch.isChecked()) {
            editor.putBoolean("nearestRoadoption", true).commit();
        } else {
            editor.putBoolean("nearestRoadoption", false).commit();
        }
        if (roadDistanceSwitch.isChecked()) {
            if (roadDistanceGroup.getCheckedRadioButtonId() != -1) {
                if (roadDistanceGroup.getCheckedRadioButtonId() == R.id.roadDistanceoption1) {
                    editor.putBoolean("roadDistanceoption1", true).commit();
                } else {
                    editor.putBoolean("roadDistanceoption2", true).commit();
                }
            } else {
                ((RadioButton) roadDistanceGroup.getChildAt(1)).setError("Select option");
                return;
            }
        } else {
            editor.putBoolean("roadDistanceoption1", false).commit();
            editor.putBoolean("roadDistanceoption2", false).commit();
        }
        if (seedSwitch.isChecked()) {
            if (seedGroup.getCheckedRadioButtonId() != -1) {
                if (seedGroup.getCheckedRadioButtonId() == R.id.seedoption1) {
                    editor.putBoolean("seedoption1", true).commit();
                } else {
                    editor.putBoolean("seedoption2", true).commit();
                }
            } else {
                ((RadioButton) seedGroup.getChildAt(1)).setError("Select option");
                return;
            }
        } else {
            editor.putBoolean("seedoption1", false).commit();
            editor.putBoolean("seedoption2", false).commit();
        }
        if (interSwitch.isChecked()) {
            if (interGroup.getCheckedRadioButtonId() != -1) {
                if (interGroup.getCheckedRadioButtonId() == R.id.interoption1) {
                    editor.putBoolean("interoption1", true).commit();
                } else {
                    editor.putBoolean("interoption2", true).commit();
                }
            } else {
                ((RadioButton) interGroup.getChildAt(1)).setError("Select option");
                return;
            }
        } else {
            editor.putBoolean("interoption1", false).commit();
            editor.putBoolean("interoption2", false).commit();
        }
        if (prevSwitch.isChecked()) {
            if (prevGroup.getCheckedRadioButtonId() != -1) {
                if (prevGroup.getCheckedRadioButtonId() == R.id.prevoption1) {
                    editor.putBoolean("prevoption1", true).commit();
                } else {
                    editor.putBoolean("prevoption2", true).commit();
                }
            } else {
                ((RadioButton) prevGroup.getChildAt(1)).setError("Select option");
                return;
            }
        } else {
            editor.putBoolean("prevoption1", false).commit();
            editor.putBoolean("prevoption2", false).commit();
        }
        if (dev1Switch.isChecked()) {
            if (dev1Group.getCheckedRadioButtonId() != -1) {
                if (dev1Group.getCheckedRadioButtonId() == R.id.dev1option1) {
                    editor.putBoolean("dev1option1", true).commit();
                } else {
                    editor.putBoolean("dev1option2", true).commit();
                }
            } else {
                ((RadioButton) dev1Group.getChildAt(1)).setError("Select option");
                return;
            }
        } else {
            editor.putBoolean("dev1option1", false).commit();
            editor.putBoolean("dev1option2", false).commit();
        }
        if (dev2Switch.isChecked()) {
            if (dev2Group.getCheckedRadioButtonId() != -1) {
                if (dev2Group.getCheckedRadioButtonId() == R.id.dev2option1) {
                    editor.putBoolean("dev2option1", true).commit();
                } else {
                    editor.putBoolean("dev2option2", true).commit();
                }
            } else {
                ((RadioButton) dev2Group.getChildAt(1)).setError("Select option");
                return;
            }
        } else {
            editor.putBoolean("dev2option1", false).commit();
            editor.putBoolean("dev2option2", false).commit();
        }
        if (dev3Switch.isChecked()) {
            if (dev3Group.getCheckedRadioButtonId() != -1) {
                if (dev3Group.getCheckedRadioButtonId() == R.id.dev3option1) {
                    editor.putBoolean("dev3option1", true).commit();
                } else {
                    editor.putBoolean("dev3option2", true).commit();
                }
            } else {
                ((RadioButton) dev3Group.getChildAt(1)).setError("Select option");
                return;
            }
        } else {
            editor.putBoolean("dev3option1", false).commit();
            editor.putBoolean("dev3option2", false).commit();
        }
        if (dev4Switch.isChecked()) {
            if (dev4Group.getCheckedRadioButtonId() != -1) {
                if (dev4Group.getCheckedRadioButtonId() == R.id.dev4option1) {
                    editor.putBoolean("dev4option1", true).commit();
                } else {
                    editor.putBoolean("dev4option2", true).commit();
                }
            } else {
                ((RadioButton) dev4Group.getChildAt(1)).setError("Select option");
                return;
            }
        } else {
            editor.putBoolean("dev4option1", false).commit();
            editor.putBoolean("dev4option2", false).commit();
        }
        if (dev5Switch.isChecked()) {
            if (dev5Group.getCheckedRadioButtonId() != -1) {
                if (dev5Group.getCheckedRadioButtonId() == R.id.dev5soption1) {
                    editor.putBoolean("dev5soption1", true).commit();
                } else {
                    editor.putBoolean("dev5option2", true).commit();
                }
            } else {
                ((RadioButton) dev5Group.getChildAt(1)).setError("Select option");
                return;
            }
        } else {
            editor.putBoolean("dev5soption1", false).commit();
            editor.putBoolean("dev5option2", false).commit();
        }

        super.onBackPressed();
    }

    private void init() {
        preferences = getSharedPreferences("Choice", 0);
        editor = preferences.edit();
        irrigationSwitch = findViewById(R.id.irrigationSwitch);
        soilSwitch = findViewById(R.id.soilSwitch);
        waterTestedSwitch = findViewById(R.id.waterTestedSwitch);
        waterSourceSwitch = findViewById(R.id.waterSourceSwitch);
        waterTypeSwitch = findViewById(R.id.waterTypeSwitch);
        supplySwitch = findViewById(R.id.supplySwitch);
        fieldSwitch = findViewById(R.id.fieldSwitch);
        elevationSwitch = findViewById(R.id.elevationSwitch);
        soilTypeSwitch = findViewById(R.id.soilTypeSwitch);
        rorSwitch = findViewById(R.id.rorSwitch);
        harvSwitch = findViewById(R.id.harvSwitch);
        tressSwitch = findViewById(R.id.tressSwitch);
        roadDistanceSwitch = findViewById(R.id.roadDistanceSwitch);
        nearestRoadSwitch = findViewById(R.id.nearestRoadSwitch);
        seedSwitch = findViewById(R.id.caneSeedSwitch);
        interSwitch = findViewById(R.id.interCropSwitch);
        prevSwitch = findViewById(R.id.prevCropSwitch);
        dev1Switch = findViewById(R.id.dev1Switch);
        dev2Switch = findViewById(R.id.dev2Switch);
        dev3Switch = findViewById(R.id.dev3Switch);
        dev4Switch = findViewById(R.id.dev4Switch);
        dev5Switch = findViewById(R.id.dev5Switch);


        irrigationGroup = findViewById(R.id.irrigationGroup);
        waterSourceGroup = findViewById(R.id.waterSourceGroup);
        soilGroup = findViewById(R.id.soilGroup);
        waterTestedGroup = findViewById(R.id.waterTestedGroup);
        waterTypeGroup = findViewById(R.id.waterTypeGroup);
        supplyGroup = findViewById(R.id.supplyGroup);
        fieldGroup = findViewById(R.id.fieldGroup);
        elevationGroup = findViewById(R.id.elevationGroup);
        soilTypeGroup = findViewById(R.id.soilTypeGroup);
        rorGroup = findViewById(R.id.rorGroup);
        harvGroup = findViewById(R.id.harvGroup);
        tressGroup = findViewById(R.id.tressGroup);
        roadDistanceGroup = findViewById(R.id.roadDistanceGroup);

        seedGroup = findViewById(R.id.seedGroup);
        prevGroup = findViewById(R.id.prevGroup);
        interGroup = findViewById(R.id.interGroup);
        dev1Group = findViewById(R.id.dev1Group);
        dev2Group = findViewById(R.id.dev2Group);
        dev3Group = findViewById(R.id.dev3Group);
        dev4Group = findViewById(R.id.dev4Group);
        dev5Group = findViewById(R.id.dev5Group);

    }
}
