package com.googledevteam.bookfinder.domain

enum class CandidateKind {
    ISBN,
    ENGLISH_TITLE,
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
)
