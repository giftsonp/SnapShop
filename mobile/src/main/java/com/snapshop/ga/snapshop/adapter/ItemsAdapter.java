package com.snapshop.ga.snapshop.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.snapshop.ga.snapshop.R;
import com.snapshop.ga.snapshop.models.CardModel;
import com.snapshop.ga.snapshop.models.ItemModel;
import com.snapshop.ga.snapshop.utils.ImageLoader;

import java.util.HashMap;
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
        TextView itemTitleCard;
        TextView itemId;
        TextView price;
        TextView actionUrl;
        ImageView imageUrl;

        public ItemsViewHolder(View v) {
            super(v);
            itemsLayout = (LinearLayout) v.findViewById(R.id.items_layout_1);
            itemTitleCard = (TextView) v.findViewById(R.id.itemTitleCard_1);
            //itemId = (TextView) v.findViewById(R.id.itemId);
            price = (TextView) v.findViewById(R.id.itemPrice_1);
            //actionUrl = (TextView) v.findViewById(R.id.actionUrl);
            imageUrl = (ImageView) v.findViewById(R.id.imageView_1);

        }
    }

    public ItemsAdapter(CardModel cardModel, int rowLayout, Context context) {

        this.items = cardModel.getListOfItems();
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @Override
    public ItemsAdapter.ItemsViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_carousal_adapter, parent, false);
        return new ItemsViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ItemsViewHolder holder, final int position) {

        holder.itemTitleCard.setText(items.get(position).getItemTitle());
        //holder.itemId.setText(items.get(position).getItemId());
        holder.price.setText(items.get(position).getPrice());
        //holder.actionUrl.setText(items.get(position).getActionUrl());

        new ImageLoader(holder.imageUrl).execute(items.get(position).getImageUrl());
//        /holder.imageUrl.setImageBitmap(new ImageLoader(iv).execute(items.get(position).getPrice()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
