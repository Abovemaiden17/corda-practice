package com.practice.controller

import com.practice.common.response.ResponseModel
import com.practice.model.RegisterUserModel
import com.practice.services.interfaces.IUserService
import javassist.NotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

private const val CONTROLLER_NAME = "api/v1/users"

@RestController
@CrossOrigin(origins = ["*"])
@RequestMapping(CONTROLLER_NAME) // The paths for HTTP requests are relative to this base path.
class UserController(private val userService: IUserService) : BaseController() {
    /**
     * Get all users
     */
    @GetMapping(value = [], produces = ["application/json"])
    private fun getAllUsers(): ResponseEntity<ResponseModel> {
        return try
        {
            val response = userService.getAll()
            ResponseEntity.ok(ResponseModel(
                    message = "Success",
                    statusCode = HttpStatus.OK.value(),
                    result = response
            ))
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(ResponseModel(
                    message = "Failed",
                    statusCode = HttpStatus.NOT_FOUND.value(),
                    result = e.localizedMessage.toString()
            ))
        }
    }

    /**
     * Get user
     */
    @GetMapping(value = ["/{userUID}"], produces = ["application/json"])
    private fun getUser(@PathVariable userUID: String?): ResponseEntity<ResponseModel> {
        return try
        {
            val response = userUID?.let { userService.get(it) }
            when {
                response != null -> ResponseEntity.ok(ResponseModel(
                        message = "Success",
                        statusCode = HttpStatus.OK.value(),
                        result = response
                ))
                else -> throw NotFoundException("User with $userUID cannot be found")
            }
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(ResponseModel(
                    message = "Failed",
                    statusCode = HttpStatus.NOT_FOUND.value(),
                    result = e.localizedMessage.toString()
            ))
        }
    }

    /**
     * Register user
     */
    @PostMapping(value = [], produces = ["application/json"])
    private fun registerUser(@RequestBody request: RegisterUserModel): ResponseEntity<ResponseModel> {
        return try
        {
            val response = userService.registerUser(request)
            ResponseEntity.ok(ResponseModel(
                    message = "Success",
                    statusCode = HttpStatus.CREATED.value(),
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

//    @GetMapping(value = ["/jwt"], produces = ["application/json"])
//    private fun jwtToken(): ResponseEntity<ResponseModel>
//    {
//        return try
//        {
//            ResponseEntity.ok(ResponseModel(
//                    message = "Success",
//                    statusCode = HttpStatus.OK.value(),
//                    result = jwt
//            ))
//        } catch (e: Exception) {
//            ResponseEntity.badRequest().body(ResponseModel(
//                    message = "Failed",
//                    statusCode = HttpStatus.BAD_REQUEST.value(),
//                    result = e.localizedMessage.toString()
//            ))
//        }
//    }

//    @GetMapping(value = ["/parser/{jwt}"], produces = ["application/json"])
//    private fun jwtParser(@PathVariable jwt: String): ResponseEntity<ResponseModel>
//    {
//        return try
//        {
//            ResponseEntity.ok(ResponseModel(
//                    message = "Success",
//                    statusCode = HttpStatus.OK.value(),
//                    result = jwtParser.decodeJWT(jwt)
//            ))
//        } catch (e: Exception) {
//            ResponseEntity.badRequest().body(ResponseModel(
//                    message = "Failed",
//                    statusCode = HttpStatus.BAD_REQUEST.value(),
//                    result = e.localizedMessage.toString()
//            ))
//        }
//    }
}