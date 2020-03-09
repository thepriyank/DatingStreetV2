package com.nowmagnate.seeker_ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nowmagnate.seeker_ui.util.GradientStatusBar;

public class CoinTask extends AppCompatActivity {

    private CardView one, two, doTaskCard;
    private TextView oneText, twoText,errorMessage;
    private ImageView oneCheck, twoCheck;
    private ImageView toolbarBack;
    private TextView toolbarTitle;
    boolean isTaskComplete = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        one = findViewById(R.id.one_card);
        two = findViewById(R.id.two_card);
        oneText = findViewById(R.id.one_text);
        twoText = findViewById(R.id.two_text);
        oneCheck = findViewById(R.id.one_check);
        twoCheck = findViewById(R.id.two_check);
        errorMessage = findViewById(R.id.error_message_text);
        doTaskCard = findViewById(R.id.do_task_card);
        toolbarBack = findViewById(R.id.back);
        toolbarTitle = findViewById(R.id.title);

        GradientStatusBar.setStatusBarGradiant(this);

        toolbarTitle.setText("TASK");
        toolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        doTaskCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doTaskCard.setClickable(false);
                if(oneCheck.getVisibility() == View.GONE){
                    numberClick(one);
                }
                else{
                    numberClick(two);
                }
            }
        });
    }

    public void numberClick(final CardView c){

        isTaskComplete = true;

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                if(isTaskComplete) {
                    c.setCardBackgroundColor(Color.parseColor("#EF5557"));
                    if(c.getId() == R.id.one_card){
                        oneText.setVisibility(View.INVISIBLE);
                        oneCheck.setVisibility(View.VISIBLE);
                    }else{
                        twoText.setVisibility(View.INVISIBLE);
                        twoCheck.setVisibility(View.VISIBLE);
                    }
                    errorMessage.setVisibility(View.GONE);
                    isTaskComplete = false;
                    doTaskCard.setClickable(true);
                }
                else{
                    errorMessage.setVisibility(View.VISIBLE);
                }
            }
        }, 5000);
    }

    @Override
    public void onBackPressed() {
        if(isTaskComplete){
            isTaskComplete = false;
            doTaskCard.setClickable(true);
            errorMessage.setVisibility(View.VISIBLE);
        }
        else {
            super.onBackPressed();
        }
    }
}
