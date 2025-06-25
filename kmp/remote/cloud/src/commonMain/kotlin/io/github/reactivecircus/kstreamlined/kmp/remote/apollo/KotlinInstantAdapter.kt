package io.github.reactivecircus.kstreamlined.kmp.remote.apollo

import com.apollographql.apollo.api.Adapter
import com.apollographql.apollo.api.CustomScalarAdapters
import com.apollographql.apollo.api.json.JsonReader
import com.apollographql.apollo.api.json.JsonWriter
import kotlin.time.Instant

// TODO remove once apollo-kotlin-adapters supports `kotlin.time.Instant`.
//  https://github.com/apollographql/apollo-kotlin-adapters/issues/31
internal object KotlinInstantAdapter : Adapter<Instant> {
    override fun fromJson(reader: JsonReader, customScalarAdapters: CustomScalarAdapters): Instant {
        return Instant.parse(reader.nextString()!!)
    }

    override fun toJson(writer: JsonWriter, customScalarAdapters: CustomScalarAdapters, value: Instant) {
        writer.value(value.toString())
    }
}
