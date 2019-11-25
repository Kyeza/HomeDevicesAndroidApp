package edu.ualr.recyclerviewasignment;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.ualr.recyclerviewasignment.adapter.DeviceListAdapter;
import edu.ualr.recyclerviewasignment.data.DataGenerator;
import edu.ualr.recyclerviewasignment.model.DeviceListItem;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private DeviceListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initRecyclerView();
    }

    private void initRecyclerView(){
        mAdapter = new DeviceListAdapter(this);
        mRecyclerView = findViewById(R.id.devices_recycler_view);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.addAll(DataGenerator.getDevicesDataset(5));

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
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
                break;

            case R.id.disconnect_all:
                mAdapter.disconnectAllDevices();
            case R.id.show_linked:


        }

        return true;
    }
}
