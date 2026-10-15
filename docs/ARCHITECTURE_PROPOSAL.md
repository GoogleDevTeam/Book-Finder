# Proposed architecture — planning only

**Status:** Proposal for owner review; not an approved design and not authorization to implement.
**Prepared:** 2026-10-15
**Scope authority:** docs/APP_PLAN.md and docs/CHANGE_CONTROL.md

## 1. Fixed scope and limits

This proposal stays inside the confirmed Book-Finder scope: a private Android APK for about five invited users, one shared group list, printed or typed/on-screen English lists (one format per page), user review of extracted entries, book/edition matching, and physical-book or ebook options with Saudi delivery/region uncertainty shown honestly. The $0/no-payment-card rule is confirmed. The app has no implementation approval: the owner must approve the complete plan and explicitly say to begin before any app source, provider integration, backend, build, or release is created.

The current plan has 16 stable requirements and 38 planned, unrun test cases. Thirty effective P0 questions remain. A plan can reduce foreseeable errors and define safe failure behavior; it cannot guarantee zero defects, perfect OCR, accurate live stock, or an APK that cannot be copied or reverse-engineered.

## 2. Recommended shape (all items below are proposals)

For a five-person Android-only app, start with a single native Android client and, if the owner accepts it, one small authenticated API service organized as a modular monolith. Do not begin with microservices: they add deployment, identity, logging, and cost surfaces without a scale need established by this project. Keep the hosting, identity provider, database, OCR engine, and retailer integrations unselected until their no-card eligibility, terms, and user data flows are verified.

### Android client

- Preferred stack to evaluate: Kotlin with Jetpack Compose, lifecycle-aware state, coroutines, and explicit presentation/domain/data boundaries. This is a candidate, not a chosen implementation stack.
- Capture/import one supported page, validate file type/size/orientation, and run English text recognition on-device where the tested OCR option supports the selected Android versions. ML Kit Text Recognition is one candidate; its bundled model reduces dependence on runtime model downloads but increases APK size. Benchmark image quality, cold start, memory, and battery on owner-selected devices before choosing it.
- Present extracted rows for correction before any search. Keep text-reading confidence separate from book-match confidence. For a scanned row with missing, invalid, or unreadable ISBN, block that row's search until a valid user-confirmed ISBN is entered, as required by BF-REQ-003.
- Treat a title/work, edition, translation, format, and ISBN as distinct evidence. Keep matching explainable: preserve source identifiers and field-level evidence; do not silently merge editions. Closest-edition ranking remains blocked on Q-19.
- Use only the camera/photo access required by the approved flow; prefer Android's system photo picker for imports where suitable rather than broad media access. Exact permissions and supported OS versions remain Q-55/Q-58 decisions.
- Do not persist images, extracted text, or search history by default until the owner resolves Q-14/Q-15/Q-42/Q-43. The confirmed shared-list requirement still needs a decision on what is saved, for how long, and how deletion works.

### Optional authenticated service

If private GitHub releases, membership/device revocation, or shared-list synchronization remain in scope, a server-side trust boundary is the safer design: the APK cannot safely hold a GitHub/provider secret, and an offline copy cannot receive immediate revocation. Use one narrowly scoped API with explicit modules for identity/authorization, provider routing, provider configuration, and release metadata. Keep the chosen host and database open until a complete no-card/no-overage path is verified on the owner's actual account.

- Candidate flow: each invited person authenticates through a maintained identity provider; the service maps the identity/device to an owner-approved member record; protected requests are authorized server-side and denied after revocation. A native OAuth/OIDC authorization-code-with-PKCE flow is a candidate where supported; the identity product and device limit are not selected.
- Store only data required for the approved shared-list and access behavior. Define data fields, retention, deletion, backup, and incident response before creating a database. Do not log titles, ISBNs, photos, tokens, or user identities unless separately approved and strictly necessary.
- Keep GitHub/provider credentials and signing material in server-side secret storage, never in the APK, client config, logs, or GitHub. Use least-privilege, short-lived credentials and rotate/revoke them. The private repository itself is not a runtime secret store.
- If the owner rejects a backend, explicitly decide which shared-list, private-release, external-search, and revocation requirements are removed or acceptably weakened; do not pretend a client-only APK can protect a shared secret or revoke offline access.

## 3. Search and provider boundaries

Use separate interfaces and result types for book metadata, physical retail offers, and ebook-store offers. A metadata match is not a purchasable offer. Each provider result should carry provider identity, the checked time, the fields actually returned, and a status that distinguishes not configured, not checked, successful with no match, partial results, and provider failure.

- Google Books is a metadata candidate: its API supports ISBN searches and country-specific sale information for the Google eBookstore. That signal applies to Google's store only; it does not establish physical stock/shipping, account purchase eligibility, device/DRM compatibility, or access across all ebook stores. Google Books terms also require user notice about submitted content potentially being made publicly available through Google services; review the exact data flow and notice before selection.
- Open Library is a possible human-oriented metadata lookup, not a default production backend. Its current API guidance asks developers to register the use case, recommends identifying requests, lists 1 request/second by default and 3/second for identified requests, and says its APIs are not intended as a third-party data backend or high-traffic commercial infrastructure. Confirm the private five-person app fits its terms before relying on it.
- Amazon.sa has an official Creators API path, but current prerequisites include target-marketplace Associates enrollment and at least 10 qualifying sales in the previous 30 days, plus API registration and credentials. Owner eligibility and app-use terms are unverified.
- Noon documentation reviewed describes seller, integrator, and partner APIs; it does not establish public consumer product search. Do not scrape shopper pages or misuse seller APIs.
- Until a provider has a permitted method, eligibility, cost, and coverage verified, mark it unsupported and show that it was not checked. Never convert provider failure into a no-match result.

For every physical offer, keep delivery status separate from stock/price and require owner-approved evidence before using a confirmed-Saudi-delivery label. For ebooks, expose the named store's country-specific availability and the checked time, but label unknown account, DRM, or entitlement conditions as unknown. The evidence standards and presentation remain open at Q-31–Q-39.

## 4. Security, resilience, and performance controls

- Validate all input at the boundary: ISBN-10/13 check digits, image MIME/size, provider response schema, URL scheme/host, and numeric values. Use parameterized database operations and safe text rendering; treat images, OCR text, provider responses, and GitHub configuration as untrusted input.
- Enforce HTTPS, least-privilege API scopes, server-side authorization on every protected operation, bounded request/response sizes, timeouts, rate limits, and controlled retries with jitter. Do not retry writes or send duplicate provider calls without idempotency.
- Handle each source independently with bounded concurrency and progressive results. Preserve explicit states for unavailable, rate-limited, stale, partial, and not configured. Any cache must show its age and must never make stale shipping/stock look current.
- Keep launch responsive: on-device OCR and update checks must not block startup indefinitely. Use cancellation, lifecycle-aware work, and clear offline/degraded states. Set numeric latency/accuracy thresholds only after Q-55/Q-58/Q-64/Q-65/Q-75 and representative test data are available.
- Protect local session material with Android-protected storage; avoid custom password storage. Define lock/logout, shared-device handling, lost-device response, session expiry, and revocation behavior before implementation.
- Obfuscation and tamper checks can raise effort, not guarantee secrecy. Assume an APK can be copied and inspected; place no secret or authorization decision that must remain secret solely in the client.

## 5. Verification and release gates

Before implementation approval, close or explicitly defer all P0/P1/P2 questions per OPEN_QUESTIONS.md; resolve the data flow and threat model; verify each selected provider's official access method; and assign owners/devices/criteria to the planned test matrix. Planned tests are not passed tests.

For each later code batch, run the applicable formatter/linter, unit and integration tests, Android build, dependency/license and vulnerability checks, secret scanning, and targeted security tests before commit. Use deterministic provider fixtures for routine tests; live provider calls are opt-in contract checks and must not expose real book data in logs. Before a release, verify APK signature/package identity, hash, version code, permissions, Android compatibility, update/recovery path, and private distribution authorization.

Android's developer-verification documentation describes a rollout beginning September 30, 2026 in Brazil, Indonesia, Singapore, and Thailand, with global expansion for certified devices in 2027. Saudi Arabia is not in that initial list, but the future distribution plan must re-check applicability. A no-fee student/hobbyist account path is documented for some developers distributing outside Google Play; eligibility, identity requirements, package registration, and timing are not yet verified for this owner.

## 6. Main-only change and deployment workflow

- Work only on the existing main branch. Never create or use a topic, temporary, release, or backup branch or worktree; never force-push. Before each batch, read the latest main commit and every affected file, then prepare one focused, internally consistent change.
- For documentation, validate question-ID uniqueness/status counts, requirement/test mappings, links, source claims, and absence of secrets. For code, complete local tests/security checks before committing; do not rely on a post-push workflow as the only gate. Keep commits small and focused, then verify GitHub main points to the intended commit and its files match the checked content.
- Add a concise PROJECT_LOG.md checkpoint after every 2–3 substantive steps/batches with the exact commit, verification, unresolved decisions, and next safe step. Do not copy private book data or credentials into the log.
- Main is the only code source of truth; no alternate branch or PR workflow. A commit to main is not production approval. Never automatically publish an APK or change production services from a main push. Obtain the owner's explicit approval for each consequential release/deployment; distribute only a signed, owner-approved artifact and preserve update identity.

## 7. Decisions still required

At minimum, the plan gate still needs the unresolved effective P0 questions listed in OPEN_QUESTIONS.md. Research has narrowed Q-23/Q-25/Q-37 but has not selected providers or closed them. The next unanswered sequential owner question remains Q-03. Do not implement, configure accounts, enter a payment card, create signing keys, or deploy while these decisions remain open.

## First-party sources consulted

- Google Books API usage and volume fields: https://developers.google.com/books/docs/v1/using and https://developers.google.com/books/docs/v1/reference/volumes
- Google Books API terms: https://developers.google.com/books/terms (page states last modified April 25, 2012; re-check before use)
- Open Library API guidance: https://openlibrary.org/developers/api
- ML Kit Text Recognition for Android: https://developers.google.com/ml-kit/vision/text-recognition/v2/android
- Android developer verification: https://developer.android.com/developer-verification/guides
- Android APK update identity: https://developer.android.com/google/play/app-updates
- Amazon Creators API prerequisites: https://affiliate-program.amazon.com/creatorsapi/docs/en-us/introduction
- Noon API overview: https://noon-docs.noonpartners.dev/docs/getting-started/intro
- GitHub private release and token guidance is summarized in docs/COST_AND_HOSTING.md.
