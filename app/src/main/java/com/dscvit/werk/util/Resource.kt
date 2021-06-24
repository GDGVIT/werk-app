package com.dscvit.werk.util

sealed class Resource<T>(val data: T?, val message: String?, val statusCode: Int?) {
    class Success<T>(data: T, statusCode: Int) : Resource<T>(data, null, statusCode)
    class Error<T>(message: String, statusCode: Int) : Resource<T>(null, message, statusCode)
}