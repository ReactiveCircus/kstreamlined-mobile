package io.github.reactivecircus.licentia.gradle

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.withIndent
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

internal object LicensesInfoGenerator {
    fun buildAllLicensesInfoFileSpec(
        packageName: String,
        artifactsJson: String,
    ): FileSpec {
        val allLicensesInfo = TypeSpec.objectBuilder("AllLicensesInfo")
            .addSuperinterface(LicensesInfo)
            .addProperty(
                PropertySpec.builder("artifacts", ArtifactListType)
                    .addModifiers(KModifier.OVERRIDE)
                    .getter(
                        FunSpec.getterBuilder()
                            .addStatement("return %L", "_artifacts")
                            .build(),
                    )
                    .build(),
            )
            .addProperty(
                PropertySpec.builder("_artifacts", ArtifactListType)
                    .addModifiers(KModifier.PRIVATE)
                    .initializer(buildArtifactsListInitializer(artifactsJson))
                    .build(),
            )
            .build()

        return FileSpec.builder(packageName, "AllLicensesInfo")
            .addFileComment("AUTO-GENERATED FILE. DO NOT MODIFY.")
            .addType(allLicensesInfo)
            .build()
    }

    private fun buildArtifactsListInitializer(artifactsJson: String): CodeBlock {
        return CodeBlock.builder()
            .add("listOf(\n")
            .withIndent {
                Json.parseToJsonElement(artifactsJson).jsonArray.forEach {
                    val groupId = it.jsonObject.getValue("groupId")
                    val artifactId = it.jsonObject.getValue("artifactId")
                    val version = it.jsonObject.getValue("version")
                    val name = it.jsonObject["name"]
                    add("%T(\n", ArtifactType)
                    withIndent {
                        add("groupId = %S,\n", groupId.jsonPrimitive.content)
                        add("artifactId = %S,\n", artifactId.jsonPrimitive.content)
                        add("version = %S,\n", version.jsonPrimitive.content)
                        add("name = %S,\n", name?.jsonPrimitive?.content)
                        val spdxLicenses = it.jsonObject["spdxLicenses"]?.jsonArray
                        if (spdxLicenses != null) {
                            add("spdxLicenses = listOf(\n")
                            withIndent {
                                spdxLicenses.forEach { license ->
                                    val identifier = license.jsonObject.getValue("identifier")
                                    val name = license.jsonObject.getValue("name")
                                    val url = license.jsonObject.getValue("url")
                                    add("%T(\n", SpdxLicenseType)
                                    withIndent {
                                        add("identifier = %S,\n", identifier.jsonPrimitive.content)
                                        add("name = %S,\n", name.jsonPrimitive.content)
                                        add("url = %S,\n", url.jsonPrimitive.content)
                                    }
                                    add("),\n")
                                }
                            }
                            add("),\n")
                        } else {
                            add("spdxLicenses = null,\n")
                        }
                        val scm = it.jsonObject["scm"]?.jsonObject
                        if (scm != null) {
                            val scmUrl = scm.getValue("url")
                            add("scm = %T(url = %S),\n", ScmType, scmUrl.jsonPrimitive.content)
                        } else {
                            add("scm = null,\n")
                        }
                        val unknownLicenses = it.jsonObject["unknownLicenses"]?.jsonArray
                        if (unknownLicenses != null) {
                            add("unknownLicenses = listOf(\n")
                            withIndent {
                                unknownLicenses.forEach { license ->
                                    val name = license.jsonObject.getValue("name")
                                    val url = license.jsonObject.getValue("url")
                                    add("%T(\n", UnknownLicenseType)
                                    withIndent {
                                        add("name = %S,\n", name.jsonPrimitive.content)
                                        add("url = %S,\n", url.jsonPrimitive.content)
                                    }
                                    add("),\n")
                                }
                            }
                            add("),\n")
                        } else {
                            add("unknownLicenses = null,\n")
                        }
                    }
                    add("),\n")
                }
            }
            .add(")")
            .build()
    }

    private val LicensesInfo = ClassName.bestGuess("io.github.reactivecircus.licentia.runtime.LicensesInfo")
    private val ArtifactType = LicensesInfo.nestedClass("Artifact")
    private val ArtifactListType = List::class.asClassName().parameterizedBy(ArtifactType)
    private val SpdxLicenseType = ArtifactType.nestedClass("SpdxLicense")
    private val ScmType = ArtifactType.nestedClass("Scm")
    private val UnknownLicenseType = ArtifactType.nestedClass("UnknownLicense")
}
