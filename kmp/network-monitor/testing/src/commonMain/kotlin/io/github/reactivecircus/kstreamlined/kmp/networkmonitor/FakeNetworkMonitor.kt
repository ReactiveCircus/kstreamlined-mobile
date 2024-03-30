package io.github.reactivecircus.kstreamlined.kmp.networkmonitor

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

public class FakeNetworkMonitor : NetworkMonitor {

    private val _networkState = MutableStateFlow(NetworkState.Unknown)

    public fun emitNetworkState(networkState: NetworkState) {
        _networkState.value = networkState
    }

    override val networkState: StateFlow<NetworkState> = _networkState
}
