package edu.ualr.recyclerviewasignment.model;

/**
 * Created by kyeza on 2019-10-04.
 */
public class DeviceSection extends DeviceListItem {

    private String label = "";

    public DeviceSection(Device.DeviceStatus status) {
        this.init(status);
    }

    private void init(Device.DeviceStatus status) {
        super.isSection = true;
        this.deviceStatus = status;
        this.label = status.toString();
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

}
