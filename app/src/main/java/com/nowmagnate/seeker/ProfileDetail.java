package com.nowmagnate.seeker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nowmagnate.seeker.util.GradientStatusBar;

import java.util.Calendar;
import java.util.Map;
import java.util.Vector;

public class ProfileDetail extends AppCompatActivity {

    private ViewPager profileImagesPager;
    private TabLayout pagerIndicator;
    Vector<String> profileImages= new Vector<>();
    private TextView tvName, tvAge, tvHeight, tvLocation, tvBio, tvProfession, tvEducation, tvSex;
    private RecyclerView rvTraits;
    private FloatingActionButton cvLike, cvSuperlike, cvReject;
    private Vector<PersonalityTraits> personalityTraits= new Vector<>();
    private GridLayoutManager gridLayoutManager;
    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase, usersDb;
    private String currentUId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_detail);
        final String userid = getIntent().getStringExtra("userid");
        fillUserDetails(userid);

        cvLike= findViewById(R.id.acceptFab);
        cvReject= findViewById(R.id.rejectFab);
        cvSuperlike= findViewById(R.id.superFab);

        tvName = findViewById(R.id.tvUsername);
        tvAge = findViewById(R.id.tvUserAge);
        tvHeight = findViewById(R.id.tvUserHeight);
        tvLocation = findViewById(R.id.tvUserLoctaion);
        tvBio = findViewById(R.id.tvUserBio);
        tvProfession = findViewById(R.id.tvUserProfession);
        tvEducation = findViewById(R.id.tvUserEducation);
        tvSex = findViewById(R.id.tvUserSex);
        rvTraits = findViewById(R.id.rvTraits);

        profileImagesPager = findViewById(R.id.profileImagesViewpager);
        pagerIndicator = findViewById(R.id.viewpagerIndicator);

        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        currentUId = mAuth.getCurrentUser().getUid();

        cvReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usersDb.child(userid).child("connections").child("nope").child(currentUId).setValue(true);
                Intent actionIntent = new Intent();
                actionIntent.putExtra("Action",true);
                setResult(123,actionIntent);
                finish();

            }
        });

        cvLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usersDb.child(userid).child("connections").child("yeps").child(currentUId).setValue(true);
                Intent actionIntent = new Intent();
                actionIntent.putExtra("Action",true);
                setResult(123,actionIntent);
                finish();
            }
        });

        cvSuperlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usersDb.child(userid).child("connections").child("yeps").child(currentUId).setValue(true);
                usersDb.child(userid).child("connections").child("super").child(currentUId).setValue(true);
                Intent actionIntent = new Intent();
                actionIntent.putExtra("Action",true);
                setResult(123,actionIntent);
                finish();
            }
        });


        GradientStatusBar.setStatusBarGradiant(this);
    }


    private void fillUserDetails(String userId) {
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        mUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if(map.get("name")!=null){
                        String initials[] = map.get("name").toString().split(" ");
                        String name = "";
                        for(int i=0; i<initials.length;i++){
                            name+= initials[i].substring(0,1);
                        }
                        tvName.setText(name +", ");
                    }
                    if(dataSnapshot.child("UserInfo").child("dob")!=null){
                        Toast.makeText(ProfileDetail.this, dataSnapshot.child("UserInfo").child("dob").getValue().toString(), Toast.LENGTH_SHORT).show();
                        String[] dob= dataSnapshot.child("UserInfo").child("dob").getValue().toString().split("/");
                        tvAge.setText(getAge(Integer.parseInt(dob[2]),Integer.parseInt(dob[0]),Integer.parseInt(dob[1])));
                    }
                    if(map.get("height_feet")!=null){
                        tvHeight.setText(map.get("height_feet").toString()+" feet ");
                    }
                    if(map.get("height_inch")!=null){
                        tvHeight.setText(tvHeight.getText().toString()+map.get("height_inch").toString()+" inch");
                    }
                    if(map.get("about")!=null){
                        tvBio.setText(map.get("about").toString());
                    }
                    if(map.get("location")!=null){
                        tvLocation.setText(map.get("location").toString());
                    }
                    if(map.get("highest_edu")!=null){
                        tvEducation.setText(map.get("highest_edu").toString());
                    }
                    if(map.get("current_profession")!=null){
                        tvProfession.setText(map.get("current_profession").toString());
                    }
                    if(dataSnapshot.child("UserInfo").child("gender")!=null){
                        tvSex.setText(dataSnapshot.child("UserInfo").child("gender").getValue().toString());
                    }
                    if(map.get("traits")!=null){
                        String traits[] = map.get("traits").toString().split(",");
                        setPersonalityTraits(traits);
                    }

                    if(dataSnapshot.child("UserInfo").child("profileImageUrl")!=null){
                        profileImages.add(dataSnapshot.child("UserInfo").child("profileImageUrl").getValue().toString());
                    }
//                    if(map.get("image1")!=null){
//                        profileImages.add(map.get("image1").toString());
//                    }
//                    if(map.get("image2")!=null){
//                        profileImages.add(map.get("image2").toString());
//                    }
//                    if(map.get("image3")!=null){
//                        profileImages.add(map.get("image3").toString());
//                    }
//                    if(map.get("image4")!=null){
//                        profileImages.add(map.get("image4").toString());
//                    }
//                    if(map.get("image5")!=null){
//                        profileImages.add(map.get("image5").toString());
//                    }
//                    if(map.get("image6")!=null){
//                        profileImages.add(map.get("image6").toString());
//                    }
//                    if(map.get("image7")!=null){
//                        profileImages.add(map.get("image7").toString());
//                    }

                    profileImagesPager.setAdapter(new ProfileImagesAdapter(getApplicationContext() ,profileImages));
                    pagerIndicator.setupWithViewPager(profileImagesPager,true);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private String getAge(int year, int month, int day){
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }

    private void setPersonalityTraits(String values[]) {

        for(int i=0;i<values.length;++i){
            personalityTraits.add(new PersonalityTraits(values[i],false));
        }
        rvTraits.setHasFixedSize(true);
        //rvTraits.setLayoutManager(new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false));
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
        rvTraits.setLayoutManager(gridLayoutManager);
        rvTraits.setAdapter(new UserViewPersonalityAdapter(getApplicationContext(),personalityTraits));
    }

}
