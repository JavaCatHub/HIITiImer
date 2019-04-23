package com.example.android.hiittimer.main;

import com.example.android.hiittimer.model.Asset;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private String[] tabTitles = {"Work out", "Assets"};
    private int id;

    public ViewPagerAdapter(FragmentManager fm, int id) {
        super(fm);
        this.id = id;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return WorkoutFragment.newInstance(id);
            case 1:
                return new AssetsFragment();
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }
}
