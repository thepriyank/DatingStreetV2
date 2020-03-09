package com.nowmagnate.seeker_ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nowmagnate.seeker_ui.util.GradientStatusBar;

public class Verification extends AppCompatActivity {

    private ImageView toolbarBack;
    private TextView toolbarTitle;

    private CardView verifyCard;
    private TextView verifyText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);


        toolbarBack = findViewById(R.id.back);
        toolbarTitle = findViewById(R.id.title);
        verifyCard = findViewById(R.id.verify_card);
        verifyText = findViewById(R.id.verifyText);

        GradientStatusBar.setStatusBarGradiant(this);

        toolbarTitle.setText("Verification");
        toolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

        verifyCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyCard.setElevation(0);
                verifyText.setBackground(null);
                verifyText.setText("Verified");
                verifyText.setTextColor(Color.parseColor("#471111"));
            }
        });
    }
}
