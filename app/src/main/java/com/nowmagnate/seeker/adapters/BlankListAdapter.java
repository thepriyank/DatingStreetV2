package com.nowmagnate.seeker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nowmagnate.seeker.R;

public class BlankListAdapter extends RecyclerView.Adapter<BlankListAdapter.ViewHolder  > {

    Context context;
    String blankItem;

    public BlankListAdapter(Context context, String blankItem) {
        this.context = context;
        this.blankItem = blankItem;
    }

    @NonNull
    @Override
    public ViewHolder  onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.blank_item, viewGroup, false);
        return new ViewHolder (view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder  viewHolder, final int position) {
        viewHolder.tvText.setText(blankItem);
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class ViewHolder  extends RecyclerView.ViewHolder {
        TextView tvText;
        public ViewHolder (@NonNull View itemView) {
            super(itemView);
            tvText= itemView.findViewById(R.id.tvBlankMessage);
        }
    }

}
