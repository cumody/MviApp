package com.mahmoudshaaban.mviapp.ui.main.state

sealed class MainStateEvent {

    // THIS CLASS FOR DETECTING WHAT HAPPENED IF THE USER CLICK ON GETUSER OR GETBLOGS AND HANDLES ALL OF THIS DATA IN VIEWMODEL
    // lock name class here are important we can have a multiple fragments so we can name the class related to events
    // this class is like resource class in local data base we chick if success or error or loading and handle it in viewmodel class
    class GetBlogPostEvent : MainStateEvent()

    class GetUserEvent(
        val userId : String
    ) : MainStateEvent()

    // we love to do none here because if we want to reset the data or something similar
    class None : MainStateEvent()
}