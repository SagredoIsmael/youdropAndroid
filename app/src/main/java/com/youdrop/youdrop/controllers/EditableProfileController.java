package com.youdrop.youdrop.controllers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;

import com.youdrop.youdrop.R;
import com.youdrop.youdrop.api.ContentManager;
import com.youdrop.youdrop.api.Utils;
import com.youdrop.youdrop.api.callbacks.FileCallback;
import com.youdrop.youdrop.api.callbacks.GetSingleUserCallback;
import com.youdrop.youdrop.api.callbacks.GetUserImagesCallback;
import com.youdrop.youdrop.api.callbacks.UserImageCallback;
import com.youdrop.youdrop.data.User;
import com.youdrop.youdrop.data.UserImage;
import com.youdrop.youdrop.data.results.UsersImageResult;
import com.youdrop.youdrop.views.adapters.UserImageRecyclerViewAdapter;
import com.youdrop.youdrop.views.sections.EditableProfileView;
import com.youdrop.youdrop.views.sections.ProfileView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by pau on 16/09/17.
 */

public class EditableProfileController implements UserImageRecyclerViewAdapter.OnUserImageInteractionListener {

    private EditableProfileView view;

    private String authToken;
    String userId;

    private User user;
    UsersImageResult images;

    public void setImages(UsersImageResult images) {
        this.images = images;
    }

    public void setUser(User user) {
        this.user = user;
    }

    private ContentManager contentManager;

    public EditableProfileController(EditableProfileView view) {
        this.view = view;
        getAuthToken();
        contentManager = ContentManager.getInstance(view.getContext());
    }

    private String getAuthToken(){
        authToken = Utils.getAuthToken(view.getContext());
        userId = Utils.getUserId(view.getContext());
        return authToken;
    }

    public void loadFromUserId(String id){
        contentManager.getUser(authToken, id, callback);
        contentManager.getUserImages(authToken, id, imagesCallback);
    }

    public void loadFromCurrentUser(){
        loadFromUserId(userId);
    }

    GetSingleUserCallback callback = new GetSingleUserCallback() {
        @Override
        public void onDataReceived(User user) {
            setUser(user);
            view.setUserData(user);
        }

        @Override
        public void onError(String error) {

        }
    };

    GetUserImagesCallback imagesCallback = new GetUserImagesCallback() {
        @Override
        public void onDataReceived(UsersImageResult images) {
            setImages(images);
            view.setUserImages(images.getItems());
        }
    };

    @Override
    public void onUserImageInteraction(final UserImage item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getActivity());
        builder.setMessage("Borrar imagen?")
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        contentManager.deleteUserImage(authToken, item, new UserImageCallback() {
                            @Override
                            public void onSuccess(UserImage file) {
                                for (UserImage i : images.getItems()){
                                    if (i.getId().equals(file.getId())){
                                        images.getItems().remove(i);
                                        break;
                                    }
                                }
                                view.setUserImages(images.getItems());
                            }

                            @Override
                            public void onError() {

                            }
                        });
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        builder.create().show();
    }

    String mCurrentPhotoPath;
    String mCurrentPhotoPathUserImage;

    public void changeAvatarPhoto(){
        try {
            view.dispatchTakePictureIntent(createImageFile());
        }catch (IOException e){

        }
    }

    public void addUserImagePhoto(){
        try {
            view.dispatchTakePictureIntentForImages(createImageFile());
        }catch (IOException e){

        }
    }

    public void addUserImageGallery(){
        view.dispatchGetPictureIntentForImages();
    }
    public void changeAvatarGallery(){
        view.dispatchGetPictureIntent();
    }

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
        mCurrentPhotoPath = image.getAbsolutePath();

        return image;
    }

    public void processImage(Uri uri){
        if (uri == null){
            processPicture();
        } else {
            processPicture(uri);
        }
    }

    public void processImageForUserImages(Uri uri){
        if (uri == null){
            processPictureForUserImage();
        } else {
            processUserImagePicture(uri);
        }
    }


    private String encodeImage(Bitmap bm)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }

    public void processPicture(){
        view.showLoading(true);
        Bitmap preview = getThumbnail();
        //view.setPreview(preview);
        com.youdrop.youdrop.data.File file = new com.youdrop.youdrop.data.File();
        file.setContent(encodeImage(preview));
        file.setMimetype("image/jpeg");
        contentManager.addFile(authToken, file, new FileCallback() {
        //contentManager.addFile(authToken, mCurrentPhotoPath,"image/jpeg",  new FileCallback() {
            @Override
            public void onSuccess(com.youdrop.youdrop.data.File file) {
                if (file != null){
                    User user = new User();
                    user.setId(userId);
                    user.setAvatarId(file.getId());
                    contentManager.updateUser(authToken, user, new GetSingleUserCallback() {
                        @Override
                        public void onDataReceived(User user) {
                            if (user!= null){
                                view.setUserData(user);
                            }
                            view.showLoading(false);
                        }

                        @Override
                        public void onError(String error) {

                        }
                    });
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

    public void processPictureForUserImage(){
        view.showLoading(true);

        contentManager.addFile(authToken, mCurrentPhotoPathUserImage,"image/jpeg",  new FileCallback() {
            @Override
            public void onSuccess(com.youdrop.youdrop.data.File file) {
                if (file != null){
                    UserImage user = new UserImage();
                    user.setFileId(file.getId());
                    contentManager.addUserImage(authToken, userId, user, new UserImageCallback() {
                        @Override
                        public void onSuccess(UserImage file) {
                            images.getItems().add(file);
                            view.addUserImage(file);
                            view.showLoading(false);
                        }

                        @Override
                        public void onError() {

                        }
                    });
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

    public void processPicture(Uri path){
        Cursor cursor;
        String[] column = { MediaStore.Images.Media.DATA };
        if(Build.VERSION.SDK_INT >19)
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

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            mCurrentPhotoPath = cursor.getString(columnIndex);
        }
        cursor.close();
        processPicture();
    }

    public void processUserImagePicture(Uri path){
        Cursor cursor;
        String[] column = { MediaStore.Images.Media.DATA };
        if(Build.VERSION.SDK_INT >19)
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

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            mCurrentPhotoPathUserImage = cursor.getString(columnIndex);
        }
        cursor.close();
        processPictureForUserImage();
    }

    private Bitmap getThumbnail() {
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

    public void showEditUser(){
        view.editUser(user.getName(), user.getUsername(), user.getEmail(), user.getDescription());
    }

    public void editUser(String name, String username, String email, String descr){
        user.setName(name);
        user.setUsername(username);
        user.setEmail(email);
        user.setDescription(descr);
        contentManager.updateUser(authToken, user, new GetSingleUserCallback() {
            @Override
            public void onDataReceived(User user) {
                if (user!= null){
                    view.setUserData(user);
                }
                //view.showLoading(false);
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    public void logOut(){
        SharedPreferences preferences = view.getContext().getSharedPreferences("session", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }

}
