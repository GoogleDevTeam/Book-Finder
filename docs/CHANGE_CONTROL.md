# Scope and change control

The owner requested that work follow the plan and not add anything else. This document makes that a working rule.

## Source of truth

- The approved version of docs/APP_PLAN.md defines what may be built.
- Confirmed decisions in docs/DECISIONS.md refine that plan.
- Open questions and proposed ideas are not requirements.
- A feature mentioned as an example is not an approved integration or feature.

## Change process

Before changing scope or implementing anything absent from the approved plan:

1. Write a proposal describing the requested change and user benefit.
2. Identify affected screens/flows, data, permissions, providers, privacy/security risks, and dependencies.
3. Describe what existing scope or timeline it affects.
4. Mark it Proposed; do not implement it.
5. Wait for the owner’s explicit approval.
6. If approved, update APP_PLAN.md, DECISIONS.md, OPEN_QUESTIONS.md, and RISK_REGISTER.md as applicable; record the date and reason.
7. Only then treat the revised plan as authorized.

## Change proposal template

- Requested change:
- User benefit:
- Affected requirements and flows:
- Data/permissions/providers affected:
- Risks and mitigations:
- Dependencies or trade-offs:
- Status: Proposed / Approved / Rejected:
- Owner approval and date:

## Implementation gate

No implementation starts until the owner explicitly approves the plan and says to begin. Brainstorming, recommendations, or technical suggestions do not count as approval. If a request conflicts with the plan, stop and ask whether the plan should change.