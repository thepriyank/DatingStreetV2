package com.nowmagnate.seeker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class VideoCallActivity extends AppCompatActivity {

    DatabaseReference userRef;
    String remoteUserId="", remoteUserImg="", remoteUserName="";
    String localUserId="", localUserImg="", localUserName="", cancelCheck="";
    String callingId="", ringingId="";
    ImageView matchImg;
    ImageButton ibReceive, ibReject;
    TextView tvMatchName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_call);
//        ibReceive=findViewById(R.id.btAcceptCall);
//        ibReject=findViewById(R.id.btDisconnectCall);

        Intent infoIntent = getIntent();

        userRef = FirebaseDatabase.getInstance().getReference().child("Users");

        remoteUserId = infoIntent.getStringExtra("remote_user");
        String type = infoIntent.getStringExtra("type");
        localUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if(type.equals("receiving")){
            ibReceive.setVisibility(View.VISIBLE);
        }else{
            ibReceive.setVisibility(View.GONE);
        }

        matchImg = findViewById(R.id.ivMatchImg);
        tvMatchName = findViewById(R.id.matchName);

        FetchMatchInformation(remoteUserId);
        FetchMatchInformation(localUserId);

        ibReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelCheck = "clicked";
                cancelCall();
            }
        });

        ibReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void cancelCall() {
        //If sender cancels call
        userRef.child(localUserId).child("Calling")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists() && dataSnapshot.hasChild("Calling")){
                            callingId=dataSnapshot.child("calling").getValue().toString();
                            userRef.child(callingId).child("Ringing").removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()) {
                                                userRef.child(localUserId).child("Calling").removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                finish();
                                                            }
                                                        });
                                            }
                                        }
                                    });
                        }
                        else{
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        //If receiver cancels call

        userRef.child(localUserId).child("Ringing")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists() && dataSnapshot.hasChild("Ringing")){
                            ringingId=dataSnapshot.child("ringing").getValue().toString();
                            userRef.child(ringingId).child("Calling").removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()) {
                                                userRef.child(localUserId).child("Ringing").removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                finish();
                                                            }
                                                        });
                                            }
                                        }
                                    });
                        }
                        else{
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void FetchMatchInformation(final String key) {
        DatabaseReference userDb = FirebaseDatabase.getInstance().getReference().child("Users").child(key);
        userDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    if(dataSnapshot.child("name").getValue()!=null){
                        if(key.equals(remoteUserId)) {
                            remoteUserName = dataSnapshot.child("name").getValue().toString();
                            tvMatchName.setText(remoteUserName);
                        }
                        else if(key.equals(localUserId)){
                            localUserName = dataSnapshot.child("name").getValue().toString();
                        }
                    }
                    if(dataSnapshot.child("profileImageUrl").getValue()!=null){
                        if(key.equals(remoteUserId)) {
                            remoteUserImg = dataSnapshot.child("profileImageUrl").getValue().toString();
                            Glide.with(getApplicationContext()).load(remoteUserImg).into(matchImg);
                        }
                        else if(key.equals(localUserId)) {
                            localUserImg = dataSnapshot.child("profileImageUrl").getValue().toString();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        userRef.child(remoteUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild("Calling") && !dataSnapshot.hasChild("Ringing")){
                    final HashMap<String, Object> callingInfo = new HashMap<>();

                    callingInfo.put("calling",remoteUserId);

                    userRef.child(localUserId).child("Calling").updateChildren(callingInfo)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        final HashMap<String, Object> ringingInfo = new HashMap<>();

                                        ringingInfo.put("ringing",localUserId);

                                        userRef.child(remoteUserId).child("Ringing").updateChildren(ringingInfo);
                                    }
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(localUserId).hasChild("Ringing") && !dataSnapshot.child(localUserId).hasChild("Calling")){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}
