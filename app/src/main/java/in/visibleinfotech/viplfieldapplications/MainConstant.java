package in.visibleinfotech.viplfieldapplications;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class MainConstant {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    public MainConstant(Context context) {
        sharedPreferences = context.getSharedPreferences("main", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    void saveLogin(String username, String mobile, String eCode, String ip, String dbName, String dbUser, String dbPass, String instance,
                   String ftpPath, String ftpUser, String ftpServer, String ftpPass, String siteCode, String companyName, int k_surveyArea, float k_SurveyAccuracy) {
        editor.putString("name", username);
        editor.putString("mobile", mobile);
        editor.putString("code", eCode);
        editor.putString("ip", ip);
        editor.putString("dbName", dbName);
        editor.putString("dbUser", dbUser);
        editor.putString("dbPass", dbPass);
        editor.putString("instance", instance);
        editor.putString("ftpPath", ftpPath).commit();
        editor.putString("ftpUser", ftpUser).commit();
        editor.putString("ftpServer", ftpServer).commit();
        editor.putString("ftpPass", ftpPass).commit();
        editor.putString("siteCode", siteCode).commit();
        editor.putString("companyName", companyName).commit();
        editor.putInt("k_surveyArea", k_surveyArea).commit();
        editor.putFloat("K_SurveyAccuracy", k_SurveyAccuracy).commit();
        editor.apply();
    }
    void savCompany(String name, String address) {
        editor.putString("C_name", name);
        editor.putString("C_address", address);
        editor.apply();
    }

    void logout() {
        editor.clear().apply();
    }

    public String getUsername() {
        return sharedPreferences.getString("name", null);
    }

    public String getMobile() {
        return sharedPreferences.getString("mobile", null);
    }

    public String geteCode() {
        return sharedPreferences.getString("code", null);
    }

    public String getIp() {
        return sharedPreferences.getString("ip", null);
    }

    public String getDbName() {
        return sharedPreferences.getString("dbName", null);
    }

    public String getDbUser() {
        return sharedPreferences.getString("dbUser", null);
    }

    public String getDbPass() {
        return sharedPreferences.getString("dbPass", null);
    }

    public String getInstance() {
        return sharedPreferences.getString("instance", null);
    }

    public String getFtpPath() {
        return sharedPreferences.getString("ftpPath", null);
    }

    public String getFtpUser() {
        return sharedPreferences.getString("ftpUser", null);
    }

    public String getFtpServer() {
        return sharedPreferences.getString("ftpServer", null);
    }

    public String getFtpPass() {
        return sharedPreferences.getString("ftpPass", null);
    }

    public String getSiteCode() {
        return sharedPreferences.getString("siteCode", null);
    }

    public String getCompanyName() {
        return sharedPreferences.getString("companyName", null);
    }

    public int getSurveyArea() {
        return sharedPreferences.getInt("k_surveyArea", 0);
    }

    public float getSurveyAccuracy() {
        return sharedPreferences.getFloat("K_SurveyAccuracy", 10.0f);
    }
}
