package com.snapshop.ga.snapshop.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
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
    private final Context context;

    public static class ItemsViewHolder extends RecyclerView.ViewHolder {
        LinearLayout itemsLayout;
        TextView storeName;
        TextView storeUrl;
        CardView carousalCard;
        ImageView imageUrl;

        public ItemsViewHolder(View v) {
            super(v);
            itemsLayout = (LinearLayout) v.findViewById(R.id.items_layout_1);
            storeName = (TextView) v.findViewById(R.id.store_name_carousal);
//            storeUrl = (TextView) v.findViewById(R.id.action_url_carousal);
            carousalCard = (CardView) v.findViewById(R.id.card_carousal);
            imageUrl = (ImageView) v.findViewById(R.id.image_carousal);

        }


    }

    public ItemsAdapter(CardModel cardModel, int rowLayout, final Context context) {

        this.items = cardModel.getListOfCards();
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

        holder.storeName.setText(items.get(position).getStoreName());
       new ImageLoader(holder.imageUrl).execute("http://logo.clearbit.com/"+items.get(position).getStoreName());
//        holder.storeUrl.setText(items.get(position).getStoreUrl());

        holder.carousalCard.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(items.get(position).getStoreUrl()));
                context.startActivity(intent);
            } });

        //new ImageLoader(holder.imageUrl).execute(items.get(position).getImageUrl());
//        /holder.imageUrl.setImageBitmap(new ImageLoader(iv).execute(items.get(position).getPrice()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


}
