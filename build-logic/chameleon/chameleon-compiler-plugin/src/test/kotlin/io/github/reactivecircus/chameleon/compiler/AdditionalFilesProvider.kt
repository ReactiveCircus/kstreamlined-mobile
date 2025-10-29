package io.github.reactivecircus.chameleon.compiler

import org.jetbrains.kotlin.test.directives.model.RegisteredDirectives
import org.jetbrains.kotlin.test.model.TestFile
import org.jetbrains.kotlin.test.model.TestModule
import org.jetbrains.kotlin.test.services.AdditionalSourceProvider
import org.jetbrains.kotlin.test.services.TestModuleStructure
import org.jetbrains.kotlin.test.services.TestServices
import java.io.File

class AdditionalFilesProvider(testServices: TestServices) : AdditionalSourceProvider(testServices) {
    private val filesPath = File("$TestDataRoot/additional-files")

    override fun produceAdditionalFiles(
        globalDirectives: RegisteredDirectives,
        module: TestModule,
        testModuleStructure: TestModuleStructure
    ): List<TestFile> = filesPath
        .walkTopDown()
        .filter {
            it.isFile && it.extension == "kt"
        }
        .map { it.toTestFile() }
        .toList()
}

private const val TestDataRoot = "chameleon/chameleon-compiler-plugin/src/test/data"
