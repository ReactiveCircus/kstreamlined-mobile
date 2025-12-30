package io.github.reactivecircus.kstreamlined.android.feature.kotlinweeklyissue.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
public data class KotlinWeeklyIssueRoute(
    val origin: String,
    val id: String,
    val issueNumber: Int,
) : NavKey
