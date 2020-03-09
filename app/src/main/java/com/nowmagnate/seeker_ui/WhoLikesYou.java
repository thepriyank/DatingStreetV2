package com.nowmagnate.seeker_ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nowmagnate.seeker_ui.util.GradientStatusBar;

public class WhoLikesYou extends AppCompatActivity {

    private ImageView toolbarBack;
    private TextView toolbarTitle;

    private CardView buyGoldPlanCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_who_likes_you);

        toolbarBack = findViewById(R.id.back);
        toolbarTitle = findViewById(R.id.title);
        buyGoldPlanCard = findViewById(R.id.buy_gold_plan_card);


        GradientStatusBar.setStatusBarGradiant(this);

        toolbarTitle.setText("WHO LIKES YOU");
        toolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

        buyGoldPlanCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buyGoldPlanCard.setClickable(false);
                startActivity(new Intent(WhoLikesYou.this,ChangePlans.class));
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        buyGoldPlanCard.setClickable(true);
    }
}
