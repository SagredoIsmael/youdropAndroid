package com.youdrop.youdrop.controllers;

import com.youdrop.youdrop.api.ContentManager;
import com.youdrop.youdrop.api.Utils;
import com.youdrop.youdrop.api.callbacks.CreateFriendshipCallback;
import com.youdrop.youdrop.api.callbacks.GetConnectionCallback;
import com.youdrop.youdrop.api.callbacks.GetFriendshipCallback;
import com.youdrop.youdrop.data.Connection;
import com.youdrop.youdrop.data.Friendship;
import com.youdrop.youdrop.views.ProfileActivityView;
import com.youdrop.youdrop.views.sections.FriendsTabView;
import com.youdrop.youdrop.R;

/**
 * Created by pau on 16/09/17.
 */

public class ProfileActivityController {

    private ProfileActivityView view;

    private Friendship friendship;
    private Connection connection;
    private boolean noFriendship = false;
    private boolean noConnection = false;
    private String userId;

    private ContentManager api;


    public ProfileActivityController(ProfileActivityView view) {
        this.view = view;
        api = ContentManager.getInstance(view.getContext());
    }


    public void loadFriendshipOptions(String userId){
        this.userId = userId;
        //TODO load friendship and connection
        String authToken = Utils.getAuthToken(view.getContext());
        api.getFriendship(authToken, userId, new GetFriendshipCallback() {
            @Override
            public void onDataReceived(Friendship friendship) {
                if (friendship == null){
                    noFriendship = true;
                } else {
                    ProfileActivityController.this.friendship = friendship;
                }
                dataLoaded();
            }
        });

        api.getConnection(authToken, userId, new GetConnectionCallback() {
            @Override
            public void onDataReceived(Connection connection) {
                if (connection == null){
                    noConnection = true;
                } else {
                    ProfileActivityController.this.connection = connection;
                }
                dataLoaded();
            }
        });

    }

    public boolean showFriendsButton(){
        return !userId.equals(Utils.getUserId(view.getContext()));
    }

    public void setFriendshipOptionClicked(int option){
        String currentUserID = Utils.getUserId(view.getContext());
        String authToken = Utils.getAuthToken(view.getContext());
        if (option == 0){
            //Friendship
            if (friendship == null){
                //add friendship

                Friendship f = new Friendship(userId);
                api.addFriendship(authToken, f, new CreateFriendshipCallback() {
                    @Override
                    public void onSuccess(Friendship friendship) {
                        ProfileActivityController.this.friendship =friendship;
                        noFriendship = false;
                        view.showMessage(view.getContext().getString(R.string.friendship_created));
                    }

                    @Override
                    public void onError() {
                        view.showMessage("Error");
                    }
                });
            } else if(friendship.isAccepted() &&friendship.getUser().getId().equals(currentUserID)) {
                //Cancelar
                api.denyFriendship(authToken, friendship, new CreateFriendshipCallback() {
                    @Override
                    public void onSuccess(Friendship friendship) {
                        ProfileActivityController.this.friendship = null;
                        noFriendship = true;

                        view.showMessage(view.getContext().getString(R.string.friendship_denied));
                    }

                    @Override
                    public void onError() {
                        view.showMessage("Error");
                    }
                });
            }else if(!friendship.isAccepted() &&friendship.getReceiver().getId().equals(currentUserID)) {
                //Aceptar
                api.acceptFriendship(authToken, friendship, new CreateFriendshipCallback() {
                    @Override
                    public void onSuccess(Friendship friendship) {
                        ProfileActivityController.this.friendship = friendship;
                        noFriendship = false;
                        view.showMessage(view.getContext().getString(R.string.friendship_accepted));
                    }

                    @Override
                    public void onError() {
                        view.showMessage("Error");
                    }
                });
            }else if(!friendship.isAccepted()){
                api.denyFriendship(authToken, friendship, new CreateFriendshipCallback() {
                    @Override
                    public void onSuccess(Friendship friendship) {
                        ProfileActivityController.this.friendship = null;
                        noFriendship = true;
                        view.showMessage(view.getContext().getString(R.string.friendship_denied));
                    }

                    @Override
                    public void onError() {
                        view.showMessage("Error");
                    }
                });
            }
        }



        if (option == 1){
            //Connection
            if (connection == null){
                //api.add
                api.follow(authToken, userId, new GetConnectionCallback() {
                    @Override
                    public void onDataReceived(Connection connection) {
                        ProfileActivityController.this.connection =connection;
                        noConnection = false;
                        view.showMessage(view.getContext().getString(R.string.follow_ok));
                    }
                });
            } else {
                api.unfollow(authToken, userId, new GetConnectionCallback() {
                    @Override
                    public void onDataReceived(Connection connection) {
                        ProfileActivityController.this.connection =null;
                        noConnection = true;
                        view.showMessage(view.getContext().getString(R.string.unfollow_ok));
                    }
                });
            }
        }
    }

    public CharSequence[] getFriendshipOptions(){
        String userID = Utils.getUserId(view.getContext());
        CharSequence options[] = new CharSequence[2];
        //{"Enviar solicitud", "Seguir usuario"}
        if (friendship == null){
            options[0] = "Enviar solicitud";
        }
        if (friendship != null && !friendship.isAccepted() && !friendship.isRevoked() && friendship.getUser().getId().equals(userID)){
            options[0] = "Cancelar solicitud";
        }
        if (friendship != null && !friendship.isAccepted() && !friendship.isRevoked() && friendship.getReceiver().getId().equals(userID)){
            options[0] = "Aceptar solicitud";
        }
        if (friendship != null && friendship.isAccepted() && !friendship.isRevoked()){
            options[0] = "Dejar amistad";
        }

        if (connection == null){
            options[1] = "Seguir usuario";
        }

        if (connection != null){
            options[1] = "No seguir usuario";
        }
        return options;
    }

    public void showFriendshipOptions(){
        boolean friendshipOk = false;
        if (friendship != null || noFriendship) friendshipOk = true;
        boolean connectionOk = false;
        if (connection != null || noConnection) connectionOk = true;
        if (friendshipOk && connectionOk){
            view.showFriendship(friendship, connection);
        }
    }

    private void dataLoaded(){
        boolean friendshipOk = false;
        if (friendship != null || noFriendship) friendshipOk = true;
        boolean connectionOk = false;
        if (connection != null || noConnection) connectionOk = true;
        if (friendshipOk && connectionOk){
        }
    }

}
