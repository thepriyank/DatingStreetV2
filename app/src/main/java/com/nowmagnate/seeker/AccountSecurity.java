package com.nowmagnate.seeker;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nowmagnate.seeker.util.GradientStatusBar;

import org.w3c.dom.Text;


public class AccountSecurity extends AppCompatActivity {
    private ImageView toolbarBack;
    private TextView toolbarTitle;

    private CardView deleteAccountCard;
    private TextView currentLoggedInAccount;
    private LinearLayout deletingLayout;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference().child("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_security);

        GradientStatusBar.setStatusBarGradiant(this);

        toolbarBack = findViewById(R.id.back);
        toolbarTitle = findViewById(R.id.title);
        deleteAccountCard = findViewById(R.id.deleteAccountCard);
        currentLoggedInAccount = findViewById(R.id.loggedInAccount);
        deletingLayout = findViewById(R.id.deletingLayout);

        toolbarTitle.setText("Account & Security");
        toolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

        if(currentLoggedInAccount.getText().toString().isEmpty()) {
            currentLoggedInAccount.setText(user.getEmail());
        }

        Log.d("GoogleActivity", user.getMetadata().toString());

        deleteAccountCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deletingLayout.setVisibility(View.VISIBLE);
                AuthCredential credential = GoogleAuthProvider
                        .getCredential(getString(R.string.default_web_client_id),null);

                // Prompt the user to re-provide their sign-in credentials
                user.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                user.delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d("TAG", "User account deleted.");
                                                    signOut();
                                                }
                                            }
                                        });
                            }
                        });
            }
        });



    }

    private void signOut() {
        // Firebase sign out
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        GoogleSignInClient mGoogleSignInClient;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestProfile()
                .build();
        // [END config_signin]

        mGoogleSignInClient = GoogleSignIn.getClient(getApplicationContext(), gso);

        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        ref.child(user.getUid()).removeValue();
                        Intent mStartActivity = new Intent(AccountSecurity.this, LoginRegister.class);
                        int mPendingIntentId = 123456;
                        PendingIntent mPendingIntent = PendingIntent.getActivity(AccountSecurity.this, mPendingIntentId,    mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
                        AlarmManager mgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 50, mPendingIntent);
                        System.exit(0);
                    }
                });
    }

    @Override
    public void onBackPressed() {
        if(deletingLayout.getVisibility() != View.VISIBLE) {
            super.onBackPressed();
        }
    }
}
