package io.github.reactivecircus.kstreamlined.kmp.presentation.tldrsummary

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import dev.zacsweers.metro.ContributesIntoMap
import io.github.reactivecircus.kstreamlined.kmp.capsule.runtime.MoleculeContext
import io.github.reactivecircus.kstreamlined.kmp.capsule.runtime.Presenter
import io.github.reactivecircus.kstreamlined.kmp.capsule.runtime.PresenterAssistedFactory
import io.github.reactivecircus.kstreamlined.kmp.capsule.runtime.PresenterAssistedFactoryKey
import io.github.reactivecircus.kstreamlined.kmp.remote.ContentService
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.datetime.TimeZone
import kotlin.coroutines.cancellation.CancellationException

@AssistedInject
public class TldrSummaryPresenter(
    @Assisted private val id: String,
    private val contentService: ContentService,
    private val timeZone: TimeZone = TimeZone.currentSystemDefault(),
    moleculeContext: MoleculeContext,
) : Presenter<TldrSummaryUiEvent, TldrSummaryUiState>(moleculeContext) {
    @Composable
    override fun present(): TldrSummaryUiState {
        var uiState by remember { mutableStateOf<TldrSummaryUiState>(TldrSummaryUiState.Loading) }
        var loadCount by remember { mutableIntStateOf(0) }
        LaunchedEffect(loadCount) {
            uiState = runCatching {
                val tldr = contentService.fetchKotlinBlogTldr(id)
                TldrSummaryUiState.Content(
                    summary = context(timeZone) { tldr.asPresentationModel() },
                )
            }.getOrElse {
                if (it is CancellationException) currentCoroutineContext().ensureActive()
                TldrSummaryUiState.Error
            }
        }
        CollectEvent { event ->
            when (event) {
                is TldrSummaryUiEvent.Retry -> {
                    loadCount++
                    uiState = TldrSummaryUiState.Loading
                }
            }
        }
        return uiState
    }

    @AssistedFactory
    @PresenterAssistedFactoryKey
    @ContributesIntoMap(AppScope::class)
    public fun interface Factory : PresenterAssistedFactory {
        public fun create(id: String): TldrSummaryPresenter
    }
}
