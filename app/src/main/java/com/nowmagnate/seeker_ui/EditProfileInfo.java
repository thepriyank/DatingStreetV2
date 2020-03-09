package com.nowmagnate.seeker_ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
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
import com.nowmagnate.seeker_ui.dialogs.DatePickerFragment;
import com.nowmagnate.seeker_ui.util.GradientStatusBar;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EditProfileInfo extends AppCompatActivity  {

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

    //Var


    Map name,about,
            height_feet,height_inch,location_base,
            current_profession,highest_edu;


    boolean isAllFieldsClear = true;
    boolean isAllFieldsUpdated = false;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("seeker-378eb");
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

        GradientStatusBar.setStatusBarGradiant(this);


        toolbarTitle.setText("EDIT PROFILE");
        toolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        initSpinner();

        initUI();




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
        name = new HashMap();
        about= new HashMap();
        height_feet = new HashMap();
        height_inch= new HashMap();
        location_base= new HashMap();
        current_profession = new HashMap();
        highest_edu = new HashMap();


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


            if(profileName.getText().toString().isEmpty()){
            name.put("name",user.getDisplayName());}
            else{
                name.put("name",profileName.getText().toString());
            }
            about.put("about",aboutMe.getText().toString());
            height_feet.put("height_feet",heightFeet.getText().toString());
            height_inch.put("height_inch",heightInch.getText().toString());
            current_profession.put("current_profession",prof_spinner.getSelectedItem().toString());
            highest_edu.put("highest_edu",edu_spinner.getSelectedItem().toString());

            ref.updateChildren(name);
            ref.updateChildren(about);
            ref.updateChildren(height_feet);
            ref.updateChildren(height_inch);
            ref.updateChildren(current_profession);
            ref.updateChildren(highest_edu);
            isAllFieldsUpdated = true;
            startActivity(new Intent(EditProfileInfo.this,MainActivity.class));
            SharedPreferences.Editor editor = getSharedPreferences("UPDATED", MODE_PRIVATE).edit();
            editor.putBoolean("isUPDATED",true);
            editor.apply();
        }

    }





    public void initUI(){
        ref = ref.child(user.getUid());
        ref.child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null) {
                    profileName.setText(dataSnapshot.getValue().toString());
                    isAllFieldsUpdated = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        ref.child("current_profession").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null) {
                    //spinner display
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ref.child("highest_edu").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null) {
                //spinner display
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ref.child("height_feet").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null) {
                heightFeet.setText(dataSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ref.child("height_inch").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null) {
                heightInch.setText(dataSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ref.child("about").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null) {
                aboutMe.setText(dataSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void popToast(String s){
        if(s == null) {
            Toast.makeText(EditProfileInfo.this, "Please enter all required fields", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(EditProfileInfo.this, s, Toast.LENGTH_SHORT).show();
        }
    }


}
