package io.github.reactivecircus.kstreamlined.kmp.appinfo

import kotlin.test.Test
import kotlin.test.assertEquals

class AppInfoTest {
    @Test
    fun `LicensesInfo can be created from JSON`() {
        val licensesInfo = LicensesInfo.fromJson {
            """
            |[
            |  {
            |    "groupId": "com.apollographql.apollo",
            |    "artifactId": "apollo-runtime",
            |    "version": "5.0.0-alpha.3",
            |    "name": "apollo-runtime",
            |    "spdxLicenses": [
            |      {
            |        "identifier": "MIT",
            |        "name": "MIT License",
            |        "url": "https://opensource.org/licenses/MIT"
            |      }
            |    ],
            |    "scm": {
            |      "url": "https://github.com/apollographql/apollo-kotlin/"
            |    }
            |  },
            |  {
            |    "groupId": "com.google.android.gms",
            |    "artifactId": "play-services-tasks",
            |    "version": "18.1.0",
            |    "name": "play-services-tasks",
            |    "unknownLicenses": [
            |      {
            |        "name": "Android Software Development Kit License",
            |        "url": "https://developer.android.com/studio/terms.html"
            |      }
            |    ]
            |  },  
            |  {
            |    "groupId": "com.google.firebase",
            |    "artifactId": "firebase-crashlytics",
            |    "version": "20.0.3",
            |    "spdxLicenses": [
            |      {
            |        "identifier": "Apache-2.0",
            |        "name": "Apache License 2.0",
            |        "url": "https://www.apache.org/licenses/LICENSE-2.0"
            |      }
            |    ],
            |    "scm": {
            |      "url": "https://github.com/firebase/firebase-android-sdk"
            |    }
            |  },
            |  {
            |    "groupId": "com.fleeksoft.ksoup",
            |    "artifactId": "ksoup",
            |    "version": "0.2.5",
            |    "unknownLicenses": [
            |      {
            |        "name": "MIT License",
            |        "url": "https://opensource.org/license/MIT"
            |      }
            |    ],
            |    "scm": {
            |      "url": "https://github.com/fleeksoft/ksoup"
            |    }
            |  }
            |]
            """.trimMargin()
        }

        assertEquals(
            listOf(
                LicensesInfo.Artifact(
                    groupId = "com.apollographql.apollo",
                    artifactId = "apollo-runtime",
                    version = "5.0.0-alpha.3",
                    name = "apollo-runtime",
                    spdxLicenses = listOf(
                        LicensesInfo.Artifact.SpdxLicense(
                            identifier = "MIT",
                            name = "MIT License",
                            url = "https://opensource.org/licenses/MIT",
                        ),
                    ),
                    scm = LicensesInfo.Artifact.Scm(
                        url = "https://github.com/apollographql/apollo-kotlin/",
                    ),
                    unknownLicenses = null,
                ),
                LicensesInfo.Artifact(
                    groupId = "com.google.android.gms",
                    artifactId = "play-services-tasks",
                    version = "18.1.0",
                    name = "play-services-tasks",
                    spdxLicenses = null,
                    scm = null,
                    unknownLicenses = listOf(
                        LicensesInfo.Artifact.UnknownLicense(
                            name = "Android Software Development Kit License",
                            url = "https://developer.android.com/studio/terms.html",
                        ),
                    ),
                ),
                LicensesInfo.Artifact(
                    groupId = "com.google.firebase",
                    artifactId = "firebase-crashlytics",
                    version = "20.0.3",
                    name = null,
                    spdxLicenses = listOf(
                        LicensesInfo.Artifact.SpdxLicense(
                            identifier = "Apache-2.0",
                            name = "Apache License 2.0",
                            url = "https://www.apache.org/licenses/LICENSE-2.0",
                        ),
                    ),
                    scm = LicensesInfo.Artifact.Scm(
                        url = "https://github.com/firebase/firebase-android-sdk",
                    ),
                    unknownLicenses = null,
                ),
                LicensesInfo.Artifact(
                    groupId = "com.fleeksoft.ksoup",
                    artifactId = "ksoup",
                    version = "0.2.5",
                    name = null,
                    spdxLicenses = null,
                    scm = LicensesInfo.Artifact.Scm(
                        url = "https://github.com/fleeksoft/ksoup",
                    ),
                    unknownLicenses = listOf(
                        LicensesInfo.Artifact.UnknownLicense(
                            name = "MIT License",
                            url = "https://opensource.org/license/MIT",
                        ),
                    ),
                ),
            ),
            licensesInfo.artifacts,
        )
    }

    @Test
    fun `LicensesInfo can be created from list of Artifacts`() {
        val expected = listOf(
            LicensesInfo.Artifact(
                groupId = "com.example",
                artifactId = "example-artifact-1",
                version = "1.0.0",
                name = "Example Artifact 1",
                spdxLicenses = listOf(
                    LicensesInfo.Artifact.SpdxLicense(
                        identifier = "MIT",
                        name = "MIT License",
                        url = "https://opensource.org/licenses/MIT",
                    ),
                ),
                scm = LicensesInfo.Artifact.Scm(url = "https://example.com/example-artifact"),
                unknownLicenses = null,
            ),
            LicensesInfo.Artifact(
                groupId = "com.example",
                artifactId = "example-artifact-2",
                version = "1.0.1",
                name = null,
                spdxLicenses = null,
                scm = null,
                unknownLicenses = null,
            ),
        )

        assertEquals(expected, LicensesInfo.from(expected).artifacts)
    }
}
