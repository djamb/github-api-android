package com.aminano.githubsample.presentation.viewmodel


import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.aminano.githubsample.data.remote.api.base.Constant.Companion.PAGE_SIZE
import com.aminano.githubsample.data.remote.api.models.GitHubAPIRepoResponse
import com.aminano.githubsample.presentation.datasource.RepoListDataSource
import com.aminano.githubsample.presentation.datasource.RepoListDataSourceFactory


import java.util.concurrent.Executors

class RepoListViewModel(private val repoListDataSourceFactory: RepoListDataSourceFactory) :
    ViewModel() {

    var dataSource: MutableLiveData<RepoListDataSource>
    lateinit var usersLiveData: LiveData<PagedList<GitHubAPIRepoResponse>>
    val isWaiting: ObservableField<Boolean> = ObservableField()
    val errorMessage: ObservableField<String> = ObservableField()
    val totalCount: ObservableField<Long> = ObservableField()

    init {
        isWaiting.set(true)
        errorMessage.set(null)
        dataSource = repoListDataSourceFactory.liveData
        initUsersListFactory()
    }

    private fun initUsersListFactory() {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setInitialLoadSizeHint(PAGE_SIZE.toInt())
            .setPageSize(PAGE_SIZE.toInt())
            .setPrefetchDistance(3)
            .build()

        val executor = Executors.newFixedThreadPool(5)

        usersLiveData =
            LivePagedListBuilder<Int, GitHubAPIRepoResponse>(repoListDataSourceFactory, config)
                .setFetchExecutor(executor)
                .build()
    }
}
