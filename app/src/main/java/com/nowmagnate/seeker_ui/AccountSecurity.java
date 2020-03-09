package com.nowmagnate.seeker_ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.nowmagnate.seeker_ui.util.GradientStatusBar;


public class AccountSecurity extends AppCompatActivity {
    private ImageView toolbarBack;
    private TextView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_security);

        GradientStatusBar.setStatusBarGradiant(this);

        toolbarBack = findViewById(R.id.back);
        toolbarTitle = findViewById(R.id.title);

        toolbarTitle.setText("Account & Security");
        toolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });



    }

}
