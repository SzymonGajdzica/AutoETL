package main.storage

interface LocalStorage {
    fun readDataFiles(): Map<String, String>
    fun saveResultData(data: String)
    fun readResultData(): String
}