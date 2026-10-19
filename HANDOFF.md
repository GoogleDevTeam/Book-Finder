# Book Finder — AI Handoff

Updated: 2026-10-19 (Asia/Riyadh)

## Branch and review status

The Android MVP and edition-safe matching are on main. PR #16 was merged on 2026-10-19. The multi-book reading-list follow-up is in draft PR #17 on feat/multi-book-reading-lists, based on main.

The user has found work on non-main branches difficult to access. Keep the README and this handoff current, make the dependency between the two PRs explicit, and do not merge either PR without explicit confirmation.

## Current behavior and follow-up

- Main contains the Kotlin/Jetpack Compose Android MVP: CameraX capture, on-device Latin ML Kit OCR, editable ISBN/title review, Google Books lookup, Room-backed single-book history, and retailer search links for Jarir, Amazon.sa, and Noon.
- Main also includes edition metadata and distinguishes exact ISBN matches from unverified ISBN responses and title candidates. Its Room schema is version 2 with a 1-to-2 migration.
- PR #17 implements issue #15: review multiple detected ISBN/title entries independently, choose a Google Books result per entry, and save only selected results together in a named local reading list. List items preserve lookup metadata; saved lists should reopen offline. Room schema version 3 adds the list and item tables.
- Single-book History saves remain supported. The app stays local-first: no scan-image upload, cloud sync, or custom backend.

## Verification status — do not overstate

The GitHub Actions workflow builds the debug APK, uploads it, and runs unit tests. Verify the check on the current PR head; a pass on an earlier commit does not verify later changes. The workflow does not exercise the Room migration or physical-device behavior. No emulator or physical-device verification has been completed. Physical scan flow and retailer routing remain open in issue #14.

## Next steps

1. Verify GitHub Actions passes for the latest draft PR #17 head; fix any build or unit-test failures.
2. Smoke-test scan, per-book review, saved-list relaunch/offline display, and retailer hand-offs on an emulator and physical Android phone. Record device and Android version; leave unrun checks open.
3. Do not merge PR #17 without explicit confirmation. Keep Arabic OCR, cloud image processing, release distribution, and motion polish out of scope unless requested.
