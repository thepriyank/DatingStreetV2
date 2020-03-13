package com.nowmagnate.seeker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.nowmagnate.seeker.MatchesObject;
import com.nowmagnate.seeker.R;

import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListViewHolders>{
    private List<MatchesObject> matchesList;
    private Context context;


    public ChatListAdapter(List<MatchesObject> matchesList, Context context){
        this.matchesList = matchesList;
        this.context = context;
    }

    @Override
    public ChatListViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_list, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        ChatListViewHolders rcv = new ChatListViewHolders(layoutView);

        return rcv;
    }

    @Override
    public void onBindViewHolder(ChatListViewHolders holder, int position) {
        holder.mMatchId = matchesList.get(position).getUserId();
        holder.mMatchName.setText(matchesList.get(position).getName());
        if(!matchesList.get(position).getProfileImageUrl().equals("default")){
            Glide.with(context).load(matchesList.get(position).getProfileImageUrl())
                    .apply(RequestOptions.circleCropTransform()).into(holder.mMatchImage);
        }
    }

    @Override
    public int getItemCount() {
        return this.matchesList.size();
    }
}
