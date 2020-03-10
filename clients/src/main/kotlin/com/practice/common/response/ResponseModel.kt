package com.practice.common.response

data class ResponseModel(
        val message: String,
        val statusCode: Int,
        val result: Any
)