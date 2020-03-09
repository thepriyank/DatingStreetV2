package com.nowmagnate.seeker_ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.nowmagnate.seeker_ui.adapters.HelpFeedbackViewPagerAdapter;
import com.nowmagnate.seeker_ui.adapters.MessageViewPagerAdapter;
import com.nowmagnate.seeker_ui.util.GradientStatusBar;

public class HelpFeedback extends AppCompatActivity {

    //toolbar Views
    private TextView toolbarTitle;
    private ImageView toolbarBack;

    TabLayout tabLayout;
    ViewPager viewPager;
    HelpFeedbackViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_feedback);

        tabLayout = findViewById(R.id.messageTabLayout);
        viewPager = findViewById(R.id.messageViewPager);
        toolbarBack = findViewById(R.id.back);
        toolbarTitle = findViewById(R.id.title);
        GradientStatusBar.setStatusBarGradiant(this);
        toolbarTitle.setText("HELP & FEEDBACK");

        toolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

        viewPagerAdapter = new HelpFeedbackViewPagerAdapter(getSupportFragmentManager(),0);
        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }
}
