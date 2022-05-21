package main.data

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class Article(
    val id: String,
    val name: String,
    val description: String,
    val creationDate: Instant,
    val authorName: String?,
    val tags: List<String>,
    val sectionName: String,
    val rating: Double?,
)