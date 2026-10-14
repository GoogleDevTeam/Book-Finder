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
