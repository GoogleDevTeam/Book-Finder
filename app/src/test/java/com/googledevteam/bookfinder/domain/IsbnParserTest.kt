package com.googledevteam.bookfinder.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class IsbnParserTest {
    @Test
    fun extractsAndValidatesIsbn10AndIsbn13() {
        assertEquals(
            listOf("0306406152", "9780306406157"),
            IsbnParser.extractIsbns("ISBN 0-306-40615-2\n978-0-306-40615-7"),
        )
    }

    @Test
    fun acceptsIsbn10WithXCheckDigit() {
        assertTrue(IsbnParser.isValidIsbn("0-8044-2957-X"))
        assertEquals(listOf("080442957X"), IsbnParser.extractIsbns("ISBN: 0-8044-2957-X"))
    }

    @Test
    fun normalizesArabicIndicAndEasternArabicDigits() {
        assertEquals("9780306406157", IsbnParser.normalizeDigits("٩٧٨٠٣٠٦٤٠٦١٥٧"))
        assertEquals("9780306406157", IsbnParser.normalizeDigits("۹۷۸۰۳۰۶۴۰۶۱۵۷"))
        assertEquals(listOf("9780306406157"), IsbnParser.extractIsbns("ISBN ٩٧٨٠٣٠٦٤٠٦١٥٧"))
    }

    @Test
    fun rejectsIncorrectChecksumsAndLongDigitRuns() {
        assertFalse(IsbnParser.isValidIsbn("0306406153"))
        assertFalse(IsbnParser.isValidIsbn("9780306406158"))
        assertTrue(IsbnParser.extractIsbns("12345678901234").isEmpty())
    }

    @Test
    fun offersEnglishTitleLinesButNotArabicOnlyOrIsbnLines() {
        assertEquals(
            listOf("Algebra Foundations", "World History"),
            IsbnParser.titleCandidates(
                "Algebra Foundations\nالعربية\nISBN 978-0-306-40615-7\nWorld History",
            ),
        )
    }
}
