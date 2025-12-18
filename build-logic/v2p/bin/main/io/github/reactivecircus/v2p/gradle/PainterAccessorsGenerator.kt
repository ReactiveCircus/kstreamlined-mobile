package io.github.reactivecircus.v2p.gradle

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName

internal object PainterAccessorsGenerator {
    fun buildFileSpec(
        packageName: String,
        objectName: String,
        configs: PerGroupCodegenConfigs,
        drawableFileNames: List<String>,
    ): FileSpec {
        val rClass = ClassName(packageName, "R")
        val drawableClass = rClass.nestedClass("drawable")
        val properties = drawableFileNames.map { fileName ->
            val resourceName = fileName.substringBeforeLast('.')
            val propertyName = resourceName
                .removePrefix(configs.prefix)
                .toPascalCase()
            PropertySpec.builder(propertyName, painterClass)
                .getter(
                    FunSpec.getterBuilder()
                        .addAnnotation(composableAnnotation)
                        .addStatement("return %M(%T.%L)", painterResource, drawableClass, resourceName)
                        .build(),
                )
                .build()
        }

        val objectBuilder = TypeSpec.objectBuilder(objectName)
            .addProperties(properties)

        if (configs.generateAsListFunction) {
            val asListFun = FunSpec.builder("asList")
                .addAnnotation(composableAnnotation)
                .returns(List::class.asClassName().parameterizedBy(painterClass))
                .addStatement(
                    "return listOf(⇥\n%L\n⇤)",
                    properties.joinToString(",\n", postfix = ",") { it.name },
                )
                .build()
            objectBuilder.addFunction(asListFun)
        }

        return FileSpec.builder(packageName, objectName)
            .addFileComment("AUTO-GENERATED FILE. DO NOT MODIFY.")
            .addType(objectBuilder.build())
            .build()
    }

    private fun String.toPascalCase(): String {
        return split('_')
            .filter { it.isNotEmpty() }
            .joinToString("") { it.replaceFirstChar { char -> char.uppercase() } }
    }

    val painterResource = MemberName("androidx.compose.ui.res", "painterResource")
    val painterClass = ClassName("androidx.compose.ui.graphics.painter", "Painter")
    val composableAnnotation = ClassName("androidx.compose.runtime", "Composable")
}
