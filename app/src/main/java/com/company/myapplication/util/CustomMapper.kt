package com.company.myapplication.util

import com.company.myapplication.data.model.chat.ConversationDTO
import com.company.myapplication.data.model.chat.ConversationViewDTO
import com.company.myapplication.data.model.response.UserResponse

object CustomMapper {
    fun ConversationDTO.toViewDTO(): ConversationViewDTO {
        return ConversationViewDTO(
            id = this.id,
            groupAvatar = this.groupAvatar,
            conversationName = this.conversationName,
            themeColor = this.themeColor,
            conversationType = this.conversationType,
            membersIds = this.membersIds
                ?.split(",")
                ?.mapNotNull { it.trim().toLongOrNull() }
                ?: emptyList(),
            name = this.name.split(",").map { it.trim() },
            pairAvatar = this.pairAvatar?.split(",")?.map { it.trim() } ?: emptyList(),
            content = this.content,
            mediaFile = this.mediaFile,
            senderId = this.senderId,
            createdAt = this.createdAt,
            isRead = this.isRead
        )
    }

    fun ConversationViewDTO.filterUser(user: UserResponse): ConversationViewDTO {
        return this.copy(
            membersIds = this.membersIds.filter { it != user.id },
            name = this.name.filter { it != user.name },
            pairAvatar = this.pairAvatar.filter { it != user.avatar }
        )
    }


}