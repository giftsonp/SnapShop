package com.snapshop.ga.snapshop.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Gift on 3/20/2017.
 */

public class ItemModel {

    @SerializedName("storeUrl")
    private String storeUrl;
    @SerializedName("storeName")
    private String storeName;

    public ItemModel(String storeUrl, String storeName, String actionUrl) {
        this.storeName = storeName;
        this.storeUrl = storeUrl;
    }

    public ItemModel() {

    }

    public String getStoreUrl() {
        return storeUrl;
    }

    public void setStoreUrl(String storeUrl) {
        this.storeUrl = storeUrl;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
}
