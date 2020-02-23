package com.youdrop.youdrop.views.publications;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;
import com.youdrop.youdrop.controllers.LockedPublicationController;
import com.youdrop.youdrop.data.Comment;
import com.youdrop.youdrop.data.Like;
import com.youdrop.youdrop.data.Publication;
import com.youdrop.youdrop.views.ProfileActivity;
import com.youdrop.youdrop.views.adapters.CommentRecyclerViewAdapter;
import com.youdrop.youdrop.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PublicationLockedFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PublicationLockedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PublicationLockedFragment extends Fragment implements LockedPublicationView {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "content";
    private static final String ARG_PARAM2 = "location";
    private CommentRecyclerViewAdapter adapter;


    @BindView(R.id.avatarIcon)
    ImageView avatarIcon;
    @BindView(R.id.categoryIcon)
    ImageView categoryIcon;
    @BindView(R.id.userName)
    TextView userName;
    @BindView(R.id.locationText)
    TextView locationText;


    @BindView(R.id.likesAction)
    Button likesAction;
    @BindView(R.id.commentsAction)
    Button commentsAction;
    @BindView(R.id.shareAction)
    Button shareAction;



    @BindView(R.id.mapView)
    MapView mMapView;
    private GoogleMap googleMap;

    private OnFragmentInteractionListener mListener;

    LockedPublicationController controller;

    public PublicationLockedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PublicationLockedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PublicationLockedFragment newInstance(Publication param1, Location param2) {
        PublicationLockedFragment fragment = new PublicationLockedFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        args.putParcelable(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        controller = new LockedPublicationController(this);
        if (getArguments() != null) {
            Publication publication = (Publication) getArguments().getSerializable(ARG_PARAM1);
            Location location = getArguments().getParcelable(ARG_PARAM2);
            controller.setPublication(publication);
            controller.setLocation(location);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_publication_locked, container, false);
        // Set the adapter
        ButterKnife.bind(this, view);
        controller.reload();


    mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
        MapsInitializer.initialize(getActivity().getApplicationContext());
    } catch (Exception e) {
        e.printStackTrace();
    }

        mMapView.getMapAsync(new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap mMap) {
            googleMap = mMap;
            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            getActivity(), R.raw.map_style));
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // Check Permissions Now
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        2);

                return;
            }
            googleMap.setMyLocationEnabled(true);
            // For showing a move to my location button
            // googleMap.setMyLocationEnabled(true);
            MarkerOptions options = new MarkerOptions();
            LatLng pos = new LatLng(controller.getPublication().getLocation().getLatitude(), controller.getPublication().getLocation().getLongitude());
            options.position(pos);
            googleMap.addMarker(options);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos,
                    13));

        }
    });
        controller.reload();
        return view;
}



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        likesAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.likeActionClicked();
            }
        });
        likesAction.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                controller.showLikes();
                return true;
            }
        });
        shareAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.sharePublication();
            }
        });
        shareAction.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });
        avatarIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.gotoProfile();
            }
        });
    }

    @Override
    public void showLikes(final List<Like> items) {
        CharSequence[] list = new CharSequence[items.size()];
        int n = 0;
        for (Like like: items) {
            list[n] = like.getUser().getName();
            n++;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.likers)
                .setItems(list, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                       gotoProfile(items.get(which).getUser().getId());
                    }
                });
        builder.create().show();
    }

    private void gotoProfile(String userId){
        Intent i = new Intent(getActivity(), ProfileActivity.class);
        Bundle args = new Bundle();
        args.putString(ProfileActivity.ARG_USERID, userId);
        i.putExtras(args);
        getActivity().startActivity(i);
    }

    @Override
    public void setLikeStatus(boolean status) {
        if (status){
            likesAction.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.dislike_button, 0);

        } else {
            likesAction.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.like_button, 0);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
            mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
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
      /*  if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
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
    public void setupViewData(Publication publication, Location location) {
        if (publication.getUser() != null && publication.getUser().getAvatar() != null && !publication.isAnonymous()){
            Picasso.with(getActivity()).load("http://api.you-drop.com/files/" +publication.getUser().getAvatar().getId()  + "/image/thumbnail").resize(120, 80).into(avatarIcon);
        } else {
            avatarIcon.setImageResource(R.drawable.perfil);
        }
        if (publication.getCategory() != null){
            Picasso.with(getActivity()).load(publication.getCategory().getIcon()).into(categoryIcon);
        }
        if (publication.getUser()!= null && !publication.isAnonymous()){
            userName.setText(publication.getUser().getName());
        } else {
            userName.setText(R.string.anonymous);
        }
        Location contentLocation = new Location("");
        contentLocation.setLatitude(publication.getLocation().getLatitude());
        contentLocation.setLongitude(publication.getLocation().getLongitude());
        float distance = location.distanceTo(contentLocation)/ 1000;
        locationText.setText(getResources().getString(R.string.location_message, distance));
    }

    @Override
    public void setCommentList(List<Comment> comments) {
     /*   Context context = commentList.getContext();
        commentList.setLayoutManager(new LinearLayoutManager(context));
        if (adapter == null){
            adapter =new CommentRecyclerViewAdapter(comments, controller, getActivity());
            commentList.setAdapter(adapter);
        }else {
            adapter.getmValues().addAll(comments);
            adapter.notifyDataSetChanged();
        }*/
    }


    @Override
    public void setNumberOfComments(int number) {
        commentsAction.setText(""+ number);
    }

    @Override
    public void setNumberOfLikes(int number) {
        likesAction.setText(""+ number);
    }

    @Override
    public Context getContext() {
        return super.getActivity();
    }
}
