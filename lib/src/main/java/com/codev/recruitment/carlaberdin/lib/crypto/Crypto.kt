package com.codev.recruitment.carlaberdin.lib.crypto

import android.os.Build
import androidx.annotation.RequiresApi
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class Crypto(val encryptionSettings: EncryptionSettings) {

    private lateinit var cipherEncrypt: Cipher
    private lateinit var cipherDecrypt: Cipher


    init {
        if (encryptionSettings.isEncryptionOn) {
            cipherEncrypt = initCipher(Cipher.ENCRYPT_MODE)
            cipherDecrypt = initCipher(Cipher.DECRYPT_MODE)
        }
    }

    fun initCipher(opMode: Int): Cipher {
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val keySpec = SecretKeySpec(encryptionSettings.key.toByteArray(Charsets.UTF_8), "AES")
        val ivSpec = IvParameterSpec(encryptionSettings.IV.toByteArray(Charsets.UTF_8))
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