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
import io.github.reactivecircus.kstreamlined.android.foundation.scheduledwork.WorkScheduler
import javax.inject.Inject

@HiltAndroidApp
open class KSApp : Application(), SingletonImageLoader.Factory, Configuration.Provider {

    @Inject
    lateinit var imageLoader: Lazy<ImageLoader>

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var workScheduler: WorkScheduler

    override fun onCreate() {
        super.onCreate()

        // TODO enable / disable AnalyticsTracker

        if (BuildConfig.ENABLE_CRASH_REPORTING) {
            FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        }

        initializeKermit()

        workScheduler.schedule()
    }

    override fun newImageLoader(context: PlatformContext): ImageLoader = imageLoader.get()

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    protected open fun initializeKermit() {
        // TODO
    }
}
