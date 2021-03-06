package ca.ulaval.ima.mp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.util.ArrayList;

import ca.ulaval.ima.mp.fragment.BluetoothDevicesFragment.OnListFragmentInteractionListener;
import ca.ulaval.ima.mp.fragment.ChooseVideoFragment;
import ca.ulaval.ima.mp.fragment.ConnectionFragment;
import ca.ulaval.ima.mp.fragment.HoteFragment;
import ca.ulaval.ima.mp.fragment.PropertiesFragment;
import ca.ulaval.ima.mp.model.BluetoothDevices;
import ca.ulaval.ima.mp.model.SelfUser;
import ca.ulaval.ima.mp.service.BluetoothService;

public class MainActivity extends AppCompatActivity implements OnListFragmentInteractionListener{

    private BottomNavigationView navigation;
    private Fragment current_fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Init navigation
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //DEFAULT FRAGMENT
        current_fragment = new ChooseVideoFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.main_content, current_fragment)
                .commit();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            FragmentManager fragmentManager = getFragmentManager();
            Fragment new_fragment = null;

            //choix du fragment selon onglet
            switch (item.getItemId()) {
                case R.id.navigation_choose:
                    new_fragment = new ChooseVideoFragment();
                    break;
                case R.id.navigation_properties:
                    new_fragment = new PropertiesFragment();
                    break;
                case R.id.navigation_connection:
                    new_fragment = new ConnectionFragment();
                    break;
                case R.id.navigation_play:
                    Intent myIntent = new Intent(getBaseContext(), PlayVideoActivity.class);
                    startActivity(myIntent);
                    return true;
            }
            if(new_fragment != null && new_fragment.getClass() != current_fragment.getClass()){
                //on remplace le fragment
                current_fragment = new_fragment;
                fragmentManager.beginTransaction().replace(R.id.main_content, new_fragment).commit();
                return true;
            }
            return false;
        }
    };

    @Override
    public void onListFragmentInteraction(BluetoothDevices.BluetoothItem item) {

    }
}
