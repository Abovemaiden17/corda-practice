package com.practice.utils.password

import se.simbio.encryption.Encryption

class PasswordEncryption(private val salt: String, private val key: String)
{
    private val iv = ByteArray(16)
    //    val encryption = Encryption.getDefault(key, salt, iv)
    val encryption = Encryption.Builder.getDefaultBuilder(key, salt, iv).setAlgorithm("AES").setCharsetName("UTF8").build()

    fun String.encrypt():String
    {
        return encryption.encryptOrNull(this)
    }
    fun String.decrypt():String
    {
        return encryption.decryptOrNull(this)
    }
}