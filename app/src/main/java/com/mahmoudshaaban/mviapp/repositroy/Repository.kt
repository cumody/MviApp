package com.mahmoudshaaban.mviapp.repositroy

import androidx.lifecycle.LiveData
import com.mahmoudshaaban.mviapp.api.MyRetrofitBuilder
import com.mahmoudshaaban.mviapp.model.BlogPost
import com.mahmoudshaaban.mviapp.model.User

import com.mahmoudshaaban.mviapp.ui.main.state.MainViewState
import com.mahmoudshaaban.util.ApiSuccessResponse

import com.mahmoudshaaban.util.DataState
import com.mahmoudshaaban.util.GenericApiResponse

object Repository {

    fun getBlogPosts(): LiveData<DataState<MainViewState>> {
        return object : NetworkBoundResource<List<BlogPost>,MainViewState>(){
            override fun handleApisuccessResponse(response: ApiSuccessResponse<List<BlogPost>>) {
                result.value = DataState.data(null,data = MainViewState(blogPost = response.body,user = null))
            }

            override fun createCall(): LiveData<GenericApiResponse<List<BlogPost>>> {
                return MyRetrofitBuilder.apiService.getBlogPost()
            }

        }.asLiveData()
    }

    fun getUser(userId : String): LiveData<DataState<MainViewState>> {
        return object : NetworkBoundResource<User,MainViewState>(){
            override fun handleApisuccessResponse(response: ApiSuccessResponse<User>) {
                result.value = DataState.data(
                    null,
                    MainViewState(
                        blogPost = null,
                        user = response.body
                    )
                )
            }

            override fun createCall(): LiveData<GenericApiResponse<User>> {
                return MyRetrofitBuilder.apiService.getUser(userId)
            }

        }.asLiveData()

    }
}