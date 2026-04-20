package io.github.reactivecircus.kstreamlined.kmp.arch.metro.retain

/**
 * Marker interface for types that can be retained via [metroRetain].
 *
 * Used as the bound type for [RetainedKey] map contributions.
 * Implement this on any class that should be injectable and retainable
 * through the Metro + retain integration.
 */
public interface Retainable
