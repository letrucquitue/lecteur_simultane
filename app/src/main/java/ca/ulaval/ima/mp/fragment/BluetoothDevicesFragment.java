package ca.ulaval.ima.mp.fragment;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import ca.ulaval.ima.mp.R;
import ca.ulaval.ima.mp.adapter.MyBluetoothDevicesRecyclerViewAdapter;
import ca.ulaval.ima.mp.model.BluetoothDevices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

/**
 * On trouve ici les appareils hotes. On peut ensuite se connecter à eux.
 */

public class BluetoothDevicesFragment extends android.app.Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private ArrayList<BluetoothDevices> mDeviceList = new ArrayList<BluetoothDevices>();
    private BluetoothAdapter mBluetoothAdapter;
    private BroadcastReceiver mReceiver;
    private Button mButtonUpdate;
    private Button mButtonDecouvrable;
    private MyBluetoothDevicesRecyclerViewAdapter mAdapter;
    private UUID mUUID =  UUID.fromString("ff85d43d-7f0c-4aa8-a89f-c102ec9993db");

    public BluetoothDevicesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothAdapter.startDiscovery();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bluetoothdevices_list, container, false);
        Context context = view.getContext();

        //Prepare notre liste
        recyclerView = view.findViewById(R.id.list);
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        /*
        recyclerView.setAdapter(new MyBluetoothDevicesRecyclerViewAdapter(mDeviceList, context));
        */
        mAdapter = new MyBluetoothDevicesRecyclerViewAdapter(mDeviceList, getActivity().getApplicationContext());
        recyclerView.setAdapter(mAdapter);
        Toast.makeText(getActivity().getApplicationContext(), "Recherche des appareils en cours...", Toast.LENGTH_LONG).show();

        //On trouve les appareils déja pairés
        mDeviceList.clear();
        lookForPairedDevices();

        //On trouve les appareils non pairés
        mReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                //Finding devices
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    mDeviceList.add(new BluetoothDevices(device.getName(), device.getAddress()));
                }
                mAdapter.notifyDataSetChanged();
            }
        };

        //On recherche à nouveau des appareils
        mButtonUpdate = view.findViewById(R.id.btnUpdate);
        mButtonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Log.d("Update", "Update de la liste des appareils");
                    mDeviceList.clear();
                    Toast.makeText(getActivity().getApplicationContext(), "Mise a jour de la liste, veuillez patienter", Toast.LENGTH_LONG).show();
                    lookforNonPairedDevices();
                    lookForPairedDevices();
                }
                catch(Exception e){
                    Log.d("Probleme update", "update a echouer");
                }
            }
        });

        //Se montre découvrable(Probablement inutile pour les invités, on supprimera au besoin
        mButtonDecouvrable = view.findViewById(R.id.btnDecouvert);
        mButtonDecouvrable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Intent discoverableIntent =
                            new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                    discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
                    startActivity(discoverableIntent);
                }
                catch(Exception e){
                    Toast.makeText(getActivity().getApplicationContext(), "Impossible de vous rendre decouvrable", Toast.LENGTH_LONG).show();
                    Log.d("Erreur", "Impossible detre decouvrable");
                }
            }
        });
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        getActivity().registerReceiver(mReceiver, filter);
        return view;
    }



    //Cherche les appareils pairés
    private void lookForPairedDevices(){
        Log.d("Update", "Update de la liste des appareils connectes");
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        for (BluetoothDevice device : pairedDevices) {
            String deviceName = device.getName();
            String deviceHardwareAddress = device.getAddress(); // MAC address
            Log.d(deviceName, deviceHardwareAddress);
            mDeviceList.add(new BluetoothDevices(deviceName,deviceHardwareAddress));
            mAdapter.notifyDataSetChanged();
        }
    }

    //Cherche les appareils non-pairés
    private void lookforNonPairedDevices(){
        Log.d("Update", "Update de la liste des appareils non connectes");
        mBluetoothAdapter.startDiscovery();
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        getActivity().registerReceiver(mReceiver, filter);
    }

    @Override
    public void onAttach (Context context){
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }
    @Override
    public void onDetach () {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(BluetoothDevices.BluetoothItem item);
    }

    @Override
    public void onDestroy () {
        super.onDestroy();
        try{
            if (mBluetoothAdapter.isDiscovering())
                getActivity().unregisterReceiver(mReceiver);
        }
        catch(Exception e){
            //Simplement pour eviter de faire planter si on est pas enregistre
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mDeviceList.clear();
        try{
            if (mBluetoothAdapter.isDiscovering())
                getActivity().unregisterReceiver(mReceiver);
        }
        catch(Exception e){
            //Simplement pour eviter de faire planter si on est pas enregistre
        }

    }
    /*
    Crée un thread pour créer des connections a des sockets avec les hotes
     */

    public class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            BluetoothSocket tmp = null;
            mmDevice = device;

            try {
                tmp = device.createRfcommSocketToServiceRecord(mUUID);
            } catch (IOException e) {
                Log.e("Erreur client", "Socket's create() method failed", e);
            }
            mmSocket = tmp;
        }

        public void run() {
            mBluetoothAdapter.cancelDiscovery();

            try {
                mmSocket.connect();
            } catch (IOException connectException) {
                try {
                    mmSocket.close();
                } catch (IOException closeException) {
                    Log.e("Erreur client", "Could not close the client socket", closeException);
                }
                return;
            }

        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e("Erreur client", "Could not close the client socket", e);
            }
        }
    }

}

