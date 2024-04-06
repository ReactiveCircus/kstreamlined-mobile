package io.github.reactivecircus.benchmark.baselineprofile

import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import io.github.reactivecircus.benchmark.PackageName
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class BaselineProfileGenerator {

    @get:Rule
    val rule = BaselineProfileRule()

    @Test
    fun generate() {
        rule.collect(
            packageName = PackageName,
            // TODO enable once https://issuetracker.google.com/issues/332982932 is fixed
            includeInStartupProfile = false,
            strictStability = true,
        ) {
            pressHome()
            startActivityAndWait()
            // TODO wait until home feed content is displayed
            // TODO scroll
        }
    }
}
