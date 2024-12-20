package com.softmintindia.pgsdk.repository

import com.softmintindia.pgsdk.network.ApiClient
import com.softmintindia.pgsdk.network.ApiService


class UserRepository {
    private val apiService = ApiClient.retrofit.create(ApiService::class.java)

}
