# Book-Finder product plan

**Version:** 0.1  
**Last updated:** 2026-10-15  
**Status:** Draft — discovery; not approved for implementation  
**Audience:** Product owner and the small invited user group

## 1. Product goal

Help a small, private group turn a photographed list of multiple books into reviewable book records, identify likely titles and editions, and find physical-book or ebook options while clearly showing whether delivery or access in Saudi Arabia is confirmed, uncertain, or unavailable.

## 2. Confirmed scope

- Android app distributed as an APK.
- Input is an image of a list of books, not a photo of book covers. For v1, printed lists and typed/on-screen lists are confirmed input formats. English is the selected list-content language. Handwritten lists, Arabic/other-language recognition, and mixed-format pages were not selected for v1; each page should use one format. A list can contain multiple books and visible details such as title, author, ISBN, or edition. Whether to capture a copy quantity per title/edition remains open.
- Users can search using ISBN, book name/title, or both. For a scanned list entry, a missing, invalid, or unreadable ISBN must be entered or corrected before that entry is searched.
- The app shows text-reading/OCR confidence separately from book-match confidence; acceptable-match criteria remain open.
- Different editions and versions are flagged for the user. When several likely editions match, preselect the closest and flag its differences; the criteria for “closest” remain open.
- The minimum useful outcome is a likely book match with edition differences; the user can choose a physical-book or ebook preference.
- Search for physical books and ebooks online.
- Saudi Arabia is the required delivery/access market. If shipping is uncertain, label it as uncertain rather than confirmed.
- Ebook regional availability or restrictions must also be flagged.
- The provider list is to be maintained through GitHub. Amazon.sa and Noon are examples only; neither is yet a confirmed integration.
- Users should receive an in-app update notice from GitHub-based releases. The app may make a small version check on launch. The user approves Android installation.
- The intended group is approximately five people and uses one shared group list. The owner chose to leave the group relationship/category unspecified for v1. Authentication, invitations, membership/permissions, user attribution, and whether history is shared or separate remain open.
- Primary use context is library/school book lists. V1 scans lists and finds book matches/sourcing options; it does not track an existing collection’s holdings, locations, or loans. Requested-copy quantity remains open under Q-08.
- Work must follow this plan. New scope requires explicit approval and a plan update first.

## 3. Proposed user journey

1. User photographs or uploads a book list.
2. App extracts separate book entries and their visible fields.
3. User reviews/corrects uncertain entries and enters or corrects any missing, invalid, or unreadable ISBN before that scanned entry is searched.
4. App searches by available ISBN/title/author details and displays likely matches with separate text-reading/OCR and book-match confidence.
5. App preselects the closest likely edition and flags differences rather than treating distinct versions as identical; the criteria for “closest” remain open.
6. App displays supported physical and ebook sources with explicit Saudi delivery or region status, source, and checked time where available; the user can choose a physical-book or ebook preference.
7. If a newer signed app release is available, the app notifies the user and offers the Android installer; installation requires user approval.

The details in this section beyond confirmed scope are proposed design direction and remain subject to open questions.

## 4. Required result states

- Text-reading/OCR confidence and book-match confidence must be visible separately; uncertain text or matches must not be presented as certain.
- Edition/version differences must be visible before a user treats a result as the exact requested book.
- Physical delivery must distinguish confirmed Saudi shipping, uncertain shipping, and no Saudi shipping. Unknown must never be labeled confirmed.
- Ebook results must show Saudi-region availability or uncertainty separately from physical shipping.
- A failed or unavailable provider must not be reported as having no matching books; show that the source could not be checked.

The exact evidence required for a “confirmed” shipping or region label is an open decision.

## 5. Privacy and security requirements

- Limit use to the intended small invited group. One shared group list is selected; the group relationship/category is intentionally unspecified for v1; the exact access-control method (authentication, invitations, membership/permissions, and revocation) remains undecided.
- Protect images, extracted lists, search history, provider credentials, GitHub credentials, and app-signing material.
- Do not put GitHub tokens, provider secrets, or signing keys in the APK or repository.
- Do not claim that private GitHub hosting alone makes app data or external searches private.
- Minimize collection and retention. Whether images are processed entirely on-device, and whether history is saved, remain open decisions.
- Verify app updates and preserve the trusted Android signing identity. Android installation must require user approval.

## 6. Non-goals unless explicitly approved later

- Building or publishing the app before this plan is approved.
- Adding providers merely because an example URL was mentioned.
- Automatic purchase, payment processing, or ebook hosting.
- Silent APK installation.
- Public user registration or public access.
- Uploading or retaining list photos by default.
- Handwritten lists and pages combining printed with typed/on-screen content are outside the selected v1 input scope. Behavior for an accidentally submitted mixed-format page remains to be specified.
- Features such as recommendations, social sharing, price alerts, barcode scanning, or saved shelves. These are not in the current scope.

## 7. Acceptance criteria for a future implementation

- Given a legible printed or typed/on-screen English list with one format per page and multiple book entries, the app presents separate entries for user review rather than one combined title.
- Given an uncertain extracted field or match, the app marks uncertainty and allows correction before searching; text-reading and book-match confidence are displayed separately.
- Given a scanned entry has a missing, invalid, or unreadable ISBN, the app requests a user-entered/corrected ISBN before searching that entry.
- Given multiple editions or versions, the app preselects the closest likely match and visibly flags differences; the rule for “closest” remains to be defined.
- Given physical-book and ebook options for a likely match, the user can choose a physical-book or ebook preference.
- Given uncertain Saudi shipping, the result says shipping is unconfirmed and does not imply that delivery is guaranteed.
- Given uncertain ebook-region access, the result flags the uncertainty.
- Given a new app release, the app can notify the user and invoke Android installation only after user approval.
- No feature outside the approved plan is implemented.

Quantitative OCR accuracy and response-time targets are intentionally unset until representative list samples and provider behavior are understood.

### Stable requirement IDs and traceability

IDs below identify confirmed scope only. An ID does not resolve its linked open questions or authorize implementation. Keep an ID stable once assigned; do not reuse retired IDs. Observable checks that depend on unresolved owner choices remain blocked until those choices are recorded.

| Requirement ID | Confirmed scope | Observable acceptance criterion | Planned verification |
|---|---|---|---|
| BF-REQ-001 | Android app distributed as an APK. | A release candidate is an Android-installable APK signed with the approved release identity; supported Android versions/devices must be selected before the compatibility result can pass. | TC-032, TC-033, TC-036; Q-52, Q-55, Q-58 |
| BF-REQ-002 | V1 accepts printed or typed/on-screen English book lists, one format per page; handwriting and mixed-format pages are not selected. | For a supported page containing multiple entries, each book is presented as a separate reviewable entry; unsupported input is not represented as reliably processed. | TC-001–TC-003, TC-041; Q-04, Q-06, Q-07 |
| BF-REQ-003 | Users review/correct uncertain scanned fields; a scanned entry with missing, invalid, or unreadable ISBN is not searched until the user supplies/corrects a valid ISBN. | Search is blocked for that entry until a valid user-confirmed ISBN is present; corrected fields are visible before search. | TC-004–TC-007, TC-041; Q-12, Q-16 |
| BF-REQ-004 | Text-reading/OCR confidence is shown separately from book-match confidence. | The result view presents two distinct confidence indicators; no unvalidated numeric threshold is implied. | TC-008, TC-009; acceptable-match rule remains open at Q-17 |
| BF-REQ-005 | Edition/version differences are flagged; when several likely editions match, the closest is preselected. | Edition differences are visible before the user treats a result as exact; the chosen candidate follows an owner-approved closeness rule. | TC-010, TC-011; Q-18, Q-19 |
| BF-REQ-006 | Minimum useful outcome is a likely book match with edition differences; user can choose physical-book or ebook preference. | The user can express the selected format preference on a likely match; exact ranking/filtering effects remain open. | TC-012; Q-01, Q-19 |
| BF-REQ-007 | Search physical books and ebooks online using only supported, permitted providers. | Each result identifies the source checked; unconfigured or unsupported sources are not reported as searched. | TC-013–TC-016, TC-021, TC-039–TC-040; Q-23–Q-27, Q-37 |
| BF-REQ-008 | Saudi Arabia is the required physical-delivery market; uncertain shipping must be labeled uncertain. | Confirmed, uncertain, unavailable, and unchecked delivery states are not conflated; unknown is never described as confirmed. | TC-017–TC-019; evidence and seller rules remain open at Q-31–Q-34 |
| BF-REQ-009 | Ebook Saudi-region availability or restrictions are flagged separately from physical shipping. | Ebook region status is separately identified and unknown access is not presented as confirmed. | TC-020; Q-37–Q-39 |
| BF-REQ-010 | The provider list is maintained through GitHub; Amazon.sa and Noon are examples only, not approved integrations. | A listed provider is treated as searchable only after its permitted method and support status are verified; an example alone never activates a source. | TC-021, TC-039–TC-040; Q-23–Q-26 |
| BF-REQ-011 | Users receive an in-app update notice for GitHub-based releases; Android installation requires user approval. | A verified signed update may be offered, but installation is not triggered without the user’s explicit Android approval. | TC-032–TC-034, TC-042; private delivery, signer custody, and supported devices remain open |
| BF-REQ-012 | Intended access is a small invited group (about five people) sharing one group list; privacy and security are top priorities. | Authorized group members can access the same shared list and unauthorized users are denied; invitation, identity, membership/permissions, revocation, and any user-specific history/data model must be approved before testing can pass. | TC-023–TC-025, TC-042; Q-02 resolved; Q-40–Q-47, Q-69–Q-78 |
| BF-REQ-013 | Protect images, extracted lists, search history, provider/GitHub credentials, and signing material; no tokens, provider secrets, or signing keys in the APK or repository. | Release/repository inspection finds no embedded secrets; data is not retained or transmitted outside the owner-approved data flow. | TC-026–TC-028, TC-039–TC-041, TC-042; retention and data flow remain open at Q-14, Q-15, Q-42, Q-43, Q-46 |
| BF-REQ-014 | Security, safety, and performance outrank convenience; uncertain states and provider failures are explicit. | Uncertain results remain uncertain, provider failure is distinct from a successful empty search, and bounded work does not leave routine startup hanging indefinitely. | TC-015–TC-020, TC-029, TC-031, TC-039–TC-041; Q-27, Q-32, Q-33, Q-75, Q-76 |
| BF-REQ-015 | Owner-confirmed $0 target and no-payment-card rule; no paid overage or automatic tier upgrade without explicit approval. | No card is requested and no unapproved charge/upgrade occurs; reaching a free limit stops or degrades the affected service. | TC-030; Q-79, Q-80, Q-81–Q-87 |
| BF-REQ-016 | Work follows the plan; new scope requires explicit approval and a plan update first. No automatic purchases or silent APK installation. | Every implemented behavior maps to an approved requirement ID; an unapproved feature, purchase, or silent install is absent. | TC-034, TC-035, TC-042; plan approval gate remains closed |

The requirements above organize the existing plan; they do not select providers, architecture, storage, authentication, Android support, or accuracy targets. Those remain open in OPEN_QUESTIONS.md.

## 8. Success measures to define

- Correct book rows and fields on representative user-provided lists.
- Rate of results users confirm as the intended title and edition.
- Correctness and freshness of Saudi shipping/ebook status labels.
- Successful update checks and user-approved updates.
- No unauthorized access or unexpected image retention.

Baselines and numeric targets are open decisions; do not invent them.

## 9. Highest priority: security, safety, and performance

**Owner-confirmed priority:** Security, safety, and performance are the top priorities for this product. The security goal is to ensure only people the owner authorizes can obtain protected access to the app and its services, and to make reverse-engineering as difficult as practical. Exact controls remain open until the threat model and distribution plan are agreed.

### Security requirements to settle and verify

- No public app distribution or unauthenticated access to protected app functions, data, provider configuration, or updates. The authorization and invitation model is not yet selected.
- Protect credentials, signing keys, private provider configuration, user data, and search history. Do not embed secrets in the APK or repository.
- Design for access revocation if a user or device is no longer authorized.
- Treat an APK as a client that an end user can copy and inspect. No Android APK can be guaranteed impossible to copy, decompile, or reverse-engineer. Obfuscation and tamper resistance can raise effort but are defense-in-depth, not a security boundary. Minimize sensitive logic/data in the APK and keep secrets or protected operations server-side where appropriate.
- Threat model, account/device controls, offline behavior, and server-side requirements are open decisions.

### Safety requirements

- Fail closed for protected data and update access when authorization or package verification fails.
- Keep uncertain OCR, edition matches, Saudi delivery, and ebook-region availability visibly uncertain.
- Do not make purchases or install updates without user approval.
- Request only permissions required by the approved flows and define safe behavior for provider/network failures.

### Performance requirements

- Security checks and the launch-time version check must not make routine app startup hang indefinitely.
- Measure scanning, review, search, and update-check behavior on agreed Android devices and representative lists. Numerical targets are open; do not invent them.
- Bound network waits, response sizes, retries, and local resource use; show clear degraded/offline states instead of leaving the UI unresponsive.
- On-device processing, caching, and concurrency are design options, not yet approved architecture decisions.

These requirements take priority during planning, but no implementation begins until the full plan and threat model are explicitly approved.


### Proposed access flow — not approved

A candidate to evaluate is: each invited person signs in once and enrolls an approved device; the app then renews a short-lived session automatically rather than prompting for a password on every launch. Protected search/update requests go through an authenticated backend, which checks whether the user/device remains allowed. A valid session can be attached to the request so authorization is checked without a separate sign-in screen for every action.

This is not a final architecture decision. One-time installation/activation with no later authorization check is not sufficient for reliable revocation or to stop a forwarded APK from accessing protected services. If online authorization is unavailable, protected server features should fail safely; whether non-sensitive local-only actions remain available offline is still open.


### Proposed automatic authorization and update checks — illustrative, not approved

These are two separate checks and should not be confused:

1. **Authorization check:** after an invited user signs in and enrolls an approved device, the service issues a limited session. The app may keep that session securely so the user does not re-enter a password for every screen.
2. **Protected request:** when the user searches a book or requests private provider/release data, the app sends the session over HTTPS to the service. The service checks that the account/device is still allowed, then performs or authorizes the protected request. A valid request proceeds; revoked/invalid access is denied and may require sign-in again. GitHub/provider credentials remain server-side.
3. **No network/service:** protected online search and private release access cannot be verified, so they should fail closed with a clear message rather than silently become public. Whether local-only capture/review can continue offline is undecided.
4. **Version check:** separately, the app makes a small, non-blocking request on launch when due. A service can check private GitHub release metadata and return only version information. If an update is available, the app notifies the user; the user starts and approves Android installation. Android verifies package signing continuity.

A version check by itself does not authorize use. A one-time check only at installation is not enough for revocation. The frequency of authorization refresh, session lifetime, sign-in method, device enrollment, exact service design, and offline grace period are open decisions. This flow is a planning example, not authorization to build it.


### Proposed performance approach — not approved

- Keep a valid, short-lived session on the device using Android-protected storage, so the user is not asked to sign in on each launch. Attach the session to protected search/update calls instead of making a separate authorization round trip for each screen. The server still verifies authorization for protected requests; caching authorization decisions longer can delay revocation.
- Run the small version check in the background and only when due. A conditional request (for example, ETag/If-None-Match) and server-side caching of GitHub release metadata can avoid downloading unchanged data. Do not let a version check block app startup. The check interval remains open.
- Prefer on-device list OCR if quality and device-performance tests support it; this avoids uploading the image and waiting for an OCR service. On-device OCR can use CPU, memory, and battery, so it must be measured on agreed devices.
- Search supported providers with bounded timeouts and controlled concurrency; show results as they arrive and clearly mark slow/unavailable sources. Use batching only where the provider supports it. Cache only data with an explicit freshness label and never represent stale shipping/stock as current.
- Measure launch, scan/review, search, and update-check behavior on representative devices and network conditions before setting numeric targets.

Security trade-off: keeping a session is not the same as repeatedly asking the user to authenticate. The server can verify the session during the protected request. Any offline use of cached authorization creates a revocation window; exact behavior remains undecided.


## 10. Zero-cost constraint

**Owner-confirmed preference:** Everything should be free. For planning, treat this as a target of $0 recurring cost for the owner and users, with no paid service, subscription, usage overage, or automatic tier upgrade unless the owner explicitly approves a plan change. No payment card or billing instrument may be required for a free tier. Any service that requires one is excluded unless the owner explicitly changes this requirement.

- Check hosting, database/auth, OCR/catalog/provider APIs, storage, email/invitations, app build/distribution, notifications, and monitoring for direct and indirect charges.
- Prefer free, permitted interfaces. If a retailer or ebook source has no permitted no-cost search method, mark it unsupported rather than add a paid API or prohibited scraping.
- If a free quota is reached, stop or degrade the affected feature and show a clear status; never silently incur charges.
- Free tiers may have usage caps, pauses, or terms that change. Performance and reliability must be evaluated alongside cost; do not call a sleeping/expiring service acceptable without owner approval.
- Hosting candidates and current official limits are tracked in docs/COST_AND_HOSTING.md. The host is not selected.
