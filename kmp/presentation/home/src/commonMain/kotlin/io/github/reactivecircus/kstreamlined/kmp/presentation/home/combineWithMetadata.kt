package io.github.reactivecircus.kstreamlined.kmp.presentation.home

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlin.experimental.ExperimentalTypeInference

@OptIn(ExperimentalTypeInference::class)
internal fun <T1, T2, T3, R> combineWithMetadata(
    flow1: Flow<T1>,
    flow2: Flow<T2>,
    flow3: Flow<T3>,
    transform: suspend (T1, T2, T3, TransformMetadata) -> R,
): Flow<R> = flow {
    var isFirstTransform = true
    var lastEmittedFlowIndex = -1
    combine(
        flow1.onEach {
            lastEmittedFlowIndex = 0
        },
        flow2.onEach {
            lastEmittedFlowIndex = 1
        },
        flow3.onEach {
            lastEmittedFlowIndex = 2
        },
    ) { value1, value2, value3 ->
        transform(
            value1,
            value2,
            value3,
            TransformMetadata(isFirstTransform, lastEmittedFlowIndex),
        )
    }.collect {
        emit(it)
        isFirstTransform = false
    }
}

internal data class TransformMetadata(
    val isFirstTransform: Boolean,
    val lastEmittedFlowIndex: Int,
)
