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
import com.youdrop.youdrop.controllers.ConversationsController;
import com.youdrop.youdrop.controllers.FriendsController;
import com.youdrop.youdrop.data.Conversation;
import com.youdrop.youdrop.data.User;
import com.youdrop.youdrop.views.adapters.ConversationRecyclerViewAdapter;
import com.youdrop.youdrop.views.adapters.UserRecyclerViewAdapter;
import com.youdrop.youdrop.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ConversationsFragment extends Fragment implements ConversationsView,SwipeRefreshLayout.OnRefreshListener {

    private static final String ARG_AUTH_TOKEN = "authToken";
    private String authToekn;
    private OnConversationInteractionListener mListener;
    private ConversationRecyclerViewAdapter adapter;

    private ConversationsController controller;

    private RecyclerView list;
    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.emptyView)
    LinearLayout emptyView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ConversationsFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ConversationsFragment newInstance(String token) {
        ConversationsFragment fragment = new ConversationsFragment();
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
        controller = new ConversationsController(this);
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
        list = (RecyclerView) view.findViewById(R.id.list);
        ButterKnife.bind(this, view);
        // Set the adapter
        if (list instanceof RecyclerView) {
            Context context = list.getContext();
            list = (RecyclerView) list;
            list.setLayoutManager(new LinearLayoutManager(context));
            adapter =new ConversationRecyclerViewAdapter(new ArrayList<Conversation>(), controller, getActivity());
            list.setAdapter(adapter);
        }

        controller.reload();


// onClickButton listener detects any click performed on buttons by touch
        return view;
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setEnabled(false);
        ContentCache.getInstance(getContext()).setConversations(null);
        controller.reload();

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    public interface OnConversationInteractionListener {
        // TODO: Update argument type and name
        void onConversationInteraction(Conversation item);
    }

    @Override
    public Context getContext() {
        return getActivity().getApplicationContext();
    }

    @Override
    public void addConversationsToList(List<Conversation> notifications) {
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
