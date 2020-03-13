package com.nowmagnate.seeker.adapters;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.nowmagnate.seeker.ChatActivity;
import com.nowmagnate.seeker.R;

public class ChatListViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView mMatchName, mChatContent;
    public ImageView mMatchImage;
    String mMatchId="";
    public ChatListViewHolders(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

//        mMatchId = itemView.findViewById(R.id.MatchId);
        mMatchName = itemView.findViewById(R.id.MatchName);
        mChatContent = itemView.findViewById(R.id.chatMessageDescription);

        mMatchImage = itemView.findViewById(R.id.MatchImage);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(view.getContext(), ChatActivity.class);
        Bundle b = new Bundle();
        b.putString("matchId", mMatchId);
        intent.putExtras(b);
        view.getContext().startActivity(intent);
    }
}
