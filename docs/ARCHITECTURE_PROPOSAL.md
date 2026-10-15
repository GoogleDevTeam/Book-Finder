# Proposed architecture — planning only

**Status:** Proposal for owner review; not an approved design and not authorization to implement.
**Prepared:** 2026-10-15
**Scope authority:** docs/APP_PLAN.md and docs/CHANGE_CONTROL.md

## 1. Fixed scope and limits

This proposal stays inside the confirmed Book-Finder scope: a private Android APK for about five invited users, one shared group list, printed or typed/on-screen English lists (one format per page), school books as the primary subject focus, user review of extracted entries, book/edition matching, and physical-book or optional ebook options with Saudi delivery/region uncertainty shown honestly. The $0/no-payment-card rule is confirmed. The app has no implementation approval: the owner must approve the complete plan and explicitly say to begin before any app source, provider integration, backend, build, or release is created.

The current plan has 16 stable requirements and 43 planned, unrun test cases. Thirty effective P0 questions remain. A plan can reduce foreseeable errors and define safe failure behavior; it cannot guarantee zero defects, perfect OCR, accurate live stock, or an APK that cannot be copied or reverse-engineered.

## 2. Recommended shape (all items below are proposals)

For a five-person Android-only app, start with a single native Android client and, if the owner accepts it, one small authenticated API service organized as a modular monolith. Do not begin with microservices: they add deployment, identity, logging, and cost surfaces without a scale need established by this project. Keep the hosting, identity provider, database, OCR engine, and retailer integrations unselected until their no-card eligibility, terms, and user data flows are verified.

### Android client

- Preferred stack to evaluate: Kotlin with Jetpack Compose, lifecycle-aware state, coroutines, and explicit presentation/domain/data boundaries. This is a candidate, not a chosen implementation stack.
- Capture/import one supported page, validate file type/size/orientation, and run English text recognition on-device where the tested OCR option supports the selected Android versions. ML Kit Text Recognition is one candidate; its bundled model reduces dependence on runtime model downloads but increases APK size. Benchmark image quality, cold start, memory, and battery on owner-selected devices before choosing it.
- Present extracted rows for correction before any search. Keep text-reading confidence separate from book-match confidence. For a scanned row with missing, invalid, or unreadable ISBN, block that row's search until a valid user-confirmed ISBN is entered, as required by BF-REQ-003.
- Treat a title/work, edition, translation, format, and ISBN as distinct evidence. Keep matching explainable: preserve source identifiers and field-level evidence; do not silently merge editions. Closest-edition ranking remains blocked on Q-19. School-specific fields such as grade, subject, curriculum/country, or academic year must not affect ranking or imply compatibility until the owner settles Q-21; do not infer those values.
- Use only the camera/photo access required by the approved flow; prefer Android's system photo picker for imports where suitable rather than broad media access. Exact permissions and supported OS versions remain Q-55/Q-58 decisions.
- Do not persist images, extracted text, or search history by default until the owner resolves Q-14/Q-15/Q-42/Q-43. The confirmed shared-list requirement still needs a decision on what is saved, for how long, and how deletion works.

### Optional authenticated service

If private GitHub releases, membership/device revocation, or shared-list synchronization remain in scope, a server-side trust boundary is the safer design: the APK cannot safely hold a GitHub/provider secret, and an offline copy cannot receive immediate revocation. Use one narrowly scoped API with explicit modules for identity/authorization, provider routing, provider configuration, and release metadata. Keep the chosen host and database open until a complete no-card/no-overage path is verified on the owner's actual account.

- Candidate flow: each invited person authenticates through a maintained identity provider; the service maps the identity/device to an owner-approved member record; protected requests are authorized server-side and denied after revocation. A native OAuth/OIDC authorization-code-with-PKCE flow is a candidate where supported; the identity product and device limit are not selected.
- Store only data required for the approved shared-list and access behavior. Define data fields, retention, deletion, backup, and incident response before creating a database. Do not log titles, ISBNs, photos, tokens, or user identities unless separately approved and strictly necessary.
- Keep GitHub/provider credentials and signing material in server-side secret storage, never in the APK, client config, logs, or GitHub. Use least-privilege, short-lived credentials and rotate/revoke them. The private repository itself is not a runtime secret store.
- If the owner rejects a backend, explicitly decide which shared-list, private-release, external-search, and revocation requirements are removed or acceptably weakened; do not pretend a client-only APK can protect a shared secret or revoke offline access.

## 3. Search and provider boundaries

Use separate interfaces and result types for book metadata, physical retail offers, and ebook-store offers. A metadata match is not a purchasable offer. Each provider result should carry provider identity, the checked time, the fields actually returned, and a status that distinguishes not configured, not checked, successful with no match, partial results, and provider failure. The confirmed per-search “Also search ebooks” checkbox is an explicit opt-in: when it is unchecked, do not invoke ebook adapters and show “not searched,” not zero results. A privacy-conservative default is unchecked, and the choice applies only to the current search rather than becoming a saved preference. A checked box still activates only an owner-approved, permitted ebook provider; no source is currently approved.

Recommended Q-24 registry shape (proposal only): use a versioned declarative allowlist with a stable provider ID, role (metadata/physical/ebook), official source and terms links plus review date, permitted access method, approved market, allowed HTTPS host, credential class (none or server-side secret reference), attribution/privacy requirements, rate limits, and operational status. Keep endpoints and request construction in reviewed code; do not accept arbitrary URLs, executable templates, scripts, or secrets from the GitHub list. Validate the manifest schema and host allowlist and fail closed. Q-24 remains owner-open; this is not a provider selection.

- Google Books is a metadata candidate: its API supports ISBN searches and country-specific sale information for the Google eBookstore. That signal applies to Google's store only; it does not establish physical stock/shipping, account purchase eligibility, device/DRM compatibility, or access across all ebook stores. Google Books terms also require user notice about submitted content potentially being made publicly available through Google services; review the exact data flow and notice before selection.
- Open Library is a possible human-oriented metadata lookup, not a default production backend. Its current API guidance asks developers to register the use case, recommends identifying requests, lists 1 request/second by default and 3/second for identified requests, and says its APIs are not intended as a third-party data backend or high-traffic commercial infrastructure. Confirm the private five-person app fits its terms before relying on it.
- Amazon.sa has an official Creators API path, but current prerequisites include target-marketplace Associates enrollment and at least 10 qualifying sales in the previous 30 days, plus API registration and credentials. Owner eligibility and app-use terms are unverified.
- Noon documentation reviewed describes seller, integrator, and partner APIs; it does not establish public consumer product search. Do not scrape shopper pages or misuse seller APIs.
- Until a provider has a permitted method, eligibility, cost, and coverage verified, mark it unsupported and show that it was not checked. Never convert provider failure into a no-match result.

For every physical offer, keep delivery status separate from stock/price and require owner-approved evidence before using a confirmed-Saudi-delivery label. For ebooks, expose the named store's country-specific availability and the checked time, but label unknown account, DRM, or entitlement conditions as unknown. The evidence standards and presentation remain open at Q-31–Q-39.
Recommended Q-32 evidence floor (proposal only): label shipping confirmed only when an approved source returns a current seller-/offer-specific confirmation or quote for the relevant Saudi destination. A marketplace/country setting, generic shipping policy, Prime/FreeShipping flag, or default in-marketplace price is insufficient. If the source cannot provide destination-specific evidence without checkout or an unapproved address, show Uncertain/Unchecked rather than claiming confirmed; never place an order. Destination granularity and freshness depend on Q-34/Q-36.


## 4. Security, resilience, and performance controls

### 4.1 Threat model and trust boundaries (proposal only)

This is a pre-build threat model for the confirmed product concept and the unapproved architecture candidates below. It is a security planning baseline, not a claim of compliance, certification, or risk elimination. The actual attack surface changes with the selected identity, data-retention, provider, hosting, and distribution decisions.

**Assets to protect:** source photos and OCR output; the shared group list, ISBN/title searches, and any history; invited-user identities, sessions, membership and revocation state; the GitHub provider registry and any server-side access credentials; application source and build dependencies; the release-signing key, APKs, and update metadata; and the provenance/freshness of retailer and ebook claims.

**Trust boundaries:** (1) camera/photo picker and device filesystem into the Android app; (2) Android app and local storage into any optional authenticated API; (3) API to GitHub and each independently operated provider; (4) GitHub-maintained provider configuration into the app/API; (5) release/build environment to GitHub distribution to Android Package Installer; and (6) provider responses into result rendering and user decisions. Treat data crossing each boundary as untrusted, including OCR text, configuration, URLs, API responses, release metadata, and claims returned by a recognized provider.

| Threat scenario | Required design control (proposal) | Existing risk / planned evidence |
|---|---|---|
| Forwarded or inspected APK, stolen credentials/device, unauthorized or revoked member, or offline copy. | Assume client code and local state can be copied or inspected. Put no shared secret or security-critical authorization decision only in the APK. If protected shared/backend features remain in scope, authorize each protected request against server-side invite/member/device state, support revocation, and fail closed when authorization cannot be checked. Offline grace and revocation delay remain owner decisions. | R13, R17–R20, R25–R26; TC-023–TC-025, TC-029; Q-40–Q-47, Q-69–Q-76. |
| Malicious or mistaken provider configuration causes server-side request forgery, arbitrary outbound traffic, or a source being falsely treated as enabled. | Use a versioned, schema-validated allowlist of known provider IDs and roles; keep request construction in reviewed adapters. Do not execute configuration, templates, or scripts, and do not accept arbitrary URLs or credentials from the manifest. Restrict HTTPS destinations to the reviewed provider host set; validate redirects. If a backend fetches provider URLs, resolve each target, reject loopback/private/link-local/metadata-service IPs at connection time, bind the connection to the validated resolution, and revalidate every redirect. Reject invalid configuration and mark the source unsupported/unchecked. | R5, R30, R34, R38; TC-021, TC-039; Q-24–Q-26, Q-47. |
| Malformed or hostile provider text/links cause script/markup injection, unsafe navigation, or a false shipping/ebook claim. | Bound response size and field lengths, validate schemas and URL schemes/hosts, render provider-controlled text as text (not executable markup/WebView content), and keep provider failure separate from a successful empty result. Preserve the evidence source and checked time; never promote an unverified seller/region claim to confirmed. | R6, R7, R8, R22, R39; TC-015–TC-020, TC-022, TC-040; Q-21, Q-27, Q-31–Q-39. |
| Crafted, corrupt, mislabeled, or extremely large image exhausts memory/CPU or reaches an unapproved processor. | Validate actual file format and orientation, apply documented byte/dimension/decoded-pixel limits before OCR, use bounded decoding and cancellation, and return a recoverable error for unsafe inputs. Do not upload or retain a source image except under an owner-approved data-flow and retention policy. Set numeric limits against selected devices rather than inventing them now. | R1, R21, R28, R40; TC-004, TC-031, TC-041; Q-13–Q-15, Q-42–Q-43, Q-55, Q-58, Q-75. |
| Reading data, credentials, or signing material leaks through logs, storage, backups, third-party calls, or the APK/repository. | Minimize collection and retention; decide transmission before enabling it; keep tokens/signing material out of source, client bundles, logs, and ordinary backups; use platform-protected storage for any approved client session material and a server-side secret store for server credentials. Apply least privilege and a rotation/revocation plan. | R9, R12, R23, R24; TC-026–TC-028; Q-14–Q-15, Q-42–Q-47, Q-72. |
| Malicious, corrupt, replayed, or downgraded update replaces the app, or key loss prevents legitimate updates. | Separate authorization from version discovery; use an owner-controlled signing identity, verify package identity/signature and monotonic version before offering an update, keep Android's user confirmation, and define key custody, backup, incident response, and recovery before first release. A hash alone is not a substitute for signature verification. | R10, R11, R23, R25; TC-032–TC-034; Q-50–Q-54, Q-78. |
| Slow provider/backend or exhausted free quota produces a hang, stale answer, false empty result, or unexpected cost. | Bound image work, response sizes, per-provider timeouts/concurrency and retry budgets; cancel obsolete work; distinguish stale, failed, unchecked, empty, and partial states; stop/degrade at quota without enabling billing. Do not retry writes or trigger purchases automatically. | R16, R21, R27, R29, R31–R35; TC-015–TC-016, TC-022, TC-029–TC-031; Q-27, Q-30, Q-36, Q-75–Q-87. |
| Compromised maintainer account, dependency, or direct-main push changes code/config or release without detection. | Keep write access minimal; before every main-only batch, re-read the current head and affected files, validate the diff and dependencies, scan for secrets, make a fast-forward-only commit, and read back the resulting main content. Main-only is the owner's workflow rule, not an independent review or branch-protection guarantee. Never deploy from a commit without separate release approval. | R14–R15, R18, R23–R24; TC-026, TC-035, TC-038; repository permissions/signature settings must be rechecked before implementation/release. |

**Security invariants for any future implementation:** no GitHub/provider/signing secret in the APK; no arbitrary executable provider configuration; no unapproved image/text transmission; no protected operation based only on client-side role flags; no unverified shipping or ebook access stated as confirmed; bounded processing and network work; and no silent update, install, purchase, paid overage, or automatic tier change. Test the selected design against the relevant MASVS/MASTG controls and the existing risk/test IDs; this is a planning checklist, not an OWASP certification claim.

**Owner decisions still required:** data flow/retention (Q-14/Q-15/Q-42/Q-43/Q-46), threat scope and confidential assets (Q-45/Q-69/Q-72), backend/auth/revocation/offline (Q-40–Q-47/Q-70–Q-76), providers and evidence standards (Q-23–Q-39), and hosting/release/key custody (Q-50–Q-58/Q-78/Q-81–Q-85). This proposal does not close those questions or authorize implementation.


### 4.2 Performance measurement proposal (Q-75; not approved)

Before setting a latency target, select representative Android devices, supported OS versions, realistic sanitized list sizes, and network conditions. On release builds, use Android Macrobenchmark for cold, warm, and hot startup and relevant end-user interactions; report repeated-run medians for typical startup and the distribution/percentiles for frame timing. Measure image decode/OCR, review rendering, provider round trips, and update metadata checks as separate stages so network variance is not attributed to local work. Test on each selected device class, include a lower/mid-range device, control background apps, battery/thermal state, compilation mode, and network, and record build/device/OS/fixture identifiers. Use deterministic provider fixtures for reproducible app timing; use live-source timings only as separately labeled observations. Numeric pass thresholds remain unset until the owner selects devices, list sizes, and acceptable trade-offs. This is a method proposal, not evidence that the app was benchmarked.

### 4.3 Incident-response proposal (Q-78; not approved)

Use a small, rehearsed playbook aligned with NIST SP 800-61 Rev. 3's incident-response risk-management guidance: prepare; detect and assess; contain; eradicate/recover; communicate; and record lessons. Maintain a current inventory of credentials, owners, release signer/package identity, approved provider IDs, and a safe contact route without copying secrets into the plan. For a suspected account/device compromise, deny or revoke the affected identity/device at the server if that capability is selected; for a GitHub/provider credential leak, revoke/rotate the credential and inspect recent access; for hostile provider configuration, disable that source and fail closed; for a suspected release/signing compromise, pause distribution and protected update metadata until signer, artifact, and source integrity are re-established. Recover only from a known-good reviewed main commit and verified release identity; issue no silent install, payment, or unapproved user-data action. Preserve only sanitized evidence, notify affected users through an owner-approved channel, and record decisions and follow-up risks. A removed GitHub release cannot uninstall installed copies; offline devices cannot receive immediate revocation. The named responder, severity, response window, user notice channel, escalation authority, and any jurisdiction-specific notification duties remain owner decisions.

- Validate all input at the boundary: ISBN-10/13 check digits, image MIME/size, provider response schema, URL scheme/host, and numeric values. Use parameterized database operations and safe text rendering; treat images, OCR text, provider responses, and GitHub configuration as untrusted input.
- Enforce HTTPS, least-privilege API scopes, server-side authorization on every protected operation, bounded request/response sizes, timeouts, rate limits, and controlled retries with jitter. Do not retry writes or send duplicate provider calls without idempotency.
- Handle each source independently with bounded concurrency and progressive results. Preserve explicit states for unavailable, rate-limited, stale, partial, and not configured. Any cache must show its age and must never make stale shipping/stock look current.
- Keep launch responsive: on-device OCR and update checks must not block startup indefinitely. Use cancellation, lifecycle-aware work, and clear offline/degraded states. Set numeric latency/accuracy thresholds only after Q-55/Q-58/Q-64/Q-65/Q-75 and representative test data are available.
- Protect local session material with Android-protected storage; avoid custom password storage. Define lock/logout, shared-device handling, lost-device response, session expiry, and revocation behavior before implementation.
- Do not require Google Play licensing entitlement as access control for the current private sideloaded APK proposal: official Play Integrity documentation reports UNLICENSED when the app is sideloaded and describes PLAY_RECOGNIZED for app/certificate versions distributed by Google Play. Any optional device-integrity signal must be tested for the actual distribution channel and evaluated for privacy, compatibility, cost, and bypass limits; attestation is not a substitute for server-side authorization. Q-77 remains open.
- Obfuscation and tamper checks can raise effort, not guarantee secrecy. Assume an APK can be copied and inspected; place no secret or authorization decision that must remain secret solely in the client.

## 5. Verification and release gates

Before implementation approval, close or explicitly defer all P0/P1/P2 questions per OPEN_QUESTIONS.md; resolve the data flow and threat model; verify each selected provider's official access method; and assign owners/devices/criteria to the planned test matrix. Planned tests are not passed tests.

For each later code batch, run the applicable formatter/linter, unit and integration tests, Android build, dependency/license and vulnerability checks, secret scanning, and targeted security tests before commit. Use deterministic provider fixtures for routine tests; live provider calls are opt-in contract checks and must not expose real book data in logs. Before a release, verify APK signature/package identity, hash, version code, permissions, Android compatibility, update/recovery path, and private distribution authorization.

Android's developer-verification documentation describes a rollout beginning September 30, 2026 in Brazil, Indonesia, Singapore, and Thailand, with global expansion for certified devices in 2027. Saudi Arabia is not in that initial list, but the future distribution plan must re-check applicability. A no-fee student/hobbyist account path is documented for some developers distributing outside Google Play; eligibility, identity requirements, package registration, and timing are not yet verified for this owner.

## 6. Main-only change and deployment workflow

- Work only on the existing main branch. Never create or use a topic, temporary, release, or backup branch or worktree; never force-push. Before each batch, read the latest main commit and every affected file, then prepare one focused, internally consistent change.
- For documentation, validate question-ID uniqueness/status counts, requirement/test mappings, links, source claims, and absence of secrets. For code, complete local tests/security checks before committing; do not rely on a post-push workflow as the only gate. Keep commits small and focused, then verify GitHub main points to the intended commit and its files match the checked content.
- Add a concise PROJECT_LOG.md checkpoint after every 2–3 substantive steps/batches with the exact commit, verification, unresolved decisions, and next safe step. Do not copy private book data or credentials into the log.
- Repository-state check (2026-10-15): only main exists; it is unprotected; only GoogleDevTeam currently has push/admin access. The latest GitHub API commit is unsigned. GitHub rejects unsigned commits when required-signature rules are enabled, so do not enable that rule until a compatible signed-write path is available and the owner approves it. No branch or repository setting was changed; re-check access before adding collaborators or releasing.
- Main is the only code source of truth; no alternate branch or PR workflow. A commit to main is not production approval. Never automatically publish an APK or change production services from a main push. Obtain the owner's explicit approval for each consequential release/deployment; distribute only a signed, owner-approved artifact and preserve update identity.

## 7. Decisions still required

At minimum, the plan gate still needs the unresolved effective P0 questions listed in OPEN_QUESTIONS.md. Research has narrowed Q-23/Q-25/Q-37 but has not selected providers or closed them. Q-03 is now confirmed; the next unanswered sequential owner question is Q-04 (interface languages). Do not implement, configure accounts, enter a payment card, create signing keys, or deploy while these decisions remain open.

## First-party sources consulted

- Google Books API usage and volume fields: https://developers.google.com/books/docs/v1/using and https://developers.google.com/books/docs/v1/reference/volumes
- Google Books API terms: https://developers.google.com/books/terms (page states last modified April 25, 2012; re-check before use)
- Open Library API guidance: https://openlibrary.org/developers/api
- ML Kit Text Recognition for Android: https://developers.google.com/ml-kit/vision/text-recognition/v2/android
- Android developer verification: https://developer.android.com/developer-verification/guides
- Android APK update identity: https://developer.android.com/google/play/app-updates
- Amazon Creators API prerequisites: https://affiliate-program.amazon.com/creatorsapi/docs/en-us/introduction
- Amazon SearchItems delivery flags and response mapping: https://affiliate-program.amazon.com/creatorsapi/docs/en-us/api-reference/operations/search-items
- Amazon OffersV2 fields and price caveat: https://affiliate-program.amazon.com/creatorsapi/docs/en-us/api-reference/resources/offersV2
- Google Play Integrity verdicts: https://developer.android.com/google/play/integrity/verdicts
- GitHub ruleset and signed-commit behavior: https://docs.github.com/en/repositories/configuring-branches-and-merges-in-your-repository/managing-rulesets/available-rules-for-rulesets
- Noon API overview: https://noon-docs.noonpartners.dev/docs/getting-started/intro
- GitHub private release and token guidance is summarized in docs/COST_AND_HOSTING.md.

## Mobile security references consulted (2026-10-15)

OWASP MASVS/MASTG are used here as organizing and verification references, not as a claim of compliance or certification.

- [OWASP MASVS](https://mas.owasp.org/MASVS/) — mobile security control groups including storage, cryptography, authentication, network, platform, code, resilience, and privacy.
- [OWASP MASTG Android data storage](https://mas.owasp.org/MASTG/0x05d-Testing-Data-Storage) — mobile storage verification guidance.
- [Android security best practices](https://developer.android.com/privacy-and-security/security-best-practices) — Android platform security guidance.
- [Android Keystore system](https://developer.android.com/privacy-and-security/keystore) — platform-backed cryptographic key storage guidance.

## Performance and incident-response sources consulted (2026-10-15)

- [Android Macrobenchmark metrics](https://developer.android.com/topic/performance/benchmarking/macrobenchmark-metrics) — startup timing and frame-timing metrics.
- [Android performance test environment](https://developer.android.com/topic/performance/appstartup/setup-env) — release builds and reducing measurement noise.
- [NIST SP 800-61 Rev. 3 Final](https://csrc.nist.gov/pubs/sp/800/61/r3/final) — incident-response recommendations within cybersecurity risk management.
