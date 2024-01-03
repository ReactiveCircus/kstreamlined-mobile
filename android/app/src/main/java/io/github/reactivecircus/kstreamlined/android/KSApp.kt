package io.github.reactivecircus.kstreamlined.android

import android.app.Application
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
open class KSApp : Application(), SingletonImageLoader.Factory {

    @Inject
    lateinit var imageLoader: ImageLoader

    override fun onCreate() {
        super.onCreate()

        // TODO enable / disable AnalyticsTracker

        if (BuildConfig.ENABLE_CRASH_REPORTING) {
            FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        }

        initializeKermit()
    }

    protected open fun initializeKermit() {
        // TODO
    }

    override fun newImageLoader(context: PlatformContext): ImageLoader = imageLoader
}
