# Book Finder — AI Handoff

Updated: 2026-10-16 (Asia/Riyadh)

## Important: access and branch state

The user says it is difficult to access work outside the main branch. Keep handoff notes and important project status discoverable from main, and do not leave the user with only a feature-branch link. Explain clearly whenever app code is still on another branch.

The Android app, Room changes, and retailer search hand-offs are on the open draft PR #11 branch `feat/android-mvp-vertical-slice`, based on `main`. Current head: `03257f6c3df0fd1ce7084f46d8e88d3387b1b142`. The PR remains unmerged; main contains the specifications and this handoff, not the Android app. Do not merge until the current head has been built and tested; ask for confirmation before merging.

## Work currently in PR #11 (not yet in main)

- Kotlin/Jetpack Compose Android MVP: CameraX capture, on-device Latin ML Kit OCR, editable ISBN/title review, and Google Books metadata lookup.
- Room v1 local cache and scan history. The user explicitly saves a selected result; lookup term/type/time and book metadata are written transactionally and shown offline.
- Camera images and history remain local; only a user-confirmed ISBN or title is sent to Google Books.
- Retailer search actions for Jarir, Amazon.sa, and Noon. Search terms are URL-encoded and opened with Android ACTION_VIEW; results remain available even when Google Books has no match. The app does not scrape pages or display retailer prices/availability.
- Added unit tests for ISBN parsing, cached-metadata round trips, and retailer search URL construction/encoding. Updated README and implementation guide on the PR branch.
- Room writes are in `BookHistoryRepository`; screen state observes its Flow.

## Verification status — do not overstate

This workspace has no Java, Gradle, or Android SDK, so the tests and APK build were not run here. GitHub Actions returned no runs for the PR branch; the current head's combined status is pending with no status records. No emulator or physical-device test has been completed. A live fetch of Amazon.sa's `/s?k=` search URL returned results for a sample query. The Noon search URL did not return clear search results in this environment; verify it and app/browser routing on an Android device before release.

## Next work, in order

1. In an Android environment with JDK 17+, Android SDK Platform 36, and matching Build Tools, run `./gradlew testDebugUnitTest` and `./gradlew assembleDebug`; fix failures and record the actual results.
2. Smoke-test camera permission/capture/OCR, Google Books lookup, and retailer hand-offs for Jarir, Amazon.sa, and Noon on an emulator and physical Android device. Confirm Noon opens the intended search results and verify app/browser routing.
3. Verify Room save, restart, newest-first offline history, empty state, and local-data clearing. Do not mark any of these checks as passed until run.
4. After verification, make the tested app code accessible on main. Do not merge the draft PR without user confirmation.
5. Keep Arabic OCR, cloud image processing, release distribution, and motion polish out of scope unless the user chooses them. Arabic Cloud Vision changes privacy and cost assumptions and requires explicit approval.

## User-facing handoff

The user asked to continue existing work without more questions. The retailer hand-off implementation is committed to PR #11, but it remains on the draft branch and is not present on main. Lead with the branch-access limitation and the concrete Android verification steps; do not claim build/device success.
