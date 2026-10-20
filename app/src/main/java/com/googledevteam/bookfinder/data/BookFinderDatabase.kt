package com.googledevteam.bookfinder.data

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Database
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Transaction
import androidx.room.Upsert
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.googledevteam.bookfinder.domain.BookResult
import kotlinx.coroutines.flow.Flow
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Entity(tableName = "book_metadata")
data class BookMetadataEntity(
    @PrimaryKey val googleVolumeId: String,
    val title: String,
    val encodedAuthors: String,
    val thumbnailUrl: String?,
    val isbn10: String? = null,
    val isbn13: String? = null,
    val publisher: String? = null,
    val publishedDate: String? = null,
)

@Entity(
    tableName = "scan_history",
    foreignKeys = [
        ForeignKey(
            entity = BookMetadataEntity::class,
            parentColumns = ["googleVolumeId"],
            childColumns = ["bookId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index(value = ["bookId"])],
)
data class ScanHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val lookupTerm: String,
    val lookupKind: String,
    val bookId: String,
    val scannedAt: Long,
)

@Entity(tableName = "reading_lists")
data class ReadingListEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val grade: String? = null,
    val subject: String? = null,
    val createdAt: Long,
)

@Entity(
    tableName = "reading_list_items",
    foreignKeys = [
        ForeignKey(
            entity = ReadingListEntity::class,
            parentColumns = ["id"],
            childColumns = ["listId"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = BookMetadataEntity::class,
            parentColumns = ["googleVolumeId"],
            childColumns = ["bookId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index(value = ["listId"]), Index(value = ["bookId"])],
)
data class ReadingListItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val listId: Long,
    val bookId: String,
    val lookupTerm: String,
    val lookupKind: String,
    val position: Int,
)

data class HistoryWithBook(
    @Embedded val history: ScanHistoryEntity,
    @Relation(parentColumn = "bookId", entityColumn = "googleVolumeId")
    val book: BookMetadataEntity,
)

data class ReadingListSummary(
    @Embedded val list: ReadingListEntity,
    @ColumnInfo(name = "bookCount") val bookCount: Int,
)

data class ReadingListItemWithBook(
    @Embedded val item: ReadingListItemEntity,
    @Relation(parentColumn = "bookId", entityColumn = "googleVolumeId")
    val book: BookMetadataEntity,
)

@Dao
interface HistoryDao {
    @Upsert
    suspend fun upsertBook(book: BookMetadataEntity)

    @Insert
    suspend fun insertHistory(entry: ScanHistoryEntity)

    @Insert
    suspend fun insertReadingList(list: ReadingListEntity): Long

    @Query("UPDATE reading_lists SET name = :name WHERE id = :listId")
    suspend fun renameReadingList(listId: Long, name: String): Int

    @Query("DELETE FROM reading_lists WHERE id = :listId")
    suspend fun deleteReadingList(listId: Long): Int

    @Insert
    suspend fun insertReadingListItem(item: ReadingListItemEntity)

    @Transaction
    @Query("SELECT * FROM scan_history ORDER BY scannedAt DESC")
    fun observeHistory(): Flow<List<HistoryWithBook>>

    @Query(
        "SELECT reading_lists.*, COUNT(reading_list_items.id) AS bookCount " +
            "FROM reading_lists LEFT JOIN reading_list_items " +
            "ON reading_list_items.listId = reading_lists.id " +
            "GROUP BY reading_lists.id ORDER BY reading_lists.createdAt DESC",
    )
    fun observeReadingLists(): Flow<List<ReadingListSummary>>

    @Transaction
    @Query("SELECT * FROM reading_list_items WHERE listId = :listId ORDER BY position ASC, id ASC")
    fun observeReadingListItems(listId: Long): Flow<List<ReadingListItemWithBook>>
}

@Database(
    entities = [BookMetadataEntity::class, ScanHistoryEntity::class, ReadingListEntity::class, ReadingListItemEntity::class],
    version = 3,
    exportSchema = false,
)
abstract class BookFinderDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao

    companion object {
        internal val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE book_metadata ADD COLUMN isbn10 TEXT")
                database.execSQL("ALTER TABLE book_metadata ADD COLUMN isbn13 TEXT")
                database.execSQL("ALTER TABLE book_metadata ADD COLUMN publisher TEXT")
                database.execSQL("ALTER TABLE book_metadata ADD COLUMN publishedDate TEXT")
            }
        }

        internal val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "CREATE TABLE reading_lists (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        "name TEXT NOT NULL, grade TEXT, subject TEXT, createdAt INTEGER NOT NULL)",
                )
                database.execSQL(
                    "CREATE TABLE reading_list_items (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        "listId INTEGER NOT NULL, bookId TEXT NOT NULL, lookupTerm TEXT NOT NULL, " +
                        "lookupKind TEXT NOT NULL, position INTEGER NOT NULL, " +
                        "FOREIGN KEY(listId) REFERENCES reading_lists(id) ON UPDATE NO ACTION ON DELETE CASCADE, " +
                        "FOREIGN KEY(bookId) REFERENCES book_metadata(googleVolumeId) ON UPDATE NO ACTION ON DELETE CASCADE)",
                )
                database.execSQL("CREATE INDEX index_reading_list_items_listId ON reading_list_items(listId)")
                database.execSQL("CREATE INDEX index_reading_list_items_bookId ON reading_list_items(bookId)")
            }
        }

        @Volatile
        private var instance: BookFinderDatabase? = null

        fun get(context: Context): BookFinderDatabase = instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(
                context.applicationContext,
                BookFinderDatabase::class.java,
                "book-finder.db",
            ).addMigrations(MIGRATION_1_2, MIGRATION_2_3).build().also { instance = it }
        }
    }
}

fun BookResult.toCachedEntity(): BookMetadataEntity = BookMetadataEntity(
    googleVolumeId = id,
    title = title,
    encodedAuthors = authors.joinToString(separator = ",") {
        URLEncoder.encode(it, StandardCharsets.UTF_8.name())
    },
    thumbnailUrl = thumbnailUrl,
    isbn10 = isbn10,
    isbn13 = isbn13,
    publisher = publisher,
    publishedDate = publishedDate,
)

fun BookMetadataEntity.toBookResult(): BookResult = BookResult(
    id = googleVolumeId,
    title = title,
    authors = encodedAuthors.takeIf(String::isNotBlank)
        ?.split(",")
        ?.map { URLDecoder.decode(it, StandardCharsets.UTF_8.name()) }
        ?: emptyList(),
    thumbnailUrl = thumbnailUrl,
    isbn10 = isbn10,
    isbn13 = isbn13,
    publisher = publisher,
    publishedDate = publishedDate,
)
