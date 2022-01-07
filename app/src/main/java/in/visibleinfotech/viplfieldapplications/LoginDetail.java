package in.visibleinfotech.viplfieldapplications;

public class LoginDetail {
    String username, mobile, dbIp, dbUser, dbPass, dbName, instance, ftpServer, ftpUser, ftpPass, ftpPath, siteCode, companyName;
    int k_surveyArea;
    float K_SurveyAccuracy;

    public LoginDetail(String username, String mobile, String dbIp, String dbUser, String dbPass, String dbName, String instance, String ftpServer, String ftpUser, String ftpPass, String ftpPath, String siteCode, String companyName, int k_surveyArea, float k_SurveyAccuracy) {
        this.username = username;
        this.mobile = mobile;
        this.dbIp = dbIp;
        this.dbUser = dbUser;
        this.dbPass = dbPass;
        this.dbName = dbName;
        this.instance = instance;
        this.ftpServer = ftpServer;
        this.ftpUser = ftpUser;
        this.ftpPass = ftpPass;
        this.ftpPath = ftpPath;
        this.siteCode = siteCode;
        this.companyName = companyName;
        this.k_surveyArea = k_surveyArea;
        this.K_SurveyAccuracy=k_SurveyAccuracy;
    }

    public String getUsername() {
        return username;
    }

    public String getMobile() {
        return mobile;
    }

    public String getDbIp() {
        return dbIp;
    }

    public String getDbUser() {
        return dbUser;
    }

    public String getDbPass() {
        return dbPass;
    }

    public String getDbName() {
        return dbName;
    }

    public String getInstance() {
        return instance;
    }

    public String getFtpServer() {
        return ftpServer;
    }

    public String getFtpUser() {
        return ftpUser;
    }

    public String getFtpPass() {
        return ftpPass;
    }

    public String getFtpPath() {
        return ftpPath;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public String getCompanyName() {
        return companyName;
    }
}
