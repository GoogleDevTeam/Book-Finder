# Decision log

**Last updated:** 2026-10-14

Only items in **Confirmed** are settled by the user. **Proposed** items are recommendations, not authorization. **Open** items require a user decision.

## Confirmed

- Target is an Android APK app.
- The image contains a list of multiple books; it is not a cover-photo identification workflow.
- Book search can use ISBN, name/title, or both.
- Show identification confidence.
- Flag different editions and versions.
- Search physical books and ebooks.
- Saudi Arabia is the required destination/market. If shipping is uncertain, state that it is uncertain.
- Flag ebook regional restrictions or uncertain availability.
- Manage the provider list through GitHub. Amazon.sa and Noon were examples, not confirmed working sources.
- Check for a GitHub-based app update on launch; user approval is required to install the APK.
- Intended usage is about five people, with strong privacy and security.
- The plan is the source of truth. Do not add unapproved scope.

## Proposed, not yet approved

- Process list images on-device by default and discard the source image after extraction.
- Separate OCR confidence from catalog-match confidence.
- Use a server-side component to access private GitHub release/config data; never embed a GitHub token in the APK.
- Allow only invited accounts/devices, with a way to revoke access.
- Cache provider configuration safely and show when data was last checked.
- Check for a new app version on launch only when the last check is old enough, and keep the check non-blocking.

## Open

- Supported image types: printed, handwritten, screen capture, or a combination.
- Languages expected in lists and desired app languages, including Arabic/English support.
- Whether “number of books” means total entries, quantity per title, or both.
- Initial providers to support and the permitted search/API method for each. Example providers are not yet integrations.
- Whether “Saudi-only” means Saudi-based sellers only, or any seller that can deliver to Saudi Arabia; the confirmed requirement is that Saudi delivery must not be falsely asserted.
- What evidence is sufficient to mark delivery or ebook access as confirmed.
- How to present an uncertain shipping result and what user action should follow.
- How users choose between editions, translations, and formats.
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
- Whether integrity/attestation controls are required, considering their privacy, compatibility, cost, and bypass limitations.


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
