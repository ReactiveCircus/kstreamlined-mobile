package io.github.reactivecircus.kstreamlined.kmp.networkmonitor

import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class FakeNetworkMonitorTest {

    private val fakeNetworkMonitor = FakeNetworkMonitor()

    @Test
    fun `networkState emits when new value is set by emitNetworkState`() = runTest {
        fakeNetworkMonitor.networkState.test {
            assertEquals(NetworkState.Unknown, awaitItem())

            fakeNetworkMonitor.emitNetworkState(NetworkState.Connected)

            assertEquals(NetworkState.Connected, awaitItem())

            fakeNetworkMonitor.emitNetworkState(NetworkState.Disconnected)

            assertEquals(NetworkState.Disconnected, awaitItem())

            fakeNetworkMonitor.emitNetworkState(NetworkState.Connected)

            assertEquals(NetworkState.Connected, awaitItem())
        }
    }
}
