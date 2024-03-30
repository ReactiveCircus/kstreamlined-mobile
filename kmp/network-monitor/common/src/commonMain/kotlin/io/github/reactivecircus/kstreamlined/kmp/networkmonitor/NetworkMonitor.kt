package io.github.reactivecircus.kstreamlined.kmp.networkmonitor

import kotlinx.coroutines.flow.StateFlow

public interface NetworkMonitor {
    public val networkState: StateFlow<NetworkState>
}

public enum class NetworkState {
    Unknown,
    Connected,
    Disconnected,
}
