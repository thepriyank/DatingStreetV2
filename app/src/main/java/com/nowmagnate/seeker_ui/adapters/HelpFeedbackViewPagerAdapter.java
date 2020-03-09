package com.nowmagnate.seeker_ui.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.nowmagnate.seeker_ui.fragments.ChatsFragment;
import com.nowmagnate.seeker_ui.fragments.FeedbackFragment;
import com.nowmagnate.seeker_ui.fragments.HelpFragment;
import com.nowmagnate.seeker_ui.fragments.MatchsFragment;

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
