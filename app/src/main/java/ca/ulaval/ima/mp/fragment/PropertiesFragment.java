package ca.ulaval.ima.mp.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import ca.ulaval.ima.mp.R;

/**
 * Created by LEOBL on 16/04/2018.
 */

public class PropertiesFragment extends Fragment{
    private EditText video_id_field;
    private SeekBar countdown_bar,timeline_bar;

    public PropertiesFragment() {
        super();
        //empty arguments
        setArguments(new Bundle());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_properties, container, false);

        //VIDEO ID FROM PREFERENCES
        video_id_field = (EditText) view.findViewById(R.id.video_id_field);
        Context context = getActivity().getApplicationContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String video_id = prefs.getString("video_id", "");
        video_id_field.setText(video_id);

        Log.e("Properties video_id : ",video_id);

        //COUNTDOWN BAR
        countdown_bar = (SeekBar) view.findViewById(R.id.countdown_bar);
        countdown_bar.setProgress(0);
        countdown_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChanged = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                progressChanged = progress;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(getActivity(),"Minuteur : "+progressChanged,
                        Toast.LENGTH_SHORT).show();
            }
        });

        //TIMELINE BAR
        timeline_bar= (SeekBar) view.findViewById(R.id.timeline_bar);

        return view;
    }

    public void onHiddenChanged(boolean hidden) {
        //onShow
        if(hidden == false){
            video_id_field = (EditText) this.getView().findViewById(R.id.video_id_field);
            Context context = getActivity().getApplicationContext();
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            String video_id = prefs.getString("video_id", "");
            video_id_field.setText(video_id);
        }
    }
}
