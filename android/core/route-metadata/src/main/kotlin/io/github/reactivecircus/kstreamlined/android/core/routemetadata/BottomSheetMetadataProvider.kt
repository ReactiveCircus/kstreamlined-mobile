package io.github.reactivecircus.kstreamlined.android.core.routemetadata

import androidx.navigation3.runtime.NavMetadataKey
import androidx.navigation3.runtime.metadata
import io.github.reactivecircus.routebinding.runtime.RouteMetadataProvider

public object BottomSheetMetadataProvider : RouteMetadataProvider {
    override fun provide(): Map<String, Any> = metadata { put(BottomSheetMetadataKey, Unit) }
}

public data object BottomSheetMetadataKey : NavMetadataKey<Unit>
