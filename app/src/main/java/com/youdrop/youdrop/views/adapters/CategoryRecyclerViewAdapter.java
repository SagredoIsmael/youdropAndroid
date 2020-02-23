package com.youdrop.youdrop.views.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.youdrop.youdrop.data.Category;
import com.youdrop.youdrop.data.Conversation;
import com.youdrop.youdrop.data.Notification;
import com.youdrop.youdrop.views.sections.NotificationsFragment;
import com.youdrop.youdrop.views.sections.friends.ConversationsFragment;
import com.youdrop.youdrop.R;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Notification} and makes a call to the
 * specified {@link NotificationsFragment.OnNotificationInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class CategoryRecyclerViewAdapter extends RecyclerView.Adapter<CategoryRecyclerViewAdapter.ViewHolder> {

    private final List<Category> mValues;
    private final OnCategoryInteractionListener mListener;

    private Context context;

    public CategoryRecyclerViewAdapter(List<Category> items, OnCategoryInteractionListener listener, Context c) {
        mValues = items;
        mListener = listener;
        context = c;
    }

    public List<Category> getmValues() {
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
        Category n = mValues.get(position);
        holder.mTitleView.setText("" + n.getName());
        holder.mContentView.setText("");
        //Picasso.with(context).load(n.getMainImageUrl()).resize(80, 80).into(holder.mImageView);
        Resources resources = context.getResources();
        final int resourceId = resources.getIdentifier(n.getIcon(), "drawable",
                context.getPackageName());
        holder.mImageView.setImageResource(resourceId);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onCategoryInteraction(holder.mItem);
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
        public Category mItem;

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

    public interface OnCategoryInteractionListener {
        // TODO: Update argument type and name
        void onCategoryInteraction(Category item);
    }
}
