package io.github.reactivecircus.kstreamlined.android.feature.tldrsummary.impl

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import kotlin.test.Test
import kotlin.test.assertEquals

class ReplaceSingleTopTest {
    @Test
    fun `current is replaced with destination when entry beneath is not destination`() {
        val backStack = NavBackStack(Root, Current)

        backStack.replaceSingleTop(current = Current, destination = Destination("1"))

        assertEquals(listOf(Root, Destination("1")), backStack)
    }

    @Test
    fun `current is popped when entry beneath is destination`() {
        val backStack = NavBackStack(Root, Destination("1"), Current)

        backStack.replaceSingleTop(current = Current, destination = Destination("1"))

        assertEquals(listOf(Root, Destination("1")), backStack)
    }

    @Test
    fun `current is replaced with destination when entry beneath is a different destination`() {
        val backStack = NavBackStack(Root, Destination("2"), Current)

        backStack.replaceSingleTop(current = Current, destination = Destination("1"))

        assertEquals(listOf(Root, Destination("2"), Destination("1")), backStack)
    }

    @Test
    fun `back stack is unchanged when current is no longer on the back stack`() {
        val backStack = NavBackStack(Root, Destination("1"))

        backStack.replaceSingleTop(current = Current, destination = Destination("1"))

        assertEquals(listOf(Root, Destination("1")), backStack)
    }

    private data object Root : NavKey

    private data object Current : NavKey

    private data class Destination(val id: String) : NavKey
}
