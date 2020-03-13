package com.nowmagnate.seeker.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nowmagnate.seeker.MatchesObject;
import com.nowmagnate.seeker.ProfileDetail;
import com.nowmagnate.seeker.R;

import java.util.List;

public class MatchesAdapter extends RecyclerView.Adapter<MatchesViewHolders>{
    private List<MatchesObject> matchesList;
    private Context context;


    public MatchesAdapter(List<MatchesObject> matchesList, Context context){
        this.matchesList = matchesList;
        this.context = context;
    }

    @Override
    public MatchesViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_matches, null, false);
        MatchesViewHolders rcv = new MatchesViewHolders(layoutView);

        return rcv;
    }

    @Override
    public void onBindViewHolder(final MatchesViewHolders holder, int position) {
        holder.mMatchId = matchesList.get(position).getUserId();
        holder.mMatchName.setText(matchesList.get(position).getName());
        if(!matchesList.get(position).getProfileImageUrl().equals("default")){
            Glide.with(context).load(matchesList.get(position).getProfileImageUrl())
                    .into(holder.mMatchImage);
        }
        holder.mViewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewIntent = new Intent(view.getContext(), ProfileDetail.class);
                viewIntent.putExtra("userid", holder.mMatchId);
                view.getContext().startActivity(viewIntent);
            }
        });
//        holder.mChatUser.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(view.getContext(), ChatActivity.class);
//                Bundle b = new Bundle();
//                b.putString("matchId", holder.mMatchId.getText().toString());
//                intent.putExtras(b);
//                view.getContext().startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return this.matchesList.size();
    }
}
