package com.saloni.mynotes;


import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.saloni.mynotes.Entity;

import java.util.ArrayList;
import java.util.List;

class GridAdapter extends RecyclerView.Adapter<GridAdapter.GridViewHolder> {

    Context mContext;
     ArrayList<Entity> nList;
    ArrayList<Entity> selected;
    LayoutInflater inflater;
    @Override
    public GridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_single, parent, false);

        return new GridViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GridViewHolder holder, int position) {
        Entity entity = nList.get(position);
        holder.id.setText(String.valueOf(entity.getID()));
        holder.title.setText(entity.getTitle());
        holder.remark.setText(entity.getRemark());
        holder.date.setText(entity.getDate());
        if(selected.contains(nList.get(position)))
            holder.linearLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.list_item_selected_state));
        else
            holder.linearLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.list_item_normal_state));

    }

    @Override
    public int getItemCount() {
        return nList.size();
    }

    public class GridViewHolder extends RecyclerView.ViewHolder {

        TextView id;
        TextView title;
        TextView remark;
        TextView date;
        LinearLayout linearLayout;
        public GridViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.grid_text_title);
            remark = (TextView) view.findViewById(R.id.grid_text_remark);
            date = (TextView) view.findViewById(R.id.grid_text_date);
            id = (TextView) view.findViewById(R.id.grid_text_id);
            linearLayout=(LinearLayout)view.findViewById(R.id.ll2);
        }
    }
    public GridAdapter(Context mContext, ArrayList<Entity> albumList, ArrayList<Entity> selected) {
        this.mContext = mContext;
        this.nList =albumList;
        this.selected=selected;
    }

}