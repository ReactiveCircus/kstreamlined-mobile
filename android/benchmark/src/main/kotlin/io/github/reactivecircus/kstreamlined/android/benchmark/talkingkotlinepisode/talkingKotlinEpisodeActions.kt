package io.github.reactivecircus.kstreamlined.android.benchmark.talkingkotlinepisode

import androidx.benchmark.macro.MacrobenchmarkScope

fun MacrobenchmarkScope.waitForTalkingKotlinEpisodeContent() {
    onElement { viewIdResourceName == "talkingKotlinEpisode:content" }
}
