package com.youdrop.youdrop.views.sections;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.youdrop.youdrop.controllers.NotificationsController;
import com.youdrop.youdrop.data.Notification;
import com.youdrop.youdrop.views.sections.notifications.PrivateNotificationsFragment;
import com.youdrop.youdrop.views.sections.notifications.PublicNotificationsFragment;
import com.youdrop.youdrop.views.adapters.NotificationRecyclerViewAdapter;
import com.youdrop.youdrop.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.ceryle.radiorealbutton.RadioRealButton;
import co.ceryle.radiorealbutton.RadioRealButtonGroup;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnNotificationInteractionListener}
 * interface.
 */
public class NotificationsFragment extends Fragment implements NotificationsView{

    private static final String ARG_AUTH_TOKEN = "authToken";
    private String authToekn;

    private NotificationsController controller;

    private boolean showPrivate = true;

    @BindView(R.id.fragmentNotificationsContainer) View mContentView;
    @BindView(R.id.radioRealButtonGroup) RadioRealButtonGroup selector;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NotificationsFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static NotificationsFragment newInstance(String token) {
        NotificationsFragment fragment = new NotificationsFragment();
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
        controller = new NotificationsController(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        // Set the adapter
        ButterKnife.bind(this, view);
        RadioRealButtonGroup group = (RadioRealButtonGroup) view.findViewById(R.id.radioRealButtonGroup);
        group.setPosition(0);

        controller.showPrivateNotifications();
// onClickButton listener detects any click performed on buttons by touch
        group.setOnClickedButtonListener(new RadioRealButtonGroup.OnClickedButtonListener() {
            @Override
            public void onClickedButton(RadioRealButton button, int position) {
                if (position == 0)controller.showPrivateNotifications();;
                if (position == 1)controller.showPublicNotifications();;
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
    public Context getContext() {
        return getActivity().getApplicationContext();
    }

    @Override
    public void showPrivateNotifications() {
        selector.setPosition(0);
        showPrivate = true;
        getFragmentManager().beginTransaction().replace(R.id.fragmentNotificationsContainer, PrivateNotificationsFragment.newInstance(authToekn)).commit();

    }

    @Override
    public void showPublicNotifications() {
        selector.setPosition(1);
        getFragmentManager().beginTransaction().replace(R.id.fragmentNotificationsContainer, PublicNotificationsFragment.newInstance(authToekn)).commit();
        showPrivate = false;
    }

    @Override
    public void reload() {
        if (showPrivate){
            showPrivateNotifications();
        } else {
            showPublicNotifications();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        reload();
    }
}
