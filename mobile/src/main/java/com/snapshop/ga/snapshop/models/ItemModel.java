package com.snapshop.ga.snapshop.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Gift on 3/20/2017.
 */

public class ItemModel {

    @SerializedName("itemTitle")
    private String itemTitle;
    @SerializedName("itemId")
    private String itemId;
    @SerializedName("price")
    private String price;
    @SerializedName("imageUrl")
    private String imageUrl;
    @SerializedName("actionUrl")
    private String actionUrl;

    public ItemModel(String itemTitle, String itemId, String price, String imageUrl, String actionUrl) {
        this.itemTitle = itemTitle;
        this.itemId = itemId;
        this.price = price;
        this.imageUrl = imageUrl;
        this.actionUrl = actionUrl;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }
}
