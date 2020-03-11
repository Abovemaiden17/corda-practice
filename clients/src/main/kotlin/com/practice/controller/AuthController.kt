package com.practice.controller

import com.practice.common.response.ResponseModel
import com.practice.model.ChangePasswordUserModel
import com.practice.model.LoginUserModel
import com.practice.services.interfaces.IAuthService
import javassist.NotFoundException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

private const val CONTROLLER_NAME = "api/v1/auth"

@RestController
@CrossOrigin(origins = ["*"])
@RequestMapping(CONTROLLER_NAME) // The paths for HTTP requests are relative to this base path.
class AuthController (private val authService: IAuthService): BaseController() {
    /**
     * Login User
     */
    @PostMapping(value = [], produces = ["application/json"])
    private fun loginUser(@RequestBody request: LoginUserModel): ResponseEntity<ResponseModel> {
        return try
        {
            val response = authService.loginUser(request)
            ResponseEntity.ok(ResponseModel(
                    message = "Success",
                    statusCode = HttpStatus.OK.value(),
                    result = response
            ))
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(ResponseModel(
                    message = "Failed",
                    statusCode = HttpStatus.BAD_REQUEST.value(),
                    result = e.localizedMessage.toString()
            ))
        }
    }

    /**
     * Change User Password
     */
    @PostMapping(value = ["/password"], produces = ["application/json"])
    private fun changePassword(@RequestHeader headers: HttpHeaders, @RequestBody request: ChangePasswordUserModel): ResponseEntity<ResponseModel> {
        return try
        {
            val response = getUserUID(headers)?.let { authService.changePassword(it, request) }
            when {
                response != null -> ResponseEntity.ok(ResponseModel(
                        message = "Success",
                        statusCode = HttpStatus.OK.value(),
                        result = response
                ))
                else -> throw NotFoundException("User uid not found")
            }
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(ResponseModel(
                    message = "Failed",
                    statusCode = HttpStatus.BAD_REQUEST.value(),
                    result = e.localizedMessage.toString()
            ))
        }
    }
}