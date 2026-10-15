# Book-Finder planning audit

**Audit date:** 2026-10-14  
**Status:** Documentation and feasibility review only; no implementation approved.  
**Scope:** Current main-branch planning documents, GitHub activity, and selected official provider/platform documentation.

## Executive finding

The project is still planning-only. The repository README says it intentionally contains planning documents; the current repository has no application code, no GitHub issues, and no pull requests. Therefore no runtime bug hunt, build, or automated test could be run. This review identifies documented planning defects and feasibility risks; it cannot prove the eventual app will work or that every future defect has been found.

The highest-risk problem is not a missing feature: the project's decision backlog, implementation gate, requirements, and risk inventories do not line up. The written gate can be interpreted differently depending on which document is followed. This needs reconciliation before plan approval.

## Findings

### Blockers to approving a complete plan

1. **The open-question list is incomplete relative to the discovery catalog.** DISCOVERY_CATALOG.md contains questions Q-01 through Q-87: 86 are marked Open and Q-79 is Confirmed. It has 32 explicit P0/Open questions; a later instruction reclassifies Q-45, Q-47, Q-54, Q-55, and Q-58 as plan blockers, making 37 effective P0 blockers. OPEN_QUESTIONS.md contains only 17 numbered prompts, with 7 under Priority 0, and does not map them to the catalog IDs. CHANGE_CONTROL.md tells the owner to resolve every P0 question in OPEN_QUESTIONS.md, while the catalog separately requires all P0 questions. Reconcile these documents and identify the single authoritative backlog before using the approval gate.

2. **The required requirement IDs do not exist.** CHANGE_CONTROL.md requires every app change to cite an approved requirement ID. APP_PLAN.md has scope bullets and acceptance criteria but no stable requirement IDs or mapping from each requirement to a test. Add IDs and a traceability table before implementation; otherwise the rule is not executable.

3. **There are two overlapping risk inventories.** DISCOVERY_CATALOG.md uses R-01 to R-26; RISK_REGISTER.md uses R1 to R35. They overlap on OCR, matching, providers, delivery, security, updates, performance, and costs, but there is no shared ID map or declared canonical list. RISK_REGISTER.md says likelihood/severity are unscored; detection evidence and residual-risk acceptance are not consistently recorded for each item. Consolidate or map the entries and add owner, severity/likelihood rationale, prevention, detection/test, safe fallback, and residual risk for every material risk.

4. **The cost decision log contradicts itself.** DECISIONS.md retains an earlier statement that whether a card-gated free tier is acceptable is open, followed later by a confirmed no-payment-card rule. The later owner-confirmed no-card rule should be the only active status; remove or clearly supersede the stale wording.

5. **The test plan is a requirement, not yet an executable test plan.** CHANGE_CONTROL.md requires testing OCR/corrections, matching, Saudi shipping and ebook uncertainty, privacy/access, outages, and signed APK updates. The risk documents name scenarios, but there is no case-by-case matrix with inputs, expected result, pass/fail threshold, test owner, device, or evidence. Representative lists, target devices, and quantitative accuracy/performance targets are also absent.

### Provider and platform feasibility checked on 2026-10-14

- **Amazon.sa:** Amazon's Saudi Product Advertising API page says PA-API was to be deprecated on May 15, 2026 and directs users to Creators API. Amazon's current Creators API documentation lists Saudi Arabia as a marketplace, but requires enrollment in Amazon Associates for the target marketplace and at least 10 qualifying sales in the previous 30 days, followed by API registration and credentials. The owner has not confirmed eligibility. Treat Amazon as unverified and potentially unavailable for this private app until eligibility, permitted use, required disclosures, and actual Saudi book coverage are proven. Sources: [Saudi PA-API notice](https://webservices.amazon.com/paapi5/documentation/locale-reference/saudi-arabia.html), [Creators API prerequisites](https://affiliate-program.amazon.com/creatorsapi/docs/en-us/introduction), [Saudi locale](https://affiliate-program.amazon.com/creatorsapi/docs/en-us/locale-reference/saudi-arabia).
- **Noon:** The official material located describes seller/partner catalog search and APIs for seller catalog, orders, and fulfillment. It does not establish a permitted consumer product-search API for this app. Do not count Noon as an integration until the exact public/partner access path, eligibility, terms, and book/offer coverage are confirmed. Source: [Noon Partner Catalog help](https://helpcenter.noon.partners/en/category/product-listing/how-to-search-for-items-in-the-partner-catalog).
- **Metadata is not availability:** Google Books documents public volume search and says public-data requests need an API key; this can help identify metadata but does not verify Saudi shipping, local stock, or ebook purchase entitlement. Open Library describes its service as human-centered book lookup, asks regular clients to identify themselves, limits default requests to 1/second (3/second when identified), and says it is not a backend for high-traffic commercial services. Treat either as a candidate metadata source only, subject to coverage, terms, privacy, quota, and reliability checks. Sources: [Google Books API](https://developers.google.com/books/docs/v1/using), [Open Library API guidance](https://openlibrary.org/developers/api).
- **Direct APK installation and updates:** Android's publishing guidance says users must enable installation from the specific unknown-app source on Android 8+; Android updates must preserve the app identity and signing certificate, and the user may need to approve installation. The plan recognizes user approval but does not yet define this permission/setup flow, signing-key custody/backup/recovery, update rollback/revocation, or support steps. Sources: [Android publishing](https://developer.android.com/studio/publish/index), [Android app signing](https://developer.android.com/studio/publish/app-signing).
- **Hosting and $0/no-card:** Current Cloudflare docs list Workers Free at 100,000 requests/day and 10 ms CPU per invocation; D1 Free lists 5 million rows read/day, 100,000 rows written/day, and 5 GB total storage. These quotas do not prove account activation needs no payment card or that the complete auth/update/provider path fits free limits. Replit's current publishing docs confirm one Starter published app that expires after 30 days but can be republished; the cost table's separate $2/month Autoscale base figure was not confirmed by the page reviewed and should be rechecked before relying on it. No host is selected. Sources: [Workers pricing](https://developers.cloudflare.com/workers/platform/pricing/), [D1 pricing](https://developers.cloudflare.com/d1/platform/pricing/), [Replit publishing costs](https://docs.replit.com/billing/deployment-pricing).

### Additional high-impact unanswered decisions

- Which list formats and languages are in scope; Arabic/English and right-to-left behavior; whether quantity is distinct from entry count. The two questions asked in this conversation remain unanswered.
- Whether each person's lists are private or shared; whether photos, extracted text, titles/ISBNs, account identifiers, history, or diagnostics leave the device or are retained; how deletion works. Book titles and ISBNs can themselves reveal sensitive interests, so the activity log must not store actual book data, user identities, or credentials.
- Invitation/authentication, device limits, revocation, offline behavior, backend acceptability/owner, and the actual first-APK distribution channel.
- Provider-specific permissions, data-use terms, Saudi delivery evidence (including address/postcode dependency), ebook entitlement semantics, price/stock freshness, and handling when a provider cannot be checked.
- Minimum Android/device/accessibility requirements, Arabic UI, camera/photo permissions, timeouts, cancellation, concurrency, latency targets, and an operational incident/recovery owner.
- The project correctly states that an APK cannot be made impossible to copy or reverse-engineer. The threat model and acceptable residual risk still need owner decisions; do not promise perfect secrecy.

## Recommended order of work (planning only)

1. Make OPEN_QUESTIONS.md and DISCOVERY_CATALOG.md one consistent, ID-mapped decision backlog; mark owner decisions separately from research tasks that can be completed from official sources.
2. Add stable requirement IDs to APP_PLAN.md and map each confirmed requirement to observable acceptance criteria and tests.
3. Reconcile the risk lists and stale cost wording; assign risk ratings only with a stated rationale and record test evidence/residual risk.
4. Prove provider and hosting feasibility against the $0/no-card rule. Select no retailer until its permitted API, account eligibility, terms, and Saudi status evidence are clear; omit a source rather than scrape or silently add a paid service.
5. Define threat/data-flow, invitation/revocation, initial install, signing-key recovery, and update flows before implementation.
6. Produce the concrete test matrix and UX state spec, including loading, empty, uncertain, offline, provider-failed, access-denied, and update-failed states; then review and explicitly approve the complete plan before any code starts.

These are recommendations, not scope approval. No app features or implementation were added during this audit.

## Audit limitations and logging rule

This was a read-only document and official-source review. No app code exists in this repository to build, test, scan, or inspect as an APK. Provider eligibility was researched from public documentation but not tested with an account, credentials, live searches, or sample books. All external pricing, terms, and APIs must be rechecked before selection and release.

Keep PROJECT_LOG.md as a concise project audit trail: record decisions, proposals, open items, actions, evidence, and commits. Do not record verbatim chat, book titles/ISBNs, personal data, tokens, keys, or credentials. This audit report is the detailed findings record.


## Follow-up: private fork setting (2026-10-15)

The connected GitHub account reports this repository as private, owned by the personal user account GoogleDevTeam (owner type User), with admin permission and allow_forking=true. After the owner selected disabling forks, a PATCH setting allow_forking=false returned HTTP 422: “Allow forks setting can only be changed on org-owned private repositories.” A subsequent GET still reports allow_forking=true. No repository setting, visibility, collaborator, or ownership change occurred. The owner then chose to leave the setting unchanged for now.

GitHub’s general [Forks reference](https://docs.github.com/en/pull-requests/reference/forks) says admins of private repositories can disallow forks; the [repository forking-policy guide](https://docs.github.com/en/repositories/managing-your-repositorys-settings-and-features/managing-repository-settings/managing-the-forking-policy-for-your-repository) describes a control for organization-owned private repositories. The available API result matches the organization-only scope for this user-owned repo. A UI-only control was not verified. Do not claim this repo can disable forks through the available API, and do not transfer it to an organization without separate approval. Private status is not a confidentiality guarantee: authorized collaborators can still clone locally.

Fork-policy entries are DISCOVERY_CATALOG.md Q-57 and R-23, and RISK_REGISTER.md R14. DISCOVERY_CATALOG.md R-14 is a separate provider-configuration risk and is not the fork-policy item.


## Research follow-up: backend and provider feasibility (2026-10-15)

Official public-document checks narrowed, but did not close, the research-first questions. Cloudflare Workers Free/D1 has documented request/database quotas and fail-on-limit behavior; Cloudflare Access advertises a free tier for fewer than 50 users and email one-time PIN login, but native Android session handling and app-specific revocation remain unverified. Supabase Free can pause after seven days of low activity. Firebase documents most Spark features as usable without payment information, a 3,000-DAU limit for most Auth providers, and no-cost App Distribution with 500 testers per project/200 per group. None of these facts proves a complete invite-only, revocable, private-release backend can be activated and operated on this owner's actual account at $0/no card. No backend or authentication option is selected. See COST_AND_HOSTING.md for source links and caveats.

Amazon's Creators API documents a Saudi marketplace and product search, but access requires Associates enrollment in the target marketplace, 10 qualifying sales in the prior 30 days, API registration, and credentials. Eligibility and use-policy fit for this private app were not tested. Noon documents partner/seller/integrator APIs and seller-authorized OAuth; the reviewed official documentation does not establish public consumer product search. No scraping or provider integration is approved. Q-83 remains partial.

For Q-85, Firebase App Distribution is advertised as no-cost with official tester limits, while GitHub Actions private-repository quotas depend on the account plan and overage is billable. Email delivery method, storage, monitoring, build requirements, exact quotas, and no-card activation remain undecided/unverified.

This was documentation research only: no account was created, no credentials or payment method were entered, no app code/tests/deployment occurred, and no provider was selected. The repo remains planning-only.


## Research follow-up: private release and APK integrity (2026-10-15)

GitHub's private release-listing API requires repository Contents read permission; GitHub App installation tokens can be limited to granted repositories/permissions and expire after one hour. This is a server-side candidate, not permission to embed a token in the Android client. Android requires signed APKs; update identity/signing must remain consistent, loss of the app signing key prevents updating installed copies, Android 8/API 26+ requires per-source install permission, and update version codes must not be lower than installed versions. Removing a release cannot uninstall existing APKs, and an offline device cannot receive revocation. A correction needs a newly signed higher-version release plus compatible data migration; protected online operations need server-side revocation checks. None of the key-custody, rollback, private-distribution, or offline policies is decided. Detailed sources and limitations are recorded in COST_AND_HOSTING.md.

Firebase App Distribution is a no-cost tester-distribution candidate within its published caps (500 testers/project, 200/group). GitHub Actions private-repository build quotas depend on the account plan; overage is billable. The account plan, no-card setup, required email/storage/monitoring services, and build capacity remain unverified. No tools were installed, app code/tests were not run, and no service was configured.


## Research follow-up: book metadata and Android developer verification (2026-10-15)

Official Google Books API documentation supports ISBN queries and reports saleability for the Google eBookstore by country. That can be evaluated as a metadata/eBook-store candidate, but it is not physical inventory or a guarantee of an individual user's purchase, DRM, or device access. Public-data requests use an API key; the key is not user authentication and requires app/API restriction and quota controls. The Google Books API terms page states that submitted content may be made publicly available through Google services and that users may not be charged for the application without separate written permission. The terms page reports last modified 2012-04-25, so re-check it before any use. No data flow or notice has been approved.

Open Library asks developers to register their use case, recommends identified requests, sets a default rate of one request per second (three per second with User-Agent and email), and says its APIs are not intended as a third-party data backend or high-traffic commercial infrastructure. Its fit for this private app is not confirmed. Amazon.sa remains gated on target-marketplace Associates eligibility and 10 qualifying sales in the preceding 30 days; Noon partner docs do not establish public consumer search. Q-23, Q-25, and Q-37 are now partially researched, not closed; no provider is selected.

Android's official developer-verification guide says the initial protections started 2026-09-30 in Brazil, Indonesia, Singapore, and Thailand, with global expansion for certified devices in 2027. Saudi Arabia is not listed in the initial rollout. Apps distributed only outside Google Play are directed to Android Developer Console for identity verification and package registration; the guide documents a no-fee student/hobbyist account path for eligible users. Owner eligibility, exact registration requirements, timing for the intended Saudi distribution, and any account costs remain unverified. Update Q-50/Q-52 and release test TC-032 accordingly; do not treat this research as a provider/distribution decision.

No account, API key, payment method, signing key, app code, deployment, or runtime test was created or used. This is a public-document planning update; implementation remains blocked by the complete-plan approval gate.

## Research follow-up: Saudi delivery evidence, Play Integrity, and main-branch controls (2026-10-15)

Official Amazon Creators API documentation confirms that SearchItems can filter delivery programs such as AmazonGlobal, FreeShipping, and Prime, but the documented response mapping marks its DeliveryInfo fields as not available. OffersV2 states its price may use a default in-marketplace shipping address and may differ for a specific customer. These are not destination-specific delivery confirmations. Recommend requiring current seller-/offer-specific evidence for the relevant Saudi destination; otherwise keep the status uncertain/unchecked. Checkout is not an acceptable proof step for this app unless separately approved, and the owner still needs to choose address granularity and freshness (Q-32/Q-34/Q-36).

Official Play Integrity docs say the app licensing verdict is UNLICENSED for a sideloaded app and app recognition is tied to Google Play-distributed app/certificate versions. Do not use Google Play entitlement as an access gate for the planned private APK. Optional device-integrity signals remain an owner choice and must be validated against the actual distribution channel, device compatibility, privacy, cost, and bypass limits (Q-77).

Repository read-only state: only main exists, it is unprotected, and only GoogleDevTeam currently has push/admin access. The latest direct-main GitHub API commit is unsigned; GitHub required-signature rules reject unsigned commits. No repository settings were changed. Keep owner-only write access, no-force fast-forward updates, latest-head checks, and pre-commit validation; revisit signing or additional branch rules only if a compatible workflow is available and the owner approves.

This is public-document and repository-metadata research only. No provider, integrity mechanism, GitHub setting, account, key, app behavior, or data flow was selected or changed. No app code or runtime tests exist. Q-32/Q-77 are Partial; Q-03 is Confirmed and Q-04 is the next sequential owner question.
