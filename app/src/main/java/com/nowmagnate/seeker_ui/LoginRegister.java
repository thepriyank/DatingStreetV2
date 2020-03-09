package com.nowmagnate.seeker_ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nowmagnate.seeker_ui.util.GradientStatusBar;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class LoginRegister extends AppCompatActivity {


    private ConstraintLayout splashScreen;
    private ImageView animImage;
    private TextView animStreet, animDating;
    private GoogleSignInOptions gso;

    private boolean isUserLoggedIn = false;
    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;

    private FirebaseAuth mAuth;

    private GoogleSignInClient mGoogleSignInClient;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("seeker-378eb");

    private CardView googleCard, facebookCard, phoneCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

        splashScreen = findViewById(R.id.splashScreen);
        googleCard = findViewById(R.id.google_card);
        facebookCard = findViewById(R.id.facebook_card);
        phoneCard = findViewById(R.id.phone_card);
        animImage = findViewById(R.id.anim_image);
        animStreet = findViewById(R.id.anim_street);
        animDating = findViewById(R.id.anim_dating);

        GradientStatusBar.setStatusBarGradiant(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestProfile()
                .build();
        // [END config_signin]

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]


        splashScreen.setClickable(true);

        SplashScreenAnim();



        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        googleCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.loading_layout).setVisibility(View.VISIBLE);
                disableClick();
                signIn();

            }
        });

        facebookCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableClick();
                onLoginCardClick();
            }
        });

        phoneCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableClick();
                onLoginCardClick();
            }
        });


    }

    public void onLoginCardClick(){
        if(!isUserLoggedIn){
            FirebaseUser user = mAuth.getCurrentUser();
        Map<String,String> id = new HashMap<>();
        id.put(user.getUid(),getResources().getString(R.string.default_web_client_id));
        ref.setValue(id);
        Map<String,String> name = new HashMap<>();
        name.put("name",user.getDisplayName());
        ref.child(user.getUid()).setValue(name);
        isInfo();
        finish();}
        else {
            startActivity(new Intent(LoginRegister.this,MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        googleCard.setClickable(true);
        facebookCard.setClickable(true);
        phoneCard.setClickable(true);
    }

    public void SplashScreenAnim(){
        Handler handler = new Handler();

        animStreet.animate().translationX(0).setDuration(800);
        animDating.animate().translationY(0).setDuration(800);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                if(isUserLoggedIn){
                    isInfo();
                }
                else {
                    splashScreen.animate().translationX(splashScreen.getRight()).alpha(0);
                    splashScreen.setClickable(false);
                }
            }
        }, 2000);
    }

    public void disableClick(){
        phoneCard.setClickable(false);
        facebookCard.setClickable(false);
        googleCard.setClickable(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            isUserLoggedIn = false;
        }
        else {
            isUserLoggedIn = true;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // [START_EXCLUDE]
                // [END_EXCLUDE]
            }
        }
    }
    // [END onactivityresult]

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            onLoginCardClick();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed. Please check the internet connection.", Snackbar.LENGTH_SHORT).show();
                            findViewById(R.id.loading_layout).setVisibility(View.GONE);
                        }
                    }
                });
    }
    // [END auth_with_google]

    // [START signin]
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signin]

    private void signOut() {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        onLoginCardClick();
                    }
                });
    }

    private void revokeAccess() {
        // Firebase sign out
        mAuth.signOut();

        // Google revoke access
        mGoogleSignInClient.revokeAccess().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        onLoginCardClick();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    public void isInfo(){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("seeker-378eb");
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();

        ref.child(user.getUid()).child("phone").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()==null){
                    ref.child(user.getUid()).child("activePlan").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getValue()==null) {
                                Calendar cad = Calendar.getInstance();
                                cad.add(Calendar.DATE,30);
                                Map m = new HashMap();
                                Map s = new HashMap();
                                Map n = new HashMap();
                                Map endPlan = new HashMap();
                                endPlan.put("endPlan",cad.getTime().toString().substring(0,10));
                                m.put("activePlan","basic");
                                s.put("superLikes",5);
                                n.put("name",user.getDisplayName());

                                ref.child(user.getUid()).updateChildren(m);
                                ref.child(user.getUid()).updateChildren(s);
                                ref.child(user.getUid()).updateChildren(n);
                                ref.child(user.getUid()).updateChildren(endPlan);

                                startActivity(new Intent(LoginRegister.this,basicInfoActivity.class));
                                finish();
                            }else{
                                if(!dataSnapshot.getValue().toString().equals("basic")){
                                    ref.child(user.getUid()).child("init_date").addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if(dataSnapshot!=null) {
                                                Calendar c = Calendar.getInstance();
                                                String dateToday = c.getTime().toString().substring(0, 10);
                                                if (!dataSnapshot.getValue().toString().equals(dateToday)) {
                                                    Map s = new HashMap();
                                                    s.put("superLikes", 5);
                                                    ref.child(user.getUid()).updateChildren(s);
                                                    startActivity(new Intent(LoginRegister.this,basicInfoActivity.class));
                                                    finish();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
                else{
                    startActivity(new Intent(LoginRegister.this,MainActivity.class));
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
