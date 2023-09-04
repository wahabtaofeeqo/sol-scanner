package com.wrisband.pda.data.models

data class APIResponse<T>(
    val status: Boolean,
    val message: String,
    val data: T? = null
)