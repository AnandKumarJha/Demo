package com.testing.demo.model.pojo

data class PostModel(
    val title: String? = "",
    val description: String? = "",
    val createdBy: String? = "",
    val createdOn: String? = "",
    val profilePics: ArrayList<String?> = ArrayList(),
    val phoneNumber: String? = ""
)