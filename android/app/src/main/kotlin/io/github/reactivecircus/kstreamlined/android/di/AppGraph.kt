package io.github.reactivecircus.kstreamlined.android.di

import android.app.Application
import android.content.Context
import android.os.Build
import androidx.tracing.trace
import app.cash.sqldelight.EnumColumnAdapter
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import coil3.ImageLoader
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import coil3.request.allowHardware
import coil3.request.crossfade
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import dev.zacsweers.metrox.viewmodel.ViewModelGraph
import io.github.reactivecircus.kstreamlined.android.BuildConfig
import io.github.reactivecircus.kstreamlined.android.core.navigation.NavEntryInstaller
import io.github.reactivecircus.kstreamlined.android.core.scheduledwork.KSWorkerFactory
import io.github.reactivecircus.kstreamlined.android.core.scheduledwork.sync.SyncScheduler
import io.github.reactivecircus.kstreamlined.android.licentia.AllLicensesInfo
import io.github.reactivecircus.kstreamlined.kmp.appinfo.AppInfo
import io.github.reactivecircus.kstreamlined.kmp.database.FeedItemEntity
import io.github.reactivecircus.kstreamlined.kmp.database.InstantAdapter
import io.github.reactivecircus.kstreamlined.kmp.database.KStreamlinedDatabase
import io.github.reactivecircus.kstreamlined.kmp.database.LastSyncMetadata
import io.github.reactivecircus.kstreamlined.kmp.settings.datasource.SettingsDataSource
import io.github.reactivecircus.kstreamlined.kmp.settings.datasource.datastore.createAppSettingsDataStore
import io.github.reactivecircus.licentia.runtime.LicensesInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okhttp3.Call
import okhttp3.OkHttpClient
import kotlin.time.DurationUnit
import kotlin.time.toDuration
import kotlin.time.toJavaDuration

@DependencyGraph(AppScope::class)
interface AppGraph : ViewModelGraph, NetworkProviders {
    val imageLoader: Lazy<ImageLoader>

    val appCoroutineScope: CoroutineScope

    val workerFactory: Lazy<KSWorkerFactory>

    val syncScheduler: SyncScheduler

    val settingsDataSource: SettingsDataSource

    val navEntryInstallers: Set<NavEntryInstaller>

    @Provides
    fun provideApplicationContext(application: Application): Context = application

    @SingleIn(AppScope::class)
    @Provides
    fun provideImageLoader(
        context: Context,
        okHttpCallFactory: Lazy<Call.Factory>,
    ): ImageLoader = trace("CoilImageLoader") {
        return ImageLoader.Builder(context)
            .components {
                add(OkHttpNetworkFetcherFactory({ okHttpCallFactory.value }))
            }
            .crossfade(enable = true)
            // only enable hardware bitmaps on API 28+. See: https://github.com/coil-kt/coil/issues/159
            .allowHardware(enable = Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
            .build()
    }

    @SingleIn(AppScope::class)
    @Provides
    fun proviedOkHttpCallFactory(): Call.Factory = trace("KSOkHttpClient") {
        val callTimeout = BuildConfig.NETWORK_TIMEOUT_SECONDS
            .toDuration(DurationUnit.SECONDS)
            .toJavaDuration()
        OkHttpClient.Builder()
            .callTimeout(callTimeout)
            .build()
    }

    @SingleIn(AppScope::class)
    @Provides
    fun provideAppCoroutineScope(): CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    @SingleIn(AppScope::class)
    @Provides
    fun provideDatabase(
        context: Context,
    ): KStreamlinedDatabase {
        return KStreamlinedDatabase(
            AndroidSqliteDriver(
                schema = KStreamlinedDatabase.Schema,
                context = context,
                name = "kstreamlined.db",
            ),
            feedItemEntityAdapter = FeedItemEntity.Adapter(
                publish_timeAdapter = InstantAdapter,
                podcast_description_formatAdapter = EnumColumnAdapter(),
            ),
            lastSyncMetadataAdapter = LastSyncMetadata.Adapter(
                resource_typeAdapter = EnumColumnAdapter(),
                last_sync_timeAdapter = InstantAdapter,
            ),
        )
    }

    @SingleIn(AppScope::class)
    @Provides
    fun provideSettingsDataSource(
        context: Context,
        appCoroutineScope: CoroutineScope,
    ): SettingsDataSource = SettingsDataSource(dataStore = createAppSettingsDataStore(context))

    @Provides
    fun provideAppInfo(): AppInfo = AppInfo(
        versionName = BuildConfig.VERSION_NAME,
        sourceCodeUrl = BuildConfig.SOURCE_CODE_URL,
    )

    @Provides
    fun provideLicensesInfo(): LicensesInfo = AllLicensesInfo

    @DependencyGraph.Factory
    fun interface Factory {
        fun create(@Provides application: Application): AppGraph
    }
}
