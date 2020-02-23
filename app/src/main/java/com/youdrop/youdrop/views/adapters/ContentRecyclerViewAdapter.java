package com.youdrop.youdrop.views.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.youdrop.youdrop.api.Utils;
import com.youdrop.youdrop.data.Publication;
import com.youdrop.youdrop.data.Notification;
import com.youdrop.youdrop.views.sections.NotificationsFragment;
import com.youdrop.youdrop.R;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Notification} and makes a call to the
 * specified {@link NotificationsFragment.OnNotificationInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ContentRecyclerViewAdapter extends RecyclerView.Adapter<ContentRecyclerViewAdapter.ViewHolder> {

    private final List<Publication> mValues;
    private final OnContentInteractionListener mListener;
    private Context context;

    public ContentRecyclerViewAdapter(List<Publication> items, OnContentInteractionListener listener, Context c) {
        mValues = items;
        mListener = listener;
        context = c;

    }

    public List<Publication> getmValues() {
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
        Publication n = mValues.get(position);
        if (!n.isAnonymous()){
            if (n.getUser() != null){
                holder.mTitleView.setText( n.getUser().getName());
            }
        } else {
            holder.mTitleView.setText("Anónimo");
        }
        holder.mContentView.setText(Utils.calculateDistance(n.getLocation().getLatitude(), n.getLocation().getLongitude()));
        if (n.getUser() != null && n.getUser().getAvatar() != null && !n.isAnonymous()){
            Picasso.with(context).load("http://api.you-drop.com/files/" +n.getUser().getAvatar().getId()  + "/image/thumbnail").resize(120, 80).into(holder.mImageView);

        }else if (n.isAnonymous()){
            holder.mImageView.setImageResource(R.drawable.perfil);
        }else {
            holder.mImageView.setImageResource(R.drawable.perfil);
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onContentInteraction(holder.mItem);
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
        public Publication mItem;

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

    public interface OnContentInteractionListener {
        // TODO: Update argument type and name
        void onContentInteraction(Publication item);
    }
}
