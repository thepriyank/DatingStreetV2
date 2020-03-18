package com.nowmagnate.seeker.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import com.nowmagnate.seeker.BuildConfig;
import com.nowmagnate.seeker.Cards.arrayAdapter;
import com.nowmagnate.seeker.Cards.cards;
import com.nowmagnate.seeker.EditProfileInfo;
import com.nowmagnate.seeker.MainActivity;
import com.nowmagnate.seeker.ProfileDetail;
import com.nowmagnate.seeker.R;
import com.nowmagnate.seeker.ThisApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardsFragment extends Fragment {

    //UI
    private FloatingActionButton rejectFAB, acceptFAB,superFAB,rewindFAB;

//    private CardView profileCard;
    private TextView datingText;
    private TextView streetText;
    private String currentUId="";
    Boolean isEmpty=false;
    Integer CurrentSuperLikes;

    int countCards =0;
    List<cards> rowItems;
    private com.nowmagnate.seeker.Cards.arrayAdapter arrayAdapter;
    LinearLayout llNoCards;
    SwipeFlingAdapterView swipeView;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    boolean isSuperLikeClick, rewindClick ;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference().child("Users");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cards, container, false);

        swipeView = view.findViewById(R.id.frame);
        llNoCards = view.findViewById(R.id.llNoCardsContent);
        rejectFAB = view.findViewById(R.id.rejectFab);
        acceptFAB = view.findViewById(R.id.acceptFab);
        superFAB = view.findViewById(R.id.superFab);
        rewindFAB = view.findViewById(R.id.rewindFab);
        datingText = view.findViewById(R.id.dating_text);
        streetText = view.findViewById(R.id.street_text);


        llNoCards.setVisibility(View.VISIBLE);
        swipeView.setVisibility(View.GONE);
        currentUId = mAuth.getCurrentUser().getUid();

        acceptFAB.setVisibility(View.GONE);
        rejectFAB.setVisibility(View.GONE);
        rewindFAB.setVisibility(View.GONE);
        superFAB.setVisibility(View.GONE);

        getUserInfo();
        checkForUpdates();
        checkUserSex();

        rowItems = new ArrayList<>();
        arrayAdapter = new arrayAdapter(getActivity(), R.layout.card_item, rowItems );

        SwipeFlingAdapterView flingContainer =  view.findViewById(R.id.frame);

        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                Log.d("LIST", "removed object!");
                rowItems.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                cards obj = (cards) dataObject;
                String userId = obj.getUserId();
                ref.child(userId).child("connections").child("nope").child(currentUId).setValue(true);
                //Toast.makeText(getActivity(), "Left", Toast.LENGTH_SHORT).show();
                countCards++;
                if(countCards%10==0) {
//                    displayInterstitialAd();
                }

            }

            @Override
            public void onRightCardExit(Object dataObject) {
                cards obj = (cards) dataObject;
                String userId = obj.getUserId();
                ref.child(userId).child("connections").child("yeps").child(currentUId).setValue(true);
                isConnectionMatch(userId);
                //Toast.makeText(getActivity(), "Right", Toast.LENGTH_SHORT).show();
                countCards++;
                if(countCards%10==0) {
//                    displayInterstitialAd();
                }

            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                if(itemsInAdapter==0 && !isEmpty){
                    acceptFAB.setVisibility(View.GONE);
                    rejectFAB.setVisibility(View.GONE);
                    rewindFAB.setVisibility(View.GONE);
                    superFAB.setVisibility(View.GONE);
                    isEmpty=true;
                    llNoCards.setVisibility(View.VISIBLE);
                    swipeView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
            }
        });

        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Intent showUserIntent = new Intent(getActivity(), ProfileDetail.class);
                showUserIntent.putExtra("userid",((cards)dataObject).getUserId());
                startActivityForResult(showUserIntent, 123);
            }
        });

        superFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isSuperLikeClick = true;
                if(CurrentSuperLikes>0) {
                    ref.child(user.getUid()).child("activePlan").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() == null) {
                                //add the dialog
                            } else {
                                if (!dataSnapshot.getValue().toString().equals("basic")) {
                                    ref.child(user.getUid()).child("superLikes").addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.getValue() == null) {
                                                Map s = new HashMap();
                                                s.put("superLikes", 4);
                                                ref.child(user.getUid()).updateChildren(s);
                                                Toast.makeText(getContext(), 4 + " Superlikes left.", Toast.LENGTH_SHORT).show();
                                            } else {
                                                if (Integer.parseInt(dataSnapshot.getValue().toString()) != 0) {

                                                    if (isSuperLikeClick) {
                                                        isSuperLikeClick = false;
                                                        Map s = new HashMap();
                                                        s.put("superLikes", Integer.parseInt(dataSnapshot.getValue().toString()) - 1);
                                                        ref.child(user.getUid()).updateChildren(s);
                                                        Toast.makeText(getContext(), Integer.parseInt(dataSnapshot.getValue().toString()) - 1 + " Superlikes left.", Toast.LENGTH_SHORT).show();
                                                    }
                                                } else {
                                                    isSuperLikeClick = false;
                                                    Toast.makeText(getContext(), "You used all Superlikes!", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                } else {
                                    ((MainActivity) getContext()).showPayLayout(true);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
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


        rejectFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cards obj = rowItems.get(0);
                String userId = obj.getUserId();
                ref.child(userId).child("connections").child("nope").child(currentUId).setValue(true);
                rowItems.remove(0);
                arrayAdapter.notifyDataSetChanged();
                countCards++;
                if(countCards%10==0)
                    displayInterstitialAd();
            }
        });

        acceptFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cards obj = rowItems.get(0);
                String userId = obj.getUserId();
                ref.child(userId).child("connections").child("yeps").child(currentUId).setValue(true);
                isConnectionMatch(userId);
                rowItems.remove(0);
                arrayAdapter.notifyDataSetChanged();
                countCards++;
                if(countCards%10==0)
                    displayInterstitialAd();
            }
        });

        superFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rowItems.size()>0) {
                    isSuperLikeClick = true;
                    if(CurrentSuperLikes>0) {
                        ref.child(user.getUid()).child("activePlan").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getValue() == null) {
                                    //add the dialog
                                } else {
                                    if (!dataSnapshot.getValue().toString().equals("basic")) {
                                        ref.child(user.getUid()).child("superLikes").addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.getValue() == null) {
                                                    Map s = new HashMap();
                                                    s.put("superLikes", 4);
                                                    ref.child(user.getUid()).updateChildren(s);
                                                    Toast.makeText(getContext(), 4 + " Superlikes left.", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    if (Integer.parseInt(dataSnapshot.getValue().toString()) != 0) {

                                                        if (isSuperLikeClick) {
                                                            isSuperLikeClick = false;
                                                            Map s = new HashMap();
                                                            s.put("superLikes", Integer.parseInt(dataSnapshot.getValue().toString()) - 1);
                                                            ref.child(user.getUid()).updateChildren(s);
                                                            Toast.makeText(getContext(), Integer.parseInt(dataSnapshot.getValue().toString()) - 1 + " Superlikes left.", Toast.LENGTH_SHORT).show();
                                                        }
                                                    } else {
                                                        isSuperLikeClick = false;
                                                        Toast.makeText(getContext(), "You used all Superlikes!", Toast.LENGTH_SHORT).show();
                                                    }
                                                    cards obj = rowItems.get(0);
                                                    String userId = obj.getUserId();
                                                    ref.child(userId).child("connections").child("yeps").child(currentUId).setValue(true);
                                                    ref.child(userId).child("connections").child("super").child(currentUId).setValue(true);
                                                    ref.child(userId).child("superLikes").setValue(--CurrentSuperLikes);

                                                    isConnectionMatch(userId);
                                                    rowItems.remove(0);
                                                    arrayAdapter.notifyDataSetChanged();
                                                    countCards++;
                                                    if (countCards % 10 == 0)
                                                        displayInterstitialAd();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    } else {
                                        ((MainActivity) getContext()).showPayLayout(true);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }
        });

        return view;
    }


    private void getUserInfo() {
        ref.child(currentUId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){

                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.child("UserInfo").getValue();
                    if(dataSnapshot.child("UserInfo").exists() && map.get("profilecreated")!=null){
                        if(map.get("profilecreated").equals("NO")){
                            // Create a dialog to ask for profile creation to win 5 COINS
                            AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
                            builder.setMessage("Complete Your Profile with Details and Image, and WIN 5 Free COINS")
                                    .setTitle("Upload Image")
                                    .setCancelable(false)
                                    .setPositiveButton("Let's Do It", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent settingIntent = new Intent(getActivity(), EditProfileInfo.class);
                                            startActivity(settingIntent);
                                        }
                                    });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }else{
                           CurrentSuperLikes = Integer.parseInt(dataSnapshot.child("UserInfo").child("superLikes").getValue().toString());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void checkForUpdates() {
        DatabaseReference updateDb= FirebaseDatabase.getInstance().getReference().child("Updates");
        final Float currentVersion = Float.parseFloat(BuildConfig.VERSION_NAME);
        updateDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Float latestVersion = Float.parseFloat(dataSnapshot.child("latestVersion").getValue().toString());
                    Toast.makeText(getActivity(), "Version:"+latestVersion, Toast.LENGTH_SHORT).show();
                    if (latestVersion>currentVersion){
                        Intent updateIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(ThisApplication.PlayStore_URL));
                        startActivity(updateIntent);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void displayInterstitialAd() {
//        if (interstitialAd.isLoaded()) {
//            interstitialAd.show();
//        }
    }

    private void isConnectionMatch(String userId) {
        DatabaseReference currentUserConnectionsDb = ref.child(currentUId).child("connections").child("yeps").child(userId);
        currentUserConnectionsDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Toast.makeText(getActivity(), "new Connection", Toast.LENGTH_LONG).show();

                    String key = FirebaseDatabase.getInstance().getReference().child("Chat").push().getKey();

                    ref.child(dataSnapshot.getKey()).child("connections").child("matches").child(currentUId).child("ChatId").setValue(key);
                    ref.child(currentUId).child("connections").child("matches").child(dataSnapshot.getKey()).child("ChatId").setValue(key);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private String userSex;
    private String oppositeUserSex;

    public void checkUserSex(){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference userDb = ref.child(user.getUid()).child("UserInfo");
        userDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    if (dataSnapshot.child("gender").getValue() != null){
                        userSex = dataSnapshot.child("gender").getValue().toString();
                        switch (userSex){
                            case "male":
                                oppositeUserSex = "female";
                                break;
                            case "female":
                                oppositeUserSex = "male";
                                break;
                        }
                        getOppositeSexUsers();
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getOppositeSexUsers(){
        ref.addChildEventListener(new ChildEventListener() {
//&& !dataSnapshot.child("connections").child("nope").hasChild(currentUId)
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.child("UserInfo").child("gender").getValue() != null) {
                    if (dataSnapshot.exists()
                            && !dataSnapshot.child("connections").child("yeps").hasChild(currentUId)
                            && dataSnapshot.child("UserInfo").child("gender").getValue().toString().equals(oppositeUserSex)) {
                        String profileImageUrl = "default";
                        if (!dataSnapshot.child("UserInfo").child("profileImageUrl").getValue().equals("default")) {
                            profileImageUrl = dataSnapshot.child("UserInfo").child("profileImageUrl").getValue().toString();
                        }

                        String initials[] = dataSnapshot.child("name").getValue().toString().split(" ");
                        String name = "";
                        for(int i=0; i<initials.length;i++){
                            name+= initials[i].substring(0,1);
                        }

                        cards item = new cards(dataSnapshot.getKey(),
                                name, profileImageUrl);
                        rowItems.add(item);
                        arrayAdapter.notifyDataSetChanged();
                        swipeView.setVisibility(View.VISIBLE);
                        llNoCards.setVisibility(View.GONE);
                        acceptFAB.setVisibility(View.VISIBLE);
                        rejectFAB.setVisibility(View.VISIBLE);
                        rewindFAB.setVisibility(View.VISIBLE);
                        superFAB.setVisibility(View.VISIBLE);

                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });
    }

    @Override
    public void onResume() {
        super.onResume();
//        profileCard.setClickable(true);
    }
}
