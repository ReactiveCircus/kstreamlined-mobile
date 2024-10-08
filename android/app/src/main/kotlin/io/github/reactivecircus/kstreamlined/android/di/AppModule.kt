package io.github.reactivecircus.kstreamlined.android.di

import android.content.Context
import android.os.Build
import androidx.tracing.trace
import coil3.ImageLoader
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import coil3.request.allowHardware
import coil3.request.crossfade
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.reactivecircus.kstreamlined.android.BuildConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okhttp3.Call
import okhttp3.OkHttpClient
import javax.inject.Qualifier
import javax.inject.Singleton
import kotlin.time.DurationUnit
import kotlin.time.toDuration
import kotlin.time.toJavaDuration

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun imageLoader(
        @ApplicationContext context: Context,
        okHttpCallFactory: Lazy<Call.Factory>,
    ): ImageLoader = trace("CoilImageLoader") {
        OkHttpNetworkFetcherFactory()
        return ImageLoader.Builder(context)
            .components {
                add(OkHttpNetworkFetcherFactory({ okHttpCallFactory.get() }))
            }
            .crossfade(enable = true)
            // only enable hardware bitmaps on API 28+. See: https://github.com/coil-kt/coil/issues/159
            .allowHardware(enable = Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
            .build()
    }

    @Provides
    @Singleton
    fun okHttpCallFactory(): Call.Factory = trace("KSOkHttpClient") {
        val callTimeout = BuildConfig.NETWORK_TIMEOUT_SECONDS
            .toDuration(DurationUnit.SECONDS)
            .toJavaDuration()
        OkHttpClient.Builder()
            .callTimeout(callTimeout)
            .build()
    }

    @Provides
    @Singleton
    @AppCoroutineScope
    fun appCoroutineScope(): CoroutineScope {
        return CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }
}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class AppCoroutineScope
