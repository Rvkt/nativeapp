package com.softmintindia.pgsdk.data.api

import com.softmintindia.pgsdk.domain.UserModel
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    // Use suspend function for easier coroutine integration
    @GET("photos")
    fun getUsers(): Call<List<UserModel>>
}