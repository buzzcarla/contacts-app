package com.codev.recruitment.carlaberdin.lib.crypto

data class EncryptionSettings(
    var isEncryptionOn: Boolean = false,
    var key : String,
    var IV : String
)
