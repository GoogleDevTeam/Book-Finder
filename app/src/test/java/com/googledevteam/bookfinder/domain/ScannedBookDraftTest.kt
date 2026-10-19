package com.googledevteam.bookfinder.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ScannedBookDraftTest {
    private val selectedBook = BookResult(
        id = "volume-1",
        title = "English Workbook",
        authors = listOf("A. Author"),
        thumbnailUrl = null,
        isbn13 = "9780306406157",
    )

    @Test
    fun oneScanKeepsIsbnsAndEnglishTitleLinesTogether() {
        val candidates = candidatesFromOcrText(
            "Algebra Foundations\nISBN 978-0-306-40615-7\nWorld History",
        )

        assertEquals(3, candidates.size)
        assertEquals(
            listOf(CandidateKind.ISBN, CandidateKind.ENGLISH_TITLE, CandidateKind.ENGLISH_TITLE),
            candidates.map { it.kind },
        )
        assertEquals(
            listOf("9780306406157", "Algebra Foundations", "World History"),
            candidates.map { it.value },
        )
    }

    @Test
    fun searchResultsRemainUnconfirmedUntilUserSelectsABook() {
        val draft = ScannedBookDraft(1, "9780306406157", CandidateKind.ISBN)
            .withSearchResults(listOf(selectedBook))

        assertEquals(CandidateStatus.NEEDS_REVIEW, draft.status)
        assertNull(draft.selectedBook)
        assertTrue(listOf(draft).confirmedReadingListBooks().isEmpty())
    }

    @Test
    fun emptySearchResultsMarkOnlyThatDraftAsNoResult() {
        val draft = ScannedBookDraft(1, "Algebra Foundations", CandidateKind.ENGLISH_TITLE)
            .withSearchResults(emptyList())

        assertEquals(CandidateStatus.NO_RESULT, draft.status)
        assertTrue(listOf(draft).confirmedReadingListBooks().isEmpty())
    }

    @Test
    fun selectedBookIsConfirmedAndQueryEditsClearTheOldMatch() {
        val confirmed = ScannedBookDraft(1, "9780306406157", CandidateKind.ISBN)
            .withSearchResults(listOf(selectedBook))
            .withSelectedBook(selectedBook)
        val saved = listOf(confirmed).confirmedReadingListBooks()

        assertEquals(CandidateStatus.MATCHED, confirmed.status)
        assertEquals(listOf(selectedBook), saved.map { it.book })
        assertEquals("9780306406157", saved.single().lookupTerm)

        val edited = confirmed.withEditedQuery("New title", CandidateKind.ENGLISH_TITLE)
        assertEquals(CandidateStatus.NEEDS_REVIEW, edited.status)
        assertNull(edited.selectedBook)
        assertTrue(edited.searchResults.isEmpty())
        assertTrue(listOf(edited).confirmedReadingListBooks().isEmpty())
    }
}
