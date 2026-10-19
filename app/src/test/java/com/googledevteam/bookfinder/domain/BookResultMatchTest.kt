package com.googledevteam.bookfinder.domain

import org.junit.Assert.assertEquals
import org.junit.Test

class BookResultMatchTest {
    @Test
    fun isbnLookupIsExactOnlyWhenReturnedIdentifiersContainTheQuery() {
        val result = BookResult(
            id = "volume-1",
            title = "Example textbook",
            authors = emptyList(),
            thumbnailUrl = null,
            isbn13 = "9780306406157",
        )

        assertEquals(
            BookMatchType.EXACT_ISBN,
            result.withLookupMatch("978-0-306-40615-7", CandidateKind.ISBN).matchType,
        )
    }

    @Test
    fun differentOrMissingIsbnIsUnverifiedRatherThanAnExactMatch() {
        val differentEdition = BookResult(
            id = "volume-2",
            title = "Example textbook",
            authors = emptyList(),
            thumbnailUrl = null,
            isbn13 = "9780131103627",
        )
        val missingIdentifier = BookResult(
            id = "volume-3",
            title = "Example textbook",
            authors = emptyList(),
            thumbnailUrl = null,
        )

        assertEquals(
            BookMatchType.ISBN_UNVERIFIED,
            differentEdition.withLookupMatch("9780306406157", CandidateKind.ISBN).matchType,
        )
        assertEquals(
            BookMatchType.ISBN_UNVERIFIED,
            missingIdentifier.withLookupMatch("9780306406157", CandidateKind.ISBN).matchType,
        )
    }

    @Test
    fun titleSearchAlwaysRemainsACandidate() {
        val result = BookResult(
            id = "volume-4",
            title = "Example textbook",
            authors = emptyList(),
            thumbnailUrl = null,
            isbn13 = "9780306406157",
        )

        assertEquals(
            BookMatchType.TITLE_CANDIDATE,
            result.withLookupMatch("Example textbook", CandidateKind.ENGLISH_TITLE).matchType,
        )
    }
}
