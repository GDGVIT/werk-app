package com.dscvit.werk.util

// Get name for anything ( Useful for logging)
fun Any.getTAG(): String {
    return this::class.java.simpleName
}