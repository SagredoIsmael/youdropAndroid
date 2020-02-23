package com.youdrop.youdrop.views.publications;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.VideoView;

import com.fxn.pix.Pix;
import com.youdrop.youdrop.controllers.PublishController;
import com.youdrop.youdrop.data.Category;
import com.youdrop.youdrop.data.Publication;
import com.youdrop.youdrop.data.User;
import com.youdrop.youdrop.views.MaterialPreviewActivity;
import com.youdrop.youdrop.views.adapters.CategoryRecyclerViewAdapter;
import com.youdrop.youdrop.R;
import com.youdrop.youdrop.views.adapters.FriendRecyclerViewAdapter;
import com.youdrop.youdrop.views.adapters.UserRecyclerViewAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.ceryle.radiorealbutton.RadioRealButton;
import co.ceryle.radiorealbutton.RadioRealButtonGroup;

public class PublishActivity extends AppCompatActivity implements PublishView {

    @BindView(R.id.friendsSelector) AutoCompleteTextView friendsSelector;
    @BindView(R.id.radioRealButtonGroup) RadioRealButtonGroup visibilitySelector;
    @BindView(R.id.public_selector) RadioRealButton publicSelector;
    @BindView(R.id.private_selector)
    RadioRealButton privateSelector;
    @BindView(R.id.message)
    EditText message;
    @BindView(R.id.anonymous) Switch anonymousSwitch;
    @BindView(R.id.downloadable) Switch downloadableSwitch;
    @BindView(R.id.previewImage)
    ImageView previewImage;
    @BindView(R.id.previewVideo)
    VideoView previewVideo;

    @BindView(R.id.list)
    RecyclerView list;
    private UserRecyclerViewAdapter adapter;
    public static final String ARG_TYPE = "type";


    private PublishController controller;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);

        ButterKnife.bind(this);
        controller = new PublishController(this);
        String type = null;
        Uri mediaUri = null;
        if (getIntent() != null) {
            if (Intent.ACTION_SEND.equals(getIntent().getAction())) {
                if (getIntent().getExtras().containsKey(Intent.EXTRA_STREAM)) {
                    // Get resource path
                    // load image
                    mediaUri = getIntent().getExtras().getParcelable(Intent.EXTRA_STREAM);

                }
            }else {
                type = (String) getIntent().getStringExtra(ARG_TYPE);
            }

        }
        controller.init(type);
        if (mediaUri != null){
            controller.processPicture(mediaUri);
        }
        visibilitySelector.setOnClickedButtonListener(new RadioRealButtonGroup.OnClickedButtonListener() {
            @Override
            public void onClickedButton(RadioRealButton button, int position) {
                if (position == 0)controller.setContentPublic();;
                if (position == 1)controller.setContentPrivate();;
            }
        });
        previewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), MaterialPreviewActivity.class);
                Bundle args = new Bundle();
                args.putString("local", controller.mCurrentPhotoPath);
                i.putExtras(args);
                startActivity(i);
            }
        });
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        myToolbar.setNavigationIcon(R.drawable.places_ic_clear);

        setSupportActionBar(myToolbar);

        Context context = list.getContext();
        list.setLayoutManager(new LinearLayoutManager(context));
        adapter =new UserRecyclerViewAdapter(new ArrayList<User>(), controller, getActivity());
        list.setAdapter(adapter);
        friendsSelector.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                controller.friendSelected(friendsSelector.getText().toString());
                friendsSelector.setText("");
            }
        });

    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void setFriendList(List<String> users) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, users);

        friendsSelector.setAdapter(adapter);
    }

    @Override
    public void addSelectedFriend(User name) {
        adapter.getmValues().add(name);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void removeSelectedFriend(int pos) {
        adapter.getmValues().remove(pos);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setPrivateMode() {
        anonymousSwitch.setVisibility(View.GONE);
        friendsSelector.setVisibility(View.VISIBLE);
        visibilitySelector.setPosition(1);

    }

    @Override
    public void setPublicMode() {
        anonymousSwitch.setVisibility(View.VISIBLE);
        friendsSelector.setVisibility(View.GONE);
        visibilitySelector.setPosition(0);

    }

    @OnClick(R.id.anonymous)
    public void anonimousClick(Switch view) {
        // call login
        controller.setAnonymous(view.isChecked());

    }

    @OnClick(R.id.downloadable)
    public void downloadableClick(Switch view) {
        // call login
        controller.setDownloadable(view.isChecked());

    }

    public void setAnonymous(boolean value){
        anonymousSwitch.setChecked(value);
    }

    public void setDownloadable(boolean value){
        downloadableSwitch.setChecked(value);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_publish, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_add:
                controller.setValue(message.getText().toString());
                controller.save();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_GET_PHOTO = 2;

    public void dispatchTakePictureIntent(File file) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            if (file != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.youdrop.youdrop",
                        file);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    static final int REQUEST_VIDEO_CAPTURE = 3;

    public void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 12);
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    @Override
    public void dispatchGetPictureIntent() {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Explain to the user why we need to read the contacts
            }

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    67);

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    67);

            // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
            // app-defined int constant that should be quite unique
            finish();
            return;
        }

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Explain to the user why we need to read the contacts
            }

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    67);

            // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
            // app-defined int constant that should be quite unique
            //finish();
            return;
        }
        Pix.start(this,                    //Activity or Fragment Instance
                REQUEST_GET_PHOTO);


        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
     //   startActivityForResult(Intent.createChooser(intent,
       //         "Select Picture"), REQUEST_GET_PHOTO);

    }

    @Override
    public void dispatchGetVideoIntent() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Video"), REQUEST_VIDEO_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            controller.processPicture();
        }
        if (resultCode == RESULT_CANCELED) {
            finish();
        }

        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Uri videoUri = data.getData();
            //mVideoView.setVideoURI(videoUri);
            controller.processVideo(videoUri);
        }

        if (requestCode == REQUEST_GET_PHOTO && resultCode == RESULT_OK) {
            ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);

           // Uri mediaUri = data.getData();
            controller.processPicture(Uri.parse(returnValue.get(0)));
        }
    }

    @Override
    public void setPreview(Bitmap bitmap) {
        previewImage.setImageBitmap(bitmap);
        previewImage.setVisibility(View.VISIBLE);
        previewVideo.setVisibility(View.GONE);
    }

    @Override
    public void setPreviewVideo(String uri) {
        previewImage.setImageResource(R.drawable.video);
        previewImage.setVisibility(View.VISIBLE);
        previewVideo.setVisibility(View.GONE);
       // previewVideo.setVideoPath(uri);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void gotoContent(Publication c) {
      /*  Intent i = new Intent(getActivity(), PublicationActivity.class);
        Bundle args = new Bundle();
        args.putSerializable(PublicationActivity.ARG_CONTENT, c);
        i.putExtras(args);
        startActivity(i);
        */
        Intent data = new Intent();
        data.putExtra("publication",c);
      setResult(RESULT_OK, data);
      finish();
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
            dialog.dismiss();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        dispatchGetPictureIntent();
    }
}
