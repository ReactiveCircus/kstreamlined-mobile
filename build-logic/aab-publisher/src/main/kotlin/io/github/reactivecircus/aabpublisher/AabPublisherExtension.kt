package io.github.reactivecircus.aabpublisher

import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property

public abstract class AabPublisherExtension {
    public abstract val variant: Property<String>
    public abstract val serviceAccountCredentials: RegularFileProperty
}
