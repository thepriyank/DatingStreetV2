package com.nowmagnate.seeker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nowmagnate.seeker.util.GradientStatusBar;

public class VIPMember extends AppCompatActivity {

    private ImageView toolbarBack;
    private TextView toolbarTitle;
    private TextView isVIP, currentActivePlan;
    private LinearLayout baseHistoryLayout;

    private boolean firstlaunch = true;

    int i;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference().child("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vipmenmber);

        toolbarBack = findViewById(R.id.back);
        toolbarTitle = findViewById(R.id.title);
        isVIP = findViewById(R.id.isVIP);
        currentActivePlan = findViewById(R.id.current_plan_text);
        baseHistoryLayout = findViewById(R.id.baseHistoryLayout);

        GradientStatusBar.setStatusBarGradiant(this);

        toolbarTitle.setText("VIP Member");
        toolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

        ref.child(user.getUid()).child("UserInfo").child("activePlan").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    final String planName = dataSnapshot.getValue().toString();
                    currentActivePlan.setText(planName.toUpperCase());
                    if(planName.equals("basic")){
                        isVIP.setText("NO");
                    }
                    else {
                        isVIP.setText("YES");
                    }
                    final DatabaseReference databaseReference = database.getReference().child("plans");
                    databaseReference.child(dataSnapshot.getValue().toString()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()) {
                                if(firstlaunch) {
                                    for (i = 0; i < dataSnapshot.getChildrenCount() - 1; i++) {
                                        final int count = (int) dataSnapshot.getChildrenCount() - 1;
                                        databaseReference.child(planName).child(String.valueOf(i)).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()) {
                                                    additem(dataSnapshot.getValue().toString());
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                    firstlaunch = false;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void additem(String s){
        TextView t = new TextView(this);
        t.setText(s);
        t.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(5,0,0,0);
        t.setLayoutParams(params);

        View box = new View(this);
        box.setBackgroundColor(Color.parseColor("#000000"));
        box.setLayoutParams(new LinearLayout.LayoutParams(20, 20));

        View line = new View(this);
        line.setBackgroundColor(Color.parseColor("#000000"));
        line.setLayoutParams(new LinearLayout.LayoutParams(4, 10));

        View lineLong = new View(this);
        lineLong.setBackgroundColor(Color.parseColor("#000000"));
        lineLong.setLayoutParams(new LinearLayout.LayoutParams(4, 8));

        View lineLong2 = new View(this);
        lineLong2.setBackgroundColor(Color.parseColor("#000000"));
        lineLong2.setLayoutParams(new LinearLayout.LayoutParams(4, 8));

        LinearLayout bulletLinearLayout = new LinearLayout(this);
        bulletLinearLayout.setGravity(Gravity.CENTER);
        bulletLinearLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout linearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);

        if(baseHistoryLayout.getChildCount() == 0){
            LinearLayout.LayoutParams bulletLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,2f);
            bulletLinearLayout.setLayoutParams(bulletLayoutParams);
            bulletLinearLayout.addView(box);
            bulletLinearLayout.addView(line);

            linearLayout.addView(bulletLinearLayout);
            linearLayout.addView(t);

            baseHistoryLayout.addView(linearLayout);
        }
        else if(baseHistoryLayout.getChildCount() == 3){
            LinearLayout.LayoutParams bulletLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,3f);
            bulletLinearLayout.setLayoutParams(bulletLayoutParams);
            bulletLinearLayout.addView(line);
            bulletLinearLayout.addView(box);

            linearLayout.addView(bulletLinearLayout);
            linearLayout.addView(t);

            baseHistoryLayout.addView(linearLayout);
        }
        else{

            LinearLayout.LayoutParams bulletLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,3f);
            bulletLinearLayout.setLayoutParams(bulletLayoutParams);
            bulletLinearLayout.addView(lineLong);
            bulletLinearLayout.addView(box);
            bulletLinearLayout.addView(lineLong2);

            linearLayout.addView(bulletLinearLayout);
            linearLayout.addView(t);

            baseHistoryLayout.addView(linearLayout);
        }
    }
}
