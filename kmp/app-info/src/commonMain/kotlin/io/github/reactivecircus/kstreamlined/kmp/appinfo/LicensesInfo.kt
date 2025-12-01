package io.github.reactivecircus.kstreamlined.kmp.appinfo

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

public interface LicensesInfo {
    public val artifacts: List<Artifact>

    @Serializable
    public data class Artifact(
        val groupId: String,
        val artifactId: String,
        val version: String,
        val name: String?,
        val spdxLicenses: List<SpdxLicense>?,
        val scm: Scm?,
        val unknownLicenses: List<UnknownLicense>?,
    ) {
        @Serializable
        public data class SpdxLicense(val identifier: String, val name: String, val url: String)

        @Serializable
        public data class Scm(val url: String)

        @Serializable
        public data class UnknownLicense(val name: String, val url: String)
    }

    public companion object {
        public fun from(artifacts: List<Artifact>): LicensesInfo = object : LicensesInfo {
            override val artifacts: List<Artifact> = artifacts
        }

        public fun fromJson(artifactsJson: () -> String): LicensesInfo =
            LicensesInfoImpl(artifactsJson)
    }
}

private class LicensesInfoImpl(private val artifactsJson: () -> String) : LicensesInfo {
    override val artifacts: List<LicensesInfo.Artifact>
        get() = _artifacts

    private val _artifacts: List<LicensesInfo.Artifact> by lazy {
        json.decodeFromString(artifactsJson())
    }

    private val json = Json {
        explicitNulls = false
        ignoreUnknownKeys = true
    }
}
