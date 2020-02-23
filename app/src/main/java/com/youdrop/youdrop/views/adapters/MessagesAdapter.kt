package com.youdrop.youdrop.views.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.youdrop.youdrop.R
import com.youdrop.youdrop.api.ContentManager
import com.youdrop.youdrop.api.Utils
import com.youdrop.youdrop.data.Message
import com.youdrop.youdrop.views.adapters.MessagesAdapter.OnListFragmentInteractionListener
import org.ocpsoft.prettytime.PrettyTime

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class MessagesAdapter(private val mListener:OnListFragmentInteractionListener?):RecyclerView.Adapter<MessagesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent:ViewGroup, viewType:Int): ViewHolder {
        load(parent.context)

        if (viewType == 1) {
            val view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_out, parent, false)
            return ViewHolder(view)
        }else {
            val view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_incoming, parent, false)
            return ViewHolder(view)
        }

    }

    val mValues:MutableList<Message> = mutableListOf()
    var userId:String? = null


    fun load(context:Context){
        if(userId == null) userId = Utils.getUserId(context)

    }

    override fun onBindViewHolder(holder: ViewHolder, position:Int) {
        val item: Message = mValues.get(position)
        holder.mItem = item


        val p = PrettyTime(holder.mView.resources.configuration.locale)
        holder.mTime.text =(p.format(item.updatedAt))

        holder.mContentView.text = item.body
        holder.mName.text = item.user?.name



        holder.mView.setOnClickListener {
            if (null != mListener) {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                mListener!!.onMessageSelected(holder.mItem as Message)
            }
        }

    }
    override fun getItemCount():Int {
        return mValues.size
    }

    override fun getItemViewType(position: Int): Int {
        val item = mValues.get(position)
        if( item.user?.id == userId){
            return 1
        }
        if( item.user?.id != userId){
            return 2
        }
        return 0
    }

    interface OnListFragmentInteractionListener {
        fun onMessageSelected(item: Message)
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {

        var mItem: Message? = null

        val mContentView:TextView
        val mName:TextView
        val mTime:TextView

        init{
            mContentView = mView.findViewById<TextView>(R.id.text)
            mName = mView.findViewById<TextView>(R.id.name)
            mTime = mView.findViewById<TextView>(R.id.time)
        }

        override fun toString():String {
            return super.toString() + " '" + mContentView.getText() + "'"
        }
    }
}
