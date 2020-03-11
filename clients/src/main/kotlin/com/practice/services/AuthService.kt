package com.practice.services

import com.practice.common.ConvertFunctions
import com.practice.common.UserConstants.Companion.SECRET_KEY
import com.practice.common.appexceptions.NotFoundException
import com.practice.common.appexceptions.UnauthorizedException
import com.practice.common.appexceptions.ValidationTracker
import com.practice.flows.user.ChangePasswordFlow
import com.practice.flows.user.LoginFlow
import com.practice.model.ChangePasswordUserModel
import com.practice.model.LoginUserModel
import com.practice.services.interfaces.IAuthService
import com.practice.states.UserState
import com.practice.utils.jwt.JwtParser
import com.practice.utils.jwt.JwtToken
import com.practice.utils.password.PasswordEncryption
import com.practice.utils.password.PasswordManager
import com.practice.webserver.NodeRPCConnection
import org.springframework.stereotype.Service

@Service
class AuthService (private val rpc: NodeRPCConnection, private val func: ConvertFunctions,
                   private val jwtParser: JwtParser, private val jwtToken: JwtToken): IAuthService {
    override fun loginUser(request: LoginUserModel): Boolean {
        // Validate client request
        val tracker = ValidationTracker()

        if(request.username.isBlank() || request.username.isEmpty())
            tracker.add("username", "Username cannot be empty")

        if(request.password.isBlank() || request.password.isEmpty())
            tracker.add("password", "Password cannot be empty")

        tracker.validate()

        val token = jwtToken.generateToken()

        // Find user using username
        val userStates = rpc.proxy.vaultQuery(UserState::class.java).states
        val foundUser = userStates.find {
            it.state.data.name == request.username
        } ?: throw NotFoundException("User with ${request.username} cannot be found")

        // Compare password hash
        val userPass = PasswordEncryption(foundUser.state.data.passwordSalt, SECRET_KEY).encryption.encrypt(request.password)
        if(foundUser.state.data.passwordHash != userPass)
            throw UnauthorizedException("Email or password is incorrect")

        val userUID = foundUser.state.data.linearId.toString()

        // Login user
        rpc.proxy.startFlowDynamic(
                LoginFlow::class.java,
                userUID,
                token
        )

        return true
    }


    override fun changePassword(userUID: String, request: ChangePasswordUserModel): Boolean {
        // Find user using userUID
        val id = func.stringToUniqueIdentifier(userUID)
        val userStates = rpc.proxy.vaultQuery(UserState::class.java).states
        val foundUser = userStates.find {
            it.state.data.linearId == id
        } ?: throw NotFoundException("User with $userUID cannot be found")

        try {
            jwtParser.decodeJWT(foundUser.state.data.jwtToken)
        } catch (e: Exception) {
            throw e
        }

        // Validate client request
        val tracker = ValidationTracker()

        if(request.oldPassword.isBlank() || request.oldPassword.isEmpty())
            tracker.add("name", "Old password cannot be empty")

        if(request.newPassword.isBlank() || request.newPassword.isEmpty())
            tracker.add("password", "New password cannot be empty")

        tracker.validate()

        val oldSalt = foundUser.state.data.passwordSalt
        val oldEncryptedPassword = PasswordEncryption(oldSalt, SECRET_KEY).encryption.encrypt(request.oldPassword)
        val encryptedPassword = PasswordEncryption(oldSalt, SECRET_KEY).encryption.encrypt(request.newPassword)

        // Compare hashes
        if (oldEncryptedPassword != foundUser.state.data.passwordHash)
            throw Exception("Specified current password is incorrect")

        if (encryptedPassword == foundUser.state.data.passwordHash)
            throw Exception("New password must be different from the current")

        // Generate salt and encrypt new password using new salt
        val newSalt = PasswordManager().generatePassword(true, isWithUppercase = true, isWithNumbers = true, isWithSpecial = false, length = 32)
        val newEncryptedPassword = PasswordEncryption(newSalt, SECRET_KEY).encryption.encrypt(request.newPassword)

        rpc.proxy.startFlowDynamic(
                ChangePasswordFlow::class.java,
                userUID,
                newEncryptedPassword,
                newSalt
        )

        return true
    }
}