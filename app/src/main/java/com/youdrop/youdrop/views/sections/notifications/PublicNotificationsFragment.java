package com.youdrop.youdrop.views.sections.notifications;

import android.content.Context;
import android.content.Intent;
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
import com.youdrop.youdrop.controllers.PublicNotificationsController;
import com.youdrop.youdrop.data.Publication;
import com.youdrop.youdrop.views.adapters.ContentRecyclerViewAdapter;
import com.youdrop.youdrop.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link }
 * interface.
 */
public class PublicNotificationsFragment extends Fragment implements PublicNotificationsView, SwipeRefreshLayout.OnRefreshListener{

    private static final String ARG_AUTH_TOKEN = "authToken";
    private String authToekn;
    private ContentRecyclerViewAdapter adapter;

    private PublicNotificationsController controller;
    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.emptyView)
    LinearLayout emptyView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PublicNotificationsFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static PublicNotificationsFragment newInstance(String token) {
        PublicNotificationsFragment fragment = new PublicNotificationsFragment();
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
        controller = new PublicNotificationsController(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification_list, container, false);
        View list = view.findViewById(R.id.list);
        // Set the adapter
        if (list instanceof RecyclerView) {
            Context context = list.getContext();
            RecyclerView recyclerView = (RecyclerView) list;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            adapter =new ContentRecyclerViewAdapter(new ArrayList<Publication>(), controller, getActivity());
            recyclerView.setAdapter(adapter);
        }

        controller.reload();
        ButterKnife.bind(this, view);


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

    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setEnabled(false);
        ContentCache.getInstance(getContext()).clearNotifications();
        controller.clear();
        controller.reload();

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public Context getContext() {
        return getActivity().getApplicationContext();
    }

    @Override
    public void addContentToList(List<Publication> publication) {
        adapter.getmValues().addAll(publication);
        adapter.notifyDataSetChanged();
        showEmpty(false);
    }


    @Override
    public void clearList() {
        adapter.getmValues().clear();
        adapter.notifyDataSetChanged();
        showEmpty(true);

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

    @Override
    public void onResume() {
        super.onResume();
        controller.resume();

    }

    @Override
    public void onPause() {
        super.onPause();
        controller.pause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        controller.clear();
        controller.reload();
    }
}
