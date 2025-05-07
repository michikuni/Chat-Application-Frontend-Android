package com.company.myapplication.data.model.response

data class CreateGroupResponse(
    val id: Long,
    val groupName: String,
    val avatar: String?,
    val createdBy: Long,
    val createdTime: String
)
