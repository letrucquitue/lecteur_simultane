package ca.ulaval.ima.mp.fragment;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
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

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class BluetoothDevicesFragment extends android.app.Fragment {

    private Activity activity;
    private Context context;
    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private ArrayList<BluetoothDevices> mDeviceList = new ArrayList<BluetoothDevices>();
    private BluetoothAdapter mBluetoothAdapter;
    private BroadcastReceiver mReceiver;
    private MyBluetoothDevicesRecyclerViewAdapter mAdapter;

    private Button mButtonUpdate;
    private Button mButtonDecouvrable;


    public BluetoothDevicesFragment() {
    }

    public static BluetoothDevicesFragment newInstance(int columnCount) {
        BluetoothDevicesFragment fragment = new BluetoothDevicesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
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
        recyclerView = view.findViewById(R.id.list);
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        recyclerView.setAdapter(new MyBluetoothDevicesRecyclerViewAdapter(mDeviceList, context));
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        mDeviceList.clear();
        Toast.makeText(getActivity().getApplicationContext(), "Recherche des appareils en cours...", Toast.LENGTH_LONG).show();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                Log.d(deviceName, deviceHardwareAddress);
                mDeviceList.add(new BluetoothDevices(deviceName,deviceHardwareAddress));
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
                MyBluetoothDevicesRecyclerViewAdapter adapter = new MyBluetoothDevicesRecyclerViewAdapter( mDeviceList, getActivity().getApplicationContext());
                recyclerView.setAdapter(adapter);
                Log.d("Nombre déja pairée", Integer.toString(mDeviceList.size()));
            }
        }
        mReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                //Finding devices
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    mDeviceList.add(new BluetoothDevices(device.getName(), device.getAddress()));

                }
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                mAdapter = new MyBluetoothDevicesRecyclerViewAdapter(mDeviceList, context);
                recyclerView.setAdapter(mAdapter);
                Log.d("Nombre de connections", Integer.toString(mDeviceList.size()));

            }
        };
        mButtonUpdate = view.findViewById(R.id.btnUpdate);
        mButtonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Log.d("Update", "Update de la liste des appareils");
                    mDeviceList.clear();
                    Toast.makeText(getActivity().getApplicationContext(), "Mise a jour de la liste, veuillez patienter", Toast.LENGTH_LONG).show();
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

        Log.d("Discovery", "lol");
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        getActivity().registerReceiver(mReceiver, filter);
        Log.d("Nombre", "Entre23sdsd3!");
        return view;
    }


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


}

