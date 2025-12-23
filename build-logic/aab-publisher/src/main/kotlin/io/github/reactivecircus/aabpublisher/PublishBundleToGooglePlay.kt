package io.github.reactivecircus.aabpublisher

import com.github.triplet.gradle.androidpublisher.CommitResponse
import com.github.triplet.gradle.androidpublisher.EditManager
import com.github.triplet.gradle.androidpublisher.EditResponse
import com.github.triplet.gradle.androidpublisher.PlayPublisher
import com.github.triplet.gradle.androidpublisher.ReleaseStatus
import com.github.triplet.gradle.androidpublisher.ResolutionStrategy
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import org.gradle.work.DisableCachingByDefault
import org.gradle.work.NormalizeLineEndings

@DisableCachingByDefault
internal abstract class PublishBundleToGooglePlay : DefaultTask() {
    @get:InputFile
    @get:PathSensitive(PathSensitivity.RELATIVE)
    @get:NormalizeLineEndings
    abstract val bundle: RegularFileProperty

    @get:InputFile
    @get:PathSensitive(PathSensitivity.RELATIVE)
    @get:NormalizeLineEndings
    abstract val serviceAccountCredentials: RegularFileProperty

    @get:Input
    abstract val applicationId: Property<String>

    @TaskAction
    fun execute() {
        val bundleFile = bundle.asFile.get()
        val credentialsFile = serviceAccountCredentials.asFile.get()
        val appId = applicationId.get()

        logger.lifecycle("Publishing {} to Google Play (internal testing).", bundleFile.name)

        val publisher = credentialsFile.inputStream().use { stream ->
            PlayPublisher(stream, appId)
        }

        val editId = when (val response = publisher.insertEdit()) {
            is EditResponse.Success -> response.id
            is EditResponse.Failure -> response.rethrow()
        }

        val editManager = EditManager(publisher, editId)
        val versionCode = editManager.uploadBundle(bundleFile, ResolutionStrategy.FAIL)
        if (versionCode != null) {
            logger.lifecycle("Uploaded bundle with version code: {}", versionCode)

            // publish to internal testing track and makes it the active version
            editManager.publishArtifacts(
                versionCodes = listOf(versionCode),
                didPreviousBuildSkipCommit = false,
                trackName = "internal",
                releaseStatus = ReleaseStatus.COMPLETED,
                releaseName = null,
                releaseNotes = null,
                userFraction = null,
                updatePriority = null,
                retainableArtifacts = null,
            )
        }

        when (val response = publisher.commitEdit(editId)) {
            is CommitResponse.Success -> {
                logger.lifecycle("Successfully published app bundle to Google Play (internal testing).")
            }

            is CommitResponse.Failure -> {
                if (response.failedToSendForReview()) {
                    val retryResponse = publisher.commitEdit(editId, sendChangesForReview = false)
                    if (retryResponse is CommitResponse.Failure) {
                        retryResponse.rethrow(response)
                    }
                    logger.lifecycle(
                        "Successfully published bundle to Google Play (internal testing)." +
                            " Changes were not sent for review.",
                    )
                } else {
                    response.rethrow()
                }
            }
        }
    }
}
