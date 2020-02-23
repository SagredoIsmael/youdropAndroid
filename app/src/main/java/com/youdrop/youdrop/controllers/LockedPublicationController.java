package com.youdrop.youdrop.controllers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;

import com.youdrop.youdrop.api.ContentManager;
import com.youdrop.youdrop.api.callbacks.CountCallback;
import com.youdrop.youdrop.api.callbacks.GetCommentsCallback;
import com.youdrop.youdrop.api.callbacks.LikeCallback;
import com.youdrop.youdrop.api.callbacks.LikesCallback;
import com.youdrop.youdrop.data.Comment;
import com.youdrop.youdrop.data.Like;
import com.youdrop.youdrop.data.Publication;
import com.youdrop.youdrop.data.results.CommentsResult;
import com.youdrop.youdrop.data.results.LikesResult;
import com.youdrop.youdrop.views.ProfileActivity;
import com.youdrop.youdrop.views.adapters.CommentRecyclerViewAdapter;
import com.youdrop.youdrop.views.publications.LockedPublicationView;
import com.youdrop.youdrop.R;

import java.util.List;

/**
 * Created by pau on 26/09/17.
 */

public class LockedPublicationController implements CommentRecyclerViewAdapter.OnCommentInteractionListener {

    LockedPublicationView view;

    public Publication getPublication() {
        return publication;
    }

    private Publication publication;
    private Location location;
    private String authToken;

    ContentManager contentManager;

    public LockedPublicationController(LockedPublicationView view) {
        this.view = view;
        contentManager = ContentManager.getInstance(view.getContext());
    }

    private String getAuthToken(){
        SharedPreferences sharedPref = view.getContext().getSharedPreferences( "session", Context.MODE_PRIVATE);
        authToken = sharedPref.getString("authToken", null);
       // userID = sharedPref.getInt("userId", -1);
        return authToken;
    }

    public void setPublication(Publication publication) {
        this.publication = publication;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void reload(){
        getAuthToken();
        if (publication != null && location != null){
            view.setupViewData(publication, location);
        }
        contentManager.getCommentsCount(authToken, publication.getId(), countCommentsCallback);
        getLikesCount();
        contentManager.getLike(authToken,publication.getId(), likeCallback );
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

    @Override
    public void onCommentInteraction(Comment item) {

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

    public void sharePublication(){

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, view.getContext().getString(R.string.share_cobtebt, publication.getId()));
        sendIntent.setType("text/plain");
        view.getContext().startActivity(Intent.createChooser(sendIntent, "Share to"));
    }

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
}
