package com.googledevteam.bookfinder.data

import com.googledevteam.bookfinder.domain.BookResult
import com.googledevteam.bookfinder.domain.CandidateKind
import com.googledevteam.bookfinder.domain.IsbnParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class GoogleBooksRepository {
    suspend fun search(query: String, kind: CandidateKind): List<BookResult> =
        withContext(Dispatchers.IO) {
            val trimmedQuery = query.trim()
            require(trimmedQuery.isNotEmpty()) { "Enter an ISBN or English book title." }

            val googleQuery = when (kind) {
                CandidateKind.ISBN -> {
                    val isbn = IsbnParser.extractIsbns(trimmedQuery).firstOrNull()
                        ?: throw IllegalArgumentException("Enter a valid ISBN-10 or ISBN-13.")
                    "isbn:$isbn"
                }
                CandidateKind.ENGLISH_TITLE -> "intitle:$trimmedQuery"
            }

            val encodedQuery = URLEncoder.encode(googleQuery, Charsets.UTF_8.name())
            val url = URL(
                "https://www.googleapis.com/books/v1/volumes?q=$encodedQuery&maxResults=10&printType=books",
            )
            val connection = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                connectTimeout = 10_000
                readTimeout = 15_000
                setRequestProperty("Accept", "application/json")
            }

            try {
                val status = connection.responseCode
                if (status !in 200..299) {
                    throw IOException(
                        if (status == 429) {
                            "Google Books is busy. Wait a moment and try again."
                        } else {
                            "Book search failed (HTTP $status). Try again."
                        },
                    )
                }

                val payload = connection.inputStream.bufferedReader().use { it.readText() }
                parseResults(JSONObject(payload))
            } finally {
                connection.disconnect()
            }
        }

    private fun parseResults(payload: JSONObject): List<BookResult> {
        val items = payload.optJSONArray("items") ?: return emptyList()
        return buildList {
            for (index in 0 until items.length()) {
                val item = items.optJSONObject(index) ?: continue
                val volume = item.optJSONObject("volumeInfo") ?: continue
                val title = volume.optString("title").trim()
                if (title.isEmpty()) continue

                val authorsJson = volume.optJSONArray("authors")
                val authors = if (authorsJson == null) {
                    emptyList()
                } else {
                    (0 until authorsJson.length())
                        .mapNotNull { authorsJson.optString(it).takeIf(String::isNotBlank) }
                }

                val imageLinks = volume.optJSONObject("imageLinks")
                val thumbnail = imageLinks?.optString("thumbnail")
                    ?.takeIf { !it.isNullOrBlank() }
                    ?.replaceFirst("http://", "https://")

                add(
                    BookResult(
                        id = item.optString("id", "volume-$index"),
                        title = title,
                        authors = authors,
                        thumbnailUrl = thumbnail,
                    ),
                )
            }
        }
    }
}
