@file:Suppress("MaxLineLength")

package io.github.reactivecircus.kstreamlined.kmp.remote

import io.github.reactivecircus.kstreamlined.kmp.remote.model.KotlinBlogTldr
import io.github.reactivecircus.kstreamlined.kmp.remote.model.KotlinWeeklyIssueEntry
import kotlin.time.Instant

public val FakeKotlinWeeklyIssueEntries: List<KotlinWeeklyIssueEntry> = listOf(
    KotlinWeeklyIssueEntry(
        title = "Amper Update – December 2023",
        summary = "Last month JetBrains introduced Amper, a tool to improve the project configuration user experience. Marton Braun gives us an update about its state in December 2023.",
        url = "https://blog.jetbrains.com/amper/2023/12/amper-update-december-2023/",
        source = "blog.jetbrains.com",
        group = KotlinWeeklyIssueEntry.Group.News,
    ),
    KotlinWeeklyIssueEntry(
        title = "How to Use the Cucumber Framework to Test Application Use Cases",
        summary = "Matthias Schenk writes today about Cucumber, a framework that can be used in application development to verify the correct behavior of the application.",
        url = "https://towardsdev.com/how-to-use-the-cucumber-framework-to-test-application-use-cases-48b4f21ee0d0",
        source = "towardsdev.com",
        group = KotlinWeeklyIssueEntry.Group.Articles,
    ),
    KotlinWeeklyIssueEntry(
        title = "Using launcher and themed icons in Android Studio, the manual way",
        summary = "Marlon Lòpez describes how we can use the launcher and the themed icons in Android Studio.",
        url = "https://dev.to/marlonlom/using-launcher-and-themed-icons-in-android-studio-the-manual-way-1h2a",
        source = "dev.to",
        group = KotlinWeeklyIssueEntry.Group.Android,
    ),
    KotlinWeeklyIssueEntry(
        title = "Setting Sail with Compose Multiplatform by Isuru Rajapakse",
        summary = "Isuru Rajapakse talks at the DevFest Sri Lanka about Compose Multiplatform.",
        url = "https://www.youtube.com/watch?v=sG60644C47I",
        source = "www.youtube.com",
        group = KotlinWeeklyIssueEntry.Group.Videos,
    ),
    KotlinWeeklyIssueEntry(
        title = "Kim - Kotlin Image Metadata",
        summary = "Kim is a Kotlin image metadata manipulation library for Kotlin Multiplatform.",
        url = "https://github.com/Ashampoo/kim",
        source = "github.com",
        group = KotlinWeeklyIssueEntry.Group.Libraries,
    ),
)

public val FakeKotlinBlogTldr: KotlinBlogTldr = KotlinBlogTldr(
    id = "https://blog.jetbrains.com/?post_type=kotlin&p=405553",
    content = """
        Kotlin 2.4.20 is a tooling release focused on multiplatform interop, smaller Wasm binaries and a faster compiler CLI.

        ### Standard library
        - Support for coroutine stack trace recovery.
        - Experimental `allDistinct()` and `allEqual()` check collection elements.
        - New overloads for `kotlin.test` assertion functions.
    """.trimIndent(),
    model = "gpt-oss-120b",
    generatedAt = Instant.parse("2026-09-07T00:00:00Z"),
)
