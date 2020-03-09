package com.nowmagnate.seeker_ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.nowmagnate.seeker_ui.BuyCoins;
import com.nowmagnate.seeker_ui.EarnCoins;
import com.nowmagnate.seeker_ui.R;
import com.nowmagnate.seeker_ui.UseCoins;

public class CoinsFragment extends Fragment {

    private TextView datingText;
    private TextView streetText;

    private CardView useCard;
    private CardView earnCard;
    private CardView buyCard;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_coins, container, false);

        datingText = view.findViewById(R.id.dating_text);
        streetText = view.findViewById(R.id.street_text);
        useCard = view.findViewById(R.id.use_card);
        earnCard = view.findViewById(R.id.earn_card);
        buyCard = view.findViewById(R.id.buy_card);

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
