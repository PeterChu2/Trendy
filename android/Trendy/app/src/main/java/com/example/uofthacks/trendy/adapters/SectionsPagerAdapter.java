package com.example.uofthacks.trendy.adapters;

/**
 * Created by peter on 22/02/15.
 */

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.example.uofthacks.trendy.R;
import com.example.uofthacks.trendy.ui.SocialListFragment;

import java.util.Locale;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

    private Activity mActivity;
    private String mTwitterString;
    private String mInstagramString;

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    public SectionsPagerAdapter(FragmentManager fm, Activity activity,
                                String twitterString,
                                String instagramString) {
        this(fm);
        mActivity = activity;
        mTwitterString = twitterString;
        mInstagramString = instagramString;
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0)
        {
            return SocialListFragment.newInstance(position, mTwitterString);
        }
        else if(position == 1)
        {
            return SocialListFragment.newInstance(position, mInstagramString);
        }
        else{
            // should never happen as position can only be 0 or 1
            return null;
        }
    }

    @Override
    public int getCount() {
        // Show 2 total pages
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();
        switch (position) {
            case 0:
                return mActivity.getString(R.string.twitter).toUpperCase(l);
            case 1:
                return mActivity.getString(R.string.instagram).toUpperCase(l);
        }
        return null;
    }

    /**
     * Destroy the item from the {@link android.support.v4.view.ViewPager}. In our case this is simply removing the
     * {@link android.view.View}.
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}