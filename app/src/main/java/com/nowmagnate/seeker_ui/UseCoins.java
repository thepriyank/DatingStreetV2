package com.nowmagnate.seeker_ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nowmagnate.seeker_ui.util.GradientStatusBar;

public class UseCoins extends AppCompatActivity {

    private ImageView toolbarBack;
    private TextView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_use_coins);

        toolbarBack = findViewById(R.id.back);
        toolbarTitle = findViewById(R.id.title);

        GradientStatusBar.setStatusBarGradiant(this);

        toolbarTitle.setText("USE COINS");
        toolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
