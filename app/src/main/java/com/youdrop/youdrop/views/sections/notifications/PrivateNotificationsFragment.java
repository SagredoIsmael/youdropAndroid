package com.youdrop.youdrop.views.sections.notifications;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.youdrop.youdrop.api.ContentCache;
import com.youdrop.youdrop.controllers.PrivateNotificationsController;
import com.youdrop.youdrop.data.Notification;
import com.youdrop.youdrop.views.adapters.NotificationRecyclerViewAdapter;
import com.youdrop.youdrop.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnNotificationInteractionListener}
 * interface.
 */
public class PrivateNotificationsFragment extends Fragment implements PrivateNotificationsView, SwipeRefreshLayout.OnRefreshListener{

    private static final String ARG_AUTH_TOKEN = "authToken";
    private String authToekn;
    private OnNotificationInteractionListener mListener;
    private NotificationRecyclerViewAdapter adapter;

    private PrivateNotificationsController controller;
    @BindView(R.id.swipeContainer) SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.emptyView)
    LinearLayout emptyView;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PrivateNotificationsFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static PrivateNotificationsFragment newInstance(String token) {
        PrivateNotificationsFragment fragment = new PrivateNotificationsFragment();
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
        controller = new PrivateNotificationsController(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification_list, container, false);
        ButterKnife.bind(this, view);

        View list = view.findViewById(R.id.list);
        // Set the adapter
        if (list instanceof RecyclerView) {
            Context context = list.getContext();
            RecyclerView recyclerView = (RecyclerView) list;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            adapter =new NotificationRecyclerViewAdapter(new ArrayList<Notification>(), controller, getActivity());
            recyclerView.setAdapter(adapter);
        }

        controller.reload();


// onClickButton listener detects any click performed on buttons by touch
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnNotificationInteractionListener) {
            mListener = (OnNotificationInteractionListener) context;
        } else {
           // throw new RuntimeException(context.toString() + " must implement OnContentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setEnabled(false);
        ContentCache.getInstance(getContext()).setPrivateNotifications(null);
        controller.clear();
        controller.reload();

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
    public void addNotificationsToList(List<Notification> notifications) {
        adapter.getmValues().addAll(notifications);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void clearList() {
        adapter.getmValues().clear();
        adapter.notifyDataSetChanged();

    }

    @Override
    public void showEmpty(boolean showEmpty) {
        try{
            if (showEmpty){
                emptyView.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setVisibility(View.GONE);
            }else{
                emptyView.setVisibility(View.GONE);
                swipeRefreshLayout.setVisibility(View.VISIBLE);

            }
        }catch (Exception e){

        }

    }
}
