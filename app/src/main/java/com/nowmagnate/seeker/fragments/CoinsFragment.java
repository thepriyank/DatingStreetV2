package com.nowmagnate.seeker.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nowmagnate.seeker.BuyCoins;
import com.nowmagnate.seeker.EarnCoins;
import com.nowmagnate.seeker.R;
import com.nowmagnate.seeker.UseCoins;

public class CoinsFragment extends Fragment {

    private CardView useCard;
    private CardView earnCard;
    private CardView buyCard;
    private TextView tvCoinsValue;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference().child("Users");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_coins, container, false);
         TextView datingText;
         TextView streetText;
        datingText = view.findViewById(R.id.dating_text);
        streetText = view.findViewById(R.id.street_text);
        useCard = view.findViewById(R.id.use_card);
        earnCard = view.findViewById(R.id.earn_card);
        buyCard = view.findViewById(R.id.buy_card);
        tvCoinsValue = view.findViewById(R.id.tvCoinsValue);

        datingText.setVisibility(View.GONE);
        streetText.setText("COINS");

        useCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableClick();
                startActivity(new Intent(getContext(), UseCoins.class));
            }
        });

        earnCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableClick();
                startActivity(new Intent(getContext(), EarnCoins.class));
            }
        });

        buyCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableClick();
                startActivity(new Intent(getContext(), BuyCoins.class));
            }
        });

        ref.child(user.getUid()).child("UserInfo").child("coins").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    tvCoinsValue.setText(dataSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        useCard.setClickable(true);
        earnCard.setClickable(true);
        buyCard.setClickable(true);
    }

    public void disableClick(){
        useCard.setClickable(false);
        earnCard.setClickable(false);
        buyCard.setClickable(false);
    }


}
