package io.github.reactivecircus.routebinding.compiler.fir

import dev.zacsweers.metro.compiler.MetroOptions
import dev.zacsweers.metro.compiler.api.fir.MetroFirDeclarationGenerationExtension
import io.github.reactivecircus.routebinding.compiler.ClassIds
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.declarations.declaredFunctions
import org.jetbrains.kotlin.fir.declarations.getDeprecationsProvider
import org.jetbrains.kotlin.fir.deserialization.toQualifiedPropertyAccessExpression
import org.jetbrains.kotlin.fir.expressions.FirAnnotation
import org.jetbrains.kotlin.fir.expressions.builder.buildAnnotation
import org.jetbrains.kotlin.fir.expressions.builder.buildAnnotationArgumentMapping
import org.jetbrains.kotlin.fir.expressions.builder.buildArgumentList
import org.jetbrains.kotlin.fir.expressions.builder.buildEnumEntryDeserializedAccessExpression
import org.jetbrains.kotlin.fir.expressions.builder.buildGetClassCall
import org.jetbrains.kotlin.fir.expressions.builder.buildLiteralExpression
import org.jetbrains.kotlin.fir.expressions.builder.buildResolvedQualifier
import org.jetbrains.kotlin.fir.extensions.ExperimentalTopLevelDeclarationsGenerationApi
import org.jetbrains.kotlin.fir.extensions.FirDeclarationPredicateRegistrar
import org.jetbrains.kotlin.fir.extensions.MemberGenerationContext
import org.jetbrains.kotlin.fir.extensions.predicate.LookupPredicate
import org.jetbrains.kotlin.fir.extensions.predicateBasedProvider
import org.jetbrains.kotlin.fir.plugin.createConstructor
import org.jetbrains.kotlin.fir.plugin.createMemberFunction
import org.jetbrains.kotlin.fir.plugin.createTopLevelClass
import org.jetbrains.kotlin.fir.resolve.defaultType
import org.jetbrains.kotlin.fir.resolve.providers.symbolProvider
import org.jetbrains.kotlin.fir.symbols.impl.FirClassLikeSymbol
import org.jetbrains.kotlin.fir.symbols.impl.FirClassSymbol
import org.jetbrains.kotlin.fir.symbols.impl.FirConstructorSymbol
import org.jetbrains.kotlin.fir.symbols.impl.FirNamedFunctionSymbol
import org.jetbrains.kotlin.fir.symbols.impl.FirRegularClassSymbol
import org.jetbrains.kotlin.fir.toFirResolvedTypeRef
import org.jetbrains.kotlin.fir.types.builder.buildResolvedTypeRef
import org.jetbrains.kotlin.fir.types.constructClassLikeType
import org.jetbrains.kotlin.name.CallableId
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.name.SpecialNames
import org.jetbrains.kotlin.name.StandardClassIds
import org.jetbrains.kotlin.types.ConstantValueKind

internal class RouteBindingDeclarationGenerationExtension(
    session: FirSession,
) : MetroFirDeclarationGenerationExtension(session) {
    private val hasRouteBindingAnnotation = LookupPredicate.BuilderContext.annotated(
        ClassIds.RouteBinding.Annotation.asSingleFqName(),
    )

    private val sourceFunctions: List<FirNamedFunctionSymbol> by lazy {
        session.predicateBasedProvider
            .getSymbolsByPredicate(hasRouteBindingAnnotation)
            .filterIsInstance<FirNamedFunctionSymbol>()
    }

    private val navEntryInstallerClassIds: List<ClassId> by lazy {
        sourceFunctions.map { function ->
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

    override fun getContributionHints(): List<ContributionHint> {
        return navEntryInstallerClassIds.map { classId ->
            ContributionHint(
                contributingClassId = classId,
                scope = ClassIds.Metro.AppScope,
            )
        }
    }

    @ExperimentalTopLevelDeclarationsGenerationApi
    override fun getTopLevelClassIds(): Set<ClassId> = navEntryInstallerClassIds.toSet()

    @ExperimentalTopLevelDeclarationsGenerationApi
    override fun generateTopLevelClassLikeDeclaration(classId: ClassId): FirClassLikeSymbol<*> {
        return createTopLevelClass(
            classId = classId,
            key = RouteBindingKeys.NavEntryInstallerClassDeclaration,
        ) {
            val navEntryInstallerSymbol = session.symbolProvider
                .getClassLikeSymbolByClassId(ClassIds.RouteBinding.NavEntryInstaller) as FirRegularClassSymbol
            superType(navEntryInstallerSymbol.defaultType().toFirResolvedTypeRef().coneType)
        }.apply {
            replaceAnnotations(annotations + buildDeprecatedAnnotation() + buildContributesIntoSetAnnotation())
            replaceDeprecationsProvider(getDeprecationsProvider(session))
        }.symbol
    }

    private fun buildContributesIntoSetAnnotation(): FirAnnotation = buildAnnotation {
        val contributesIntoSetSymbol = session.symbolProvider
            .getClassLikeSymbolByClassId(ClassIds.Metro.ContributesIntoSet) as FirRegularClassSymbol
        annotationTypeRef = buildResolvedTypeRef {
            coneType = contributesIntoSetSymbol.defaultType().toFirResolvedTypeRef().coneType
        }
        argumentMapping = buildAnnotationArgumentMapping {
            mapping[Name.identifier("scope")] = buildGetClassCall {
                val appScopeSymbol = session.symbolProvider
                    .getClassLikeSymbolByClassId(ClassIds.Metro.AppScope) as FirRegularClassSymbol
                val appScopeConeType = appScopeSymbol.classId.constructClassLikeType()
                val kClassSymbol = session.symbolProvider
                    .getClassLikeSymbolByClassId(StandardClassIds.KClass) as FirRegularClassSymbol
                val kClassType = kClassSymbol.classId.constructClassLikeType(arrayOf(appScopeConeType))
                coneTypeOrNull = kClassType
                argumentList = buildArgumentList {
                    arguments += buildResolvedQualifier {
                        coneTypeOrNull = appScopeConeType
                        symbol = appScopeSymbol
                        packageFqName = ClassIds.Metro.AppScope.packageFqName
                        relativeClassFqName = ClassIds.Metro.AppScope.relativeClassName
                        resolvedToCompanionObject = false
                        isFullyQualified = true
                    }
                }
            }
        }
    }

    override fun getCallableNamesForClass(classSymbol: FirClassSymbol<*>, context: MemberGenerationContext): Set<Name> {
        val classSymbol = context.owner
        if (navEntryInstallerClassIds.none { it == classSymbol.classId }) return emptySet()
        return setOf(SpecialNames.INIT, Name.identifier("install"))
    }

    override fun generateConstructors(context: MemberGenerationContext): List<FirConstructorSymbol> {
        val classSymbol = context.owner
        if (navEntryInstallerClassIds.none { it == classSymbol.classId }) return emptyList()
        return listOf(
            createConstructor(
                owner = classSymbol,
                key = RouteBindingKeys.NavEntryInstallerClassDeclaration,
                isPrimary = true,
                generateDelegatedNoArgConstructorCall = true,
            ).symbol,
        )
    }

    override fun generateFunctions(
        callableId: CallableId,
        context: MemberGenerationContext?,
    ): List<FirNamedFunctionSymbol> {
        val classSymbol = context?.owner ?: return emptyList()
        if (navEntryInstallerClassIds.none { it == classSymbol.classId }) return emptyList()
        if (callableId.callableName.asString() != "install") return emptyList()
        return listOf(generateInstallFunction(classSymbol))
    }

    private fun generateInstallFunction(classSymbol: FirClassSymbol<*>): FirNamedFunctionSymbol {
        val navEntryInstallerSymbol = session.symbolProvider
            .getClassLikeSymbolByClassId(ClassIds.RouteBinding.NavEntryInstaller) as FirRegularClassSymbol
        val installFunctionSymbol = navEntryInstallerSymbol.declaredFunctions(session)
            .first { it.name == Name.identifier("install") }

        return createMemberFunction(
            owner = classSymbol,
            key = RouteBindingKeys.InstallFunctionDeclaration,
            name = Name.identifier("install"),
            returnType = installFunctionSymbol.resolvedReturnType,
        ) {
            installFunctionSymbol.contextParameterSymbols.forEach {
                contextReceiver(it.resolvedReturnType)
            }
            installFunctionSymbol.valueParameterSymbols.forEach {
                valueParameter(it.name, it.resolvedReturnType)
            }
            status {
                isOverride = true
            }
        }.symbol
    }

    private fun buildDeprecatedAnnotation(): FirAnnotation = buildAnnotation {
        val deprecatedAnnotation = session.symbolProvider
            .getClassLikeSymbolByClassId(StandardClassIds.Annotations.Deprecated) as FirRegularClassSymbol

        annotationTypeRef = deprecatedAnnotation.defaultType().toFirResolvedTypeRef()
        argumentMapping = buildAnnotationArgumentMapping {
            mapping[Name.identifier("message")] = buildLiteralExpression(
                source = null,
                kind = ConstantValueKind.String,
                value = "This synthesized declaration should not be used directly",
                setType = true,
            )
            mapping[Name.identifier("level")] =
                buildEnumEntryDeserializedAccessExpression {
                    enumClassId = StandardClassIds.DeprecationLevel
                    enumEntryName = Name.identifier("HIDDEN")
                }.toQualifiedPropertyAccessExpression(session)
        }
    }

    internal class Factory : MetroFirDeclarationGenerationExtension.Factory {
        override fun create(
            session: FirSession,
            options: MetroOptions,
        ): MetroFirDeclarationGenerationExtension = RouteBindingDeclarationGenerationExtension(session)
    }
}
