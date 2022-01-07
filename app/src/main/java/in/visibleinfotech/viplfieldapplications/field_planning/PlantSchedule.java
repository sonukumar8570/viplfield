package in.visibleinfotech.viplfieldapplications.field_planning;

public class PlantSchedule {
    int pp_updStatus;
    private String pp_plotId, pp_adviceCode, pp_AdviceDate, p_adviceHn, pp_upddate, p_adviceEng, pp_remark, pp_status;

    public PlantSchedule(int pp_updStatus, String pp_plotId, String pp_adviceCode, String pp_AdviceDate, String p_adviceHn, String pp_upddate, String p_adviceEng, String pp_remark, String pp_status) {
        this.pp_updStatus = pp_updStatus;
        this.pp_plotId = pp_plotId;
        this.pp_adviceCode = pp_adviceCode;
        this.pp_AdviceDate = pp_AdviceDate;
        this.p_adviceHn = p_adviceHn;
        this.pp_upddate = pp_upddate;
        this.p_adviceEng = p_adviceEng;
        this.pp_remark = pp_remark;
        this.pp_status = pp_status;
    }

    public String getPp_status() {
        return pp_status;
    }

    public String getPp_plotId() {
        return pp_plotId;
    }

    public String getP_adviceEng() {
        return p_adviceEng;
    }

    public String getPp_remark() {
        return pp_remark;
    }

    public String getPp_adviceCode() {
        return pp_adviceCode;
    }

    public String getPp_AdviceDate() {
        return pp_AdviceDate;
    }

    public String getP_adviceHn() {
        return p_adviceHn;
    }

    public int getPp_updStatus() {
        return pp_updStatus;
    }

    public String getPp_upddate() {
        return pp_upddate;
    }
}
