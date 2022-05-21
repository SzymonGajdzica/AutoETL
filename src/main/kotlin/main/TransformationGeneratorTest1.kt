package main

import kotlinx.datetime.Instant
import kotlinx.serialization.json.*
import main.data.DataTransformation
import main.transform.TransformationGenerator
import kotlin.math.round

class TransformationGeneratorTest1: TransformationGenerator {

    override val mainFileName: String
        get() = "test1"

    override fun generate(): List<DataTransformation> {
        return listOf(
            DataTransformation.Rename(
                fromField = "code",
                toField = "id",
            ),
            DataTransformation.Split(
                field = "text",
                field2 = "description",
                transformer = { jsonElement ->
                    jsonElement.jsonPrimitive.content.split("\n").let {
                        Pair(JsonPrimitive(it.first()), JsonPrimitive(it.drop(1).joinToString("\n")))
                    }
                }
            ),
            DataTransformation.Remove(
                field = "comment",
            ),
            DataTransformation.Transform(
                field = "authorName",
                transformer = { jsonElement ->
                    if(jsonElement.jsonPrimitive.content == "unknown") JsonNull else jsonElement
                },
            ),
            DataTransformation.Join(
                sourceField = "sectionId",
                destinationField = "id",
                destinationTable = "test1_section",
            ),
            DataTransformation.Remove(
                field = "sectionId",
            ),
            DataTransformation.Rename(
                fromField = "name",
                toField = "sectionName",
            ),
            DataTransformation.Rename(
                fromField = "text",
                toField = "name",
            ),
            DataTransformation.Merge(
                field = "ratingSum",
                field2 = "ratingCount",
                transformer = { sum, count ->
                    if(count.jsonPrimitive.int == 0)
                        JsonPrimitive(0)
                    else
                        JsonPrimitive((sum.jsonPrimitive.int.toDouble() / count.jsonPrimitive.int.toDouble() / 10.0))
                },
            ),
            DataTransformation.Rename(
                fromField = "ratingSum",
                toField = "rating"
            ),
            DataTransformation.Transform(
                field = "tags",
                transformer = { jsonElement ->
                    jsonElement.jsonPrimitive.content.split(",").map {
                        JsonPrimitive(it.trim())
                    }.let { JsonArray(it) }
                }
            ),
            DataTransformation.Transform(
                field = "creationDate",
                transformer = {
                    JsonPrimitive(Instant.fromEpochMilliseconds(it.jsonPrimitive.long).toString())
                }
            )
        )
    }

}