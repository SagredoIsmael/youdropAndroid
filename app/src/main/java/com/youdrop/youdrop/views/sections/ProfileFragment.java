package com.youdrop.youdrop.views.sections;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.youdrop.youdrop.controllers.ProfileController;
import com.youdrop.youdrop.data.User;
import com.youdrop.youdrop.data.UserImage;
import com.youdrop.youdrop.views.adapters.UserImageRecyclerViewAdapter;
import com.youdrop.youdrop.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements ProfileView {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_USERID = "userId";
    public static final String CURRENT_USER = null;

    private ProfileController controller;

    // TODO: Rename and change types of parameters
    private String userId;

    private OnFragmentInteractionListener mListener;

    @BindView(R.id.avatar)
    CircularImageView avatar;

    @BindView(R.id.nickname)
    TextView nickname;
    @BindView(R.id.name) TextView name;
    @BindView(R.id.description) TextView description;
    @BindView(R.id.num_publications) TextView numPublications;
    @BindView(R.id.num_likes) TextView numLikes;
    @BindView(R.id.num_active_publications) TextView numActivePublications;
    @BindView(R.id.commentList)
    RecyclerView commentList;

    UserImageRecyclerViewAdapter adapter;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param userId Parameter 1.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String userId) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USERID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getString(ARG_USERID);
        }
        controller = new ProfileController(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (userId == CURRENT_USER) {
            controller.loadFromCurrentUser();
        } else {
            controller.loadFromUserId(userId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        Context context = commentList.getContext();
        commentList.setLayoutManager(new LinearLayoutManager(context));
        adapter =new UserImageRecyclerViewAdapter(new ArrayList<UserImage>(), controller, getActivity());
        commentList.setAdapter(adapter);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void setUserData(User user) {
        if (user == null) return;
        nickname.setText(user.getName());
        name.setText(user.getName());
        description.setText(user.getDescription());
        numPublications.setText("0"  );
        numActivePublications.setText("0" );
        numLikes.setText("0" );
        if (user.getAvatar() != null){
            Picasso.with(getContext()).load("http://api.you-drop.com/files/" +user.getAvatar().getId() + "/image/thumbnail").resize(80, 80).into(avatar);
        }
    }

    @Override
    public void setUserImages(List<UserImage> images) {
        adapter.getmValues().clear();
        adapter.getmValues().addAll(images);
        adapter.notifyDataSetChanged();
    }
}
