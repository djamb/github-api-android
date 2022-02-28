package com.aminano.githubsample.data.remote.api.models

import com.google.gson.annotations.SerializedName

data class GitHubOnwerURL(
    @SerializedName("login") var login: String,
    @SerializedName("html_url") var url: String,
    @SerializedName("avatar_url") var avatar_url: String


)