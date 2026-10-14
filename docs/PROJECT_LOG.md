# Project log

**Repository:** Book-Finder  
**Started:** 2026-10-14  
**Purpose:** Concise, auditable record of app-related requirements, decisions, proposals, open questions, risks, plan changes, and work outcomes.

## Logging rule

Every app-related discussion or action must be summarized here. Before any app change, the approved plan must authorize it and be updated first. After the change, record the Git commit/reference and verification result before continuing. Use statuses: Confirmed, Proposed, Open, Rejected, Completed. This is a project record, not a verbatim chat transcript. Never store credentials, tokens, private keys, or unnecessary personal information.

## Backfilled project history

| Date | Type | Record | Status / reference |
|---|---|---|---|
| 2026-10-14 | Product scope | Android APK concept: photograph a list containing multiple books and fields; identify/search by ISBN/title, show confidence, and flag editions. | Confirmed; APP_PLAN.md; initial plan commit c3123dd05f46530208e6f0cf30177fdd39be0d18 |
| 2026-10-14 | Market/provider scope | Search physical books and ebooks for Saudi Arabia; distinguish confirmed/uncertain shipping and ebook regional availability. Amazon.sa and Noon are examples, not confirmed integrations. | Confirmed constraints; provider feasibility open; c3123dd05f46530208e6f0cf30177fdd39be0d18 |
| 2026-10-14 | Distribution | Intended audience is about five invited people; GitHub-based updates should be checked in app, and Android installation requires user approval. | Confirmed goals; authentication/update design open; c3123dd05f46530208e6f0cf30177fdd39be0d18 |
| 2026-10-14 | Governance | The plan is the scope authority. No implementation before explicit approval; new work must be planned first. | Confirmed; planning gate 64e1c9eabb17429964a26659b26fd1cfbcbd8008 |
| 2026-10-14 | Discovery | Added comprehensive question and risk inventory, with open items explicitly marked rather than assumed. | Completed; abc934df1ed7d49a4a2518174b792352af729f71 |
| 2026-10-14 | Priority | Security, safety, and performance are the owner’s top priorities; the APK cannot be guaranteed impossible to copy or reverse-engineer. | Confirmed priority; controls remain open; d04e70316fb2fdbca261e486b5aeb69df8a6fa8a |
| 2026-10-14 | Access design | Proposed one sign-in/device enrollment with automatic renewal and backend checks; one-time install-only checks are inadequate for revocation. | Proposed, not approved; b36a36ce3024a1f8479eb7e331f627ad22627439 |
| 2026-10-14 | Performance | Proposed bundled authorization, non-blocking conditional update checks, bounded provider searches, and measured OCR performance. | Proposed, not approved; f14377cea4d4a39073de46a9e94d40a3c072c52d |
| 2026-10-14 | Cost | Owner wants everything free; no paid overages or upgrades without explicit approval. | Confirmed preference; 408650ede0d4fc4e5693a35c394753ea6cc26ccd |
| 2026-10-14 | Cost constraint | No service may require a payment card for its free tier. Candidates remain unapproved until signup/billing terms are verified. | Confirmed; ea6df04ab34f2ce6c3c94d8d25149a4c6adf31c2 |
| 2026-10-14 | Repository | Created private Book-Finder planning repository; no app code or provider integration added. | Completed; repository main branch |
| 2026-10-14 | Logging policy | Owner requires app-related discussions and work to be recorded in this project log and plan. | Confirmed; this commit |

## Current status

- Planning only; no app implementation has started.
- Hosting, provider integrations, login method, offline policy, and performance targets are not selected.
- Free/no-card requirements are hard planning constraints; candidate services must be verified before selection.

## New entries

Append new project events here in chronological order. Update the relevant plan, decision, discovery, or risk document in the same change when applicable.

| 2026-10-14 | Work direction | Owner asked to continue with the existing discovery questions before implementation; no approval to begin the build was given. | Confirmed; planning only |
| 2026-10-14 | Discovery | List-format/language question presented; no decision captured. | Open; OPEN_QUESTIONS.md item 1 |
| 2026-10-14 | Discovery | Meaning of “number of books” question presented; no decision captured. | Open; OPEN_QUESTIONS.md item 2 |
| 2026-10-14 | Project log | Owner asked for ongoing records of completed and in-progress work; this existing project log will be used rather than creating a duplicate. | Confirmed |

| 2026-10-14 | Quality planning request | Owner asked how to catch likely errors before they occur, consider features, and make the app feel smooth and polished. | Proposed; no new features or implementation approved |
| 2026-10-14 | Recommended next work | Convert the risk register and acceptance criteria into a pre-build test matrix and UI state specification; keep new feature ideas as proposals until explicitly approved. | Proposed; planning only |

| 2026-10-14 | Planning audit | Reviewed current planning docs, full GitHub issue/PR history, and selected official provider/platform docs. Found backlog mismatch (86 open catalog questions; 37 effective P0 blockers versus 17 prompts in OPEN_QUESTIONS.md), missing requirement IDs, two overlapping unscored risk lists, stale conflicting no-card wording, and no executable test matrix. Public-source checks flagged Amazon Creators API eligibility, unverified Noon consumer search, APK install/signing steps, and unverified no-card hosting feasibility. | Completed; detailed findings and sources in docs/PLAN_AUDIT.md; commit e9efa2234142682c06d1a4695c09d844c70518bb; no app code/scope changes |
