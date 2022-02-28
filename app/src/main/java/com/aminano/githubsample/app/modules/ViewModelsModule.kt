package com.aminano.githubsample.app.modules

import com.aminano.githubsample.presentation.viewmodel.RepoListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val repoListViewModel = module {
    viewModel { RepoListViewModel(get()) }
}
