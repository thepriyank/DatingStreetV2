package com.nowmagnate.seeker_ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nowmagnate.seeker_ui.MainActivity;
import com.nowmagnate.seeker_ui.ProfileDetail;
import com.nowmagnate.seeker_ui.R;

import java.util.HashMap;
import java.util.Map;

public class CardsFragment extends Fragment {

    //UI
    private FloatingActionButton rejectFAB, acceptFAB,superFAB,rewindFAB;

    private CardView profileCard;

    private TextView datingText;
    private TextView streetText;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    boolean isSuperLikeClick, rewindClick ;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("seeker-378eb");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cards, container, false);

        rejectFAB = view.findViewById(R.id.rejectFab);
        acceptFAB = view.findViewById(R.id.acceptFab);
        superFAB = view.findViewById(R.id.superFab);
        rewindFAB = view.findViewById(R.id.rewindFab);
        datingText = view.findViewById(R.id.dating_text);
        streetText = view.findViewById(R.id.street_text);

        profileCard = view.findViewById(R.id.profileCard);

        profileCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileCard.setClickable(false);
                startActivity(new Intent(getContext(), ProfileDetail.class));
            }
        });

        superFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isSuperLikeClick = true;
                ref.child(user.getUid()).child("activePlan").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getValue()==null){
                            //add the dialog
                        }
                        else{
                            if(!dataSnapshot.getValue().toString().equals("basic")){
                                ref.child(user.getUid()).child("superLikes").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.getValue()==null){
                                            Map s = new HashMap();
                                            s.put("superLikes",4);
                                            ref.child(user.getUid()).updateChildren(s);
                                            Toast.makeText(getContext(),4+" Superlikes left.",Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            if(Integer.parseInt(dataSnapshot.getValue().toString())!=0){

                                                if(isSuperLikeClick) {
                                                    isSuperLikeClick = false;
                                                    Map s = new HashMap();
                                                    s.put("superLikes", Integer.parseInt(dataSnapshot.getValue().toString()) - 1);
                                                    ref.child(user.getUid()).updateChildren(s);
                                                    Toast.makeText(getContext(), Integer.parseInt(dataSnapshot.getValue().toString()) - 1 + " Superlikes left.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                            else{
                                                isSuperLikeClick = false;
                                                Toast.makeText(getContext(), "You used all Superlikes!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                            else {
                                ((MainActivity)getContext()).showPayLayout(true);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        rewindFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rewindClick = true;
                ref.child(user.getUid()).child("activePlan").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                       if(dataSnapshot.getValue()!=null) {
                           if(rewindClick) {
                               rewindClick=false;
                               if (dataSnapshot.getValue().toString().equals("basic")) {
                                   ((MainActivity) getContext()).showPayLayout(true);
                               } else {
                                   Toast.makeText(getContext(), "Rewind Clicked!", Toast.LENGTH_SHORT).show();
                               }
                           }
                       }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        profileCard.setClickable(true);
    }
}
