package io.github.reactivecircus.chameleon.gradle

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property

public abstract class ChameleonExtension internal constructor(objects: ObjectFactory) {
    public val snapshotFunction: Property<String> = objects.property(String::class.java)
    public val themeVariantEnum: Property<String> = objects.property(String::class.java)
}
