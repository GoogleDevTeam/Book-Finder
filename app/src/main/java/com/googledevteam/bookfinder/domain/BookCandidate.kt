package com.googledevteam.bookfinder.domain

enum class CandidateKind {
    ISBN,
    ENGLISH_TITLE,
}

enum class BookMatchType {
    EXACT_ISBN,
    ISBN_UNVERIFIED,
    TITLE_CANDIDATE,
}

enum class CandidateStatus {
    NEEDS_REVIEW,
    SEARCHING,
    NO_RESULT,
    MATCHED,
}

data class BookCandidate(
    val value: String,
    val kind: CandidateKind,
)

fun candidatesFromOcrText(text: String): List<BookCandidate> {
    val isbnCandidates = IsbnParser.extractIsbns(text).map { BookCandidate(it, CandidateKind.ISBN) }
    val titleCandidates = IsbnParser.titleCandidates(text).map { BookCandidate(it, CandidateKind.ENGLISH_TITLE) }
    return (isbnCandidates + titleCandidates).distinctBy { it.kind to it.value.trim().lowercase() }
}

data class BookResult(
    val id: String,
    val title: String,
    val authors: List<String>,
    val thumbnailUrl: String?,
    val isbn10: String? = null,
    val isbn13: String? = null,
    val publisher: String? = null,
    val publishedDate: String? = null,
    val matchType: BookMatchType = BookMatchType.TITLE_CANDIDATE,
)

data class ScannedBookDraft(
    val id: Long,
    val query: String,
    val kind: CandidateKind,
    val status: CandidateStatus = CandidateStatus.NEEDS_REVIEW,
    val searchResults: List<BookResult> = emptyList(),
    val selectedBook: BookResult? = null,
)

data class ConfirmedReadingListBook(
    val book: BookResult,
    val lookupTerm: String,
    val lookupKind: CandidateKind,
)

fun ScannedBookDraft.withEditedQuery(query: String, kind: CandidateKind): ScannedBookDraft = copy(
    query = query,
    kind = kind,
    status = CandidateStatus.NEEDS_REVIEW,
    searchResults = emptyList(),
    selectedBook = null,
)

fun ScannedBookDraft.withSearchResults(results: List<BookResult>): ScannedBookDraft = copy(
    status = if (results.isEmpty()) CandidateStatus.NO_RESULT else CandidateStatus.NEEDS_REVIEW,
    searchResults = results,
    selectedBook = null,
)

fun ScannedBookDraft.withSelectedBook(book: BookResult): ScannedBookDraft = copy(
    status = CandidateStatus.MATCHED,
    selectedBook = book,
)

fun List<ScannedBookDraft>.confirmedReadingListBooks(): List<ConfirmedReadingListBook> = mapNotNull { draft ->
    val selectedBook = draft.selectedBook
    if (draft.status == CandidateStatus.MATCHED && selectedBook != null && draft.query.isNotBlank()) {
        ConfirmedReadingListBook(selectedBook, draft.query.trim(), draft.kind)
    } else {
        null
    }
}

fun BookResult.withLookupMatch(lookupTerm: String, lookupKind: CandidateKind): BookResult {
    if (lookupKind != CandidateKind.ISBN) {
        return copy(matchType = BookMatchType.TITLE_CANDIDATE)
    }

    val requestedIsbn = IsbnParser.extractIsbns(lookupTerm).firstOrNull()
    val resultIsbns = listOfNotNull(isbn10, isbn13)
        .flatMap { identifier -> IsbnParser.extractIsbns(identifier) }
    val matchType = if (requestedIsbn != null && requestedIsbn in resultIsbns) {
        BookMatchType.EXACT_ISBN
    } else {
        BookMatchType.ISBN_UNVERIFIED
    }
    return copy(matchType = matchType)
}
