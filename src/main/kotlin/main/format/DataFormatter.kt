package main.format

import kotlinx.serialization.json.JsonElement

interface DataFormatter {
    fun textToJson(text: String): JsonElement
}