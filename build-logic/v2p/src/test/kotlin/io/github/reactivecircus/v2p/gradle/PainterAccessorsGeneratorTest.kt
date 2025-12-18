package io.github.reactivecircus.v2p.gradle

import kotlin.test.Test
import kotlin.test.assertEquals

class PainterAccessorsGeneratorTest {
    @Test
    fun `PainterAccessorsGenerator generates expected Compose Painter accessors`() {
        val configs = PerGroupCodegenConfigs(
            prefix = "ic_",
            generateAsListFunction = false,
        )
        val drawableFileNames = listOf(
            "ic_arrow_down.xml",
            "ic_arrow_right.xml",
            "ic_kotlin.xml",
        )
        val fileSpec = PainterAccessorsGenerator.buildFileSpec(
            packageName = "com.example",
            containerName = "KSIcons",
            configs = configs,
            drawableFileNames = drawableFileNames,
        )

        val expectedOutput = """
            |// AUTO-GENERATED FILE. DO NOT MODIFY.
            |package com.example
            |
            |import androidx.compose.runtime.Composable
            |import androidx.compose.ui.graphics.painter.Painter
            |import androidx.compose.ui.res.painterResource
            |
            |public object KSIcons {
            |  public val ArrowDown: Painter
            |    @Composable
            |    get() = painterResource(R.drawable.ic_arrow_down)
            |
            |  public val ArrowRight: Painter
            |    @Composable
            |    get() = painterResource(R.drawable.ic_arrow_right)
            |
            |  public val Kotlin: Painter
            |    @Composable
            |    get() = painterResource(R.drawable.ic_kotlin)
            |}
            |
        """.trimMargin()

        assertEquals("KSIcons", fileSpec.name)
        assertEquals(expectedOutput, fileSpec.toString())
    }

    @Test
    fun `PainterAccessorsGenerator generates expected asList() function`() {
        val configs = PerGroupCodegenConfigs(
            prefix = "ic_",
            generateAsListFunction = true,
        )
        val drawableFileNames = listOf(
            "ic_arrow_down.xml",
            "ic_arrow_right.xml",
            "ic_kotlin.xml",
        )
        val fileSpec = PainterAccessorsGenerator.buildFileSpec(
            packageName = "com.example",
            containerName = "KSIcons",
            configs = configs,
            drawableFileNames = drawableFileNames,
        )

        val expectedOutput = """
            |// AUTO-GENERATED FILE. DO NOT MODIFY.
            |package com.example
            |
            |import androidx.compose.runtime.Composable
            |import androidx.compose.ui.graphics.painter.Painter
            |import androidx.compose.ui.res.painterResource
            |import kotlin.collections.List
            |
            |public object KSIcons {
            |  public val ArrowDown: Painter
            |    @Composable
            |    get() = painterResource(R.drawable.ic_arrow_down)
            |
            |  public val ArrowRight: Painter
            |    @Composable
            |    get() = painterResource(R.drawable.ic_arrow_right)
            |
            |  public val Kotlin: Painter
            |    @Composable
            |    get() = painterResource(R.drawable.ic_kotlin)
            |
            |  @Composable
            |  public fun asList(): List<Painter> = listOf(
            |    ArrowDown,
            |    ArrowRight,
            |    Kotlin,
            |  )
            |}
            |
        """.trimMargin()

        assertEquals(expectedOutput, fileSpec.toString())
    }
}
