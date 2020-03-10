package com.practice.common.appexceptions

import com.google.gson.Gson
import java.util.HashMap

class NotFoundException(message: String) : Exception(message)
class ValidationException(message: String): Exception(message)

class ValidationTracker {
    private val _validations= HashMap<String, String>()

    fun validate()
    {
        if(_validations.size > 0){
            val jsonMsg = Gson().toJson(_validations)
            throw ValidationException(jsonMsg)
        }
    }

    fun add(propertyName: String, validationMessage: String)
    {
        _validations[propertyName] = validationMessage
    }
}