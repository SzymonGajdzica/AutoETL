package main

import kotlinx.datetime.*
import kotlinx.datetime.TimeZone
import kotlinx.serialization.json.*
import main.data.DataTransformation
import main.transform.TransformationGenerator
import java.util.*
import kotlin.math.round

class TransformationGeneratorTest2: TransformationGenerator {

    override val mainFileName: String
        get() = "test2"

    override fun generate(): List<DataTransformation> {
        return listOf(
            DataTransformation.Add(
                field = "id",
                creator = {
                    JsonPrimitive(UUID.randomUUID().toString())
                }
            ),
            DataTransformation.Join(
                sourceField = "authorId",
                destinationField = "id",
                destinationTable = "test2_author",
            ),
            DataTransformation.Remove(
                field = "authorId",
            ),
            DataTransformation.Remove(
                field = "age",
            ),
            DataTransformation.Rename(
                fromField = "name",
                toField = "authorName",
            ),
            DataTransformation.Rename(
                fromField = "title",
                toField = "name",
            ),
            DataTransformation.Transform(
                field = "rating",
                transformer = { jsonElement ->
                    JsonPrimitive(jsonElement.jsonPrimitive.int.toDouble() / 10.0)
                }
            ),
            DataTransformation.Add(
                field = "tags",
                creator = {
                    JsonArray(listOf())
                }
            ),
            DataTransformation.Rename(
                fromField = "section",
                toField = "sectionName"
            ),
            DataTransformation.SimpleMerge(
                field = "hour",
                field2 = "minute",
                separator = "/"
            ),
            DataTransformation.SimpleMerge(
                field = "hour",
                field2 = "second",
                separator = "/"
            ),
            DataTransformation.SimpleMerge(
                field = "hour",
                field2 = "year",
                separator = "/"
            ),
            DataTransformation.SimpleMerge(
                field = "hour",
                field2 = "month",
                separator = "/"
            ),
            DataTransformation.SimpleMerge(
                field = "hour",
                field2 = "day",
                separator = "/"
            ),
            DataTransformation.Transform(
                field = "hour",
                transformer = { jsonElement ->
                    val date = jsonElement.jsonPrimitive.content.split("/").map { it.toInt() }.let {
                        LocalDateTime(
                            month = Month(it[4]),
                            year = it[3],
                            hour = it[0],
                            minute = it[1],
                            second = it[2],
                            dayOfMonth = it[5],
                            nanosecond = 0,
                        )
                    }
                    JsonPrimitive(date.toInstant(TimeZone.UTC).toString())
                }
            ),
            DataTransformation.Rename(
                fromField = "hour",
                toField = "creationDate",
            )
        )
    }


}