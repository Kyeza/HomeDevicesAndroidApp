package edu.ualr.recyclerviewasignment.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedList;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.ualr.recyclerviewasignment.R;
import edu.ualr.recyclerviewasignment.data.DeviceDataFormatTools;
import edu.ualr.recyclerviewasignment.model.Device;
import edu.ualr.recyclerviewasignment.model.DeviceListItem;
import edu.ualr.recyclerviewasignment.model.DeviceSection;

/**
 * Created by kyeza on 2019-10-04.
 */
public class DeviceListAdapter extends RecyclerView.Adapter {

    private static final int DEVICE_VIEW = 0;
    private static final int SECTION_VIEW = 1;

    private SortedList<DeviceListItem> mItems;
    private Context mContext;
    private List<Device> linkedDevices;

    final private ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(int position, Device device);
    }

    public DeviceListAdapter(Context context, ListItemClickListener listener) {
        this.mOnClickListener = listener;
        this.linkedDevices = new ArrayList<>();
        this.mContext = context;
        this.mItems = new SortedList<>(DeviceListItem.class, new SortedList.Callback<DeviceListItem>() {
            @Override
            public int compare(DeviceListItem o1, DeviceListItem o2) {

                if (o1.isSection() && !o2.isSection()) {
                    if (o1.getDeviceStatus().ordinal() <= o2.getDeviceStatus().ordinal()) {
                        return -1;
                    } else {
                        return 1;
                    }
                } else if (!o1.isSection() && o2.isSection()) {
                    if (o1.getDeviceStatus().ordinal() < o2.getDeviceStatus().ordinal()) {
                        return -1;
                    } else {
                        return 1;
                    }
                } else if ((!o1.isSection() && !o2.isSection()) || (o1.isSection() && o2.isSection())) {
                    return o1.getDeviceStatus().ordinal() - o2.getDeviceStatus().ordinal();
                } else return 0;
            }

            @Override
            public void onChanged(int position, int count) {
                notifyDataSetChanged();

            }

            @Override
            public boolean areContentsTheSame(DeviceListItem oldItem, DeviceListItem newItem) {
                return false;
            }

            @Override
            public boolean areItemsTheSame(DeviceListItem item1, DeviceListItem item2) {
                return false;
            }

            @Override
            public void onInserted(int position, int count) {
                notifyItemRangeInserted(position, count);
            }

            @Override
            public void onRemoved(int position, int count) {
                notifyItemRangeRemoved(position, count);
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {
                notifyItemMoved(fromPosition, toPosition);
            }
        });
    }

    public void addAll(List<DeviceListItem> devices) {
        mItems.beginBatchedUpdates();
        for (int i = 0; i < devices.size(); i++) {
            mItems.add(devices.get(i));
        }
        mItems.endBatchedUpdates();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == DEVICE_VIEW) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.device_list_item, parent, false);
            vh = new DeviceViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.section_header, parent, false);
            vh = new SectionViewHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DeviceListItem item = mItems.get(position);

        if (holder instanceof DeviceViewHolder) {
            DeviceViewHolder view = (DeviceViewHolder) holder;
            Device device = (Device) item;
            view.name.setText(device.getName());
            view.elapsedTimeLabel.setText(DeviceDataFormatTools.getTimeSinceLastConnection(mContext, device));
            DeviceDataFormatTools.setDeviceStatusMark(mContext, view.statusMark, device);
            DeviceDataFormatTools.setDeviceThumbnail(mContext, view.image, device);
            DeviceDataFormatTools.setConnectionBtnLook(mContext, view.connectBtn, device.getDeviceStatus());
        } else {
            SectionViewHolder view = (SectionViewHolder) holder;
            DeviceSection section = (DeviceSection) item;
            view.title_section_label.setText(section.getLabel());
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return this.mItems.get(position).isSection() ? SECTION_VIEW : DEVICE_VIEW;
    }

    private class SectionViewHolder extends RecyclerView.ViewHolder {
        public TextView title_section_label;

        public SectionViewHolder(View v) {
            super(v);
            title_section_label = v.findViewById(R.id.title_section_label);
        }
    }

    public class DeviceViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout image;
        private ImageView statusMark;
        private TextView name;
        private TextView elapsedTimeLabel;
        private ImageButton connectBtn;

        public DeviceViewHolder(View v) {
            super(v);
            image = v.findViewById(R.id.image);
            statusMark = v.findViewById(R.id.status_mark);
            name = v.findViewById(R.id.name);
            elapsedTimeLabel = v.findViewById(R.id.elapsed_time);
            connectBtn = v.findViewById(R.id.device_connect_btn);
            connectBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleConnection();
                }
            });
            RelativeLayout deviceLayout = v.findViewById(R.id.device_item_container);
            deviceLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewDetail();
                }
            });
        }

        private void viewDetail() {
            Device device = (Device) mItems.get(getAdapterPosition());
            int positon = getAdapterPosition();
            mOnClickListener.onListItemClick(positon, device);
        }

        private void toggleConnection() {
            Device device = (Device) mItems.get(getAdapterPosition());
            Device.DeviceStatus deviceStatus = device.getDeviceStatus();
            if (deviceStatus == Device.DeviceStatus.Connected) {
                device.setDeviceStatus(Device.DeviceStatus.Ready);
                device.setLastConnection(new Date());
            } else if (deviceStatus == Device.DeviceStatus.Ready) {
                device.setDeviceStatus(Device.DeviceStatus.Connected);
            }
            mItems.updateItemAt(getAdapterPosition(), device);
        }
    }

    public void updateDevice(int postion, Device device) {
        mItems.updateItemAt(postion, device);
    }

    public void connectAllDevices() {

        mItems.beginBatchedUpdates();
        for (int i = 0; i < mItems.size(); i++) {

            DeviceListItem deviceListItem = mItems.get(i);

            if (deviceListItem instanceof Device) {

                Device device = (Device) deviceListItem;

                if (device.getDeviceStatus() == Device.DeviceStatus.Ready) {
                    device.setDeviceStatus(Device.DeviceStatus.Connected);
                }

                mItems.updateItemAt(i, device);
            }
        }
        mItems.endBatchedUpdates();
    }


    public void disconnectAllDevices() {
        List<DeviceListItem> temp = new ArrayList<>();

        for (int i = 0; i < mItems.size(); i++) {
            temp.add(mItems.get(i));
        }

        mItems.clear();


        mItems.beginBatchedUpdates();
        for (int i = 0; i < temp.size(); i++) {

            DeviceListItem deviceListItem = temp.get(i);

            if (deviceListItem instanceof Device) {

                Device device = (Device) deviceListItem;

                if (device.getDeviceStatus() == Device.DeviceStatus.Connected) {
                    device.setDeviceStatus(Device.DeviceStatus.Ready);
                    device.setLastConnection(new Date());
                }
                temp.set(i, device);
            }

            mItems.add(temp.get(i));
        }

        mItems.endBatchedUpdates();

    }

    public void hideLinked() {


        mItems.beginBatchedUpdates();
        for (int i = 0; i < mItems.size(); i++) {

            DeviceListItem deviceListItem = mItems.get(i);

            if (deviceListItem instanceof Device) {

                Device device = (Device) deviceListItem;

                if (device.getDeviceStatus() == Device.DeviceStatus.Linked) {

                    mItems.removeItemAt(i);
                    linkedDevices.add(device);
                }
            }
        }

        mItems.endBatchedUpdates();

    }

    public void showLinked() {

        if (!linkedDevices.isEmpty()) {
            mItems.beginBatchedUpdates();
            for (int i = 0; i < linkedDevices.size(); i++) {
                mItems.add(linkedDevices.get(i));
            }
            mItems.endBatchedUpdates();
            linkedDevices.clear();
        }

    }
}
