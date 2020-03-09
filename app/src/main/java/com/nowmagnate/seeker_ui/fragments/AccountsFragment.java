package com.nowmagnate.seeker_ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nowmagnate.seeker_ui.AddEditProfileImages;
import com.nowmagnate.seeker_ui.ChangePlans;
import com.nowmagnate.seeker_ui.EditProfileInfo;
import com.nowmagnate.seeker_ui.LoginRegister;
import com.nowmagnate.seeker_ui.MainActivity;
import com.nowmagnate.seeker_ui.R;
import com.nowmagnate.seeker_ui.ReferEarn;
import com.nowmagnate.seeker_ui.Settings;
import com.nowmagnate.seeker_ui.VIPMember;
import com.nowmagnate.seeker_ui.Verification;
import com.nowmagnate.seeker_ui.WhoLikesYou;

import java.util.Calendar;

public class AccountsFragment extends Fragment {

    //Profile Card UI
    private ImageView profileImage, verificationImage,
                        editProfileImage , editProfileName,
                        blurImg;
    private TextView profileName,profileAge;
    private TextView datingText;
    private TextView streetText;

    //Cards
    private  CardView settingsCard,
                        whoLikesYouCard , logOutCard,
                            referEarnCard, verifyProfileImageCard,
                                vipMemberCard,changePlansCard;

    private Context c;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();

    private GoogleSignInClient mGoogleSignInClient;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("seeker-378eb");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        profileImage = view.findViewById(R.id.accountImage);
        profileAge = view.findViewById(R.id.accountAge);
        profileName = view.findViewById(R.id.accountName);
        verificationImage = view.findViewById(R.id.verifiedImage);
        editProfileImage = view.findViewById(R.id.edit_profile_image);
        editProfileName = view.findViewById(R.id.edit_profile_name);
        blurImg = view.findViewById(R.id.blurImg);
        datingText = view.findViewById(R.id.dating_text);
        streetText = view.findViewById(R.id.street_text);
        changePlansCard = view.findViewById(R.id.change_plans_card);

        settingsCard = view.findViewById(R.id.settings_card);
        whoLikesYouCard = view.findViewById(R.id.who_likes_you_card);
        logOutCard = view.findViewById(R.id.log_out_card);
        referEarnCard = view.findViewById(R.id.refer_earn_card);
        verifyProfileImageCard = view.findViewById(R.id.verify_dp_card);
        vipMemberCard = view.findViewById(R.id.vip_member_card);

        datingText.setVisibility(View.GONE);
        streetText.setText("ACCOUNT");

        initUI();

        settingsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableClick();
                startActivity(new Intent(getContext(), Settings.class));
            }
        });

        editProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableClick();
                startActivity(new Intent(getContext() , AddEditProfileImages.class));
            }
        });

        editProfileName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableClick();
                startActivity(new Intent(getContext(), EditProfileInfo.class));
            }
        });

        verifyProfileImageCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableClick();
                startActivity(new Intent(getContext(), Verification.class));
            }
        });

        vipMemberCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableClick();
                startActivity(new Intent(getContext(), VIPMember.class));
            }
        });

        whoLikesYouCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableClick();
                startActivity(new Intent(getContext(), WhoLikesYou.class));
            }
        });

        referEarnCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableClick();
                startActivity(new Intent(getContext(), ReferEarn.class));
            }
        });

        logOutCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });

        changePlansCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableClick();
                startActivity(new Intent(getContext(), ChangePlans.class));
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        settingsCard.setClickable(true);
        whoLikesYouCard.setClickable(true);
        logOutCard.setClickable(true);
        referEarnCard.setClickable(true);
        verifyProfileImageCard.setClickable(true);
        editProfileName.setClickable(true);
        editProfileImage.setClickable(true);
        vipMemberCard.setClickable(true);
        changePlansCard.setClickable(true);
    }


    public void disableClick(){
        settingsCard.setClickable(false);
        whoLikesYouCard.setClickable(false);
        logOutCard.setClickable(false);
        referEarnCard.setClickable(false);
        verifyProfileImageCard.setClickable(false);
        editProfileName.setClickable(false);
        editProfileImage.setClickable(false);
        vipMemberCard.setClickable(false);
        changePlansCard.setClickable(false);
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

        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);

        mGoogleSignInClient.signOut().addOnCompleteListener(((MainActivity)getContext()).getMainActivity(),
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
        ((MainActivity)getContext()).signOut();
        startActivity(new Intent(getContext(),LoginRegister.class));
        ((MainActivity)getContext()).finish();
    }

    public void initUI() {
        ref = ref.child(user.getUid());
        ref.child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    profileName.setText(dataSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ref.child("dob").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    String date = dataSnapshot.getValue().toString();

                    int d = Integer.parseInt(date.substring(0,2));
                    int m = Integer.parseInt(date.substring(3,5));
                    int y = Integer.parseInt(date.substring(6));
                    String age = calculateAge(y,m,d);
                    profileAge.setText(", "+age);

                    Log.i("pop", date.substring(0,2) +" "+
                            date.substring(3,5)+" "+
                            date.substring(6));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
}
