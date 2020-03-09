package com.nowmagnate.seeker_ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.nowmagnate.seeker_ui.util.GradientStatusBar;


public class ShowMeWho extends AppCompatActivity {
    private CardView genderMaleCard, genderFemaleCard, doneCard;
    private TextView ageRangeTextView;
    private ImageView toolbarBack;
    private TextView toolbarTitle;
    private CrystalRangeSeekbar ageSeekbar;
    private int maxAgeSeek = 100;
    private int minAgeSeek = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_me_who);

        GradientStatusBar.setStatusBarGradiant(this);

        genderMaleCard = findViewById(R.id.gender_male_card);
        genderFemaleCard = findViewById(R.id.gender_Female_card);
        ageRangeTextView = findViewById(R.id.age_range_text);
        toolbarBack = findViewById(R.id.back);
        toolbarTitle = findViewById(R.id.title);
        doneCard = findViewById(R.id.done_card);
        ageSeekbar = findViewById(R.id.age_seekbar);

        toolbarTitle.setText("SHOW ME WHO");
        toolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        initAgeRangeSeekBar();

        genderMaleCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                genderMaleCard.setCardBackgroundColor(Color.parseColor("#EF5557"));
                genderFemaleCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"));

                //append code here
            }
        });
        
        genderFemaleCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                genderFemaleCard.setCardBackgroundColor(Color.parseColor("#EF5557"));
                genderMaleCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"));

                //append code here
            }
        });




        doneCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public void initAgeRangeSeekBar(){

        ageSeekbar.setMinValue(13);

        ageSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                if(maxValue.intValue() == 60){
                    ageRangeTextView.setText(minValue + " - " + maxValue + "+");
                }else {
                    ageRangeTextView.setText(minValue + " - " + maxValue);
                }
            }
        });


    }
}
