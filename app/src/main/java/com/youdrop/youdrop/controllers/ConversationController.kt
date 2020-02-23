package com.youdrop.youdrop.controllers

import com.paugonzaleza.learning.api.callbacks.MessageCallback
import com.paugonzaleza.talks.api.callbacks.MessagesCallback
import com.paugonzaleza.talks.views.sections.ConversationView
import com.youdrop.youdrop.api.Api
import com.youdrop.youdrop.api.ContentManager
import com.youdrop.youdrop.api.Utils
import com.youdrop.youdrop.data.Conversation
import com.youdrop.youdrop.data.Message
import com.youdrop.youdrop.data.results.MessagesResult
import com.youdrop.youdrop.views.adapters.MessagesAdapter

/**
 * Created by pau on 06/01/18.
 */
class ConversationController :MessagesAdapter.OnListFragmentInteractionListener{

    private var view: ConversationView

    private val api:Api
    private var authToken:String? = null
    var userId:String? = null
    var conversation:Conversation? = null
    private var items:MessagesResult? = null


    constructor(view: ConversationView) {
        this.view = view
        api = ContentManager(view.getContext())
    }

    fun getAuth(){
        authToken = Utils.getAuthToken(view.getContext())
        userId = Utils.getUserId(view.getContext())
    }

    fun init(){
        getAuth()
        fillMessages()

    }

    fun fillMessages(){
        if (items == null){
            api.getMessages(authToken!!, conversation?.id!!, object: MessagesCallback(){
                override fun onSuccess(result: MessagesResult) {
                    items = result
                    if (result.items.isEmpty()){
                        view.clearItems()
                    } else {
                        view.addItems(result.items)
                    }

                }

                override fun onError(error: String) {
                    view.clearItems()
                }
            })
        } else {
            if (items!!.items.isEmpty()){
                view.clearItems()
            } else {
                view.addItems(items!!.items)
            }
        }
    }

    fun addMessage(value:String?){
        val message = Message()
        message.body = value
       // message.userId= userId
        api.addMessage(authToken!!, conversation?.id!!, message, object: MessageCallback(){

            override fun onSuccess(result: Message) {
                items!!.items.add(result)
                view.addItem(result);
                view.clearForm()
            }

            override fun onError(error: String) {

            }
        })
    }

    override fun onMessageSelected(item: Message) {

    }
}
