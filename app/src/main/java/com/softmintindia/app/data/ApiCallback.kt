package com.softmintindia.app.data

// Changed fun interface to a regular interface or abstract class to allow default behavior
fun interface ApiCallback<T> {
    fun handleResponse(success: Boolean, data: T?, message: String?)

    // Default implementations for onSuccess and onError
    fun onSuccess(data: T) {
        handleResponse(true, data, null)
    }

    fun onError(message: String) {
        handleResponse(false, null, message)
    }
}
