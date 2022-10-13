package com.example.thirdactivity.api

import com.google.gson.annotations.SerializedName

data class UserInfo(
    @SerializedName("id") val id: Int?,
    @SerializedName("nom") val nom: String?,
    @SerializedName("prenom") val prenom: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("classe") val classe: String?,
)
