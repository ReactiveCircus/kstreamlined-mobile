package io.github.reactivecircus.kstreamlined.android.di

import android.content.Context
import android.os.Build
import coil3.ImageLoader
import coil3.request.allowHardware
import coil3.request.crossfade
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun imageLoader(@ApplicationContext context: Context): ImageLoader {
        return ImageLoader.Builder(context)
            .crossfade(enable = true)
            // only enable hardware bitmaps on API 28+. See: https://github.com/coil-kt/coil/issues/159
            .allowHardware(enable = Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
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
