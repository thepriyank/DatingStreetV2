package com.nowmagnate.seeker_ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nowmagnate.seeker_ui.util.GradientStatusBar;

public class NotificationChat extends AppCompatActivity {

    // Toobar UI
    private ImageView toolbarBack;
    private TextView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_chat);

        toolbarBack = findViewById(R.id.back);
        toolbarTitle = findViewById(R.id.title);

        GradientStatusBar.setStatusBarGradiant(this);

        toolbarTitle.setText("Notification & Chat");
        toolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
