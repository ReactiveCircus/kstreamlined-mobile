package io.github.reactivecircus.kstreamlined.android.feature.licenses

import io.github.reactivecircus.licentia.runtime.LicensesInfo

internal fun List<LicensesInfo.Artifact>.toArtifactLicenseItems(): List<ArtifactLicenseItem> {
    return map {
        ArtifactLicenseItem(
            title = it.name ?: it.artifactId,
            description = "${it.groupId}:${it.artifactId}",
            version = it.version,
            scmUrl = it.scm?.url,
            licenses = it.spdxLicenses.orEmpty().map { spdxLicense ->
                spdxLicense.name
            } + it.unknownLicenses.orEmpty().map { unknownLicense ->
                unknownLicense.name
            },
        )
    }.sortedBy { it.title.lowercase() }
}
