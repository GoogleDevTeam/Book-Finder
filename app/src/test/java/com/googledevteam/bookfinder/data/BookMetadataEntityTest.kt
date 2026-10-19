package com.googledevteam.bookfinder.data

import com.googledevteam.bookfinder.domain.BookResult
import org.junit.Assert.assertEquals
import org.junit.Test

class BookMetadataEntityTest {
    @Test
    fun cachedMetadataRoundTripsMultipleAuthorsAndPunctuation() {
        val original = BookResult(
            id = "volume-1",
            title = "A Book",
            authors = listOf("First Author", "Second, Jr."),
            thumbnailUrl = "https://example.test/cover.jpg",
            isbn10 = "0306406152",
            isbn13 = "9780306406157",
            publisher = "Example Education",
            publishedDate = "2024",
        )

        assertEquals(original, original.toCachedEntity().toBookResult())
    }

    @Test
    fun cachedMetadataRoundTripsMissingOptionalFields() {
        val original = BookResult(
            id = "volume-2",
            title = "Untitled Author",
            authors = emptyList(),
            thumbnailUrl = null,
        )

        assertEquals(original, original.toCachedEntity().toBookResult())
    }
}
