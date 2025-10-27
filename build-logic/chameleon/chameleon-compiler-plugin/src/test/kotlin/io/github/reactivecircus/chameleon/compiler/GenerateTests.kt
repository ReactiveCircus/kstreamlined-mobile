package io.github.reactivecircus.chameleon.compiler

import org.jetbrains.kotlin.generators.generateTestGroupSuiteWithJUnit5

fun main() {
    generateTestGroupSuiteWithJUnit5 {
        testGroup(
            testDataRoot = "chameleon/chameleon-compiler-plugin/src/test/data",
            testsRoot = "chameleon/chameleon-compiler-plugin/src/test/java",
        ) {
            testClass<AbstractIrDumpTest> { model("ir") }
        }
    }
}
