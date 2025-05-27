package io.github.reactivecircus.kstreamlined.android.benchmark.baselineprofile

import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import io.github.reactivecircus.kstreamlined.android.benchmark.PackageName
import io.github.reactivecircus.kstreamlined.android.benchmark.home.homeFeedListScrollDown
import io.github.reactivecircus.kstreamlined.android.benchmark.home.waitForHomeFeedContent
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class BaselineProfileGenerator {

    @get:Rule
    val rule = BaselineProfileRule()

    @Test
    fun generate() = rule.collect(
        packageName = PackageName,
        includeInStartupProfile = true,
        strictStability = true,
    ) {
        pressHome()
        startActivityAndWait()
        runCatching {
            waitForHomeFeedContent()
            homeFeedListScrollDown()
        }
    }
}
