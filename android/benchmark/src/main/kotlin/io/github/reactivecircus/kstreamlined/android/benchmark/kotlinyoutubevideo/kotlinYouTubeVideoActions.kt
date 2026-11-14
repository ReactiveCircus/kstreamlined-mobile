package io.github.reactivecircus.kstreamlined.android.benchmark.kotlinyoutubevideo

import androidx.benchmark.macro.MacrobenchmarkScope

fun MacrobenchmarkScope.waitForKotlinYouTubeVideoContent() {
    onElement { viewIdResourceName == "kotlinYouTubeVideo:content" }
}
