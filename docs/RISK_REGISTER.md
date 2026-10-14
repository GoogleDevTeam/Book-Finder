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