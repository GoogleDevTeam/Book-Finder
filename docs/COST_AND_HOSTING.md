# Cost and hosting evaluation

**Checked:** 2026-10-14  
**Status:** Research snapshot; candidates only, no host selected.  
**Requirement:** The owner wants everything to be free. The working requirement is $0 recurring cost for the owner and users, no payment card/billing instrument required, and no paid overages, subscriptions, or automatic upgrades unless the owner explicitly changes the plan.

Free plans and quotas can change. Recheck official terms before choosing a service and before release. If a free limit is reached, the app should degrade or stop the affected function with a clear message; it must not silently incur a charge.

## Candidate comparison

| Candidate | Current no-cost information | Fit and risks to verify |
|---|---|---|
| Replit deployment | Replit’s current docs say Starter includes one free published app that expires after 30 days but can be republished. Autoscale pricing currently lists a $2/month base fee plus usage, with plan credits applied to publishing costs. | Useful for building and prototyping, but not a dependable permanent $0 production backend without repeated republishing or a paid plan. Confirm current account-specific terms before use. [Source](https://docs.replit.com/billing/deployment-pricing) |
| Cloudflare Workers Free + D1 | Current Workers Free limits include 100,000 requests/day and 10 ms CPU time per invocation. D1 Free includes 5 million rows read/day, 100,000 rows written/day, 5 GB total account storage, and a 500 MB per-database limit. Free-limit errors stop operations rather than automatically moving to a paid tier. | First candidate to test for a small request-based API; the quotas appear ample for a five-person app, but real usage must be measured. It still needs a secure invite/auth design, protected GitHub access, and tests under its CPU/subrequest limits. [Workers pricing](https://developers.cloudflare.com/workers/platform/pricing/) · [Workers limits](https://developers.cloudflare.com/workers/platform/limits/) · [D1 pricing](https://developers.cloudflare.com/d1/platform/pricing/) · [D1 limits](https://developers.cloudflare.com/d1/platform/limits/) |
| Supabase Free | Current page lists $0, 50,000 monthly active users, 500 MB database, 5 GB egress, and 1 GB file storage; free projects pause after one week of inactivity. | Auth/database features may reduce custom work, but inactivity pauses conflict with always-available performance. Verify whether every needed server-side function, email/auth method, and private GitHub access fits the free tier. [Source](https://supabase.com/pricing) |
| GitHub Free | GitHub Free for personal accounts includes unlimited private repositories with a limited feature set and a monthly Actions-minute quota. | Suitable for the private plan/source repository within current limits, but GitHub is not the runtime backend. Private repo/release access still needs secure server-side handling in the app architecture. [Source](https://docs.github.com/en/get-started/learning-about-github/githubs-plans) |

## Provisional recommendation for planning

Evaluate Cloudflare Workers Free + D1 first as a possible $0 backend/API, and compare it with Supabase Free if built-in authentication is more valuable than always-on behavior. These are not selected technologies. Replit remains suitable for creating the app, but current Starter publishing terms do not establish a permanent, always-on $0 production server.

No recommendation is final until a proof-of-feasibility answers: can invited-user authentication be done safely for $0; can private GitHub release metadata be accessed without placing a token in the APK; can the service fail closed without exceeding limits; and can provider searches be performed through permitted no-cost methods?

## Costs that must be checked

- Backend/API hosting, database, authentication, email or invitation delivery, and storage.
- OCR/catalog/search APIs and retailer/ebook data access.
- GitHub Actions/build minutes, APK hosting/downloads, and release bandwidth.
- Any domain, notification service, analytics, monitoring, support, or backup service.
- Whether a free tier requires a payment card, permits automatic overage, pauses for inactivity, or has quotas that can block the app.

## No-cost operating rules

1. Do not add a paid API, plan, hosting tier, domain, or service without an explicit approved plan change.
2. Do not enable automatic paid overages or upgrades. Configure hard spending limits where available; if a provider cannot guarantee no-charge behavior, treat it as an unresolved risk.
3. If a provider has no free and permitted integration path, do not scrape around its rules or silently substitute a paid service. Mark it unsupported and return to planning.
4. Recheck free-tier terms and quotas before implementation approval and before release. “Free today” is not a guarantee of permanent free service.
5. If limits are reached, show a clear unavailable state and wait for reset or an approved plan change; never create a bill to preserve uptime.

## Open budget decisions

- Confirmed: $0 recurring cost for owner/users and no payment card or billing instrument required. Any candidate whose signup or free tier requires a card is excluded.
- Are pauses/cold starts or manual republishing acceptable, given performance is also a top priority?
- If a source or service cannot be used at $0, should that capability be omitted until a free permitted alternative exists? The safe default is yes.
- Do the five users already have/free accounts required by a candidate service, or would account creation itself be a blocker?


## No-card verification status

The published free quotas above do not by themselves prove that signup and continued use require no payment card. This requirement must be checked for each candidate before it can be selected. Until that evidence is recorded, Cloudflare, Supabase, Replit, and any other service remain unapproved candidates.

## Official-source refresh (2026-10-15)

This is a dated public-documentation refresh; it does not select a host or establish account-specific billing eligibility. Recheck at signup and before release.

- **Cloudflare Workers Free + D1:** Current Workers documentation says Free is the default plan, with 100,000 Worker requests/day and 10 ms CPU per invocation. D1 Free lists 5 million rows read/day, 100,000 rows written/day, and 5 GB account storage. Since 2026-09-01, D1 requests that exceed daily read/write quotas return errors until midnight UTC; Worker request-limit responses also fail rather than silently upgrading the service. This supports a clear degrade/stop path, not guaranteed availability. The paid Workers plan is a separate $5/month minimum and must not be enabled under the current $0 rule. [Workers pricing](https://developers.cloudflare.com/workers/platform/pricing/) · [Workers limits](https://developers.cloudflare.com/workers/platform/limits/) · [D1 pricing](https://developers.cloudflare.com/d1/platform/pricing/) · [D1 limit enforcement](https://developers.cloudflare.com/changelog/post/2026-09-01-d1-free-tier-limit-enforcement/)
- **Cloudflare Access:** Official pricing lists a $0 plan for up to 50 users, and Worker Access documentation describes request protection and access to the authenticated identity in the Worker. This is a five-user candidate to investigate, not an approved choice: native Android sign-in/session acquisition, revocation behavior, exact no-card account setup, and owner maintenance remain unverified. [Access pricing](https://www.cloudflare.com/zero-trust/products/access/) · [Worker Access](https://developers.cloudflare.com/workers/configuration/cloudflare-access/)
- **Supabase Free:** Current pricing lists $0, 500 MB database, and 5 GB egress; Free projects may pause after seven days of low activity. That pause is a material fit risk given the confirmed performance priority unless the owner accepts it. Reviewed sources do not establish the exact no-card signup flow. [Pricing](https://supabase.com/pricing) · [Free project pausing](https://supabase.com/docs/guides/platform/free-project-pausing)
- **No-card gate:** Cloudflare and Supabase remain unapproved until the no-payment-card requirement is verified in the actual free-tier activation path. Do not enter a card, enable billing, or select a paid tier to complete this verification. Published quotas and a $0 price label alone are not proof of the owner’s no-card requirement.


## Official feasibility research checkpoint (2026-10-15)

This is public-document research only. It does not select a provider, approve a data flow, establish account eligibility, or prove a complete $0/no-card operating path. Recheck limits and terms before implementation.

### Backend, authentication, distribution, and build candidates

- **Cloudflare Workers Free + D1:** Official docs list the default Free plan at 100,000 Worker requests/day and 10 ms CPU per invocation; D1 Free includes 5 million rows read/day, 100,000 rows written/day, and 5 GB total account storage. Exceeding Free limits causes operations to fail rather than automatically upgrading. This is a plausible request-based backend candidate, not evidence that end-to-end signup requires no payment card or that custom auth/revocation is safe. [Workers pricing](https://developers.cloudflare.com/workers/platform/pricing/) · [D1 pricing](https://developers.cloudflare.com/d1/platform/pricing/)
- **Cloudflare Access:** The product page lists a $0 Free plan for teams under 50 users, and official docs support one-time PIN login to approved email addresses. Neither source establishes a native Android sign-in/session design, per-user/device revocation semantics for this app, or the no-card activation path. Do not put a Cloudflare service token or shared credential in the APK. [Access pricing](https://www.cloudflare.com/zero-trust/products/access/) · [One-time PIN](https://developers.cloudflare.com/cloudflare-one/integrations/identity-providers/one-time-pin/)
- **Supabase Free:** Supabase says Free projects with low activity over a seven-day period can be paused. That is a material fit risk for a small, intermittent-use app when performance is a top priority. The published pricing and pause docs do not establish the no-card signup path. [Free project pausing](https://supabase.com/docs/guides/platform/free-project-pausing)
- **Firebase Spark + App Distribution:** Firebase says most no-cost Spark features can be started without payment information; its Authentication docs list a 3,000 daily-active-user limit for most sign-in providers on Spark. Firebase describes App Distribution as no-cost; its tester docs cap a project at 500 testers and a group at 200. This is a plausible identity and private-test-distribution candidate, not a complete backend: account invitation, membership, device policy, protected GitHub-release access, and immediate revocation still need an authenticated server check. [Firebase billing plans](https://firebase.google.com/docs/projects/billing/firebase-pricing-plans) · [Auth limits](https://firebase.google.com/docs/auth/limits) · [App Distribution](https://firebase.google.com/products/app-distribution) · [Tester limits](https://firebase.google.com/docs/app-distribution/add-remove-testers)
- **GitHub Actions:** Standard GitHub-hosted runners are free for public repositories; private-repository minutes and storage depend on the account plan, and use beyond included amounts is billed. The account's plan/quota was not checked, so no exact budget is claimed. Making this planning repo public is not an access prerequisite for the connected GitHub integration and must not be done just to seek public-runner minutes. [GitHub Actions billing](https://docs.github.com/en/billing/concepts/product-billing/github-actions)

**Security architecture constraint:** If a backend is selected, the Android client must not contain GitHub/provider secrets, service tokens, or signing keys. The backend must authorize every protected operation against an owner-approved invite/member/device state and fail closed when the authorization check is unavailable. Token validity alone is not group membership or timely revocation.

### Retailer/catalog feasibility

- **Amazon.sa:** Amazon's current Creators API documentation includes a Saudi marketplace locale (www.amazon.sa), and SearchItems can search product catalog entries. Access requires enrollment in the Associates program for the target marketplace, at least 10 qualifying sales within the previous 30 days, API registration, and credentials. Therefore an endpoint exists, but this does not prove that this owner qualifies, that the app's private-group use satisfies program rules, or that required offer/affiliate disclosures fit the product. Keep it unapproved until eligibility and permitted use are confirmed. [Creators API prerequisites](https://affiliate-program.amazon.com/creatorsapi/docs/en-us/introduction) · [Saudi locale](https://affiliate-program.amazon.com/creatorsapi/docs/en-us/locale-reference/saudi-arabia) · [SearchItems](https://affiliate-program.amazon.com/creatorsapi/docs/en-us/api-reference/operations/search-items)
- **Noon:** Official API documentation describes marketplace services for sellers, integrators, and partners. OAuth integration requires Noon partner-integrator registration, credentials obtained from Noon, authenticated integrator service-account access, and seller authorization for seller-scoped access. The reviewed docs do not establish a public consumer catalog-search API for this app; do not scrape or treat seller/partner APIs as shopper search. [Noon API introduction](https://noon-docs.noonpartners.dev/docs/getting-started/intro) · [OAuth prerequisites](https://noon-docs.noonpartners.dev/docs/authorization/getting-credentials-oauth)

**Current result:** Q-83 is partially researched, not resolved. No retailer is approved. Other public book-metadata sources may help identify titles but do not prove Saudi delivery, stock, or ebook entitlement.
