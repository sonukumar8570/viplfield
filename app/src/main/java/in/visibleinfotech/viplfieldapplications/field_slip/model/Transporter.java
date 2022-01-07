package in.visibleinfotech.viplfieldapplications.field_slip.model;

public class Transporter {
    private String transporterId, transporterName;

    public Transporter(String transporterId, String transporterName) {
        this.transporterId = transporterId;
        this.transporterName = transporterName;
    }

    public String getTransporterId() {
        return transporterId;
    }

    public String getTransporterName() {
        return transporterName;
    }
}
