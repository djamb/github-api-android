package com.aminano.githubsample.domain.api

import com.aminano.githubsample.data.remote.api.base.Resource
import com.aminano.githubsample.data.remote.api.models.*

interface GithubApiClient {

    suspend fun getRepoInfo(
        username: String,
        page: String,
        pageSize: String
    ): Resource<List<GitHubAPIRepoResponse>>

}