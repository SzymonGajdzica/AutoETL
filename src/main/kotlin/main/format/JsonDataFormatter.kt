package main.format

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

class JsonDataFormatter: DataFormatter {

    override fun textToJson(text: String): JsonElement {
        return Json.parseToJsonElement(text)
    }

}