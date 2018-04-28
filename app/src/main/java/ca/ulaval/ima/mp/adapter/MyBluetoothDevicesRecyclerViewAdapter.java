package ca.ulaval.ima.mp.adapter;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import ca.ulaval.ima.mp.R;
import ca.ulaval.ima.mp.model.BluetoothDevices;
import ca.ulaval.ima.mp.model.SelfUser;

import java.util.List;

public class MyBluetoothDevicesRecyclerViewAdapter extends RecyclerView.Adapter<MyBluetoothDevicesRecyclerViewAdapter.ViewHolder> {

    private List<BluetoothDevices> mDevices;
    private Context mContext;
    private Button btnRejoindre;

    public  MyBluetoothDevicesRecyclerViewAdapter(List<BluetoothDevices> devices, Context context){
        mDevices = devices;
        mContext = context;
    }
    private Context getContext() {
        return mContext;
    }

    @Override
    public MyBluetoothDevicesRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.fragment_bluetoothdevices, parent, false);
        MyBluetoothDevicesRecyclerViewAdapter.ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyBluetoothDevicesRecyclerViewAdapter.ViewHolder viewHolder, int position) {
        final BluetoothDevices device = mDevices.get(position);
        viewHolder.nameTextView.setText(device.getNom());
        viewHolder.btnRejoindre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getContext(), "Tentative de connection avec l hote", Toast.LENGTH_LONG).show();
                SelfUser.setmHost(device);

                //TODO: Lorsque lhote envoie la video, on change de tab
                /*
                FragmentManager manager = ((Activity) getContext()).getFragmentManager();
                manager.beginTransaction().replace(R.id.main_content, new PlayFragment()).commit();
                */
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDevices.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public Button btnRejoindre;
        public ViewHolder(View view) {

            super(view);
            btnRejoindre = (Button) itemView.findViewById(R.id.btnRejoindre);
            nameTextView = (TextView) itemView.findViewById(R.id.id);
        }

    }
}
