package io.github.reactivecircus.kstreamlined.android

import android.app.Application
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.HiltAndroidApp
import io.github.reactivecircus.kstreamlined.kmp.feed.sync.FeedSyncEngine
import javax.inject.Inject

@HiltAndroidApp
open class KSApp : Application(), SingletonImageLoader.Factory {

    @Inject
    lateinit var imageLoader: ImageLoader

    @Inject
    lateinit var feedSyncEngine: FeedSyncEngine

    override fun onCreate() {
        super.onCreate()

        // TODO enable / disable AnalyticsTracker

        if (BuildConfig.ENABLE_CRASH_REPORTING) {
            FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        }

        initializeKermit()
    }

    override fun newImageLoader(context: PlatformContext): ImageLoader = imageLoader

    protected open fun initializeKermit() {
        // TODO
    }
}
