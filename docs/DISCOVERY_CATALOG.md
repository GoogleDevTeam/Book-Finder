# Discovery catalog: questions, issues, dependencies, and safeguards

**Version:** 0.1  
**Last updated:** 2026-10-15  
**Status:** Working inventory — not a final approved product specification  
**Purpose:** Make product decisions and foreseeable failure modes visible before any implementation.

This catalog deliberately distinguishes confirmed requirements from open decisions and recommendations. Do not infer an answer from a suggested mitigation. Priority labels are working recommendations for planning, not user-approved commitments.

## 1. Confirmed guardrails

| ID | Confirmed direction |
|---|---|
| C-01 | Android app distributed as an APK. |
| C-02 | The input image is a list of multiple books, not a photo of the books themselves. |
| C-03 | Search by ISBN, book name/title, or both; show identification confidence. |
| C-04 | Flag different editions and versions. |
| C-05 | Search physical-book and ebook sources; flag uncertain ebook-region availability. |
| C-06 | Saudi Arabia is the required destination/market; uncertain shipping must be labeled uncertain, not confirmed. |
| C-07 | Provider names/list are managed through GitHub. Amazon.sa and Noon are examples only, not confirmed integrations. |
| C-08 | Check for updates from GitHub when the app launches; installation requires the user's Android approval. |
| C-09 | About five people are expected to use the app; privacy and security are high priorities. |
| C-10 | The approved plan is the scope authority. Every app addition, edit, creation, and bug fix must be planned and traceable before work begins. |

## 2. Discovery questions

**Backlog control:** OPEN_QUESTIONS.md is the canonical ID-mapped status, priority, and handling index for Q-01–Q-87. This catalog keeps detailed question context and rationale; mirror its statuses and priorities from the canonical index. Do not maintain a second, differently filtered approval backlog.

Priority guide: **P0** blocks approval of a complete plan; unresolved effective P0 items must be resolved before plan approval. **P1** must be decided before the affected feature is built; the project-wide gate in OPEN_QUESTIONS.md and CHANGE_CONTROL.md also requires unresolved P1 items to be resolved or explicitly deferred before plan approval. **P2** may be deferred only when the affected behavior is explicitly excluded from the approved v1 scope. A deferral removes that behavior from approved scope until a decision is recorded; an open item does not authorize implementation. Status is **Open** unless explicitly marked otherwise.

### Product purpose and users

| ID | Priority / status | Question or decision | Why it matters |
|---|---|---|---|
| Q-01 | Confirmed | Minimum useful v1 outcome is likely book matches with edition differences; the user can choose a physical-book or ebook preference. Exact-edition/default selection behavior remains open under Q-19. | Prevents scope from drifting into unrelated book-management features. |
| Q-02 | P1 / Confirmed | The owner selected one shared group list for about five users and explicitly chose to leave the group relationship/category unspecified for v1. Authentication and authorization remain open under separate questions. | Records the list-sharing model without collecting group relationship data. |
| Q-03 | P1 / Partial | The owner selected “Library or school inventory” as the main context. Clarify whether v1 manages holdings (for example, copies, locations, or loan status) or only finds book matches and online sources from such lists. The current plan covers list scanning and sourcing; collection management requires an explicit scope update. | This distinction changes the data model, shared-list semantics, permissions, retention, and acceptance tests; do not infer the broader workflow. |
| Q-04 | P1 / Open | Which languages should the app interface support? Is Arabic/RTL required, English, or both? | Affects layout, accessibility, OCR display, and testing. |
| Q-05 | P1 / Open | What is explicitly out of scope for version one? | Prevents optional ideas from becoming accidental requirements. |

### Book-list photo and extraction

| ID | Priority / status | Question or decision | Why it matters |
|---|---|---|---|
| Q-06 | Confirmed | Printed and typed/on-screen lists were selected for v1, one format per page; handwritten and mixed-format pages were not selected. | OCR performance varies substantially by input type. |
| Q-07 | Confirmed | English was selected as the list-content language; Arabic, mixed Arabic/English, and other-language recognition were not selected. | Determines OCR capability and false-match risks. |
| Q-08 | P0 / Open | The owner is unsure whether “number of books” means list entries, copies per title/edition, or both. How should a line such as “Dune, 2nd edition ×3” be represented? | Prevents quantity from being confused with book count or duplicate titles. |
| Q-09 | P1 / Open | What layouts are common: one title per line, columns, tables, checklists, notes in margins? | Affects row/column segmentation and review UI. |
| Q-10 | P1 / Open | Can one list span multiple pages or images? Should the user combine pages? | Defines batch handling and duplicate detection. |
| Q-11 | P1 / Open | Which fields should be extracted, and which are optional (title, author, ISBN, edition, publisher, year, quantity, language, format, notes)? | Defines data model and review screen. |
| Q-12 | P1 / Open | How should uncertain characters and missing fields be shown and corrected? | Avoids silently searching OCR guesses. |
| Q-13 | P1 / Open | Should the app reject poor photos, guide retaking, or allow manual correction even when OCR fails? | Defines safe fallback and permission UX. |
| Q-14 | P1 / Open | Should images be processed entirely on-device? May any image or extracted text be sent to a backend? | Core privacy and OCR architecture decision. |
| Q-15 | P1 / Open | Should the app retain source images, corrected lists, or history? If yes, where, for how long, and how can a user delete them? | Determines data minimization, storage, and deletion behavior. |

### Identification, editions, and catalog matching

| ID | Priority / status | Question or decision | Why it matters |
|---|---|---|---|
| Q-16 | Confirmed | For a scanned list entry with a missing, invalid, or unreadable ISBN, require the user to enter or correct a valid ISBN before that entry is searched. | Lists may lack ISBNs; ISBN validation alone cannot identify every book. |
| Q-17 | P0 / Partial | The owner chose separate text-reading/OCR and book-match confidence. Define what counts as an acceptable book match. | Reading text correctly is different from matching the intended book. |
| Q-18 | P1 / Open | Which edition differences must be called out: publisher, year, language, translation, format, revised edition, volume, or ISBN? | Defines meaningful edition warnings. |
| Q-19 | P1 / Partial | The owner chose to preselect the closest edition and flag differences. Define what “closest” means and what to do when no candidate is suitable or the requested edition is unavailable. | Avoids substituting a different book without consent. |
| Q-20 | P1 / Open | How should duplicates, series/volume numbers, pseudonyms, alternate titles, and transliterations be handled? | Reduces duplicate results and incorrect merges. |
| Q-21 | P1 / Open | Which book metadata must be displayed, and what source should be considered authoritative when sources disagree? | Catalogs can disagree on title, author, ISBN, and edition. |
| Q-22 | P1 / Open | What confidence explanation is useful: a label, detected fields, match evidence, or all of these? | Makes confidence understandable rather than a misleading score. |

### Providers and online search

| ID | Priority / status | Question or decision | Why it matters |
|---|---|---|---|
| Q-23 | P0 / Partial | Which metadata, retail, and ebook providers are acceptable v1 candidates? Google Books (metadata/Google eBookstore) and Open Library (human-oriented metadata) are research candidates; Amazon.sa eligibility and Noon consumer search remain unverified. No provider is selected. | A bounded source list is testable; “everywhere online” is not. | 
| Q-24 | P0 / Open | What exactly will be stored in the GitHub provider list: names/links, search templates, API configuration, or something else? | A list of URLs alone may not make a provider searchable or verifiable. |
| Q-25 | P0 / Partial | For each provider, is there an official API or other permitted search method, and are credentials required? First-party docs were reviewed for Google Books, Open Library, Amazon.sa, and Noon; use-case terms, account eligibility, costs, live coverage, and provider-specific permission are not fully verified. | Avoids blocked, brittle, or disallowed scraping and exposes credential needs. | 
| Q-26 | P1 / Open | Who may add/edit providers, how are changes reviewed, and how does the app know a source is supported? | Prevents arbitrary or broken source entries from becoming active. |
| Q-27 | P1 / Partial | The plan requires a provider failure to be shown as unchecked, not as no results. Which retry/backoff and partial-response behavior, if any, should be specified? | A failed search must not look like “no book exists.” |
| Q-28 | P1 / Open | How should results be sorted or compared, and should the app show one result or results from every enabled provider? | Affects completeness, ranking, and user trust. |
| Q-29 | P2 / Open | Are affiliate links or sponsored results allowed? If so, how are they disclosed? | Prevents hidden ranking conflicts. |
| Q-30 | P1 / Open | How often may the app check for source/config changes, and what stale-cache behavior is acceptable? | Controls freshness, latency, and external request volume. |

### Saudi delivery, prices, and ebooks

| ID | Priority / status | Question or decision | Why it matters |
|---|---|---|---|
| Q-31 | P0 / Open | Does “Saudi-only” mean the seller must be based in Saudi Arabia, or may any seller appear if it ships to Saudi Arabia? | Determines source eligibility. The confirmed minimum is that Saudi delivery must not be falsely asserted. |
| Q-32 | P0 / Open | What evidence is sufficient for “confirmed shipping to Saudi Arabia”: provider shipping policy, product page, destination-specific quote, or checkout confirmation? | Without a standard, status labels will be inconsistent. |
| Q-33 | P0 / Partial | Unconfirmed shipping must be labeled uncertain. Should an uncertain result still be shown with a warning, or hidden until verified? | User requested explicit uncertainty, but the display/action policy is open. |
| Q-34 | P1 / Open | Does shipping need to work to all Saudi addresses, or can it vary by city, postal code, or delivery method? | Some sellers restrict remote areas or require address details. |
| Q-35 | P1 / Open | Should price include shipping, taxes, customs, and currency conversion, or show only the provider’s listed price? | Avoids misleading comparisons. |
| Q-36 | P1 / Open | How fresh must price, stock, and shipping data be? Should the app display the time checked? | Availability and shipping terms can change. |
| Q-37 | P0 / Partial | Which ebook providers are candidates, and how will Saudi-region availability be verified? Google Books reports country-specific saleability for its own eBookstore; this does not guarantee user account/DRM/device access or cover other stores. No ebook provider is approved. | Ebook licensing may differ from physical delivery. | 
| Q-38 | P1 / Open | Should ebook results distinguish purchase, subscription, library loan, sample, and free/legal access? | These are not interchangeable forms of availability. |
| Q-39 | P1 / Open | Should format/DRM/device compatibility be shown, and is that in initial scope? | A purchasable ebook may not work on the user’s device or preferred reader. |

### Privacy, security, and access

| ID | Priority / status | Question or decision | Why it matters |
|---|---|---|---|
| Q-40 | P0 / Open | How are the approximately five users invited and authenticated? Named accounts, invite codes, or device enrollment? | Private APK distribution does not itself provide private data access. |
| Q-41 | P0 / Open | Must user lists and history be isolated per person, or shared within the group? | Determines authorization and data model. |
| Q-42 | P0 / Open | Which data may leave the device: source photo, extracted text, ISBN/title search, account identifier, diagnostics? | External searches reveal query terms to providers; backend logs may also retain them. |
| Q-43 | P1 / Open | What should be stored locally or remotely, and how long? Is there an export/delete requirement? | Defines retention and recovery. |
| Q-44 | P1 / Open | What is the response if a phone is lost, a user leaves, or an invite is shared? | Requires revocation and session/device handling. |
| Q-45 | P1 / Open | Which trust-boundary failures must the threat model cover: accidental data exposure, malicious provider/OCR content, or a compromised GitHub/release configuration? | “As secure as possible” needs concrete threats to test. |
| Q-46 | P1 / Open | What operational logs are allowed, and must they avoid titles/ISBNs or user identity? | Logs can unintentionally reveal reading/search activity. |
| Q-47 | P1 / Open | For private GitHub provider configuration and release metadata, is a server-side broker acceptable (rather than a client-held GitHub token), or should those capabilities be omitted? Who would maintain it? | A private GitHub token must not be embedded in the APK; a server has cost and maintenance implications. |

### Repository, APK distribution, and updates

| ID | Priority / status | Question or decision | Why it matters |
|---|---|---|---|
| Q-48 | P1 / Open | Should this plan-only repository later contain app code, or remain separate from a future code repository? | Keeps the plan authoritative and controls who sees source/build artifacts. |
| Q-49 | P1 / Open | Should provider configuration share this repo or live in a separate, more restricted repo? | Configuration edits can affect app behavior and must be reviewed. |
| Q-50 | P0 / Partial | How will the first APK reach the five users privately? Firebase App Distribution remains a no-cost candidate within published tester caps; Android's outside-Play developer-verification rollout begins in four countries in 2026 and expands globally in 2027. No distribution choice or eligibility is verified. | Initial installation and later updates are separate workflows; private tester delivery must not put secrets in the APK. | 
| Q-51 | P0 / Partial | How will private releases be fetched without placing a GitHub token in the APK? | GitHub release listing requires Contents read; GitHub App installation tokens can be scoped and expire after one hour. Use only server-side credentials; backend remains unselected. |
| Q-52 | P0 / Partial | What is the signing-key creation, storage, backup, access, and recovery procedure? APK updates require signing identity continuity; outside-Play package registration/developer verification may apply based on release date/region. No key exists, and custody, eligibility, registration, and recovery remain undecided. | Loss or compromise can make updates impossible or unsafe. | 
| Q-53 | P1 / Open | On launch, how often should the app check for updates, and should checks be manual as well? | Defines the “small launch check” and prevents unnecessary requests. |
| Q-54 | P1 / Partial | How should the app handle a failed/corrupt update, a downgrade, or a release that must be revoked? | Android update identity/signature must be preserved and version code cannot be lower; removing release metadata cannot uninstall offline APKs. Rollback/revocation/offline policy remains open. |
| Q-55 | P1 / Open | Which Android OS versions and device models must the app support? | APK installation and installer behavior vary by Android version/device policy. |
| Q-56 | P1 / Open | Is an in-app notice on next launch enough, or are push notifications required when users are not opening the app? | A launch check cannot alert a user who never launches the app. |
| Q-57 | P1 / Partial | The owner chose to leave the existing private-fork setting unchanged for now. This repo is private and owned by the personal GitHub account GoogleDevTeam, with allow_forking=true; the connected API rejected disabling it with HTTP 422, stating the setting can only be changed on organization-owned private repositories. A UI-only control was not verified. Whether to transfer the repository for stricter fork controls remains open. | Limit access to intended collaborators; private visibility does not prevent authorized users from cloning. Any organization transfer needs separate owner approval. |

### App behavior, accessibility, and operations

| ID | Priority / status | Question or decision | Why it matters |
|---|---|---|---|
| Q-58 | P1 / Open | What screen sizes/layouts, camera/photo permissions, and accessibility needs must v1 support? (OS/device coverage is Q-55.) | Sets supported devices and test matrix. |
| Q-59 | P1 / Open | Must list extraction and editing work offline? Which actions may require connectivity? | Determines OCR and offline/cache architecture. |
| Q-60 | P1 / Open | What loading, no-match, uncertain-match, provider-error, and offline states must the user see? | Makes failure behavior explicit instead of ambiguous. |
| Q-61 | P2 / Open | Are saved lists, history, favorites, export, or sharing required? | These add storage and privacy scope; none is approved yet. |
| Q-62 | P1 / Open | What support and recovery path exists if a provider or update service breaks? | A private app still needs an owner and recovery process. |
| Q-63 | P1 / Confirmed | The owner-confirmed budget target is $0 recurring cost, no payment card, and no unapproved paid service/overage/upgrade; candidate-specific eligibility and quota fit remain open under Q-82/Q-85. | This resolves the budget ceiling only; it does not approve any host/provider or account. |

### Quality and approval

| ID | Priority / status | Question or decision | Why it matters |
|---|---|---|---|
| Q-64 | P0 / Open | Can representative real lists be supplied for evaluation, with sensitive information removed? | Accuracy targets require examples rather than assumptions. |
| Q-65 | P0 / Open | What error rate is acceptable for OCR fields and final book matches? | “Confidence” needs a measurable meaning and safe threshold. |
| Q-66 | P1 / Open | What test evidence is required for Saudi shipping, ebook-region claims, provider outages, and updates? | Defines proof before labels or releases are trusted. |
| Q-67 | P0 / Confirmed | The project owner gives final approval of the complete plan and later scope changes. | Keeps decisions with the owner and prevents assistant assumptions. |
| Q-68 | P0 / Confirmed | The implementation gate opens only when the owner explicitly approves the complete plan and says to begin; no exact phrase is required. | Prevents an idea or discussion from being mistaken for build authorization. |

## 3. Legacy risk ID crosswalk

RISK_REGISTER.md is the only maintained risk inventory. The R-01–R-26 identifiers below are preserved as legacy aliases so earlier references remain searchable; they are not separate assessments and must not be renumbered or assigned new meanings. Use the canonical R1–R37 entries for owner, impact, likelihood rationale, prevention, detection, fallback, and residual risk.

| Legacy ID | Original concern | Canonical risk ID(s) |
|---|---|---|
| R-01 | OCR reads a title, author, or ISBN incorrectly. | R1, R3, R4 |
| R-02 | Rows/columns merge, split, or lose quantities. | R2 |
| R-03 | Unsupported handwriting, mixed formats, or language is treated as supported. | R1, R2, R36 |
| R-04 | Invalid or mistyped ISBN identifies another book. | R3 |
| R-05 | Different translation, volume, edition, or format is matched. | R4, R22 |
| R-06 | Confidence appears more precise than the evidence supports. | R1, R4 |
| R-07 | A provider listed in GitHub is mistaken for an active integration. | R5 |
| R-08 | Provider access changes, is blocked, or is not permitted. | R5, R6, R30, R34 |
| R-09 | A partial source set omits books while search appears comprehensive. | R5, R6, R30 |
| R-10 | Saudi shipping is inferred incorrectly from general seller information. | R7, R22, R27 |
| R-11 | Price, customs, tax, or delivery limits make an offer misleading. | R7, R22 |
| R-12 | Price or stock is stale when the seller is opened. | R27 |
| R-13 | Ebook is region-restricted, incompatible, or unavailable. | R8 |
| R-14 | Provider configuration points at untrusted endpoints or code. | R5 |
| R-15 | GitHub/provider/signing secret is extracted from source or APK. | R9, R18, R23, R24 |
| R-16 | One invited user sees another person’s data or access fails open. | R13, R19, R20, R25 |
| R-17 | Photo, title, or ISBN is retained or sent externally unexpectedly. | R12, R13 |
| R-18 | Phone is lost, an invite is shared, or a user is not revoked. | R13, R17, R19, R25, R26 |
| R-19 | APK is corrupted, substituted, downgraded, or signed with another key. | R10, R23 |
| R-20 | Android installer requires approval or blocks installation. | R11 |
| R-21 | Launch-only update checks miss inactive users or unavailable metadata. | R16, R37 |
| R-22 | GitHub, network, or backend outage disrupts search or updates. | R6, R16, R27, R29 |
| R-23 | Authorized collaborators create private forks or local copies. | R14 |
| R-24 | Scope drifts through unapproved small changes. | R15 |
| R-25 | Provider/service terms, prices, rights, or availability change. | R30, R32, R34 |
| R-26 | Five-person app costs or maintenance exceed expectations. | R29, R31, R32, R33, R35 |

Two uncovered concerns discovered during reconciliation now have distinct canonical IDs: R36 covers unsupported input being treated as reliable, and R37 covers launch-only update checks missing inactive users. R21 and R28 in the canonical register additionally cover performance/resource constraints without a direct legacy alias.

Likelihood is not numerically scored: there is no app runtime, representative sample set, selected provider set, or target-device evidence. See RISK_REGISTER.md and TEST_MATRIX.md; all referenced test cases remain planned, not run.

## 4. Dependencies and evidence to gather

- Representative book-list samples covering expected languages and layouts, with personal information removed.
- Provider-by-provider evidence: permitted search method, book coverage, Saudi delivery evidence, ebook-region evidence, rate limits, and expected cost.
- Android device/version matrix and actual signed-APK installation/update test.
- Proposed data-flow diagram showing what stays on-device, what reaches the backend, GitHub, and providers.
- Access-control and revocation design for the small group.
- Test dataset and expected outputs for OCR, ISBN validation, edition matching, uncertainty labels, and safe failure states.

Do not claim a dependency is solved until evidence is recorded in the plan.

## 5. Planning gate

No implementation starts until the owner explicitly approves the complete plan. At minimum, all P0 questions must be answered, every in-scope requirement must have acceptance criteria, selected provider feasibility must be checked, the privacy/security/data-flow design must be reviewed, and every material risk must have a mitigation, detection method, fallback, and residual-risk status. Any high-impact unresolved risk requires explicit owner acceptance or a scope change that removes the risk.

This gate reduces foreseeable errors; it cannot guarantee that no new defect will arise after implementation begins.

## 6. Newly confirmed top priority: security, safety, and performance

The owner has confirmed these are the product’s highest priorities. This changes how planning questions are ordered; it does not by itself choose a technical architecture or approve a feature. An APK cannot be guaranteed impossible to copy or reverse-engineer once it reaches a user. Plan for strict access control, minimal exposed secrets/data, signed releases, and documented residual risk rather than promising absolute secrecy.

| ID | Priority / status | Question or decision | Why it matters |
|---|---|---|---|
| Q-69 | P0 / Open | Which user/device abuse scenarios must the access model address: a forwarded APK, a lost device, shared credentials, a compromised member account, or reverse-engineering? | “Only people I allow” needs an explicit attacker and access model. |
| Q-70 | P0 / Open | Must every protected feature require an online authorization check, or must some features work offline? | Strict server-side gating improves revocation but reduces offline availability. |
| Q-71 | P0 / Open | Should each allowed person have a named account, an enrolled device, or both? How many devices may each use? | Defines access boundaries and how to prevent shared APKs from being useful to guests. |
| Q-72 | P0 / Open | What information must remain confidential: source code, credentials, provider config, search activity, or app concept? | Different information needs different controls; an app’s visible behavior cannot be hidden from its authorized users. |
| Q-73 | P1 / Open | Is obfuscation/tamper resistance acceptable as a deterrent, with the understanding it is not a guarantee? | Sets realistic reverse-engineering expectations and implementation cost. |
| Q-74 | P0 / Open | Is a private authenticated backend acceptable for invitation/membership authorization and revocation of the shared list, and who would maintain it? GitHub release/config brokering is separate under Q-47. | Strong access control and private GitHub access cannot depend on a secret embedded in the APK. |
| Q-75 | P1 / Open | What are acceptable performance targets for startup, OCR/review, provider search, and update checks, and on which devices? | Performance cannot be verified without a baseline and measurable targets. |
| Q-76 | P1 / Open | What should remain usable when the internet/backend is unavailable, and which protected operations must fail closed? | Balances security, safety, and availability. |
| Q-77 | P1 / Open | Are device-integrity checks/attestation required? What privacy, compatibility, cost, and bypass limits are acceptable? | Attestation may add friction and is not an absolute anti-tamper guarantee. |
| Q-78 | P1 / Open | What is the incident response for a leaked APK, stolen device, exposed key, or compromised user account? | Revoke, rotate, notify, and recover before the issue occurs. |

Reclassify Q-40, Q-41, Q-42, Q-45, Q-47, Q-50, Q-51, Q-52, Q-54, Q-55, and Q-58 as security/performance plan blockers. Their earlier priority labels are superseded by this owner-confirmed priority.


## 7. Owner-confirmed cost constraint and hosting discovery

The owner wants everything to be free. Safe default: target $0 ongoing cost for both owner and users; no paid services, automatic overages, or upgrades without explicit approval. Free-tier quotas/pauses and terms changes are risks, not hidden assumptions. Refer to docs/COST_AND_HOSTING.md for the dated research snapshot.

| ID | Priority / status | Question or decision | Why it matters |
|---|---|---|---|
| Q-79 | Confirmed | Can any app service require a payment card for its free tier? No. | The owner confirmed that a payment card must not be required; exclude any card-gated service even if it claims $0 usage. |
| Q-80 | P0 / Confirmed | At a free quota limit, stop or degrade the affected service without charging; any paid change needs explicit owner approval. | Prevents accidental spend; current safe default is no paid overage. |
| Q-81 | P0 / Open | Are sleep/idle pauses, cold starts, or manual republishing acceptable? | Free tiers can conflict with security/performance availability goals. |
| Q-82 | P0 / Partial | Which candidate backend can securely support invite-only auth, private GitHub release access, and revocation at $0? | Published Cloudflare/Firebase/Supabase limits narrow candidates but do not prove a complete no-card, native-auth, revocable path; Supabase may pause after low activity. No backend approved. |
| Q-83 | P0 / Partial | Do Amazon.sa, Noon, and later providers offer a permitted no-cost search/data method? | Amazon's official Saudi Creators API has affiliate eligibility and qualifying-sales prerequisites; Noon docs are seller/partner/integrator APIs and do not establish public consumer search. Verify owner eligibility and use terms; no provider approved and no scraping. |
| Q-84 | P1 / Open | If no free permitted method exists for a source, should that source be omitted? | Safe default: omit it rather than create a paid or noncompliant dependency. |
| Q-85 | P1 / Partial | Are email/invitation delivery, storage, monitoring, and build minutes required, and can each stay within free quotas? | Firebase App Distribution has published no-cost tester caps; private GitHub Actions quotas are account-plan-dependent and excess is billable. Other required services remain unknown. |
| Q-86 | P1 / Open | How frequently will the group use the app, and are free-tier pauses, cold starts, or manual republishing acceptable at that cadence? | Connects expected use cadence to acceptable free-tier availability. |
| Q-87 | P1 / Confirmed | At a quota limit or plan change, stop or degrade safely; no automatic paid upgrade or overage. A different policy needs explicit owner approval. | Defines safe failure and prevents automatic charges. |
