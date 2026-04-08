package io.github.reactivecircus.routebinding.compiler.fir.diagnostics

import io.github.reactivecircus.routebinding.compiler.ClassIds
import org.jetbrains.kotlin.KtSourceElement
import org.jetbrains.kotlin.descriptors.Visibilities
import org.jetbrains.kotlin.diagnostics.DiagnosticReporter
import org.jetbrains.kotlin.diagnostics.reportOn
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.analysis.checkers.MppCheckerKind
import org.jetbrains.kotlin.fir.analysis.checkers.context.CheckerContext
import org.jetbrains.kotlin.fir.analysis.checkers.declaration.FirSimpleFunctionChecker
import org.jetbrains.kotlin.fir.declarations.FirFunction
import org.jetbrains.kotlin.fir.declarations.FirNamedFunction
import org.jetbrains.kotlin.fir.declarations.FirValueParameter
import org.jetbrains.kotlin.fir.declarations.getAnnotationByClassId
import org.jetbrains.kotlin.fir.declarations.getKClassArgument
import org.jetbrains.kotlin.fir.declarations.hasAnnotation
import org.jetbrains.kotlin.fir.declarations.utils.effectiveVisibility
import org.jetbrains.kotlin.fir.declarations.utils.nameOrSpecialName
import org.jetbrains.kotlin.fir.resolve.getContainingClass
import org.jetbrains.kotlin.fir.types.ConeKotlinType
import org.jetbrains.kotlin.fir.types.classId
import org.jetbrains.kotlin.fir.types.coneType
import org.jetbrains.kotlin.fir.types.constructClassLikeType
import org.jetbrains.kotlin.fir.types.isSubtypeOf
import org.jetbrains.kotlin.fir.types.renderReadable
import org.jetbrains.kotlin.name.Name

internal object RouteBindingFunctionChecker : FirSimpleFunctionChecker(MppCheckerKind.Common) {
    context(context: CheckerContext, reporter: DiagnosticReporter)
    override fun check(declaration: FirNamedFunction) {
        if (!declaration.hasAnnotation(ClassIds.RouteBinding.Annotation, context.session)) return

        val functionName = declaration.nameOrSpecialName.asString()

        if (declaration.getContainingClass() != null) {
            reporter.reportOn(
                declaration.source,
                RouteBindingDiagnostics.FUNCTION_MUST_BE_TOP_LEVEL,
                functionName,
            )
        }

        if (!declaration.hasAnnotation(ClassIds.Compose.Composable, context.session)) {
            reporter.reportOn(
                declaration.source,
                RouteBindingDiagnostics.FUNCTION_MUST_BE_COMPOSABLE,
                functionName,
            )
        }

        val visibility = declaration.effectiveVisibility.toVisibility()

        if (visibility == Visibilities.Private) {
            reporter.reportOn(
                declaration.source,
                RouteBindingDiagnostics.FUNCTION_CANNOT_BE_PRIVATE,
                functionName,
            )
        }
        if (visibility.isPublicAPI) {
            reporter.reportOn(
                declaration.source,
                RouteBindingDiagnostics.FUNCTION_CAN_BE_INTERNAL,
                functionName,
            )
        }

        checkParameterTypes(declaration, functionName)
    }

    context(context: CheckerContext, reporter: DiagnosticReporter)
    private fun checkParameterTypes(declaration: FirFunction, functionName: String) {
        val session = context.session
        val seenParamTypes = mutableMapOf<SupportedParamType, Boolean>()

        val annotationRouteType = declaration
            .getAnnotationByClassId(ClassIds.RouteBinding.Annotation, session)
            ?.getKClassArgument(routeArgName)

        val receiverParam = declaration.receiverParameter
        if (receiverParam != null) {
            val receiverType = receiverParam.typeRef.coneType
            val supportedType = receiverType.toSupportedParamTypeOrNull(session)
            if (supportedType == null) {
                reporter.reportOn(
                    receiverParam.source,
                    RouteBindingDiagnostics.UNSUPPORTED_RECEIVER_TYPE,
                    functionName,
                    receiverType.renderReadable(),
                )
            } else {
                seenParamTypes[supportedType] = true
                if (supportedType == SupportedParamType.NavKeySubtype) {
                    checkRouteTypeMismatch(receiverParam.source, receiverType, annotationRouteType, functionName)
                }
            }
        }

        for (param in declaration.contextParameters) {
            checkContextParameter(param, functionName, session, seenParamTypes, annotationRouteType)
        }

        for (param in declaration.valueParameters) {
            checkValueParameter(param, functionName, session, seenParamTypes, annotationRouteType)
        }
    }

    context(_: CheckerContext, reporter: DiagnosticReporter)
    private fun checkContextParameter(
        param: FirValueParameter,
        functionName: String,
        session: FirSession,
        seenParamTypes: MutableMap<SupportedParamType, Boolean>,
        annotationRouteType: ConeKotlinType?,
    ) {
        val paramType = param.returnTypeRef.coneType
        val paramName = param.name.asString()
        if (paramType.toSupportedParamTypeOrNull(session) == null) {
            reporter.reportOn(
                param.source,
                RouteBindingDiagnostics.UNSUPPORTED_CONTEXT_PARAMETER_TYPE,
                functionName,
                paramName,
                paramType.renderReadable(),
            )
            return
        }
        val supportedType = paramType.toSupportedParamTypeOrNull(session) ?: return
        if (seenParamTypes.containsKey(supportedType)) {
            reporter.reportOn(
                param.source,
                RouteBindingDiagnostics.DUPLICATE_PARAMETER_TYPE,
                functionName,
                supportedType.displayName,
            )
        } else {
            seenParamTypes[supportedType] = true
            if (supportedType == SupportedParamType.NavKeySubtype) {
                checkRouteTypeMismatch(param.source, paramType, annotationRouteType, functionName)
            }
        }
    }

    context(_: CheckerContext, reporter: DiagnosticReporter)
    private fun checkValueParameter(
        param: FirValueParameter,
        functionName: String,
        session: FirSession,
        seenParamTypes: MutableMap<SupportedParamType, Boolean>,
        annotationRouteType: ConeKotlinType?,
    ) {
        if (param.defaultValue != null) return
        val paramType = param.returnTypeRef.coneType
        val paramName = param.name.asString()
        if (paramType.toSupportedParamTypeOrNull(session) == null) {
            reporter.reportOn(
                param.source,
                RouteBindingDiagnostics.UNSUPPORTED_VALUE_PARAMETER_TYPE,
                functionName,
                paramName,
                paramType.renderReadable(),
            )
            return
        }
        val supportedType = paramType.toSupportedParamTypeOrNull(session) ?: return
        if (seenParamTypes.containsKey(supportedType)) {
            reporter.reportOn(
                param.source,
                RouteBindingDiagnostics.DUPLICATE_PARAMETER_TYPE,
                functionName,
                supportedType.displayName,
            )
        } else {
            seenParamTypes[supportedType] = true
            if (supportedType == SupportedParamType.NavKeySubtype) {
                checkRouteTypeMismatch(param.source, paramType, annotationRouteType, functionName)
            }
        }
    }

    context(_: CheckerContext, reporter: DiagnosticReporter)
    private fun checkRouteTypeMismatch(
        source: KtSourceElement?,
        paramType: ConeKotlinType,
        annotationRouteType: ConeKotlinType?,
        functionName: String,
    ) {
        if (annotationRouteType == null) return
        if (paramType.classId != annotationRouteType.classId) {
            reporter.reportOn(
                source,
                RouteBindingDiagnostics.ROUTE_PARAMETER_TYPE_MISMATCH,
                functionName,
                annotationRouteType.renderReadable(),
                paramType.renderReadable(),
            )
        }
    }
}

private val routeArgName = Name.identifier("route")

private enum class SupportedParamType(val displayName: String) {
    SharedTransitionScope("SharedTransitionScope"),
    NavBackStack("NavBackStack<NavKey>"),
    NavKeySubtype("NavKey"),
}

private fun ConeKotlinType.toSupportedParamTypeOrNull(session: FirSession): SupportedParamType? {
    val classId = this.classId ?: return null
    if (classId == ClassIds.Compose.SharedTransitionScope) return SupportedParamType.SharedTransitionScope
    if (classId == ClassIds.Nav3.NavBackStack) return SupportedParamType.NavBackStack
    val navKeyType = ClassIds.Nav3.NavKey.constructClassLikeType()
    if (this.isSubtypeOf(navKeyType, session)) return SupportedParamType.NavKeySubtype
    return null
}
