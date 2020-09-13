package com.mahmoudshaaban.mviapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.mahmoudshaaban.mviapp.model.BlogPost
import com.mahmoudshaaban.mviapp.model.User
import com.mahmoudshaaban.mviapp.repositroy.Repository
import com.mahmoudshaaban.mviapp.ui.main.state.MainStateEvent
import com.mahmoudshaaban.mviapp.ui.main.state.MainStateEvent.*
import com.mahmoudshaaban.mviapp.ui.main.state.MainViewState
import com.mahmoudshaaban.util.AbsentLiveData
import com.mahmoudshaaban.util.DataState

class MainViewModel : ViewModel(){

    private val _stateEvent: MutableLiveData<MainStateEvent> = MutableLiveData()
    private val _viewState: MutableLiveData<MainViewState> = MutableLiveData()

    val viewState: LiveData<MainViewState>
        get() = _viewState



    // what this method does it is listening to _stateevent MutableliveDataObject if it changes switchmap will detect this changes
    // and executes the code run in it
    // Imp : why we return <MainViewState> which we detect _stateevent that confused me
    // b/c we are wrapping our mainViewstate into Datastate class for handling loading and error
    // i will discuss it if we fire off event like getuser once that data retrun from repository it will be returned throw datastate
    // and capture it in a switch map and return here
    // when we observe datastate in viewmodel whether it in error or loading doesn't matter then we set it to viewstate
    // it is clear now ?
    val dataState: LiveData<DataState<MainViewState>> = Transformations
        .switchMap(_stateEvent){stateEvent ->
            stateEvent?.let {
                // when different state is handled we return livedata
                handleStateEvent(stateEvent)
            }
        }

    fun handleStateEvent(stateEvent: MainStateEvent): LiveData<DataState<MainViewState>>{
        when(stateEvent){

            is GetBlogPostEvent -> {
                return Repository.getBlogPosts()
            }

            is GetUserEvent -> {
                return Repository.getUser(stateEvent.userId)
            }

            is None ->{
                return AbsentLiveData.create()
            }
        }
    }

    fun setBLogListData(blogPost: List<BlogPost>){
        val update = getCurrentViewStateOrNew()
        update.blogPost = blogPost
        _viewState.value = update
    }
    fun setUser(user : User){
        val update = getCurrentViewStateOrNew()
        update.user = user
        _viewState.value = update
    }

    // here we want to get the current view state to be updated or new
     fun getCurrentViewStateOrNew() : MainViewState{
        val value = viewState.value?.let {
            it

        }?: MainViewState()
        return value
    }

    // we create this method because we observing _stateevent so we need way to trigger this whole process ( switchmap )
    fun setStateEvent(event: MainStateEvent){
        _stateEvent.value = event
    }
}
