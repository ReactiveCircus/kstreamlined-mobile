package io.github.reactivecircus.kstreamlined.android.feature.licenses

import app.cash.molecule.RecompositionMode
import app.cash.turbine.test
import io.github.reactivecircus.kstreamlined.kmp.appinfo.LicensesInfo
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class LicensesPresenterTest {
    private val testDispatcher = StandardTestDispatcher()

    private val testScope = TestScope(testDispatcher)

    private val presenter = LicensesPresenter(
        licensesInfo = LicensesInfo.from(
            listOf(
                LicensesInfo.Artifact(
                    name = "name",
                    artifactId = "artifact-id",
                    groupId = "group-id",
                    version = "version",
                    scm = LicensesInfo.Artifact.Scm(url = "url"),
                    spdxLicenses = null,
                    unknownLicenses = null,
                ),
            ),
        ),
        scope = testScope.backgroundScope,
        recompositionMode = RecompositionMode.Immediate,
    )

    @Test
    fun `presenter emits Loading state followed by Content state with artifact license items when initialized`() =
        testScope.runTest {
            presenter.states.test {
                assertEquals(LicensesUiState.Loading, awaitItem())

                assertEquals(
                    LicensesUiState.Content(
                        items = listOf(
                            ArtifactLicenseItem(
                                title = "name",
                                description = "group-id:artifact-id",
                                version = "version",
                                scmUrl = "url",
                                licenses = emptyList(),
                            ),
                        ),
                    ),
                    awaitItem(),
                )
            }
        }
}
