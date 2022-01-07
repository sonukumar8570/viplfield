package in.visibleinfotech.viplfieldapplications.field_slip.model;

public class Plot {
    private String PL_PLOTID, PL_GrowerCode, PL_VillageCode, PL_plantationdate, PL_CropId, PL_CaneId, pl_AREA, pl_supplymodeId, pl_landName, PL_enable, Maturity_Days,
            pl_esttons, PL_GName, place_name, PL_CropNAMe, PL_VARNAME, zone_id, PL_plotvillcode, PL_VillNAme;
    int PL_PlotBrix;

    public Plot(String PL_PLOTID, String PL_GrowerCode, String PL_VillageCode, String PL_plantationdate, String PL_CropId, String PL_CaneId, String pl_AREA, String pl_supplymodeId, String pl_landName, String PL_enable, String maturity_Days, String pl_esttons, String PL_GName, String place_name, String PL_CropNAMe, String PL_VARNAME, String zone_id, String PL_plotvillcode, String PL_VillNAme,int PL_PlotBrix) {
        this.PL_PLOTID = PL_PLOTID;
        this.PL_GrowerCode = PL_GrowerCode;
        this.PL_VillageCode = PL_VillageCode;
        this.PL_plantationdate = PL_plantationdate;
        this.PL_CropId = PL_CropId;
        this.PL_CaneId = PL_CaneId;
        this.pl_AREA = pl_AREA;
        this.PL_PlotBrix=PL_PlotBrix;
        this.pl_supplymodeId = pl_supplymodeId;
        this.pl_landName = pl_landName;
        this.PL_enable = PL_enable;
        Maturity_Days = maturity_Days;
        this.pl_esttons = pl_esttons;
        this.PL_GName = PL_GName;
        this.place_name = place_name;
        this.PL_CropNAMe = PL_CropNAMe;
        this.PL_VARNAME = PL_VARNAME;
        this.zone_id = zone_id;
        this.PL_plotvillcode = PL_plotvillcode;
        this.PL_VillNAme = PL_VillNAme;
    }

    public int getPL_PlotBrix() {
        return PL_PlotBrix;
    }

    public String getPL_PLOTID() {
        return PL_PLOTID;
    }

    public String getPL_GrowerCode() {
        return PL_GrowerCode;
    }

    public String getPL_VillageCode() {
        return PL_VillageCode;
    }

    public String getPL_plantationdate() {
        return PL_plantationdate;
    }

    public String getPL_CropId() {
        return PL_CropId;
    }

    public String getPL_CaneId() {
        return PL_CaneId;
    }

    public String getPl_AREA() {
        return pl_AREA;
    }

    public String getPl_supplymodeId() {
        return pl_supplymodeId;
    }

    public String getPl_landName() {
        return pl_landName;
    }

    public String getPL_enable() {
        return PL_enable;
    }

    public String getMaturity_Days() {
        return Maturity_Days;
    }

    public String getPl_esttons() {
        return pl_esttons;
    }

    public String getPL_GName() {
        return PL_GName;
    }

    public String getPlace_name() {
        return place_name;
    }

    public String getPL_CropNAMe() {
        return PL_CropNAMe;
    }

    public String getPL_VARNAME() {
        return PL_VARNAME;
    }

    public String getZone_id() {
        return zone_id;
    }

    public String getPL_plotvillcode() {
        return PL_plotvillcode;
    }

    public String getPL_VillNAme() {
        return PL_VillNAme;
    }
}