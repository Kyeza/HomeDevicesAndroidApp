package edu.ualr.recyclerviewasignment.model;

import androidx.annotation.NonNull;

import java.util.Date;

/**
 * Created by kyeza on 2019-10-04.
 */
public class Device extends DeviceListItem {
    private String deviceId;
    private String name;
    private DeviceType deviceType;
    private Date lastConnection;

    public Device(@NonNull String deviceId) {
        this.lastConnection = null;
        this.isSection = false;
        this.deviceId = deviceId;
        this.name = "";
        this.deviceStatus = DeviceStatus.Linked;
        this.deviceType = DeviceType.Unknown;
    }

    public Device(final Device device) {
        isSection = device.isSection;
        deviceId = device.getDeviceId();
        name = device.getName();
        deviceStatus = device.getDeviceStatus();
        deviceType = device.getDeviceType();
        Date timeStamp = device.getLastConnection();
        if (timeStamp == null) {
            lastConnection = null;
        } else {
            lastConnection = (Date) timeStamp.clone();
        }
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    public Date getLastConnection() {
        return this.lastConnection;
    }

    public void setLastConnection(Date timeStamp) {
        this.lastConnection = timeStamp;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof Device &&
                deviceId.equals(((Device) obj).getDeviceId());
    }

    public enum DeviceType {
        Unknown,
        Desktop,
        Laptop,
        Tablet,
        Smartphone,
        SmartTV,
        GameConsole
    }

    public enum DeviceStatus {
        Connected,
        Ready,
        Linked
    }
}
