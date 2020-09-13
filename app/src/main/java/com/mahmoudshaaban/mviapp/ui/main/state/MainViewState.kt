package com.mahmoudshaaban.mviapp.ui.main.state

import com.mahmoudshaaban.mviapp.model.BlogPost
import com.mahmoudshaaban.mviapp.model.User

data class MainViewState(
    // in MVVM we put this in mutable live data but in mvi we have a class to package all views in one class
    var blogPost : List<BlogPost>? = null,
    var user : User? = null)
{

}