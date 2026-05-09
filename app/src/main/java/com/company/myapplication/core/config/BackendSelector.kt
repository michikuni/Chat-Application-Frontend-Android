package com.company.myapplication.core.config

import javax.inject.Inject
import javax.inject.Singleton

enum class BackendType { REMOTE, FIRESTORE }

@Singleton
class BackendSelector @Inject constructor(
    private val remoteConfigManager: RemoteConfigManager
) {
    fun current(): BackendType =
        if (remoteConfigManager.useFirestore()) BackendType.FIRESTORE else BackendType.REMOTE
}
