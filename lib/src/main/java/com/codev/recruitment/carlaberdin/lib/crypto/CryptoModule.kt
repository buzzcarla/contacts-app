package com.codev.recruitment.carlaberdin.lib.crypto

import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class CryptoModule(encryptionSettings: EncryptionSettings) {

    private var encryptionSettings : EncryptionSettings

    init {
        this.encryptionSettings = encryptionSettings
    }

    @Singleton
    @Provides
    fun providesCryptography(): Crypto {
        return Crypto (encryptionSettings)
    }
}