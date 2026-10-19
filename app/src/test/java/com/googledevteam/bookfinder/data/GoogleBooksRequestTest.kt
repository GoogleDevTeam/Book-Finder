package com.googledevteam.bookfinder.data

import com.googledevteam.bookfinder.domain.CandidateKind
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

class GoogleBooksRequestTest {
    @Test
    fun isbnRequestIncludesNormalizedQueryAndConfiguredKey() {
        val url = buildGoogleBooksRequestUrl(
            "978-0-13-110362-7",
            CandidateKind.ISBN,
            "AIza-test_key-123",
        )

        assertTrue(url.contains("q=isbn%3A9780131103627"))
        assertTrue(url.endsWith("&key=AIza-test_key-123"))
    }

    @Test
    fun titleRequestEncodesQueryAndKey() {
        val url = buildGoogleBooksRequestUrl(
            "C Programming",
            CandidateKind.ENGLISH_TITLE,
            "test key",
        )

        assertTrue(url.contains("q=intitle%3AC+Programming"))
        assertTrue(url.endsWith("&key=test+key"))
    }

    @Test
    fun missingKeyFailsWithActionableMessage() {
        val error = assertThrows(IllegalStateException::class.java) {
            buildGoogleBooksRequestUrl("9780131103627", CandidateKind.ISBN, "")
        }

        assertTrue(error.message.orEmpty().contains("Retailer searches are still available"))
    }

    @Test
    fun quotaFailurePointsUsersToRetailerSearch() {
        assertEquals(
            "Google Books has reached its request quota. Try again later or use a retailer search.",
            googleBooksHttpErrorMessage(429, "rateLimitExceeded"),
        )
    }
}
