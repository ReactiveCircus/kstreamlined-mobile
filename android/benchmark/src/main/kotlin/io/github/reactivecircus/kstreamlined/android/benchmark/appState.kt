package io.github.reactivecircus.kstreamlined.android.benchmark

import android.net.Uri
import androidx.test.platform.app.InstrumentationRegistry

fun appState(block: AppStateScope.() -> Unit) {
    AppStateScopeImpl.block()
}

sealed interface AppStateScope {
    fun resetFeedSelections()
    fun selectSingleFeed(feedOriginKey: FeedOriginKey)
}

private object AppStateScopeImpl : AppStateScope {
    private val contentResolver
        get() = InstrumentationRegistry
            .getInstrumentation()
            .context
            .contentResolver

    private val uri: Uri
        get() = Uri.parse("content://$PackageName.benchmark")

    override fun resetFeedSelections() {
        contentResolver.call(uri, "resetFeedSelections", null, null)
    }

    override fun selectSingleFeed(feedOriginKey: FeedOriginKey) {
        contentResolver.call(uri, "selectSingleFeed", feedOriginKey.name, null)
    }
}

enum class FeedOriginKey {
    KotlinBlog,
    KotlinYouTubeChannel,
    TalkingKotlinPodcast,
    KotlinWeekly,
}
