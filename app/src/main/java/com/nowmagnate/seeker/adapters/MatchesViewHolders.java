package com.nowmagnate.seeker.adapters;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.nowmagnate.seeker.ProfileDetail;
import com.nowmagnate.seeker.R;

public class MatchesViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView  mMatchName, mViewUser, mChatUser;
    public ImageView mMatchImage;
    String mMatchId="";
    public MatchesViewHolders(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        mMatchName = itemView.findViewById(R.id.matchName);
        mViewUser = itemView.findViewById(R.id.tvViewUser);
        mViewUser = itemView.findViewById(R.id.tvViewUser);
        mChatUser = itemView.findViewById(R.id.tvChatUser);

        mMatchImage = itemView.findViewById(R.id.matchImage);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tvChatUser:
//                Intent intent = new Intent(view.getContext(), ChatActivity.class);
//                Bundle b = new Bundle();
//                b.putString("matchId", mMatchId.getText().toString());
//                intent.putExtras(b);
//                view.getContext().startActivity(intent);
                break;
            case R.id.tvViewUser:
                Intent viewIntent = new Intent(view.getContext(), ProfileDetail.class);
                viewIntent.putExtra("userid", mMatchId);
                view.getContext().startActivity(viewIntent);
                break;

        }

    }
}
