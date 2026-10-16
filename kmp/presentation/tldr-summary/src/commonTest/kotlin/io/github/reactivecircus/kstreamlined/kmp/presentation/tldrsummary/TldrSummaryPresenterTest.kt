package io.github.reactivecircus.kstreamlined.kmp.presentation.tldrsummary

import app.cash.turbine.test
import io.github.reactivecircus.kstreamlined.kmp.capsule.testing.asMoleculeContext
import io.github.reactivecircus.kstreamlined.kmp.remote.FakeContentService
import io.github.reactivecircus.kstreamlined.kmp.remote.FakeKotlinBlogTldr
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.TimeZone
import kotlin.test.Test
import kotlin.test.assertEquals

class TldrSummaryPresenterTest {
    private val contentService = FakeContentService()

    private val testDispatcher = StandardTestDispatcher()

    private val testScope = TestScope(testDispatcher)

    private val timeZone = TimeZone.UTC

    private val id = FakeKotlinBlogTldr.id

    private val expectedSummary = FakeKotlinBlogTldr.asPresentationModel(timeZone = timeZone)

    private fun presenter() = TldrSummaryPresenter(
        id = id,
        contentService = contentService,
        timeZone = timeZone,
        moleculeContext = testDispatcher.asMoleculeContext(),
    )

    @Test
    fun `presenter emits Content state when initialized and fetching TLDR succeeds`() =
        testScope.runTest {
            val requestedIds = mutableListOf<String>()
            contentService.nextKotlinBlogTldrResponse = {
                requestedIds += it
                FakeKotlinBlogTldr
            }
            presenter().states.test {
                assertEquals(TldrSummaryUiState.Loading, awaitItem())
                assertEquals(TldrSummaryUiState.Content(expectedSummary), awaitItem())
                assertEquals(listOf(id), requestedIds)
            }
        }

    @Test
    fun `presenter emits Error state when initialized and fetching TLDR fails`() =
        testScope.runTest {
            contentService.nextKotlinBlogTldrResponse = {
                error("Failed to fetch TLDR")
            }
            presenter().states.test {
                assertEquals(TldrSummaryUiState.Loading, awaitItem())
                assertEquals(TldrSummaryUiState.Error, awaitItem())
            }
        }

    @Test
    fun `presenter emits Content state when current state is Error and Retry event is dispatched`() =
        testScope.runTest {
            contentService.nextKotlinBlogTldrResponse = {
                error("Failed to fetch TLDR")
            }
            val presenter = presenter()
            presenter.states.test {
                assertEquals(TldrSummaryUiState.Loading, awaitItem())
                assertEquals(TldrSummaryUiState.Error, awaitItem())

                contentService.nextKotlinBlogTldrResponse = { FakeKotlinBlogTldr }

                presenter.eventSink(TldrSummaryUiEvent.Retry)

                assertEquals(TldrSummaryUiState.Loading, awaitItem())
                assertEquals(TldrSummaryUiState.Content(expectedSummary), awaitItem())
            }
        }
}
