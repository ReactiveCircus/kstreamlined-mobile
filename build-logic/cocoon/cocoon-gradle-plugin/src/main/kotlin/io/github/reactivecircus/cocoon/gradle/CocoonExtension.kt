package io.github.reactivecircus.cocoon.gradle

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property

public abstract class CocoonExtension internal constructor(objects: ObjectFactory) {
    public val annotation: Property<String> = objects.property(String::class.java)
    public val wrappingFunction: Property<String> = objects.property(String::class.java)
}
