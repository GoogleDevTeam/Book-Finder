# Risk register

**Last reviewed:** 2026-10-15  
**Canonical register:** R1–R37 in this file. Legacy catalog IDs R-01–R-26 are aliases cross-walked in DISCOVERY_CATALOG.md; they are not a second risk inventory.

## Rating and ownership rules

- Project owner is accountable for mitigation decisions and residual-risk acceptance; an implementation/test owner must be named before build approval.
- Impact is qualitative and justified per risk below. Likelihood is **Unrated** for each risk because there is no application runtime, representative test dataset, selected provider set, target-device matrix, or production history. This is a reasoned evidence gap, not a zero likelihood.
- Prevention and fallback are recorded per risk. Detection references planned cases in TEST_MATRIX.md; those cases are not yet run. A risk remains Open until mitigation/evidence is reviewed and any residual risk requiring acceptance is explicitly accepted by the owner.

## Canonical risk treatment register
### R1 — OCR misreads title, author, or ISBN

- **Owner:** Project owner (risk acceptance); implementation/test owner TBD.
- **Impact:** Moderate — an incorrect field can trigger a wrong search; review can stop it before selection.
- **Likelihood:** Unrated — no runtime/sample/provider/device evidence exists to support a probability score.
- **Prevention:** Test printed/typed English samples; expose field uncertainty and allow correction before search.
- **Detection / test:** TC-001–TC-009: compare sanitized fixture to extracted fields and verify uncertain fields block search; real accuracy target Q-64/Q-65.
- **Safe fallback:** Mark uncertain; require correction or leave entry unsearched.
- **Residual risk:** Open; Recognition errors will remain; no representative sample set or accuracy threshold exists.

### R2 — Rows, columns, or quantities are mis-segmented

- **Owner:** Project owner (risk acceptance); implementation/test owner TBD.
- **Impact:** Moderate — merged/split rows can omit or duplicate books.
- **Likelihood:** Unrated — no runtime/sample/provider/device evidence exists to support a probability score.
- **Prevention:** Keep entries reviewable and quantity distinct from entry count; do not silently merge duplicates.
- **Detection / test:** TC-001, TC-002, TC-004, TC-005: verify row count and corrections on synthetic layouts; complex layout scope Q-08–Q-11.
- **Safe fallback:** Pause search for uncertain rows; allow user review/correction once behavior is approved.
- **Residual risk:** Open; Human review can still miss a segmentation error; copy quantity remains undecided.

### R3 — ISBN digit error identifies a different book

- **Owner:** Project owner (risk acceptance); implementation/test owner TBD.
- **Impact:** High — a valid-looking wrong ISBN can return a different title.
- **Likelihood:** Unrated — no runtime/sample/provider/device evidence exists to support a probability score.
- **Prevention:** Validate ISBN structure/check digit and compare returned metadata; user enters/corrects missing or invalid values.
- **Detection / test:** TC-006, TC-007: missing/invalid/unreadable scanned ISBN must block that entry until a valid user-confirmed value exists.
- **Safe fallback:** Do not search the scanned entry until the required valid ISBN is supplied.
- **Residual risk:** Open; A valid ISBN can still be mistyped or have conflicting catalog metadata; require result review.

### R4 — Wrong title, edition, translation, or format is matched

- **Owner:** Project owner (risk acceptance); implementation/test owner TBD.
- **Impact:** High — a user could rely on the wrong book or edition.
- **Likelihood:** Unrated — no runtime/sample/provider/device evidence exists to support a probability score.
- **Prevention:** Show bibliographic evidence and confidence; flag edition differences and require review.
- **Detection / test:** TC-008–TC-012: inspect confidence separation and edition flags; closest-match criterion is blocked by Q-17/Q-19.
- **Safe fallback:** Show alternatives/uncertainty; do not imply exact match or auto-purchase.
- **Residual risk:** Open; Metadata sources may be wrong and the closest-edition rule is unresolved.

### R5 — A provider-list entry is mistaken for a working integration

- **Owner:** Project owner (risk acceptance); implementation/test owner TBD.
- **Impact:** Moderate — unsupported sources can create misleading search coverage.
- **Likelihood:** Unrated — no runtime/sample/provider/device evidence exists to support a probability score.
- **Prevention:** Require an approved method, explicit support status, schema validation, and no executable config.
- **Detection / test:** TC-013, TC-014, TC-021: verify only approved sources are queried; blocked by Q-23–Q-26.
- **Safe fallback:** Mark source unsupported/not configured; do not say it was searched.
- **Residual risk:** Open; Provider terms or endpoints can change after verification.

### R6 — Provider changes, blocks, rate-limits, or returns incomplete data

- **Owner:** Project owner (risk acceptance); implementation/test owner TBD.
- **Impact:** Moderate — searches may be incomplete or unavailable.
- **Likelihood:** Unrated — no runtime/sample/provider/device evidence exists to support a probability score.
- **Prevention:** Use permitted interfaces, bounded retries, isolated adapters, and source-level status.
- **Detection / test:** TC-015, TC-016, TC-022: inject timeout/rate-limit/malformed/partial responses and distinguish failure from zero results.
- **Safe fallback:** Show source failed/unchecked and preserve successful sources separately.
- **Residual risk:** Open; External changes and outages remain outside the app owner's control; retry rules are not approved.

### R7 — Saudi shipping is wrong, stale, or only confirmed at checkout

- **Owner:** Project owner (risk acceptance); implementation/test owner TBD.
- **Impact:** High — a result may mislead a user about delivery or cost.
- **Likelihood:** Unrated — no runtime/sample/provider/device evidence exists to support a probability score.
- **Prevention:** Define acceptable destination evidence; show source and checked time; do not infer from a general policy page.
- **Detection / test:** TC-017–TC-019: positive confirmation is blocked by Q-31/Q-32; unknown stays uncertain and no-delivery is distinct.
- **Safe fallback:** Label uncertain/unconfirmed; never promise delivery.
- **Residual risk:** Open; Retailer availability can change between check and checkout; evidence standard remains open.

### R8 — Ebook listing is region-restricted or unusable in Saudi Arabia

- **Owner:** Project owner (risk acceptance); implementation/test owner TBD.
- **Impact:** High — a listed item may not be accessible to the user.
- **Likelihood:** Unrated — no runtime/sample/provider/device evidence exists to support a probability score.
- **Prevention:** Separate ebook region status from physical delivery; verify entitlement and restrictions.
- **Detection / test:** TC-020: exercise confirmed/restricted/unknown fixtures; provider/evidence choices remain Q-37–Q-39.
- **Safe fallback:** Mark unknown/restricted; do not promise access.
- **Residual risk:** Open; Account, device, license, or region rules may change after the check.

### R9 — GitHub, provider, or signing credentials leak

- **Owner:** Project owner (risk acceptance); implementation/test owner TBD.
- **Impact:** High — exposure can compromise releases, data, or accounts.
- **Likelihood:** Unrated — no runtime/sample/provider/device evidence exists to support a probability score.
- **Prevention:** Keep secrets out of APK/repository; use managed server-side secrets only if an approved backend exists; least privilege.
- **Detection / test:** TC-026–TC-028: scan source and release artifacts; inspect synthetic logs and data paths.
- **Safe fallback:** Revoke/rotate exposed credentials and stop affected protected service.
- **Residual risk:** Open; Copies may persist outside the repository; no backend or secret-handling design is selected.

### R10 — Corrupted, malicious, or wrong APK is offered

- **Owner:** Project owner (risk acceptance); implementation/test owner TBD.
- **Impact:** High — an untrusted update can compromise app users.
- **Likelihood:** Unrated — no runtime/sample/provider/device evidence exists to support a probability score.
- **Prevention:** Protect signing identity and verify package identity, provenance, and signer continuity.
- **Detection / test:** TC-032, TC-033: accept valid signer only; reject corrupted/mismatched package and retain installed version.
- **Safe fallback:** Refuse installation and keep current app; show failure/recovery guidance.
- **Residual risk:** Open; A compromised signing/build account can undermine the update chain; key recovery is undecided.

### R11 — Android requires user confirmation or blocks installation

- **Owner:** Project owner (risk acceptance); implementation/test owner TBD.
- **Impact:** Moderate — updates may be delayed or unavailable on some devices.
- **Likelihood:** Unrated — no runtime/sample/provider/device evidence exists to support a probability score.
- **Prevention:** Explain Android's approval flow and test supported OS/device versions.
- **Detection / test:** TC-032, TC-034, TC-036: confirm approval prompt, user cancellation, and device-specific behavior; Q-55/Q-58 open.
- **Safe fallback:** Keep installed version usable and offer a clear retry path.
- **Residual risk:** Open; Manufacturer/OS installer behavior varies and support devices are not selected.

### R12 — External searches expose reading interests

- **Owner:** Project owner (risk acceptance); implementation/test owner TBD.
- **Impact:** High — title/ISBN queries can reveal sensitive interests to providers or logs.
- **Likelihood:** Unrated — no runtime/sample/provider/device evidence exists to support a probability score.
- **Prevention:** Minimize query fields; document data flow; avoid retaining server-side history by default.
- **Detection / test:** TC-027, TC-028: inspect synthetic network/log traces after data-flow policy is approved.
- **Safe fallback:** Do not claim queries are invisible to providers; disable unapproved logging/transmission.
- **Residual risk:** Open; Providers may retain queries under their own terms; broker/direct-search choice is open.

### R13 — Private access is shared, lost, or not revoked

- **Owner:** Project owner (risk acceptance); implementation/test owner TBD.
- **Impact:** High — an unauthorized person could access protected functions/data.
- **Likelihood:** Unrated — no runtime/sample/provider/device evidence exists to support a probability score.
- **Prevention:** Choose invite/account/device limits, least privilege, and revocation before implementation.
- **Detection / test:** TC-023–TC-025: unauthorized access to the shared group list, revocation window, and any separately scoped user data/history; Q-40–Q-44/Q-69–Q-76 open.
- **Safe fallback:** Deny protected requests after revocation; keep unverified access fail-closed.
- **Residual risk:** Open; No authentication model or local-data wipe behavior is approved.

### R14 — Authorized collaborators create private forks or local copies

- **Owner:** Project owner (risk acceptance); implementation/test owner TBD.
- **Impact:** High — additional source copies can exist beyond the canonical repository.
- **Likelihood:** Unrated — no runtime/sample/provider/device evidence exists to support a probability score.
- **Prevention:** Limit collaborators, keep secrets out of repo, and avoid unapproved visibility/ownership changes.
- **Detection / test:** TC-038: current private personal repo reports allow_forking=true; API returned 422 when disabling; no actual fork is evidenced in the last repository read.
- **Safe fallback:** Treat copies as possible; do not transfer the repo or change access without explicit approval.
- **Residual risk:** Open; Owner chose to leave the setting unchanged; authorized collaborators can still clone. UI-only control is unverified.

### R15 — Scope expands through small unapproved changes

- **Owner:** Project owner (risk acceptance); implementation/test owner TBD.
- **Impact:** High — unapproved behavior can violate owner intent and privacy/cost constraints.
- **Likelihood:** Unrated — no runtime/sample/provider/device evidence exists to support a probability score.
- **Prevention:** Require BF-REQ IDs, approval, and plan-first change control for every app change.
- **Detection / test:** TC-035: review proposed changes against approved plan and change record.
- **Safe fallback:** Stop the change and write a proposal; wait for explicit approval.
- **Residual risk:** Open; Review processes can miss an unrecorded change; no implementation approval exists yet.

### R16 — GitHub, network, or backend outage disrupts search/update

- **Owner:** Project owner (risk acceptance); implementation/test owner TBD.
- **Impact:** Moderate — protected workflows may be unavailable.
- **Likelihood:** Unrated — no runtime/sample/provider/device evidence exists to support a probability score.
- **Prevention:** Bound requests and retries; label freshness; avoid blocking routine startup.
- **Detection / test:** TC-015, TC-016, TC-029, TC-031, TC-037: inject service/network failures and inspect status and responsiveness.
- **Safe fallback:** Show unavailable/unchecked; never call failure a no-match or use stale data as current.
- **Residual risk:** Open; Availability depends on unselected external services and the offline policy is open.

### R17 — Forwarded APK is used by an unauthorized person

- **Owner:** Project owner (risk acceptance); implementation/test owner TBD.
- **Impact:** High — a private download link cannot revoke an installed client.
- **Likelihood:** Unrated — no runtime/sample/provider/device evidence exists to support a probability score.
- **Prevention:** Require approved authorization for protected operations; define device/user revocation.
- **Detection / test:** TC-023, TC-024: test outside-group access and revocation when an access model exists.
- **Safe fallback:** Deny future protected service access; explain that a local APK copy cannot be recalled.
- **Residual risk:** Open; Offline/local-only features may remain copyable; backend and offline decisions are open.

### R18 — APK inspection reveals code, endpoints, or configuration

- **Owner:** Project owner (risk acceptance); implementation/test owner TBD.
- **Impact:** High — secrets or sensitive logic in a client can be extracted.
- **Likelihood:** Unrated — no runtime/sample/provider/device evidence exists to support a probability score.
- **Prevention:** Put no secrets in APK; minimize client-sensitive data; treat obfuscation only as defense-in-depth.
- **Detection / test:** TC-026: inspect built artifact and scan manifests/source after implementation is authorized.
- **Safe fallback:** Rotate any exposed secret and move protected operations server-side only with approval.
- **Residual risk:** Open; Client code cannot be made impossible to inspect or reverse-engineer.

### R19 — Stolen/shared login or enrolled device gives guest access

- **Owner:** Project owner (risk acceptance); implementation/test owner TBD.
- **Impact:** High — unauthorized access can persist until detected/revoked.
- **Likelihood:** Unrated — no runtime/sample/provider/device evidence exists to support a probability score.
- **Prevention:** Choose strong authentication, device/session controls, and a revocation path.
- **Detection / test:** TC-023, TC-024: test identity boundaries and revocation delay after Q-40/Q-44/Q-71.
- **Safe fallback:** Revoke session/device and require reauthorization for protected requests.
- **Residual risk:** Open; Credential sharing and detection limits remain until a threat model is approved.

### R20 — Authorization outage fails open or blocks legitimate users

- **Owner:** Project owner (risk acceptance); implementation/test owner TBD.
- **Impact:** High — fail-open exposes protected data; fail-closed may block users.
- **Likelihood:** Unrated — no runtime/sample/provider/device evidence exists to support a probability score.
- **Prevention:** Define protected operations, service boundaries, and fail-closed behavior.
- **Detection / test:** TC-023, TC-024, TC-029: test denied, expired, revoked, and unavailable states.
- **Safe fallback:** Fail closed for protected data; show clear unavailable/reauthorization status.
- **Residual risk:** Open; Legitimate access can be unavailable during outages; local-only fallback is undecided.

### R21 — Auth, OCR, or slow providers make the app unresponsive

- **Owner:** Project owner (risk acceptance); implementation/test owner TBD.
- **Impact:** Moderate — delays or resource use can make normal use impractical.
- **Likelihood:** Unrated — no runtime/sample/provider/device evidence exists to support a probability score.
- **Prevention:** Bound waits/retries, avoid blocking launch, measure realistic lists on selected devices.
- **Detection / test:** TC-031, TC-036, TC-037: slow-service and device checks; numeric target Q-75 remains open.
- **Safe fallback:** Provide progress/cancel and a clear source status; do not wait indefinitely.
- **Residual risk:** Open; Low-end device behavior and acceptable latency are unknown.

### R22 — Wrong OCR/match/shipping status leads to a mistaken purchase decision

- **Owner:** Project owner (risk acceptance); implementation/test owner TBD.
- **Impact:** High — user may choose the wrong edition or expect delivery/access that fails.
- **Likelihood:** Unrated — no runtime/sample/provider/device evidence exists to support a probability score.
- **Prevention:** Show confidence/differences and evidence; keep uncertain statuses explicit; no auto-purchase.
- **Detection / test:** TC-008–TC-020: verify confidence, edition, Saudi shipping and ebook-state distinctions.
- **Safe fallback:** Present uncertainty and source/check time; do not purchase or install without user action.
- **Residual risk:** Open; Users can overlook warnings; no purchase or checkout validation is in scope.

### R23 — Signing key loss or compromise blocks or corrupts future updates

- **Owner:** Project owner (risk acceptance); implementation/test owner TBD.
- **Impact:** High — trusted update continuity can be lost or malicious releases published.
- **Likelihood:** Unrated — no runtime/sample/provider/device evidence exists to support a probability score.
- **Prevention:** Define secure generation, access, backup, recovery, and release authorization before build.
- **Detection / test:** TC-026, TC-032, TC-033: artifact/signature verification and custody review; Q-52/Q-54.
- **Safe fallback:** Reject untrusted packages; retain installed version and follow owner-approved recovery.
- **Residual risk:** Open; A lost/compromised key may be unrecoverable under Android's signer rules.

### R24 — Obfuscation creates false confidence while secrets remain extractable

- **Owner:** Project owner (risk acceptance); implementation/test owner TBD.
- **Impact:** High — weak client controls can expose credentials or protected data.
- **Likelihood:** Unrated — no runtime/sample/provider/device evidence exists to support a probability score.
- **Prevention:** Use a realistic threat model; never rely on obfuscation as the security boundary.
- **Detection / test:** TC-026, TC-035: artifact inspection and requirement/scope review.
- **Safe fallback:** Remove exposed secrets, rotate credentials, and stop the affected access path.
- **Residual risk:** Open; Authorized users can inspect client behavior; complete code secrecy is impossible.

### R25 — Install-only activation is mistaken for ongoing revocation

- **Owner:** Project owner (risk acceptance); implementation/test owner TBD.
- **Impact:** High — removed users or forwarded APKs could continue protected access.
- **Likelihood:** Unrated — no runtime/sample/provider/device evidence exists to support a probability score.
- **Prevention:** Require owner-approved revalidation for protected backend requests; document session lifetime.
- **Detection / test:** TC-023, TC-024, TC-029: test protected requests after revocation/offline.
- **Safe fallback:** Deny the next protected request when authorization cannot be verified.
- **Residual risk:** Open; Offline grace/cache can create a revocation delay; exact limit is open.

### R26 — Cached authorization decisions delay revocation

- **Owner:** Project owner (risk acceptance); implementation/test owner TBD.
- **Impact:** High — a revoked user could stay authorized beyond the intended window.
- **Likelihood:** Unrated — no runtime/sample/provider/device evidence exists to support a probability score.
- **Prevention:** Distinguish reusable session tokens from cached permission decisions; validate protected requests server-side.
- **Detection / test:** TC-024: measure actual revocation delay under active and cached sessions.
- **Safe fallback:** Stop protected access when the approved lease expires; show reauthorization status.
- **Residual risk:** Open; Maximum delay cannot be set until backend/session architecture is selected.

### R27 — Slow/failed providers or caching display stale data as current

- **Owner:** Project owner (risk acceptance); implementation/test owner TBD.
- **Impact:** High — stale stock/shipping can mislead a user.
- **Likelihood:** Unrated — no runtime/sample/provider/device evidence exists to support a probability score.
- **Prevention:** Use bounded per-provider concurrency, progressive status, and freshness labels.
- **Detection / test:** TC-015, TC-016, TC-022: inject failures and stale timestamps; verify labels.
- **Safe fallback:** Mark failed or stale data; never label stale shipping/stock confirmed.
- **Residual risk:** Open; A listing can change after its check; freshness interval remains open.

### R28 — On-device OCR is too slow or resource-intensive

- **Owner:** Project owner (risk acceptance); implementation/test owner TBD.
- **Impact:** Moderate — CPU, memory, or battery use may make scans fail.
- **Likelihood:** Unrated — no runtime/sample/provider/device evidence exists to support a probability score.
- **Prevention:** Benchmark representative lists/devices before selecting OCR architecture.
- **Detection / test:** TC-001, TC-002, TC-031, TC-036: measure on approved devices after samples exist.
- **Safe fallback:** Allow cancellation/manual correction; avoid blocking the interface.
- **Residual risk:** Open; Device variation and target resource limits are not known.

### R29 — Free backend pauses, cold-starts, or hits quotas

- **Owner:** Project owner (risk acceptance); implementation/test owner TBD.
- **Impact:** Moderate — protected search/update may become unavailable.
- **Likelihood:** Unrated — no runtime/sample/provider/device evidence exists to support a probability score.
- **Prevention:** Verify current no-card terms and quotas; test pauses/outages before selecting a host.
- **Detection / test:** TC-029–TC-031: simulate unavailable service and quota response; host remains unselected.
- **Safe fallback:** Show unavailable; wait for reset or an approved plan change; never charge automatically.
- **Residual risk:** Open; A $0 tier may not meet performance/availability needs.

### R30 — Provider search requires paid API or prohibited method

- **Owner:** Project owner (risk acceptance); implementation/test owner TBD.
- **Impact:** High — cost or terms violation can block or invalidate the source.
- **Likelihood:** Unrated — no runtime/sample/provider/device evidence exists to support a probability score.
- **Prevention:** Verify official permitted access, eligibility, fees, and data rights per source.
- **Detection / test:** TC-013, TC-014, TC-021, TC-030: verify method, configuration and no-card cost before enabling.
- **Safe fallback:** Mark source unsupported; do not scrape or silently substitute a paid service.
- **Residual risk:** Open; Provider coverage may be limited or unavailable at $0.

### R31 — Overage/auto-upgrade creates an unexpected bill

- **Owner:** Project owner (risk acceptance); implementation/test owner TBD.
- **Impact:** High — direct conflict with the confirmed $0/no-card rule.
- **Likelihood:** Unrated — no runtime/sample/provider/device evidence exists to support a probability score.
- **Prevention:** Use no-card $0 tiers, disable automatic paid upgrades/overages, and set hard limits where available.
- **Detection / test:** TC-030: inspect candidate billing configuration and simulate quota exhaustion without enabling billing.
- **Safe fallback:** Stop/degrade at the free limit; require explicit approval before any paid change.
- **Residual risk:** Open; Provider billing controls can change or be misconfigured; service choices remain open.

### R32 — Free-tier limits or terms change after approval

- **Owner:** Project owner (risk acceptance); implementation/test owner TBD.
- **Impact:** Moderate — a source may become unavailable or costly.
- **Likelihood:** Unrated — no runtime/sample/provider/device evidence exists to support a probability score.
- **Prevention:** Record official source/date and recheck before build approval and release.
- **Detection / test:** TC-022, TC-030: confirm versioned terms/quota evidence and service status at review time.
- **Safe fallback:** Pause/omit the source and update the plan; do not silently migrate to paid tier.
- **Residual risk:** Open; Terms can change between reviews; recheck cadence is not defined.

### R33 — Weak authentication is chosen only to stay free

- **Owner:** Project owner (risk acceptance); implementation/test owner TBD.
- **Impact:** High — cost saving could expose a small private group.
- **Likelihood:** Unrated — no runtime/sample/provider/device evidence exists to support a probability score.
- **Prevention:** Treat security as higher priority than convenience; validate supported no-cost auth before selection.
- **Detection / test:** TC-023, TC-026, TC-030: review auth boundary and secret/cost handling; no backend selected.
- **Safe fallback:** Stop build and present the cost/security tradeoff if no safe $0 option is verified.
- **Residual risk:** Open; A secure $0 auth path may not exist for the chosen constraints.

### R34 — Free book search is implemented through prohibited/brittle scraping

- **Owner:** Project owner (risk acceptance); implementation/test owner TBD.
- **Impact:** High — terms, access, or reliability may be violated.
- **Likelihood:** Unrated — no runtime/sample/provider/device evidence exists to support a probability score.
- **Prevention:** Use only verified permitted source methods; a public website is not proof of API permission.
- **Detection / test:** TC-013, TC-014, TC-021: verify the provider method and ensure unsupported endpoints are inactive.
- **Safe fallback:** Disable/omit the source; state which sources were actually checked.
- **Residual risk:** Open; A permitted free search method may not exist for required providers.

### R35 — Nominally free service requires a payment card or billing activation

- **Owner:** Project owner (risk acceptance); implementation/test owner TBD.
- **Impact:** High — conflicts with the owner's confirmed no-card rule and risks charges.
- **Likelihood:** Unrated — no runtime/sample/provider/device evidence exists to support a probability score.
- **Prevention:** Verify current signup/free-tier requirements; do not enter a card or enable billing.
- **Detection / test:** TC-030: review official signup terms and quota behavior before selecting service.
- **Safe fallback:** Exclude the service; return to planning if no eligible option remains.
- **Residual risk:** Open; Card requirements and plans can change; any future choice needs re-verification.

### R36 — Unsupported handwritten, mixed-format, or non-English input is treated as reliably scanned

- **Owner:** Project owner (risk acceptance); implementation/test owner TBD.
- **Impact:** Moderate — unsupported pages may produce misleading entries and wrong searches.
- **Likelihood:** Unrated — no runtime or representative negative-input tests exist.
- **Prevention:** Clearly bound supported input; define unsupported-page handling before implementation and do not imply support for handwriting, mixed formats, or other languages.
- **Detection / test:** TC-003 — submit unsupported formats and verify they are not treated as reliably extracted; exact fallback remains Q-04/Q-06/Q-07/Q-60.
- **Safe fallback:** Stop unreliable extraction and offer only an owner-approved manual/cancel path; do not search guessed fields.
- **Residual risk:** Open; users may submit ambiguous pages and the exact message/manual workflow is not chosen.

### R37 — Launch-only update checks miss inactive users

- **Owner:** Project owner (risk acceptance); implementation/test owner TBD.
- **Impact:** Moderate — an invited user who does not open the app may not see an available update promptly.
- **Likelihood:** Unrated — usage cadence and update interval are not known.
- **Prevention:** Set an explicit update-check cadence and state that an in-app launch check does not notify an inactive user; do not add push/email without approval.
- **Detection / test:** TC-037 — compare checks at the approved cadence and verify unavailable metadata is distinct from no update; cadence remains Q-53.
- **Safe fallback:** Keep the installed version, show its last successful check when available, and offer a user-approved retry on next use.
- **Residual risk:** Open; notification coverage depends on the owner-approved cadence and whether the user opens the app.

## Review gate

Before plan approval, review every High-impact item, assign the execution/test owner, resolve its blocking questions, and record evidence and any explicit residual-risk acceptance. Do not claim that a risk is solved solely because a mitigation is proposed or a test is planned.

The register is planning-only. No application behavior has been implemented or tested.