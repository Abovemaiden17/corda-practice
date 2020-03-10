package com.practice.controller

import com.practice.common.appexceptions.NotFoundException
import com.practice.common.appexceptions.ValidationException
import com.practice.common.response.ResponseModel
import net.corda.core.contracts.TransactionVerificationException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.io.PrintWriter
import java.io.StringWriter
import java.time.Instant
import java.util.*

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