package main.format

import kotlinx.serialization.json.JsonElement

class DataFormatterImpl : DataFormatter {

    private val dataFormatters: List<DataFormatter> = listOf(
        JsonDataFormatter(),
    )

    override fun textToJson(text: String): JsonElement {
        return dataFormatters.mapNotNull {
            runCatching { it.textToJson(text) }.getOrNull()
        }.single()
    }

}