package com.nowmagnate.seeker;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.nowmagnate.seeker.util.GradientStatusBar;


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
