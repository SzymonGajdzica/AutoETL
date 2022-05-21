package main.transform

import main.data.DataTransformation

interface TransformationGenerator {
    val mainFileName: String
    fun generate(): List<DataTransformation>
}