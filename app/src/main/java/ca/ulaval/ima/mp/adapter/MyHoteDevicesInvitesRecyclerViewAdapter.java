package ca.ulaval.ima.mp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import ca.ulaval.ima.mp.R;
import ca.ulaval.ima.mp.model.BluetoothDevices;
import ca.ulaval.ima.mp.model.SelfUser;

import java.util.List;


public class MyHoteDevicesInvitesRecyclerViewAdapter extends RecyclerView.Adapter<MyHoteDevicesInvitesRecyclerViewAdapter.ViewHolder> {
    private List<BluetoothDevices> mDevices;
    private Context mContext;

    public  MyHoteDevicesInvitesRecyclerViewAdapter(List<BluetoothDevices> devices, Context context){
        mDevices = devices;
        mContext = context;
    }
    private Context getContext() {
        return mContext;
    }

    @Override
    public MyHoteDevicesInvitesRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.fragment_hotedevicesinvites, parent, false);
        MyHoteDevicesInvitesRecyclerViewAdapter.ViewHolder viewHolder = new MyHoteDevicesInvitesRecyclerViewAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyHoteDevicesInvitesRecyclerViewAdapter.ViewHolder viewHolder, int position) {
        final BluetoothDevices device = mDevices.get(position);
        viewHolder.nameTextView.setText(device.getNom());
        viewHolder.connectionToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToggleButton button = (ToggleButton) view;
                if (button.isChecked()){
                    try{
                        SelfUser.addConnectedDevices(device);
                        Log.d("Update devices list", "Ajout du device :" + device.getNom());
                        SelfUser.print();
                    }
                    catch(Exception e){
                        Log.d("Erreur mConnectedList",e.toString() );
                    }

                }
                else{
                    try{
                        SelfUser.removeConnectedDevices(device);
                        Log.d("Update devices list", "Rejet du device :" + device.getNom());
                        SelfUser.print();
                    }
                    catch(Exception e){
                        Log.d("Erreur mConnectedList",e.toString() );
                    }
                    Log.d("Update devices list", "Rejet du device :" + device.getNom());
                    SelfUser.removeConnectedDevices(device);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDevices.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public ToggleButton connectionToggle;
        public ViewHolder(View view) {

            super(view);
            nameTextView = (TextView) itemView.findViewById(R.id.nameTextView);
            connectionToggle = (ToggleButton) itemView.findViewById(R.id.toggleAcceptConnection);
        }

    }
}
