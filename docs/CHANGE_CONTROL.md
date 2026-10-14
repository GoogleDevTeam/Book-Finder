# Scope and change control

The owner requested that all work follow the plan and nothing else be added. This is a binding project workflow rule.

## Source of truth

- The explicitly approved version of docs/APP_PLAN.md defines what may be built.
- Confirmed decisions in docs/DECISIONS.md refine that plan.
- docs/DISCOVERY_CATALOG.md tracks the complete discovery inventory, assumptions, dependencies, and risks.
- Open questions and proposed ideas are not requirements.
- A feature or provider mentioned as an example is not an approved integration or feature.

## Plan-first traceability for every change

Before adding, editing, or creating any app artifact—including screens, user flows, code, tests, data models, provider entries, APIs, backend services, permissions, build/release configuration, or user-visible behavior:

1. Identify the approved requirement ID in the plan that authorizes the work.
2. If no approved requirement covers it, do not make the app change. Write a change proposal and wait for the owner's explicit approval.
3. If approved, update the plan and decision/risk documents first, assign or reference a requirement ID, then make the change.
4. Record the resulting implementation change and its verification against that requirement in the same change record/commit or linked change log.
5. If work reveals a new requirement, risk, data flow, or behavior, stop and update the plan before continuing.

This applies to bug fixes as well as new features. A fix may be narrow, but it still needs a plan reference, a defined expected behavior, and a verification step. Do not add unrequested improvements while fixing an issue.

## Change process

Before changing scope or implementing anything absent from the approved plan:

1. Write a proposal describing the requested change and user benefit.
2. Identify affected screens/flows, data, permissions, providers, privacy/security risks, and dependencies.
3. Describe what existing scope or timeline it affects.
4. Mark it Proposed; do not implement it.
5. Wait for the owner's explicit approval.
6. If approved, update APP_PLAN.md, DISCOVERY_CATALOG.md, DECISIONS.md, OPEN_QUESTIONS.md, and RISK_REGISTER.md as applicable; record the date and reason.
7. Only then treat the revised plan as authorized.

## Change proposal template

- Requested change:
- User benefit:
- Affected requirement IDs and flows:
- Data/permissions/providers affected:
- Risks and mitigations:
- Dependencies or trade-offs:
- Acceptance criteria and verification:
- Status: Proposed / Approved / Rejected:
- Owner approval and date:

## Planning completion gate

No implementation begins until the owner has reviewed and explicitly approved the complete plan and says to begin. Before that approval, the planning documents must:

- Resolve every Priority 0 question in OPEN_QUESTIONS.md and record the answers in DECISIONS.md.
- Define end-to-end user flows, supported inputs/outputs, access model, data flow/retention, provider boundaries, and update/distribution approach.
- Give every in-scope requirement an observable acceptance criterion.
- For each material risk, record prevention, detection, safe fallback, and residual risk. No known high-impact risk may be silently treated as solved; it must be mitigated or explicitly accepted by the owner.
- Validate the feasibility and permitted access method of every selected provider. Examples are not integrations.
- Include a test plan for OCR and corrections, book/edition matching, Saudi shipping and ebook uncertainty, privacy/access, outages, and signed APK updates with user approval.
- List non-goals and keep unapproved ideas out of implementation scope.

Planning cannot guarantee that no future defect will occur. The goal is to prevent foreseeable failures, detect others early, define safe failure behavior, and make remaining uncertainty visible before implementation.

## Implementation gate

No implementation starts until the owner explicitly approves the complete plan and says to begin. Brainstorming, recommendations, or technical suggestions do not count as approval. If a request conflicts with the plan, stop and ask whether the plan should change.