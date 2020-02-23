package com.youdrop.youdrop.views.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.youdrop.youdrop.data.Notification;
import com.youdrop.youdrop.data.User;
import com.youdrop.youdrop.data.UserImage;
import com.youdrop.youdrop.views.sections.NotificationsFragment;
import com.youdrop.youdrop.views.sections.friends.FriendsFragment;
import com.youdrop.youdrop.R;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Notification} and makes a call to the
 * specified {@link NotificationsFragment.OnNotificationInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class UserImageRecyclerViewAdapter extends RecyclerView.Adapter<UserImageRecyclerViewAdapter.ViewHolder> {

    private final List<UserImage> mValues;
    private final OnUserImageInteractionListener mListener;

    private Context context;

    public UserImageRecyclerViewAdapter(List<UserImage> items, OnUserImageInteractionListener listener, Context c) {
        mValues = items;
        mListener = listener;
        context = c;
    }

    public List<UserImage> getmValues() {
        return mValues;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_user_image_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        UserImage n = mValues.get(position);
        holder.mTitleView.setText("" + n.getBody());
        if (n.getFile() != null){
            Picasso.with(context).load("http://api.you-drop.com/files/" +n.getFile().getId() + "/image/thumbnail" ).resize(1024, 512).centerCrop().into(holder.mImageView);
        }


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onUserImageInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitleView;
        public final ImageView mImageView;
        public UserImage mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleView = (TextView) view.findViewById(R.id.title);
            mImageView = (ImageView)view.findViewById(R.id.imageView);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitleView.getText() + "'";
        }
    }

    public interface OnUserImageInteractionListener {
        // TODO: Update argument type and name
        void onUserImageInteraction(UserImage item);
    }
}
