package in.visibleinfotech.viplfieldapplications.field_planning.ui.home;

public class Plot {
    String plotId, plotName;

    public Plot(String plotId, String plotName) {
        this.plotId = plotId;
        this.plotName = plotName;
    }

    public String getPlotId() {
        return plotId;
    }

    public String getPlotName() {
        return plotName;
    }
}
