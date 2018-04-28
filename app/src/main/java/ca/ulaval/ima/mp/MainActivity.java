package ca.ulaval.ima.mp;

import android.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import ca.ulaval.ima.mp.fragment.BluetoothDevicesFragment.OnListFragmentInteractionListener;
import ca.ulaval.ima.mp.fragment.ChooseVideoFragment;
import ca.ulaval.ima.mp.fragment.ConnectionFragment;
import ca.ulaval.ima.mp.fragment.HoteFragment;
import ca.ulaval.ima.mp.fragment.PlayFragment;
import ca.ulaval.ima.mp.fragment.PropertiesFragment;
import ca.ulaval.ima.mp.model.BluetoothDevices;

public class MainActivity extends AppCompatActivity implements OnListFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //DEFAULT FRAGMENT
        FragmentManager fragmentManager = getFragmentManager() ;
        fragmentManager.beginTransaction()
                .replace(R.id.main_content, new ChooseVideoFragment())
                .commit();
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            FragmentManager fragmentManager = getFragmentManager();

            switch (item.getItemId()) {
                case R.id.navigation_choose:
                    fragmentManager.beginTransaction().replace(R.id.main_content, new ChooseVideoFragment()).commit();
                    return true;
                case R.id.navigation_properties:
                    fragmentManager.beginTransaction().replace(R.id.main_content, new PropertiesFragment()).commit();
                    return true;
                case R.id.navigation_connection:
                    fragmentManager.beginTransaction().replace(R.id.main_content, new ConnectionFragment()).commit();
                    return true;
                case R.id.navigation_play:
                    fragmentManager.beginTransaction().replace(R.id.main_content, new PlayFragment()).commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    public void onListFragmentInteraction(BluetoothDevices.BluetoothItem item) {

    }



    /*@SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fragmentManager = getFragmentManager();

        if (id == R.id.navigation_choose) {
            fragmentManager.beginTransaction().replace(R.id.main_content, new ChooseVideoFragment()).commit();
        } else if (id == R.id.navigation_properties) {
            fragmentManager.beginTransaction().replace(R.id.main_content, new PropertiesFragment()).commit();
        } else if (id == R.id.navigation_connection) {
            fragmentManager.beginTransaction().replace(R.id.main_content, new ConnectionFragment()).commit();
        } else if (id == R.id.navigation_play) {
            fragmentManager.beginTransaction().replace(R.id.main_content, new PlayFragment()).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }*/
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(BluetoothDevices.BluetoothItem item);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
