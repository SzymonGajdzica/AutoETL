package main.transform

import kotlinx.serialization.json.JsonElement
import main.data.DataTransformation

interface DataTransformer {
    fun transform(jsonElements: Map<String, JsonElement>, transformations: Map<String, List<DataTransformation>>): JsonElement
}