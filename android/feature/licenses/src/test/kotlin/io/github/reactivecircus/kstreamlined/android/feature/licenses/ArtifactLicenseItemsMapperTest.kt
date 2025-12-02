package io.github.reactivecircus.kstreamlined.android.feature.licenses

import io.github.reactivecircus.kstreamlined.kmp.appinfo.LicensesInfo
import kotlin.test.Test
import kotlin.test.assertEquals

class ArtifactLicenseItemsMapperTest {
    @Test
    fun `title of transformed ArtifactLicenseItem is name if available`() {
        val artifactWithName = LicensesInfo.Artifact(
            name = "name",
            groupId = "group",
            artifactId = "artifact",
            version = "1.0.0",
            spdxLicenses = emptyList(),
            unknownLicenses = emptyList(),
            scm = null,
        )

        assertEquals(
            listOf(
                ArtifactLicenseItem(
                    title = "name",
                    description = "group:artifact",
                    version = "1.0.0",
                    scmUrl = null,
                    licenses = emptyList(),
                ),
            ),
            listOf(artifactWithName).toArtifactLicenseItems(),
        )
    }

    @Test
    fun `title of transformed ArtifactLicenseItem is artifactId if name is not available`() {
        val artifactWithoutName = LicensesInfo.Artifact(
            name = null,
            groupId = "group",
            artifactId = "artifact",
            version = "1.0.0",
            spdxLicenses = emptyList(),
            unknownLicenses = emptyList(),
            scm = null,
        )

        assertEquals(
            listOf(
                ArtifactLicenseItem(
                    title = "artifact",
                    description = "group:artifact",
                    version = "1.0.0",
                    scmUrl = null,
                    licenses = emptyList(),
                ),
            ),
            listOf(artifactWithoutName).toArtifactLicenseItems(),
        )
    }

    @Test
    fun `licenses of transformed ArtifactLicenseItem contain both SPDX and unknown licenses`() {
        val artifactWithLicenses = LicensesInfo.Artifact(
            name = "name",
            groupId = "group",
            artifactId = "artifact",
            version = "1.0.0",
            scm = null,
            spdxLicenses = listOf(
                LicensesInfo.Artifact.SpdxLicense(
                    identifier = "MIT",
                    name = "MIT License",
                    url = "https://opensource.org/license/mit/",
                ),
            ),
            unknownLicenses = listOf(
                LicensesInfo.Artifact.UnknownLicense(
                    name = "Android Software Development Kit License",
                    url = "https://developer.android.com/studio/terms.html",
                ),
            ),
        )

        assertEquals(
            listOf(
                ArtifactLicenseItem(
                    title = "name",
                    description = "group:artifact",
                    version = "1.0.0",
                    scmUrl = null,
                    licenses = listOf("MIT License", "Android Software Development Kit License"),
                ),
            ),
            listOf(artifactWithLicenses).toArtifactLicenseItems(),
        )
    }

    @Test
    fun `scmUrl of transformed ArtifactLicenseItem is expected`() {
        val artifactWithScm = LicensesInfo.Artifact(
            name = "name",
            groupId = "group",
            artifactId = "artifact",
            version = "1.0.0",
            scm = LicensesInfo.Artifact.Scm(url = "https://github.com/group/artifact"),
            spdxLicenses = emptyList(),
            unknownLicenses = emptyList(),
        )

        assertEquals(
            listOf(
                ArtifactLicenseItem(
                    title = "name",
                    description = "group:artifact",
                    version = "1.0.0",
                    scmUrl = "https://github.com/group/artifact",
                    licenses = emptyList(),
                ),
            ),
            listOf(artifactWithScm).toArtifactLicenseItems(),
        )
    }

    @Test
    fun `transformed ArtifactLicenseItems are sorted by title`() {
        val artifacts = listOf(
            LicensesInfo.Artifact(
                name = "name1",
                groupId = "group",
                artifactId = "artifact-b",
                version = "1.0.0",
                scm = null,
                spdxLicenses = emptyList(),
                unknownLicenses = emptyList(),
            ),
            LicensesInfo.Artifact(
                name = "Name2",
                groupId = "group",
                artifactId = "artifact-c",
                version = "1.0.0",
                scm = null,
                spdxLicenses = emptyList(),
                unknownLicenses = emptyList(),
            ),
            LicensesInfo.Artifact(
                name = null,
                groupId = "group",
                artifactId = "artifact-a",
                version = "1.0.0",
                scm = null,
                spdxLicenses = emptyList(),
                unknownLicenses = emptyList(),
            ),
        )

        assertEquals(
            listOf(
                ArtifactLicenseItem(
                    title = "artifact-a",
                    description = "group:artifact-a",
                    version = "1.0.0",
                    scmUrl = null,
                    licenses = emptyList(),
                ),
                ArtifactLicenseItem(
                    title = "name1",
                    description = "group:artifact-b",
                    version = "1.0.0",
                    scmUrl = null,
                    licenses = emptyList(),
                ),
                ArtifactLicenseItem(
                    title = "Name2",
                    description = "group:artifact-c",
                    version = "1.0.0",
                    scmUrl = null,
                    licenses = emptyList(),
                ),
            ),
            artifacts.toArtifactLicenseItems(),
        )
    }
}
