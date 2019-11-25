package edu.ualr.recyclerviewasignment.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import edu.ualr.recyclerviewasignment.model.Device;
import edu.ualr.recyclerviewasignment.model.DeviceListItem;
import edu.ualr.recyclerviewasignment.model.DeviceSection;

/**
 * Created by kyeza on 2019-10-04.
 */
public class DataGenerator {

    private static Random r = new Random();

    public static int randInt(int max) {
        int min = 0;
        return r.nextInt((max - min) + 1) + min;
    }

    public static List<DeviceListItem> getDevicesDataset(int datasetSize) {
        Device device;
        DeviceSection section;
        String id;
        Device.DeviceStatus status;
        Device.DeviceType type;
        List<DeviceListItem> items = new ArrayList<>();
        for (int i = 0; i < datasetSize; i++) {
            type = Device.DeviceType.values()[randInt(6)];
            id = type.toString() + "-" + String.valueOf(i);
            status = Device.DeviceStatus.values()[randInt(2)];
            device = new Device(id);
            device.setDeviceStatus(status);
            device.setName(id);
            device.setDeviceType(type);
            items.add(device);
        }

        for (Device.DeviceStatus value : Device.DeviceStatus.values()) {
            status = value;
            section = new DeviceSection(status);
            items.add(section);
        }

        Collections.shuffle(items);
        return items;
    }
}
