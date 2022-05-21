package main.transform

import kotlinx.serialization.json.*
import main.data.DataTransformation

class DataTransformerImpl : DataTransformer {

    override fun transform(
        jsonElements: Map<String, JsonElement>,
        transformations: Map<String, List<DataTransformation>>
    ): JsonElement {
        return transformations.map { (key, mTransformations) ->
            jsonElements.getValue(key).jsonArray.map { jsonElement ->
                jsonElement.jsonObject.toMutableMap()
            }.also { jsonObjects ->
                mTransformations.forEach { transformation ->
                    applyTransformations(
                        jsonElements = jsonElements,
                        jsonObjects = jsonObjects,
                        transformation = transformation,
                    )
                }
            }
        }.map { list ->
            list.map { JsonObject(it) }
        }.reduce { acc, jsonArray ->
            acc + jsonArray
        }.let {
            JsonArray(it)
        }
    }

    private fun applyTransformations(
        jsonElements: Map<String, JsonElement>,
        jsonObjects: List<MutableMap<String, JsonElement>>,
        transformation: DataTransformation,
    ) {
        when(transformation) {
            is DataTransformation.Add -> jsonObjects.forEach {
                require(!it.containsKey(transformation.field))
                it[transformation.field] = transformation.creator()
            }
            is DataTransformation.Join -> {
                require(jsonElements.containsKey(transformation.destinationTable))
                val table = jsonElements.getValue(transformation.destinationTable).jsonArray.map { jsonElement ->
                    jsonElement.jsonObject.mapValues { it.value.jsonPrimitive }
                }.associateBy {
                    it.getValue(transformation.destinationField).content
                }
                jsonObjects.forEach {
                    table.getValue(it.getValue(transformation.sourceField).jsonPrimitive.content).forEach { (newKey, newValue) ->
                        if(newKey != transformation.destinationField) {
                            require(!it.containsKey(newKey))
                            it[newKey] = newValue
                        }
                    }
                }
            }
            is DataTransformation.Merge -> jsonObjects.forEach {
                it[transformation.field] = transformation.transformer(
                    it.getValue(transformation.field), it.getValue(transformation.field2)
                )
                it.remove(transformation.field2)
            }
            is DataTransformation.Remove -> jsonObjects.forEach {
                require(it.containsKey(transformation.field))
                it.remove(transformation.field)
            }
            is DataTransformation.Rename -> jsonObjects.forEach {
                require(!it.containsKey(transformation.toField))
                it[transformation.toField] = requireNotNull(it.remove(transformation.fromField))
            }
            is DataTransformation.Transform -> jsonObjects.forEach {
                it[transformation.field] = transformation.transformer(it.getValue(transformation.field))
            }
            is DataTransformation.Split -> jsonObjects.forEach {
                require(!it.containsKey(transformation.field2))
                val result = transformation.transformer(
                    it.getValue(transformation.field)
                )
                it[transformation.field] = result.first
                it[transformation.field2] = result.second
            }
        }
    }

}