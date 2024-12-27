package com.softmintindia.app.network

fun interface ApiCallback<T> {
    fun handleResponse(success: Boolean, data: T?, message: String?)

    fun onSuccess(data: T) = handleResponse(true, data, null)
    fun onError(message: String) = handleResponse(false, null, message)
}
