package io.github.reactivecircus.kstreamlined.kmp.networkmonitor

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

public class FakeNetworkMonitor : NetworkMonitor {
    override val networkState: StateFlow<NetworkState>
        field = MutableStateFlow(NetworkState.Unknown)

    public fun emitNetworkState(newState: NetworkState) {
        networkState.value = newState
    }
}
