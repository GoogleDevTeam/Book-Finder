package com.googledevteam.bookfinder.retail

import java.net.URLEncoder
import java.nio.charset.StandardCharsets

/** Public HTTPS search destinations; the app opens them but does not inspect retailer pages. */
enum class RetailerSearch(
    val label: String,
    private val searchEndpoint: String,
    private val queryParameter: String,
) {
    JARIR("Jarir", "https://www.jarir.com/sa-en/catalogsearch/result", "q"),
    AMAZON_SA("Amazon.sa", "https://www.amazon.sa/s", "k"),
    NOON("Noon", "https://www.noon.com/saudi-en/search/", "q");

    fun searchUrl(query: String): String {
        val reviewedQuery = query.trim()
        require(reviewedQuery.isNotEmpty()) { "A reviewed ISBN or title is required." }
        val encodedQuery = URLEncoder.encode(reviewedQuery, StandardCharsets.UTF_8.name())
        return "$searchEndpoint?$queryParameter=$encodedQuery"
    }
}
