package io.github.reactivecircus.kstreamlined.android.feature.savedforlater

import androidx.compose.ui.platform.AndroidUiDispatcher
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.FeedDataSource
import io.github.reactivecircus.kstreamlined.kmp.presentation.savedforlater.SavedForLaterPresenter
import kotlinx.coroutines.CoroutineScope

@ViewModelKey(SavedForLaterViewModel::class)
@ContributesIntoMap(AppScope::class)
public class SavedForLaterViewModel(
    feedDataSource: FeedDataSource,
) : ViewModel() {
    internal val presenter = SavedForLaterPresenter(
        feedDataSource = feedDataSource,
        scope = CoroutineScope(viewModelScope.coroutineContext + AndroidUiDispatcher.Main),
    )
}
