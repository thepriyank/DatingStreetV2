package com.nowmagnate.seeker_ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class basicInfoActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{


    private EditText phone;
    private ImageView dateOfBirth;
    private TextView dateOfBirthText,maleText,femaleText,genderText;

    String gen;
    private Calendar calendar;
    int year,day,month;
    private CardView update;

    boolean isAllFieldsClear = true;

    Map gender,dob,
            phone_base;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("seeker-378eb");
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_info);

        dateOfBirth = findViewById(R.id.btn_select_date);
        dateOfBirthText = findViewById(R.id.selected_date);
        phone = findViewById(R.id.phone_editText);
        maleText = findViewById(R.id.male_text);
        femaleText = findViewById(R.id.female_text);
        genderText = findViewById(R.id.gender_text);
        update = findViewById(R.id.update);

        GradientStatusBar.setStatusBarGradiant(this);

        dateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateDialog();
            }
        });

        dateOfBirthText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateDialog();
            }
        });
        maleText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                maleText.setBackground(getResources().getDrawable(R.drawable.edittext_pink_left_round_background));
                femaleText.setBackground(getResources().getDrawable(R.drawable.edittext_grey_right_round_background));
                gen = "male";
            }
        });
        femaleText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                femaleText.setBackground(getResources().getDrawable(R.drawable.edittext_pink_right_round_background));
                maleText.setBackground(getResources().getDrawable(R.drawable.edittext_grey_left_round_background));
                gen = "female";
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
    public void onDateSet(DatePicker datePicker, int ye, int mont, int dayOfMonth) {

        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, ye);
        c.set(Calendar.MONTH, mont);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

        day = dayOfMonth;
        month = mont+1;
        year = ye;

        String d;
        String m;
        String y;

        if(day <10){
            d = "0"+day;
        }
        else{
            d=String.valueOf(day);
        }


        if(month <10){
            m = "0"+(month);
        }
        else{
            m = String.valueOf(month);
        }

        if(Integer.parseInt(calculateAge(year,month,year)) < 13){
            dateOfBirthText.setTextColor(Color.RED);
            dateOfBirthText.setText("You need to be atleast 13");
        }
        else {
            dateOfBirthText.setTextColor(getResources().getColor(R.color.TextColor));
            dateOfBirthText.setText(d + "/" + m + "/" + String.valueOf(year));
        }
    }

    public void update(){

        dob = new HashMap();
        gender = new HashMap();
        phone_base = new HashMap();

        if(dateOfBirthText.getText().toString().isEmpty()||dateOfBirthText.getText().toString().equals("You need to be atleast 13")){
            dateOfBirthText.setHintTextColor(Color.RED);
            isAllFieldsClear = false;
            popToast(null);
        }
        else {
            isAllFieldsClear = true;
        }
        if(gen==null||gen.isEmpty()){
            genderText.setTextColor(Color.RED);
            isAllFieldsClear = false;
            popToast(null);
        }
        else {
            isAllFieldsClear = true;
        }
        if(phone.getText().toString().isEmpty()){
            phone.setHintTextColor(Color.RED);
            isAllFieldsClear = false;
            popToast(null);
        }
        else {
            isAllFieldsClear = true;
        }

        if(!phone.getText().toString().isEmpty()){
            if(gen!=null||!gen.isEmpty()){
                if(!dateOfBirthText.getText().toString().isEmpty()){
                    if(!dateOfBirthText.getText().toString().equals("You need to be atleast 13")){
                    gender.put("gender", gen);
                    phone_base.put("phone", phone.getText().toString());
                    dob.put("dob", dateOfBirthText.getText());
                    ref = ref.child(user.getUid());
                    ref.updateChildren(dob);
                    ref.updateChildren(gender);
                    ref.updateChildren(phone_base);

                    finish();

                    }
                }
            }
        }



    }

    public void dateDialog(){
        hideKeyboard();

        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        DialogFragment datePicker = new DatePickerFragment(year, month, day);
        datePicker.show(getSupportFragmentManager(), "date picker");
    }

    public void popToast(String s){
        if(s == null) {
            Toast.makeText(basicInfoActivity.this, "Please enter all required fields", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(basicInfoActivity.this, s, Toast.LENGTH_SHORT).show();
        }
    }

    public String calculateAge(int year, int month, int day){
        Calendar d_o_b = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        d_o_b.set(year, month, day);

        int age = today.get(Calendar.YEAR) - d_o_b.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < d_o_b.get(Calendar.DAY_OF_YEAR)){
            age--;
        }


        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }

    public void hideKeyboard() {
        try {
            // use application level context to avoid unnecessary leaks.
            InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            assert inputManager != null;
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initUI(){
        ref.child("dob").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null) {
                    dateOfBirthText.setText(dataSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ref.child("gender").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null) {
                    gen = dataSnapshot.getValue().toString();
                    if(gen.equals("male")){
                        maleText.setBackground(getResources().getDrawable(R.drawable.edittext_pink_left_round_background));
                        femaleText.setBackground(getResources().getDrawable(R.drawable.edittext_grey_right_round_background));
                    }else{
                        maleText.setBackground(getResources().getDrawable(R.drawable.edittext_grey_left_round_background));
                        femaleText.setBackground(getResources().getDrawable(R.drawable.edittext_pink_right_round_background));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        ref.child("phone").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null) {
                    phone.setText(dataSnapshot.getValue().toString());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
