package com.snapshop.ga.snapshop.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.snapshop.ga.snapshop.R;
import com.snapshop.ga.snapshop.models.ItemModel;

import java.util.List;

/**
 * Created by Gift on 3/20/2017.
 */

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemsViewHolder>{

    private List<ItemModel> items;
    private int rowLayout;
    private Context context;


    public static class ItemsViewHolder extends RecyclerView.ViewHolder {
        LinearLayout itemsLayout;
        TextView itemTitle;
        TextView itemId;
        TextView price;
        TextView actionUrl;
        TextView imageUrl;

        public ItemsViewHolder(View v) {
            super(v);
            itemsLayout = (LinearLayout) v.findViewById(R.id.items_layout);
            itemTitle = (TextView) v.findViewById(R.id.itemTitle);
            itemId = (TextView) v.findViewById(R.id.itemId);
            price = (TextView) v.findViewById(R.id.price);
            actionUrl = (TextView) v.findViewById(R.id.actionUrl);
            imageUrl = (TextView) v.findViewById(R.id.imageUrl);
        }
    }

    public ItemsAdapter(List<ItemModel> items, int rowLayout, Context context) {
        this.items = items;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @Override
    public ItemsAdapter.ItemsViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new ItemsViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ItemsViewHolder holder, final int position) {
        holder.itemTitle.setText(items.get(position).getItemTitle());
        holder.itemId.setText(items.get(position).getItemId());
        holder.price.setText(items.get(position).getPrice());
        holder.actionUrl.setText(items.get(position).getActionUrl());
        holder.imageUrl.setText(items.get(position).getImageUrl());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
