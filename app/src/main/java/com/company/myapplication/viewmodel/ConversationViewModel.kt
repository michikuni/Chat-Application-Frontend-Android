package com.company.myapplication.viewmodel

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.myapplication.data.model.chat.ConversationDTO
import com.company.myapplication.data.model.chat.CreateConversation
import com.company.myapplication.data.model.chat.CreateConversationGroup
import com.company.myapplication.data.model.chat.Message
import com.company.myapplication.domain.repository.ConversationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConversationViewModel @Inject constructor(
    private val repo: ConversationRepository
) : ViewModel() {

    private var errorMsg by mutableStateOf<String?>(null)

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> get() = _messages

    private var createMessageSuccess by mutableStateOf(false)

    private val _conversation = MutableStateFlow<List<ConversationDTO>>(emptyList())
    val conversation: StateFlow<List<ConversationDTO>> get() = _conversation

    fun getAllMessage(conversationId: Long) {
        viewModelScope.launch {
            try {
                repo.getAllMessage(conversationId)?.let { _messages.value = it }
            } catch (e: Exception) {
                errorMsg = e.message
            }
        }
    }

    fun createConversation(userId: Long, body: CreateConversation) {
        viewModelScope.launch {
            createMessageSuccess = repo.createMessage(userId, body)
        }
    }

    fun getAllConversation(userId: Long) {
        viewModelScope.launch {
            try {
                repo.getAllConversation(userId)?.let { _conversation.value = it }
            } catch (e: Exception) {
                errorMsg = e.message
            }
        }
    }

    fun updateTheme(context: Context, conversationId: Long, color: List<String>) {
        viewModelScope.launch {
            try {
                repo.updateTheme(context, conversationId, color)
            } catch (e: Exception) {
                errorMsg = e.message
            }
        }
    }

    suspend fun findConversation(userId: Long, friendId: Long): Long =
        repo.findConversation(userId, friendId)

    fun sendMediaFile(context: Context, uri: Uri, userId: Long, conversationId: Long) {
        viewModelScope.launch {
            repo.sendMediaFile(context, uri, userId, conversationId)
        }
    }

    fun createConversationGroup(data: CreateConversationGroup, uri: Uri, context: Context) {
        viewModelScope.launch {
            repo.createConversationGroup(data, uri, context)
        }
    }
}
