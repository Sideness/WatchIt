package com.ktung.watchit.ui.shows.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ktung.watchit.ui.shows.CalendarFragment;
import com.ktung.watchit.ui.shows.DiscoverFragment;
import com.ktung.watchit.ui.shows.ProgressFragment;
import com.ktung.watchit.ui.shows.WatchlistFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShowsPagerAdapter extends FragmentPagerAdapter {
    private static final List<String> fragments = new ArrayList<>(Arrays.asList(
        DiscoverFragment.class.getName(),
        ProgressFragment.class.getName(),
        CalendarFragment.class.getName(),
        WatchlistFragment.class.getName()
    ));

    private Context context;

    public ShowsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        return Fragment.instantiate(context, fragments.get(position));
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String classname = fragments.get(position);
        return classname.substring(classname.lastIndexOf('.')+1, classname.lastIndexOf("Fragment"));
    }
}
