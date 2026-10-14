# Open questions

**Last updated:** 2026-10-14  
These are discovery questions, not permission to implement a guessed answer.

## Priority 0 — needed to define core behavior

1. Which languages/scripts should be supported, and should one list/page be allowed to mix printed and typed/on-screen content? Printed and typed/on-screen lists were selected for v1; handwriting was not selected.
2. Does “number of books” mean the number of list entries, a quantity for each title, or both?
3. Which provider(s) should be tested first? Amazon.sa and Noon are examples only. What provider access methods are acceptable?
4. Does Saudi-only allow a non-Saudi seller if Saudi delivery is explicitly confirmed, or must the seller itself be based in Saudi Arabia?
5. What evidence is enough for “confirmed” Saudi delivery and confirmed ebook-region access?
6. Should results with unconfirmed shipping be displayed as candidates with a warning, or hidden until delivery is verified?
7. Should the app require an exact edition/translation, or let the user choose among flagged alternatives?

## Priority 1 — privacy, access, and operations

8. How should about five people be invited and authenticated? How should access be revoked?
9. Should images and extracted lists remain only on-device? Should the app save history or favorites?
10. Is a small private backend acceptable to mediate access to GitHub releases/configuration?
11. May the app send title/author/ISBN searches directly to providers, or should searches be brokered through a backend?
12. How will the first APK be shared privately? Later updates are intended to happen from within the installed app.
13. Which Android versions/devices must be supported? How often should update checks run on launch?
14. Should the interface support Arabic, English, or both?

## Priority 2 — measurement and later scope

15. Provide representative lists so extraction success can be measured before setting a target.
16. Set acceptable identification confidence and define what “accurate enough” means for users.
17. Decide whether price/stock freshness needs a target and how stale results should be handled.

Do not resolve these by assumption. Record each user answer in DECISIONS.md and update the plan before implementation.