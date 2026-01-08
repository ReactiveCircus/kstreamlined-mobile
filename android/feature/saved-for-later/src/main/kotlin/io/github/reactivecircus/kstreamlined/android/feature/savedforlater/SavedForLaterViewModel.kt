package io.github.reactivecircus.kstreamlined.android.feature.savedforlater

import androidx.compose.ui.platform.AndroidUiDispatcher
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.FeedDataSource
import io.github.reactivecircus.kstreamlined.kmp.presentation.savedforlater.SavedForLaterPresenter
import io.github.reactivecircus.kstreamlined.kmp.presentation.savedforlater.SavedForLaterUiEvent
import io.github.reactivecircus.kstreamlined.kmp.presentation.savedforlater.SavedForLaterUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

@Inject
@ViewModelKey(SavedForLaterViewModel::class)
@ContributesIntoMap(AppScope::class)
public class SavedForLaterViewModel(
    feedDataSource: FeedDataSource,
) : ViewModel() {
    private val presenter = SavedForLaterPresenter(
        feedDataSource = feedDataSource,
        scope = CoroutineScope(viewModelScope.coroutineContext + AndroidUiDispatcher.Main),
    )
    internal val uiState: StateFlow<SavedForLaterUiState> = presenter.states
    internal val eventSink: (SavedForLaterUiEvent) -> Unit = presenter.eventSink
}
