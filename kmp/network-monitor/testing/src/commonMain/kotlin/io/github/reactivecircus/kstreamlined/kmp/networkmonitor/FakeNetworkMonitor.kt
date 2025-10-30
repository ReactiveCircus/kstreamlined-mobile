package io.github.reactivecircus.kstreamlined.kmp.networkmonitor

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

public class FakeNetworkMonitor : NetworkMonitor {
    private val _networkState = MutableStateFlow(NetworkState.Unknown)
    override val networkState: StateFlow<NetworkState> = _networkState

    public fun emitNetworkState(newState: NetworkState) {
        _networkState.value = newState
    }
}
