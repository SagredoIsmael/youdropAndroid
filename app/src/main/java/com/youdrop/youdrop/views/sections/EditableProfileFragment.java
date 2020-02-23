package com.youdrop.youdrop.views.sections;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.youdrop.youdrop.controllers.EditableProfileController;
import com.youdrop.youdrop.controllers.ProfileController;
import com.youdrop.youdrop.data.User;
import com.youdrop.youdrop.data.UserImage;
import com.youdrop.youdrop.views.LoginActivity;
import com.youdrop.youdrop.views.MainActivity;
import com.youdrop.youdrop.views.adapters.UserImageRecyclerViewAdapter;
import com.youdrop.youdrop.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditableProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditableProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditableProfileFragment extends Fragment implements EditableProfileView, View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_USERID = "userId";
    public static final String CURRENT_USER = null;

    private EditableProfileController controller;

    // TODO: Rename and change types of parameters
    private String userId;

    private OnFragmentInteractionListener mListener;

    @BindView(R.id.avatarChange)
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
    @BindView(R.id.edit) ImageButton edit;
    @BindView(R.id.logOut) ImageButton logOut;
    @BindView(R.id.addFoto) ImageButton addFoto;

    UserImageRecyclerViewAdapter adapter;

    public EditableProfileFragment() {
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
    public static EditableProfileFragment newInstance(String userId) {
        EditableProfileFragment fragment = new EditableProfileFragment();
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
        controller = new EditableProfileController(this);

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
        View view = inflater.inflate(R.layout.fragment_editable_profile, container, false);
        ButterKnife.bind(this, view);
        Context context = commentList.getContext();
        commentList.setLayoutManager(new LinearLayoutManager(context));
        adapter =new UserImageRecyclerViewAdapter(new ArrayList<UserImage>(), controller, getActivity());
        commentList.setAdapter(adapter);
        avatar.setOnClickListener(this);
        addFoto.setOnClickListener(this);
        edit.setOnClickListener(this);
        logOut.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        if (v == avatar){
            CharSequence options[] = new CharSequence[]{"Foto","Galeria"};

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(R.string.avatar_change);
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == 0)controller.changeAvatarPhoto();
                    if (which == 1)controller.changeAvatarGallery();
                }
            });
            builder.show();
        }
        if (v == addFoto){
            CharSequence options[] = new CharSequence[]{"Foto","Galeria"};

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(R.string.avatar_change);
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == 0)controller.addUserImagePhoto();
                    if (which == 1)controller.addUserImageGallery();
                }
            });
            builder.show();
        }
        if (v == edit){
            controller.showEditUser();
        }
        if (v == logOut){
            controller.logOut();
            LoginManager.getInstance().logOut();
            getActivity().finish();
            Intent intent = ((MainActivity)getActivity()).getIntent();
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    @Override
    public void editUser(String nameValue, String usernameValue, String emailValue, String descr) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.avatar_change);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edit_account, null);
        builder.setView(view);
        final EditText name = view.findViewById(R.id.name);
        final EditText username = view.findViewById(R.id.username);
        final EditText email = view.findViewById(R.id.email);
        final EditText description = view.findViewById(R.id.description);
        name.setText(nameValue);
        username.setText(usernameValue);
        email.setText(emailValue);
        description.setText(descr);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                controller.editUser(name.getText().toString(),username.getText().toString(),email.getText().toString(), description.getText().toString());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
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
        nickname.setText(user.getUsername());
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

    @Override
    public void addUserImage(UserImage image) {
        adapter.getmValues().add(image);
        adapter.notifyItemInserted(adapter.getmValues().size() -1);
    }

    public static final int REQUEST_TAKE_PHOTO = 1;
    public static final int REQUEST_GET_PHOTO = 2;

    public static final int REQUEST_TAKE_PHOTO_FOR_IMAGES = 3;
    public static final int REQUEST_GET_PHOTO_FOR_IMAGES = 4;

    public void dispatchTakePictureIntent(File file) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            if (file != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(),
                        "com.youdrop.youdrop",
                        file);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    public void dispatchTakePictureIntentForImages(File file) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            if (file != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(),
                        "com.youdrop.youdrop",
                        file);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO_FOR_IMAGES);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //int current = mViewPager.getCurrentItem();
        //mViewPager.setCurrentItem(0);
        if (requestCode == EditableProfileFragment.REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            controller.processImage(null);
        }


        if (requestCode == EditableProfileFragment.REQUEST_GET_PHOTO && resultCode == Activity.RESULT_OK) {
            Uri mediaUri = data.getData();
            controller.processImage(mediaUri);
        }

        if (requestCode == EditableProfileFragment.REQUEST_TAKE_PHOTO_FOR_IMAGES && resultCode == Activity.RESULT_OK) {
            controller.processImageForUserImages(null);
        }


        if (requestCode == EditableProfileFragment.REQUEST_GET_PHOTO_FOR_IMAGES && resultCode == Activity.RESULT_OK) {
            Uri mediaUri = data.getData();
            controller.processImageForUserImages(mediaUri);
        }
    }

    @Override
    public void dispatchGetPictureIntent() {

        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Explain to the user why we need to read the contacts
            }

            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    67);

            // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
            // app-defined int constant that should be quite unique
            //finish();
            return;
        }

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), REQUEST_GET_PHOTO);

    }

    @Override
    public void dispatchGetPictureIntentForImages() {

        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Explain to the user why we need to read the contacts
            }

            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    67);

            // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
            // app-defined int constant that should be quite unique
            //finish();
            return;
        }

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), REQUEST_GET_PHOTO_FOR_IMAGES);

    }

    @Override
    public void processImage(Uri uri) {
        controller.processImage(uri);
    }

    ProgressDialog dialog;
    @Override
    public void showLoading(boolean show) {
        if (show){
            if (dialog == null || !dialog.isShowing()){
                dialog = ProgressDialog.show(getActivity(), "",
                        "Loading. Please wait...", true);
            }
        }else {
            if (dialog != null){
                dialog.dismiss();
            }
        }
    }
}
