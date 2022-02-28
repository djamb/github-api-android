package com.aminano.githubsample.data.remote.api

import com.aminano.githubsample.data.remote.api.models.*
import retrofit2.Response
import retrofit2.http.*

interface GithubApi {

    @GET("users/{username}/repos")
    suspend fun getRepoInfo(
        @Path("username") username: String,
       @Query("page") pageNumber: String,
        @Query("per_page") repoPerPage: String
    ): Response<List<GitHubAPIRepoResponse>>
}