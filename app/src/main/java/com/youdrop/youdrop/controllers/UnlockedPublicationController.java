package com.youdrop.youdrop.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import com.youdrop.youdrop.api.ContentManager;
import com.youdrop.youdrop.api.callbacks.CountCallback;
import com.youdrop.youdrop.api.callbacks.CreateCommentCallback;
import com.youdrop.youdrop.api.callbacks.GetCommentsCallback;
import com.youdrop.youdrop.api.callbacks.GetSingleContentCallback;
import com.youdrop.youdrop.api.callbacks.LikeCallback;
import com.youdrop.youdrop.api.callbacks.LikesCallback;
import com.youdrop.youdrop.api.tasks.GetFileTask;
import com.youdrop.youdrop.api.tasks.RestTaskCallback;
import com.youdrop.youdrop.data.Comment;
import com.youdrop.youdrop.data.Like;
import com.youdrop.youdrop.data.Publication;
import com.youdrop.youdrop.data.results.CommentsResult;
import com.youdrop.youdrop.data.results.LikesResult;
import com.youdrop.youdrop.views.ProfileActivity;
import com.youdrop.youdrop.views.adapters.CommentRecyclerViewAdapter;
import com.youdrop.youdrop.views.publications.UnlockedPublicationView;
import com.youdrop.youdrop.R;

import org.afinal.simplecache.ACache;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by pau on 26/09/17.
 */

public class UnlockedPublicationController implements CommentRecyclerViewAdapter.OnCommentInteractionListener {

    UnlockedPublicationView view;

    private Publication publication;
    private Location location;
    private String authToken;
    private String userId;

    private CommentsResult commentsResult;
    boolean showingComments = false;

    ContentManager contentManager;

    public UnlockedPublicationController(UnlockedPublicationView view) {
        this.view = view;
        getAuthToken();
        contentManager  = ContentManager.getInstance(view.getContext());
    }

    private String getAuthToken(){
        SharedPreferences sharedPref = view.getContext().getSharedPreferences( "session", Context.MODE_PRIVATE);
        authToken = sharedPref.getString("authToken", null);
        userId = sharedPref.getString("userId", null);
        return authToken;
    }

    public void setPublication(Publication publication) {
        this.publication = publication;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void reload(){
        if (publication != null && location != null){
            view.setupViewData(publication, location);
            cacheFile();
        }
        view.setNumberOfLikes(0);
        view.setNumberOfComments(0);
        contentManager.getCommentsCount(authToken, publication.getId(), countCommentsCallback);
        getLikesCount();
        contentManager.getLike(authToken,publication.getId(), likeCallback );

    }

    public CharSequence[] getOptions(){
        ArrayList<CharSequence> list = new ArrayList<>();
        list.add("Reportar");
        if (publication.isDownloadable()) list.add("Descargar");
        if (publication.getUser().getId().equals(userId)) list.add("Eliminar");
        return list.toArray(new CharSequence[0]); //new CharSequence[]{"Reportar","Descargar", "Eliminar"};
    }

    public void optionClicked(int which){
        if (which == 0){
            //TODO report
            view.askReport();
        }
        if (which == 1){
            if (publication.isDownloadable()){
                //TODO download
            }else if (publication.getUser().getId().equals(userId)){
                //TODO delete
                view.askDelete();
            }
        }
        if (which == 2){
            if (publication.getUser().getId().equals(userId)){
                //TODO delete
                view.askDelete();
            }
        }
    }

    public void reportClicked(String which){

    }

    public void deletePublication(){
        contentManager.deletePublication(authToken, publication, new GetSingleContentCallback() {
            @Override
            public void onDataReceived(Publication publication) {
                ACache.get(view.getActivity()).clear();
                view.getActivity().setResult(Activity.RESULT_OK);
                view.getActivity().finish();
            }
        });
    }

    private Like like;

    LikeCallback likeCallback = new LikeCallback() {
        @Override
        public void onSuccess(Like file) {
            like = file;
            if (file != null){
                view.setLikeStatus(true);
            }else {
                view.setLikeStatus(false);
            }
            getLikesCount();
        }

        @Override
        public void onError() {
            view.setLikeStatus(false);
        }
    };

    public void gotoProfile(){
        Intent i = new Intent(view.getActivity(), ProfileActivity.class);
        Bundle args = new Bundle();
        args.putString(ProfileActivity.ARG_USERID, publication.getUser().getId());
        i.putExtras(args);
        view.getActivity().startActivity(i);
    }

    public void likeActionClicked(){
        if (like != null){
            contentManager.dislike(authToken, publication.getId(), new LikeCallback() {
                @Override
                public void onSuccess(Like file) {
                    like = null;
                    view.setLikeStatus(false);
                    getLikesCount();
                }

                @Override
                public void onError() {

                }
            });
        }else {
            contentManager.addLike(authToken, publication.getId(), new LikeCallback() {
                @Override
                public void onSuccess(Like file) {
                    like = file;
                    view.setLikeStatus(true);
                    getLikesCount();
                }

                @Override
                public void onError() {

                }
            });
        }
    }

    public void showLikes(){
        contentManager.getLikes(authToken, publication.getId(), new LikesCallback() {
            @Override
            public void onSuccess(LikesResult result) {
                if (!result.getItems().isEmpty()){
                    view.showLikes(result.getItems());
                }
            }
        });

    };

    private void getLikesCount(){
        contentManager.getLikesCount(authToken, publication.getId(), countLikesCallback);
    }

    CountCallback countCommentsCallback = new CountCallback() {
        @Override
        public void onSuccess(int count) {
            view.setNumberOfComments(count);
        }
    };

    CountCallback countLikesCallback = new CountCallback() {
        @Override
        public void onSuccess(int count) {
            view.setNumberOfLikes(count);
        }
    };

    GetCommentsCallback callback = new GetCommentsCallback() {
        @Override
        public void onDataReceived(CommentsResult comments) {
            commentsResult = comments;

            view.toggleComments(true);
            view.setCommentList(comments.getItems());
        }
    };

    private String getTempFile() throws IOException{
        java.io.File outputDir = view.getContext().getCacheDir(); // context being the Activity pointer
        java.io.File outputFile = java.io.File.createTempFile("prefix", "extension", outputDir);
        return  outputFile.getAbsolutePath();
    }

    public void cacheFile(){
        if (publication.getFile() != null && ("mp4".equals(publication.getFile().getMimetype()) || "video/mp4".equals(publication.getFile().getMimetype())) ){
            String videoPath = "http://api.you-drop.com/files/content/" +publication.getFile().getId();

            try{
                new GetFileTask(videoPath, authToken, getTempFile(), new RestTaskCallback(){
                    @Override
                    public void onTaskComplete(String result) {
                        view.showVideo(result);
                    }
                }).execute();
            }catch (IOException e){
                //view.showError();
            }
        }
    }

    @Override
    public void onCommentInteraction(Comment item) {

    }
    public void sharePublication(){

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, view.getContext().getString(R.string.share_cobtebt, publication.getId()));
            sendIntent.setType("text/plain");
            view.getContext().startActivity(Intent.createChooser(sendIntent, "Share to"));
        }

    public void sharePublication2(){

        if (publication.getFile() == null){
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, publication.getContent());
            sendIntent.setType("text/plain");
            view.getContext().startActivity(Intent.createChooser(sendIntent, "Share to"));
        }
        if (publication.getFile() != null && "image/jpeg".equals(publication.getFile().getMimetype()) ){
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, publication.getContent());
            sendIntent.setType("image/*");
            Uri uri = Uri.parse("http://api.you-drop.com/files/" +publication.getFile().getId() + "/image/thumbnail");

            // Add the URI to the Intent.
            sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
            view.getContext().startActivity(Intent.createChooser(sendIntent, "Share to"));
        }
        if (publication.getFile() != null && "video/mp4".equals(publication.getFile().getMimetype()) ){
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, publication.getContent());
            sendIntent.setType("video/*");
            Uri uri = Uri.parse("http://api.you-drop.com/files/content/" +publication.getFile().getId());

            // Add the URI to the Intent.
            sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
            view.getContext().startActivity(Intent.createChooser(sendIntent, "Share to"));
        }
    }

    public void showComments(){
        if (!showingComments) {
            if (commentsResult == null){
                contentManager.getComments(authToken, publication.getId(), callback);
            } else {
               // view.setCommentList(commentsResult.getItems());
                view.toggleComments(true);
            }
            showingComments = true;
        } else {
            view.toggleComments(false);
            showingComments = false;
        }
    }

    public void addComment(String body){
        Comment c = new Comment();
        c.setBody(body);
        contentManager.addComment(authToken, publication.getId(), c, new CreateCommentCallback() {
            @Override
            public void onSuccess(Comment comment) {
                view.addComment(comment);
            }

            @Override
            public void onError() {

            }
        });
    }
}
