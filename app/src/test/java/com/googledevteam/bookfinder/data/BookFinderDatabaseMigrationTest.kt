package com.googledevteam.bookfinder.data

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.Room
import androidx.room.RoomDatabase
import com.googledevteam.bookfinder.domain.BookResult
import com.googledevteam.bookfinder.domain.CandidateKind
import com.googledevteam.bookfinder.domain.ConfirmedReadingListBook
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RuntimeEnvironment
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class BookFinderDatabaseMigrationTest {
    @Test
    fun versionTwoHistoryMigratesAndReadingListsReopenOffline() = runBlocking {
        val context: Context = RuntimeEnvironment.getApplication()
        val databaseName = "book-finder-migration-test.db"
        context.deleteDatabase(databaseName)

        val legacyDatabase = Room.databaseBuilder(
            context,
            VersionTwoBookFinderDatabase::class.java,
            databaseName,
        ).allowMainThreadQueries().build()
        val legacyBook = BookMetadataEntity(
            googleVolumeId = "legacy-volume",
            title = "Legacy Workbook",
            encodedAuthors = "A.+Author",
            thumbnailUrl = null,
            isbn10 = "0306406152",
            isbn13 = "9780306406157",
            publisher = "Example Press",
            publishedDate = "2024",
        )
        try {
            legacyDatabase.legacyDao().insertBook(legacyBook)
            legacyDatabase.legacyDao().insertHistory(
                ScanHistoryEntity(
                    lookupTerm = "9780306406157",
                    lookupKind = CandidateKind.ISBN.name,
                    bookId = legacyBook.googleVolumeId,
                    scannedAt = 1234L,
                ),
            )
        } finally {
            legacyDatabase.close()
        }

        val migratedDatabase = Room.databaseBuilder(
            context,
            BookFinderDatabase::class.java,
            databaseName,
        )
            .addMigrations(BookFinderDatabase.MIGRATION_1_2, BookFinderDatabase.MIGRATION_2_3)
            .allowMainThreadQueries()
            .build()

        try {
            val migratedRepository = BookHistoryRepository(migratedDatabase)
            val migratedHistory = migratedRepository.historyEntries.first()
            assertEquals(1, migratedHistory.size)
            assertEquals("Legacy Workbook", migratedHistory.single().book.title)
            assertEquals("9780306406157", migratedHistory.single().book.isbn13)
            assertEquals("Example Press", migratedHistory.single().book.publisher)

            val confirmedBooks = listOf(
                ConfirmedReadingListBook(
                    book = BookResult(
                        id = "volume-first",
                        title = "First Book",
                        authors = listOf("First Author"),
                        thumbnailUrl = null,
                        isbn13 = "9780306406157",
                    ),
                    lookupTerm = "9780306406157",
                    lookupKind = CandidateKind.ISBN,
                ),
                ConfirmedReadingListBook(
                    book = BookResult(
                        id = "volume-second",
                        title = "Second Book",
                        authors = listOf("Second Author"),
                        thumbnailUrl = null,
                    ),
                    lookupTerm = "World History",
                    lookupKind = CandidateKind.ENGLISH_TITLE,
                ),
            )
            val listId = migratedRepository.saveReadingList(
                name = "Grade 7 books",
                grade = "7",
                subject = "English",
                books = confirmedBooks,
            )
            migratedDatabase.close()

            val reopenedDatabase = Room.databaseBuilder(
                context,
                BookFinderDatabase::class.java,
                databaseName,
            )
                .addMigrations(BookFinderDatabase.MIGRATION_1_2, BookFinderDatabase.MIGRATION_2_3)
                .allowMainThreadQueries()
                .build()
            try {
                val reopenedRepository = BookHistoryRepository(reopenedDatabase)
                val savedLists = reopenedRepository.readingLists.first()
                assertEquals(1, savedLists.size)
                assertEquals(listId, savedLists.single().list.id)
                assertEquals("Grade 7 books", savedLists.single().list.name)
                assertEquals("7", savedLists.single().list.grade)
                assertEquals("English", savedLists.single().list.subject)
                assertEquals(2, savedLists.single().bookCount)

                val savedItems = reopenedRepository.readingListItems(listId).first()
                assertEquals(
                    listOf("volume-first", "volume-second"),
                    savedItems.map { it.book.googleVolumeId },
                )
                assertEquals("9780306406157", savedItems.first().item.lookupTerm)
                assertEquals(CandidateKind.ISBN.name, savedItems.first().item.lookupKind)
                assertEquals("World History", savedItems.last().item.lookupTerm)
                assertEquals("9780306406157", savedItems.first().book.isbn13)
            } finally {
                reopenedDatabase.close()
                context.deleteDatabase(databaseName)
            }
        } finally {
            if (migratedDatabase.isOpen) migratedDatabase.close()
        }
    }
}

@Dao
interface VersionTwoBookFinderDao {
    @Insert
    suspend fun insertBook(book: BookMetadataEntity)

    @Insert
    suspend fun insertHistory(entry: ScanHistoryEntity)
}

@Database(
    entities = [BookMetadataEntity::class, ScanHistoryEntity::class],
    version = 2,
    exportSchema = false,
)
abstract class VersionTwoBookFinderDatabase : RoomDatabase() {
    abstract fun legacyDao(): VersionTwoBookFinderDao
}
