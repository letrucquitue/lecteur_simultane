package ca.ulaval.ima.mp.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import ca.ulaval.ima.mp.model.SelfUser;

/**
 * Created by LEOBL on 16/04/2018.
 */

//Fragment de l'onglet Connexion

public class ConnectionFragment extends Fragment{
    private Button btnInviteNotPair;
    private Button btnHote;
    private Context mContext;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Activer le bluetooth
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter.isEnabled() == false) {
            mBluetoothAdapter.enable();
        }

        //Rendre l'appareil visible
        try{
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
        catch(Exception e){
            Toast.makeText(getActivity().getApplicationContext(), "Impossible de vous rendre decouvrable", Toast.LENGTH_LONG).show();
            Log.d("Erreur", "Impossible detre decouvrable");
        }

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_connection, container, false);
        btnInviteNotPair = view.findViewById(R.id.buttonInviteNotPair);
        btnHote = view.findViewById(R.id.buttonHote);
        mContext = view.getContext();
        btnInviteNotPair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BluetoothDevicesFragment BluetoothFragment = new BluetoothDevicesFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_content,BluetoothFragment);
                fragmentTransaction.commit();

                SelfUser.setIsHost(false);
        }
        });


        btnHote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HoteFragment BluetoothFragment = new HoteFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_content,BluetoothFragment);
                fragmentTransaction.commit();

                //On se set en tant de Hote
                SelfUser.setIsHost(true);

                //TODO : Associer le video choisi a la classe SelfUser
            }
        });

        return view;
    }
}
