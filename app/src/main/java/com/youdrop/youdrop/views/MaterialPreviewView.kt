package com.paugonzaleza.talks.views.sections

import android.content.Context
import android.support.v7.app.AppCompatActivity

/**
 * Created by pau on 15/11/17.
 */
interface MaterialPreviewView {
    fun getContext(): Context?
    fun getActivity(): AppCompatActivity?

    fun showLoading(show: Boolean)
    fun setPhotoPreview(path: String)
    fun setVideoPreview(path: String)
    fun setFormatedTextPreview(text: String)
}
