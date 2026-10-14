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
