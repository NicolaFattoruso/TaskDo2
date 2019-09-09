package com.example.android.taskdo;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {

    /** Context of the app */
    private Context mContext;

    public SimpleFragmentPagerAdapter(Context context, FragmentManager fragmentManager) {
        super(fragmentManager);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0: return new MondayFragment();
            case 1: return new TuesdayFragment();
            case 2: return new WednesdayFragment();
            case 3: return new ThursdayFragment();
            case 4: return new FridayFragment();
            case 5: return new SaturdayFragment();
            case 6: return new SundayFragment();


            default: return null;
        }
    }

    @Override
    public int getCount() {
        return 7;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position)
        {
            case 0: return "LUN";
            case 1: return "MAR";
            case 2: return "MER";
            case 3: return "GIO";
            case 4: return "VEN";
            case 5: return "SAB";
            case 6: return "DOM";
            default: return "DEF_PAGE_TITLE";
        }
    }
}
