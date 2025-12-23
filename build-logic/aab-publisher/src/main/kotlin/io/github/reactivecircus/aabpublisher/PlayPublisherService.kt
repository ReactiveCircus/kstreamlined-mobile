package io.github.reactivecircus.aabpublisher

import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.services.BuildService
import org.gradle.api.services.BuildServiceParameters
import javax.inject.Inject

internal abstract class PlayPublisherService @Inject constructor(

) : BuildService<PlayPublisherService.Params> {

    // TODO

    interface Params : BuildServiceParameters {
        val appId: Property<String>
        val credentialsFile: RegularFileProperty
    }
}
