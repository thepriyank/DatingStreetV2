package com.nowmagnate.seeker_ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.nowmagnate.seeker_ui.util.GradientStatusBar;

public class ProfileDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_detail);

        GradientStatusBar.setStatusBarGradiant(this);
    }
}
