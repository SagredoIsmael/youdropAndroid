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
import com.youdrop.youdrop.views.sections.NotificationsFragment;
import com.youdrop.youdrop.views.sections.notifications.PrivateNotificationsFragment.OnNotificationInteractionListener;
import com.youdrop.youdrop.R;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.List;
import java.util.Locale;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Notification} and makes a call to the
 * specified {@link NotificationsFragment.OnNotificationInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class NotificationRecyclerViewAdapter extends RecyclerView.Adapter<NotificationRecyclerViewAdapter.ViewHolder> {

    private final List<Notification> mValues;
    private final OnNotificationInteractionListener mListener;

    private Context context;

    public NotificationRecyclerViewAdapter(List<Notification> items, OnNotificationInteractionListener listener, Context c) {
        mValues = items;
        mListener = listener;
        context = c;
    }

    public List<Notification> getmValues() {
        return mValues;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        Notification n = mValues.get(position);
        holder.mTitleView.setText("" + n.getTitle());
        if (n.getPublication() != null && n.getPublication().getUser().getAvatar() != null){
            Picasso.with(context).load("http://api.you-drop.com/files/" +n.getPublication().getUser().getAvatar().getId() + "/image/thumbnail" ).resize(120, 120).into(holder.mImageView);
        } else if (n.getProfile() != null && n.getProfile().getAvatar() != null){
            Picasso.with(context).load("http://api.you-drop.com/files/" + n.getProfile().getAvatar().getId() + "/image/thumbnail").resize(120, 120).into(holder.mImageView);
        } else {
            holder.mImageView.setImageResource(R.drawable.perfil);
        }

        PrettyTime p = new PrettyTime(Locale.getDefault());
        holder.mContentView.setText(p.format(n.getCreatedAt()));



        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onNotificationInteraction(holder.mItem);
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
        public final TextView mContentView;
        public Notification mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleView = (TextView) view.findViewById(R.id.title);
            mContentView = (TextView) view.findViewById(R.id.publication);
            mImageView = (ImageView)view.findViewById(R.id.imageView);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
