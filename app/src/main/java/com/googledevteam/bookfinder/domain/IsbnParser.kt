package com.googledevteam.bookfinder.domain

/**
 * Extracts ISBN-10/13 candidates from OCR text and validates their checksums.
 * OCR may represent Arabic-Indic or Eastern Arabic digits; both are normalized
 * before matching so printed ISBNs remain usable on Arabic-language pages.
 */
object IsbnParser {
    private val candidatePattern = Regex(
        """(?<![A-Za-z0-9])(?:97[89][ \t-]?)?(?:\d[ \t-]?){9}[\dX](?![A-Za-z0-9])(?![ \t-]*[\dX])""",
        RegexOption.IGNORE_CASE,
    )

    fun normalizeDigits(value: String): String = buildString(value.length) {
        value.forEach { char ->
            val normalized = when (char) {
                in '\u0660'..'\u0669' -> ('0'.code + char.code - '\u0660'.code).toChar()
                in '\u06F0'..'\u06F9' -> ('0'.code + char.code - '\u06F0'.code).toChar()
                else -> char
            }
            append(normalized)
        }
    }

    fun extractIsbns(text: String): List<String> {
        val normalized = normalizeDigits(text).uppercase()
        return candidatePattern.findAll(normalized)
            .map { match -> match.value.filterNot { it.isWhitespace() || it == '-' } }
            .filter(::isValidIsbn)
            .distinct()
            .toList()
    }

    fun isValidIsbn(value: String): Boolean {
        val isbn = normalizeDigits(value)
            .filterNot { it.isWhitespace() || it == '-' }
            .uppercase()

        return when (isbn.length) {
            10 -> isValidIsbn10(isbn)
            13 -> isValidIsbn13(isbn)
            else -> false
        }
    }

    fun titleCandidates(text: String): List<String> = text
        .lineSequence()
        .map { it.trim().replace(Regex("""\s+"""), " ") }
        .filter { line ->
            line.length in 3..120 &&
                line.any(Char::isLetter) &&
                line.any { it in 'A'..'Z' || it in 'a'..'z' } &&
                extractIsbns(line).isEmpty()
        }
        .map { line ->
            line.replace(Regex("""^(?:title|book)\s*[:\-]\s*""", RegexOption.IGNORE_CASE), "").trim()
        }
        .filter(String::isNotBlank)
        .distinct()
        .take(5)
        .toList()

    private fun isValidIsbn10(isbn: String): Boolean {
        if (!isbn.take(9).all(Char::isDigit)) return false
        if (!isbn.last().isDigit() && isbn.last() != 'X') return false

        val sum = isbn.take(9).mapIndexed { index, digit ->
            (10 - index) * digit.digitToInt()
        }.sum() + (if (isbn.last() == 'X') 10 else isbn.last().digitToInt())
        return sum % 11 == 0
    }

    private fun isValidIsbn13(isbn: String): Boolean {
        if (!isbn.all(Char::isDigit) || !(isbn.startsWith("978") || isbn.startsWith("979"))) {
            return false
        }

        val weightedSum = isbn.take(12).mapIndexed { index, digit ->
            digit.digitToInt() * if (index % 2 == 0) 1 else 3
        }.sum()
        val checkDigit = (10 - weightedSum % 10) % 10
        return isbn.last().digitToInt() == checkDigit
    }
}
