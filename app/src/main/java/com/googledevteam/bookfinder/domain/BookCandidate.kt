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

data class BookCandidate(
    val value: String,
    val kind: CandidateKind,
)

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
