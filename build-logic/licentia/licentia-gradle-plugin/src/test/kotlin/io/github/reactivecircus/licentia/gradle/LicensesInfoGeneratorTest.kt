package io.github.reactivecircus.licentia.gradle

import kotlin.test.Test
import kotlin.test.assertEquals

class LicensesInfoGeneratorTest {
    @Test
    fun `LicensesInfoGenerator generates expected AllLicensesInfo object from Json`() {
        val artifactsJson =
            """
            |[
            |    {
            |        "groupId": "androidx.activity",
            |        "artifactId": "activity",
            |        "version": "1.12.1",
            |        "name": "Activity",
            |        "spdxLicenses": [
            |            {
            |                "identifier": "Apache-2.0",
            |                "name": "Apache License 2.0",
            |                "url": "https://www.apache.org/licenses/LICENSE-2.0"
            |            }
            |        ],
            |        "scm": {
            |            "url": "https://cs.android.com/androidx/platform/frameworks/support"
            |        }
            |    },
            |    {
            |        "groupId": "org.jetbrains.kotlin",
            |        "artifactId": "kotlin-stdlib",
            |        "version": "2.1.0",
            |        "spdxLicenses": [
            |            {
            |                "identifier": "Apache-2.0",
            |                "name": "Apache License 2.0",
            |                "url": "https://www.apache.org/licenses/LICENSE-2.0"
            |            }
            |        ],
            |        "scm": {
            |            "url": "https://github.com/JetBrains/kotlin"
            |        }
            |    },
            |    {
            |        "groupId": "com.example",
            |        "artifactId": "no-scm-library",
            |        "version": "1.0.0",
            |        "name": "No SCM Library",
            |        "spdxLicenses": [
            |            {
            |                "identifier": "MIT",
            |                "name": "MIT License",
            |                "url": "https://opensource.org/licenses/MIT"
            |            }
            |        ]
            |    },
            |    {
            |        "groupId": "com.example",
            |        "artifactId": "unknown-license-library",
            |        "version": "2.0.0",
            |        "name": "Unknown License Library",
            |        "unknownLicenses": [
            |            {
            |                "name": "Custom License",
            |                "url": "https://example.com/license"
            |            }
            |        ],
            |        "scm": {
            |            "url": "https://github.com/example/unknown-license"
            |        }
            |    },
            |    {
            |        "groupId": "com.example",
            |        "artifactId": "multiple-licenses",
            |        "version": "3.0.0",
            |        "name": "Multiple Licenses Library",
            |        "spdxLicenses": [
            |            {
            |                "identifier": "Apache-2.0",
            |                "name": "Apache License 2.0",
            |                "url": "https://www.apache.org/licenses/LICENSE-2.0"
            |            },
            |            {
            |                "identifier": "MIT",
            |                "name": "MIT License",
            |                "url": "https://opensource.org/licenses/MIT"
            |            }
            |        ],
            |        "unknownLicenses": [
            |            {
            |                "name": "Proprietary Addon",
            |                "url": "https://example.com/proprietary"
            |            },
            |            {
            |                "name": "Special Terms",
            |                "url": "https://example.com/special"
            |            }
            |        ],
            |        "scm": {
            |            "url": "https://github.com/example/multiple"
            |        }
            |    }
            |]
            """.trimMargin()

        val fileSpec = LicensesInfoGenerator.buildFileSpec(
            packageName = "com.example.licentia",
            artifactsJson = artifactsJson,
        )

        val expectedOutput =
            """
            |// AUTO-GENERATED FILE. DO NOT MODIFY.
            |package com.example.licentia
            |
            |import io.github.reactivecircus.licentia.runtime.LicensesInfo
            |import kotlin.collections.List
            |
            |public object AllLicensesInfo : LicensesInfo {
            |  override val artifacts: List<LicensesInfo.Artifact>
            |    get() = _artifacts
            |
            |  private val _artifacts: List<LicensesInfo.Artifact> = listOf(
            |        LicensesInfo.Artifact(
            |          groupId = "androidx.activity",
            |          artifactId = "activity",
            |          version = "1.12.1",
            |          name = "Activity",
            |          spdxLicenses = listOf(
            |            LicensesInfo.Artifact.SpdxLicense(
            |              identifier = "Apache-2.0",
            |              name = "Apache License 2.0",
            |              url = "https://www.apache.org/licenses/LICENSE-2.0",
            |            ),
            |          ),
            |          scm = LicensesInfo.Artifact.Scm(url = "https://cs.android.com/androidx/platform/frameworks/support"),
            |          unknownLicenses = null,
            |        ),
            |        LicensesInfo.Artifact(
            |          groupId = "org.jetbrains.kotlin",
            |          artifactId = "kotlin-stdlib",
            |          version = "2.1.0",
            |          name = null,
            |          spdxLicenses = listOf(
            |            LicensesInfo.Artifact.SpdxLicense(
            |              identifier = "Apache-2.0",
            |              name = "Apache License 2.0",
            |              url = "https://www.apache.org/licenses/LICENSE-2.0",
            |            ),
            |          ),
            |          scm = LicensesInfo.Artifact.Scm(url = "https://github.com/JetBrains/kotlin"),
            |          unknownLicenses = null,
            |        ),
            |        LicensesInfo.Artifact(
            |          groupId = "com.example",
            |          artifactId = "no-scm-library",
            |          version = "1.0.0",
            |          name = "No SCM Library",
            |          spdxLicenses = listOf(
            |            LicensesInfo.Artifact.SpdxLicense(
            |              identifier = "MIT",
            |              name = "MIT License",
            |              url = "https://opensource.org/licenses/MIT",
            |            ),
            |          ),
            |          scm = null,
            |          unknownLicenses = null,
            |        ),
            |        LicensesInfo.Artifact(
            |          groupId = "com.example",
            |          artifactId = "unknown-license-library",
            |          version = "2.0.0",
            |          name = "Unknown License Library",
            |          spdxLicenses = null,
            |          scm = LicensesInfo.Artifact.Scm(url = "https://github.com/example/unknown-license"),
            |          unknownLicenses = listOf(
            |            LicensesInfo.Artifact.UnknownLicense(
            |              name = "Custom License",
            |              url = "https://example.com/license",
            |            ),
            |          ),
            |        ),
            |        LicensesInfo.Artifact(
            |          groupId = "com.example",
            |          artifactId = "multiple-licenses",
            |          version = "3.0.0",
            |          name = "Multiple Licenses Library",
            |          spdxLicenses = listOf(
            |            LicensesInfo.Artifact.SpdxLicense(
            |              identifier = "Apache-2.0",
            |              name = "Apache License 2.0",
            |              url = "https://www.apache.org/licenses/LICENSE-2.0",
            |            ),
            |            LicensesInfo.Artifact.SpdxLicense(
            |              identifier = "MIT",
            |              name = "MIT License",
            |              url = "https://opensource.org/licenses/MIT",
            |            ),
            |          ),
            |          scm = LicensesInfo.Artifact.Scm(url = "https://github.com/example/multiple"),
            |          unknownLicenses = listOf(
            |            LicensesInfo.Artifact.UnknownLicense(
            |              name = "Proprietary Addon",
            |              url = "https://example.com/proprietary",
            |            ),
            |            LicensesInfo.Artifact.UnknownLicense(
            |              name = "Special Terms",
            |              url = "https://example.com/special",
            |            ),
            |          ),
            |        ),
            |      )
            |}
            |
            """.trimMargin()

        assertEquals("AllLicensesInfo", fileSpec.name)
        assertEquals(expectedOutput, fileSpec.toString())
    }
}
