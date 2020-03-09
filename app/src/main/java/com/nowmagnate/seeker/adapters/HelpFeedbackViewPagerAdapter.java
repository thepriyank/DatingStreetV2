package com.nowmagnate.seeker.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.nowmagnate.seeker.fragments.FeedbackFragment;
import com.nowmagnate.seeker.fragments.HelpFragment;

public class HelpFeedbackViewPagerAdapter extends FragmentPagerAdapter {

    public HelpFeedbackViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0)
        {
            fragment = new HelpFragment();
        }
        else if (position == 1)
        {
            fragment = new FeedbackFragment();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
