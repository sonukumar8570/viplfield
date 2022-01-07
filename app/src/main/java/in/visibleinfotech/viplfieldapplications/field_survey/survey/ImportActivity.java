package in.visibleinfotech.viplfieldapplications.field_survey.survey;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import in.visibleinfotech.viplfieldapplications.ConnectionStr;
import in.visibleinfotech.viplfieldapplications.MainConstant;
import in.visibleinfotech.viplfieldapplications.R;
import in.visibleinfotech.viplfieldapplications.field_survey.localdatabase.ViplDatabase;
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

public class ImportActivity extends AppCompatActivity {
    TextView loadMsg;

    ViplDatabase viplDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.survey_activity_import);

        viplDatabase = ViplDatabase.getDatabase(this);

        loadMsg = findViewById(R.id.loaded);


        new ZonesFromServer().execute();

    }


    class ZonesFromServer extends AsyncTask<String, Integer, Void> {
        ProgressDialog progress;
        boolean flag = false;

        StringBuffer builder;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(ImportActivity.this);
            progress.setMessage("Loading..please wait.....");
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.setIndeterminate(false);
            progress.setMax(100);
            progress.setIcon(android.R.drawable.stat_sys_download);
            progress.setCancelable(false);
            progress.show();
            builder = new StringBuffer();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progress.setProgress(values[0]);
        }

        @Override
        protected Void doInBackground(String... voids) {
            ConnectionStr conStr = new ConnectionStr();

            MainConstant c = new MainConstant(ImportActivity.this);
            if (c.getIp() != null) {
                try {
                    Connection connect = conStr.connectionclasss(c.getDbUser(), c.getDbPass(), c.getDbName(), c.getIp(), c.getInstance());
                    if (connect == null) {
                        flag = true;
                    } else {
                        viplDatabase.clearAllTables();
                        Statement stmt = connect.createStatement();
                        try {

                            stmt.setQueryTimeout(5000);
                            String q = "select place_id,place_name from VIPL_Place_master";
                            Log.d("myname", q);
                            ResultSet rs = stmt.executeQuery(q);

                            while (rs.next()) {
                                publishProgress((int) (100 / 26));
                                viplDatabase.placeMasterDao().insert(new PlaceMaster(rs.getString(1).trim(), rs.getString(2).trim()));
                            }
                        } catch (SQLException e) {
                            Log.d("myname", "" + e);
                        }
                        try {
                            stmt = connect.createStatement();
                            String q = "select account_id,a.place_id,account_name,isnull(G_MobileNo,1234567890) G_MobileNo from VIPL_Accounts_master a inner join Vipl_Place_master b \n" +
                                    "on a.place_id=b.place_id inner join Vipl_zonemaster c on b.zone_id = c.z_code  where G_AcType='F' and G_Enable = 1";

                            Log.d("myname", q);
                            stmt.setQueryTimeout(5000);
                            ResultSet rs = stmt.executeQuery(q);

                            while (rs.next()) {
                                publishProgress((int) (2 * 100 / 26));
                                viplDatabase.accountMasterDao().insert(new AccountMaster(rs.getString(1).trim(), rs.getString(2).trim(), rs.getString(3).trim(), rs.getString(4).trim()));

                            }
                            stmt.close();
                        } catch (SQLException e) {

                            Log.d("myname", "" + e);

                        }
                        try {
                            stmt = connect.createStatement();
                            String q = "select nCropId,cCropType from VIPL_CropType";
                            Log.d("myname", q);
                            stmt.setQueryTimeout(5000);
                            ResultSet rs = stmt.executeQuery(q);

                            while (rs.next()) {
                                publishProgress((int) (3 * 100 / 26));
                                viplDatabase.cropTypeDao().insert(new CropType(rs.getString(1).trim(), rs.getString(2).trim()));
                            }
                            stmt.close();
                        } catch (SQLException e) {
                            Log.d("myname", "" + e);
                        }
                        try {
                            stmt = connect.createStatement();
                            String q = "select id,Variety_Name from VIPL_VarietyMaster";
                            Log.d("myname", q);
                            stmt.setQueryTimeout(5000);
                            ResultSet rs = stmt.executeQuery(q);

                            while (rs.next()) {
                                publishProgress((int) (4 * 100 / 26));
                                viplDatabase.varietyMasterDao().insert(new VarietyMaster(rs.getString(1).trim(), rs.getString(2).trim()));
                            }
                            stmt.close();
                        } catch (SQLException e) {
                            Log.d("myname", "" + e);
                        }
                        try {
                            stmt = connect.createStatement();
                            String q = "select S_Code,S_Name from VIPL_SoilMaster";
                            Log.d("myname", q);
                            stmt.setQueryTimeout(5000);
                            ResultSet rs = stmt.executeQuery(q);

                            while (rs.next()) {
                                publishProgress((int) (5 * 100 / 26));
                                viplDatabase.soilMasterDao().insert(new SoilMaster(rs.getString(1).trim(), rs.getString(2).trim()));
                            }
                            stmt.close();
                        } catch (SQLException e) {
                            Log.d("myname", "" + e);
                        }
                        try {
                            stmt = connect.createStatement();
                            String q = "select WT_Code,WT_Name from VIPL_WaterType";
                            Log.d("myname", q);
                            stmt.setQueryTimeout(5000);
                            ResultSet rs = stmt.executeQuery(q);

                            while (rs.next()) {
                                publishProgress((int) (6 * 100 / 26));
                                viplDatabase.waterTypeDao().insert(new WaterType(rs.getString(1).trim(), rs.getString(2).trim()));
                            }
                            stmt.close();
                        } catch (SQLException e) {
                            Log.d("myname", "" + e);
                        }
                        try {
                            stmt = connect.createStatement();
                            String q = "select M_Code,M_Name from VIPL_ModeMaster";
                            Log.d("myname", q);
                            stmt.setQueryTimeout(5000);
                            ResultSet rs = stmt.executeQuery(q);

                            while (rs.next()) {
                                publishProgress((int) (7 * 100 / 26));
                                viplDatabase.supplyMasterDao().insert(new SupplyMaster(rs.getString(1).trim(), rs.getString(2).trim()));
                            }
                            stmt.close();
                        } catch (SQLException e) {
                            Log.d("myname", "" + e);
                        }

                        try {
                            stmt = connect.createStatement();
                            String q = "select W_Code,W_WaterSource from VIPL_WaterSourceMaster";
                            Log.d("myname", q);
                            stmt.setQueryTimeout(5000);
                            ResultSet rs = stmt.executeQuery(q);

                            while (rs.next()) {
                                publishProgress((int) (8 * 100 / 26));
                                viplDatabase.waterSourceDao().insert(new WaterSourceMaster(rs.getString(1).trim(), rs.getString(2).trim()));
                            }
                            stmt.close();
                        } catch (SQLException e) {
                            Log.d("myname", "" + e);
                        }
                        try {
                            stmt = connect.createStatement();
                            String q = "select Ir_Codge,Ir_Name from VIPL_IrrigationMaster";
                            Log.d("myname", q);
                            stmt.setQueryTimeout(5000);
                            ResultSet rs = stmt.executeQuery(q);

                            while (rs.next()) {
                                publishProgress((int) (9 * 100 / 26));
                                viplDatabase.irrigationMasterDao().insert(new IrrigationMaster(rs.getString(1).trim(), rs.getString(2).trim()));
                            }
                            stmt.close();
                        } catch (SQLException e) {
                            Log.d("myname", "" + e);
                        }
                        try {
                            stmt = connect.createStatement();
                            String q = "select F_FieldCode,F_FieldName from VIPL_FieldQualityMaster";
                            Log.d("myname", q);
                            stmt.setQueryTimeout(5000);
                            ResultSet rs = stmt.executeQuery(q);

                            while (rs.next()) {
                                publishProgress((int) (10 * 100 / 26));
                                viplDatabase.qualitiyMasterDao().insert(new FieldQualityMaster(rs.getString(1).trim(), rs.getString(2).trim()));
                            }
                            stmt.close();
                        } catch (SQLException e) {
                            Log.d("myname", "" + e);
                        }
                        try {
                            stmt = connect.createStatement();
                            String q = "select Cane_RowCode,Cane_RowFit from VIPL_CaneRowDistanceMaster";
                            Log.d("myname", q);
                            stmt.setQueryTimeout(5000);
                            ResultSet rs = stmt.executeQuery(q);

                            while (rs.next()) {
                                publishProgress((int) 11 * 100 / 26);
                                viplDatabase.distanceMasterDao().insert(new CaneRowDistanceMaster(rs.getString(1).trim(), rs.getString(2).trim()));
                            }
                            stmt.close();
                        } catch (SQLException e) {
                            Log.d("myname", "" + e);
                        }
                        try {
                            stmt = connect.createStatement();
                            String q = "select ROW_Code,ROW_Name from VIPL_RowToRowFieldMapDirction";
                            Log.d("myname", q);
                            stmt.setQueryTimeout(5000);
                            ResultSet rs = stmt.executeQuery(q);

                            while (rs.next()) {
                                publishProgress((int) 12 * 100 / 26);
                                viplDatabase.rowDirectionDao().insert(new RowToRowFieldMapDirection(rs.getString(1).trim(), rs.getString(2).trim()));
                            }
                            stmt.close();
                        } catch (SQLException e) {
                            Log.d("myname", "" + e);
                        }
                        try {
                            stmt = connect.createStatement();
                            String q = "select FE_Code,FE_Name from VIPL_FieldElivationTypeMaster";
                            Log.d("myname", q);
                            stmt.setQueryTimeout(5000);
                            ResultSet rs = stmt.executeQuery(q);

                            while (rs.next()) {
                                publishProgress((int) 13 * 100 / 26);
                                viplDatabase.elevationMasterDao().insert(new FieldElivationTypeMaster(rs.getString(1).trim(), rs.getString(2).trim()));
                            }
                            stmt.close();
                        } catch (SQLException e) {
                            Log.d("myname", "" + e);
                        }
                        try {
                            stmt = connect.createStatement();
                            String q = "select H_Code,H_Name from Vipl_HarvMaster";
                            Log.d("myname", q);
                            stmt.setQueryTimeout(5000);
                            ResultSet rs = stmt.executeQuery(q);

                            while (rs.next()) {
                                publishProgress((int) 14 * 100 / 26);
                                viplDatabase.harvMasterDao().insert(new HarvMaster(rs.getString(1).trim(), rs.getString(2).trim()));
                            }
                        } catch (SQLException e) {
                            Log.d("myname", "" + e);
                        }

                        try {
                            stmt = connect.createStatement();
                            String q = "select r_code,r_name,R_VillCode from VIPL_RouteMaster";
                            Log.d("myname", q);
                            stmt.setQueryTimeout(5000);
                            ResultSet rs = stmt.executeQuery(q);

                            while (rs.next()) {
                                publishProgress((int) 15 * 100 / 26);
                                viplDatabase.routeMasterDao().insert(new RouteMaster(rs.getString(1).trim(), rs.getString(2).trim(), rs.getString(3).trim()));
                            }
                            stmt.close();
                        } catch (SQLException e) {
                            Log.d("myname", "" + e);
                        }
                        try {
                            stmt = connect.createStatement();
                            String q = "select I_cropCode, I_cropName from Vipl_InterCropMaster";
                            Log.d("myname", q);
                            stmt.setQueryTimeout(5000);
                            ResultSet rs = stmt.executeQuery(q);

                            while (rs.next()) {
                                publishProgress((int) 16 * 100 / 26);
                                viplDatabase.interStateDao().insert(new InterCrop(rs.getString(1).trim(), rs.getString(2).trim()));

                            }
                            stmt.close();
                        } catch (SQLException e) {
                            Log.d("myname", "" + e);
                        }
                        try {
                            stmt = connect.createStatement();
                            String q = "select p_cropCode, p_cropName from Vipl_PrevCropMaster";
                            Log.d("myname", q);
                            stmt.setQueryTimeout(5000);
                            ResultSet rs = stmt.executeQuery(q);

                            while (rs.next()) {
                                publishProgress((int) 17 * 100 / 26);
                                viplDatabase.prevCropDao().insert(new PrevCrop(rs.getString(1).trim(), rs.getString(2).trim()));

                            }
                            stmt.close();
                        } catch (SQLException e) {
                            Log.d("myname", "" + e);
                        }
                        try {
                            stmt = connect.createStatement();
                            String q = "select S_Code,S_Name from Vipl_PlantSeed";
                            Log.d("myname", q);
                            stmt.setQueryTimeout(5000);
                            ResultSet rs = stmt.executeQuery(q);

                            while (rs.next()) {
                                publishProgress((int) 18 * 100 / 26);
                                viplDatabase.plantSeedDao().insert(new PlantSeed(rs.getString(1).trim(), rs.getString(2).trim()));

                            }
                            stmt.close();
                        } catch (SQLException e) {
                            Log.d("myname", "" + e);
                        }
                        try {
                            stmt = connect.createStatement();
                            String q = "select O_Code,O_Name from Vipl_CaneDevOption1";
                            Log.d("myname", q);
                            stmt.setQueryTimeout(5000);
                            ResultSet rs = stmt.executeQuery(q);

                            while (rs.next()) {
                                publishProgress((int) 19 * 100 / 26);
                                viplDatabase.dev1Dao().insert(new CaneDev1(rs.getString(1).trim(), rs.getString(2).trim()));

                            }
                            stmt.close();
                        } catch (SQLException e) {
                            Log.d("myname", "" + e);
                        }
                        try {
                            stmt = connect.createStatement();
                            String q = "select O_Code,O_Name from Vipl_CaneDevOption2";
                            Log.d("myname", q);
                            stmt.setQueryTimeout(5000);
                            ResultSet rs = stmt.executeQuery(q);

                            while (rs.next()) {
                                publishProgress((int) 20 * 100 / 26);
                                viplDatabase.dev2Dao().insert(new CaneDev2(rs.getString(1).trim(), rs.getString(2).trim()));

                            }
                            stmt.close();
                        } catch (SQLException e) {
                            Log.d("myname", "" + e);
                        }
                        try {
                            stmt = connect.createStatement();
                            String q = "select O_Code,O_Name from Vipl_CaneDevOption3";
                            Log.d("myname", q);
                            stmt.setQueryTimeout(5000);
                            ResultSet rs = stmt.executeQuery(q);

                            while (rs.next()) {
                                publishProgress((int) 21 * 100 / 26);
                                viplDatabase.dev3Dao().insert(new CaneDev3(rs.getString(1).trim(), rs.getString(2).trim()));

                            }
                            stmt.close();
                        } catch (SQLException e) {
                            Log.d("myname", "" + e);
                        }
                        try {
                            stmt = connect.createStatement();
                            String q = "select O_Code,O_Name from Vipl_CaneDevOption4";
                            Log.d("myname", q);
                            stmt.setQueryTimeout(5000);
                            ResultSet rs = stmt.executeQuery(q);

                            while (rs.next()) {
                                publishProgress((int) 22 * 100 / 26);
                                viplDatabase.dev4Dao().insert(new CaneDev4(rs.getString(1).trim(), rs.getString(2).trim()));

                            }
                            stmt.close();
                        } catch (SQLException e) {
                            Log.d("myname", "" + e);
                        }
                        try {
                            stmt = connect.createStatement();
                            String q = "select O_Code,O_Name from Vipl_CaneDevOption5";
                            Log.d("myname", q);
                            stmt.setQueryTimeout(5000);
                            ResultSet rs = stmt.executeQuery(q);

                            while (rs.next()) {
                                publishProgress((int) 23 * 100 / 26);
                                viplDatabase.dev5Dao().insert(new CaneDev5(rs.getString(1).trim(), rs.getString(2).trim()));

                            }
                            stmt.close();
                        } catch (SQLException e) {
                            Log.d("myname", "" + e);
                        }

                        try {
                            stmt = connect.createStatement();
                            String q = "select account_user_id,account_name from VIPL_Accounts_master where G_AcType='H'  and account_user_id is not Null";
                            Log.d("myname", q);
                            stmt.setQueryTimeout(5000);
                            ResultSet rs = stmt.executeQuery(q);
                            while (rs.next()) {
                                publishProgress((int) 24 * 100 / 26);
                                viplDatabase.harvesterDao().insert(new Harvester(rs.getString(1).trim(), rs.getString(2).trim()));
                            }
                            stmt.close();
                        } catch (SQLException e) {
                            Log.d("myname", "" + e);
                        }
                        try {
                            stmt = connect.createStatement();
                            String q = "select account_user_id,account_name from VIPL_Accounts_master where G_AcType='T'  and account_user_id is not  Null";
                            Log.d("myname", q);
                            stmt.setQueryTimeout(5000);
                            ResultSet rs = stmt.executeQuery(q);

                            while (rs.next()) {
                                publishProgress((int) 25 * 100 / 26);
                                viplDatabase.transporterDao().insert(new Transporter(rs.getString(1).trim(), rs.getString(2).trim()));
                            }
                            stmt.close();
                        } catch (SQLException e) {
                            Log.d("myname", "" + e);
                        }


                    }
                } catch (Exception e) {
                    Log.d("myname", e.toString());

                }
            } else {
                Toast.makeText(ImportActivity.this, "Please Edit settings First", Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);

            if (flag) {
                Toast.makeText(ImportActivity.this, "Settings are not correct", Toast.LENGTH_SHORT).show();
            } else {
                loadMsg.setText("All data loaded successfully as below :\n" + builder.toString());
            }
            progress.dismiss();

        }
    }
}
