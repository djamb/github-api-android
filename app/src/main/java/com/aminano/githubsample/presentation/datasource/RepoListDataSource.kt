package com.aminano.githubsample.presentation.datasource


import androidx.paging.PageKeyedDataSource
import kotlinx.coroutines.*
import androidx.lifecycle.MutableLiveData
import com.aminano.githubsample.data.remote.api.base.Constant.Companion.NAME
import com.aminano.githubsample.data.remote.api.base.Constant.Companion.PAGE_SIZE
import com.aminano.githubsample.data.remote.api.base.Status
import com.aminano.githubsample.data.remote.api.models.GitHubAPIRepoResponse
import com.aminano.githubsample.domain.api.GithubApiClient

class RepoListDataSource(private val githubApiClient: GithubApiClient) :
    PageKeyedDataSource<Int, GitHubAPIRepoResponse>() {

    private val dataSourceJob = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Main + dataSourceJob)
    val loadStateLiveData: MutableLiveData<Status> = MutableLiveData()
    val totalCount: MutableLiveData<Long> = MutableLiveData()
    var page: String= "1"

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, GitHubAPIRepoResponse>
    ) {
        scope.launch {
            loadStateLiveData.postValue(Status.LOADING)
            totalCount.postValue(0)

            val response = githubApiClient.getRepoInfo(NAME, page,PAGE_SIZE)
            when (response.status) {
                Status.ERROR -> loadStateLiveData.postValue(Status.ERROR)
                Status.EMPTY -> loadStateLiveData.postValue(Status.EMPTY)
                else -> {
                    response.data?.let {
                        callback.onResult(it, null, 1)
                        loadStateLiveData.postValue(Status.SUCCESS)
                        totalCount.postValue(it.size.toLong())
                    }
                }
            }
        }
    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, GitHubAPIRepoResponse>
    ) {
        scope.launch {
            var newPage:Int
            newPage= page.toInt() + 1
            page=newPage.toString()
            val response = githubApiClient.getRepoInfo(NAME, page,PAGE_SIZE);
            response.data?.let {
                callback.onResult(it, params.key + 1)
            }
        }
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, GitHubAPIRepoResponse>
    ) {

    }
}