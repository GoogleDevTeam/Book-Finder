# Decision log

**Last updated:** 2026-10-15

Only items in **Confirmed** are settled by the user. **Proposed** items are recommendations, not authorization. **Open** items require a user decision.

## Confirmed

- Target is an Android APK app.
- The image contains a list of multiple books; it is not a cover-photo identification workflow.
- For v1, the owner selected printed lists and typed/on-screen lists; each page must use one format. Handwritten lists and mixed-format pages were not selected.
- English was selected as the list-content language. Arabic, mixed Arabic/English, and other-language recognition were not selected.
- Book search can use ISBN, name/title, or both. For a scanned list entry, require a valid, readable ISBN—provided or corrected by the user—before searching that entry.
- Show text-reading/OCR confidence separately from book-match confidence.
- Flag different editions and versions. When multiple editions match, preselect the closest and flag differences.
- The minimum useful outcome is likely book matches with edition differences. The owner chose an explicit per-search “Also search ebooks” checkbox; when unchecked, ebook providers are not queried. Ebook search remains in v1, but this UI choice does not approve any provider or use of Amazon-derived content.
- Search physical books; offer ebook search only when the user opts in for that search. Only configured, owner-approved, permitted providers may be queried.
- Saudi Arabia is the required destination/market. If shipping is uncertain, state that it is uncertain.
- Flag ebook regional restrictions or uncertain availability.
- Manage the provider list through GitHub. Amazon.sa and Noon were examples, not confirmed working sources.
- Check for a GitHub-based app update on launch; user approval is required to install the APK.
- Intended usage is about five people, with strong privacy and security. They share one group book list. The owner explicitly chose to leave the group relationship/category unspecified for v1. Authentication, invitations, membership/permissions, user attribution, and history separation remain separate open decisions.
- The main use context is library/school book lists. V1 scans lists and finds book matches/sourcing options; tracking an existing collection’s holdings, locations, or loans is out of scope. Requested-copy quantity remains open under Q-08.
- The plan is the source of truth. Do not add unapproved scope.

## Proposed, not yet approved

- Process list images on-device by default and discard the source image after extraction.
- Use a server-side component to access private GitHub release/config data; never embed a GitHub token in the APK.
- Allow only invited accounts/devices, with a way to revoke access.
- Cache provider configuration safely and show when data was last checked.
- Check for a new app version on launch only when the last check is old enough, and keep the check non-blocking.
- Evaluate a native Android client plus one modular authenticated API, on-device OCR, and separate metadata/offer provider adapters; see ARCHITECTURE_PROPOSAL.md. This is a planning recommendation only: no stack, host, provider, auth, persistence, or data flow is selected.

## Open

- The owner prefers Amazon.sa as the only intended source for now, as recorded in [issue #10](https://github.com/GoogleDevTeam/Book-Finder/issues/10). This is not provider approval. The permitted mobile-app use path, private GitHub APK compatibility, and Saudi ebook coverage remain unresolved.


- The app interface language(s), including whether it should support Arabic alongside English. List content is currently English-only by selection.
- Whether to track list-entry count, copy quantity per title/edition, or both. The owner is not sure yet. Edition/version identifies the book and is separate from copies.
- Initial providers to support and the permitted search/API method for each. Example providers are not yet integrations.
- Whether “Saudi-only” means Saudi-based sellers only, or any seller that can deliver to Saudi Arabia; the confirmed requirement is that Saudi delivery must not be falsely asserted.
- Q-32: A seller-/offer-specific destination confirmation is the conservative recommendation. Amazon SearchItems exposes delivery-program flags but maps related DeliveryInfo fields as unavailable; its offer price may use a default in-marketplace address. These signals do not prove delivery to a specific Saudi destination. The exact evidence and freshness threshold remain owner decisions.
- How to present an uncertain shipping result and what user action should follow.
- What “closest” means for edition/translation ranking and what to do if no candidate is suitable; preselection of the closest with a visible difference flag is confirmed. Physical-book versus ebook preference is requested, but its exact effect on ranking/filtering is not defined.
- Whether images, recognized entries, history, or favorites are saved; where and for how long.
- How the five people are invited/authenticated and how access is revoked.
- How the first APK is distributed privately; in-app update flow applies after installation.
- Whether a small backend is acceptable for private GitHub access and update delivery.
- Android version/device support, update-check frequency, and whether update notifications beyond app launches are needed.
- Whether search details may be sent directly to external providers or must be brokered through a server.
- Numeric success targets for extraction and matching, after sample lists are available.

## Owner-confirmed priority — 2026-10-14

Security, safety, and performance are the top priorities. Only people the owner explicitly allows should receive protected access to the app. The owner also wants strong resistance to reverse-engineering and exposure of the app’s internals.

## Technical boundary to plan around

A private APK can be distributed and access-controlled, but an APK that reaches a user cannot be guaranteed impossible to copy, inspect, or decompile. Obfuscation/tamper resistance can increase effort; they cannot guarantee secrecy. Practical protections include keeping secrets and protected operations out of the client, requiring authorization for protected services, limiting data in the package, signing releases, and being able to revoke users/devices. These are design recommendations, not selected architecture.

## Security, safety, and performance decisions still open

- Authentication/invitation method, per-user/device limits, device revocation, and whether any features work offline.
- Threat scenarios and acceptable residual risk; what specifically must remain confidential (credentials, source code, provider configuration, search data, or proprietary logic).
- Whether a backend is acceptable and who maintains it; no GitHub/provider secret may be embedded in the APK.
- Performance targets and test devices for startup, OCR, book matching, provider search, and update checks.
- Q-77: Whether device-integrity checks/attestation are required. Google Play Integrity reports UNLICENSED for sideloaded apps; do not gate this private APK on Play entitlement without verifying a compatible distribution path. Privacy, compatibility, cost, and bypass limits remain open.


## Proposed access flow — pending owner decision

- Candidate: invite and authorize each person/device once, then renew access automatically using short-lived sessions; avoid repeated password prompts during normal use.
- Candidate: check authorization as part of protected backend requests, rather than adding a blocking login check on every screen. The backend would mediate private GitHub release/config and any protected provider access; no GitHub/provider secret belongs in the APK.
- Candidate: revoke users/devices server-side so subsequent protected requests are denied. Any offline cache/lease creates a revocation delay and remains undecided.
- Not accepted: a one-time install-only check with no later authorization, because it cannot reliably revoke an installed/forwarded copy.

These are recommendations for discussion, not an approved access model. Sign-in method, device binding, online/offline behavior, session lifetime, and revocation timing remain open.


## Proposed automatic-check flow — not approved

- Authorization and version checks are separate. A version check only reports an available release; it does not prove a user is allowed to use protected features.
- Candidate authorization flow: invite-only sign-in/device enrollment once; securely retained, limited session; authorization checked by a backend when protected search, private provider data, or private update data is requested; denial/revocation stops the next protected request. This avoids asking the user to sign in repeatedly while still allowing access to be revoked.
- Candidate update flow: a non-blocking launch-time check, when due, asks a service for version metadata from private GitHub releases. The service keeps GitHub credentials private. The app then offers a signed APK and leaves final installation approval to Android/the user.
- If authorization or service is unavailable, protected features should not fail open. Offline local-only behavior is a separate open decision.
- No session duration, check interval, login provider, backend, or device-binding method has been chosen.


## Proposed performance optimizations — not approved

- Avoid a separate auth request for every screen: retain a short-lived protected session and attach it to each protected backend request. Server-side authorization still applies; longer caching of authorization decisions delays revocation.
- Make version checks asynchronous/non-blocking and conditional; check only when due and let the server cache GitHub release metadata. Check frequency is open.
- Benchmark on-device OCR before choosing cloud OCR; local OCR avoids image upload latency/privacy exposure but may consume device CPU, memory, and battery.
- Use bounded provider concurrency and timeouts, progressive results, and explicit per-provider status. Do not use stale shipping/availability as if current.
- Define performance targets only after choosing representative Android devices, list sizes, and network conditions.

These are candidates only. No latency target, cache duration, OCR engine, session lifetime, or provider concurrency limit is approved yet.


## Owner-confirmed cost preference — 2026-10-14

The owner wants everything to be free. Safe planning default: target $0 for the owner and users; no paid services, subscriptions, automatic overages, or upgrades without explicit approval. If a free quota is exceeded, the service should fail/degrade safely rather than charge.

**Superseded for payment-card eligibility by the confirmed no-card rule below.** A card-gated free tier is not acceptable (Q-79). The separate question of whether cold starts, pauses, or manual republishing are acceptable remains open, given performance is also a top priority. Hosting candidates and dated evidence are in COST_AND_HOSTING.md; none is selected.


## Confirmed cost constraint — no payment card

The owner confirmed that a service must not require a payment card to access its free tier. A card-gated “free” service is not acceptable. Do not enter a payment method or enable billing to satisfy a service requirement. The no-card rule applies to hosting, auth/database, OCR, provider data/search, storage, email, builds, updates, and other app services.


## Confirmed workflow requirement — project activity log

The owner asked for project-related work and decisions to be recorded on GitHub. A concise project log will capture every app-related requirement, answer, decision, proposal, open question, risk, plan change, implementation/test/release action, and outcome. It is not a verbatim transcript and must never contain secrets or credentials. Before app changes, the plan is updated first; after work, the commit and verification are logged.


## Owner-confirmed private-fork setting — 2026-10-15

The owner chose to leave private forking unchanged for now. The private repository remains under the personal GitHub account GoogleDevTeam with allow_forking=true. The connected GitHub API rejected disabling the setting (HTTP 422; the endpoint only accepts organization-owned private repositories); a UI-only control was not verified. No repository transfer or access change is approved. This setting is not a confidentiality guarantee; authorized collaborators may still clone the code.
