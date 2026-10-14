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

## Planning completion gate

No implementation begins until the owner has reviewed and explicitly approved the plan and says to begin. Before that approval, the planning documents must:

- Resolve every Priority 0 question in OPEN_QUESTIONS.md and record the answers in DECISIONS.md.
- Define the end-to-end user flows, supported inputs, outputs, access model, data flow, retention, provider boundaries, and update/distribution approach.
- Give every in-scope requirement an observable acceptance criterion.
- For each material risk, record prevention, how it will be detected, a safe fallback, and any residual risk. No known high-impact risk may be silently treated as solved; it must be mitigated or explicitly accepted by the owner.
- Validate the feasibility and permitted access method of every selected provider. Examples are not integrations.
- Include a test plan for OCR and corrections, book/edition matching, Saudi shipping and ebook uncertainty, privacy/access, outages, and signed APK updates with user approval.
- List non-goals and keep any unapproved ideas out of implementation scope.

Planning cannot guarantee that no future defect will occur. The goal is to prevent foreseeable failures, detect others early, define safe failure behavior, and make remaining uncertainty visible before implementation.

## Implementation gate

No implementation starts until the owner explicitly approves the complete plan and says to begin. Brainstorming, recommendations, or technical suggestions do not count as approval. If a request conflicts with the plan, stop and ask whether the plan should change.