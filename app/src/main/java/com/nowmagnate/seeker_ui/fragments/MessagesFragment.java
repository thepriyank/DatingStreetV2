package com.nowmagnate.seeker_ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.nowmagnate.seeker_ui.R;
import com.nowmagnate.seeker_ui.adapters.MessageViewPagerAdapter;

public class MessagesFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager viewPager;
    MessageViewPagerAdapter viewPagerAdapter;

    private TextView datingText;
    private TextView streetText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages, container, false);

        tabLayout = view.findViewById(R.id.messageTabLayout);
        viewPager = view.findViewById(R.id.messageViewPager);

        datingText = view.findViewById(R.id.dating_text);
        streetText = view.findViewById(R.id.street_text);

        datingText.setVisibility(View.GONE);
        streetText.setText("MESSAGES");

        viewPagerAdapter = new MessageViewPagerAdapter(getChildFragmentManager(),0);
        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        return view;
    }
}
