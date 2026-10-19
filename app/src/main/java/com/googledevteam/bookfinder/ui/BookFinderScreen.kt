package com.googledevteam.bookfinder.ui

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.googledevteam.bookfinder.camera.CameraCapture
import com.googledevteam.bookfinder.data.BookFinderDatabase
import com.googledevteam.bookfinder.data.BookHistoryRepository
import com.googledevteam.bookfinder.data.HistoryWithBook
import com.googledevteam.bookfinder.data.GoogleBooksRepository
import com.googledevteam.bookfinder.data.toBookResult
import com.googledevteam.bookfinder.domain.BookCandidate
import com.googledevteam.bookfinder.domain.BookResult
import com.googledevteam.bookfinder.domain.CandidateKind
import com.googledevteam.bookfinder.domain.IsbnParser
import com.googledevteam.bookfinder.domain.SearchRequestVersion
import com.googledevteam.bookfinder.retail.RetailerSearch
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.util.Date

private enum class FinderPage {
    SCAN,
    REVIEW,
    RESULTS,
    HISTORY,
}

@Composable
fun BookFinderScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val repository = remember { GoogleBooksRepository() }
    val database = remember { BookFinderDatabase.get(context) }
    val historyRepository = remember(database) { BookHistoryRepository(database) }
    val historyEntries by historyRepository.historyEntries.collectAsStateWithLifecycle(initialValue = emptyList())
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED,
        )
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { granted ->
        hasCameraPermission = granted
    }

    var page by remember { mutableStateOf(FinderPage.SCAN) }
    var candidates by remember { mutableStateOf(emptyList<BookCandidate>()) }
    var selectedIndex by remember { mutableIntStateOf(0) }
    var query by remember { mutableStateOf("") }
    var queryKind by remember { mutableStateOf(CandidateKind.ENGLISH_TITLE) }
    var results by remember { mutableStateOf(emptyList<BookResult>()) }
    var loading by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf<String?>(null) }
    var historySavedMessage by remember { mutableStateOf<String?>(null) }
    var retailerMessage by remember { mutableStateOf<String?>(null) }

    val searchRequestVersion = remember { SearchRequestVersion() }

    fun clearSearchState() {
        searchRequestVersion.invalidate()
        loading = false
        results = emptyList()
        message = null
        historySavedMessage = null
        retailerMessage = null
    }

    fun reviewRecognizedText(ocrText: String) {
        clearSearchState()
        val isbns = IsbnParser.extractIsbns(ocrText)
        val detected = if (isbns.isNotEmpty()) {
            isbns.map { BookCandidate(it, CandidateKind.ISBN) }
        } else {
            IsbnParser.titleCandidates(ocrText)
                .map { BookCandidate(it, CandidateKind.ENGLISH_TITLE) }
        }
        candidates = detected.ifEmpty {
            listOf(BookCandidate("", CandidateKind.ENGLISH_TITLE))
        }
        selectedIndex = 0
        query = candidates.first().value
        queryKind = candidates.first().kind
        message = if (detected.isEmpty()) {
            "No clear English title or ISBN was found. You can type a title below."
        } else {
            null
        }
        page = FinderPage.REVIEW
    }

    fun chooseCandidate(index: Int) {
        selectedIndex = index
        candidates.getOrNull(index)?.let { candidate ->
            query = candidate.value
            queryKind = candidate.kind
        }
    }

    fun searchBooks() {
        val requestVersion = searchRequestVersion.begin()
        val searchQuery = query.trim()
        val searchKind = queryKind
        loading = true
        message = null
        historySavedMessage = null
        retailerMessage = null
        results = emptyList()
        page = FinderPage.RESULTS

        scope.launch {
            try {
                val newResults = repository.search(searchQuery, searchKind)
                if (!searchRequestVersion.isCurrent(requestVersion)) return@launch
                results = newResults
                if (newResults.isEmpty()) message = "No matches found. Try editing the search."
            } catch (exception: Exception) {
                if (!searchRequestVersion.isCurrent(requestVersion)) return@launch
                message = exception.localizedMessage ?: "Search failed. Try again."
            } finally {
                if (searchRequestVersion.isCurrent(requestVersion)) loading = false
            }
        }
    }

    fun openRetailerSearch(retailer: RetailerSearch) {
        val searchTerm = query.trim()
        if (searchTerm.isBlank()) return

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(retailer.searchUrl(searchTerm)))
        try {
            context.startActivity(intent)
            retailerMessage = null
        } catch (_: ActivityNotFoundException) {
            retailerMessage = "No browser or retailer app is available to open ${retailer.label}."
        }
    }

    fun saveResult(book: BookResult) {
        val selectedTerm = query.trim()
        val selectedKind = queryKind
        if (selectedTerm.isBlank()) return
        historySavedMessage = null
        scope.launch {
            try {
                historyRepository.saveResult(book, selectedTerm, selectedKind)
                historySavedMessage = "Saved on this device. It will be available in History offline."
            } catch (exception: Exception) {
                historySavedMessage = exception.localizedMessage ?: "Could not save this result. Try again."
            }
        }
    }

    Scaffold(containerColor = MaterialTheme.colorScheme.background) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            AppHeader(
                title = when (page) {
                    FinderPage.SCAN -> "Find a book"
                    FinderPage.REVIEW -> "Check the text"
                    FinderPage.RESULTS -> "Book matches"
                    FinderPage.HISTORY -> "Scan history"
                },
                subtitle = when (page) {
                    FinderPage.SCAN -> "Scan a school list or enter a title."
                    FinderPage.REVIEW -> "Correct the ISBN or English title before searching."
                    FinderPage.RESULTS -> "Results come from Google Books."
                    FinderPage.HISTORY -> "Saved books stay on this device and work offline."
                },
            )

            when (page) {
                FinderPage.SCAN -> {
                    if (hasCameraPermission) {
                        CameraCapture(
                            onCaptureStarted = { clearSearchState() },
                            onTextRecognized = ::reviewRecognizedText,
                            onCaptureError = { message = it },
                            modifier = Modifier.fillMaxWidth(),
                        )
                        Text(
                            text = "The image is processed on this device. Only the ISBN or title you confirm is sent to Google Books.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    } else {
                        PermissionCard(
                            onRequestPermission = {
                                permissionLauncher.launch(Manifest.permission.CAMERA)
                            },
                        )
                    }
                    OutlinedButton(
                        onClick = {
                            clearSearchState()
                            candidates = listOf(BookCandidate("", CandidateKind.ENGLISH_TITLE))
                            selectedIndex = 0
                            query = ""
                            queryKind = CandidateKind.ENGLISH_TITLE
                            message = null
                            page = FinderPage.REVIEW
                        },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text("Enter a title instead")
                    }
                    OutlinedButton(
                        onClick = { page = FinderPage.HISTORY },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text("History (${historyEntries.size})")
                    }
                    message?.let { InlineMessage(it) }
                }

                FinderPage.REVIEW -> {
                    CandidateReview(
                        candidates = candidates,
                        selectedIndex = selectedIndex,
                        query = query,
                        queryKind = queryKind,
                        onQueryKindChanged = { queryKind = it },
                        message = message,
                        onSelectCandidate = ::chooseCandidate,
                        onQueryChanged = { query = it },
                        onSearch = ::searchBooks,
                        onBack = { page = FinderPage.SCAN },
                    )
                }

                FinderPage.RESULTS -> {
                    ResultsScreen(
                        results = results,
                        loading = loading,
                        message = message,
                        saveMessage = historySavedMessage,
                        retailerMessage = retailerMessage,
                        onOpenRetailer = ::openRetailerSearch,
                        onSaveResult = ::saveResult,
                        onEditSearch = { page = FinderPage.REVIEW },
                        onScanAgain = {
                            clearSearchState()
                            page = FinderPage.SCAN
                        },
                    )
                }

                FinderPage.HISTORY -> {
                    HistoryScreen(
                        entries = historyEntries,
                        onBack = { page = FinderPage.SCAN },
                    )
                }
            }
        }
    }
}

@Composable
private fun AppHeader(title: String, subtitle: String) {
    Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
        Text(
            text = "BOOK FINDER",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
        )
        Text(text = title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.SemiBold)
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun PermissionCard(onRequestPermission: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
    ) {
        Column(
            modifier = Modifier.padding(22.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text("Camera access", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
            Text(
                "Book Finder uses the camera only when you choose to scan a page. The image stays on this device.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Button(onClick = onRequestPermission) {
                Text("Allow camera")
            }
        }
    }
}

@Composable
private fun CandidateReview(
    candidates: List<BookCandidate>,
    selectedIndex: Int,
    query: String,
    queryKind: CandidateKind,
    onQueryKindChanged: (CandidateKind) -> Unit,
    message: String?,
    onSelectCandidate: (Int) -> Unit,
    onQueryChanged: (String) -> Unit,
    onSearch: () -> Unit,
    onBack: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        if (candidates.size > 1 || candidates.firstOrNull()?.value?.isNotBlank() == true) {
            Text("Choose the best match", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            candidates.forEachIndexed { index, candidate ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onSelectCandidate(index) }
                        .padding(horizontal = 4.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    RadioButton(
                        selected = selectedIndex == index,
                        onClick = { onSelectCandidate(index) },
                    )
                    Column(modifier = Modifier.padding(start = 6.dp)) {
                        Text(
                            if (candidate.kind == CandidateKind.ISBN) "ISBN" else "English title",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                        )
                        Text(candidate.value, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }

        OutlinedTextField(
            value = query,
            onValueChange = onQueryChanged,
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(if (queryKind == CandidateKind.ISBN) "ISBN" else "English title")
            },
            supportingText = {
                Text("Edit anything the camera misread.")
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
        )
        Text("Search by", style = MaterialTheme.typography.labelLarge)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(
                selected = queryKind == CandidateKind.ISBN,
                onClick = { onQueryKindChanged(CandidateKind.ISBN) },
                label = { Text("ISBN") },
            )
            FilterChip(
                selected = queryKind == CandidateKind.ENGLISH_TITLE,
                onClick = { onQueryKindChanged(CandidateKind.ENGLISH_TITLE) },
                label = { Text("English title") },
            )
        }
        message?.let { InlineMessage(it) }
        Button(
            onClick = onSearch,
            enabled = query.isNotBlank(),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Search Google Books")
        }
        TextButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text("Back to scan")
        }
    }
}

@Composable
private fun ResultsScreen(
    results: List<BookResult>,
    loading: Boolean,
    message: String?,
    saveMessage: String?,
    retailerMessage: String?,
    onOpenRetailer: (RetailerSearch) -> Unit,
    onSaveResult: (BookResult) -> Unit,
    onEditSearch: () -> Unit,
    onScanAgain: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        if (loading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 36.dp),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
            Text(
                "Looking up the confirmed ISBN or title…",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.bodyMedium,
            )
        } else if (results.isNotEmpty()) {
            results.forEach { book ->
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    BookResultCard(book)
                    OutlinedButton(
                        onClick = { onSaveResult(book) },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text("Save this result to history")
                    }
                }
            }
        } else if (message != null) {
            InlineMessage(message)
        }

        if (!loading) {
            Text(
                "Search this ISBN or title with a retailer",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                "Opens the retailer's search page. Prices and availability are provided by the retailer.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                RetailerSearch.entries.forEach { retailer ->
                    OutlinedButton(
                        onClick = { onOpenRetailer(retailer) },
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(horizontal = 4.dp),
                    ) {
                        Text(
                            text = retailer.label,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }
        }

        saveMessage?.let { InlineMessage(it) }
        retailerMessage?.let { InlineMessage(it) }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            OutlinedButton(onClick = onEditSearch, modifier = Modifier.weight(1f)) {
                Text("Edit search")
            }
            Button(onClick = onScanAgain, modifier = Modifier.weight(1f)) {
                Text("Scan again")
            }
        }
    }
}

@Composable
private fun HistoryScreen(
    entries: List<HistoryWithBook>,
    onBack: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text(
            "History is stored locally. Saved metadata remains viewable offline.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        if (entries.isEmpty()) {
            InlineMessage("No saved books yet. Save a result after a lookup to see it here.")
        } else {
            entries.forEach { entry ->
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        "${entry.history.lookupKind.lowercase().replace('_', ' ')}: ${entry.history.lookupTerm}",
                        style = MaterialTheme.typography.labelLarge,
                    )
                    Text(
                        DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT)
                            .format(Date(entry.history.scannedAt)),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    BookResultCard(entry.book.toBookResult())
                }
            }
        }
        TextButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text("Back to scan")
        }
    }
}

@Composable
private fun BookResultCard(book: BookResult) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (book.thumbnailUrl != null) {
                AsyncImage(
                    model = book.thumbnailUrl,
                    contentDescription = "Cover of ${book.title}",
                    modifier = Modifier
                        .size(width = 76.dp, height = 112.dp)
                        .clip(RoundedCornerShape(10.dp)),
                )
            } else {
                Surface(
                    modifier = Modifier.size(width = 76.dp, height = 112.dp),
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("Book", style = MaterialTheme.typography.labelLarge)
                    }
                }
            }
            Spacer(Modifier.width(14.dp))
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(book.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text(
                    text = book.authors.joinToString().ifBlank { "Author not listed" },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    "Google Books result",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

@Composable
private fun InlineMessage(message: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.secondaryContainer,
    ) {
        Text(
            text = message,
            modifier = Modifier.padding(14.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
        )
    }
}
