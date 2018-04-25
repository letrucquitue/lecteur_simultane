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
import android.widget.AdapterView;
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

public class ConnectionFragment extends Fragment{
    private Button become_host_btn,join_host_btn;

    //DEVICES REQUEST

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        //Si on est ni host ni client TODO
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            view = inflater.inflate(R.layout.fragment_connection_host, container, false);
        }
        else {
            view = inflater.inflate(R.layout.fragment_connection_main, container, false);
            become_host_btn = (Button) view.findViewById(R.id.become_host_btn);
            join_host_btn = (Button) view.findViewById(R.id.join_host_btn);
            become_host_btn.setOnClickListener(new BecomeHost());
            join_host_btn.setOnClickListener(new JoinHost());
        }

        return view;
    }

    private class BecomeHost implements View.OnClickListener{
        public void onClick(View v){
            //launch Socket BT
            Fragment fragment = new ConnectionFragment();
            Bundle bundle = new Bundle();
            bundle.putString("mode", "host");
            fragment.setArguments(bundle);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.main_content, fragment).commit();
        }
    }

    private class JoinHost implements View.OnClickListener{
        public void onClick(View v){
            //join
            Fragment fragment = new ClientFragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.main_content, fragment).commit();
        }
    }
}
