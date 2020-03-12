package com.nowmagnate.seeker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nowmagnate.seeker.adapters.ChooseTraits;
import com.nowmagnate.seeker.adapters.PersonalityTraitsAdapter;
import com.nowmagnate.seeker.util.GradientStatusBar;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class EditProfileInfo extends AppCompatActivity implements ChooseTraits {

    //toolbar Views
    private TextView toolbarTitle;
    private ImageView toolbarBack;

    private Spinner edu_spinner;
    private Spinner prof_spinner;

    private EditText profileName;
    private EditText aboutMe;
    private EditText heightFeet;
    private EditText heightInch;

    private EditText location;
    private TextView heightText;

    private CardView update;
    private RecyclerView rvTraits;
    private Vector<PersonalityTraits> personalityTraits= new Vector<>();
    private GridLayoutManager gridLayoutManager;
    //Var

    Map UserProfile;


    boolean isAllFieldsClear = true;
    boolean isAllFieldsUpdated = false;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_info);


        toolbarBack = findViewById(R.id.back);
        toolbarTitle = findViewById(R.id.title);
        edu_spinner = findViewById(R.id.spinner_edu);
        prof_spinner = findViewById(R.id.spinner_prof);
        profileName = findViewById(R.id.profile_name_editText);
        aboutMe = findViewById(R.id.about_editText);
        heightFeet = findViewById(R.id.height_feet_editText);
        heightInch = findViewById(R.id.height_inches_editText);
        location = findViewById(R.id.location_editText);
        update = findViewById(R.id.update);
        heightText = findViewById(R.id.height_text);
        rvTraits = findViewById(R.id.rvTraits);

        GradientStatusBar.setStatusBarGradiant(this);

        toolbarTitle.setText("EDIT PROFILE");
        toolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        setPersonalityTraits();
        initSpinner();
        fillUserDetails(user.getUid());
        //initUI();

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //add map fragment code here
                Toast.makeText(EditProfileInfo.this,"Add map fragment code here", Toast.LENGTH_LONG).show();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
            }
        });

    }

    private void fillUserDetails(String userId) {
        DatabaseReference mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        mUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if(map.get("name")!=null){
                        profileName.setText(map.get("name").toString());
                    }
                    if(map.get("height_feet")!=null){
                        heightFeet.setText(map.get("height_feet").toString());
                    }
                    if(map.get("height_inch")!=null){
                        heightInch.setText(map.get("height_inch").toString());
                    }
                    if(map.get("about")!=null){
                        aboutMe.setText(map.get("about").toString());
                    }
                    if(map.get("location_base")!=null){
                       location.setText(map.get("location_base").toString());
                    }
                    if(map.get("highest_edu")!=null){
                        edu_spinner.setSelection(Integer.parseInt(map.get("highest_edu").toString()));
                    }
                    if(map.get("current_profession")!=null){
                       prof_spinner.setSelection(Integer.parseInt(map.get("current_profession").toString()));
                    }

                    if(map.get("traits")!=null){
                        String traits[] = map.get("traits").toString().split(",");
                        for(int i=0; i<traits.length; ++i){
                            for(int j=0; j<personalityTraits.size(); ++j){
                                if(personalityTraits.get(j).getTrait().equals(traits[i])){
                                    personalityTraits.get(j).setChosen(true);
                                }
                            }
                        }
                        rvTraits.setHasFixedSize(true);
                        //rvTraits.setLayoutManager(new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false));
                        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
                        rvTraits.setLayoutManager(gridLayoutManager);
                        rvTraits.setAdapter(new PersonalityTraitsAdapter(getApplicationContext(),personalityTraits, EditProfileInfo.this));
                    }

//                    if(map.get("profileImageUrl")!=null){
//                        profileImages.add(map.get("profileImageUrl").toString());
//                    }
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
//
//                    profileImagesPager.setAdapter(new ProfileImagesAdapter(getApplicationContext() ,profileImages));
//                    pagerIndicator.setupWithViewPager(profileImagesPager,true);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void setPersonalityTraits() {
        String values[] = {"ambitious", "confident","dependable","dominant","energetic","enthusiastic","impatient","impulsive","insecure",
                "naive","objective","open minded","optimistic","outgoing","passive","self centered","sensitive","tolerant","mature","honest",
                "loyal","independent","empathetic","humorous","affectionate","integrity","consistent","kind","curious","trustworthy","friendly"};

        for(int i=0;i<values.length;++i){
            personalityTraits.add(new PersonalityTraits(values[i],false));
        }
        rvTraits.setHasFixedSize(true);
        //rvTraits.setLayoutManager(new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false));
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
        rvTraits.setLayoutManager(gridLayoutManager);
        rvTraits.setAdapter(new PersonalityTraitsAdapter(getApplicationContext(),personalityTraits, EditProfileInfo.this));
    }

    @Override
    public void onBackPressed() {
        if(isAllFieldsUpdated){
            startActivity(new Intent(EditProfileInfo.this,MainActivity.class));
        }
        //super.onBackPressed();
    }

    public void initSpinner(){
        ArrayAdapter<String> adapterProf = new ArrayAdapter<String>(this,
                R.layout.spinner_item, getResources()
                .getStringArray(R.array.prof));
        ArrayAdapter<String> adapterEdu = new ArrayAdapter<String>(this,
                R.layout.spinner_item, getResources()
                .getStringArray(R.array.edu));

        adapterProf.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterEdu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        prof_spinner.setAdapter(adapterProf);
        prof_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        edu_spinner.setAdapter(adapterEdu);
        edu_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    public void update(){

        UserProfile = new HashMap();

        if(heightFeet.getText().toString().isEmpty()||heightInch.getText().toString().isEmpty()){
            heightText.setTextColor(Color.RED);
            if(heightFeet.getText().toString().isEmpty()){
                heightFeet.setHintTextColor(Color.RED);
            }
            if(heightInch.getText().toString().isEmpty()){
                heightInch.setHintTextColor(Color.RED);
            }
            isAllFieldsClear = false;
            popToast(null);
        }
        else {
            isAllFieldsClear = true;
        }

        if(isAllFieldsClear){

            String traits = "";

            for(int i=0; i<personalityTraits.size();++i){
                if(personalityTraits.get(i).getChosen()) {
                    if(traits.equals(""))
                        traits += personalityTraits.get(i).getTrait();
                    else
                        traits = traits + "," + personalityTraits.get(i).getTrait();
                }
            }

            if(profileName.getText().toString().isEmpty()){
                UserProfile.put("name",user.getDisplayName());}
            else{
                UserProfile.put("name",profileName.getText().toString());
            }
            UserProfile.put("about",aboutMe.getText().toString());
            UserProfile.put("height_feet",heightFeet.getText().toString());
            UserProfile.put("height_inch",heightInch.getText().toString());
            UserProfile.put("traits",traits);
//            UserProfile.put("location",location.getText().toString());
            UserProfile.put("current_profession",String.valueOf(prof_spinner.getSelectedItemId()));
            UserProfile.put("highest_edu",String.valueOf(edu_spinner.getSelectedItemId()));

            ref.child("Users").child(user.getUid()).updateChildren(UserProfile);

            isAllFieldsUpdated = true;
            startActivity(new Intent(EditProfileInfo.this,MainActivity.class));
            SharedPreferences.Editor editor = getSharedPreferences("UPDATED", MODE_PRIVATE).edit();
            editor.putBoolean("isUPDATED",true);
            editor.apply();
        }

    }

//    public void initUI(){
//        ref = ref.child(user.getUid());
//        ref.child("name").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if(dataSnapshot.getValue()!=null) {
//                    profileName.setText(dataSnapshot.getValue().toString());
//                    isAllFieldsUpdated = true;
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//
//
//        ref.child("current_profession").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if(dataSnapshot.getValue()!=null) {
//                    //spinner display
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//        ref.child("highest_edu").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if(dataSnapshot.getValue()!=null) {
//                //spinner display
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//        ref.child("height_feet").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if(dataSnapshot.getValue()!=null) {
//                heightFeet.setText(dataSnapshot.getValue().toString());
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//        ref.child("height_inch").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if(dataSnapshot.getValue()!=null) {
//                heightInch.setText(dataSnapshot.getValue().toString());
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//        ref.child("about").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if(dataSnapshot.getValue()!=null) {
//                aboutMe.setText(dataSnapshot.getValue().toString());
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }

    public void popToast(String s){
        if(s == null) {
            Toast.makeText(EditProfileInfo.this, "Please enter all required fields", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(EditProfileInfo.this, s, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemChange(Vector<PersonalityTraits> traits) {
        personalityTraits=traits;
    }
}
