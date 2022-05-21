package main

import main.data.DataTransformation
import main.transform.TransformationGenerator

class TransformationGeneratorMain: TransformationGenerator {

    override val mainFileName: String
        get() = "mocked"

    override fun generate(): List<DataTransformation> {
        return listOf()
    }

}