# Book-Finder foreseeable scenario register

**Version:** 0.1  
**Recorded:** 2026-10-14  
**Status:** Planning inventory; not a requirements approval, test report, or guarantee of completeness.  
**Related records:** DISCOVERY_CATALOG.md, OPEN_QUESTIONS.md, RISK_REGISTER.md, PLAN_AUDIT.md, REMEDIATION_RUNDOWN.md.

## How to use this register

This is a broad first-pass inventory of foreseeable conditions across the proposed app lifecycle. It expands the existing questions and risk lists into concrete situations worth deciding, preventing, detecting, or testing. It cannot list every possible event: real examples, provider behavior, device diversity, implementation, and security testing will reveal more.

The safe outcomes below are planning recommendations, not all confirmed behavior. The existing approved plan and owner-confirmed decisions remain authoritative. Any behavior outside them needs explicit plan approval before implementation. Question IDs point to the detailed open-question catalog; risk IDs refer to the existing overlapping risk lists and should be reconciled before they are treated as canonical.

## A. Scope, decisions, and change control

- **SC-001** A discussion, suggestion, or prototype is mistaken for approval to build. Keep the implementation gate explicit; require the owner's clear approval of the complete plan.
- **SC-002** A developer fixes a defect that has no approved requirement ID. Stop before changing the app; propose the behavior and acceptance test first. (Q-67, Q-68)
- **SC-003** A new provider, data field, permission, or collection behavior slips in as a “small improvement.” Treat it as a scope change and get approval.
- **SC-004** APP_PLAN, DECISIONS, OPEN_QUESTIONS, and DISCOVERY_CATALOG disagree. Mark one source authoritative, reconcile statuses, and record the correction before approval.
- **SC-005** A requirement is vague or cannot be verified. Do not invent a threshold; define an observable pass condition with the owner.
- **SC-006** A research result is mistaken for owner consent or a selected product. Keep feasibility evidence, recommendation, and confirmed decision as distinct statuses.

## B. Installation, identity, authorization, and revocation

- **SC-007** An invited person forwards the APK or installation link. Private distribution alone cannot revoke a copied client; define which protected services require authorization. (Q-40, Q-50, Q-69–Q-74)
- **SC-008** A person outside the invited group obtains an invite, activation code, or account. Define invite expiry, enrollment, limits, and recovery before choosing a mechanism.
- **SC-009** A user shares credentials or uses more devices than allowed. Device/account limits and response are open; do not claim a limit exists until enforced and tested. (Q-40, Q-44, Q-71)
- **SC-010** A phone is lost, sold, or stolen. Define how to revoke sessions/devices and what local data remains accessible after revocation. (Q-43, Q-44, Q-78)
- **SC-011** An authorized user leaves the group. Revoke future protected access; define whether stored local information can be wiped or only becomes inaccessible to services.
- **SC-012** A session expires, is replayed, or is invalidated while the app is in use. Protected calls should be denied safely and explain how the user can reauthorize, if that flow is approved.
- **SC-013** The authorization service is slow or unavailable. Do not fail open for protected operations; define whether local-only review can continue offline. (Q-47, Q-70, Q-76)
- **SC-014** Account recovery is abused or unavailable. Define recovery ownership and safeguards; do not use a weak shared secret merely to avoid service costs.
- **SC-015** A user/device is revoked while a cached session is still valid. Decide and test the maximum revocation delay; do not describe cached authorization as immediate revocation. (Q-44, Q-70, Q-71)

## C. Photo capture, image import, and permissions

- **SC-016** The user denies camera permission. Explain the limitation and offer only an approved alternative input method; do not repeatedly prompt without user action. (Q-13, Q-58)
- **SC-017** The user cancels camera, file picker, or image selection. Return safely to the prior state without creating an empty or misleading book record.
- **SC-018** The image is blank, nearly blank, or contains no book list. Detect or disclose that extraction found no usable entries; do not report “no matching books.”
- **SC-019** The photo is blurry, low-resolution, overexposed, dark, shadowed, glared, or partly obscured. Mark affected fields uncertain and allow retake/manual correction rather than silently guessing. (Q-06, Q-12, Q-13)
- **SC-020** The page is rotated, skewed, curved, cropped, or photographed at perspective. Detect poor orientation/layout when feasible; preserve uncertainty and provide a safe correction path.
- **SC-021** The list spans multiple pages/images. Multi-page combining is open; do not merge pages or infer ordering unless the user-approved flow defines it. (Q-10)
- **SC-022** The image uses an unsupported file type, huge dimensions, or corrupt encoding. Reject gracefully with a clear supported-input message; bound memory and processing. (Q-06, Q-58)
- **SC-023** An image includes personal information beyond book details. Avoid retaining or uploading it unless explicitly approved; provide a clear data-flow statement. (Q-14, Q-15, Q-42)
- **SC-024** The user selects an image from shared/cloud storage or a source requiring another app permission. Request only needed access and avoid copying more than required. (Q-58)

## D. OCR, segmentation, and correction

- **SC-025** Printed and typed/on-screen lists are selected for v1, one format per page; handwritten and mixed-format pages are out of scope. Decide how to message accidental mixed-format submissions without treating them as reliable input. (Q-06)
- **SC-026** English was selected for list content. Arabic, mixed Arabic/English, and other languages were not selected; transliterated names within English lists may still need representative samples. Do not claim unselected language support. (Q-07)
- **SC-027** If Arabic interface support or Arabic list content is later approved, right-to-left text, mixed direction, punctuation, or digits may reorder. Arabic list content is not currently selected; interface language remains open. (Q-04, Q-07)
- **SC-028** A title wraps across lines or includes subtitles, series, volume, or notes. Avoid splitting or joining fields without review. (Q-09, Q-11)
- **SC-029** The page has columns, a table, checkboxes, handwritten annotations, or marginal notes. Decide which layouts are supported; do not merge adjacent rows. (Q-09)
- **SC-030** A row is split into two, two rows are merged, or reading order changes. Present individual reviewable entries and preserve the visible source association where approved. (Q-08, Q-09)
- **SC-031** Quantity is interpreted as row count, or duplicate titles are collapsed. Keep quantity separate from entry count; exact meaning is still an owner decision. (Q-08)
- **SC-032** A scanned entry has a missing, partly read, wrong, or check-digit-invalid ISBN. Mark it uncertain and require a user-entered/corrected valid ISBN before searching that entry; if none is supplied, leave that entry unsearched. Whether to let other entries proceed while this one is blocked remains open. (Q-16)
- **SC-033** OCR returns a plausible but wrong title/author. Separate text-reading confidence from book-match confidence if approved; require correction when uncertainty is material. (Q-12, Q-17, Q-22)
- **SC-034** No text is extracted or extraction returns duplicate/empty rows. Explain extraction failure distinctly from a successful search with no match; permit only approved manual entry/correction. (Q-12, Q-13)
- **SC-035** The app is interrupted, backgrounded, killed, or the device runs low on memory during extraction. Define safe recovery, temporary-data cleanup, and whether the work can resume. (Q-15, Q-59)

## E. Book identity, catalog matching, and editions

- **SC-036** A valid ISBN finds no catalog record. Offer title/author search or manual handling only as approved; state that no match was found in checked sources, not that the book does not exist. (Q-16)
- **SC-037** ISBN, title, author, or publisher data conflict across catalogs. Show source disagreement or uncertainty; do not silently choose an authority without a decision. (Q-21)
- **SC-038** Multiple books share a title, or authors have similar/transliterated names. Show evidence and alternatives; avoid presenting a weak match as certain. (Q-17, Q-20, Q-22)
- **SC-039** The result is a different edition, translation, language, publisher, year, format, volume, or revised version. Preselect the closest and flag differences; define the closeness rule and when it may be treated as exact. (Q-18, Q-19)
- **SC-040** ISBN differs but the edition may be the same, or metadata says the same ISBN but details differ. Preserve ambiguity for user review rather than deduplicating automatically. (Q-18, Q-20, Q-21)
- **SC-041** A series title, volume number, pseudonym, alternate title, or transliteration causes duplicate or wrong matches. Define supported normalization and show enough evidence to distinguish. (Q-20)
- **SC-042** A match has no author, edition, or publication metadata. Lower confidence or mark fields unknown; do not fill missing facts from an unverified source. (Q-17, Q-21)
- **SC-043** Confidence appears numeric but is uncalibrated or misleading. Do not imply statistical precision; validate against labeled samples before setting a numeric threshold. (Q-17, Q-22, Q-65)
- **SC-044** No exact edition is available but similar editions exist. Closest-match preselection is approved; define how to signal that no exact candidate exists and what fallback is appropriate. (Q-19)

## F. Provider configuration and searches

- **SC-045** A provider is listed in GitHub but has no working or permitted search method. Show unsupported/not configured, not searched/no match. (Q-23–Q-25)
- **SC-046** Provider config has a typo, invalid URL, duplicate entry, or missing fields. Validate against a fixed schema and reject safely; do not execute arbitrary config. (Q-24, Q-26)
- **SC-047** A config change adds an untrusted endpoint or changes search behavior without review. Define who can edit/review and how the app verifies configuration provenance. (Q-26, Q-49)
- **SC-048** Provider terms, API, website, or eligibility changes. Recheck before use/release; disable or mark unavailable until permitted access is confirmed. (Q-25, Q-27, Q-83)
- **SC-049** Credentials are missing, revoked, expired, or over-scoped. Keep credentials off-device; report source-specific failure and rotate/restrict credentials through managed secrets. (Q-25, Q-42, Q-51)
- **SC-050** Provider rate-limits, throttles, blocks, or returns quota errors. Bound retries and distinguish rate-limit/unavailable from no results. (Q-27, Q-30)
- **SC-051** Provider returns malformed, partial, stale, duplicate, or unexpected data. Validate responses, identify missing fields, and do not overstate completeness. (Q-27, Q-28, Q-36)
- **SC-052** One provider fails while another succeeds. Show per-source status; do not let a partial result imply all sources were checked. (Q-27, Q-28)
- **SC-053** Every checked provider returns zero results. Distinguish a successful empty result from a source that could not be checked; state which sources were searched. (Q-27, Q-28)
- **SC-054** Search takes too long or the user leaves/cancels. Bound waits; define cancellation and whether late results may appear. (Q-27, Q-30, Q-75)
- **SC-055** Search terms reveal reading interests to an external provider or backend logs. Minimize transmitted data, disclose the data flow, and avoid logging titles/ISBNs unless specifically approved. (Q-42, Q-46)
- **SC-056** A metadata source returns book details but no offer, shipping, stock, or ebook entitlement. Keep metadata separate from market availability. (Q-21, Q-25, Q-37)
- **SC-057** Search results are incomplete because only a subset of providers is supported. Show coverage limits and do not claim an exhaustive web search. (Q-23, Q-28)

## G. Physical-book delivery and ebook access

- **SC-058** A seller's general policy suggests shipping to Saudi Arabia but the specific item/destination is unverified. Mark unconfirmed; do not promote a policy page to confirmed item-level delivery. (Q-31–Q-34)
- **SC-059** Delivery varies by city, postal code, address, stock location, shipping method, or seller. Define whether address details are collected; otherwise preserve uncertainty. (Q-34, Q-42)
- **SC-060** Seller is outside Saudi Arabia but ships there, or is Saudi-based but will not ship to the user's address. Keep seller location and delivery eligibility as distinct facts. (Q-31)
- **SC-061** Shipping, taxes, customs, or remote-area fees change the total price. Do not imply a landed total unless all required components are supported and fresh. (Q-35, Q-36)
- **SC-062** Stock or shipping information is stale, cached, or changed at checkout. Show freshness/source when available and never present stale data as a current guarantee. (Q-36)
- **SC-063** A result is out of stock, preorder, used, or a marketplace listing with an uncertain seller. Define which offer types are in scope and disclose the source status. (Q-28, Q-36)
- **SC-064** The provider cannot distinguish a Saudi destination or fails to provide shipping evidence. Label unknown/unconfirmed, not “ships to Saudi Arabia.” (Q-32, Q-33)
- **SC-065** An ebook is listed but restricted, unavailable, or licensed differently in Saudi Arabia. Separate regional entitlement from print delivery and state uncertainty. (Q-37)
- **SC-066** Ebook access is purchase, subscription, loan, sample, or free access. Define which types count and label them distinctly if included. (Q-38)
- **SC-067** Ebook format, DRM, reader/device, or account country prevents use after purchase. Do not equate listing/purchase availability with successful reading access. (Q-39)
- **SC-068** Ebook availability or physical shipping cannot be checked because the source is down. Report check failure, not unavailable/no book. (Q-27, Q-32, Q-37)
- **SC-069** Affiliate/sponsored placement could influence ranking or links. Keep commercial relationships out unless approved and visibly disclosed. (Q-29)
- **SC-101** The user prefers physical books or ebooks, but a matched title has no result, uncertain regional access, or only the other format. Keep title-match confidence separate from offer availability; do not silently substitute a different edition or imply that a preferred format is available. The exact format-preference effect, closest-edition criteria, and fallback remain open. (Q-01, Q-19, Q-27, Q-38, Q-39)

## H. Privacy, retention, data separation, and logs

- **SC-070** Images, extracted text, search terms, history, favorites, or identifiers are stored unexpectedly. Define collection and retention before build; default to no unapproved persistence. (Q-14, Q-15, Q-42, Q-43)
- **SC-071** A list/photo is sent to OCR, backend, catalog, or provider without the user understanding it. Map each data transfer and obtain the required product decision/disclosure. (Q-14, Q-42)
- **SC-072** One user's data is visible to another member. Decide shared vs isolated data and test cross-account access; fail closed. (Q-02, Q-41)
- **SC-073** Data remains after sign-out, revocation, deletion, app uninstall, backup, or device restore. Define lifecycle semantics and verify actual device/backend behavior. (Q-15, Q-43, Q-44)
- **SC-074** Crash reports, analytics, debug output, or support screenshots contain titles, ISBNs, images, or identities. Define a minimal logging policy and scrub sensitive fields. (Q-46)
- **SC-075** GitHub/provider/signing secrets enter source, build artifacts, APK, logs, or a public link. Scan before release; revoke and rotate if exposed. (Q-51, Q-52)

## I. Availability, cost, and performance

- **SC-076** Network is absent, intermittent, captive, or switches during a task. Define which local actions remain usable and how protected calls fail safely. (Q-59, Q-70, Q-76)
- **SC-077** Backend sleeps, cold-starts, pauses, or reaches free-tier limits. Show service unavailable and avoid unexpected charges; no-card/zero-cost behavior is a hard constraint. (Q-80–Q-87)
- **SC-078** A service requires a card, enables auto-upgrade, or charges overage despite a nominally free tier. Exclude it unless the owner changes the confirmed rule. (Q-79, Q-82, Q-85)
- **SC-079** OCR/search/update check consumes excessive time, CPU, memory, battery, or bandwidth. Measure on agreed devices and bound work; numeric limits are still open. (Q-75, Q-81, Q-86)
- **SC-080** Launch waits indefinitely for authentication, GitHub, or a version check. Keep startup bounded; proposed asynchronous checking still needs approval and verification. (Q-53, Q-75)
- **SC-081** Concurrent provider calls flood a service or exhaust free quotas. Define and test request limits, retries, cache duration, and backoff per permitted provider. (Q-27, Q-30, Q-82, Q-87)
- **SC-082** A cache shows an old title/price/stock/delivery state as current. Label freshness and never use stale availability as confirmed. (Q-30, Q-36)
- **SC-083** A free service changes its limits/terms or becomes unavailable after approval. Reassess before release; never silently move the app to a paid tier. (Q-82, Q-87)

## J. Android compatibility, accessibility, and app lifecycle

- **SC-084** Android version, device manufacturer, screen size, storage, or language differs from the test device. Support scope and device matrix remain open; do not promise untested compatibility. (Q-55, Q-58)
- **SC-085** Screen reader, text scaling, contrast, keyboard navigation, or reduced-motion needs expose inaccessible controls. Define accessibility requirements and test them on supported devices. (Q-58)
- **SC-086** Arabic/RTL and English/LTR mixed content breaks order, truncates titles, or misplaces controls. Test locale switching, mixed scripts, and bidirectional text. (Q-04, Q-07)
- **SC-087** App is backgrounded, process-killed, rotated, or device storage is low during work. Define recoverable state and cleanup; avoid losing or duplicating records unexpectedly. (Q-15, Q-59)
- **SC-088** Permission is denied permanently or changed in Android settings. Explain how to continue with approved alternatives; do not assume permission remains granted. (Q-58)
- **SC-089** User taps twice, retries, or returns to an old result. Prevent duplicate actions where possible and label results with checked time/source. (Q-27, Q-36)
- **SC-090** Empty, loading, uncertain, partial, error, denied, or stale states look identical. Give each materially different state distinct wording/action; final UX copy remains open. (Q-60)

## K. Release, signing, recovery, and incident response

- **SC-091** First APK cannot be fetched privately by an invited user. Define initial distribution separately from in-app updates. (Q-50)
- **SC-092** Update metadata is public or GitHub credentials are embedded in the app to read a private release. Keep secrets server-side if an owner-approved service is selected. (Q-51)
- **SC-093** Signing key is lost, leaked, unavailable, or held by one person without backup. Define protected custody, backup, authorized release access, and recovery before first release. (Q-52, Q-78)
- **SC-094** Update package has wrong signer, corrupt bytes, unexpected package identity, downgrade, or compromised provenance. Refuse installation; preserve the installed app and explain recovery. (Q-54)
- **SC-095** Android requires per-source install permission or explicit user approval. Explain the system step; never claim silent installation. (Q-54, Q-55)
- **SC-096** A bad release needs rollback, revocation, or replacement but users are offline. Define what can be revoked server-side and what cannot be recalled from a device. (Q-54, Q-78)
- **SC-097** User ignores launch-time checks or does not open the app. A launch-only notice cannot reach an inactive user; decide whether that limitation is acceptable. (Q-53, Q-56)
- **SC-098** Build/signing pipeline or release account is compromised. Restrict access, review provenance, revoke credentials, and document incident ownership. (Q-52, Q-78)
- **SC-099** Provider, auth, data, or signing incident occurs and no one knows whom to notify or how to recover. Define an owner and response steps before release. (Q-62, Q-78)
- **SC-100** A security/update check fails during an incident. Fail closed for protected operations; make clear which local functions, if any, remain available. (Q-54, Q-70, Q-76)

## How this connects to tests and decisions

Before implementation, convert each in-scope scenario into a test case with: requirement ID, trigger/setup, expected result, safe fallback, target device/provider/version, pass criterion, evidence, and unresolved owner decision. Some scenarios should become non-goals after the owner bounds v1; that is a documented scope decision, not a missing risk. Numeric accuracy/latency thresholds must wait for representative sanitized lists, provider constraints, and agreed devices.

Priority decision groups and the existing Q-01–Q-87 descriptions remain in DISCOVERY_CATALOG.md. The effective P0 blocker IDs are listed in REMEDIATION_RUNDOWN.md. The two old risk inventories still need an ID crosswalk; this scenario register does not silently merge or renumber them.

## Limitations

This is not literally every event that could happen. It is a structured, reviewable starting set based on the current proposed workflow. It has not been validated against real photos, selected Android devices, live provider responses, a threat model, or an implemented application. Review it again after owner answers, provider feasibility work, architecture selection, prototype testing, and each release.
