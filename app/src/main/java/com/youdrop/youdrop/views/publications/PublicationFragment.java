package com.youdrop.youdrop.views.publications;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;
import com.youdrop.youdrop.controllers.LockedPublicationController;
import com.youdrop.youdrop.controllers.UnlockedPublicationController;
import com.youdrop.youdrop.data.Comment;
import com.youdrop.youdrop.data.Like;
import com.youdrop.youdrop.data.Publication;
import com.youdrop.youdrop.views.MaterialPreviewActivity;
import com.youdrop.youdrop.views.ProfileActivity;
import com.youdrop.youdrop.views.adapters.CommentRecyclerViewAdapter;
import com.youdrop.youdrop.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PublicationLockedFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PublicationLockedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PublicationFragment extends Fragment implements UnlockedPublicationView {
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
    @BindView(R.id.image_preview)
    ImageView imagePreview;
    @BindView(R.id.video_preview)
    VideoView videoPreview;

    @BindView(R.id.likesAction)
    Button likesAction;
    @BindView(R.id.commentsAction)
    Button commentsAction;
    @BindView(R.id.shareAction)
    Button shareAction;
    @BindView(R.id.moreActions)
    Button moreActions;

    @BindView(R.id.commentList)
    RecyclerView commentList;

    @BindView(R.id.message)
    TextView message;

    @BindView(R.id.progressBar)
    ProgressBar progress;
    @BindView(R.id.loading_video)
    TextView loading_video;
    @BindView(R.id.commentsContainer)
    LinearLayout commentsContainer;

    @BindView(R.id.scrollView)
    NestedScrollView scrollView;

    @BindView(R.id.commentBody)
    TextInputEditText commentBody;

    @BindView(R.id.sendComment)
    Button sendComment;

    private OnFragmentInteractionListener mListener;

    UnlockedPublicationController controller;

    public PublicationFragment() {
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
    public static PublicationFragment newInstance(Publication param1, Location param2) {
        PublicationFragment fragment = new PublicationFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        args.putParcelable(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        controller = new UnlockedPublicationController(this);
        if (getArguments() != null) {
            Publication publication = (Publication) getArguments().getSerializable(ARG_PARAM1);
            Location location = getArguments().getParcelable(ARG_PARAM2);
            controller.setPublication(publication);
            controller.setLocation(location);
        }

    }

    @OnClick(R.id.image_preview)
    void preview(){
        Intent i = new Intent(getActivity(), MaterialPreviewActivity.class);
        Bundle args = new Bundle();
        args.putString("url", imagePreviewUrl);
        i.putExtras(args);
        startActivity(i);
    }

    private String imagePreviewUrl = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_publication, container, false);
        // Set the adapter
        ButterKnife.bind(this, view);
        controller.reload();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        avatarIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.gotoProfile();
            }
        });
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
        commentsAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.showComments();
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
        moreActions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence options[] = controller.getOptions();

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(R.string.more_actions);
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        controller.optionClicked(which);
                    }
                });
                builder.show();
            }
        });
        sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.addComment(commentBody.getText().toString());
            }
        });
        commentList.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter =new CommentRecyclerViewAdapter(new ArrayList<Comment>(), controller, getActivity());
            commentList.setAdapter(adapter);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void askDelete() {
android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        builder.setMessage("Borrar publicación?")
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        controller.deletePublication();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
         builder.create();
    }

    @Override
    public void askReport() {
        final CharSequence options[] = new CharSequence[]{"Contenido inapropiado","Acoso o intimidación", "Violación de la ley", "Comportamiento inadmisible"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.report);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                controller.reportClicked(options[which].toString());
            }
        });
        builder.show();
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
        message.setText(publication.getContent());
        message.setVisibility(View.VISIBLE);
        imagePreview.setVisibility(View.GONE);
        videoPreview.setVisibility(View.GONE);

        if (publication.getFile() != null && "image/jpeg".equals(publication.getFile().getMimetype()) ){
            Picasso.with(getActivity()).load("http://api.you-drop.com/files/" +publication.getFile().getId()  + "/image/thumbnail").into(imagePreview);
            videoPreview.setVisibility(View.GONE);
           // imagePreview.setImageResource(R.drawable.amigos);
            imagePreview.setVisibility(View.VISIBLE);
            imagePreviewUrl = "http://api.you-drop.com/files/" +publication.getFile().getId()  + "/image/large";
        }

       if (publication.getFile() != null && "mp4".equals(publication.getFile().getMimetype()) ){
           // videoPreview.setVideoPath("http://api.you-drop.com/files/content/" +publication.getFile().getId()+ ".mp4");
           // videoPreview.start();
           progress.setVisibility(View.VISIBLE);
           loading_video.setVisibility(View.VISIBLE);
           // imagePreview.setVisibility(View.GONE);
        }
      /*   if (publication.getVideoUrl() != null && !"/videos/original/missing.png".equals(publication.getVideoUrl())){
            videoPreview.setVideoPath("http://api.you-drop.com" + publication.getVideoUrl());
            videoPreview.setVisibility(View.VISIBLE);
        }*/
    }

    @Override
    public void showVideo(String file) {
        videoPreview.setVideoPath(file);
         videoPreview.start();
        videoPreview.setVisibility(View.VISIBLE);
        imagePreview.setVisibility(View.GONE);
        progress.setVisibility(View.GONE);
        loading_video.setVisibility(View.GONE);

    }

    public void setCommentList(List<Comment> comments) {
            adapter.getmValues().clear();
            adapter.getmValues().addAll(comments);
            adapter.notifyDataSetChanged();

    }

    @Override
    public void addComment(Comment comment) {
        commentBody.setText("");
        adapter.getmValues().add(comment);
        adapter.notifyItemInserted(adapter.getmValues().size()-1);
    }

    @Override
    public void toggleComments(boolean show) {
        if (show){
            commentsContainer.setVisibility(View.VISIBLE);
            scrollView.fullScroll(View.FOCUS_DOWN);
        } else {
            commentsContainer.setVisibility(View.GONE);
        }
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
}
