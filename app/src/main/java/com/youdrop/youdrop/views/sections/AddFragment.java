package com.youdrop.youdrop.views.sections;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.youdrop.youdrop.controllers.PublishController;
import com.youdrop.youdrop.data.Publication;
import com.youdrop.youdrop.views.MainActivity;
import com.youdrop.youdrop.views.publications.PublicationActivity;
import com.youdrop.youdrop.views.publications.PublishActivity;
import com.youdrop.youdrop.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AddFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddFragment newInstance(String param1, String param2) {
        AddFragment fragment = new AddFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        ButterKnife.bind(this, view);
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



    @OnClick(R.id.foto)
    public void photoButtonCLicked(Button view) {
        // call login
        Intent i = new Intent(getActivity(), PublishActivity.class);
        Bundle args = new Bundle();
        args.putString(PublishActivity.ARG_TYPE, "PHOTO");
        i.putExtras(args);
        startActivityForResult(i, 999);

    }


    @OnClick(R.id.video)
    public void videoButtonCLicked(Button view) {
        // call login
        Intent i = new Intent(getActivity(), PublishActivity.class);
        Bundle args = new Bundle();
        args.putString(PublishActivity.ARG_TYPE, "VIDEO");
        i.putExtras(args);
        startActivityForResult(i, 999);

    }

    @OnClick(R.id.videogallery)
    public void videoGalleryButtonCLicked(Button view) {
        // call login
        Intent i = new Intent(getActivity(), PublishActivity.class);
        Bundle args = new Bundle();
        args.putString(PublishActivity.ARG_TYPE, "VIDEO_GALLERY");
        i.putExtras(args);
        startActivityForResult(i, 999);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 999 && resultCode == Activity.RESULT_OK){
            Publication p = (Publication) data.getSerializableExtra("publication");

            Intent i = new Intent(getActivity(), PublicationActivity.class);
            Bundle args = new Bundle();
            args.putSerializable(PublicationActivity.ARG_CONTENT, p);
            i.putExtras(args);
            startActivity(i);
            try{
                ((MainActivity)getActivity()).showNearPanel();
            }catch (Exception e){

            }
        }
    }
}
