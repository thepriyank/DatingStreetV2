package com.nowmagnate.seeker.Chat;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.nowmagnate.seeker.R;

public class ChatViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView mMessage;
    public LinearLayout mContainer , mChatCard;
    public ChatViewHolders(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        mMessage = itemView.findViewById(R.id.message);
        mContainer = itemView.findViewById(R.id.container);
        mChatCard = itemView.findViewById(R.id.llChatcard);
    }

    @Override
    public void onClick(View view) {
    }
}
