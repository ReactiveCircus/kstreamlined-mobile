package io.github.reactivecircus.kstreamlined.kmp.presentation.home

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class CombineWithMetadataTest {

    @Test
    fun `combineWithMetadata emits expected metadata`() =
        runTest {
            val flow1 = flow {
                delay(500)
                emit(1)
                delay(3_000)
                emit(2)
            }
            val flow2 = flow {
                emit("a")
                delay(1_000)
                emit("b")
            }
            val flow3 = flow {
                emit(true)
                delay(2_000)
                emit(false)
            }

            val combined = combineWithMetadata(flow1, flow2, flow3) { v1, v2, v3, metadata ->
                Triple(v1, v2, v3) to metadata
            }.toList()

            assertEquals(
                listOf(
                    Triple(1, "a", true) to TransformMetadata(isFirstTransform = true, lastEmittedFlowIndex = 0),
                    Triple(1, "b", true) to TransformMetadata(isFirstTransform = false, lastEmittedFlowIndex = 1),
                    Triple(1, "b", false) to TransformMetadata(isFirstTransform = false, lastEmittedFlowIndex = 2),
                    Triple(2, "b", false) to TransformMetadata(isFirstTransform = false, lastEmittedFlowIndex = 0),
                ),
                combined,
            )
        }
}
