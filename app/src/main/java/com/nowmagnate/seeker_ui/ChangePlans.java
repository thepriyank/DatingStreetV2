package com.nowmagnate.seeker_ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nowmagnate.seeker_ui.adapters.OnPlanListener;
import com.nowmagnate.seeker_ui.adapters.PlanAdapter;
import com.nowmagnate.seeker_ui.util.GradientStatusBar;

public class ChangePlans extends AppCompatActivity implements OnPlanListener {

    private ImageView toolbarBack;
    private TextView toolbarTitle;
    private RecyclerView planRecycler;
    private PlanAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_plans);

        toolbarBack = findViewById(R.id.back);
        toolbarTitle = findViewById(R.id.title);
        planRecycler = findViewById(R.id.plan_recycler);

        GradientStatusBar.setStatusBarGradiant(this);

        toolbarTitle.setText("CHANGE PLAN");
        toolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

        adapter = new PlanAdapter(this,this);
        planRecycler.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        planRecycler.setLayoutManager(linearLayoutManager);

    }

    public void popToast(String s){
        Toast.makeText(ChangePlans.this,s,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPlanClick(int position) {

    }
}
