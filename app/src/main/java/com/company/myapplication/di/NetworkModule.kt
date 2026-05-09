package com.company.myapplication.di

import com.company.myapplication.core.network.AuthInterceptor
import com.company.myapplication.data.api.AuthApi
import com.company.myapplication.data.api.ConversationApi
import com.company.myapplication.data.api.FcmApi
import com.company.myapplication.data.api.FeedApi
import com.company.myapplication.data.api.FriendApi
import com.company.myapplication.data.api.UserApi
import com.company.myapplication.repository.apiconfig.ApiConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(45, TimeUnit.SECONDS)
            .writeTimeout(45, TimeUnit.SECONDS)
            .addInterceptor(authInterceptor)
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(ApiConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

    @Provides @Singleton fun provideAuthApi(retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)
    @Provides @Singleton fun provideUserApi(retrofit: Retrofit): UserApi = retrofit.create(UserApi::class.java)
    @Provides @Singleton fun provideFriendApi(retrofit: Retrofit): FriendApi = retrofit.create(FriendApi::class.java)
    @Provides @Singleton fun provideConversationApi(retrofit: Retrofit): ConversationApi = retrofit.create(ConversationApi::class.java)
    @Provides @Singleton fun provideFeedApi(retrofit: Retrofit): FeedApi = retrofit.create(FeedApi::class.java)
    @Provides @Singleton fun provideFcmApi(retrofit: Retrofit): FcmApi = retrofit.create(FcmApi::class.java)
}
