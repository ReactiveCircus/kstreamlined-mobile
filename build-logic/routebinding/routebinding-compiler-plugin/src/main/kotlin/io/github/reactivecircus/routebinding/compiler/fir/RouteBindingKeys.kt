package io.github.reactivecircus.routebinding.compiler.fir

import org.jetbrains.kotlin.GeneratedDeclarationKey

internal object RouteBindingKeys {
    data object Default : GeneratedDeclarationKey()

    data object NavEntryInstallerClassDeclaration : GeneratedDeclarationKey()

    data object InstallFunctionDeclaration : GeneratedDeclarationKey()

    data object BindingMirrorClassDeclaration : GeneratedDeclarationKey()

    data object ContributionHint : GeneratedDeclarationKey()

    val MetroContributionClassDeclaration: GeneratedDeclarationKey by lazy {
        getMetroKeyOrFallback("MetroContributionClassDeclaration", MetroFallbacks.MetroContributionClassDeclaration)
    }

    val MetroFactoryClassDeclaration: GeneratedDeclarationKey by lazy {
        getMetroKeyOrFallback("InjectConstructorFactoryClassDeclaration", MetroFallbacks.MetroFactoryClassDeclaration)
    }

    val MetroFactoryCreateFunction: GeneratedDeclarationKey by lazy {
        getMetroKeyOrFallback("FactoryCreateFunction", MetroFallbacks.MetroFactoryCreateFunction)
    }

    val MetroFactoryNewInstanceFunction: GeneratedDeclarationKey by lazy {
        getMetroKeyOrFallback("FactoryNewInstanceFunction", MetroFallbacks.MetroFactoryNewInstanceFunction)
    }

    object MetroFallbacks {
        data object MetroContributionClassDeclaration : GeneratedDeclarationKey()

        data object MetroFactoryClassDeclaration : GeneratedDeclarationKey()

        data object MetroFactoryCreateFunction : GeneratedDeclarationKey()

        data object MetroFactoryNewInstanceFunction : GeneratedDeclarationKey()
    }
}

/**
 * Gets a Metro key via reflection, or returns the fallback key if Metro compiler is not on the classpath.
 */
private fun getMetroKeyOrFallback(keyName: String, fallback: GeneratedDeclarationKey): GeneratedDeclarationKey {
    return runCatching {
        val nestedClass = Class.forName($$"dev.zacsweers.metro.compiler.fir.Keys$$$keyName")
        val instanceField = nestedClass.getDeclaredField("INSTANCE")
        instanceField.isAccessible = true
        instanceField.get(null) as GeneratedDeclarationKey
    }.getOrDefault(fallback)
}
