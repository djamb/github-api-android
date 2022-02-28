package com.aminano.githubsample.data.remote.api.models

import com.google.gson.annotations.SerializedName


data class GitHubAPIRepoResponse(
    @SerializedName("name") var repoName: String ,
    @SerializedName("description") var repoDescription: String,
    @SerializedName("html_url") var url: String,
    @SerializedName("fork") var isForked: Boolean = false,
    @SerializedName("owner") var items: GitHubOnwerURL
)