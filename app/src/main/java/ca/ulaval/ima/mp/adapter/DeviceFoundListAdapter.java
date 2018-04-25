package ca.ulaval.ima.mp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ca.ulaval.ima.mp.R;
import ca.ulaval.ima.mp.listener.OnItemClickListener;
import ca.ulaval.ima.mp.model.VideoModel;

/**
 * Created by LEOBL on 16/04/2018.
 */

public class DeviceFoundListAdapter extends RecyclerView.Adapter<DeviceFoundListAdapter.DeviceFoundPostHolder> {

    private ArrayList<String> dataSet;
    private Context mContext = null;
    private final OnItemClickListener listener;


    public DeviceFoundListAdapter(Context mContext, ArrayList<String> dataSet, OnItemClickListener listener) {
        this.dataSet = dataSet;
        this.mContext = mContext;
        this.listener = listener;

    }

    @Override
    public DeviceFoundPostHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.a_device_found_layout,parent,false);
        DeviceFoundPostHolder postHolder = new DeviceFoundPostHolder(view);
        return postHolder;
    }

    @Override
    public void onBindViewHolder(DeviceFoundPostHolder holder, int position) {

        //set the views here
        TextView deviceFoundName = holder.deviceFoundName;

        String dev_name = dataSet.get(position);

        deviceFoundName.setText(dev_name);
        holder.bind(dataSet.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public static class DeviceFoundPostHolder extends RecyclerView.ViewHolder {
        TextView deviceFoundName;

        public DeviceFoundPostHolder(View itemView) {
            super(itemView);
            this.deviceFoundName = (TextView) itemView.findViewById(R.id.deviceFoundName);
        }

        public void bind(final String item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}
