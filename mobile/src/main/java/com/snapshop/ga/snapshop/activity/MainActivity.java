/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.snapshop.ga.snapshop.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Movie;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.snapshop.ga.snapshop.adapter.ItemsAdapter;
import com.snapshop.ga.snapshop.adapter.ItemsListAdapter;
import com.snapshop.ga.snapshop.api.ApiClient;
import com.snapshop.ga.snapshop.api.ApiInterface;
import com.snapshop.ga.snapshop.models.CardModel;
import com.snapshop.ga.snapshop.models.MainModel;
import com.snapshop.ga.snapshop.utils.JsonHelper;
import com.snapshop.ga.snapshop.utils.PermissionUtil;
import com.snapshop.ga.snapshop.R;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/*
 * MainActivity class that loads MainFragment
 */
public class MainActivity extends AppCompatActivity {
    /**
     * Called when the activity is first created.
     */

    public static final String TMP_FILE = "temp.jpg";

    private static final String SERVER_URL = "http://127.0.0.1:8080/SnapShopService";


    private static final int GALLERY_PERMISSIONS_REQUEST = 0;
    private static final int GALLERY_IMAGE_REQUEST = 1;
    public static final int CAMERA_PERMISSIONS_REQUEST = 2;
    public static final int CAMERA_IMAGE_REQUEST = 3;

    private static final String TAG = MainActivity.class.getSimpleName();

    private ImageView mMainImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_1);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        callSnapShopServer();

//
//        Button btn = (Button) findViewById(R.id.action_settings);
//        //R.id.action_settings
//        btn.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//                builder
//                        .setMessage(R.string.dialog_select_prompt)
//                        .setPositiveButton(R.string.dialog_select_gallery, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                startGalleryChooser();
//                            }
//                        })
//                        .setNegativeButton(R.string.dialog_select_camera, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                startCamera();
//                            }
//                        });
//                builder.create().show();
//            }
//        });

        mMainImage = (ImageView) findViewById(R.id.image_capture);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void startCamera() {
        if (PermissionUtil.requestPermission(
                this,
                CAMERA_PERMISSIONS_REQUEST,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            Uri photoUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", getCameraFile());
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(intent, CAMERA_IMAGE_REQUEST);
        }
    }

    public File getCameraFile() {
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return new File(dir, TMP_FILE);
    }

    public void startGalleryChooser() {
        if (PermissionUtil.requestPermission(this, GALLERY_PERMISSIONS_REQUEST, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select a photo"),
                    GALLERY_IMAGE_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            uploadToServer(data.getData());
        } else if (requestCode == CAMERA_IMAGE_REQUEST && resultCode == RESULT_OK) {
//            Uri photoUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", getCameraFile());
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            mMainImage.setImageBitmap(photo);
            //uploadToServer(photoUri);
        }
    }

    public void uploadToServer(Uri uri) {
        if (uri != null) {
            try {
                // scale the image to save on bandwidth
                Bitmap bitmap =
                        scaleBitmapDown(
                                MediaStore.Images.Media.getBitmap(getContentResolver(), uri),
                                1200);

                //callGCVAPI(bitmap);
                callSnapShopServer();
                mMainImage.setImageBitmap(bitmap);


            } catch (IOException e) {
                Log.d(TAG, "Image picking failed because " + e.getMessage());
                Toast.makeText(this, R.string.image_picker_error, Toast.LENGTH_LONG).show();
            }
        } else {
            Log.d(TAG, "Image picker gave us a null image.");
            Toast.makeText(this, R.string.image_picker_error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_PERMISSIONS_REQUEST:
                if (PermissionUtil.permissionGranted(requestCode, CAMERA_PERMISSIONS_REQUEST, grantResults)) {
                    startCamera();
                }
                break;
            case GALLERY_PERMISSIONS_REQUEST:
                if (PermissionUtil.permissionGranted(requestCode, GALLERY_PERMISSIONS_REQUEST, grantResults)) {
                    startGalleryChooser();
                }
                break;
        }
    }

    public Bitmap scaleBitmapDown(Bitmap bitmap, int maxDimension) {

        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int resizedWidth = maxDimension;
        int resizedHeight = maxDimension;

        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension;
            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = maxDimension;
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
    }
//
    public void callSnapShopServer() {

        final RecyclerView recyclerView_Horizontal = (RecyclerView) findViewById(R.id.recyler_view_horizontal);
        recyclerView_Horizontal.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
        recyclerView_Horizontal.setHasFixedSize(true);

        final RecyclerView recyclerView_List = (RecyclerView) findViewById(R.id.recycler_view_list);
        recyclerView_List.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
        recyclerView_List.setLayoutManager(new LinearLayoutManager(this));

        final RecyclerView recyclerView_Horizontal2 = (RecyclerView) findViewById(R.id.recyler_view_vertical);
        recyclerView_Horizontal2.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
        recyclerView_Horizontal2.setHasFixedSize(true);

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

//        Call<List<ItemModel>> call = apiService.getItems();
        Call<MainModel> call = apiService.getModularResponse();
        call.enqueue(new Callback<MainModel> () {
//            @Override
//            public void onResponse(Call<List<ItemModel>> call, Response<List<ItemModel>> response) {
//                int statusCode = response.code();
//                List<ItemModel> items = response.body();
//                recyclerView_Horizontal.setAdapter(new ItemsAdapter(items, R.layout.activity_main_1, getApplicationContext()));
//                recyclerView_List.setAdapter(new ItemsListAdapter(items, R.layout.activity_main_1, getApplicationContext()));
//                recyclerView_Horizontal2.setAdapter(new ItemsAdapter(items, R.layout.activity_main_1, getApplicationContext()));
//
//            }

            @Override
            public void onResponse(Call<MainModel> call, Response<MainModel> response) {
                HashMap<String, CardModel> mapOfCards;
                int statusCode = response.code();
                mapOfCards = new JsonHelper(response.body()).getCardsMap();
                //response.body().getAsJsonObject().get("cards").getAsJsonObject().get("card_1");

                TextView cardModule1Title = (TextView) findViewById(R.id.module1_title);
                cardModule1Title.setText(mapOfCards.get("card_1").getCardName());

                TextView cardModule2Title = (TextView) findViewById(R.id.module2_title);
                cardModule2Title.setText(mapOfCards.get("card_2").getCardName());

                TextView cardModule3Title = (TextView) findViewById(R.id.module3_title);
                cardModule3Title.setText(mapOfCards.get("card_3").getCardName());

                recyclerView_Horizontal.setAdapter(new ItemsAdapter(mapOfCards.get("card_1"), R.layout.activity_main_1, getApplicationContext()));
                recyclerView_List.setAdapter(new ItemsListAdapter(mapOfCards.get("card_2"), R.layout.activity_main_1, getApplicationContext()));
//                recyclerView_Horizontal2.setAdapter(new ItemsAdapter(mapOfCards.get("card_3"), R.layout.activity_main_1, getApplicationContext()));


                Button myButton;
                LinearLayout scrViewButLay = (LinearLayout) findViewById(R.id.buttonLayout);
               // LinearLayout ll = (LinearLayout) findViewById(R.id.buttonLayout);
                Button[] keywordBtns = new Button[response.body().getListOfKeywords().size()];

                for(int index = 0; index < response.body().getListOfKeywords().size(); index++){

                    keywordBtns[index] = new Button(getApplicationContext());
                    keywordBtns[index].setText(response.body().getListOfKeywords().get(index));

                    scrViewButLay.addView(keywordBtns[index]);
                }


            }

            @Override
            public void onFailure(Call<MainModel> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });
    }

    public void openCamera(MenuItem item){
        //Toast.makeText(this, "Hello World", Toast.LENGTH_LONG).show();

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder
                .setMessage(R.string.dialog_select_prompt)
                .setPositiveButton(R.string.dialog_select_gallery, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startGalleryChooser();
                    }
                })
                .setNegativeButton(R.string.dialog_select_camera, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startCamera();
                    }
                });
        builder.create().show();
    }
}
