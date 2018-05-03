package ca.ulaval.ima.mp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.util.ArrayList;

import ca.ulaval.ima.mp.fragment.BluetoothDevicesFragment.OnListFragmentInteractionListener;
import ca.ulaval.ima.mp.adapter.FragmentAdapter;
import ca.ulaval.ima.mp.fragment.ChooseVideoFragment;
import ca.ulaval.ima.mp.fragment.ConnectionFragment;
import ca.ulaval.ima.mp.fragment.HoteFragment;
import ca.ulaval.ima.mp.fragment.PlayFragment;
import ca.ulaval.ima.mp.fragment.PropertiesFragment;
import ca.ulaval.ima.mp.model.BluetoothDevices;
import ca.ulaval.ima.mp.model.SelfUser;
import ca.ulaval.ima.mp.service.BluetoothService;

public class MainActivity extends AppCompatActivity implements OnListFragmentInteractionListener, BluetoothService.ServiceCallbacks {

    private BottomNavigationView navigation;
    private ArrayList<Fragment> fragments;
    private Fragment current_fragment;
    private Fragment choose_fragment;
    private Fragment properties_fragment;
    private Fragment connection_fragment;
    private Fragment play_fragment;
    private BluetoothService myService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        /*
        fragments = new ArrayList<>();
        fragments.add(new ChooseVideoFragment());
        fragments.add(new PropertiesFragment());
        fragments.add(new ConnectionFragment());
        fragments.add(new PlayFragment());
        */

        //FRAGMENT
        choose_fragment = new ChooseVideoFragment();
        properties_fragment = new PropertiesFragment();
        connection_fragment = new ConnectionFragment();
        play_fragment = new PlayFragment();
        current_fragment = choose_fragment;

        //DEFAULT FRAGMENT
        /*FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.main_content,properties_fragment,"properties_fragment");
        fragmentTransaction.hide(properties_fragment);
        fragmentTransaction.add(R.id.main_content,connection_fragment,"connection_fragment");
        fragmentTransaction.hide(connection_fragment);
        fragmentTransaction.add(R.id.main_content,choose_fragment,"choose_fragment");
        fragmentTransaction.hide(choose_fragment);
        fragmentTransaction.show(current_fragment);
        fragmentTransaction.commit();*/
        //fragmentTransaction.replace(R.id.main_content, current_fragment).commit();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.main_content, current_fragment)
                .commit();

        registerReceiver(broadcastReceiver, new IntentFilter(
                BluetoothService.BROADCAST_ACTION));
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            FragmentManager fragmentManager = getFragmentManager();
            Fragment new_fragment = null;

            switch (item.getItemId()) {
                case R.id.navigation_choose:
                    //new_fragment = choose_fragment;
                    new_fragment = new ChooseVideoFragment();
                    //new_fragment = (ChooseVideoFragment) getFragmentManager().findFragmentByTag("choose_fragment");
                    break;
                case R.id.navigation_properties:
                    //new_fragment = properties_fragment;
                    new_fragment = new PropertiesFragment();
                    //new_fragment = (PropertiesFragment) getFragmentManager().findFragmentByTag("properties_fragment");
                    break;
                case R.id.navigation_connection:
                    //new_fragment = connection_fragment;
                    new_fragment = new ConnectionFragment();
                    //new_fragment = (ConnectionFragment) getFragmentManager().findFragmentByTag("connection_fragment");
                    break;
                case R.id.navigation_play:
                    Intent myIntent = new Intent(getBaseContext(), PlayVideoActivity.class);
                    startActivity(myIntent);
                    //new_fragment = new PlayFragment();
                    return true;
            }
            if(new_fragment != null && new_fragment.getClass() != current_fragment.getClass()){
                //on détruit l'ancien fragment
                //current_fragment.onDestroy();
                //fragmentManager.beginTransaction().hide(current_fragment).commit();
                current_fragment = new_fragment;
                fragmentManager.beginTransaction().replace(R.id.main_content, new_fragment).commit();
                //fragmentManager.beginTransaction().show(new_fragment).commit();
                /*FragmentManager fm = getFragmentManager();
                fm.beginTransaction()
                        .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                        .show(new_fragment)
                        .commit();*/
                return true;
            }
            return false;
        }
    };

    public BottomNavigationView getNavigationBottomView(){
        return this.navigation;
    }

    @Override
    public void onListFragmentInteraction(BluetoothDevices.BluetoothItem item) {

    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    };

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(BluetoothDevices.BluetoothItem item);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
