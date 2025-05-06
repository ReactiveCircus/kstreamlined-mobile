package io.github.reactivecircus.cocoon.gradle

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.property

public abstract class CocoonExtension internal constructor(objects: ObjectFactory) {
    public val annotation: Property<String> = objects.property<String>()
    public val wrappingFunction: Property<String> = objects.property<String>()
}
