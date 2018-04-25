package ca.ulaval.ima.mp.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Set;

import ca.ulaval.ima.mp.R;
import ca.ulaval.ima.mp.adapter.DeviceFoundListAdapter;
import ca.ulaval.ima.mp.listener.OnItemClickListener;
import ca.ulaval.ima.mp.model.VideoModel;

/**
 * Created by LEOBL on 16/04/2018.
 */

public class ClientFragment extends Fragment{
    //DEVICES FOUND (Client Layout)
    private Set<BluetoothDevice> devices;
    private RecyclerView list_devices_found = null;
    private DeviceFoundListAdapter adapter = null;
    private ArrayList<String> listDevicesName = new ArrayList<>();
    private BroadcastReceiver bluetoothReceiver = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
         //CLIENT
        view = inflater.inflate(R.layout.fragment_connection_client_1, container, false);
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null)
            Log.e("BLUETOOTH EXIST : ","NOPE");
        else {
            Log.e("BLUETOOTH EXIST : ", "YEAH");
            if (!bluetoothAdapter.isEnabled()) {
                bluetoothAdapter.enable();
                Log.e("BLUETOOTH ENABLED : ", "YEAH");
            }

            /*devices = bluetoothAdapter.getBondedDevices();
            for (BluetoothDevice blueDevice : devices) {
                listDevicesName.add(blueDevice.getName());
            }*/

            //RECHERCHE D'APPAREILS AYANT ACTIVE LE BT
            bluetoothReceiver = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                        listDevicesName.add(device.getName());
                    }
                }
            };

            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            getActivity().registerReceiver(bluetoothReceiver, filter);

            bluetoothAdapter.startDiscovery();

            list_devices_found = (RecyclerView) view.findViewById(R.id.list_devices_found);
            initList(listDevicesName);
        }
        return view;
    }

    //Initialisation de la liste de
    private void initList(ArrayList<String> mListData) {
        list_devices_found.setLayoutManager(new LinearLayoutManager(getActivity()));
        //Adapter qui va gerer le clic sur un element
        adapter = new DeviceFoundListAdapter(getActivity(), mListData, new OnItemClickListener() {
            @Override
            public void onItemClick(VideoModel item) {
                // A REVOIR
            }

            @Override
            public void onItemClick(String item) {
                //établir lien avec l'hôte
                Log.e("CLICK : ",item);
            }
        });
        list_devices_found.setAdapter(adapter);

    }

    public void onDestroy() {
        super.onDestroy();
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter != null) {
            bluetoothAdapter.cancelDiscovery();
            getActivity().unregisterReceiver(bluetoothReceiver);
        }
    }
}
