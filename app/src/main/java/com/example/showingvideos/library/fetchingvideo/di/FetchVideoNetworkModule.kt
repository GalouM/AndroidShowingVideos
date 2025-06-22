package com.example.showingvideos.library.fetchingvideo.di

import com.example.showingvideos.library.fetchingvideo.remote.PexelAuthInterceptor
import com.example.showingvideos.library.fetchingvideo.remote.VideoListRemoteSource
import com.facebook.flipper.plugins.network.FlipperOkhttpInterceptor
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class FetchVideoNetworkModule {

    @Provides
    @Singleton
    fun provideNetworkFlipperPlugin(): NetworkFlipperPlugin {
        return NetworkFlipperPlugin()
    }

    @Provides
    @Singleton
    fun providePexelAuthInterceptor(): PexelAuthInterceptor {
        return PexelAuthInterceptor()
    }

    @Provides
    @Singleton
    @PexelOkHttp
    fun provideOkHttpClient(
        authInterceptor: PexelAuthInterceptor,
        networkFlipperPlugin: NetworkFlipperPlugin
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addNetworkInterceptor(FlipperOkhttpInterceptor(networkFlipperPlugin))
            .build()
    }

    @Provides
    @VideoApi
    fun provideRetrofit(@PexelOkHttp pexelOkHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.pexels.com/")
            .client(pexelOkHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    @Provides
    fun provideFetchVideoRemoteSource(@VideoApi retrofit: Retrofit): VideoListRemoteSource {
        return retrofit.create(VideoListRemoteSource::class.java)
    }

    @Provides
    @IoDispatcher
    fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO
}

annotation class IoDispatcher

annotation class VideoApi

annotation class PexelOkHttp