package com.nowmagnate.seeker_ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nowmagnate.seeker_ui.ChangePlans;
import com.nowmagnate.seeker_ui.MainActivity;
import com.nowmagnate.seeker_ui.R;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class PlanViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    OnPlanListener listener;
    CardView baseCard;
    LinearLayout contentLayout;
    Context c;
    TextView sticker,price,buy;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("planIndex");
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    String SelectedPlan;



    public PlanViewHolder(@NonNull View itemView, OnPlanListener listener,Context c) {
        super(itemView);
        this.listener = listener;
        this.baseCard = itemView.findViewById(R.id.baseCard);
        this.contentLayout = itemView.findViewById(R.id.content_layout);
        this.sticker = itemView.findViewById(R.id.sticker);
        this.price = itemView.findViewById(R.id.price);
        this.buy = itemView.findViewById(R.id.buy);
        this.c = c;
        itemView.setOnClickListener(this);
    }



    @SuppressLint({"RestrictedApi", "ClickableViewAccessibility"})
    public void onBind(){
            ref.child(String.valueOf(getAdapterPosition())).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue()==null){

                    }
                    else{
                        final String planName = dataSnapshot.getValue().toString();
                        sticker.setText(planName.toUpperCase());
                        SelectedPlan = planName;
                        final DatabaseReference ref = database.getReference("plans");
                        ref.child(planName).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.getValue()==null){
                                }else{
                                    ref.child(planName).child("price").addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.getValue()==null){

                                            }
                                            else{
                                                price.setText(dataSnapshot.getValue().toString().toUpperCase());
                                                Log.i("price", "called " + dataSnapshot.getValue().toString());
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                    final int childCount = (int) dataSnapshot.getChildrenCount();
                                    Log.i("addView", "called " + childCount);
                                    for(int i = 0; i < dataSnapshot.getChildrenCount();i++){
                                        ref.child(planName).child(String.valueOf(i)).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if(dataSnapshot.getValue()==null){

                                                }
                                                else {
                                                    if(contentLayout.getChildCount()<((childCount*2)-2)) {
                                                        addView(dataSnapshot.getValue().toString());
                                                        Log.i("addView", "called " + contentLayout.getChildCount());
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

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            buy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(SelectedPlan!=null){
                        Calendar cad = Calendar.getInstance();
                        cad.add(Calendar.DATE,30);
                        Log.i("popGet", cad.getTime().toString().substring(0,10));
                        Map plan = new HashMap();
                        plan.put("activePlan",SelectedPlan);

                        Map endPlan = new HashMap();
                        endPlan.put("endPlan",cad.getTime().toString().substring(0,10));
                        DatabaseReference ref = database.getReference("seeker-378eb").child(user.getUid());
                        ref.updateChildren(plan);
                        ref.updateChildren(endPlan);
                        ref.child("superLikes").setValue(5);
                        ((ChangePlans)c).popToast("Activated plan " + SelectedPlan.toUpperCase());
                    }
                }
            });
    }

    public void addView(String s){
        TextView t = new TextView(c);
        t.setText(s);
        t.setGravity(Gravity.CENTER);
        t.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(10,10,10,10);
        t.setLayoutParams(layoutParams);
        View v = new View(c);
        v.setBackgroundColor(Color.parseColor("#eeeeee"));
        v.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2));
        contentLayout.addView(t);
        contentLayout.addView(v);
    }


    @Override
    public void onClick(View view) {
        listener.onPlanClick(getAdapterPosition());
    }
}
