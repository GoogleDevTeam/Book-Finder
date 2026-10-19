# Implementation Guide

The project includes a Kotlin/Compose Android app scaffold. The initial implementation covers camera capture, on-device Latin OCR, ISBN/title review, and Google Books lookup. Use the README and this guide as the current scope; the remaining steps describe verification and follow-on work. The 13 legacy planning files were removed from main; their earlier versions remain in Git history and do not add requirements. The app has not yet been built or verified on a physical Android device.

## 1. Open and build the Android app

1. Open the repository root in the current stable Android Studio release, or use the included Gradle wrapper from a shell.
2. The `app` module uses application ID `com.googledevteam.bookfinder`, minimum SDK 29, and compile/target SDK 36. Gradle dependencies and versions are declared in `app/build.gradle.kts`.
3. Use JDK 17 or newer. Run `./gradlew testDebugUnitTest` for the pure ISBN/title parser tests and `./gradlew assembleDebug` to produce a debug APK. The first build requires Android SDK Platform 36 and the matching Build Tools.
4. Install and run the APK on an emulator, then verify camera permission, capture, OCR, editing, and a Google Books lookup on a physical Android phone before distributing it.

## 2. Define the smallest app flow

Implement these Compose screens and a single clear path through them:

1. **Scan:** explain camera use, request permission when the user chooses Scan, and show a CameraX preview with a capture action.
2. **Review text:** show detected ISBN/title candidates so the user can correct OCR errors before lookup. Offer manual English-title entry if OCR finds no usable candidate.
3. **Book result:** show title, author, cover when available, and retailer search buttons.
4. **History:** list locally saved scans and cached metadata; cached entries remain viewable offline.

Use a Material 3 theme with tonal palettes, 12dp card corners, and 28dp bottom sheets; avoid heavy drop shadows. Enable predictive-back animations in the activity and navigation flow. Keep UI state in ViewModels and data work in repositories. Start with a small set of models: a scan candidate, normalized book metadata, and a timestamped local history record. Do not add accounts, cloud sync, analytics, or a custom service.

For scan/results motion, use a Compose LookaheadScope and spring-based transitions. When OCR finds a valid ISBN or title, animate a bounding box to the recognized text. On selection, use a shared-element transition from that text to the results card or bottom sheet, using the shared-transition APIs supported by the pinned Compose version.

## 3. Camera capture and on-device OCR

This initial implementation is in place. Complete the device and lifecycle checks below before treating camera capture as release-ready.

1. Use the native CameraXViewfinder composable, connected to the CameraX surface request, with ImplementationMode.EMBEDDED. Do not wrap the preview in AndroidView; embedded mode lets Compose clipping, shapes, bounds, and gestures work on the camera feed. Bind ImageCapture to the screen lifecycle for the still image used by OCR.
2. Capture a still image for each scan and process it with ML Kit Text Recognition v2 using the Latin model. Keep image processing and recognition off the UI thread.
3. Show a clear permission-denied, capture-failed, and no-text state with retry/manual-entry actions.
4. Do not upload or persist the captured image in the default flow. Release image and recognizer resources when the scan is finished or the screen is disposed.
5. Preserve the transform between captured-image coordinates and viewfinder coordinates so the OCR bounding box lands on the correct text. Test portrait/landscape rotation, focus, glare, low light, and small printed text on at least one physical device.

## 4. ISBN and title review

The initial implementation includes the pure ISBN parser and editable candidate-review flow. Extend and verify it against the cases below.

1. Normalize OCR whitespace and common ISBN separators. If OCR recognizes Arabic-Indic digits (٠١٢٣٤٥٦٧٨٩) or Eastern Arabic digits (۰۱۲۳۴۵۶۷۸۹), normalize them to ASCII digits before matching.
2. Search the text blocks for ISBN-10 and ISBN-13 candidates. Validate the ISBN checksum, allow X only as the final ISBN-10 character, and remove duplicates. A digit-pattern match alone is not sufficient.
3. If one or more valid ISBNs are found, present them for user confirmation and query each selected ISBN first.
4. If no valid ISBN is found, offer likely English title lines for review and editing. Do not send an unreviewed OCR paragraph as a title query.
5. Keep this parsing logic in a pure Kotlin component so unit tests can cover it without camera hardware.

## 5. Google Books metadata lookup

The initial implementation includes a lookup repository and results view. Verify provider behavior and error cases below before release.

1. Use the Google Books volumes search endpoint over HTTPS. Search ISBNs with the ISBN query operator and titles with the title query operator, URL-encoding the search value.
2. Parse the returned title, authors, thumbnail, ISBN-10/ISBN-13 identifiers, publisher, and publication date when present. Mark a result as an exact ISBN match only when its returned identifiers contain the queried ISBN; otherwise show that the ISBN is unverified. Label title-query results as candidates and let the user choose when multiple plausible results remain. Do not infer a specific edition from a title or publication date alone.
3. Handle no results, missing author or cover, timeouts, offline mode, quota responses, and other HTTP errors. Never treat an empty result as proof that a book does not exist.
4. Send only the selected ISBN or reviewed English title to Google Books. Never send the scan image to this endpoint.
5. Follow Google's current API identification and quota requirements. If an API key is required, restrict it to this Android package/signing certificate and the Books API, set quotas, and assume the key can be extracted from the APK. Do not put a service-account credential in the app.

## 6. Persist history and cached metadata with Room

The Room database keeps normalized book metadata and scan history locally. Version 2 adds nullable ISBN, publisher, and publication-date columns; the 1-to-2 migration preserves saved books. Version 3 adds named reading lists and ordered list items with a 2-to-3 migration. Creating a list transactionally saves its name, optional grade and subject/course, and only the books the user explicitly selected from search results; each item preserves the reviewed lookup term and kind. Saved lists and book metadata are read back from Room and remain available offline. Single-result saves to History remain available. Cover URLs are stored as text rather than copying image bytes into the database. No scan image, cloud sync, or custom backend is used.

A Robolectric test creates a version-2 database and verifies migration preserves history and cached metadata, then saves a reading list, closes/reopens the database, and checks the list and item order offline. Before release, still verify History ordering and app-data clearing on a device. If the schema changes, increment the database version and add a migration; do not mark migration checks as passed until run.

## 7. Build retailer search links, not a price scraper

RetailerSearch.searchUrl builds encoded query URLs for Jarir, Amazon.sa, and Noon; results remain available even if Google Books returns no match. The results screen opens the HTTPS URL with Android ACTION_VIEW, allowing Android to resolve an installed retailer app link or a browser.

- Jarir: https://www.jarir.com/sa-en/catalogsearch/result?q={encoded-query} (from the product brief).
- Amazon.sa: https://www.amazon.sa/s?k={encoded-query}; a live public search fetch returned results for a sample term.
- Noon: https://www.noon.com/saudi-en/search/?q={encoded-query}; a live fetch did not confirm search results, so verify this destination and app/browser routing on an Android device before release.

Unit tests cover each URL shape, reserved-character encoding, and blank-query rejection. Keep the destinations configurable; do not discover or update them by crawling retailer pages. The app stops at handing off the search; it never parses retailer pages or presents prices, stock, shipping, or ebook availability as app data.

## 8. Treat Arabic OCR as an explicit optional change

ML Kit Text Recognition v2's on-device model does not recognize Arabic. Keep the default release limited to ISBN recognition where available and English-title fallback.

If Arabic title extraction is mandatory, get approval for the changed privacy/cost boundary before implementation. Then integrate Google Cloud Vision OCR using the smallest necessary image crop, Arabic language hints, clear in-app notice and consent, API restrictions, quotas, and error handling. This option sends image content to Google Cloud Vision; it is not part of the default local-only flow. Never embed a service-account key. If a safe, restricted direct Android API setup is not available, defer Arabic OCR rather than silently adding a server or proxy.

## 9. Verify behavior before release

- **ISBN parsing:** valid and invalid ISBN-10/13 checksums, final X, spaces/hyphens, duplicate matches, OCR noise, and normalized Arabic-Indic digits.
- **Title parsing:** empty text, numeric-only lines, mixed Arabic/English text, and user-edited titles.
- **Google Books:** exact ISBN result, a different-edition result, missing identifiers, title-only candidates, missing publisher/date/author/thumbnail, ambiguous titles, no items, timeout, offline, and quota/error responses.
- **Room and reading lists:** the Robolectric suite verifies v2→v3 migration preserves history and cached metadata and that saved lists reopen with their metadata and item order. Device checks should still verify app relaunch/offline access, clearing app data, and that unreviewed OCR entries are never persisted as list items.
- **UI and device:** camera permission grant/denial, EMBEDDED viewfinder clipping/gesture behavior, OCR bounding-box alignment, review/edit flow, predictive-back navigation, shared-element/spring transitions, no-network history, and retailer handoff with and without a retailer app installed.
- **Privacy boundary:** in the default configuration, verify that only a selected ISBN/title is sent to Google Books and that no camera image or history data is uploaded.

Run the Gradle unit-test and Android test tasks configured in the project, then make a release build and install it on a physical Android phone. Record actual test outcomes; do not mark planned device checks as passed until they have run.

## 10. Distribute the private test build with Firebase App Distribution

1. Create or select a Firebase project, register the Android application using the exact application ID, and add the five intended testers (or a private tester group).
2. Create a signed release APK. Keep the keystore and passwords outside GitHub, and never commit them to the repository. App Distribution is only the test-delivery channel; it does not require a custom app updater.
3. For the first release, upload the APK in the Firebase Console, add concise release notes, and send the tester invitation. Use the Firebase App Tester installation flow described in the current Firebase documentation.
4. Ask each tester to install the build and report device/Android version plus any scan issue. Fix release-blocking defects, upload the next build through App Distribution, and verify the update path.
5. Add Gradle/CI upload automation only if repeated releases justify it. Do not distribute through GitHub Releases or build a custom APK update mechanism.

## Release acceptance checklist

- The app targets minimum SDK 29, uses the native embedded CameraXViewfinder, and presents the specified Material 3 shapes and motion.
- A user can capture a page, review a validated ISBN or English title, and correct the candidate before lookup.
- A matching Google Books result displays the available title, author, and cover, and can be cached locally.
- History works offline, and a retailer action opens a search page without scraping or presenting claimed live prices.
- The default flow uploads no scan image; the Arabic Cloud Vision path is absent unless separately approved and clearly disclosed.
- A signed APK has been installed successfully by the five Firebase App Distribution testers.

## Official references

- Jetpack Compose: https://developer.android.com/develop/ui/compose
- Material 3 for Compose: https://developer.android.com/develop/ui/compose/designsystems/material3
- CameraX: https://developer.android.com/training/camerax
- ML Kit Text Recognition v2 for Android: https://developers.google.com/ml-kit/vision/text-recognition/v2/android
- Google Books API: https://developers.google.com/books/docs/v1/using
- Google Cloud Vision OCR: https://cloud.google.com/vision/docs/ocr
- Room persistence library: https://developer.android.com/training/data-storage/room
- Firebase App Distribution for Android: https://firebase.google.com/docs/app-distribution/android/distribute-console
