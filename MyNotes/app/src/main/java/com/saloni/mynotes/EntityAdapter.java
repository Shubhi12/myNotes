package com.saloni.mynotes;


import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

class EntityAdapter extends RecyclerView.Adapter<EntityAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<Entity> nList;
    ArrayList<Entity> selected = new ArrayList<>();
    LayoutInflater inflater;
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
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

    @Override
    public long getItemId(int position) {
        Entity entity = nList.get(position);
        return entity.getID();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView id;
        TextView title;
        TextView remark;
        TextView date;
        LinearLayout linearLayout;
        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.txtnamerow);
            remark = (TextView) view.findViewById(R.id.txtremark);
            date = (TextView) view.findViewById(R.id.txtdate);
            id = (TextView) view.findViewById(R.id.txtidrow);
            linearLayout=(LinearLayout) view.findViewById(R.id.ll1);
        }
    }
    public EntityAdapter(Context mContext, ArrayList<Entity> albumList, ArrayList<Entity> selected_list) {
        this.mContext = mContext;
        this.nList =albumList;
        this.selected=selected_list;
    }

}