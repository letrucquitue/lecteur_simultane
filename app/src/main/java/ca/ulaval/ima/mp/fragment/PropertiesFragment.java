package ca.ulaval.ima.mp.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.ulaval.ima.mp.R;

/**
 * Created by LEOBL on 16/04/2018.
 */

public class PropertiesFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_properties, container, false);
        return view;
    }
}
