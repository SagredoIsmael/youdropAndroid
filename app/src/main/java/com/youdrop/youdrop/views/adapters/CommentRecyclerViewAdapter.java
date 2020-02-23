package com.youdrop.youdrop.views.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.youdrop.youdrop.data.Category;
import com.youdrop.youdrop.data.Comment;
import com.youdrop.youdrop.data.Notification;
import com.youdrop.youdrop.views.sections.NotificationsFragment;
import com.youdrop.youdrop.R;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.List;
import java.util.Locale;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Notification} and makes a call to the
 * specified {@link NotificationsFragment.OnNotificationInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class CommentRecyclerViewAdapter extends RecyclerView.Adapter<CommentRecyclerViewAdapter.ViewHolder> {

    private final List<Comment> mValues;
    private final OnCommentInteractionListener mListener;

    private Context context;

    public CommentRecyclerViewAdapter(List<Comment> items, OnCommentInteractionListener listener, Context c) {
        mValues = items;
        mListener = listener;
        context = c;
    }

    public List<Comment> getmValues() {
        return mValues;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_regular_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        Comment n = mValues.get(position);
        holder.mTitleView.setText(n.getBody());
        PrettyTime p = new PrettyTime(Locale.getDefault());
        holder.mContentView.setText(p.format(n.getCreatedAt()));

        if (n.getUser()!= null && n.getUser().getAvatar()!= null){
            Picasso.with(context).load("http://api.you-drop.com/files/" +n.getUser().getAvatar().getId()  + "/image/thumbnail").resize(120, 120).into(holder.mImageView);

        }

        // Picasso.with(context).load(n.getIcon()).resize(120, 80).into(holder.mImageView);


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onCommentInteraction(holder.mItem);
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
        public Comment mItem;

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

    public interface OnCommentInteractionListener {
        // TODO: Update argument type and name
        void onCommentInteraction(Comment item);
    }
}
