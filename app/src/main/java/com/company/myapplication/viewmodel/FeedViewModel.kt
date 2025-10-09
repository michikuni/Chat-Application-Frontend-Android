package com.company.myapplication.viewmodel

import android.app.Activity
import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.myapplication.data.model.chat.Message
import com.company.myapplication.data.model.feed.FeedDTO
import com.company.myapplication.data.model.response.ApiResponse
import com.company.myapplication.repository.FeedRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FeedViewModel(activity: Activity): ViewModel(){
    val repo = FeedRepository(context = activity)

    private val _state = MutableStateFlow<ApiResponse?>(null)
    val state: StateFlow<ApiResponse?> get() = _state

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> get() = _loading
//    private val _allFeed = MutableStateFlow<List<FeedDTO>>(emptyList())
//    val allFeed: StateFlow<List<FeedDTO>> get() = _allFeed
//
//    fun getAllFeed(userId: Long){
//        viewModelScope.launch {
//            val result = repo.getAllFeeds(userId = userId)
//            try {
//                if (result != null) {
//                    _allFeed.value = result
//                }
//            } catch (e: Exception){
//                _state.value = ApiResponse(error = e.message)
//            } finally {
//                _loading.value = false
//            }
//        }
//    }

    private val _allFeed = MutableStateFlow<List<FeedDTO>>(emptyList())
    val allFeed: StateFlow<List<FeedDTO>> = _allFeed

    fun getAllFeed(userId: Long) {
        viewModelScope.launch {
            try {
                val response = repo.getAllFeedByUserId(userId)
                _allFeed.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun postNewsFeed(context: Context, userId: Long, content: String?, mediaUri: Uri?) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val res = repo.postNewsFeed(context, userId, content, mediaUri)
                _state.value = res.body() ?: ApiResponse(error = "Empty response")
            } catch (e: Exception) {
                _state.value = ApiResponse(error = e.message)
            } finally {
                _loading.value = false
            }
        }
    }

    fun clearState() {
        _state.value = null
    }
}