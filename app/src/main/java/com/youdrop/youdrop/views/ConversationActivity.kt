package com.youdrop.youdrop.views

import android.app.ActionBar
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.paugonzaleza.talks.views.sections.ConversationView
import com.youdrop.youdrop.R
import com.youdrop.youdrop.controllers.ConversationController
import com.youdrop.youdrop.data.Conversation
import com.youdrop.youdrop.data.Message
import com.youdrop.youdrop.views.adapters.MessagesAdapter
import com.youdrop.youdrop.views.sections.friends.ConversationsView
import kotlinx.android.synthetic.main.activity_conversation.*

class ConversationActivity : AppCompatActivity(),ConversationView {


    private var controller: ConversationController = ConversationController(this)
    var adapter: MessagesAdapter? = null
    private var mColumnCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversation)
        if (intent != null){
            val conversation = intent.getSerializableExtra(ConversationActivity.INTENT_CONVERSATION) as Conversation
            controller.conversation = conversation
        }
        if (mColumnCount <= 1) {
            val mLayoutManager = LinearLayoutManager(this)
            mLayoutManager.reverseLayout = true
            mLayoutManager.stackFromEnd = true
            list.layoutManager = mLayoutManager
        } else {
            list.layoutManager = GridLayoutManager(getContext(), mColumnCount)
        }
        adapter = MessagesAdapter(controller)
        list.adapter = adapter
        controller.init()
        adapter?.userId = controller.userId
        send.setOnClickListener { v: View? ->
            val value = message.text.toString()
            if (value.length > 0){
                controller.addMessage(value)
            }
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


    override fun clearForm() {
        message.setText("")
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm!!.hideSoftInputFromWindow(message.getWindowToken(), 0)
    }

    override fun getActivity(): AppCompatActivity? {
        return this
    }

    override fun getContext(): Context? {
        return this
    }

    override fun clearItems() {
        emptyView.visibility = View.VISIBLE
        list.visibility = View.INVISIBLE
        adapter?.mValues?.clear()
        adapter?.notifyDataSetChanged()
    }

    override fun addItem(item: Message) {
        emptyView.visibility = View.GONE
        list.visibility = View.VISIBLE
        adapter?.mValues?.add(0, item)
        val position = 0//adapter?.mValues?.size!!.minus(1)
        adapter?.notifyItemInserted(position)
        list.scrollToPosition(position)
    }

    fun updateItem(item: Message, position: Int) {
        adapter?.mValues?.set(position, item)
        adapter?.notifyItemChanged(position)
    }

    fun addOlderItems(items: List<Message>) {
        emptyView.visibility = View.GONE
        list.visibility = View.VISIBLE
        adapter?.mValues?.addAll(items)
        adapter?.notifyDataSetChanged()
        list.scrollToPosition(adapter?.mValues?.size!!.minus(1))
    }

    override fun addItems(items: List<Message>) {
        emptyView.visibility = View.GONE
        list.visibility = View.VISIBLE
        adapter?.mValues?.addAll(0,items)
        adapter?.notifyDataSetChanged()
        list.scrollToPosition(0)

    }

    override fun onSupportNavigateUp(): Boolean {
        setResult(Activity.RESULT_CANCELED)
        finish()
        return true
    }

    companion object {

        val INTENT_CONVERSATION = "conversation"
        val RESULT:Int = 3534

        fun newIntent(context: Context, conversation: Conversation): Intent {
            val intent = Intent(context, ConversationActivity::class.java)
            intent.putExtra(INTENT_CONVERSATION, conversation)
            return intent
        }

    }
}
