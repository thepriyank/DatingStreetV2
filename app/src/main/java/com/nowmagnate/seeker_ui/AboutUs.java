package com.nowmagnate.seeker_ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nowmagnate.seeker_ui.util.GradientStatusBar;

public class AboutUs extends AppCompatActivity {

    private ImageView toolbarBack;
    private TextView toolbarTitle;
    private NestedScrollView innerDialog;
    private TextView headerText,termsOfServices,pricacyPolicy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        toolbarBack = findViewById(R.id.back);
        toolbarTitle = findViewById(R.id.title);
        innerDialog = findViewById(R.id.inner_dialog);
        headerText = findViewById(R.id.header_text);
        pricacyPolicy = findViewById(R.id.privacy_policy_text);
        termsOfServices = findViewById(R.id.terms_service_text);

        GradientStatusBar.setStatusBarGradiant(this);

        toolbarTitle.setText("ABOUT DATING STREET");
        toolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        pricacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                headerText.setText("PRIVACY POLICY");
                innerDialog.setVisibility(View.VISIBLE);
            }
        });


        termsOfServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                headerText.setText("TERMS OF SERVICES");
                innerDialog.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onBackPressed() {

        if(innerDialog.getVisibility() == View.VISIBLE){
            innerDialog.setVisibility(View.GONE);
            toolbarTitle.setText("ABOUT DATING STREET");
        }
        else {
            super.onBackPressed();
            finish();
        }
    }
}
