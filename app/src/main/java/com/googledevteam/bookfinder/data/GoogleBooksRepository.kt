package com.googledevteam.bookfinder.data

import com.googledevteam.bookfinder.BuildConfig
import com.googledevteam.bookfinder.domain.BookResult
import com.googledevteam.bookfinder.domain.CandidateKind
import com.googledevteam.bookfinder.domain.IsbnParser
import com.googledevteam.bookfinder.domain.withLookupMatch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class GoogleBooksRepository(
    private val apiKey: String = BuildConfig.GOOGLE_BOOKS_API_KEY,
) {
    suspend fun search(query: String, kind: CandidateKind): List<BookResult> =
        withContext(Dispatchers.IO) {
            val requestUrl = buildGoogleBooksRequestUrl(query, kind, apiKey)
            var retryCount = 0

            while (true) {
                val connection = (URL(requestUrl).openConnection() as HttpURLConnection).apply {
                    requestMethod = "GET"
                    connectTimeout = 10_000
                    readTimeout = 15_000
                    setRequestProperty("Accept", "application/json")
                }

                try {
                    val status = connection.responseCode
                    if (shouldRetryGoogleBooksRequest(status, retryCount)) {
                        retryCount += 1
                        connection.disconnect()
                        delay(750L)
                        continue
                    }
                    if (status !in 200..299) {
                        val errorBody = connection.errorStream?.bufferedReader()?.use { it.readText() }
                        throw IOException(
                            googleBooksHttpErrorMessage(status, extractGoogleBooksErrorReason(errorBody)),
                        )
                    }

                    val payload = connection.inputStream.bufferedReader().use { it.readText() }
                    return@withContext parseResults(JSONObject(payload)).map { it.withLookupMatch(query.trim(), kind) }
                } finally {
                    connection.disconnect()
                }
            }

            @Suppress("UNREACHABLE_CODE")
            emptyList()
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

                var isbn10: String? = null
                var isbn13: String? = null
                val identifiers = volume.optJSONArray("industryIdentifiers")
                if (identifiers != null) {
                    for (identifierIndex in 0 until identifiers.length()) {
                        val identifier = identifiers.optJSONObject(identifierIndex) ?: continue
                        val normalizedIsbn = IsbnParser.extractIsbns(
                            identifier.optString("identifier"),
                        ).firstOrNull() ?: continue
                        when (identifier.optString("type")) {
                            "ISBN_10" -> if (isbn10 == null) isbn10 = normalizedIsbn
                            "ISBN_13" -> if (isbn13 == null) isbn13 = normalizedIsbn
                        }
                    }
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
                        isbn10 = isbn10,
                        isbn13 = isbn13,
                        publisher = volume.optionalText("publisher"),
                        publishedDate = volume.optionalText("publishedDate"),
                    ),
                )
            }
        }
    }

    private fun JSONObject.optionalText(key: String): String? {
        val value = optString(key).trim()
        return value.takeIf { it.isNotEmpty() && !it.equals("null", ignoreCase = true) }
    }
}

internal fun buildGoogleBooksRequestUrl(
    query: String,
    kind: CandidateKind,
    apiKey: String,
): String {
    val trimmedQuery = query.trim()
    require(trimmedQuery.isNotEmpty()) { "Enter an ISBN or English book title." }
    val trimmedApiKey = apiKey.trim()
    check(trimmedApiKey.isNotEmpty()) {
        "Google Books isn't configured in this build. Retailer searches are still available."
    }

    val googleQuery = when (kind) {
        CandidateKind.ISBN -> {
            val isbn = IsbnParser.extractIsbns(trimmedQuery).firstOrNull()
                ?: throw IllegalArgumentException("Enter a valid ISBN-10 or ISBN-13.")
            "isbn:$isbn"
        }
        CandidateKind.ENGLISH_TITLE -> "intitle:$trimmedQuery"
    }

    val encodedQuery = URLEncoder.encode(googleQuery, Charsets.UTF_8.name())
    val encodedApiKey = URLEncoder.encode(trimmedApiKey, Charsets.UTF_8.name())
    return "https://www.googleapis.com/books/v1/volumes?q=$encodedQuery&maxResults=10&printType=books&key=$encodedApiKey"
}

internal fun shouldRetryGoogleBooksRequest(status: Int, retryCount: Int): Boolean =
    status in setOf(500, 502, 503, 504) && retryCount < 1

internal fun googleBooksHttpErrorMessage(status: Int, reason: String? = null): String = when {
    status == 429 || reason in setOf("dailyLimitExceeded", "quotaExceeded", "rateLimitExceeded") ->
        "Google Books has reached its request quota. Try again later or use a retailer search."
    status == 400 || status == 403 ->
        "Google Books rejected the request or API key. Check the key setup and restrictions, then try again."
    status in 500..599 ->
        "Google Books is temporarily unavailable (HTTP $status). Try again later or use a retailer search."
    else -> "Book search failed (HTTP $status). Try again."
}

private fun extractGoogleBooksErrorReason(errorBody: String?): String? {
    if (errorBody.isNullOrBlank()) return null
    return runCatching {
        JSONObject(errorBody)
            .optJSONObject("error")
            ?.optJSONArray("errors")
            ?.optJSONObject(0)
            ?.optString("reason")
            ?.takeIf(String::isNotBlank)
    }.getOrNull()
}
