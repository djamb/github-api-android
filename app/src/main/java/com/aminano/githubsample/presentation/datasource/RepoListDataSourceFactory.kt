package com.aminano.githubsample.presentation.datasource

import androidx.paging.DataSource
import androidx.lifecycle.MutableLiveData
import com.aminano.githubsample.data.remote.api.models.GitHubAPIRepoResponse
import com.aminano.githubsample.domain.api.GithubApiClient

class RepoListDataSourceFactory(private val githubApiClient: GithubApiClient): DataSource.Factory<Int,  GitHubAPIRepoResponse>() {

    val liveData: MutableLiveData<RepoListDataSource> = MutableLiveData()

    override fun create(): DataSource<Int,  GitHubAPIRepoResponse> {
        val usersListDataSource = RepoListDataSource(githubApiClient)
        liveData.postValue(usersListDataSource)
        return usersListDataSource
    }
}