package com.mahmoudshaaban.mviapp.api

import androidx.lifecycle.LiveData
import com.mahmoudshaaban.mviapp.model.BlogPost
import com.mahmoudshaaban.mviapp.model.User
import com.mahmoudshaaban.util.GenericApiResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("placeholder/blogs")
    fun getBlogPost(
    ) : LiveData<GenericApiResponse<List<BlogPost>>>

    @GET("placeholder/user/{userId}")
    fun getUser(
        @Path("userId") userId : String
    ) : LiveData<GenericApiResponse<User>>
}
