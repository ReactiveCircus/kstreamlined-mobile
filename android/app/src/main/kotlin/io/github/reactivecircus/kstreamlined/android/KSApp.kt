package io.github.reactivecircus.kstreamlined.android

import android.app.Application
import androidx.work.Configuration
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dev.zacsweers.metro.createGraphFactory
import io.github.reactivecircus.kstreamlined.android.di.AppGraph
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

open class KSApp : Application(), SingletonImageLoader.Factory, Configuration.Provider {
    val appGraph by lazy { createGraphFactory<AppGraph.Factory>().create(this) }

    override fun onCreate() {
        super.onCreate()

        // TODO enable / disable AnalyticsTracker

        if (BuildConfig.ENABLE_CRASH_REPORTING) {
            FirebaseCrashlytics.getInstance().isCrashlyticsCollectionEnabled = true
        }

        initializeKermit()

        appGraph.appCoroutineScope.launch(Dispatchers.Main.immediate) {
            appGraph.syncScheduler.initialize()
        }
    }

    override fun newImageLoader(context: PlatformContext): ImageLoader = appGraph.imageLoader.value

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(appGraph.workerFactory.value)
            .build()

    protected open fun initializeKermit() {
        // TODO
    }
}
