package com.practice.utils.jwt

import com.practice.common.UserConstants.Companion.JWT_KEY
import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Component
import java.util.*
import javax.xml.bind.DatatypeConverter

@Component
open class JwtParser {
    fun decodeJWT(jwt: String?): Any {
        val nowMillis = System.currentTimeMillis()
        val now = Date(nowMillis)
        val parse = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(JWT_KEY))
                .parseClaimsJws(jwt).body

        if(now.after(parse.expiration)) throw IllegalArgumentException("Token already expired") else return parse
    }
}