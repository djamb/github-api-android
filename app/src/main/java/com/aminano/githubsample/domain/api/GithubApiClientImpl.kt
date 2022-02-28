package com.aminano.githubsample.domain.api

import android.util.Log
import com.aminano.githubsample.data.remote.api.GithubApi
import com.aminano.githubsample.data.remote.api.base.Resource
import com.aminano.githubsample.data.remote.api.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GithubApiClientImpl(private val githubApi: GithubApi) : GithubApiClient {

    override suspend fun getRepoInfo(
        username: String,
        page: String,
        pageSize: String
    ): Resource<List<GitHubAPIRepoResponse>> = withContext(Dispatchers.IO) {
        try {
            val response = githubApi.getRepoInfo(username, page, pageSize)

            if (response.isSuccessful) {
                Resource.success(response.body())

            } else {
                Resource.error(response.message())
            }
        } catch (ex: Throwable) {
            Resource.error<List<GitHubAPIRepoResponse>>("${ex.message}")
        }
    }

}