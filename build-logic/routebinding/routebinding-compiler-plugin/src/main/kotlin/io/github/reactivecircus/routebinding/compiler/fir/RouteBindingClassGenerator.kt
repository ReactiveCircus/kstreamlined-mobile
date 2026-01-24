package io.github.reactivecircus.routebinding.compiler.fir

import io.github.reactivecircus.routebinding.compiler.ClassIds
import org.jetbrains.kotlin.descriptors.ClassKind
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.expressions.FirAnnotation
import org.jetbrains.kotlin.fir.expressions.builder.buildAnnotation
import org.jetbrains.kotlin.fir.expressions.builder.buildAnnotationArgumentMapping
import org.jetbrains.kotlin.fir.expressions.builder.buildArgumentList
import org.jetbrains.kotlin.fir.expressions.builder.buildGetClassCall
import org.jetbrains.kotlin.fir.expressions.builder.buildLiteralExpression
import org.jetbrains.kotlin.fir.expressions.builder.buildResolvedQualifier
import org.jetbrains.kotlin.fir.extensions.ExperimentalTopLevelDeclarationsGenerationApi
import org.jetbrains.kotlin.fir.extensions.FirDeclarationGenerationExtension
import org.jetbrains.kotlin.fir.extensions.FirDeclarationPredicateRegistrar
import org.jetbrains.kotlin.fir.extensions.MemberGenerationContext
import org.jetbrains.kotlin.fir.extensions.NestedClassGenerationContext
import org.jetbrains.kotlin.fir.extensions.predicate.LookupPredicate
import org.jetbrains.kotlin.fir.extensions.predicateBasedProvider
import org.jetbrains.kotlin.fir.plugin.createConeType
import org.jetbrains.kotlin.fir.plugin.createConstructor
import org.jetbrains.kotlin.fir.plugin.createMemberFunction
import org.jetbrains.kotlin.fir.plugin.createNestedClass
import org.jetbrains.kotlin.fir.plugin.createTopLevelClass
import org.jetbrains.kotlin.fir.resolve.defaultType
import org.jetbrains.kotlin.fir.resolve.providers.symbolProvider
import org.jetbrains.kotlin.fir.symbols.impl.FirClassLikeSymbol
import org.jetbrains.kotlin.fir.symbols.impl.FirClassSymbol
import org.jetbrains.kotlin.fir.symbols.impl.FirConstructorSymbol
import org.jetbrains.kotlin.fir.symbols.impl.FirNamedFunctionSymbol
import org.jetbrains.kotlin.fir.types.ConeKotlinType
import org.jetbrains.kotlin.fir.types.builder.buildResolvedTypeRef
import org.jetbrains.kotlin.fir.types.constructClassLikeType
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.name.SpecialNames
import org.jetbrains.kotlin.types.ConstantValueKind

internal class RouteBindingClassGenerator(session: FirSession) : FirDeclarationGenerationExtension(session) {

    companion object {
        private const val NavEntryInstallerSuffix = "_NavEntryInstaller"
        private val MetroFactoryName = Name.identifier("MetroFactory")
        private val HasRouteBindingAnnotation = LookupPredicate.create {
            annotated(ClassIds.RouteBinding.Annotation.asSingleFqName())
        }
    }

    private val predicateBasedProvider = session.predicateBasedProvider
    private val matchedFunctions by lazy {
        predicateBasedProvider.getSymbolsByPredicate(HasRouteBindingAnnotation)
            .filterIsInstance<FirNamedFunctionSymbol>()
            .filterNot { it.isLocal }
    }

    override fun FirDeclarationPredicateRegistrar.registerPredicates() {
        register(HasRouteBindingAnnotation)
    }

    @ExperimentalTopLevelDeclarationsGenerationApi
    override fun getTopLevelClassIds(): Set<ClassId> {
        return matchedFunctions.map { function ->
            val packageFqName = function.callableId.packageName
            val className = Name.identifier("${function.name.asString()}$NavEntryInstallerSuffix")
            ClassId(packageFqName, className)
        }.toSet()
    }

    @ExperimentalTopLevelDeclarationsGenerationApi
    override fun generateTopLevelClassLikeDeclaration(classId: ClassId): FirClassLikeSymbol<*>? {
        if (!classId.shortClassName.asString().endsWith(NavEntryInstallerSuffix)) {
            return null
        }
        return createTopLevelClass(
            classId = classId,
            key = RouteBindingPluginKey,
        ) {
            superType(ClassIds.RouteBinding.NavEntryInstaller.createConeType(session))
        }.apply {
            replaceAnnotations(annotations + buildDeprecatedAnnotation() + buildContributesIntoSetAnnotation())
        }.symbol
    }

    private fun buildContributesIntoSetAnnotation(): FirAnnotation = buildAnnotation {
        annotationTypeRef = buildResolvedTypeRef {
            coneType = ClassIds.Metro.ContributesIntoSet.createConeType(session)
        }
        argumentMapping = buildAnnotationArgumentMapping {
            mapping[Name.identifier("scope")] = buildGetClassCall {
                val appScopeSymbol = session.symbolProvider.getClassLikeSymbolByClassId(ClassIds.Metro.AppScope)!!
                val appScopeConeType = appScopeSymbol.classId.constructClassLikeType(emptyArray(), isMarkedNullable = false)
                argumentList = buildArgumentList {
                    arguments += buildResolvedQualifier {
                        coneTypeOrNull = appScopeConeType
                        symbol = appScopeSymbol
                        packageFqName = ClassIds.Metro.AppScope.packageFqName
                        relativeClassFqName = ClassIds.Metro.AppScope.relativeClassName
                        resolvedToCompanionObject = false
                    }
                }
            }
        }
    }

    override fun getCallableNamesForClass(classSymbol: FirClassSymbol<*>, context: MemberGenerationContext): Set<Name> {
        val classId = classSymbol.classId
        // For NavEntryInstaller class
        if (classId.shortClassName.asString().endsWith(NavEntryInstallerSuffix)) {
            return setOf(SpecialNames.INIT, Name.identifier("install"))
        }
        // For MetroFactory nested object
        if (classId.shortClassName == MetroFactoryName &&
            classId.outerClassId?.shortClassName?.asString()?.endsWith(NavEntryInstallerSuffix) == true) {
            return setOf(SpecialNames.INIT, Name.identifier("create"), Name.identifier("newInstance"))
        }
        return emptySet()
    }

    override fun generateConstructors(context: MemberGenerationContext): List<FirConstructorSymbol> {
        val classSymbol = context.owner
        val classId = classSymbol.classId

        // For NavEntryInstaller class - public constructor
        if (classId.shortClassName.asString().endsWith(NavEntryInstallerSuffix)) {
            return listOf(
                createConstructor(
                    owner = classSymbol,
                    key = RouteBindingPluginKey,
                    isPrimary = true,
                ).symbol
            )
        }

        // For MetroFactory nested object - private constructor
        if (classId.shortClassName == MetroFactoryName &&
            classId.outerClassId?.shortClassName?.asString()?.endsWith(NavEntryInstallerSuffix) == true) {
            return listOf(
                createConstructor(
                    owner = classSymbol,
                    key = RouteBindingPluginKey,
                    isPrimary = true,
                ).symbol
            )
        }

        return emptyList()
    }

    override fun generateFunctions(
        callableId: org.jetbrains.kotlin.name.CallableId,
        context: MemberGenerationContext?,
    ): List<FirNamedFunctionSymbol> {
        if (context == null) return emptyList()
        val classSymbol = context.owner
        val classId = classSymbol.classId

        // For NavEntryInstaller class - install function
        if (classId.shortClassName.asString().endsWith(NavEntryInstallerSuffix)) {
            if (callableId.callableName.asString() != "install") {
                return emptyList()
            }
            return listOf(generateInstallFunction(classSymbol))
        }

        // For MetroFactory nested object - create and newInstance functions
        if (classId.shortClassName == MetroFactoryName &&
            classId.outerClassId?.shortClassName?.asString()?.endsWith(NavEntryInstallerSuffix) == true) {
            val outerClassId = classId.outerClassId!!
            val outerClassType = outerClassId.createConeType(session)
            val factoryType = ClassIds.Metro.Factory.constructConeTypeWith(outerClassType)

            return when (callableId.callableName.asString()) {
                "create" -> listOf(
                    createMemberFunction(
                        owner = classSymbol,
                        key = RouteBindingPluginKey,
                        name = Name.identifier("create"),
                        returnType = factoryType,
                    ).symbol
                )
                "newInstance" -> listOf(
                    createMemberFunction(
                        owner = classSymbol,
                        key = RouteBindingPluginKey,
                        name = Name.identifier("newInstance"),
                        returnType = outerClassType,
                    ).symbol
                )
                else -> emptyList()
            }
        }

        return emptyList()
    }

    private fun generateInstallFunction(classSymbol: FirClassSymbol<*>): FirNamedFunctionSymbol {
        // Build types for parameters
        val navKeyType = ClassIds.Nav3.NavKey.createConeType(session)
        val entryProviderScopeType = ClassIds.Nav3.EntryProviderScope.constructConeTypeWith(navKeyType)
        val sharedTransitionScopeType = ClassIds.Compose.SharedTransitionScope.createConeType(session)
        val navBackStackType = ClassIds.Nav3.NavBackStack.constructConeTypeWith(navKeyType)
        val unitType = session.builtinTypes.unitType.coneType

        return createMemberFunction(
            owner = classSymbol,
            key = RouteBindingPluginKey,
            name = Name.identifier("install"),
            returnType = unitType,
        ) {
            // Context parameters - using contextReceiver API (names not supported yet)
            contextReceiver(entryProviderScopeType)
            contextReceiver(sharedTransitionScopeType)
            // Value parameter
            valueParameter(Name.identifier("backStack"), navBackStackType)
            // Mark as override (open is default for overrides)
            status {
                isOverride = true
            }
        }.symbol
    }

    private fun ClassId.constructConeTypeWith(vararg typeArguments: ConeKotlinType): ConeKotlinType {
        return constructClassLikeType(typeArguments, isMarkedNullable = false)
    }

    // --- Nested class generation for MetroFactory ---

    override fun getNestedClassifiersNames(
        classSymbol: FirClassSymbol<*>,
        context: NestedClassGenerationContext,
    ): Set<Name> {
        if (!classSymbol.classId.shortClassName.asString().endsWith(NavEntryInstallerSuffix)) {
            return emptySet()
        }
        return setOf(MetroFactoryName)
    }

    override fun generateNestedClassLikeDeclaration(
        owner: FirClassSymbol<*>,
        name: Name,
        context: NestedClassGenerationContext,
    ): FirClassLikeSymbol<*>? {
        if (!owner.classId.shortClassName.asString().endsWith(NavEntryInstallerSuffix)) {
            return null
        }
        if (name != MetroFactoryName) {
            return null
        }

        val ownerType = owner.defaultType()
        val factoryType = ClassIds.Metro.Factory.constructConeTypeWith(ownerType)

        return createNestedClass(
            owner = owner,
            key = RouteBindingPluginKey,
            name = MetroFactoryName,
            classKind = ClassKind.OBJECT,
        ) {
            superType(factoryType)
        }.apply {
            replaceAnnotations(annotations + buildDeprecatedAnnotation())
        }.symbol
    }

    private fun buildDeprecatedAnnotation(): FirAnnotation = buildAnnotation {
        annotationTypeRef = buildResolvedTypeRef {
            coneType = ClassIds.Kotlin.Deprecated.createConeType(session)
        }
        argumentMapping = buildAnnotationArgumentMapping {
            // message = "This synthesized declaration should not be used directly"
            mapping[Name.identifier("message")] = buildLiteralExpression(
                source = null,
                kind = ConstantValueKind.String,
                value = "This synthesized declaration should not be used directly",
                setType = true,
            )
            // TODO: Add level = DeprecationLevel.HIDDEN
            // Enum entries cannot be resolved via getClassLikeSymbolByClassId
            // Need to investigate proper FIR API for enum entry access
        }
    }
}

