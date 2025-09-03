package com.company.myapplication.viewmodel

import android.app.Activity
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.myapplication.data.model.chat.CreateConversation
import com.company.myapplication.data.model.chat.GetConversation
import com.company.myapplication.data.model.chat.Message
import com.company.myapplication.repository.ConversationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ConversationViewModel(activity: Activity): ViewModel(){
    private val repo = ConversationRepository(activity)

    private var errorMsg by mutableStateOf<String?>(null)

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> get() = _messages

    private var createConversationSuccess by mutableStateOf(false)

    private val _conversation = MutableStateFlow<List<GetConversation>>(emptyList())
    val conversation: StateFlow<List<GetConversation>> get() = _conversation

    fun getAllMessage(conversationId: Long){
        viewModelScope.launch {
            try {
                val message = repo.getAllMessage(conversationId = conversationId)
                if (message != null){
                    _messages.value = message
                }
            } catch (e: Exception){
                errorMsg = e.message
            }
        }
    }

    fun createConversation(userId: Long, body: CreateConversation){
        viewModelScope.launch {
            val createConversation = repo.createConversation(userId = userId, body = body)
            createConversationSuccess = createConversation
        }
    }

    fun getAllConversation(userId: Long) {
        viewModelScope.launch {
            try {
                val conversation = repo.getAllConversation(userId)
                if (conversation != null) _conversation.value = conversation
            } catch (e: Exception) {
                errorMsg = e.message
            }
        }
    }


    fun updateTheme(context: Context, conversationId: Long, color: List<String>){
        viewModelScope.launch {
            try {
                repo.updateTheme(context = context, conversationId = conversationId, color = color)
            } catch (e: Exception){
                errorMsg = e.message
            }
        }
    }

}