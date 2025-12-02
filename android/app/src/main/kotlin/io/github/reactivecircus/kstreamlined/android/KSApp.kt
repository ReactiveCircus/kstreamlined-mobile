package io.github.reactivecircus.kstreamlined.android

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.Lazy
import dagger.hilt.android.HiltAndroidApp
import io.github.reactivecircus.kstreamlined.android.core.scheduledwork.sync.SyncScheduler
import io.github.reactivecircus.kstreamlined.android.di.AppCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
open class KSApp : Application(), SingletonImageLoader.Factory, Configuration.Provider {
    @Inject
    lateinit var imageLoader: Lazy<ImageLoader>

    @Inject
    lateinit var workerFactory: Lazy<HiltWorkerFactory>

    @Inject
    lateinit var syncScheduler: SyncScheduler

    @Inject
    @AppCoroutineScope
    lateinit var appCoroutineScope: CoroutineScope

    override fun onCreate() {
        super.onCreate()

        // TODO enable / disable AnalyticsTracker

        if (BuildConfig.ENABLE_CRASH_REPORTING) {
            FirebaseCrashlytics.getInstance().isCrashlyticsCollectionEnabled = true
        }

        initializeKermit()

        appCoroutineScope.launch(Dispatchers.Main.immediate) { syncScheduler.initialize() }
    }

    override fun newImageLoader(context: PlatformContext): ImageLoader = imageLoader.get()

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory.get())
            .build()
    }

    protected open fun initializeKermit() {
        // TODO
    }
}
