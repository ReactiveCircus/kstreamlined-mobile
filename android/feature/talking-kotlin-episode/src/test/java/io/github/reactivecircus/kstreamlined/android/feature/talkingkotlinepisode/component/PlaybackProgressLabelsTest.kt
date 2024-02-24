package io.github.reactivecircus.kstreamlined.android.feature.talkingkotlinepisode.component

import org.junit.Test
import kotlin.test.assertEquals

class PlaybackProgressLabelsTest {

    @Test
    fun `test playback progress labels`() {
        assertEquals("00:00:00" to "-00:00:00", playbackProgressLabels(0, 0))
        assertEquals("00:00:00" to "-00:00:00", playbackProgressLabels(1000, 0))
        assertEquals("00:00:00" to "-00:00:00", playbackProgressLabels(-1000, 1000))
        assertEquals("00:00:00" to "-00:00:00", playbackProgressLabels(0, -1000))
        assertEquals("00:00:00" to "-00:00:01", playbackProgressLabels(0, 1000))
        assertEquals("00:00:01" to "-00:00:00", playbackProgressLabels(1000, 1000))
        assertEquals("00:00:01" to "-00:00:01", playbackProgressLabels(1000, 2000))
        assertEquals("00:00:59" to "-00:00:01", playbackProgressLabels(59000, 60000))
        assertEquals("00:01:00" to "-00:00:00", playbackProgressLabels(60000, 60000))
        assertEquals("00:01:00" to "-00:00:01", playbackProgressLabels(60000, 61000))
        assertEquals("00:59:59" to "-00:00:01", playbackProgressLabels(3599000, 3600000))
        assertEquals("01:03:24" to "-00:06:36", playbackProgressLabels(3804000, 4200000))
        assertEquals("00:06:36" to "-01:03:24", playbackProgressLabels(396000, 4200000))
    }
}
