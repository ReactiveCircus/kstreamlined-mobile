package io.github.reactivecircus.routebinding.compiler.fir.diagnostics

import io.github.reactivecircus.routebinding.compiler.ClassIds
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.types.ConeKotlinType
import org.jetbrains.kotlin.fir.types.classId
import org.jetbrains.kotlin.fir.types.constructClassLikeType
import org.jetbrains.kotlin.fir.types.isSubtypeOf

@Suppress("ReturnCount")
internal fun ConeKotlinType.isSupportedRouteBindingType(session: FirSession): Boolean {
    val classId = this.classId ?: return false
    if (classId == ClassIds.Compose.SharedTransitionScope) return true
    if (classId == ClassIds.Nav3.NavBackStack) return true
    val navKeyType = ClassIds.Nav3.NavKey.constructClassLikeType()
    return this.isSubtypeOf(navKeyType, session)
}
