package com.paugonzaleza.talks.views.sections

import android.content.Context
import android.support.v7.app.AppCompatActivity
import com.youdrop.youdrop.data.Message

/**
 * Created by pau on 15/11/17.
 */
interface ConversationView {
    fun getContext(): Context?
    fun getActivity(): AppCompatActivity?

    fun clearItems()
    fun addItems(items: List<Message>)
    fun addItem(item:Message)
    fun clearForm()
}
