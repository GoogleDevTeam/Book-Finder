# Book Finder MVP Plan

## Product focus

Help users identify the correct English-language educational book edition from a printed school list or syllabus, save the confirmed books locally, and open useful retailer searches. The primary MVP is the native Android app.

## Priorities

### P0 — Verify the current scan-to-book flow on Android

- Build and install the app in an Android environment; exercise the flow on an emulator and a physical phone.
- Test real printed pages with ISBNs and English titles, including camera permission, capture, OCR correction, low light, no-network behavior, and local history.
- Confirm the user can review and correct the recognized ISBN/title before lookup, and that the default flow sends only the confirmed lookup term—not the camera image—to Google Books.
- Verify Jarir, Amazon.sa, and Noon hand-offs from Android, both with and without retailer apps installed.

### P1 — Make textbook edition matching trustworthy

- Prefer a validated exact ISBN match over a title-only match.
- Show available identifiers and edition details—ISBN, publisher, edition/year, title, and author—so users can distinguish similar textbooks.
- Clearly label title-only results as candidates; require the user to choose or correct an ambiguous match before saving it.
- Allow optional user-entered context such as grade, subject, or curriculum; do not infer it from incomplete book metadata.

### P1 — Support a complete school-list workflow

- Test whether one scan can identify multiple book entries; improve separation and review where needed.
- Let users correct and confirm each entry, then save related books together as a named course or reading list.
- Keep saved lists and history on-device and available offline; provide manual entry when OCR cannot identify a usable title or ISBN.

### P2 — Validate with real school lists before expanding scope

- Run a small hands-on pilot using real English educational book lists.
- Record where the flow fails—OCR, edition matching, missing metadata, or retailer routing—and fix the most frequent blockers first.
- Keep retailer actions labeled as search links. Do not claim live price, stock, shipping, or availability unless a verified data source is added later.

## Out of scope for the MVP

- A general-purpose AI chatbot or AI-generated book matches.
- A separate website before the Android flow is validated.
- Retailer scraping, price comparison, accounts, cloud sync, or uploading scan images.

AI may be considered later as an optional aid for interpreting messy lists or natural-language requests, but book identity and edition selection should remain grounded in verified metadata and user confirmation. Any cloud OCR or image processing requires a separate privacy and cost decision.

## MVP acceptance

A user can scan a printed English educational book list, review and correct multiple recognized entries, confirm the right edition—preferably by ISBN—save the list for offline access, and open a relevant retailer search for each book without the app claiming unverified prices or stock.
