package co.netguru.blueprint.services.store.domain

import com.google.gson.TypeAdapter
import com.google.gson.annotations.JsonAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.io.IOException


@JsonAdapter(OrderStatus.Adapter::class)
enum class OrderStatus(val value: String) {

    PLACED("placed"),

    APPROVED("approved"),

    DELIVERED("delivered");

    override fun toString(): String {
        return value
    }

    class Adapter : TypeAdapter<OrderStatus>() {
        @Throws(IOException::class)
        override fun write(jsonWriter: JsonWriter, enumeration: OrderStatus) {
            jsonWriter.value(enumeration.value)
        }

        @Throws(IOException::class)
        override fun read(jsonReader: JsonReader): OrderStatus? {
            val value = jsonReader.nextString()
            return fromValue(value.toString())
        }
    }

    companion object {

        fun fromValue(text: String?): OrderStatus? {
            return OrderStatus.values().firstOrNull { it.value == text }
        }
    }
}