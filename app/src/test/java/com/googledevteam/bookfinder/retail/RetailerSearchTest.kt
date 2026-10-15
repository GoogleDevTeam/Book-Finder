package com.googledevteam.bookfinder.retail

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RetailerSearchTest {
    @Test
    fun buildsSearchUrlsForAllThreeRetailers() {
        val isbn = "9780306406157"

        assertEquals(
            "https://www.jarir.com/sa-en/catalogsearch/result?q=$isbn",
            RetailerSearch.JARIR.searchUrl(isbn),
        )
        assertEquals(
            "https://www.amazon.sa/s?k=$isbn",
            RetailerSearch.AMAZON_SA.searchUrl(isbn),
        )
        assertEquals(
            "https://www.noon.com/saudi-en/search/?q=$isbn",
            RetailerSearch.NOON.searchUrl(isbn),
        )
    }

    @Test
    fun encodesReservedCharactersInReviewedTitles() {
        val encoded = "A+%26+B+%2B+Basics"
        RetailerSearch.entries.forEach { retailer ->
            assertTrue(retailer.searchUrl(" A & B + Basics ").endsWith("=$encoded"))
        }
    }

    @Test(expected = IllegalArgumentException::class)
    fun rejectsAnEmptySearchTerm() {
        RetailerSearch.JARIR.searchUrl("  ")
    }
}
