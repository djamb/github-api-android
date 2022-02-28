package com.aminano.githubsample.app

import android.app.Application
import com.aminano.githubsample.BuildConfig
import com.aminano.githubsample.app.modules.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class Application : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@Application)
            modules(
                listOf(
                    githubApiModule,
                    githubApiClientModule,
                    repoListViewModel,
                    repoListDataSourceFactory

                )
            )
        }

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

}