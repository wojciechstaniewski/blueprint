package co.netguru.blueprint.services.pet.domain

import com.google.gson.TypeAdapter
import com.google.gson.annotations.JsonAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.io.IOException

@JsonAdapter(Status.Adapter::class)
enum class Status(val value: String) {

    AVAILABLE("available"),

    PENDING("pending"),

    SOLD("sold ");

    override fun toString(): String {
        return value
    }

    class Adapter : TypeAdapter<Status>() {
        @Throws(IOException::class)
        override fun write(jsonWriter: JsonWriter, enumeration: Status) {
            jsonWriter.value(enumeration.value)
        }

        @Throws(IOException::class)
        override fun read(jsonReader: JsonReader): Status? {
            val value = jsonReader.nextString()
            return fromValue(value.toString())
        }
    }

    companion object {

        fun fromValue(text: String?): Status? {
            return Status.values().firstOrNull { it.value == text }
        }
    }
}