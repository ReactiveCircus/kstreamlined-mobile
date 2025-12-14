package io.github.reactivecircus.licentia.runtime

public interface LicensesInfo {
    public val artifacts: List<Artifact>

    public data class Artifact(
        val groupId: String,
        val artifactId: String,
        val version: String,
        val name: String?,
        val spdxLicenses: List<SpdxLicense>?,
        val scm: Scm?,
        val unknownLicenses: List<UnknownLicense>?,
    ) {
        public data class SpdxLicense(val identifier: String, val name: String, val url: String)

        public data class Scm(val url: String)

        public data class UnknownLicense(val name: String, val url: String)
    }
}
