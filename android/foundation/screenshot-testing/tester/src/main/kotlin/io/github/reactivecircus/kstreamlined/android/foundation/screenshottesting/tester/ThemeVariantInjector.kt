package io.github.reactivecircus.kstreamlined.android.foundation.screenshottesting.tester

import org.junit.runner.Description
import org.junit.runner.Runner
import org.junit.runner.notification.RunNotifier
import org.junit.runners.BlockJUnit4ClassRunner
import org.junit.runners.model.FrameworkMethod
import org.junit.runners.model.InitializationError
import org.junit.runners.model.Statement
import java.lang.reflect.Field

/**
 * A test runner that runs snapshot tests with both Light and Dark theme variants.
 */
public class ThemeVariantInjector(private val testClass: Class<*>) : Runner() {
    private val delegate: BlockJUnit4ClassRunner

    init {
        try {
            delegate = object : BlockJUnit4ClassRunner(testClass) {
                override fun getChildren(): List<FrameworkMethod> {
                    val originalMethods = super.getChildren()
                    return originalMethods.flatMap { method ->
                        listOf(
                            ThemeVariantMethod(method, ThemeVariant.Light),
                            ThemeVariantMethod(method, ThemeVariant.Dark),
                        )
                    }
                }

                override fun describeChild(method: FrameworkMethod): Description {
                    return if (method is ThemeVariantMethod) {
                        Description.createTestDescription(
                            testClass.name,
                            "${method.delegate.name}[${method.variant.name}]",
                        )
                    } else {
                        super.describeChild(method)
                    }
                }

                override fun methodInvoker(method: FrameworkMethod, test: Any): Statement {
                    return if (method is ThemeVariantMethod) {
                        ThemeVariantStatement(
                            super.methodInvoker(method.delegate, test),
                            test,
                            method.variant,
                        )
                    } else {
                        super.methodInvoker(method, test)
                    }
                }
            }
        } catch (e: InitializationError) {
            @Suppress("TooGenericExceptionThrown")
            throw RuntimeException("Failed to initialize ThemeVariantInjector", e)
        }
    }

    override fun run(notifier: RunNotifier) {
        delegate.run(notifier)
    }

    override fun getDescription(): Description = delegate.description

    private class ThemeVariantMethod(
        val delegate: FrameworkMethod,
        val variant: ThemeVariant,
    ) : FrameworkMethod(delegate.method) {
        override fun getName(): String = "${delegate.name}[${variant.name}]"

        override fun validatePublicVoidNoArg(isStatic: Boolean, errors: MutableList<Throwable>) {
            delegate.validatePublicVoidNoArg(isStatic, errors)
        }
    }

    private class ThemeVariantStatement(
        private val delegate: Statement,
        private val testInstance: Any,
        private val variant: ThemeVariant,
    ) : Statement() {
        override fun evaluate() {
            val testerFields = findSnapshotTesterFields(testInstance)
            testerFields.forEach { field ->
                val tester = field.get(testInstance) as SnapshotTester
                tester.setCurrentThemeVariant(variant)
            }
            delegate.evaluate()
        }

        private fun findSnapshotTesterFields(testInstance: Any): List<Field> {
            return testInstance.javaClass.declaredFields
                .filter { SnapshotTester::class.java.isAssignableFrom(it.type) }
                .onEach { it.isAccessible = true }
        }
    }
}
