package edu.ualr.recyclerviewasignment.model;

/**
 * Created by kyeza on 2019-10-04.
 */
public abstract class DeviceListItem {
    protected boolean isSection = false;

    public Device.DeviceStatus getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(Device.DeviceStatus deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    protected Device.DeviceStatus deviceStatus = Device.DeviceStatus.Linked;
    public boolean isSection() {
        return this.isSection;
    }
}
