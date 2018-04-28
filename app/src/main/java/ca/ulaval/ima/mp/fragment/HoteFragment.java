package ca.ulaval.ima.mp.fragment;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

import ca.ulaval.ima.mp.R;
import ca.ulaval.ima.mp.adapter.MyBluetoothDevicesRecyclerViewAdapter;
import ca.ulaval.ima.mp.adapter.MyHoteDevicesInvitesRecyclerViewAdapter;
import ca.ulaval.ima.mp.model.BluetoothDevices;


public class HoteFragment extends android.app.Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private BluetoothDevicesFragment.OnListFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private ArrayList<BluetoothDevices> mDeviceList = new ArrayList<BluetoothDevices>();
    private BluetoothAdapter mBluetoothAdapter;
    private BroadcastReceiver mReceiver;
    private MyHoteDevicesInvitesRecyclerViewAdapter mAdapter;

    private Button mButtonUpdate;
    private Button mButtonDecouvert;

    public HoteFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hote, container, false);
        Context context = view.getContext();
        recyclerView = view.findViewById(R.id.list);
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        recyclerView.setAdapter(new MyBluetoothDevicesRecyclerViewAdapter(mDeviceList, context));


        //On tente de trouver les appareils déja pairés
        Toast.makeText(context, "Recherche des appareils, veuillez patienter", Toast.LENGTH_LONG).show();


        //On cherche les appareils déja pairés
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                Log.d(deviceName, deviceHardwareAddress);
                mDeviceList.add(new BluetoothDevices(deviceName,deviceHardwareAddress));
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
                MyHoteDevicesInvitesRecyclerViewAdapter adapter = new MyHoteDevicesInvitesRecyclerViewAdapter( mDeviceList, getActivity().getApplicationContext());
                recyclerView.setAdapter(adapter);
                Log.d("Nombre déja pairée", Integer.toString(mDeviceList.size()));
            }
        }

        //On tente de trouver les appareils pas pairés
        mBluetoothAdapter.startDiscovery();
        mReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    mDeviceList.add(new BluetoothDevices(device.getName(), device.getAddress()));
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
                mAdapter = new MyHoteDevicesInvitesRecyclerViewAdapter(mDeviceList, getActivity().getApplicationContext());
                recyclerView.setAdapter(mAdapter);
                Log.d("Nombre de connections", Integer.toString(mDeviceList.size()));
                mBluetoothAdapter.cancelDiscovery();

            }
        };
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        getActivity().registerReceiver(mReceiver, filter);


        mButtonUpdate = view.findViewById(R.id.btnUpdate);
        mButtonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Log.d("Update", "Update de la liste des appareils");
                    mDeviceList.clear();
                    Toast.makeText(getActivity().getApplicationContext(), "Mise à jour de la liste, veuillez patienter", Toast.LENGTH_LONG).show();
                    Log.d("Nombre appareils", Integer.toString(mDeviceList.size()));
                    lookforNonPairedDevices();
                    lookForPairedDevices();
                    mAdapter.notifyDataSetChanged();
                }
                catch(Exception e){
                    Log.d("Probleme update", "update a echouer");
                    Toast.makeText(getActivity().getApplicationContext(), "Impossible de mettre a jour la liste, veuillez reessayer", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mButtonDecouvert = view.findViewById(R.id.btnDecouvert);
        mButtonDecouvert.setOnClickListener(new View.OnClickListener() {
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
        return view;
    }


    //Lorsque on update les listes
    private void lookForPairedDevices(){
        Log.d("Update", "Update de la liste des appareils connectes");
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
    }

    private void lookforNonPairedDevices(){
        Log.d("Update", "Update de la liste des appareils non connectes");
        mBluetoothAdapter.startDiscovery();
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        getActivity().registerReceiver(mReceiver, filter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BluetoothDevicesFragment.OnListFragmentInteractionListener) {
            mListener = (BluetoothDevicesFragment.OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(BluetoothDevices.BluetoothItem item);
    }

    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(mReceiver);
        super.onDestroy();
    }

}