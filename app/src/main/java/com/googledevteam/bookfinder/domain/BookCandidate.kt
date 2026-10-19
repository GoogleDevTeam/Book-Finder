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
    val titleHint: String? = null,
)

fun candidatesFromOcrText(text: String): List<BookCandidate> {
    val lines = text.lineSequence()
        .map { it.trim().replace(Regex("""\s+"""), " ") }
        .filter(String::isNotBlank)
        .toList()
    val isbnsByLine = lines.map(IsbnParser::extractIsbns)
    val titlesByLine = lines.map { IsbnParser.titleCandidates(it).firstOrNull() }
    val pairedTitleByIsbnLine = mutableMapOf<Int, String>()
    val pairedTitleLines = mutableSetOf<Int>()

    // If a title and ISBN are on the same or immediately adjacent OCR lines, keep them
    // together as one ISBN-first candidate and show the detected title for user review.
    for (index in lines.indices) {
        if (isbnsByLine[index].isEmpty()) continue
        val titleLine = if (titlesByLine[index] != null) {
            index
        } else {
            listOf(index - 1, index + 1).firstOrNull { neighbor ->
                neighbor in lines.indices &&
                    isbnsByLine[neighbor].isEmpty() &&
                    titlesByLine[neighbor] != null &&
                    neighbor !in pairedTitleLines
            }
        } ?: continue
        pairedTitleByIsbnLine[index] = titlesByLine[titleLine].orEmpty()
        pairedTitleLines += titleLine
    }

    return buildList {
        for (index in lines.indices) {
            val isbns = isbnsByLine[index]
            if (isbns.isNotEmpty()) {
                isbns.forEachIndexed { isbnIndex, isbn ->
                    add(
                        BookCandidate(
                            value = isbn,
                            kind = CandidateKind.ISBN,
                            titleHint = if (isbnIndex == 0) pairedTitleByIsbnLine[index] else null,
                        ),
                    )
                }
            } else if (index !in pairedTitleLines) {
                titlesByLine[index]?.let { title ->
                    add(BookCandidate(title, CandidateKind.ENGLISH_TITLE))
                }
            }
        }
    }
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
    val titleHint: String? = null,
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

fun ScannedBookDraft.withKindChanged(kind: CandidateKind): ScannedBookDraft =
    if (kind == this.kind) this else withEditedQuery(query, kind)

fun ScannedBookDraft.withSearchResults(results: List<BookResult>): ScannedBookDraft = copy(
    status = if (results.isEmpty()) CandidateStatus.NO_RESULT else CandidateStatus.NEEDS_REVIEW,
    searchResults = results,
    selectedBook = null,
)

fun ScannedBookDraft.withSearchCancelled(): ScannedBookDraft = if (status == CandidateStatus.SEARCHING) {
    copy(
        status = CandidateStatus.NEEDS_REVIEW,
        searchResults = emptyList(),
        selectedBook = null,
    )
} else {
    this
}

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
