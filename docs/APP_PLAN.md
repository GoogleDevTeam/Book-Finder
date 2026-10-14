# Book-Finder product plan

**Version:** 0.1  
**Last updated:** 2026-10-14  
**Status:** Draft — discovery; not approved for implementation  
**Audience:** Product owner and the small invited user group

## 1. Product goal

Help a small, private group turn a photographed list of multiple books into reviewable book records, identify likely titles and editions, and find physical-book or ebook options while clearly showing whether delivery or access in Saudi Arabia is confirmed, uncertain, or unavailable.

## 2. Confirmed scope

- Android app distributed as an APK.
- Input is a photo of a list of books, not a photo of book covers. A list can contain multiple books and fields such as number/quantity, title, author, ISBN, and other book details.
- Users can search using ISBN, book name/title, or both.
- The app shows how confident it is in book identification.
- Different editions and versions are flagged for the user.
- Search for physical books and ebooks online.
- Saudi Arabia is the required delivery/access market. If shipping is uncertain, label it as uncertain rather than confirmed.
- Ebook regional availability or restrictions must also be flagged.
- The provider list is to be maintained through GitHub. Amazon.sa and Noon are examples only; neither is yet a confirmed integration.
- Users should receive an in-app update notice from GitHub-based releases. The app may make a small version check on launch. The user approves Android installation.
- The intended group is approximately five people; privacy and security are high priorities.
- Work must follow this plan. New scope requires explicit approval and a plan update first.

## 3. Proposed user journey

1. User photographs or uploads a book list.
2. App extracts separate book entries and their visible fields.
3. User reviews and corrects uncertain entries before searching.
4. App searches by available ISBN/title/author details and displays likely matches with confidence.
5. App flags distinct editions/versions instead of treating them as identical.
6. App displays supported physical and ebook sources with explicit Saudi delivery or region status, source, and checked time where available.
7. If a newer signed app release is available, the app notifies the user and offers the Android installer; installation requires user approval.

The details in this section beyond confirmed scope are proposed design direction and remain subject to open questions.

## 4. Required result states

- Identification confidence must be visible; uncertain text or matches must not be presented as certain.
- Edition/version differences must be visible before a user treats a result as the exact requested book.
- Physical delivery must distinguish confirmed Saudi shipping, uncertain shipping, and no Saudi shipping. Unknown must never be labeled confirmed.
- Ebook results must show Saudi-region availability or uncertainty separately from physical shipping.
- A failed or unavailable provider must not be reported as having no matching books; show that the source could not be checked.

The exact evidence required for a “confirmed” shipping or region label is an open decision.

## 5. Privacy and security requirements

- Limit use to the intended small invited group; exact access-control method is undecided.
- Protect images, extracted lists, search history, provider credentials, GitHub credentials, and app-signing material.
- Do not put GitHub tokens, provider secrets, or signing keys in the APK or repository.
- Do not claim that private GitHub hosting alone makes app data or external searches private.
- Minimize collection and retention. Whether images are processed entirely on-device, and whether history is saved, remain open decisions.
- Verify app updates and preserve the trusted Android signing identity. Android installation must require user approval.

## 6. Non-goals unless explicitly approved later

- Building or publishing the app before this plan is approved.
- Adding providers merely because an example URL was mentioned.
- Automatic purchase, payment processing, or ebook hosting.
- Silent APK installation.
- Public user registration or public access.
- Uploading or retaining list photos by default.
- Features such as recommendations, social sharing, price alerts, barcode scanning, or saved shelves. These are not in the current scope.

## 7. Acceptance criteria for a future implementation

- Given a legible list containing multiple book entries, the app presents separate entries for user review rather than one combined title.
- Given an uncertain extracted field or match, the app marks uncertainty and allows correction before searching.
- Given multiple editions or versions, the app visibly flags the differences.
- Given uncertain Saudi shipping, the result says shipping is unconfirmed and does not imply that delivery is guaranteed.
- Given uncertain ebook-region access, the result flags the uncertainty.
- Given a new app release, the app can notify the user and invoke Android installation only after user approval.
- No feature outside the approved plan is implemented.

Quantitative OCR accuracy and response-time targets are intentionally unset until representative list samples and provider behavior are understood.

## 8. Success measures to define

- Correct book rows and fields on representative user-provided lists.
- Rate of results users confirm as the intended title and edition.
- Correctness and freshness of Saudi shipping/ebook status labels.
- Successful update checks and user-approved updates.
- No unauthorized access or unexpected image retention.

Baselines and numeric targets are open decisions; do not invent them.