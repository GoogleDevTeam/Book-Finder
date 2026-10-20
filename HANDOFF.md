# Book Finder - AI Handoff

Updated: 2026-10-20 (Asia/Riyadh)

## Access and branch status

- Keep this handoff current on main because the user has found work on non-main branches difficult to access.
- Main contains the Android MVP: CameraX capture, on-device Latin-script OCR, ISBN/title review, Google Books metadata lookup, Room-backed single-book history, and retailer search links.
- PR #16 (edition-safe matching) was merged on 2026-10-19. PR #17, the multi-book reading-list follow-up, remains open and in draft on branch feat/multi-book-reading-lists.
- The debug APK the user is testing was built from PR #17 source-code commit 5bac526be359afedfe4948114e21f53e35c63998 by successful workflow run #39331341117. Later handoff-only commits do not alter that APK. Do not merge PR #17 without explicit user confirmation.

## Current behavior and scope

- PR #17 implements independent review and lookup of multiple ISBN/title entries, explicit selection of each book result, and saving selected books in a named local reading list. Saved list metadata and item order should persist offline. Single-book History remains supported.
- The app is local-first: scan images are processed on-device by the default flow; no cloud sync or custom backend. The app sends a user-selected ISBN or English title to Google Books.
- Retailer links hand off searches to Jarir, Amazon.sa, and Noon. The app does not scrape retailer pages or report live prices/stock.
- Default on-device ML Kit OCR supports Latin text, not Arabic text extraction. Keep cloud OCR, accounts, sync, scraping, and price aggregation out of scope unless explicitly requested.

## CI verification

- GitHub Actions run #39331341117 on the current PR #17 head completed successfully for the Android build and unit-test workflow. The workflow builds/uploads a debug APK and runs unit tests.
- The user reports installing the app after being directed to that run's debug APK artifact. Device model and Android version have not been provided.
- Unit tests cover Room migration and reading-list persistence; they do not replace emulator or physical-device testing.

## User-reported device test log

Test input supplied in chat:

    GRADE 7 READING LIST
    The Hunger Games - Suzanne Collins - ISBN 978-0-439-02352-8
    Charlotte's Web - E. B. White
    A Wrinkle in Time - Madeleine L'Engle

Observed scan/review result: FAIL. The heading "GRADE 7 READING LIST" appeared as a book candidate. "Charlotte's Web" and its author "E. B. White" appeared as separate book candidates. The user has not reported the complete candidate list, so outcomes for the other sample entries are unknown. Expected behavior is to ignore the heading and keep each book's title and author together as one editable entry.

Observed Google Books result: FAIL. Search showed: "Book search failed (HTTP 503). Try again." The retry outcome has not yet been reported. The cause is not established; do not label it a key, quota, or global-service issue without further evidence.

Not yet reported as tested: camera permission-denied recovery; low-light and no-text scans; title-only and no-match recovery; successful per-book lookup; saving a named reading list; force-close/reopen and offline list display; no-network lookup recovery; Jarir, Amazon.sa, and Noon routing (including app/browser behavior).

## Next steps

1. Address scan candidate grouping/filtering: ignore list headings and associate author lines with their book title; add regression coverage for this sample.
2. Retry one Google Books lookup with connectivity enabled. If HTTP 503 persists, investigate the Google Books API/project configuration and quota privately; never put the API key in this file, chat, or logs.
3. Continue physical-device validation for camera, scan/review, saving and reopening lists offline, error recovery, and all retailer hand-offs. Record device/Android version and observed results; leave unrun checks open in issue #14.
4. Keep PR #17 in draft until review and verification are complete. Do not merge without explicit user confirmation.
