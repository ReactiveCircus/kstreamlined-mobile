package io.github.reactivecircus.kstreamlined.kmp.networkmonitor

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.stateIn
import platform.Network.nw_interface_type_wifi
import platform.Network.nw_path_get_status
import platform.Network.nw_path_monitor_cancel
import platform.Network.nw_path_monitor_create_with_type
import platform.Network.nw_path_monitor_set_queue
import platform.Network.nw_path_monitor_set_update_handler
import platform.Network.nw_path_monitor_start
import platform.Network.nw_path_status_satisfied
import platform.darwin.dispatch_queue_create

public class AppleNetworkMonitor(
    coroutineScope: CoroutineScope,
) : NetworkMonitor {
    override val networkState: StateFlow<NetworkState> = callbackFlow {
        val monitor = nw_path_monitor_create_with_type(nw_interface_type_wifi)
        nw_path_monitor_set_update_handler(monitor) {
            trySend(
                if (nw_path_get_status(it) == nw_path_status_satisfied) {
                    NetworkState.Connected
                } else {
                    NetworkState.Disconnected
                },
            )
        }
        nw_path_monitor_set_queue(monitor, dispatch_queue_create("NWPathMonitor", null))
        nw_path_monitor_start(monitor)
        awaitClose {
            nw_path_monitor_cancel(monitor)
        }
    }.stateIn(coroutineScope, started = SharingStarted.Lazily, NetworkState.Unknown)
}
