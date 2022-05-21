package main.data

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonPrimitive

sealed interface DataTransformation {
    data class Rename(val fromField: String, val toField: String): DataTransformation
    data class Transform(val field: String, val transformer: (JsonElement) -> JsonElement): DataTransformation
    data class Remove(val field: String): DataTransformation
    data class Add(val field: String, val creator: () -> JsonElement): DataTransformation
    data class Join(val sourceField: String, val destinationTable: String, val destinationField: String): DataTransformation
    data class Merge(val field: String, val field2: String, val transformer: (JsonElement, JsonElement) -> JsonElement): DataTransformation
    data class Split(val field: String, val field2: String, val transformer: (JsonElement) -> Pair<JsonElement, JsonElement>): DataTransformation

    companion object {
        fun SimpleMerge(field: String, field2: String, separator: String): Merge {
            return Merge(field, field2) { jsonElement, jsonElement2 ->
                JsonPrimitive("${jsonElement.jsonPrimitive.content}${separator}${jsonElement2.jsonPrimitive.content}")
            }
        }
    }

}