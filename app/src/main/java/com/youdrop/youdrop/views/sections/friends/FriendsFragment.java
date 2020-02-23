package com.youdrop.youdrop.views.sections.friends;

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
import com.youdrop.youdrop.controllers.FriendsController;
import com.youdrop.youdrop.data.Notification;
import com.youdrop.youdrop.data.User;
import com.youdrop.youdrop.views.adapters.FriendRecyclerViewAdapter;
import com.youdrop.youdrop.views.adapters.UserRecyclerViewAdapter;
import com.youdrop.youdrop.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A fragment representing a list of Items.
 * <p/>
 * interface.
 */
public class FriendsFragment extends Fragment implements FriendsView,SwipeRefreshLayout.OnRefreshListener {

    private static final String ARG_AUTH_TOKEN = "authToken";
    private String authToekn;
    private FriendRecyclerViewAdapter adapter;

    private FriendsController controller;
    @BindView(R.id.emptyView)
    LinearLayout emptyView;
    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.list)
    RecyclerView list;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FriendsFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static FriendsFragment newInstance(String token) {
        FriendsFragment fragment = new FriendsFragment();
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
        controller = new FriendsController(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends_list, container, false);
        View list = view.findViewById(R.id.list);
        ButterKnife.bind(this, view);

        // Set the adapter
        if (list instanceof RecyclerView) {
            Context context = list.getContext();
            RecyclerView recyclerView = (RecyclerView) list;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            adapter =new FriendRecyclerViewAdapter(new ArrayList<User>(), controller, getActivity());
            recyclerView.setAdapter(adapter);
        }


        controller.reload();

// onClickButton listener detects any click performed on buttons by touch
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setEnabled(false);
        ContentCache.getInstance(getContext()).setFriends(null);
        controller.reload();

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public Context getContext() {
        return getActivity().getApplicationContext();
    }

    @Override
    public void addUsersToList(List<User> notifications) {
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
        if (showEmpty){
            emptyView.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setVisibility(View.GONE);
        }else{
            emptyView.setVisibility(View.GONE);
            swipeRefreshLayout.setVisibility(View.VISIBLE);

        }

    }
}
