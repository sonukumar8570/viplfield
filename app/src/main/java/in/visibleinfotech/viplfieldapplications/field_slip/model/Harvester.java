package in.visibleinfotech.viplfieldapplications.field_slip.model;

public class Harvester {
    private String harvesterId, harvesterName;

    public Harvester(String harvesterId, String harvesterName) {
        this.harvesterId = harvesterId;
        this.harvesterName = harvesterName;
    }

    public String getHarvesterId() {
        return harvesterId;
    }

    public String getHarvesterName() {
        return harvesterName;
    }
}
