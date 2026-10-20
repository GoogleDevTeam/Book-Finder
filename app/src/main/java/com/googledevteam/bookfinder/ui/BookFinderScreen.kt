package com.googledevteam.bookfinder.ui

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.googledevteam.bookfinder.camera.CameraCapture
import com.googledevteam.bookfinder.data.BookFinderDatabase
import com.googledevteam.bookfinder.data.BookHistoryRepository
import com.googledevteam.bookfinder.data.HistoryWithBook
import com.googledevteam.bookfinder.data.ReadingListItemWithBook
import com.googledevteam.bookfinder.data.ReadingListSummary
import com.googledevteam.bookfinder.data.GoogleBooksRepository
import com.googledevteam.bookfinder.data.toBookResult
import com.googledevteam.bookfinder.domain.CandidateStatus
import com.googledevteam.bookfinder.domain.ScannedBookDraft
import com.googledevteam.bookfinder.domain.SearchRequestVersion
import com.googledevteam.bookfinder.domain.candidatesFromOcrText
import com.googledevteam.bookfinder.domain.confirmedReadingListBooks
import com.googledevteam.bookfinder.domain.withEditedQuery
import com.googledevteam.bookfinder.domain.withKindChanged
import com.googledevteam.bookfinder.domain.withSearchCancelled
import com.googledevteam.bookfinder.domain.withSearchResults
import com.googledevteam.bookfinder.domain.withSelectedBook
import com.googledevteam.bookfinder.domain.BookResult
import com.googledevteam.bookfinder.domain.CandidateKind
import com.googledevteam.bookfinder.domain.withLookupMatch
import com.googledevteam.bookfinder.retail.RetailerSearch
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.flowOf
import java.text.DateFormat
import java.util.Date

private fun Context.findActivity(): Activity? {
    var current: Context? = this
    while (current != null) {
        if (current is Activity) return current
        current = (current as? ContextWrapper)?.baseContext
    }
    return null
}

private enum class FinderPage {
    SCAN,
    REVIEW,
    RESULTS,
    HISTORY,
    READING_LISTS,
    READING_LIST_DETAIL,
}

@Composable
fun BookFinderScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val repository = remember { GoogleBooksRepository() }
    val database = remember { BookFinderDatabase.get(context) }
    val historyRepository = remember(database) { BookHistoryRepository(database) }
    val historyEntries by historyRepository.historyEntries.collectAsStateWithLifecycle(initialValue = emptyList())
    val readingLists by historyRepository.readingLists.collectAsStateWithLifecycle(initialValue = emptyList())
    var selectedReadingListId by remember { mutableStateOf<Long?>(null) }
    val readingListItemsFlow = remember(historyRepository, selectedReadingListId) {
        selectedReadingListId?.let(historyRepository::readingListItems)
            ?: flowOf(emptyList<ReadingListItemWithBook>())
    }
    val readingListItems by readingListItemsFlow.collectAsStateWithLifecycle(initialValue = emptyList())
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED,
        )
    }
    var hasRequestedCameraPermission by rememberSaveable { mutableStateOf(false) }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { granted ->
        hasRequestedCameraPermission = true
        hasCameraPermission = granted
    }
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner, context) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                hasCameraPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_GRANTED
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }
    val activity = context.findActivity()
    val shouldShowCameraRationale = activity?.let {
        ActivityCompat.shouldShowRequestPermissionRationale(it, Manifest.permission.CAMERA)
    } ?: false
    val cameraSettingsRequired = hasRequestedCameraPermission && !hasCameraPermission && !shouldShowCameraRationale

    var activeCaptureId by remember { mutableStateOf<Long?>(null) }
    var page by remember { mutableStateOf(FinderPage.SCAN) }
    var drafts by remember { mutableStateOf(emptyList<ScannedBookDraft>()) }
    var nextDraftId by remember { mutableStateOf(1L) }
    var activeDraftId by remember { mutableStateOf<Long?>(null) }
    var query by remember { mutableStateOf("") }
    var queryKind by remember { mutableStateOf(CandidateKind.ENGLISH_TITLE) }
    var results by remember { mutableStateOf(emptyList<BookResult>()) }
    var loading by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf<String?>(null) }
    var historySavedMessage by remember { mutableStateOf<String?>(null) }
    var retailerMessage by remember { mutableStateOf<String?>(null) }
    var readingListName by remember { mutableStateOf("") }
    var readingListGrade by remember { mutableStateOf("") }
    var readingListSubject by remember { mutableStateOf("") }
    var savingReadingList by remember { mutableStateOf(false) }
    var listManagementMessage by remember { mutableStateOf<String?>(null) }

    val searchRequestVersion = remember { SearchRequestVersion() }

    fun clearSearchState() {
        searchRequestVersion.invalidate()
        loading = false
        results = emptyList()
        message = null
        historySavedMessage = null
        retailerMessage = null
        activeDraftId = null
        drafts = drafts.map { it.withSearchCancelled() }
    }

    fun newDraft(value: String, kind: CandidateKind, titleHint: String? = null): ScannedBookDraft {
        val draft = ScannedBookDraft(id = nextDraftId, query = value, kind = kind, titleHint = titleHint)
        nextDraftId += 1
        return draft
    }

    fun updateDraft(id: Long, transform: (ScannedBookDraft) -> ScannedBookDraft) {
        drafts = drafts.map { draft -> if (draft.id == id) transform(draft) else draft }
    }

    fun resetListFields() {
        readingListName = ""
        readingListGrade = ""
        readingListSubject = ""
        historySavedMessage = null
        message = null
        activeDraftId = null
    }

    fun reviewRecognizedText(ocrText: String) {
        clearSearchState()
        val detected = candidatesFromOcrText(ocrText)
        drafts = detected.map { newDraft(it.value, it.kind, it.titleHint) }.ifEmpty {
            listOf(newDraft("", CandidateKind.ENGLISH_TITLE))
        }
        resetListFields()
        query = drafts.first().query
        queryKind = drafts.first().kind
        message = if (detected.isEmpty()) {
            "No clear English title or ISBN was found. Add or type each book below."
        } else {
            null
        }
        page = FinderPage.REVIEW
    }

    fun startManualEntry() {
        activeCaptureId = null
        clearSearchState()
        drafts = listOf(newDraft("", CandidateKind.ENGLISH_TITLE))
        resetListFields()
        query = ""
        queryKind = CandidateKind.ENGLISH_TITLE
        page = FinderPage.REVIEW
    }

    fun searchCandidate(id: Long) {
        val draft = drafts.firstOrNull { it.id == id } ?: return
        val term = draft.query.trim()
        if (term.isBlank()) {
            message = "Enter an ISBN or English title before searching."
            return
        }
        val requestVersion = searchRequestVersion.begin()
        activeDraftId = id
        query = term
        queryKind = draft.kind
        loading = true
        message = null
        historySavedMessage = null
        retailerMessage = null
        results = emptyList()
        updateDraft(id) { it.copy(status = CandidateStatus.SEARCHING) }
        page = FinderPage.RESULTS
        scope.launch {
            try {
                val foundResults = repository.search(term, draft.kind)
                if (!searchRequestVersion.isCurrent(requestVersion)) return@launch
                results = foundResults
                updateDraft(id) { it.withSearchResults(foundResults) }
                if (foundResults.isEmpty()) message = "No matches found. Edit this entry or try another search."
            } catch (exception: Exception) {
                if (!searchRequestVersion.isCurrent(requestVersion)) return@launch
                updateDraft(id) { it.withSearchResults(emptyList()) }
                message = exception.localizedMessage ?: "Search failed. Try again."
            } finally {
                if (searchRequestVersion.isCurrent(requestVersion)) loading = false
            }
        }
    }

    fun chooseBookForList(book: BookResult) {
        val draftId = activeDraftId ?: return
        updateDraft(draftId) { it.withSelectedBook(book) }
        activeDraftId = null
        results = emptyList()
        message = null
        page = FinderPage.REVIEW
    }

    fun openRetailerSearchFor(searchTerm: String, retailer: RetailerSearch) {
        val normalizedTerm = searchTerm.trim()
        if (normalizedTerm.isBlank()) return

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(retailer.searchUrl(normalizedTerm)))
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

    fun saveReadingList() {
        val confirmedBooks = drafts.confirmedReadingListBooks()
        if (readingListName.isBlank()) {
            message = "Give this reading list a name before saving."
            return
        }
        if (confirmedBooks.isEmpty()) {
            message = "Choose at least one confirmed book before saving the list."
            return
        }
        savingReadingList = true
        message = null
        scope.launch {
            try {
                selectedReadingListId = historyRepository.saveReadingList(
                    name = readingListName,
                    grade = readingListGrade,
                    subject = readingListSubject,
                    books = confirmedBooks,
                )
                page = FinderPage.READING_LIST_DETAIL
            } catch (exception: Exception) {
                message = exception.localizedMessage ?: "Could not save this reading list. Try again."
            } finally {
                savingReadingList = false
            }
        }
    }

    fun renameSavedList(listId: Long, name: String) {
        listManagementMessage = null
        scope.launch {
            try {
                historyRepository.renameReadingList(listId, name)
                listManagementMessage = "Reading list renamed."
            } catch (exception: Exception) {
                listManagementMessage = exception.localizedMessage ?: "Could not rename this reading list. Try again."
            }
        }
    }

    fun deleteSavedList(listId: Long) {
        listManagementMessage = null
        scope.launch {
            try {
                if (historyRepository.deleteReadingList(listId)) {
                    if (selectedReadingListId == listId) selectedReadingListId = null
                    listManagementMessage = "Reading list deleted."
                } else {
                    listManagementMessage = "This reading list is no longer available."
                }
            } catch (exception: Exception) {
                listManagementMessage = exception.localizedMessage ?: "Could not delete this reading list. Try again."
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
                    FinderPage.SCAN -> "Find books"
                    FinderPage.REVIEW -> "Review your list"
                    FinderPage.RESULTS -> "Book matches"
                    FinderPage.HISTORY -> "Scan history"
                    FinderPage.READING_LISTS -> "Reading lists"
                    FinderPage.READING_LIST_DETAIL -> "Reading list"
                },
                subtitle = when (page) {
                    FinderPage.SCAN -> "Scan an English school list or enter books by hand."
                    FinderPage.REVIEW -> "Correct each entry, choose its match, then save the confirmed books together."
                    FinderPage.RESULTS -> "Choose the matching edition for this entry."
                    FinderPage.HISTORY -> "Saved books stay on this device and work offline."
                    FinderPage.READING_LISTS -> "Your named school lists are stored on this device."
                    FinderPage.READING_LIST_DETAIL -> "Saved book details remain available offline."
                },
            )

            when (page) {
                FinderPage.SCAN -> {
                    if (hasCameraPermission) {
                        CameraCapture(
                            onCaptureStarted = { captureId ->
                                activeCaptureId = captureId
                                clearSearchState()
                            },
                            onTextRecognized = { captureId, text ->
                                if (page == FinderPage.SCAN && activeCaptureId == captureId) {
                                    activeCaptureId = null
                                    reviewRecognizedText(text)
                                }
                            },
                            onCaptureError = { captureId, error ->
                                if (activeCaptureId == captureId) {
                                    activeCaptureId = null
                                    message = error
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                        )
                        Text(
                            text = "The image is processed on this device. Only the ISBN or title you confirm is sent to Google Books.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    } else {
                        PermissionCard(
                            settingsRequired = cameraSettingsRequired,
                            onRequestPermission = {
                                hasRequestedCameraPermission = true
                                permissionLauncher.launch(Manifest.permission.CAMERA)
                            },
                            onOpenSettings = {
                                try {
                                    val settingsIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                        data = Uri.fromParts("package", context.packageName, null)
                                    }
                                    context.startActivity(settingsIntent)
                                } catch (_: ActivityNotFoundException) {
                                    message = "Open Book Finder's app settings to enable camera access."
                                }
                            },
                        )
                    }
                    OutlinedButton(
                        onClick = {
                            startManualEntry()
                        },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text("Enter books manually")
                    }
                    OutlinedButton(
                        onClick = { activeCaptureId = null; page = FinderPage.HISTORY },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text("History (${historyEntries.size})")
                    }
                    OutlinedButton(
                        onClick = { activeCaptureId = null; page = FinderPage.READING_LISTS },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text("Reading lists (${readingLists.size})")
                    }
                    message?.let { InlineMessage(it) }
                }

                FinderPage.REVIEW -> {
                    CandidateReview(
                        drafts = drafts,
                        listName = readingListName,
                        grade = readingListGrade,
                        subject = readingListSubject,
                        message = message,
                        saving = savingReadingList,
                        onListNameChanged = { readingListName = it; message = null },
                        onGradeChanged = { readingListGrade = it },
                        onSubjectChanged = { readingListSubject = it },
                        onQueryChanged = { id, value ->
                            clearSearchState()
                            updateDraft(id) { it.withEditedQuery(value, it.kind) }
                            message = null
                        },
                        onKindChanged = { id, kind ->
                            val currentDraft = drafts.firstOrNull { it.id == id }
                            if (currentDraft != null && currentDraft.kind != kind) {
                                clearSearchState()
                                updateDraft(id) { it.withKindChanged(kind) }
                                message = null
                            }
                        },
                        onSearchCandidate = ::searchCandidate,
                        onAddCandidate = { title ->
                            drafts = drafts + newDraft(title.orEmpty(), CandidateKind.ENGLISH_TITLE)
                        },
                        onRemoveCandidate = { id -> if (drafts.size > 1) drafts = drafts.filterNot { it.id == id } },
                        onChangeMatch = { id -> updateDraft(id) { it.withEditedQuery(it.query, it.kind) } },
                        onSaveList = ::saveReadingList,
                        onBack = { page = FinderPage.SCAN },
                    )
                }

                FinderPage.RESULTS -> {
                    ResultsScreen(
                        searchTerm = query,
                        results = results,
                        loading = loading,
                        message = message,
                        saveMessage = historySavedMessage,
                        retailerMessage = retailerMessage,
                        retailerTitleFallback = drafts.firstOrNull { it.id == activeDraftId }?.titleHint,
                        onOpenRetailer = ::openRetailerSearchFor,
                        onSaveResult = ::saveResult,
                        onUseForList = ::chooseBookForList,
                        onEditSearch = {
                            clearSearchState()
                            page = FinderPage.REVIEW
                        },
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

                FinderPage.READING_LISTS -> {
                    ReadingListsScreen(
                        lists = readingLists,
                        message = listManagementMessage,
                        onOpenList = { id ->
                            selectedReadingListId = id
                            listManagementMessage = null
                            page = FinderPage.READING_LIST_DETAIL
                        },
                        onRenameList = ::renameSavedList,
                        onDeleteList = ::deleteSavedList,
                        onBack = { page = FinderPage.SCAN },
                    )
                }

                FinderPage.READING_LIST_DETAIL -> {
                    val selectedList = readingLists.firstOrNull { it.list.id == selectedReadingListId }?.list
                    ReadingListDetailScreen(
                        list = selectedList,
                        items = readingListItems,
                        retailerMessage = retailerMessage,
                        onOpenRetailerSearch = ::openRetailerSearchFor,
                        onBack = { page = FinderPage.READING_LISTS },
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
private fun PermissionCard(
    settingsRequired: Boolean,
    onRequestPermission: () -> Unit,
    onOpenSettings: () -> Unit,
) {
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
                if (settingsRequired) {
                    "Camera access is disabled for Book Finder. Enable it in app settings, or enter books manually."
                } else {
                    "Book Finder uses the camera only when you choose to scan a page. The image stays on this device."
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            if (settingsRequired) {
                Button(onClick = onOpenSettings) {
                    Text("Open app settings")
                }
            } else {
                Button(onClick = onRequestPermission) {
                    Text("Allow camera")
                }
            }
        }
    }
}

@Composable
private fun CandidateReview(
    drafts: List<ScannedBookDraft>,
    listName: String,
    grade: String,
    subject: String,
    message: String?,
    saving: Boolean,
    onListNameChanged: (String) -> Unit,
    onGradeChanged: (String) -> Unit,
    onSubjectChanged: (String) -> Unit,
    onQueryChanged: (Long, String) -> Unit,
    onKindChanged: (Long, CandidateKind) -> Unit,
    onSearchCandidate: (Long) -> Unit,
    onAddCandidate: (String?) -> Unit,
    onRemoveCandidate: (Long) -> Unit,
    onChangeMatch: (Long) -> Unit,
    onSaveList: () -> Unit,
    onBack: () -> Unit,
) {
    val confirmedCount = drafts.count { it.status == CandidateStatus.MATCHED && it.selectedBook != null }
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text("Name this reading list", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        OutlinedTextField(
            value = listName,
            onValueChange = onListNameChanged,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("List name (required)") },
            placeholder = { Text("e.g. Grade 7 English") },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = grade,
                onValueChange = onGradeChanged,
                modifier = Modifier.weight(1f),
                label = { Text("Grade (optional)") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
            )
            OutlinedTextField(
                value = subject,
                onValueChange = onSubjectChanged,
                modifier = Modifier.weight(1f),
                label = { Text("Subject/course") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
            )
        }
        Text(
            "Review each detected book separately. Only a book you select as a match is included when you save.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        drafts.forEachIndexed { index, draft ->
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 1.dp,
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text("Book ${index + 1}", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                        Text(
                            when (draft.status) {
                                CandidateStatus.NEEDS_REVIEW -> "Needs review"
                                CandidateStatus.SEARCHING -> "Searching…"
                                CandidateStatus.NO_RESULT -> "No result"
                                CandidateStatus.MATCHED -> "Matched"
                            },
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                    if (drafts.size > 1) {
                        TextButton(onClick = { onRemoveCandidate(draft.id) }) {
                            Text("Remove this entry")
                        }
                    }
                    OutlinedTextField(
                        value = draft.query,
                        onValueChange = { onQueryChanged(draft.id, it) },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(if (draft.kind == CandidateKind.ISBN) "ISBN" else "English title") },
                        supportingText = { Text("Correct any OCR errors before searching.") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                    )
                    draft.titleHint?.let { titleHint ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = "Detected nearby title: " + titleHint,
                                modifier = Modifier.weight(1f),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            TextButton(onClick = { onAddCandidate(titleHint) }) {
                                Text("Add separately")
                            }
                        }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(
                            selected = draft.kind == CandidateKind.ISBN,
                            onClick = { onKindChanged(draft.id, CandidateKind.ISBN) },
                            label = { Text("ISBN") },
                        )
                        FilterChip(
                            selected = draft.kind == CandidateKind.ENGLISH_TITLE,
                            onClick = { onKindChanged(draft.id, CandidateKind.ENGLISH_TITLE) },
                            label = { Text("English title") },
                        )
                    }
                    val selectedBook = draft.selectedBook
                    if (draft.status == CandidateStatus.MATCHED && selectedBook != null) {
                        BookResultCard(selectedBook)
                        OutlinedButton(
                            onClick = { onChangeMatch(draft.id) },
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text("Change selected book")
                        }
                    } else {
                        if (draft.status == CandidateStatus.NO_RESULT) {
                            InlineMessage("No result for this entry. Edit it and search again, or leave it out of the list.")
                        }
                        Button(
                            onClick = { onSearchCandidate(draft.id) },
                            enabled = draft.query.isNotBlank() && draft.status != CandidateStatus.SEARCHING,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text(if (draft.status == CandidateStatus.NO_RESULT) "Search again" else "Search this book")
                        }
                    }
                }
            }
        }
        OutlinedButton(onClick = { onAddCandidate(null) }, modifier = Modifier.fillMaxWidth()) {
            Text("Add another book manually")
        }
        message?.let { InlineMessage(it) }
        Button(
            onClick = onSaveList,
            enabled = !saving && listName.isNotBlank() && confirmedCount > 0,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(if (saving) "Saving reading list…" else "Save reading list ($confirmedCount books)")
        }
        TextButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text("Back to scan")
        }
    }
}

@Composable
private fun ResultsScreen(
    searchTerm: String,
    results: List<BookResult>,
    loading: Boolean,
    message: String?,
    saveMessage: String?,
    retailerMessage: String?,
    retailerTitleFallback: String?,
    onOpenRetailer: (String, RetailerSearch) -> Unit,
    onSaveResult: (BookResult) -> Unit,
    onUseForList: (BookResult) -> Unit,
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
                    Button(
                        onClick = { onUseForList(book) },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text("Select this book for the reading list")
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
                    val titleOnly = retailer.requiresTitleOnly(searchTerm)
                    val retailerQuery = retailer.searchTermFor(
                        searchTerm,
                        retailerTitleFallback ?: results.firstOrNull()?.title,
                    )
                    OutlinedButton(
                        enabled = retailerQuery != null,
                        onClick = { retailerQuery?.let { onOpenRetailer(it, retailer) } },
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(horizontal = 4.dp),
                    ) {
                        Text(
                            text = if (titleOnly) retailer.label + "\nTitle only" else retailer.label,
                            maxLines = 2,
                            textAlign = TextAlign.Center,
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
                    val lookupKind = runCatching {
                        CandidateKind.valueOf(entry.history.lookupKind)
                    }.getOrDefault(CandidateKind.ENGLISH_TITLE)
                    BookResultCard(
                        entry.book.toBookResult().withLookupMatch(entry.history.lookupTerm, lookupKind),
                    )
                }
            }
        }
        TextButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text("Back to scan")
        }
    }
}

@Composable
private fun ReadingListsScreen(
    lists: List<ReadingListSummary>,
    message: String?,
    onOpenList: (Long) -> Unit,
    onRenameList: (Long, String) -> Unit,
    onDeleteList: (Long) -> Unit,
    onBack: () -> Unit,
) {
    var renameTarget by remember { mutableStateOf<ReadingListSummary?>(null) }
    var renameValue by remember { mutableStateOf("") }
    var deleteTarget by remember { mutableStateOf<ReadingListSummary?>(null) }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            "Reading lists are stored on this device and can be opened offline.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        message?.let { InlineMessage(it) }
        if (lists.isEmpty()) {
            InlineMessage("No reading lists yet. Scan a school list, confirm books, and save them together.")
        } else {
            lists.forEach { summary ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onOpenList(summary.list.id) },
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 1.dp,
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Text(summary.list.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        val details = listOfNotNull(summary.list.grade, summary.list.subject).filter(String::isNotBlank)
                        if (details.isNotEmpty()) {
                            Text(details.joinToString(" • "), style = MaterialTheme.typography.bodySmall)
                        }
                        Text(
                            "${summary.bookCount} ${if (summary.bookCount == 1) "book" else "books"}",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.End,
                ) {
                    TextButton(
                        onClick = {
                            renameValue = summary.list.name
                            renameTarget = summary
                        },
                    ) {
                        Text("Rename")
                    }
                    TextButton(onClick = { deleteTarget = summary }) {
                        Text("Delete", color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
        TextButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text("Back to scan")
        }
    }

    renameTarget?.let { target ->
        AlertDialog(
            onDismissRequest = { renameTarget = null },
            title = { Text("Rename reading list") },
            text = {
                OutlinedTextField(
                    value = renameValue,
                    onValueChange = { renameValue = it },
                    label = { Text("List name") },
                    singleLine = true,
                )
            },
            confirmButton = {
                TextButton(
                    enabled = renameValue.isNotBlank() && renameValue.trim() != target.list.name,
                    onClick = {
                        onRenameList(target.list.id, renameValue)
                        renameTarget = null
                    },
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { renameTarget = null }) {
                    Text("Cancel")
                }
            },
        )
    }

    deleteTarget?.let { target ->
        AlertDialog(
            onDismissRequest = { deleteTarget = null },
            title = { Text("Delete reading list?") },
            text = {
                Text("Delete \"${target.list.name}\" and its saved items from this device? This cannot be undone.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDeleteList(target.list.id)
                        deleteTarget = null
                    },
                ) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { deleteTarget = null }) {
                    Text("Cancel")
                }
            },
        )
    }
}

@Composable
private fun ReadingListDetailScreen(
    list: com.googledevteam.bookfinder.data.ReadingListEntity?,
    items: List<ReadingListItemWithBook>,
    retailerMessage: String?,
    onOpenRetailerSearch: (String, RetailerSearch) -> Unit,
    onBack: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        if (list == null) {
            InlineMessage("This reading list is unavailable. Return to your saved lists.")
        } else {
            Text(list.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
            val details = listOfNotNull(list.grade, list.subject).filter(String::isNotBlank)
            if (details.isNotEmpty()) {
                Text(details.joinToString(" • "), style = MaterialTheme.typography.bodyMedium)
            }
            Text(
                "Saved on this device. Book details are available offline.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            if (items.isEmpty()) {
                InlineMessage("This list has no saved books.")
            } else {
                items.forEach { item ->
                    val kind = runCatching { CandidateKind.valueOf(item.item.lookupKind) }
                        .getOrDefault(CandidateKind.ENGLISH_TITLE)
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        BookResultCard(
                            item.book.toBookResult().withLookupMatch(item.item.lookupTerm, kind),
                        )
                        Text(
                            "Search this book with a retailer",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            RetailerSearch.entries.forEach { retailer ->
                                val titleOnly = retailer.requiresTitleOnly(item.item.lookupTerm)
                                val retailerQuery = retailer.searchTermFor(item.item.lookupTerm, item.book.title)
                                OutlinedButton(
                                    enabled = retailerQuery != null,
                                    onClick = { retailerQuery?.let { onOpenRetailerSearch(it, retailer) } },
                                    modifier = Modifier.weight(1f),
                                    contentPadding = PaddingValues(horizontal = 4.dp),
                                ) {
                                    Text(
                                        text = if (titleOnly) retailer.label + "\nTitle only" else retailer.label,
                                        maxLines = 2,
                                        textAlign = TextAlign.Center,
                                        overflow = TextOverflow.Ellipsis,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        retailerMessage?.let { InlineMessage(it) }
        TextButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text("Back to reading lists")
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
                    text = when (book.matchType) {
                        com.googledevteam.bookfinder.domain.BookMatchType.EXACT_ISBN -> "ISBN confirmed in Google Books"
                        com.googledevteam.bookfinder.domain.BookMatchType.ISBN_UNVERIFIED -> "ISBN not listed — verify this edition"
                        com.googledevteam.bookfinder.domain.BookMatchType.TITLE_CANDIDATE -> "Title candidate — confirm edition"
                    },
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                )
                book.isbn13?.let {
                    Text("ISBN-13: $it", style = MaterialTheme.typography.bodySmall)
                }
                book.isbn10?.let {
                    Text("ISBN-10: $it", style = MaterialTheme.typography.bodySmall)
                }
                book.publisher?.let {
                    Text("Publisher: $it", style = MaterialTheme.typography.bodySmall)
                }
                book.publishedDate?.let {
                    Text("Published: $it", style = MaterialTheme.typography.bodySmall)
                }
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
