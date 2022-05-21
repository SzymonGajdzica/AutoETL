package main

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import main.data.Article
import main.format.DataFormatter
import main.format.DataFormatterImpl
import main.mock.DataMocker
import main.mock.DataMockerImpl
import main.storage.LocalStorage
import main.storage.LocalStorageImpl
import main.transform.DataTransformer
import main.transform.DataTransformerImpl
import main.transform.TransformationGenerator

class AutoETL {

    private val localStorage: LocalStorage = LocalStorageImpl()
    private val dataFormatter: DataFormatter = DataFormatterImpl()
    private val dataTransformer: DataTransformer = DataTransformerImpl()
    private val format = Json {
        prettyPrint = true
    }
    private val generators: List<TransformationGenerator> = listOf(
        TransformationGeneratorMain(),
        TransformationGeneratorTest1(),
        TransformationGeneratorTest2(),
    )
    private val dataMocker: DataMocker = DataMockerImpl()

    fun run() {
        dataMocker.mockArticles()
        generate()
        read()
    }

    private fun generate() {
        val files = localStorage.readDataFiles()
        val jsonElements = files.mapValues {
            dataFormatter.textToJson(it.value)
        }
        val dataTransformations = generators.associate {
            it.mainFileName to it.generate()
        }
        val transformed = dataTransformer.transform(jsonElements, dataTransformations)
        localStorage.saveResultData(format.encodeToString(transformed))
    }

    private fun read() {
        val data = localStorage.readResultData()
        val articleList = format.decodeFromString<List<Article>>(data)
        println("----------------------------JSON----------------------------")
        println(data)
        println("----------------------------Object----------------------------")
        println(articleList)
    }

}