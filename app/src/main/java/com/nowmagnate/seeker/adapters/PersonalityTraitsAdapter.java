package com.nowmagnate.seeker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nowmagnate.seeker.PersonalityTraits;
import com.nowmagnate.seeker.R;

import java.util.Vector;

public class PersonalityTraitsAdapter extends RecyclerView.Adapter<PersonalityTraitsAdapter.ViewHolder  > {

    Context context;
    Vector<PersonalityTraits> traits;
    private ChooseTraits mCallback;

    public PersonalityTraitsAdapter(Context context, Vector<PersonalityTraits> traits, ChooseTraits listener) {
        this.context = context;
        this.traits = traits;
        mCallback = listener;
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
                if(traits.get(position).getChosen()){
                    viewHolder.tvTrait.setBackgroundResource(R.drawable.fancy_button);
                    viewHolder.tvTrait.setTextColor(context.getResources().getColor(R.color.colorWhite));
                }
              viewHolder.tvTrait.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {
                      if(traits.get(position).getChosen()){
                          viewHolder.tvTrait.setBackgroundResource(R.drawable.edittext_border);
                          traits.get(position).setChosen(false);
                          viewHolder.tvTrait.setTextColor(context.getResources().getColor(R.color.colorBlack));
                      }else{
                          viewHolder.tvTrait.setBackgroundResource(R.drawable.fancy_button);
                          traits.get(position).setChosen(true);
                          viewHolder.tvTrait.setTextColor(context.getResources().getColor(R.color.colorWhite));
                      }
                        mCallback.onItemChange(traits);
                  }
              });
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
