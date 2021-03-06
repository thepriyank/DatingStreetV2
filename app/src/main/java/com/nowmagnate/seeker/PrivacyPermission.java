package com.nowmagnate.seeker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nowmagnate.seeker.util.GradientStatusBar;

public class PrivacyPermission extends AppCompatActivity {

    private ImageView toolbarBack;
    private TextView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_permission);

        toolbarBack = findViewById(R.id.back);
        toolbarTitle = findViewById(R.id.title);

        GradientStatusBar.setStatusBarGradiant(this);

        toolbarTitle.setText("Privacy & Permission");
        toolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

    }
}
