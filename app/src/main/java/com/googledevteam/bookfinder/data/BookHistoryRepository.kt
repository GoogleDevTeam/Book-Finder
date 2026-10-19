package com.googledevteam.bookfinder.data

import androidx.room.withTransaction
import com.googledevteam.bookfinder.domain.BookResult
import com.googledevteam.bookfinder.domain.CandidateKind
import com.googledevteam.bookfinder.domain.ConfirmedReadingListBook
import kotlinx.coroutines.flow.Flow

class BookHistoryRepository(private val database: BookFinderDatabase) {
    private val historyDao = database.historyDao()

    val historyEntries: Flow<List<HistoryWithBook>> = historyDao.observeHistory()
    val readingLists: Flow<List<ReadingListSummary>> = historyDao.observeReadingLists()

    fun readingListItems(listId: Long): Flow<List<ReadingListItemWithBook>> =
        historyDao.observeReadingListItems(listId)

    suspend fun saveResult(book: BookResult, lookupTerm: String, lookupKind: CandidateKind) {
        require(lookupTerm.isNotBlank()) { "A reviewed lookup term is required." }
        database.withTransaction {
            historyDao.upsertBook(book.toCachedEntity())
            historyDao.insertHistory(
                ScanHistoryEntity(
                    lookupTerm = lookupTerm,
                    lookupKind = lookupKind.name,
                    bookId = book.id,
                    scannedAt = System.currentTimeMillis(),
                ),
            )
        }
    }

    suspend fun saveReadingList(
        name: String,
        grade: String,
        subject: String,
        books: List<ConfirmedReadingListBook>,
    ): Long {
        val normalizedName = name.trim()
        require(normalizedName.isNotBlank()) { "A reading list name is required." }
        require(books.isNotEmpty()) { "At least one confirmed book is required." }
        require(books.all { it.lookupTerm.isNotBlank() && it.book.id.isNotBlank() }) {
            "Every saved book needs a confirmed lookup and result."
        }
        return database.withTransaction {
            val listId = historyDao.insertReadingList(
                ReadingListEntity(
                    name = normalizedName,
                    grade = grade.trim().takeIf(String::isNotBlank),
                    subject = subject.trim().takeIf(String::isNotBlank),
                    createdAt = System.currentTimeMillis(),
                ),
            )
            books.forEachIndexed { index, confirmed ->
                historyDao.upsertBook(confirmed.book.toCachedEntity())
                historyDao.insertReadingListItem(
                    ReadingListItemEntity(
                        listId = listId,
                        bookId = confirmed.book.id,
                        lookupTerm = confirmed.lookupTerm.trim(),
                        lookupKind = confirmed.lookupKind.name,
                        position = index,
                    ),
                )
            }
            listId
        }
    }
}
