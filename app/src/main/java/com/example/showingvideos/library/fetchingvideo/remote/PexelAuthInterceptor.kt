package com.example.showingvideos.library.fetchingvideo.remote

import com.example.showingvideos.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class PexelAuthInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestWithHeader = chain.request().newBuilder()
            .header("Authorization", BuildConfig.PEXELS_API_KEY)
            .build()

        return chain.proceed(requestWithHeader)
    }
}