package com.codev.recruitment.carlaberdin.lib.crypto

import com.codev.recruitment.carlaberdin.repository.ContactDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class CryptoModule(key: String, IV: String) {

    private var key : String
    private var IV : String

    init {
        this.key = key
        this.IV = IV
    }


    @Singleton
    @Provides
    fun providesCryptography(): Crypto {
        return Crypto (key, IV)
    }
}