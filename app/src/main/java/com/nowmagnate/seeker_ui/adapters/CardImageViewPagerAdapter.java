package com.nowmagnate.seeker_ui.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.nowmagnate.seeker_ui.fragments.CardImageFragment;

public class CardImageViewPagerAdapter extends FragmentPagerAdapter {

    public CardImageViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0)
        {
            fragment = new CardImageFragment();
        }
        else if (position == 1)
        {
            fragment = new CardImageFragment();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
