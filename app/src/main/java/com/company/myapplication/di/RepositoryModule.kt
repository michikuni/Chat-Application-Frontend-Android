package com.company.myapplication.di

import com.company.myapplication.data.repository.AuthRepositoryImpl
import com.company.myapplication.data.repository.ConversationRepositoryImpl
import com.company.myapplication.data.repository.FcmRepositoryImpl
import com.company.myapplication.data.repository.FeedRepositoryImpl
import com.company.myapplication.data.repository.FriendRepositoryImpl
import com.company.myapplication.data.repository.UserRepositoryImpl
import com.company.myapplication.domain.repository.AuthRepository
import com.company.myapplication.domain.repository.ConversationRepository
import com.company.myapplication.domain.repository.FcmRepository
import com.company.myapplication.domain.repository.FeedRepository
import com.company.myapplication.domain.repository.FriendRepository
import com.company.myapplication.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds @Singleton
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

    @Binds @Singleton
    abstract fun bindFriendRepository(impl: FriendRepositoryImpl): FriendRepository

    @Binds @Singleton
    abstract fun bindConversationRepository(impl: ConversationRepositoryImpl): ConversationRepository

    @Binds @Singleton
    abstract fun bindFeedRepository(impl: FeedRepositoryImpl): FeedRepository

    @Binds @Singleton
    abstract fun bindFcmRepository(impl: FcmRepositoryImpl): FcmRepository
}
