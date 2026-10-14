# Book-Finder: planning remediation rundown

**Snapshot date:** 2026-10-14  
**Status:** Historical audit plus remediation status; planning documentation only. Not approval to build.  
**Status refresh (2026-10-15):** F-01–F-05 planning-document repairs and the issue #7 status correction are now recorded on main. Draft PR #8 is superseded by the direct main-branch commits. F-06 provider research and F-07 owner/security decisions remain open. No application code exists, and no app tests have run.  
**Repository:** GoogleDevTeam/Book-Finder  
**Basis:** Main-branch documents and GitHub state read for this review.

## Executive readout

The likely Saudi book project is Book-Finder. It is a proposed private Android APK for turning a photographed multi-book list into reviewable book records, matching titles/editions, and finding physical or ebook options while clearly separating confirmed from uncertain Saudi delivery/access.

At the original 2026-10-14 review, the project was planning-only, with no app code, GitHub issues, or pull requests. No runtime bug hunt, build, or app test could run. That review found planning defects and feasibility risks; it could not prove the eventual app would work or that every future defect had been found. The F-01–F-05 documentation repairs are now recorded on main. Remaining blockers include provider/hosting research and unresolved owner decisions; no runtime behavior is verified.

The next step is to complete research that can proceed without owner input, then ask only for decisions or samples the owner must supply. Do not interpret a candidate architecture, provider, recommendation, or this rundown as approval to implement it.

## Current product baseline

The following is recorded as confirmed in the existing plan and decision log:

- Android APK, intended for about five invited users.
- Input is a photo of a list containing multiple books, not cover-photo recognition.
- Search can use ISBN, title/name, or both; show text-reading/OCR confidence separately from book-match confidence and flag edition/version differences. Acceptable-match criteria remain open.
- Search physical-book and ebook options for Saudi Arabia; never describe uncertain delivery or ebook-region access as confirmed.
- Provider names/list are maintained through GitHub. Amazon.sa and Noon are examples only; neither is an approved working integration.
- Check for GitHub-based releases when the app launches; Android/user approval is required to install.
- Privacy and security are high priorities. Security, safety, and performance are the owner's top priorities.
- The owner wants a $0 service path with no card requirement, paid overage, or automatic upgrade. If a free quota is reached, the safe default is to stop/degrade rather than charge.
- The plan is the scope authority. No feature, provider, or app behavior outside the explicitly approved plan may be implemented.

Open details include the criteria for a “closest” edition and fallback if none is suitable, response to accidental mixed-format input, app interface language, access model, data retention, provider methods, Saudi evidence standard, first-APK distribution, and target devices. Printed and typed/on-screen English lists, one format per page, are selected; handwritten lists, mixed-format pages, Arabic, and other-language recognition were not selected. The minimum outcome is a likely match with edition differences and a physical-book/ebook preference.

## GitHub state checked

- Repository: private, main branch, planning documents only.
- At the original 2026-10-14 review, no open or closed issues were returned by the repository issue listing and no pull requests were returned. Later planning issues and branch status are recorded in the project log.
- Main had one branch at the time of review. The latest commit recorded the planning audit; no application implementation was present.
- The repo's own rules require all app-related discussion outcomes to be logged, every change to map to an approved requirement, and explicit approval of the complete plan before implementation.

## Findings and the safe repair for each

### F-01 — The decision backlog and approval gate disagreed at audit time

At the original 2026-10-14 audit, the catalog had 86 open items and Q-79 confirmed. After the recorded owner decisions and the 2026-10-15 backlog reconciliation, the catalog contains 73 Open, five Partial (Q-17, Q-19, Q-27, Q-33, Q-57), and nine Confirmed (Q-01, Q-06, Q-07, Q-16, Q-67, Q-68, Q-79, Q-80, Q-87). OPEN_QUESTIONS.md now has one row for every Q-01–Q-87 and is the canonical status/priority index; the catalog mirrors it. Thirty effective P0 blockers remain: 25 active catalog-P0 items plus five audit-elevated P1 items (Q-45, Q-47, Q-54, Q-55, Q-58).

**Completed repair (2026-10-15):** OPEN_QUESTIONS.md is the canonical ID-mapped status/priority index; DISCOVERY_CATALOG.md carries detailed context and mirrors the index; CHANGE_CONTROL.md gates approval on its active effective-P0 list. Research-only tasks and owner decisions are distinguished. Q-79 remains confirmed; counts and blocker IDs must be updated together whenever a status changes.

### F-02 — Requirement traceability was not executable at audit time

At the 2026-10-14 audit, CHANGE_CONTROL.md required a plan requirement ID before every app change, but APP_PLAN.md did not yet provide stable IDs or requirement-to-test mapping.

**Completed repair (2026-10-15; committed directly to main):** APP_PLAN.md now assigns BF-REQ-001–BF-REQ-016 to confirmed scope, with observable acceptance criteria and mapped planned cases in TEST_MATRIX.md. Proposed and open decisions remain outside the confirmed requirement set.

### F-03 — The risk inventory was duplicated at audit time

At the 2026-10-14 audit, DISCOVERY_CATALOG.md and RISK_REGISTER.md had overlapping risk lists with no canonical register or crosswalk, and some entries lacked consistent treatment fields.

**Completed repair (2026-10-15; committed directly to main):** RISK_REGISTER.md is canonical for R1–R37; DISCOVERY_CATALOG.md preserves R-01–R-26 as legacy aliases in a complete crosswalk. Each canonical entry records an accountable owner, impact rationale, unrated likelihood with the evidence gap, prevention, planned detection/test, safe fallback, and residual risk. Planned tests have not run; high-impact residual risks remain open for review/acceptance.

### F-04 — Older wording conflicted with the no-card rule at audit time

At the 2026-10-14 audit, an earlier cost paragraph appeared to conflict with the later owner-confirmed no-payment-card rule.

**Completed repair (2026-10-15; committed directly to main):** DECISIONS.md explicitly marks the older wording as superseded for card eligibility, while preserving the separate open question about pauses/cold starts. APP_PLAN.md and COST_AND_HOSTING.md consistently retain the $0/no-card constraint; no host or billing setting has been selected.

### F-05 — The test matrix was missing at audit time

At the 2026-10-14 audit, the change-control policy required tests for OCR/correction, matching, Saudi delivery and ebook uncertainty, privacy/access, outages, and signed APK updates, but no case-by-case matrix existed. No app tests could run because there was no code.

**Completed planning repair (2026-10-15; committed directly to main):** TEST_MATRIX.md now contains 38 planned cases covering those required flows and additional failure states. Each case records input, expected result, pass criterion, owner/environment, evidence, and unresolved prerequisites; unavailable thresholds/devices/owners remain explicitly TBD. The matrix is a plan only: it has not been executed, and the repository still has no app code.

### F-06 — Provider feasibility is not established

The dated audit found:

- Amazon.sa: the older PA-API was documented as deprecated for May 15, 2026; the newer Creators API lists Saudi Arabia, but the audit found eligibility prerequisites including Associates enrollment and 10 qualifying sales in the previous 30 days. Owner eligibility, permitted use, required disclosures, and useful Saudi book coverage are unverified. Treat as a research candidate, not an integration.
- Noon: the official material reviewed described seller/partner catalog tools, not a proven permitted consumer product-search API for this app. Treat as unverified; do not scrape or infer access.
- Google Books and Open Library: possible bibliographic metadata candidates only. Their metadata does not prove Saudi stock, delivery, or ebook purchase rights/region access.
- Hosting: no backend is selected. Published free quotas do not prove that signup is card-free or that the full authentication/private-release path works at $0. The audit's no-card feasibility was not tested with provider accounts.

**Proposed repair:** For every source, capture the exact official permitted access method, eligibility, cost/card requirements, book/offer coverage, rate limits, query data exposure, Saudi shipping/ebook evidence, and failure behavior. If there is no permitted $0/no-card route, mark it unsupported rather than substituting scraping or a paid service. Recheck source terms and prices before approval/release; the audit is a dated snapshot.

### F-07 — Security and performance goals need a threat model and measurable test basis

The plan correctly says an APK cannot be made impossible to copy, inspect, or decompile. Obfuscation may raise effort but cannot be a security boundary. A private GitHub repo or private APK download alone does not revoke a forwarded APK or protect secrets embedded in it.

**Proposed repair:** Decide what must be protected and against whom; define invited-user/device enrollment, revocation, offline behavior, backend acceptability/ownership, secret boundaries, signing-key custody/recovery, and incident response. Keep secrets and protected operations out of the APK. Measure startup, scanning/review, provider search, and update behavior on agreed devices. State residual reverse-engineering risk plainly rather than promising impossible secrecy.

## Complete effective P0 blocker map

OPEN_QUESTIONS.md is the canonical status/priority register. The grouped map below is a navigation aid; the catalog retains detailed context.

- Product/input definition: Q-08. Q-01 (minimum outcome), Q-06 (one format per page), and Q-07 (English list content) are confirmed.
- Identification behavior: Q-17 is partial (separate confidence types confirmed; acceptable-match criteria open). Q-16 (require a corrected ISBN for scanned entries before search) is confirmed.
- Provider list and permitted method: Q-23, Q-24, Q-25.
- Saudi market and claim evidence: Q-31, Q-32, Q-33, Q-37.
- Data, access, and backend threat boundary: Q-40, Q-41, Q-42, Q-45, Q-47.
- First distribution, private release access, signing, supported Android and install behavior: Q-50, Q-51, Q-52, Q-54, Q-55, Q-58.
- Accuracy and owner-provided evidence: Q-64, Q-65. Plan authorization (Q-67 and Q-68) is confirmed.
- Owner-confirmed security priority: Q-69, Q-70, Q-71, Q-72, Q-74.
- No-cost operation and provider feasibility: Q-81, Q-82, Q-83. Quota-stop/no-overage behavior (Q-80 and Q-87) is confirmed.

That is 30 effective blockers. Q-79 is confirmed: a payment card is not acceptable for a free tier. Current status is 73 Open, five Partial, and nine Confirmed; see OPEN_QUESTIONS.md for the authoritative per-ID list. The remaining open P1/P2 items are also in DISCOVERY_CATALOG.md; they include app interface language, user separation, usage situation, supported layouts/fields, correction UX, source ranking/freshness, closest-edition criteria/fallback, prices/taxes, ebook entitlement/DRM, retention/deletion, logs, incidents, monitoring, collaboration policy, accessibility, offline behavior, user-visible states, and numeric targets. Do not close them by inference; defer only when the approved v1 scope explicitly permits it.

## Candidate documentation improvements (not product approvals)

These are proposed ways to make the future product safer and clearer. They are not new confirmed requirements or a license to build them:

- Let users review/correct extracted rows before search; preserve quantity separately from the number of rows.
- Show per-source states so an unavailable/untested provider is not mistaken for a successful empty search.
- Separate physical delivery from ebook-region entitlement; show evidence/source and checked time when available.
- Keep provider failures, timeouts, stale data, and offline states explicit; never translate them to “no book found” or confirmed availability.
- Evaluate on-device OCR as a privacy option, but benchmark battery, memory, and latency before choosing it.
- Evaluate an invite-only access service and server-side mediation for private GitHub data, but only if the owner accepts a backend and a safe $0/no-card option is actually verified.
- Treat an update as untrusted unless release provenance/signing checks pass; Android installation remains user-approved.
- Use bounded requests, timeouts, retries, and progressive per-provider results; decide exact limits only after device/provider tests.

Any idea that adds user-facing scope, data collection, providers, or app behavior still needs a change proposal and owner approval under CHANGE_CONTROL.md.

## Proposed pre-build test matrix outline

Add exact fixture files, devices, and thresholds only after owner input. The outline below defines expected safety behavior without inventing performance targets.

| Area | Case | Expected observable result | Evidence / decision still needed |
|---|---|---|---|
| Photo/OCR | Legible multi-row printed or typed/on-screen English list, one format per page | Distinct editable rows; text-reading/OCR and book-match confidence shown separately | Sanitized English samples and supported layouts; target accuracy TBD |
| OCR failure | Blur, rotation, handwriting, mixed language, missing fields | Mark uncertain/unsupported; allow correction or manual entry; do not search guessed data silently | Which samples and languages are in scope |
| Segmentation | Columns, duplicates, quantity, wrapped titles | Preserve row boundaries and quantity independently; review before search | Representative layouts and meaning of “number of books” |
| ISBN | Missing, invalid, unreadable, or one-digit error in a scanned entry | Request a valid user-entered/corrected ISBN before searching that entry | Define validation behavior and test fixtures; do not assume a title/author fallback for this case |
| Matching | Same-title editions, translations, volume, format | Preselect the closest match and flag differences; do not call it exact without evidence | Define “closest,” acceptance/review, and fallback when no candidate is suitable |
| Provider | Supported, unsupported, timed out, rate-limited, partial response | Per-source status; failed check is not “no result”; no unsupported source claimed as searched | Approved provider list/methods and service limits |
| Saudi print | Confirmed, unknown, unavailable, address-dependent delivery | Separate states; unknown never shown as confirmed; show evidence and freshness where available | Owner-approved evidence standard and address scope |
| Ebook | Region unknown, restricted, purchase, subscription, loan | State access type and region uncertainty separately from print shipping | Candidate providers and in-scope access types |
| Privacy/access | Unauthorized, revoked, expired, wrong user/device | Protected data/release denied; no cross-user list leakage | Auth model, device limits, revocation window |
| Data lifecycle | Image/text/history disabled or enabled by decision | Actual retention matches plan; delete/clear behavior verified; logs avoid book content | Retention, sharing, export/delete decision |
| Network/outage | No network, backend down, GitHub/provider failure, quota reached | Bounded wait, clear degraded state, safe fallback, no charge, no false “not found” | Offline allowance and free-tier limits |
| APK/update | Valid signature, wrong signer, corrupt package, stale release, downgrade | Reject untrusted package; preserve installed version; require Android/user approval | Signing custody/recovery and rollback policy |
| Performance | Startup, OCR/review, search, update check on agreed devices | No indefinite hang; cancellation/progress where approved; update check does not block routine startup | Device matrix, realistic list sizes, numeric targets TBD |

Each eventual test row should add: requirement ID, case ID, exact input/setup, steps, expected result, measurable threshold if approved, device/provider/version, pass/fail, evidence link, and owner. No pass claims exist yet because there is no implementation or executable suite.

## UX/failure states that should be specified before implementation

At minimum, define the user's visible behavior for: waiting/scanning; low-confidence extraction; correction required; no candidate; multiple edition candidates; source not configured; per-source loading/partial result/timeout/rate limit; stale source data; confirmed/unconfirmed/unavailable Saudi delivery; uncertain/restricted ebook region; offline; authorization denied/revoked; GitHub/backend unavailable; no update/current update/new update; invalid signature; Android install approval required; free quota exhausted; and recovery/retry. These states are proposed quality requirements; wording, actions, and offline availability remain open.

## Recommended order of work

1. Owner resolves the grouped effective P0 questions; capture answers in DECISIONS.md and the matching catalog entries. Research-only unknowns can be investigated, but do not convert research into owner approval.
2. Reconcile OPEN_QUESTIONS.md with Q-01–Q-87 and the 37-blocker count; make Q-79 the sole active no-card rule.
3. Add stable requirement IDs to confirmed APP_PLAN scope and map each one to acceptance criteria/tests.
4. Select a canonical risk register and create the old-ID crosswalk; add detection/evidence/residual-risk fields without inventing scores.
5. Verify provider and hosting feasibility against $0/no-card, privacy, and permitted use; select nothing whose account eligibility, terms, or safe access method is unknown.
6. Decide the threat/data-flow, access/revocation, offline, first-install, signing/recovery, and update behavior.
7. Build the concrete test matrix and UX state specification; use sanitized sample lists and agreed Android devices to set targets.
8. Review the complete reconciled plan and obtain explicit approval to begin implementation. Only then start app code.

## Record boundary

This rundown records findings and recommendations. It does not modify APP_PLAN.md, change any decision to Confirmed, select a host/provider/auth method, add a feature, or authorize app implementation. The project remains planning-only. The formal plan, decision, change-control, risk, and audit documents remain authoritative until they are updated through the approved project process.

## Sources and audit limits

The underlying dated review is docs/PLAN_AUDIT.md, with the current scope and policies in docs/APP_PLAN.md and docs/CHANGE_CONTROL.md. Cost/provider statements are dated snapshots, not guarantees; verify live terms before approval. No app code, APK, provider credentials, live bookstore search, or sample list was available to test in this review.

External references captured by the existing audit:
- Amazon Saudi PA-API notice: https://webservices.amazon.com/paapi5/documentation/locale-reference/saudi-arabia.html
- Amazon Creators API prerequisites: https://affiliate-program.amazon.com/creatorsapi/docs/en-us/introduction
- Amazon Saudi marketplace locale: https://affiliate-program.amazon.com/creatorsapi/docs/en-us/locale-reference/saudi-arabia
- Noon Partner Catalog help: https://helpcenter.noon.partners/en/category/product-listing/how-to-search-for-items-in-the-partner-catalog
- Google Books API: https://developers.google.com/books/docs/v1/using
- Open Library API guidance: https://openlibrary.org/developers/api
- Android publishing: https://developer.android.com/studio/publish/index
- Android app signing: https://developer.android.com/studio/publish/app-signing
- Cloudflare Workers pricing: https://developers.cloudflare.com/workers/platform/pricing/
- Cloudflare D1 pricing: https://developers.cloudflare.com/d1/platform/pricing/
- Replit deployment pricing: https://docs.replit.com/billing/deployment-pricing
