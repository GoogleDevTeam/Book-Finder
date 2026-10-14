# Risk register

**Last updated:** 2026-10-14  
Risk ratings are not assigned yet; impact and likelihood need evidence from sample lists, provider tests, and the chosen privacy model.

| ID | Risk | Prevention | Fallback / required behavior |
|---|---|---|---|
| R1 | OCR fails on handwriting, language, blur, rotation, or layout. | Test representative list photos; show field-level confidence; allow edits before search. | Let users enter or correct entries manually; never silently search guessed fields. |
| R2 | Rows, quantities, or multiple columns are mis-segmented. | Treat each detected row as a separate editable record; preserve quantity separately from entry count. | Show a review table; do not auto-merge duplicates. |
| R3 | ISBN digits are misread. | Validate ISBN format/check digit and cross-check returned book metadata. | Mark invalid/uncertain ISBN and allow title/author search or manual correction. |
| R4 | Search matches the wrong book, edition, translation, or format. | Show bibliographic details and confidence; flag alternatives and differences. | Require user review; never imply an exact edition match without evidence. |
| R5 | A provider list entry is mistaken for a working integration. | Separate provider configuration from integration capability; document the approved method per source. | Mark source unsupported/unavailable; do not claim it was searched. |
| R6 | Provider changes its website/API, blocks requests, or rate-limits access. | Use permitted interfaces, maintain source-specific adapters, and monitor failures. | Show provider check failure and timestamp; do not present failure as “no book found.” |
| R7 | Shipping status is wrong, stale, or only discovered at checkout. | Define evidence for confirmed status; keep checked time and source; test Saudi destination explicitly. | Label uncertain; do not call it confirmed or guaranteed. |
| R8 | Ebook is listed but unavailable in Saudi Arabia or restricted by region. | Treat ebook region status separately from physical shipping; verify with source. | Flag unknown/restricted status and avoid availability promises. |
| R9 | Private GitHub credentials leak through the APK or provider configuration. | Keep credentials server-side; use an authenticated backend if private repository access is needed. | Revoke exposed credentials, rotate them, and stop affected update/config access. |
| R10 | Malicious, corrupted, or wrong APK is offered as an update. | Protect signing key; require a trusted signer; verify release integrity and origin. | Refuse update and retain installed version; report verification failure. |
| R11 | Android requires user confirmation, delaying updates. | Make the in-app notification and install flow clear; test on supported Android versions. | Keep current version usable; show update status and retry option. |
| R12 | Book searches expose reading interests to external providers. | Minimize fields sent; explain external searches; avoid retaining server-side history by default. | Offer manual/limited search where practical; do not claim searches are invisible to providers. |
| R13 | Private access is shared, lost, or not revoked after a device/user change. | Choose an invitation model with revocation and least privilege. | Revoke account/device access and prevent future release/config access. |
| R14 | GitHub private forking cannot be disabled for this personal-account repository. | Share repository access only with intended collaborators; avoid adding access unnecessarily. | Consider an organization-owned repo only if tighter fork controls become a requirement. |
| R15 | Scope expands during implementation without approval. | Treat this plan as the only approved baseline; require a documented change proposal. | Stop affected work until the plan is updated and the owner explicitly approves. |
| R16 | GitHub, network, or backend is unavailable. | Keep app behavior non-blocking; use safe caching and retry/backoff where approved. | Show last-check status; never treat stale data as current or confirmed. |

## Items that need testing before build approval

- OCR on real list photos in expected languages and writing styles.
- Provider search, source permissions, and Saudi shipping evidence for each candidate.
- Private release/config access and user revocation.
- Update signing and installation confirmation on target Android versions.

## Additional risks raised by the owner’s top priorities

| ID | Risk | Prevention | Detection / fallback |
|---|---|---|---|
| R17 | An authorized recipient forwards or copies the APK to an unauthorized person. A private repository alone cannot stop this after download. | Keep initial downloads and releases private; require authorization for protected app services and data; decide account/device enrollment and revocation before implementation. | Revoke users/devices and tokens; deny protected service access. State clearly that local offline features may still be copyable if included in the APK. |
| R18 | An APK recipient decompiles the app and discovers its code, endpoints, or embedded configuration. | Put no secrets in the client; minimize sensitive logic and data in the package; keep protected operations server-side where justified; use obfuscation only as an extra barrier. | Review released package contents and dependency manifests; remove exposed secrets immediately and rotate them. Do not promise impossible-to-crack protection. |
| R19 | A stolen/shared login or enrolled device gives an unwanted guest access. | Choose strong authorization, least privilege, session/device controls, and a revocation process. Exact authentication controls remain open. | Detect/review unusual access where privacy permits; revoke sessions/devices and require reauthorization. |
| R20 | Security checks or backend outages either block legitimate users or fail open and expose protected data. | Define protected operations, service boundaries, timeout behavior, and fail-closed rules before implementation. | Test denied, expired, offline, and unavailable states; protect sensitive operations when authorization cannot be verified and explain the limitation. |
| R21 | Authentication, attestation, OCR, or slow providers make the app feel unresponsive or drain resources. | Set measurable targets on agreed devices; bound calls/retries; avoid blocking launch on update checks; test realistic lists and network conditions. | Track timing without logging book content; use progress, cancellation, and explicit unavailable states. |
| R22 | Wrong OCR/match/shipping status leads a user to buy the wrong edition or believe delivery is guaranteed. | Require review of uncertain matches; distinguish confidence and edition; define evidence for confirmed Saudi delivery; never auto-purchase. | Show source and check time where available; correct the status and document the incident; keep uncertain results labeled uncertain. |
| R23 | Signing key loss or compromise prevents safe updates or lets an attacker publish a malicious APK. | Define protected key generation, access, backup, recovery, and release authorization before build. | Verify signer/release integrity; refuse untrusted packages; maintain a documented recovery plan. |
| R24 | Attempts to hide all app internals create false confidence while leaving credentials or sensitive logic in the APK. | Set a realistic threat model; classify what must be private; never rely on obfuscation as the sole protection. | Review the built APK and network flows before release; explicitly accept documented residual reverse-engineering risk. |

Likelihood and severity remain unscored until the threat model, distribution design, and test devices are decided.
