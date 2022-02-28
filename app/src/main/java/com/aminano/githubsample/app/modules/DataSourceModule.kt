package com.aminano.githubsample.app.modules

import com.aminano.githubsample.presentation.datasource.RepoListDataSourceFactory
import org.koin.dsl.module

val repoListDataSourceFactory = module {
    single { RepoListDataSourceFactory(get()) }
}
