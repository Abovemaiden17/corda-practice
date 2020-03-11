package com.practice.utils.jwt

import com.practice.common.UserConstants.Companion.JWT_KEY
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.spec.SecretKeySpec
import javax.xml.bind.DatatypeConverter

@Component
open class JwtToken {
    fun generateToken(): String {
        //The JWT signature algorithm we will be using to sign the token
        val signatureAlgorithm = SignatureAlgorithm.HS256

        val nowMillis = System.currentTimeMillis()
        val now = Date(nowMillis)

        //We will sign our JWT with our ApiKey secret
        val apiKeySecretBytes = DatatypeConverter.parseBase64Binary(JWT_KEY)
        val signingKey = SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.jcaName)
        val ttlMillis = 10000

        //JWT Claims
        val builder = Jwts.builder().setId("1")
                .setIssuedAt(now)
                .setSubject("test subject")
                .setIssuer("test issuer")
                .signWith(signatureAlgorithm, signingKey)
        //Expiration
        val expMillis = nowMillis + ttlMillis
        val exp = Date(expMillis)
        builder.setExpiration(exp)

        return builder.compact()
    }

}