package ca.ulaval.ima.mp.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by LEOBL on 24/04/2018.
 */

public class FragmentAdapter extends FragmentPagerAdapter {

    private final ArrayList<Fragment> fragments;

    //On fournit à l'adapter la liste des fragments à afficher
    public FragmentAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return this.fragments.get(position);
    }

    @Override
    public int getCount() {
        return this.fragments.size();
    }
}