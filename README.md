# Book-Finder

**Repository status:** Planning draft — not approved for implementation  
**Visibility:** Private

Book-Finder is a proposed Android APK app for identifying several books from a photo of a book list and finding physical-book and ebook options with clear Saudi Arabia delivery and region status.

## Scope lock

This repository is the single source of truth for product scope. The current plan is still in discovery. Nothing is authorized for implementation until the owner explicitly approves the complete plan and says to begin implementation.

Only requirements marked **Confirmed** in the plan may be treated as agreed. Open questions and recommendations are not approvals. Do not add features, providers, data collection, integrations, or app behavior that is not in the approved plan. Put new ideas in a change proposal and wait for explicit approval before changing scope or implementing them.

## Planning documents

- [Product plan](docs/APP_PLAN.md) — intended user journey, confirmed scope, acceptance criteria, and non-goals.
- [Cost and hosting evaluation](docs/COST_AND_HOSTING.md) — dated free-tier comparison; no host selected.
- [Discovery catalog](docs/DISCOVERY_CATALOG.md) — broad question inventory, assumptions, dependencies, and foreseeable risks.
- [Decision log](docs/DECISIONS.md) — confirmed decisions, proposed choices, and unresolved decisions.
- [Risk register](docs/RISK_REGISTER.md) — foreseeable issues, prevention, and fallbacks.
- [Open questions](docs/OPEN_QUESTIONS.md) — current discovery items that must be settled before implementation.
- [Scope change control](docs/CHANGE_CONTROL.md) — plan-first traceability and implementation gate.

## Working rules

1. Read the approved plan before doing product or engineering work.
2. Every app change—including additions, edits, creations, and bug fixes—must map to an approved plan requirement before work starts and be recorded in the plan/change log.
3. If a request conflicts with the plan, stop and propose a plan change; do not silently choose or implement it.
4. Keep provider examples distinct from confirmed integrations.
5. Never describe uncertain Saudi shipping or ebook-region access as confirmed.
6. Keep secrets out of GitHub and the APK. Private repository access is not a secrets-management system.

The repository intentionally contains planning documents only.