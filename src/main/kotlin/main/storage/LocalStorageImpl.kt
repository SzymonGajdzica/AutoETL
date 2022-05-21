package main.storage

import java.io.File

class LocalStorageImpl : LocalStorage {

    override fun readDataFiles(): Map<String, String> {
        return File("data").listFiles()?.associate {
            it.nameWithoutExtension to it.readText()
        } ?: mapOf()
    }

    override fun saveResultData(data: String) {
        File("result.json").writeText(data)
    }

    override fun readResultData(): String {
        return File("result.json").readText()
    }

}