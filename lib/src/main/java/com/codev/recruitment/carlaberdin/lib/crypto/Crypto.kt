package com.codev.recruitment.carlaberdin.lib.crypto

import android.os.Build
import androidx.annotation.RequiresApi
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class Crypto(val key: String, val IV: String) {

    private var cipherEncrypt: Cipher
    private var cipherDecrypt: Cipher


    init {
        cipherEncrypt = initCipher(Cipher.ENCRYPT_MODE)
        cipherDecrypt = initCipher(Cipher.DECRYPT_MODE)
    }

    fun initCipher(opMode: Int): Cipher {
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val keySpec = SecretKeySpec(key.toByteArray(Charsets.UTF_8), "AES")
        val ivSpec = IvParameterSpec(IV.toByteArray(Charsets.UTF_8))
        cipher.init(opMode, keySpec, ivSpec)
        return cipher
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    fun encryptAES265(input: ByteArray?): String {
        try {
            val cipherText = cipherEncrypt.doFinal(input)
            return Base64.getEncoder().encodeToString(cipherText)
        } catch (e: Exception) {
        }
        return ""
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    fun decryptAES265(input: String?): String {
        try {
            val toDecode = Base64.getDecoder().decode(input)
            val decryptedText = cipherDecrypt.doFinal(toDecode)
            return String(decryptedText)
        } catch (e: Exception) {
        }
        return ""
    }
}