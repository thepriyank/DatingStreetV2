package com.nowmagnate.seeker_ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.nowmagnate.seeker_ui.util.GradientStatusBar;

public class Settings extends AppCompatActivity {

    // Toobar UI
    private ImageView toolbarBack;
    private TextView toolbarTitle;

    //Discovery Settings UI
    private CardView showMeWhoCard;
    private TextView locationTextView, distanceTextView;
    private SeekBar distanceSeekBar;


    //App Settings UI
    private CardView notificationChatCard, accountSecurityCard,
                    privacyPermissionCard, helpFeedbackCard,
                    aboutUsCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        GradientStatusBar.setStatusBarGradiant(this);

        showMeWhoCard = findViewById(R.id.show_me_who_card);
        locationTextView = findViewById(R.id.location_text);
        distanceTextView = findViewById(R.id.distance_text);
        distanceSeekBar = findViewById(R.id.distance_seekbar);
        toolbarBack = findViewById(R.id.back);
        toolbarTitle = findViewById(R.id.title);


        notificationChatCard = findViewById(R.id.notification_and_chat_card);
        accountSecurityCard = findViewById(R.id.account_and_security_card);
        privacyPermissionCard = findViewById(R.id.privacy_and_permission_card);
        helpFeedbackCard = findViewById(R.id.help_and_feedback_card);
        aboutUsCard = findViewById(R.id.about_us_card);


        toolbarTitle.setText("SETTINGS");

        initDistanceSeekBar();

        toolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        showMeWhoCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableClick();
                startActivity(new Intent(Settings.this,ShowMeWho.class));
            }
        });

        aboutUsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableClick();
                startActivity(new Intent(Settings.this,AboutUs.class));
            }
        });

        helpFeedbackCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableClick();
                startActivity(new Intent(Settings.this,HelpFeedback.class));
            }
        });

        accountSecurityCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableClick();
                startActivity(new Intent(Settings.this,AccountSecurity.class));
            }
        });

        notificationChatCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableClick();disableClick();
                startActivity(new Intent(Settings.this,NotificationChat.class));
            }
        });

        privacyPermissionCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableClick();
                startActivity(new Intent(Settings.this,PrivacyPermission.class));
            }
        });



    }

    private void initDistanceSeekBar(){

        distanceSeekBar.setMax(100);
        distanceSeekBar.setProgress(1);

        distanceSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(i < 1){
                    distanceSeekBar.setProgress(1);
                    distanceTextView.setText(1 + " Km");
                }else if(distanceSeekBar.getProgress() == 100){
                    distanceTextView.setText(String.valueOf(i) + "+" + " Km");
                }else {
                    distanceTextView.setText(String.valueOf(i) + " Km");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        showMeWhoCard.setClickable(true);
        aboutUsCard.setClickable(true);
        helpFeedbackCard.setClickable(true);
        accountSecurityCard.setClickable(true);
        notificationChatCard.setClickable(true);
        privacyPermissionCard.setClickable(true);

    }

    public void disableClick(){
        showMeWhoCard.setClickable(false);
        aboutUsCard.setClickable(false);
        helpFeedbackCard.setClickable(false);
        accountSecurityCard.setClickable(false);
        notificationChatCard.setClickable(false);
        privacyPermissionCard.setClickable(false);

    }
}
