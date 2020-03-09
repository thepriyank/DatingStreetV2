package com.nowmagnate.seeker_ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nowmagnate.seeker_ui.R;

public class PlanAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private OnPlanListener listener;
    private TextView sticker,price;
    private Context c;

    public PlanAdapter(OnPlanListener listener, Context context) {
        this.listener = listener;
        this.c = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.plan_item_layout, parent, false);
        return new PlanViewHolder(view, listener,c);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder h, int position) {
        ((PlanViewHolder)h).onBind();
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


}
