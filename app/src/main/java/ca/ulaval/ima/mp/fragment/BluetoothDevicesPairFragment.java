package ca.ulaval.ima.mp.fragment;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.ulaval.ima.mp.R;
import ca.ulaval.ima.mp.adapter.MyBluetoothDevicesRecyclerViewAdapter;
import ca.ulaval.ima.mp.model.BluetoothDevices;

import java.util.ArrayList;
import java.util.Set;

public class BluetoothDevicesPairFragment extends android.app.Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private BluetoothDevicesFragment.OnListFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private ArrayList<BluetoothDevices> mDeviceList = new ArrayList<BluetoothDevices>();
    private BluetoothAdapter mBluetoothAdapter;
    private BroadcastReceiver mReceiver;

    public BluetoothDevicesPairFragment() {
    }

    public static BluetoothDevicesPairFragment newInstance(int columnCount) {
        BluetoothDevicesPairFragment fragment = new BluetoothDevicesPairFragment();
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
        Log.d("Nombre", "Entre23sdsd3!");
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
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
        return view;
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
        // TODO: Update argument type and name
        void onListFragmentInteraction(BluetoothDevices.BluetoothItem item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}