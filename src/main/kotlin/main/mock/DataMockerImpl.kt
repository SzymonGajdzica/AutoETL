package main.mock

import kotlinx.datetime.Clock
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import main.data.Article
import java.io.File
import java.util.*

class DataMockerImpl : DataMocker {

    private val format = Json {
        prettyPrint = true
    }

    override fun mockArticles() {
        val articles = listOf(
            Article(
                id = UUID.randomUUID().toString(),
                name = "Why drinking water is important?",
                creationDate = Clock.System.now(),
                authorName = "Andrew",
                description = "It is important because...",
                rating = 0.7,
                sectionName = "Health",
                tags = listOf("Water", "Health", "Drinking")
            ),
            Article(
                id = UUID.randomUUID().toString(),
                name = "What is the best way to find butter?",
                creationDate = Clock.System.now(),
                authorName = "Micheal",
                description = "Its easy, just...",
                rating = 0.7,
                sectionName = "Butter",
                tags = listOf("Butter", "BestWay")
            ),
        )
        File("data/mocked.json").writeText(format.encodeToString(articles))
    }

}