package com.youdrop.youdrop.views.sections;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.youdrop.youdrop.controllers.FriendsTabController;
import com.youdrop.youdrop.data.Notification;
import com.youdrop.youdrop.views.sections.friends.ConversationsFragment;
import com.youdrop.youdrop.views.sections.friends.FriendsFragment;
import com.youdrop.youdrop.views.sections.notifications.PublicNotificationsFragment;
import com.youdrop.youdrop.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.ceryle.radiorealbutton.RadioRealButton;
import co.ceryle.radiorealbutton.RadioRealButtonGroup;

/**
 * A fragment representing a list of Items.
 * <p/>
 * interface.
 */
public class FriendsTabFragment extends Fragment implements FriendsTabView {

    private static final String ARG_AUTH_TOKEN = "authToken";
    private String authToekn;

    private FriendsTabController controller;

    private boolean showFriends = true;

    @BindView(R.id.fragmentContentContainer) View mContentView;
    @BindView(R.id.radioRealButtonGroup) RadioRealButtonGroup selector;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FriendsTabFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static FriendsTabFragment newInstance(String token) {
        FriendsTabFragment fragment = new FriendsTabFragment();
        Bundle args = new Bundle();
        args.putString(ARG_AUTH_TOKEN, token);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            authToekn = getArguments().getString(ARG_AUTH_TOKEN);
        }
        controller = new FriendsTabController(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends_tab, container, false);
        // Set the adapter
        ButterKnife.bind(this, view);
        RadioRealButtonGroup group = (RadioRealButtonGroup) view.findViewById(R.id.radioRealButtonGroup);
        group.setPosition(0);

        controller.showConversations();
// onClickButton listener detects any click performed on buttons by touch
        group.setOnClickedButtonListener(new RadioRealButtonGroup.OnClickedButtonListener() {
            @Override
            public void onClickedButton(RadioRealButton button, int position) {
                if (position == 0)controller.showConversations();;
                if (position == 1)controller.showFriends();;
            }
        });
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnNotificationInteractionListener {
        // TODO: Update argument type and name
        void onNotificationInteraction(Notification item);
    }

    @Override
    public void onResume() {
        super.onResume();
        reload();
    }

    @Override
    public Context getContext() {
        return getActivity().getApplicationContext();
    }

    @Override
    public void showFriends() {
        selector.setPosition(1);
        showFriends = true;
        getFragmentManager().beginTransaction().replace(R.id.fragmentContentContainer, FriendsFragment.newInstance(authToekn)).commit();

    }

    @Override
    public void showXats() {
        selector.setPosition(0);
        getFragmentManager().beginTransaction().replace(R.id.fragmentContentContainer, ConversationsFragment.newInstance(authToekn)).commit();
        showFriends = false;
    }

    @Override
    public void reload() {
        if (showFriends){
            showFriends();
        } else {
            showXats();
        }
    }
}
