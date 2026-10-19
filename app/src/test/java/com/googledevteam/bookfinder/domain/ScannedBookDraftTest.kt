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
    fun oneScanKeepsOrderAndPairsAnAdjacentTitleWithItsIsbn() {
        val candidates = candidatesFromOcrText(
            "Algebra Foundations\nISBN 978-0-306-40615-7\nWorld History",
        )

        assertEquals(2, candidates.size)
        assertEquals(
            listOf(CandidateKind.ISBN, CandidateKind.ENGLISH_TITLE),
            candidates.map { it.kind },
        )
        assertEquals(listOf("9780306406157", "World History"), candidates.map { it.value })
        assertEquals("Algebra Foundations", candidates.first().titleHint)
    }

    @Test
    fun interleavedCandidatesStayInOcrOrderAndKeepEachPairedTitleVisible() {
        val candidates = candidatesFromOcrText(
            "Algebra Foundations\nISBN 978-0-306-40615-7\nWorld History\nISBN 978-0-13-110362-7\nBiology",
        )

        assertEquals(
            listOf("9780306406157", "9780131103627", "Biology"),
            candidates.map { it.value },
        )
        assertEquals("Algebra Foundations", candidates[0].titleHint)
        assertEquals("World History", candidates[1].titleHint)
    }

    @Test
    fun retainsMoreThanFiveTitleOnlyEntriesAndRepeatedTitles() {
        val candidates = candidatesFromOcrText(
            "Algebra\nWorld History\nBiology\nChemistry\nPhysics\nArt\nAlgebra",
        )

        assertEquals(7, candidates.size)
        assertEquals("Algebra", candidates.first().value)
        assertEquals("Algebra", candidates.last().value)
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
    fun cancellingAnInFlightSearchReturnsDraftToReviewAndClearsOldResults() {
        val searching = ScannedBookDraft(
            id = 1,
            query = "New title",
            kind = CandidateKind.ENGLISH_TITLE,
            status = CandidateStatus.SEARCHING,
            searchResults = listOf(selectedBook),
        )

        val cancelled = searching.withSearchCancelled()

        assertEquals(CandidateStatus.NEEDS_REVIEW, cancelled.status)
        assertTrue(cancelled.searchResults.isEmpty())
        assertNull(cancelled.selectedBook)
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

    @Test
    fun selectingTheAlreadyActiveKindKeepsTheConfirmedBook() {
        val confirmed = ScannedBookDraft(1, "9780306406157", CandidateKind.ISBN)
            .withSearchResults(listOf(selectedBook))
            .withSelectedBook(selectedBook)

        val unchanged = confirmed.withKindChanged(CandidateKind.ISBN)

        assertEquals(CandidateStatus.MATCHED, unchanged.status)
        assertEquals(selectedBook, unchanged.selectedBook)
    }

    @Test
    fun changingKindClearsTheOldConfirmedMatch() {
        val confirmed = ScannedBookDraft(1, "9780306406157", CandidateKind.ISBN)
            .withSearchResults(listOf(selectedBook))
            .withSelectedBook(selectedBook)

        val changed = confirmed.withKindChanged(CandidateKind.ENGLISH_TITLE)

        assertEquals(CandidateStatus.NEEDS_REVIEW, changed.status)
        assertNull(changed.selectedBook)
        assertEquals(CandidateKind.ENGLISH_TITLE, changed.kind)
    }
}
