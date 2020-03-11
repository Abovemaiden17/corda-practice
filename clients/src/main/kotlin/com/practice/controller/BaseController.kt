package com.practice.controller

import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders

abstract class BaseController
{
    companion object
    {
        private val logger = LoggerFactory.getLogger(BaseController::class.java)
    }

    fun getUserUID(headers: HttpHeaders): String? {
        return headers.getFirst("user-uid")
    }
}