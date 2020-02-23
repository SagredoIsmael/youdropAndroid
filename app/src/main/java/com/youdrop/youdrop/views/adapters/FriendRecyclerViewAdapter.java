package com.youdrop.youdrop.views.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.youdrop.youdrop.R;
import com.youdrop.youdrop.data.Notification;
import com.youdrop.youdrop.data.User;
import com.youdrop.youdrop.views.sections.NotificationsFragment;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Notification} and makes a call to the
 * specified {@link NotificationsFragment.OnNotificationInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class FriendRecyclerViewAdapter extends RecyclerView.Adapter<FriendRecyclerViewAdapter.ViewHolder> {

    private final List<User> mValues;
    private final OnUserInteractionListener mListener;

    private Context context;

    public FriendRecyclerViewAdapter(List<User> items, OnUserInteractionListener listener, Context c) {
        mValues = items;
        mListener = listener;
        context = c;
    }

    public List<User> getmValues() {
        return mValues;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friend_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        User n = mValues.get(position);
        holder.mTitleView.setText("" + n.getName());
        holder.mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onAddConversationInteraction(holder.mItem);
                }
            }
        });
        if (n.getAvatar() != null){
            Picasso.with(context).load("http://api.you-drop.com/files/" +n.getAvatar().getId() + "/image/thumbnail" ).resize(120, 120).into(holder.mImageView);
        } else {
            holder.mImageView.setImageResource(R.drawable.perfil);
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onUserInteraction(holder.mItem);
                }
            }
        });
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnUserInteractionListener {
        // TODO: Update argument type and name
        void onUserInteraction(User item);
        void onAddConversationInteraction(User item);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitleView;
        public final ImageView mImageView;
        public final ImageButton mContentView;
        public User mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleView = (TextView) view.findViewById(R.id.title);
            mContentView = (ImageButton) view.findViewById(R.id.publication);
            mImageView = (ImageView)view.findViewById(R.id.imageView);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitleView.getText() + "'";
        }
    }
}
