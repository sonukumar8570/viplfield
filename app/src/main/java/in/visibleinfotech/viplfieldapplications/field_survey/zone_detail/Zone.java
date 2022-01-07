package in.visibleinfotech.viplfieldapplications.field_survey.zone_detail;

public class Zone {
    String z_code, z_name, z_dist, z_tehsil;

    public Zone(String z_code, String z_name) {
        this.z_code = z_code;
        this.z_name = z_name;
        this.z_dist = z_dist;
        this.z_tehsil = z_tehsil;
    }

    public String getZ_code() {
        return z_code;
    }

    public String getZ_name() {
        return z_name;
    }

}
