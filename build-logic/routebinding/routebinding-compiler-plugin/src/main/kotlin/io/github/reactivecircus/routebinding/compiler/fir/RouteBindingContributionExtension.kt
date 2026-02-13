package io.github.reactivecircus.routebinding.compiler.fir

import dev.zacsweers.metro.compiler.MetroOptions
import dev.zacsweers.metro.compiler.api.fir.MetroContributionExtension
import dev.zacsweers.metro.compiler.api.fir.MetroContributions
import io.github.reactivecircus.routebinding.compiler.ClassIds
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.extensions.FirDeclarationPredicateRegistrar
import org.jetbrains.kotlin.fir.extensions.predicate.LookupPredicate
import org.jetbrains.kotlin.fir.extensions.predicateBasedProvider
import org.jetbrains.kotlin.fir.resolve.defaultType
import org.jetbrains.kotlin.fir.resolve.providers.symbolProvider
import org.jetbrains.kotlin.fir.symbols.impl.FirNamedFunctionSymbol
import org.jetbrains.kotlin.fir.symbols.impl.FirRegularClassSymbol
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.Name

internal class RouteBindingContributionExtension(
    private val session: FirSession,
) : MetroContributionExtension {
    private val hasRouteBindingAnnotation = LookupPredicate.BuilderContext.annotated(
        ClassIds.RouteBinding.Annotation.asSingleFqName(),
    )

    private val navEntryInstallerClassIds: List<ClassId> by lazy {
        session.predicateBasedProvider
            .getSymbolsByPredicate(hasRouteBindingAnnotation)
            .filterIsInstance<FirNamedFunctionSymbol>()
            .map { function ->
                // TODO move FirNamedFunctionSymbol -> ClassId to generate to RouteBindingFunctionToClassMapping
                val packageFqName = function.callableId.packageName
                val classNameSuffix = ClassIds.RouteBinding.NavEntryInstaller.shortClassName.asString()
                val className = Name.identifier("${function.name.asString()}_$classNameSuffix")
                ClassId(packageFqName, className)
            }
    }

    override fun FirDeclarationPredicateRegistrar.registerPredicates() {
        register(hasRouteBindingAnnotation)
    }

    override fun getContributions(scopeClassId: ClassId): List<MetroContributionExtension.Contribution> {
        if (scopeClassId != ClassIds.Metro.AppScope) return emptyList()
        return navEntryInstallerClassIds.mapNotNull { classId ->
            val metroContributionClassId = MetroContributions.metroContributionClassId(
                contributingClassId = classId,
                scopeClassId = scopeClassId,
            )

            val metroContributionSymbol = session.symbolProvider
                .getClassLikeSymbolByClassId(metroContributionClassId) as? FirRegularClassSymbol ?: return@mapNotNull null

            MetroContributionExtension.Contribution(
                supertype = metroContributionSymbol.defaultType(),
                replaces = emptyList(),
                originClassId = classId,
            )
        }
    }

    internal class Factory : MetroContributionExtension.Factory {
        override fun create(
            session: FirSession,
            options: MetroOptions,
        ): MetroContributionExtension = RouteBindingContributionExtension(session)
    }
}
