# Book Finder — AI Handoff

Updated: 2026-10-16 (Asia/Riyadh)

## Important: access and branch state

The user says it is difficult to access work outside the main branch. Keep handoff notes and important project status discoverable from main, and do not leave the user with only a feature-branch link. Explain clearly whenever app code is still on another branch.

The Android app and Room changes are currently on the pre-existing draft PR branch feat/android-mvp-vertical-slice, in PR #11, based on main. The PR is open and still a draft. Latest head at this handoff: fdc6be0e89bc7aa37e054f7b0c291426095c375e. The PR has not been merged; main currently contains the README and implementation guide plus this handoff note. Do not merge the PR automatically: the user asked for a handoff note, not a merge, and the new Android code has not been built or tested.

## Work currently in PR #11 (not yet in main)

- Kotlin/Jetpack Compose Android MVP: CameraX capture, on-device Latin ML Kit OCR, editable ISBN/title review, and Google Books metadata lookup.
- Room v1 database with separate book metadata and scan-history tables. The user explicitly saves a selected result; the lookup term/type/time and book metadata are written transactionally. The History screen observes Room data and displays saved entries offline.
- Camera images and history remain local; only a user-confirmed ISBN or title is sent to Google Books.
- Added unit tests for ISBN parsing and cached-metadata serialization, and updated README, implementation guide, and PR description.
- Room transactions are in BookHistoryRepository; the Compose screen observes its Flow and requests saves through the repository.

## Verification status — do not overstate

The Room work and added tests have not been run. This session had no Java, Gradle, Kotlin compiler, or Android SDK. GitHub showed zero Actions runs and zero commit statuses for the branch. No APK or emulator/physical-device test has been completed. The earlier PR description recorded that ./gradlew help passed before the Room changes and the original Android test task was blocked by the missing SDK; that is not verification of the current branch head.

## Next work, in order

1. Make the app work accessible on main after verification. First use JDK 17+, Android SDK Platform 36, and matching Build Tools; run ./gradlew testDebugUnitTest and ./gradlew assembleDebug, fix any failures, then test camera permission/capture/OCR/Google Books on an emulator and a physical Android device. Ask before merging the draft PR into main.
2. Verify Room behavior: explicitly save a match, restart the app, confirm newest-first History works offline, test empty state, and confirm clearing app data removes local history. Do not claim these checks passed until they run.
3. Add external retailer search hand-offs (no scraping or live price/stock claims). Suggested school-book order based on dedicated category coverage: Jarir first (https://www.jarir.com/sa-en/school-textbooks.html), Noon second (https://www.noon.com/saudi-en/books/school-books/category), Amazon.sa as a general ISBN/title-search fallback. This is not a sales/popularity ranking. Confirm Amazon.sa and Noon URL formats before shipping; the existing guide has Jarir's search pattern.
4. Keep Arabic OCR, cloud image processing, release distribution, and motion polish out of scope unless the user chooses them. Arabic Cloud Vision would change privacy and cost assumptions and needs explicit approval.

## User-facing handoff

The user asked that the main branch carry a note for the next AI and include the branch-access issue, updates, and remaining tasks. Start the next session by acknowledging that the app code is still only in draft PR #11, summarize the verification blocker, and work toward getting tested changes onto main rather than assuming the user can access the feature branch.