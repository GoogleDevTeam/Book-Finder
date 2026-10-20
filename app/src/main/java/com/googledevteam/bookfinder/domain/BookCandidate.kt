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

private data class OcrTitleGroup(
    val startIndex: Int,
    val endIndex: Int,
    val value: String,
)

private val schoolListHeadingPattern = Regex(
    """^(?:grade\s+[\p{L}\p{N}-]+\s+)?(?:[\p{L}\p{N}&'’/-]+\s+)*(?:reading\s+list|book\s+list|books|syllabus)$""",
    RegexOption.IGNORE_CASE,
)

private val standaloneAuthorNamePattern = Regex(
    """^[\p{Lu}][\p{L}\p{M}'’.-]*(?:\s+[\p{Lu}][\p{L}\p{M}'’.-]*){1,3}$""",
)
private val authorInitialPattern = Regex("""(?:^|\s)[\p{Lu}]\.(?:\s|$)""")
private val internalApostropheNamePattern = Regex("""[\p{Lu}][\p{L}\p{M}]*['’][\p{Lu}][\p{L}\p{M}]*""")
private val possessiveWordPattern = Regex("""[\p{L}\p{M}]+['’]s(?:\b|$)""", RegexOption.IGNORE_CASE)
private val hyphenatedNamePattern = Regex("""[\p{Lu}][\p{L}\p{M}]+-[\p{Lu}][\p{L}\p{M}]+""")

private fun cleanTitleCandidate(value: String?): String? = value
    ?.trim()
    ?.trimEnd(' ', '-', '–', '—', ':', ';', '|')
    ?.trim()
    ?.takeIf(String::isNotBlank)

private fun looksLikeStandaloneAuthor(value: String): Boolean {
    if (!standaloneAuthorNamePattern.matches(value) || possessiveWordPattern.containsMatchIn(value)) {
        return false
    }

    return authorInitialPattern.containsMatchIn(value) ||
        internalApostropheNamePattern.containsMatchIn(value) ||
        hyphenatedNamePattern.containsMatchIn(value)
}

fun candidatesFromOcrText(text: String): List<BookCandidate> {
    val lines = text.lineSequence()
        .map { it.trim().replace(Regex("""\s+"""), " ") }
        .filter(String::isNotBlank)
        .filterNot(schoolListHeadingPattern::matches)
        .toList()
    val isbnsByLine = lines.map(IsbnParser::extractIsbns)
    val titlesByLine = lines.map { line ->
        cleanTitleCandidate(IsbnParser.titleCandidates(line).firstOrNull())
    }

    // Join author lines only when they have a strong name signal (initials or a
    // punctuated surname), so adjacent title-only entries are not merged.
    val titleGroups = mutableListOf<OcrTitleGroup>()
    val pairedAuthorLines = mutableSetOf<Int>()
    for (index in lines.indices) {
        if (index in pairedAuthorLines) continue
        val title = titlesByLine[index] ?: continue
        val authorIndex = index + 1
        val author = titlesByLine.getOrNull(authorIndex)
            ?.takeIf { isbnsByLine[authorIndex].isEmpty() && looksLikeStandaloneAuthor(it) }
        if (author != null) {
            titleGroups += OcrTitleGroup(index, authorIndex, "$title - $author")
            pairedAuthorLines += authorIndex
        } else {
            titleGroups += OcrTitleGroup(index, index, title)
        }
    }

    val pairedTitleGroups = mutableSetOf<Int>()
    val candidatesByLine = List(lines.size) { mutableListOf<BookCandidate>() }
    for (index in lines.indices) {
        val isbns = isbnsByLine[index]
        if (isbns.isEmpty()) continue

        val matchingTitleGroup = listOfNotNull(
            titleGroups.firstOrNull { it.startIndex == index },
            titleGroups.firstOrNull { it.endIndex == index - 1 },
            titleGroups.firstOrNull { it.startIndex == index + 1 },
        ).firstOrNull { it.startIndex !in pairedTitleGroups }
        matchingTitleGroup?.let { pairedTitleGroups += it.startIndex }

        isbns.forEachIndexed { isbnIndex, isbn ->
            candidatesByLine[index] += BookCandidate(
                value = isbn,
                kind = CandidateKind.ISBN,
                titleHint = if (isbnIndex == 0) matchingTitleGroup?.value else null,
            )
        }
    }

    titleGroups
        .filterNot { it.startIndex in pairedTitleGroups }
        .forEach { group ->
            candidatesByLine[group.startIndex] += BookCandidate(group.value, CandidateKind.ENGLISH_TITLE)
        }

    return candidatesByLine.flatten()
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
