package in.visibleinfotech.viplfieldapplications.field_survey.models;

public class SurveyModel {
    String farmerCode, farmerName, plotName, plotID, canevariety, cropType, plotArea, plDate;


    public SurveyModel(String farmerCode, String farmerName, String plotName, String plotID, String canevariety, String cropType, String plotArea, String plDate) {
        this.farmerCode = farmerCode;
        this.farmerName = farmerName;
        this.plotName = plotName;
        this.plotID = plotID;
        this.canevariety = canevariety;
        this.cropType = cropType;
        this.plotArea = plotArea;
        this.plDate = plDate;
    }

    public String getPlDate() {
        return plDate;
    }

    public String getPlotArea() {
        return plotArea;
    }

    public String getFarmerCode() {
        return farmerCode;
    }

    public String getFarmerName() {
        return farmerName;
    }

    public String getPlotName() {
        return plotName;
    }

    public String getPlotID() {
        return plotID;
    }

    public String getCanevariety() {
        return canevariety;
    }

    public String getCropType() {
        return cropType;
    }
}
