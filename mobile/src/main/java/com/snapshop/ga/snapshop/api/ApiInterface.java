package com.snapshop.ga.snapshop.api;

import com.snapshop.ga.snapshop.models.ItemModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Gift on 3/20/2017.
 */

public interface ApiInterface {

    @GET("TestServlet")
    Call<List<ItemModel>> getItems();
}
