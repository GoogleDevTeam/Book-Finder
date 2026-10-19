package com.googledevteam.bookfinder.data

import androidx.room.withTransaction
import com.googledevteam.bookfinder.domain.BookResult
import com.googledevteam.bookfinder.domain.CandidateKind
import kotlinx.coroutines.flow.Flow

class BookHistoryRepository(private val database: BookFinderDatabase) {
    private val historyDao = database.historyDao()

    val historyEntries: Flow<List<HistoryWithBook>> = historyDao.observeHistory()

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
}
