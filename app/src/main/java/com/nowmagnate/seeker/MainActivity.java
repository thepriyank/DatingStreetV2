package com.nowmagnate.seeker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nowmagnate.seeker.adapters.OnPlanListener;
import com.nowmagnate.seeker.fragments.AccountsFragment;
import com.nowmagnate.seeker.fragments.CardsFragment;
import com.nowmagnate.seeker.fragments.CoinsFragment;
import com.nowmagnate.seeker.fragments.CrushFabFragment;
import com.nowmagnate.seeker.fragments.MessagesFragment;
import com.nowmagnate.seeker.util.NotificationService;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements OnPlanListener {

    //Bottom App Bar Icons UI
    private ImageView cards,coins,messages,account;
    private FloatingActionButton crushFab;
    private CardView buyPlanCard;
    private ConstraintLayout buyDialog;

    SharedPreferences preferences;
    Fragment selectedFragment = null;
    private boolean isFirstLaunch;

    boolean trap = true;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference().child("Users");
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cards = findViewById(R.id.cards);
        coins = findViewById(R.id.coins);
        messages = findViewById(R.id.messages);
        account = findViewById(R.id.account);
        crushFab = findViewById(R.id.crushFab);
        buyDialog = findViewById(R.id.buyDialog);
        buyPlanCard = findViewById(R.id.buyPlanCard);

        Intent i = getIntent();
        if(i.getIntExtra("startAccount",0)==1){
            replaceFragment(new AccountsFragment());
            navIconSelected(account);
        }

        preferences = getSharedPreferences("UPDATED",MODE_PRIVATE);
        if(preferences.getBoolean("isUPDATED", false)==false){
            //startActivity(new Intent(MainActivity.this,EditProfileInfo.class));
        }
        Log.i("pop", String.valueOf(preferences.getBoolean("isUPDATED",false)));



        cards.setColorFilter(Color.parseColor("#AB0092FF"));
        addDateStamp();
        checkPlanEnd();

        getCurrentTime();

        setStatusBarGradiant(this);
        intBottomNavBar();

        buyPlanCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buyPlanCard.setClickable(false);
                startActivity(new Intent(MainActivity.this,ChangePlans.class));
                showPayLayout(false);
            }
        });

        buyDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPayLayout(false);
            }
        });

        Intent serviceIntent = new Intent(this, NotificationService
                .class);

        ContextCompat.startForegroundService(this, serviceIntent);
    }

    public void intBottomNavBar(){

        replaceFragment(new CardsFragment());

        cards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new CardsFragment());
                navIconSelected(cards);

            }
        });

        coins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new CoinsFragment());
                navIconSelected(coins);
            }
        });

        crushFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new CrushFabFragment());
                navIconSelected(crushFab);
            }
        });

        messages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new MessagesFragment());
                navIconSelected(messages);
            }
        });

        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new AccountsFragment());
                navIconSelected(account);
            }
        });
    }

    public void replaceFragment(Fragment fragment){
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_frame,
//                fragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_frame,fragment).commit();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarGradiant(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            Drawable background = activity.getResources().getDrawable(R.drawable.gradient_red_blue);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
            window.setNavigationBarColor(activity.getResources().getColor(android.R.color.transparent));
            window.setBackgroundDrawable(background);
        }
    }

    public void navIconSelected(ImageView c){

        cards.setColorFilter(null);
        coins.setColorFilter(null);
        messages.setColorFilter(null);
        account.setColorFilter(null);
        crushFab.setColorFilter(null);

        c.setColorFilter(Color.parseColor("#AB0092FF"));
    }

    public void fin(){
        finish();
    }

    public void signOut(){
        SharedPreferences.Editor editor = getSharedPreferences("UPDATED", MODE_PRIVATE).edit();
        editor.putBoolean("isUPDATED",false);
        editor.apply();
    }

    public void addDateStamp(){
        isFirstLaunch = true;
        if(user!=null) {
            ref.child(user.getUid()).child("init_date").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Calendar c = Calendar.getInstance();
                    Log.i("pop", c.getTime().toString().substring(0, 10));
                    Map d = new HashMap();
                    d.put("init_date", c.getTime().toString().substring(0, 10));
                    if (dataSnapshot.getValue() == null) {

                        ref.child(user.getUid()).updateChildren(d);
                        isFirstLaunch = false;
                    }
                    else if(!dataSnapshot.getValue().equals(d)){
                        Map superLike = new HashMap();
                        superLike.put("superLikes",5);
                        ref.child(user.getUid()).child("UserInfo").updateChildren(superLike);
                        ref.child(user.getUid()).child("UserInfo").updateChildren(d);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }else{
            startActivity(new Intent(MainActivity.this,LoginRegister.class));
        }
        }

    public static String getDateStamp(){
        Calendar c = Calendar.getInstance();


        return c.getTime().toString().substring(0,10);
    }

    public static long getCurrentTime(){
        Calendar c = Calendar.getInstance();
        return c.getTimeInMillis();
    }

    public void showPayLayout(boolean show){
        if(show) {
            buyDialog.setVisibility(View.VISIBLE);
        }else{
            buyDialog.setVisibility(View.GONE);
        }
    }

    public void checkPlanEnd(){
        if(user!=null) {
            ref.child(user.getUid()).child("UserInfo").child("endPlan").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        if (dataSnapshot.getValue().toString().equals(getDateStamp())) {
                            ref.child(user.getUid()).child("UserInfo").child("activePlan").setValue("basic");
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
    public void onBackPressed() {
        if(buyDialog.getVisibility()==View.VISIBLE){
            showPayLayout(false);
        }else {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        buyPlanCard.setClickable(true);
    }

    @Override
    public void onPlanClick(int position) {

    }

    public Activity getMainActivity(){
        return this;
    }
}
