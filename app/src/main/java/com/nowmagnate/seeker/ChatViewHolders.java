package com.nowmagnate.seeker;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class ChatViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView mMessage;
    public LinearLayout mContainer;
    public CardView mChatCard;
    public ChatViewHolders(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        mMessage = itemView.findViewById(R.id.message);
        mContainer = itemView.findViewById(R.id.container);
        mChatCard = itemView.findViewById(R.id.cvChatcard);
    }

    @Override
    public void onClick(View view) {
    }
}
