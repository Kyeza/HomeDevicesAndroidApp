package edu.ualr.recyclerviewasignment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;

import java.util.zip.DataFormatException;

import edu.ualr.recyclerviewasignment.adapter.DeviceListAdapter;
import edu.ualr.recyclerviewasignment.data.DataGenerator;
import edu.ualr.recyclerviewasignment.data.DeviceDataFormatTools;
import edu.ualr.recyclerviewasignment.model.Device;

public class MainActivity extends AppCompatActivity implements DeviceListAdapter.ListItemClickListener {

    private RecyclerView mRecyclerView;
    private DeviceListAdapter mAdapter;
    private Toast mToast;

    private BottomSheetDialog bottomSheetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initRecyclerView();
    }

    private void initRecyclerView(){
        mAdapter = new DeviceListAdapter(this, this);
        mRecyclerView = findViewById(R.id.devices_recycler_view);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.addAll(DataGenerator.getDevicesDataset(5));

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    private void createDialog(final int position, final Device device) {
        if (bottomSheetDialog == null) {
            View view = LayoutInflater.from(this).inflate(R.layout.device_detail_fragment, null);
            TextView deviceStatus = view.findViewById(R.id.detail_device_status);
            RelativeLayout image = view.findViewById(R.id.detail_thumbnail_image);
            ImageView statusMark = view.findViewById(R.id.detail_status_mark);
            final EditText deviceName = view.findViewById(R.id.detail_device_name_edittext);
            TextView lastConnection = view.findViewById(R.id.last_time_connection_textview);
            Spinner deviceTypeSpinner = view.findViewById(R.id.device_type_spinner);

            ArrayAdapter<Device.DeviceType> adapter = new ArrayAdapter<>(this,
                    R.layout.spinner_list_item, Device.DeviceType.values());
            adapter.setDropDownViewResource(R.layout.spinner_selector_item);
            deviceTypeSpinner.setAdapter(adapter);
            deviceTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Device.DeviceType type = (Device.DeviceType) parent.getItemAtPosition(position);
                    device.setDeviceType(type);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            Device.DeviceType compareValue = device.getDeviceType();

            if (compareValue != null) {
                int spinnerPosition = adapter.getPosition(compareValue);
                deviceTypeSpinner.setSelection(spinnerPosition);
            }

            MaterialButton saveBtn = view.findViewById(R.id.save_btn);

            lastConnection.setText(DeviceDataFormatTools.getTimeSinceLastConnection(this, device));
            deviceName.setHint(device.getName());
            DeviceDataFormatTools.setDeviceThumbnail(this, image, device);
            DeviceDataFormatTools.setDeviceStatusMark(this, statusMark, device);
            deviceStatus.setText(device.getDeviceStatus().toString());

            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    device.setName(deviceName.getText().toString());
                    mAdapter.updateDevice(position, device);
                    dismissDialog();
                }
            });

            bottomSheetDialog = new BottomSheetDialog(this);
            bottomSheetDialog.setContentView(view);
            bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    bottomSheetDialog = null;
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return  true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.connect_all:
                mAdapter.connectAllDevices();
                return true;

            case R.id.disconnect_all:
                mAdapter.disconnectAllDevices();
                return true;

            case R.id.show_linked:
                if (item.isChecked()) {
                    item.setChecked(false);
                    mAdapter.hideLinked();
                } else {
                    item.setChecked(true);
                    mAdapter.showLinked();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void showDialog() {
        if (bottomSheetDialog != null) {
            bottomSheetDialog.show();
        }
    }

    public void dismissDialog() {
        if (bottomSheetDialog != null) {
            bottomSheetDialog.dismiss();
            bottomSheetDialog = null;
        }
    }

    @Override
    public void onListItemClick(int position, Device device) {
        createDialog(position, device);
        showDialog();
    }
}
