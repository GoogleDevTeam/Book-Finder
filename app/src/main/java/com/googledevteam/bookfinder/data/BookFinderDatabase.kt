package com.googledevteam.bookfinder.data

import android.content.Context
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

data class HistoryWithBook(
    @Embedded val history: ScanHistoryEntity,
    @Relation(parentColumn = "bookId", entityColumn = "googleVolumeId")
    val book: BookMetadataEntity,
)

@Dao
interface HistoryDao {
    @Upsert
    suspend fun upsertBook(book: BookMetadataEntity)

    @Insert
    suspend fun insertHistory(entry: ScanHistoryEntity)

    @Transaction
    @Query("SELECT * FROM scan_history ORDER BY scannedAt DESC")
    fun observeHistory(): Flow<List<HistoryWithBook>>
}

@Database(
    entities = [BookMetadataEntity::class, ScanHistoryEntity::class],
    version = 2,
    exportSchema = false,
)
abstract class BookFinderDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao

    companion object {
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE book_metadata ADD COLUMN isbn10 TEXT")
                database.execSQL("ALTER TABLE book_metadata ADD COLUMN isbn13 TEXT")
                database.execSQL("ALTER TABLE book_metadata ADD COLUMN publisher TEXT")
                database.execSQL("ALTER TABLE book_metadata ADD COLUMN publishedDate TEXT")
            }
        }

        @Volatile
        private var instance: BookFinderDatabase? = null

        fun get(context: Context): BookFinderDatabase = instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(
                context.applicationContext,
                BookFinderDatabase::class.java,
                "book-finder.db",
            ).addMigrations(MIGRATION_1_2).build().also { instance = it }
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
