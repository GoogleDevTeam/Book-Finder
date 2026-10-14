# Discovery catalog: questions, issues, dependencies, and safeguards

**Version:** 0.1  
**Last updated:** 2026-10-14  
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

Priority guide: **P0** blocks a reliable product/technical plan; **P1** must be decided before the affected feature is built; **P2** can be deferred only if the approved scope excludes it. Status is **Open** unless explicitly marked otherwise.

### Product purpose and users

| ID | Priority / status | Question or decision | Why it matters |
|---|---|---|---|
| Q-01 | P0 / Open | What exact job should the app complete for the user, and what should a successful session end with: a corrected list, a set of likely matches, or purchase options? | Prevents scope from drifting into unrelated book-management features. |
| Q-02 | P1 / Open | Who are the approximately five users, and do they need separate private data or one shared group list? | Determines identity, data separation, sharing, and access controls. |
| Q-03 | P1 / Open | How often and in what situation will the app be used (one-off list, recurring shopping, library/school list)? | Affects list persistence, speed, offline expectations, and what must be saved. |
| Q-04 | P1 / Open | Which languages should the app interface support? Is Arabic/RTL required, English, or both? | Affects layout, accessibility, OCR display, and testing. |
| Q-05 | P1 / Open | What is explicitly out of scope for version one? | Prevents optional ideas from becoming accidental requirements. |

### Book-list photo and extraction

| ID | Priority / status | Question or decision | Why it matters |
|---|---|---|---|
| Q-06 | P0 / Open | Are source lists printed, handwritten, typed on a screen, photographed from a document, or mixed? | OCR performance varies substantially by input type. |
| Q-07 | P0 / Open | Which languages/scripts appear in the lists, including mixed Arabic/English or transliterated names? | Determines OCR capability and false-match risks. |
| Q-08 | P0 / Open | What does “number of books” mean: number of entries, a quantity for each title, or both? | Prevents quantity from being confused with book count or duplicate titles. |
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
| Q-16 | P0 / Open | What should happen when ISBN is missing, invalid, or unreadable: title/author search, manual entry, or both? | Lists may lack ISBNs; ISBN validation alone cannot identify every book. |
| Q-17 | P0 / Open | What counts as an acceptable book match, and should OCR confidence and catalog-match confidence be shown separately? | Reading text correctly is different from matching the intended book. |
| Q-18 | P1 / Open | Which edition differences must be called out: publisher, year, language, translation, format, revised edition, volume, or ISBN? | Defines meaningful edition warnings. |
| Q-19 | P1 / Open | Should the app rank editions, or present alternatives without choosing? What should happen when the requested edition is unavailable? | Avoids substituting a different book without consent. |
| Q-20 | P1 / Open | How should duplicates, series/volume numbers, pseudonyms, alternate titles, and transliterations be handled? | Reduces duplicate results and incorrect merges. |
| Q-21 | P1 / Open | Which book metadata must be displayed, and what source should be considered authoritative when sources disagree? | Catalogs can disagree on title, author, ISBN, and edition. |
| Q-22 | P1 / Open | What confidence explanation is useful: a label, detected fields, match evidence, or all of these? | Makes confidence understandable rather than a misleading score. |

### Providers and online search

| ID | Priority / status | Question or decision | Why it matters |
|---|---|---|---|
| Q-23 | P0 / Open | Which providers are first-version candidates? Amazon.sa and Noon are examples only. | A bounded source list is testable; “everywhere online” is not. |
| Q-24 | P0 / Open | What exactly will be stored in the GitHub provider list: names/links, search templates, API configuration, or something else? | A list of URLs alone may not make a provider searchable or verifiable. |
| Q-25 | P0 / Open | For each provider, is there an official API or other permitted search method? Are credentials required? | Avoids blocked, brittle, or disallowed scraping and exposes credential needs. |
| Q-26 | P1 / Open | Who may add/edit providers, how are changes reviewed, and how does the app know a source is supported? | Prevents arbitrary or broken source entries from becoming active. |
| Q-27 | P1 / Open | What should happen when a provider times out, rate-limits, changes its site, or returns incomplete data? | A failed search must not look like “no book exists.” |
| Q-28 | P1 / Open | How should results be sorted or compared, and should the app show one result or results from every enabled provider? | Affects completeness, ranking, and user trust. |
| Q-29 | P2 / Open | Are affiliate links or sponsored results allowed? If so, how are they disclosed? | Prevents hidden ranking conflicts. |
| Q-30 | P1 / Open | How often may the app check for source/config changes, and what stale-cache behavior is acceptable? | Controls freshness, latency, and external request volume. |

### Saudi delivery, prices, and ebooks

| ID | Priority / status | Question or decision | Why it matters |
|---|---|---|---|
| Q-31 | P0 / Open | Does “Saudi-only” mean the seller must be based in Saudi Arabia, or may any seller appear if it ships to Saudi Arabia? | Determines source eligibility. The confirmed minimum is that Saudi delivery must not be falsely asserted. |
| Q-32 | P0 / Open | What evidence is sufficient for “confirmed shipping to Saudi Arabia”: provider shipping policy, product page, destination-specific quote, or checkout confirmation? | Without a standard, status labels will be inconsistent. |
| Q-33 | P0 / Open | Should uncertain-shipping results be shown with a warning, or hidden until verified? | User requested explicit uncertainty, but the display/action policy is open. |
| Q-34 | P1 / Open | Does shipping need to work to all Saudi addresses, or can it vary by city, postal code, or delivery method? | Some sellers restrict remote areas or require address details. |
| Q-35 | P1 / Open | Should price include shipping, taxes, customs, and currency conversion, or show only the provider’s listed price? | Avoids misleading comparisons. |
| Q-36 | P1 / Open | How fresh must price, stock, and shipping data be? Should the app display the time checked? | Availability and shipping terms can change. |
| Q-37 | P0 / Open | Which ebook providers are candidates, and how will Saudi-region availability be verified? | Ebook licensing may differ from physical delivery. |
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
| Q-45 | P1 / Open | What threat model matters: accidental exposure, a shared device, unauthorized group access, malicious provider response, or compromised GitHub? | “As secure as possible” needs concrete threats to test. |
| Q-46 | P1 / Open | What operational logs are allowed, and must they avoid titles/ISBNs or user identity? | Logs can unintentionally reveal reading/search activity. |
| Q-47 | P1 / Open | Is a private backend acceptable for mediating GitHub access and updates? Who will maintain it? | A private GitHub token must not be embedded in the APK; a server has cost and maintenance implications. |

### Repository, APK distribution, and updates

| ID | Priority / status | Question or decision | Why it matters |
|---|---|---|---|
| Q-48 | P1 / Open | Should this plan-only repository later contain app code, or remain separate from a future code repository? | Keeps the plan authoritative and controls who sees source/build artifacts. |
| Q-49 | P1 / Open | Should provider configuration share this repo or live in a separate, more restricted repo? | Configuration edits can affect app behavior and must be reviewed. |
| Q-50 | P0 / Open | How will the first APK reach the five users privately? | In-app updates solve later releases, not first installation. |
| Q-51 | P0 / Open | How will private releases be fetched without placing a GitHub token in the APK? | Determines whether an authenticated backend or another distribution mechanism is required. |
| Q-52 | P0 / Open | What is the signing-key creation, storage, backup, access, and recovery procedure? | Losing or exposing the key can block trusted updates or compromise users. |
| Q-53 | P1 / Open | On launch, how often should the app check for updates, and should checks be manual as well? | Defines the “small launch check” and prevents unnecessary requests. |
| Q-54 | P1 / Open | How should the app handle a failed/corrupt update, a downgrade, or a release that must be revoked? | A safe recovery path is needed before distribution. |
| Q-55 | P1 / Open | What Android versions/devices and installation permissions must be supported? | APK installation and installer behavior vary by Android version/device policy. |
| Q-56 | P1 / Open | Is an in-app notice on next launch enough, or are push notifications required when users are not opening the app? | A launch check cannot alert a user who never launches the app. |
| Q-57 | P1 / Open | What is the GitHub collaboration policy? The current personal-account repo is private, but GitHub would not let its private-fork setting be disabled. | Collaborators should be added only if needed; private forking is a known account-level limitation. |

### App behavior, accessibility, and operations

| ID | Priority / status | Question or decision | Why it matters |
|---|---|---|---|
| Q-58 | P1 / Open | What Android minimum version, screen sizes, camera permissions, and accessibility needs are required? | Sets supported devices and test matrix. |
| Q-59 | P1 / Open | Must list extraction and editing work offline? Which actions may require connectivity? | Determines OCR and offline/cache architecture. |
| Q-60 | P1 / Open | What loading, no-match, uncertain-match, provider-error, and offline states must the user see? | Makes failure behavior explicit instead of ambiguous. |
| Q-61 | P2 / Open | Are saved lists, history, favorites, export, or sharing required? | These add storage and privacy scope; none is approved yet. |
| Q-62 | P1 / Open | What support and recovery path exists if a provider or update service breaks? | A private app still needs an owner and recovery process. |
| Q-63 | P1 / Open | What ongoing hosting, API, OCR, or service costs are acceptable? | A free prototype can become costly or stop working if dependencies are overlooked. |

### Quality and approval

| ID | Priority / status | Question or decision | Why it matters |
|---|---|---|---|
| Q-64 | P0 / Open | Can representative real lists be supplied for evaluation, with sensitive information removed? | Accuracy targets require examples rather than assumptions. |
| Q-65 | P0 / Open | What error rate is acceptable for OCR fields and final book matches? | “Confidence” needs a measurable meaning and safe threshold. |
| Q-66 | P1 / Open | What test evidence is required for Saudi shipping, ebook-region claims, provider outages, and updates? | Defines proof before labels or releases are trusted. |
| Q-67 | P0 / Open | Who gives final approval of the complete plan and later scope changes? | Keeps decisions with the owner and prevents assistant assumptions. |
| Q-68 | P0 / Open | What exact phrase or action will mean “plan approved; begin implementation”? | Prevents an idea or discussion from being mistaken for build authorization. |

## 3. Foreseeable issue and prevention register

Likelihood and severity remain unscored until sample lists, selected providers, and the access architecture are known. Each item needs prevention, detection, a safe fallback, and documented residual risk before the implementation gate.

| ID | Failure mode / impact | Prevention | Detection and safe fallback |
|---|---|---|---|
| R-01 | OCR reads a title, author, or ISBN incorrectly, producing a wrong search. | Evaluate on representative languages and list styles; validate ISBNs; show field confidence and allow correction. | Compare extracted text with user confirmation; if uncertain, pause search and offer manual entry. |
| R-02 | Multiple rows/columns merge, split, or lose quantities. | Define supported layouts and data fields; keep quantity separate from entry count; show all rows for review. | Flag incomplete/low-confidence rows; preserve originals and allow row correction; never silently merge duplicates. |
| R-03 | Handwriting or mixed Arabic/English is unsupported by the chosen OCR. | Decide languages/input types before selection; test on real examples. | If unsupported, disclose limitation and use manual entry rather than claiming a reliable scan. |
| R-04 | Invalid ISBN or a one-digit error points to another book. | Check ISBN structure/check digit; cross-check title/author/edition metadata. | Flag mismatch and allow correction; fall back to title/author search. |
| R-05 | Same title matches another translation, volume, or edition. | Define edition fields and matching rules; show ISBN, language, publisher, year, format. | Surface alternatives and ask user to choose; do not auto-substitute. |
| R-06 | Confidence number looks precise but is not calibrated. | Decide how confidence is generated and what it means; separate OCR confidence from catalog-match confidence if approved. | Test against labeled examples; use plain uncertainty labels until a score is validated. |
| R-07 | Provider name in GitHub is mistaken for a functional integration. | Require each provider to have an approved method, support status, owner, and tests. | Show “not configured/unsupported” distinctly from “searched with no result.” |
| R-08 | A provider’s API/site changes, blocks access, or disallows the search method. | Verify terms and technical access before selecting provider; keep integrations isolated and maintainable. | Detect errors/rate limits; mark source unavailable; do not imply search completeness. |
| R-09 | Search results omit books because only a subset of sources is supported. | Display supported sources and which ones were checked; avoid “searched everywhere” claims. | Report per-source failure/incompleteness; let user retry only if approved. |
| R-10 | Saudi shipping is assumed from a general seller page but fails for the product/address. | Define evidence standards; test destination-specific status; label source and check time. | Use Confirmed / Unconfirmed / Does not ship states; never turn unknown into confirmed. |
| R-11 | Delivery cost, customs, taxes, or remote-area restrictions make a result misleading. | Decide which landed-cost fields are in scope; distinguish seller-listed price from total cost. | State what is excluded and ask user to confirm checkout total. |
| R-12 | Price or stock is stale when user opens the seller page. | Define freshness rules and cache duration; show last checked time if data is retrieved. | Recheck before presenting current availability when feasible; otherwise label as potentially changed. |
| R-13 | Ebook is geo-blocked, subscription-only, incompatible, or no longer licensed. | Separate ebook status from print shipping; define whether purchase/subscription/library access count. | Flag unknown or restricted regional status; link to source; do not promise access. |
| R-14 | Provider configuration is altered to call an untrusted endpoint or execute code. | Treat GitHub provider data as schema-validated data, never executable code; allowlist supported source types/domains. | Reject invalid or unexpected config; keep last known-good config and show config error. |
| R-15 | GitHub token, provider key, or signing key is extracted from APK/source. | Never place secrets in client bundle or repository; keep server credentials in managed secrets; restrict scopes. | Scan releases/repo; revoke and rotate exposed credentials; stop affected service access. |
| R-16 | One invited user sees another person’s list or search history. | Decide isolation model; enforce authorization server-side; minimize stored records. | Test cross-account access; fail closed; revoke access and investigate without retaining unnecessary content. |
| R-17 | Photo, book title, or ISBN is retained or sent externally unexpectedly. | Define data flow and retention; process locally where approved; disclose provider query sharing. | Audit network/log paths; provide delete/clear controls if storage is approved; avoid sensitive logs. |
| R-18 | Phone is lost, invite is shared, or a user should be removed. | Select authentication/device enrollment and revocation rules before build. | Revoke user/device access and sessions; stop access to private updates/config. |
| R-19 | APK is corrupted, substituted, downgraded, or signed with a different key. | Protect signing identity, verify provenance/integrity, use a secure release process, document recovery. | Refuse the package and keep installed app; provide a planned recovery/rollback procedure. |
| R-20 | Android installer requires user action or blocks the update. | Test actual sideload flow on target devices; explain permission and approval steps. | Keep old version usable; show status and a retry path; never claim a silent update. |
| R-21 | Update service is unavailable or users do not open the app. | Define launch-check cadence, cache policy, and whether push is needed. | Show last check; retry with backoff; accept that launch-only checks cannot notify inactive users. |
| R-22 | GitHub rate limits, network loss, or backend outage disrupts search/update. | Use bounded requests, timeouts, caching, and retries where approved. | Show source/service unavailable; do not misreport it as no match or current release. |
| R-23 | Personal private repo allows collaborators to fork privately. | Limit collaborator access; do not add people without need. | Review access periodically; move to an organization only if stronger fork controls become necessary. |
| R-24 | Scope drifts through “small improvements” or assumptions during implementation. | Require every change to reference an approved requirement ID before work; update plan first. | Stop unplanned work, propose change, and await explicit owner approval. |
| R-25 | External service terms, API pricing, catalog rights, or availability change. | Verify permitted use, costs, and data rights for every selected source before approving the plan. | Reassess source and update plan before replacing it; do not silently switch providers. |
| R-26 | Costs/maintenance exceed expectations for a five-person app. | Identify service owners, recurring costs, secrets rotation, and maintenance burden in design. | Set budget/operational limits before choosing paid dependencies; define graceful degradation. |

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
| Q-69 | P0 / Open | Which threat scenarios must the design address: casual forwarding, a lost device, a user sharing credentials, reverse-engineering, or a compromised account? | “Only people I allow” needs an explicit attacker and access model. |
| Q-70 | P0 / Open | Must every protected feature require an online authorization check, or must some features work offline? | Strict server-side gating improves revocation but reduces offline availability. |
| Q-71 | P0 / Open | Should each allowed person have a named account, an enrolled device, or both? How many devices may each use? | Defines access boundaries and how to prevent shared APKs from being useful to guests. |
| Q-72 | P0 / Open | What information must remain confidential: source code, credentials, provider config, search activity, or app concept? | Different information needs different controls; an app’s visible behavior cannot be hidden from its authorized users. |
| Q-73 | P1 / Open | Is obfuscation/tamper resistance acceptable as a deterrent, with the understanding it is not a guarantee? | Sets realistic reverse-engineering expectations and implementation cost. |
| Q-74 | P0 / Open | Is a private authenticated backend acceptable for access checks, revocation, and private update delivery? Who will maintain it? | Strong access control and private GitHub access cannot depend on a secret embedded in the APK. |
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
| Q-80 | P0 / Open | If a free quota is exhausted, should the app stop/degrade with no charge? | Prevents accidental spend; current safe default is no paid overage. |
| Q-81 | P0 / Open | Are sleep/idle pauses, cold starts, or manual republishing acceptable? | Free tiers can conflict with security/performance availability goals. |
| Q-82 | P0 / Open | Which candidate backend can securely support invite-only auth, private GitHub release access, and revocation at $0? | A free hosting quota alone does not provide a secure end-to-end design. |
| Q-83 | P0 / Open | Do Amazon.sa, Noon, and later providers offer a permitted no-cost search/data method? | Do not assume a free API exists or use prohibited scraping. |
| Q-84 | P1 / Open | If no free permitted method exists for a source, should that source be omitted? | Safe default: omit it rather than create a paid or noncompliant dependency. |
| Q-85 | P1 / Open | Are email/invitation delivery, storage, monitoring, and build minutes required, and can each stay within free quotas? | These indirect services can create costs even when the API host is free. |
| Q-86 | P1 / Open | How often will users use the app, and is a service that pauses after inactivity acceptable? | Usage frequency changes whether a free tier is reliable enough. |
| Q-87 | P1 / Open | How should the app behave when any free service reaches its quota or changes its plan? | Defines safe failure and prevents automatic charges. |
