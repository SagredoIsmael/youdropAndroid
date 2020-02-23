package com.youdrop.youdrop.controllers;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.util.Base64;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.youdrop.youdrop.api.ContentManager;
import com.youdrop.youdrop.api.Utils;
import com.youdrop.youdrop.api.callbacks.CreatePublicationCallback;
import com.youdrop.youdrop.api.callbacks.FileCallback;
import com.youdrop.youdrop.api.callbacks.GetCategoriesCallback;
import com.youdrop.youdrop.api.callbacks.GetSingleUserCallback;
import com.youdrop.youdrop.api.callbacks.GetUsersCallback;
import com.youdrop.youdrop.data.Category;
import com.youdrop.youdrop.data.Publication;
import com.youdrop.youdrop.data.Data;
import com.youdrop.youdrop.data.User;
import com.youdrop.youdrop.data.results.CategoriesResult;
import com.youdrop.youdrop.data.results.UsersResult;
import com.youdrop.youdrop.views.adapters.CategoryRecyclerViewAdapter;
import com.youdrop.youdrop.views.adapters.FriendRecyclerViewAdapter;
import com.youdrop.youdrop.views.adapters.UserRecyclerViewAdapter;
import com.youdrop.youdrop.views.publications.PublishView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by pau on 27/09/17.
 */

public class PublishController implements UserRecyclerViewAdapter.OnUserInteractionListener {

    PublishView view;

    ContentManager contentManager;

    private boolean isPublic = true, anonymous = false, downloadable = false;

    private String userID;
    private String authToken;
    private Location location;

    private String type;

    private String value;

    private Bitmap image;

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public PublishController(PublishView view) {
        this.view = view;
        contentManager  = ContentManager.getInstance(view.getActivity());
    }

    @Override
    public void onUserInteraction(User item) {
        int pos = selectedFriends.indexOf(item);
        selectedFriends.remove(pos);
        view.removeSelectedFriend(pos);
    }

    private ArrayList<User> selectedFriends = new ArrayList<>();

    public void friendSelected(String name){
        User u = null;
        for (User a : friends.getItems()){
            if (a.getName().equals(name)){
                u = a;
                break;
            }
        }
        if (!selectedFriends.contains(u)){
            selectedFriends.add(u);
        }
        view.addSelectedFriend(u);
    }

    public void init(String type){
        getAuthToken();
        setupLocation();
        contentManager.getFriends(authToken, friendsCallback);
        setContentPublic();
        this.type = type;
        if ("PHOTO".equals(type)){
            //OPEN photo
            view.dispatchGetPictureIntent();
        }
        if ("VIDEO".equals(type)){
            view.dispatchTakeVideoIntent();
        }
        if ("GALLERY".equals(type)){
            view.dispatchGetPictureIntent();
        }
        if ("VIDEO_GALLERY".equals(type)){
            view.dispatchGetVideoIntent();
        }
        if ("AUDIO".equals(type)){
          //  view.dispatchGetPictureIntent();
        }
    }

    public String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = view.getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private FusedLocationProviderClient mFusedLocationClient;

    public void setupLocation() {
        if (ActivityCompat.checkSelfPermission(view.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            ActivityCompat.requestPermissions(view.getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    2);

            return;
        }
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(view.getContext());

        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(view.getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            PublishController.this.location = location;
                        }
                    }
                });

        mFusedLocationClient.requestLocationUpdates(LocationRequest.create(),
                mLocationCallback,
                null /* Looper */);
    }

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            for (Location location : locationResult.getLocations()) {
                if (location != null) {
                    PublishController.this.location = location;
                }
            }
        };
    };

    private void getAuthToken(){
        authToken = Utils.getAuthToken(view.getContext());
        userID = Utils.getUserId(view.getContext());
    }

    public void setContentPrivate(){
        isPublic = false;
        view.setPrivateMode();
    }

    public void setContentPublic(){
        isPublic = true;
        view.setPublicMode();
        selectedFriends.clear();
    }

    private UsersResult friends;

    GetUsersCallback friendsCallback = new GetUsersCallback() {
        @Override
        public void onDataReceived(UsersResult result) {
            friends = result;
            List<String> names = new ArrayList<String>();
            if (result != null && !result.getItems().isEmpty()){
                for (User user : result.getItems()) {
                    names.add(user.getName());
                }
                view.setFriendList(names);
            }

        }
    };

    public void setAnonymous(boolean value){
        anonymous = value;
    }

    public void setDownloadable(boolean value){
        downloadable = value;
    }

    private boolean validate(){

        if (authToken == null) {
            view.showError("Needs authentication");
            return false;
        }
        if (value == null && "TEXT".equals(type)) {
            view.showError("Value is needed");
            return false;
        }

        if (location == null) {
            view.showError("No location");
            return false;
        }
        if (("PHOTO".equals(type) || "GALLERY".equals(type)) && image == null){
            view.showError("No image selected");
            return false;
        }
        if ((!"TEXT".equals(type)) && mCurrentPhotoPath == null){
            view.showError("No media selected");
            return false;
        }
        return true;
    }

    public void save(){
        view.showLoading(true);
        if (!validate()){
            view.showLoading(false);
            return;
        }
        if ("TEXT".equals(type)){
            processPublication(null);
        } else {
            processFile();
        }
    }

    private void processPublication(com.youdrop.youdrop.data.File file){
        Publication c = new Publication();
        c.setContent(value);
        c.setDownloadable(downloadable);
        com.youdrop.youdrop.data.Location loc = new com.youdrop.youdrop.data.Location(location.getLatitude(),location.getLongitude());
        c.setLocation(loc);
         c.setUserId(userID);
         c.setAnonymous(anonymous);
        if (!isPublic){
            c.setReceivers(selectedFriends);
        }

        // set fileId
        if (file != null) c.setFileId(file.getId());

        contentManager.getApi().createPublication(authToken, c, callback);
    }

    String mime;

    private void processFile(){
        contentManager.addFile(authToken, mCurrentPhotoPath,mime,  new FileCallback() {
            @Override
            public void onSuccess(com.youdrop.youdrop.data.File file) {
                if (file != null){
                    processPublication(file);
                } else {
                    view.showLoading(false);
                }
            }

            @Override
            public void onError() {
                view.showLoading(false);
            }
        });
    }

    private void processFile2(){
        //view.setPreview(preview);
        try{
            com.youdrop.youdrop.data.File file = new com.youdrop.youdrop.data.File();
            file.setContent(encodeVideo(mCurrentPhotoPath));
            if (("PHOTO".equals(type) || "GALLERY".equals(type))){
                file.setMimetype("image/jpeg");
            }
            if (("VIDEO".equals(type) || ("VIDEO_GALLERY".equals(type)))){
                file.setMimetype("mp4");
            }

            contentManager.addFile(authToken, file, new FileCallback() {
                @Override
                public void onSuccess(com.youdrop.youdrop.data.File file) {
                    if (file != null){
                        processPublication(file);
                    } else {
                        view.showLoading(false);
                    }
                }

                @Override
                public void onError() {
                    view.showLoading(false);
                }
            });
        }catch (IOException e){
                view.showError("Sin memoria");
                view.showLoading(false);
        }
    }

    CreatePublicationCallback callback = new CreatePublicationCallback() {
        @Override
        public void onSuccess(Publication publication) {
            view.showLoading(false);
            view.gotoContent(publication);
        }

        @Override
        public void onError() {
            view.showLoading(false);
            view.showError("Error al publicar");
        }
    };


    private String encodeImage(Bitmap bm)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }

    private String encodeVideo(String url) throws IOException, OutOfMemoryError
    {
        InputStream is =  new FileInputStream(url);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        int read = -1;
        byte[] buf = new byte[1024];

        try {
            while( (read = is.read(buf)) != -1) {
                os.write(buf, 0, read);
            }
        }
        finally {
            is.close();
            os.close();
        }
        byte[] b = os.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encImage;
    }

    public void processPicture(){
        setImage(getPhoto());
        Bitmap preview = getPic();
        view.setPreview(preview);
        mime = "image/jpeg";
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void processPicture2(Uri path){
        String wholeID = DocumentsContract.getDocumentId(path);

// Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = { MediaStore.Images.Media.DATA };

// where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = view.getActivity().getContentResolver().
                query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        column, sel, new String[]{ id }, null);

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            mCurrentPhotoPath = cursor.getString(columnIndex);
        }

        cursor.close();
        processPicture();
    }

    public void processPicture(Uri path){
        Cursor cursor;
        String[] column = { MediaStore.Images.Media.DATA };
        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.KITKAT)
        {
            try {
                // Will return "image:x*"
                String wholeID = DocumentsContract.getDocumentId(path);
                String id = wholeID.split(":")[1];
// where id is equal to
                String sel = MediaStore.Images.Media._ID + "=?";

                cursor = view.getActivity().getContentResolver().
                        query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                column, sel, new String[]{ id }, null);
            } catch (IllegalArgumentException e){
                //Try safe
                cursor = view.getActivity().getContentResolver().query(path, column, null, null, null);
            }
        }
        else
        {
            cursor = view.getActivity().getContentResolver().query(path, column, null, null, null);
        }

        if(cursor != null){
            int columnIndex = cursor.getColumnIndex(column[0]);

            if (cursor.moveToFirst()) {
                mCurrentPhotoPath = cursor.getString(columnIndex);
            }
            cursor.close();
        } else {
            mCurrentPhotoPath = path.getPath();
        }
        processPicture();
    }

    public void processVideo(Uri path) {
        Cursor cursor;
        String[] column = { MediaStore.Video.Media.DATA };
        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.KITKAT)
        {
            try {
                // Will return "image:x*"
                String wholeID = DocumentsContract.getDocumentId(path);
                String id = wholeID.split(":")[1];
// where id is equal to
                String sel = MediaStore.Video.Media._ID + "=?";

                cursor = view.getActivity().getContentResolver().
                        query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                                column, sel, new String[]{ id }, null);
            }catch (IllegalArgumentException e){
                cursor = view.getActivity().getContentResolver().query(path, column, null, null, null);

            }
        }
        else
        {
            cursor = view.getActivity().getContentResolver().query(path, column, null, null, null);
        }

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            mCurrentPhotoPath = cursor.getString(columnIndex);
        }
        cursor.close();
        view.setPreviewVideo(mCurrentPhotoPath);
        mime = "video/mp4";
    }

    private Bitmap getPic() {
        // Get the dimensions of the View
        int targetW = 200;
        int targetH = 200;

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        return bitmap;
    }

    private Bitmap getPhoto() {
       // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = false;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        return bitmap;
    }
}
