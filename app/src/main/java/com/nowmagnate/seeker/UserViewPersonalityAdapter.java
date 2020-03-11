package com.nowmagnate.seeker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Vector;

public class UserViewPersonalityAdapter extends RecyclerView.Adapter<UserViewPersonalityAdapter.ViewHolder  > {

    Context context;
    Vector<PersonalityTraits> traits;

    public UserViewPersonalityAdapter(Context context, Vector<PersonalityTraits> traits) {
        this.context = context;
        this.traits = traits;
    }

    @NonNull
    @Override
    public ViewHolder  onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.personality_trait_item, viewGroup, false);
        return new ViewHolder (view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder  viewHolder, final int position) {
              viewHolder.tvTrait.setText(traits.get(position).getTrait());

                    viewHolder.tvTrait.setBackgroundResource(R.drawable.fancy_button);
                    viewHolder.tvTrait.setTextColor(context.getResources().getColor(R.color.colorWhite));

    }

    @Override
    public int getItemCount() {
        return traits.size();
    }

    public class ViewHolder  extends RecyclerView.ViewHolder {

        TextView tvTrait;
        public ViewHolder (@NonNull View itemView) {
            super(itemView);
            tvTrait= itemView.findViewById(R.id.tvTrait);
        }
    }

}
