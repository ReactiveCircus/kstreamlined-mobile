package io.github.reactivecircus.routebinding.compiler

import org.jetbrains.kotlin.generators.dsl.junit5.generateTestGroupSuiteWithJUnit5

fun main() {
    generateTestGroupSuiteWithJUnit5 {
        testGroup(
            testDataRoot = "routebinding/routebinding-compiler-plugin/src/test/data",
            testsRoot = "routebinding/routebinding-compiler-plugin/src/test/java",
        ) {
            testClass<AbstractFirDiagnosticTest> { model("diagnostic/fir") }
            testClass<AbstractIrDumpTest> { model("dump/ir") }
            testClass<AbstractIrDiagnosticTest> { model("diagnostic/ir") }
        }
    }
}
