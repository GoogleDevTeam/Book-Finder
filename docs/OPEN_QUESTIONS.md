# Discovery backlog and question index

Last reviewed: 2026-10-15  
Authority: This is the canonical, ID-mapped backlog and status/priority register for every discovery item Q-01–Q-87 in docs/DISCOVERY_CATALOG.md. Each ID appears once in this index. Keep the catalog status and priority synchronized; update this index, the catalog, any affected decision/risk record, and the project log together.

## Current gate

- 87 discovery IDs: 61 Open, 15 Partial, 11 Confirmed.
- 30 effective P0 blockers must be resolved before plan approval: Q-08, Q-17, Q-23, Q-24, Q-25, Q-31, Q-32, Q-33, Q-37, Q-40, Q-41, Q-42, Q-45, Q-47, Q-50, Q-51, Q-52, Q-54, Q-55, Q-58, Q-64, Q-65, Q-69, Q-70, Q-71, Q-72, Q-74, Q-81, Q-82, Q-83. This includes five P1 catalog items escalated by the audit: Q-45, Q-47, Q-54, Q-55, and Q-58. Q-67, Q-68, and Q-80 are now confirmed and no longer block approval.
- P1/P2 items must be resolved or explicitly deferred before plan approval. Research findings are not product approval.

## Handling labels

- Owner decision: pause for the owner’s preference, policy, or scope choice.
- Owner-provided evidence: request the named samples or operational information from the owner.
- Research—can proceed without owner input: gather public or technical evidence; a recommendation is not a decision.
- Research first, then owner decision: research options first, then return only the actual choice to the owner.
- Recorded decision: the answer is already documented; do not ask it again.
- Deferral: explicitly exclude the affected behavior from approved v1 scope; an open item is not an approved requirement.

## ID-mapped backlog

| ID | Catalog priority | Status | Handling | Gate | Question or recorded answer |
|---|---|---|---|---|---|
| Q-01 | — | Confirmed | Recorded decision | Resolved | Minimum useful v1 outcome is likely book matches with edition differences; the user can choose a physical-book or ebook preference. Exact-edition/default selection behavior remains open under Q-19. |
| Q-02 | P1 | Confirmed | Recorded decision | Resolved | About five users share one group list; the group relationship/category is intentionally unspecified for v1. Authentication, membership/permissions, and history separation remain open under Q-40/Q-41/Q-43. |
| Q-03 | P1 | Open | Owner decision | P1/P2—resolve or explicitly defer | Which usage contexts must v1 support: a one-time personal list, recurring shopping, or library/school inventory? |
| Q-04 | P1 | Open | Owner decision | P1/P2—resolve or explicitly defer | Which languages should the app interface support? Is Arabic/RTL required, English, or both? |
| Q-05 | P1 | Open | Owner decision | P1/P2—resolve or explicitly defer | What is explicitly out of scope for version one? |
| Q-06 | — | Confirmed | Recorded decision | Resolved | Printed and typed/on-screen lists were selected for v1, one format per page; handwritten and mixed-format pages were not selected. |
| Q-07 | — | Confirmed | Recorded decision | Resolved | English was selected as the list-content language; Arabic, mixed Arabic/English, and other-language recognition were not selected. |
| Q-08 | P0 | Open | Owner decision | Effective P0—resolve before plan approval | The owner is unsure whether “number of books” means list entries, copies per title/edition, or both. How should a line such as “Dune, 2nd edition ×3” be represented? |
| Q-09 | P1 | Open | Owner decision | P1/P2—resolve or explicitly defer | What layouts are common: one title per line, columns, tables, checklists, notes in margins? |
| Q-10 | P1 | Open | Owner decision | P1/P2—resolve or explicitly defer | Can one list span multiple pages or images? Should the user combine pages? |
| Q-11 | P1 | Open | Owner decision | P1/P2—resolve or explicitly defer | Which fields should be extracted, and which are optional (title, author, ISBN, edition, publisher, year, quantity, language, format, notes)? |
| Q-12 | P1 | Open | Owner decision | P1/P2—resolve or explicitly defer | How should uncertain characters and missing fields be shown and corrected? |
| Q-13 | P1 | Open | Owner decision | P1/P2—resolve or explicitly defer | Should the app reject poor photos, guide retaking, or allow manual correction even when OCR fails? |
| Q-14 | P1 | Open | Owner decision | P1/P2—resolve or explicitly defer | Should images be processed entirely on-device? May any image or extracted text be sent to a backend? |
| Q-15 | P1 | Open | Owner decision | P1/P2—resolve or explicitly defer | Should the app retain source images, corrected lists, or history? If yes, where, for how long, and how can a user delete them? |
| Q-16 | — | Confirmed | Recorded decision | Resolved | For a scanned list entry with a missing, invalid, or unreadable ISBN, require the user to enter or correct a valid ISBN before that entry is searched. |
| Q-17 | P0 | Partial | Owner decision remains | Effective P0—resolve before plan approval | Separate text-reading/OCR and book-match confidence are confirmed; the acceptable-match criterion remains open. |
| Q-18 | P1 | Open | Owner decision | P1/P2—resolve or explicitly defer | Which edition differences must be called out: publisher, year, language, translation, format, revised edition, volume, or ISBN? |
| Q-19 | P1 | Partial | Owner decision remains | P1/P2—resolve or explicitly defer | Preselect the closest edition and flag differences is confirmed; closeness criteria and fallback remain open. |
| Q-20 | P1 | Open | Owner decision | P1/P2—resolve or explicitly defer | How should duplicates, series/volume numbers, pseudonyms, alternate titles, and transliterations be handled? |
| Q-21 | P1 | Open | Owner decision | P1/P2—resolve or explicitly defer | Which book metadata must be displayed, and what source should be considered authoritative when sources disagree? |
| Q-22 | P1 | Open | Owner decision | P1/P2—resolve or explicitly defer | What confidence explanation is useful: a label, detected fields, match evidence, or all of these? |
| Q-23 | P0 | Partial | Research first, then owner decision | Effective P0—resolve before plan approval | Which metadata, retail, and ebook providers are acceptable v1 candidates? Google Books (metadata/Google eBookstore) and Open Library (human-oriented metadata) are research candidates; Amazon.sa eligibility and Noon consumer search remain unverified. No provider is selected. |
| Q-24 | P0 | Open | Research first, then owner decision | Effective P0—resolve before plan approval | What exactly will be stored in the GitHub provider list: names/links, search templates, API configuration, or something else? |
| Q-25 | P0 | Partial | Research—can proceed without owner input | Effective P0—resolve before plan approval | For each provider, is there an official API or other permitted search method, and are credentials required? First-party docs were reviewed for Google Books, Open Library, Amazon.sa, and Noon; use-case terms, account eligibility, costs, live coverage, and final provider-specific permission are not fully verified. |
| Q-26 | P1 | Open | Owner decision | P1/P2—resolve or explicitly defer | Who may add/edit providers, how are changes reviewed, and how does the app know a source is supported? |
| Q-27 | P1 | Partial | Owner decision remains | P1/P2—resolve or explicitly defer | Provider failures must be shown as unchecked, not as no results; retry/backoff and partial-response behavior remain open. |
| Q-28 | P1 | Open | Owner decision | P1/P2—resolve or explicitly defer | How should results be sorted or compared, and should the app show one result or results from every enabled provider? |
| Q-29 | P2 | Open | Owner decision | P1/P2—resolve or explicitly defer | Are affiliate links or sponsored results allowed? If so, how are they disclosed? |
| Q-30 | P1 | Open | Owner decision | P1/P2—resolve or explicitly defer | How often may the app check for source/config changes, and what stale-cache behavior is acceptable? |
| Q-31 | P0 | Open | Owner decision | Effective P0—resolve before plan approval | Does “Saudi-only” mean the seller must be based in Saudi Arabia, or may any seller appear if it ships to Saudi Arabia? |
| Q-32 | P0 | Open | Research first, then owner decision | Effective P0—resolve before plan approval | What evidence is sufficient for “confirmed shipping to Saudi Arabia”: provider shipping policy, product page, destination-specific quote, or checkout confirmation? |
| Q-33 | P0 | Partial | Owner decision remains | Effective P0—resolve before plan approval | Unconfirmed shipping must be labeled uncertain; whether to show candidates with a warning or hide them until verified remains open. |
| Q-34 | P1 | Open | Owner decision | P1/P2—resolve or explicitly defer | Does shipping need to work to all Saudi addresses, or can it vary by city, postal code, or delivery method? |
| Q-35 | P1 | Open | Owner decision | P1/P2—resolve or explicitly defer | Should price include shipping, taxes, customs, and currency conversion, or show only the provider’s listed price? |
| Q-36 | P1 | Open | Owner decision | P1/P2—resolve or explicitly defer | How fresh must price, stock, and shipping data be? Should the app display the time checked? |
| Q-37 | P0 | Partial | Research first, then owner decision | Effective P0—resolve before plan approval | Which ebook providers are candidates, and how will Saudi-region availability be verified? Google Books reports country-specific saleability for its own eBookstore; this does not guarantee user account/DRM/device access or cover other stores. No ebook provider is approved. |
| Q-38 | P1 | Open | Owner decision | P1/P2—resolve or explicitly defer | Should ebook results distinguish purchase, subscription, library loan, sample, and free/legal access? |
| Q-39 | P1 | Open | Owner decision | P1/P2—resolve or explicitly defer | Should format/DRM/device compatibility be shown, and is that in initial scope? |
| Q-40 | P0 | Open | Owner decision | Effective P0—resolve before plan approval | How are the approximately five users invited and authenticated? Named accounts, invite codes, or device enrollment? |
| Q-41 | P0 | Open | Owner decision | Effective P0—resolve before plan approval | Must user lists and history be isolated per person, or shared within the group? |
| Q-42 | P0 | Open | Owner decision | Effective P0—resolve before plan approval | Which data may leave the device: source photo, extracted text, ISBN/title search, account identifier, diagnostics? |
| Q-43 | P1 | Open | Owner decision | P1/P2—resolve or explicitly defer | What should be stored locally or remotely, and how long? Is there an export/delete requirement? |
| Q-44 | P1 | Open | Owner decision | P1/P2—resolve or explicitly defer | What is the response if a phone is lost, a user leaves, or an invite is shared? |
| Q-45 | P1 | Open | Owner decision | Effective P0—resolve before plan approval | Which trust-boundary failures must the threat model cover: accidental data exposure, malicious provider/OCR content, or a compromised GitHub/release configuration? |
| Q-46 | P1 | Open | Owner decision | P1/P2—resolve or explicitly defer | What operational logs are allowed, and must they avoid titles/ISBNs or user identity? |
| Q-47 | P1 | Open | Research first, then owner decision | Effective P0—resolve before plan approval | For private GitHub provider configuration and release metadata, is a server-side broker acceptable (rather than a client-held GitHub token), or should those capabilities be omitted? Who would maintain it? |
| Q-48 | P1 | Open | Owner decision | P1/P2—resolve or explicitly defer | Should this plan-only repository later contain app code, or remain separate from a future code repository? |
| Q-49 | P1 | Open | Owner decision | P1/P2—resolve or explicitly defer | Should provider configuration share this repo or live in a separate, more restricted repo? |
| Q-50 | P0 | Partial | Research first, then owner decision | Effective P0—resolve before plan approval | How will the first APK reach the five users privately? Firebase App Distribution remains a no-cost candidate within published tester caps; Android's outside-Play developer-verification rollout is initially limited to four countries from 2026-09-30 and expands globally in 2027. No distribution choice, eligibility, account activation, or private onboarding path is verified. |
| Q-51 | P0 | Partial | Research first, then owner decision | Effective P0—resolve before plan approval | How will private releases be fetched without placing a GitHub token in the APK? GitHub release listing requires Contents read; GitHub App installation tokens can be repository/permission-scoped and expire after one hour. Backend/auth flow remains unselected. |
| Q-52 | P0 | Partial | Research first, then owner decision | Effective P0—resolve before plan approval | What is the signing-key creation, storage, backup, access, and recovery procedure? APK updates require signing identity continuity; outside-Play package registration/developer verification may apply based on release date/region. No key exists, and custody, eligibility, registration, and recovery are undecided. |
| Q-53 | P1 | Open | Owner decision | P1/P2—resolve or explicitly defer | On launch, how often should the app check for updates, and should checks be manual as well? |
| Q-54 | P1 | Partial | Research first, then owner decision | Effective P0—resolve before plan approval | How should the app handle a failed/corrupt update, a downgrade, or a release that must be revoked? Android update guidance requires preserved app/signing identity and a version code no lower than installed; removal from GitHub cannot uninstall existing/offline copies. Rollback, revocation, and offline behavior remain open. |
| Q-55 | P1 | Open | Owner decision | Effective P0—resolve before plan approval | Which Android OS versions and device models must the app support? |
| Q-56 | P1 | Open | Owner decision | P1/P2—resolve or explicitly defer | Is an in-app notice on next launch enough, or are push notifications required when users are not opening the app? |
| Q-57 | P1 | Partial | Owner decision remains | P1/P2—resolve or explicitly defer | The owner chose to leave private forking unchanged for now; transferring the repository for stricter controls remains undecided. |
| Q-58 | P1 | Open | Owner decision | Effective P0—resolve before plan approval | What screen sizes/layouts, camera/photo permissions, and accessibility needs must v1 support? (OS/device coverage is Q-55.) |
| Q-59 | P1 | Open | Owner decision | P1/P2—resolve or explicitly defer | Must list extraction and editing work offline? Which actions may require connectivity? |
| Q-60 | P1 | Open | Owner decision | P1/P2—resolve or explicitly defer | What loading, no-match, uncertain-match, provider-error, and offline states must the user see? |
| Q-61 | P2 | Open | Owner decision | P1/P2—resolve or explicitly defer | Are saved lists, history, favorites, export, or sharing required? |
| Q-62 | P1 | Open | Owner decision | P1/P2—resolve or explicitly defer | What support and recovery path exists if a provider or update service breaks? |
| Q-63 | P1 | Confirmed | Recorded decision | Resolved | The owner-confirmed budget target is $0 recurring cost, no payment card, and no unapproved paid service/overage/upgrade; candidate-specific eligibility and quota fit remain open under Q-82/Q-85. |
| Q-64 | P0 | Open | Owner-provided evidence required | Effective P0—resolve before plan approval | Can representative real lists be supplied for evaluation, with sensitive information removed? |
| Q-65 | P0 | Open | Owner decision | Effective P0—resolve before plan approval | What error rate is acceptable for OCR fields and final book matches? |
| Q-66 | P1 | Open | Research first, then owner decision | P1/P2—resolve or explicitly defer | What test evidence is required for Saudi shipping, ebook-region claims, provider outages, and updates? |
| Q-67 | P0 | Confirmed | Recorded decision | Resolved | The project owner gives final approval of the complete plan and later scope changes. |
| Q-68 | P0 | Confirmed | Recorded decision | Resolved | The implementation gate opens only when the owner explicitly approves the complete plan and says to begin; no exact phrase is required. |
| Q-69 | P0 | Open | Owner decision | Effective P0—resolve before plan approval | Which user/device abuse scenarios must the access model address: a forwarded APK, a lost device, shared credentials, a compromised member account, or reverse-engineering? |
| Q-70 | P0 | Open | Owner decision | Effective P0—resolve before plan approval | Must every protected feature require an online authorization check, or must some features work offline? |
| Q-71 | P0 | Open | Owner decision | Effective P0—resolve before plan approval | Should each allowed person have a named account, an enrolled device, or both? How many devices may each use? |
| Q-72 | P0 | Open | Owner decision | Effective P0—resolve before plan approval | What information must remain confidential: source code, credentials, provider config, search activity, or app concept? |
| Q-73 | P1 | Open | Owner decision | P1/P2—resolve or explicitly defer | Is obfuscation/tamper resistance acceptable as a deterrent, with the understanding it is not a guarantee? |
| Q-74 | P0 | Open | Owner decision | Effective P0—resolve before plan approval | Is a private authenticated backend acceptable for invitation/membership authorization and revocation of the shared list, and who would maintain it? GitHub release/config brokering is separate under Q-47. |
| Q-75 | P1 | Open | Research first, then owner decision | P1/P2—resolve or explicitly defer | What are acceptable performance targets for startup, OCR/review, provider search, and update checks, and on which devices? |
| Q-76 | P1 | Open | Owner decision | P1/P2—resolve or explicitly defer | What should remain usable when the internet/backend is unavailable, and which protected operations must fail closed? |
| Q-77 | P1 | Open | Owner decision | P1/P2—resolve or explicitly defer | Are device-integrity checks/attestation required? What privacy, compatibility, cost, and bypass limits are acceptable? |
| Q-78 | P1 | Open | Research first, then owner decision | P1/P2—resolve or explicitly defer | What is the incident response for a leaked APK, stolen device, exposed key, or compromised user account? |
| Q-79 | — | Confirmed | Recorded decision | Resolved | Can any app service require a payment card for its free tier? No. |
| Q-80 | P0 | Confirmed | Recorded decision | Resolved | At a free quota limit, stop or degrade the affected service without charging; any paid change needs explicit owner approval. |
| Q-81 | P0 | Open | Owner decision | Effective P0—resolve before plan approval | Are sleep/idle pauses, cold starts, or manual republishing acceptable? |
| Q-82 | P0 | Partial | Research first, then owner decision | Effective P0—resolve before plan approval | Which candidate backend can securely support invite-only auth, private GitHub release access, and revocation at $0? Cloudflare Workers/D1, Firebase Spark/Auth, and Supabase Free have relevant published limits, but no complete no-card, native-auth, revocable backend path is verified; Supabase may pause after 7 days of low activity. No option selected. |
| Q-83 | P0 | Partial | Research first, then owner decision | Effective P0—resolve before plan approval | Do Amazon.sa, Noon, and later providers offer a permitted no-cost search/data method? Official research found Amazon.sa Creators API but it requires target-marketplace Associates enrollment, 10 qualifying sales in 30 days, API registration, and credentials; Noon documentation covers partner/seller/integrator APIs, not a public consumer search API. Owner eligibility and compliant fit remain unverified; no provider is approved. |
| Q-84 | P1 | Open | Owner decision | P1/P2—resolve or explicitly defer | If no free permitted method exists for a source, should that source be omitted? |
| Q-85 | P1 | Partial | Research first, then owner decision | P1/P2—resolve or explicitly defer | Are email/invitation delivery, storage, monitoring, and build minutes required, and can each stay within free quotas? Firebase App Distribution is no-cost within published tester caps; private GitHub Actions quotas depend on account plan and overage is billed. Email, storage, monitoring, exact account quotas, and required services remain open. |
| Q-86 | P1 | Open | Owner decision | P1/P2—resolve or explicitly defer | How frequently will the group use the app, and are free-tier pauses, cold starts, or manual republishing acceptable at that cadence? |
| Q-87 | P1 | Confirmed | Recorded decision | Resolved | At a quota limit or plan change, stop or degrade safely; no automatic paid upgrade or overage. A different policy needs explicit owner approval. |

Do not create duplicate prompts in this file. To add a newly discovered question, assign the next Q-ID in DISCOVERY_CATALOG.md and add exactly one matching row here.