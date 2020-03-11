package com.nowmagnate.seeker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.net.Uri;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nowmagnate.seeker.util.GradientStatusBar;

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
    DatabaseReference ref = database.getReference().child("Users");

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
//                onLoginCardClick();
            }
        });

        phoneCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableClick();
//                onLoginCardClick();
            }
        });
    }

    public void onLoginCardClick(GoogleSignInAccount acct){
        final String name = acct.getDisplayName();
        final Uri imgUrl = acct.getPhotoUrl();
        final FirebaseUser user = mAuth.getCurrentUser();
        ref.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild("UserInfo")){
                    Calendar cad = Calendar.getInstance();
                    cad.add(Calendar.DATE,30);

                    Map userProfile = new HashMap<>();
                    userProfile.put("name",name);
                    userProfile.put("userdp", imgUrl.toString());

                    userProfile.put("endPlan",cad.getTime().toString().substring(0,10));
                    userProfile.put("activePlan","basic");
                    userProfile.put("superLikes",0);
                    userProfile.put("coins",0);

                    ref.child(user.getUid()).setValue(userProfile);
//                                .updateChildren(userProfile);
                    startActivity(new Intent(LoginRegister.this,basicInfoActivity.class));
                }else{
                    startActivity(new Intent(LoginRegister.this,MainActivity.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

//        if(!isUserLoggedIn){
//            isInfo();
//            finish();}
//        else {
//            startActivity(new Intent(LoginRegister.this,MainActivity.class));
//            finish();
//        }
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

        animStreet.animate().translationX(0).setDuration(2000);
        animDating.animate().translationY(0).setDuration(2000);

                if(isUserLoggedIn){
                    isInfo();
                }
                else {
                    splashScreen.animate().translationX(splashScreen.getRight()).alpha(0);
                    splashScreen.setClickable(false);
                }
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
    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
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
                            isUserLoggedIn= true;

                            onLoginCardClick(acct);
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
//                        onLoginCardClick();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    public void isInfo(){
        final FirebaseUser user = mAuth.getCurrentUser();
        String userId = user.getUid();
        ref.child("UserInfo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    startActivity(new Intent(LoginRegister.this, MainActivity.class));
                }
                else{
                    Calendar cad = Calendar.getInstance();
                    cad.add(Calendar.DATE,30);

                    Map basicInfo = new HashMap();
                    basicInfo.put("endPlan",cad.getTime().toString().substring(0,10));
                    basicInfo.put("activePlan","basic");
                    basicInfo.put("superLikes",0);
                    basicInfo.put("coins",0);

                    ref.child(user.getUid()).updateChildren(basicInfo);
                    startActivity(new Intent(LoginRegister.this,basicInfoActivity.class));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
