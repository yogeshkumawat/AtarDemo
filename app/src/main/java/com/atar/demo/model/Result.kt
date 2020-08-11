package com.atar.demo.model

data class Result<T> @JvmOverloads constructor(
    val success: Boolean,
    val data: T?,
    val exception: Exception?,
    val time: Long,
    val isFromCache: Boolean?
) {
    constructor(success: Boolean, data: T?, exception: Exception?)
            : this(success, data, exception, 0, false) {

    }

    constructor(success: Boolean, data: T?, exception: Exception?, time: Long)
            : this(success, data, exception, time, false) {

    }
}