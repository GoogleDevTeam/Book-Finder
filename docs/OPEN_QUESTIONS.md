# Open questions

**Last updated:** 2026-10-14  
These are discovery questions, not permission to implement a guessed answer.

## Priority 0 — needed to define core behavior

1. For a line such as “Dune, 2nd edition ×3,” should the app track only the title/edition entry, quantity 3, or both? The owner is unsure; edition and copy count are separate.
2. Which provider(s) should be tested first? Amazon.sa and Noon are examples only. What provider access methods are acceptable?
3. Does Saudi-only allow a non-Saudi seller if Saudi delivery is explicitly confirmed, or must the seller itself be based in Saudi Arabia?
4. What evidence is enough for “confirmed” Saudi delivery and confirmed ebook-region access?
5. Should results with unconfirmed shipping be displayed as candidates with a warning, or hidden until delivery is verified?
6. What should count as the “closest” edition, and what should happen if no candidate is suitable? The owner chose to preselect the closest and flag differences; criteria and fallback remain open.

## Priority 1 — privacy, access, and operations

7. How should about five people be invited and authenticated? How should access be revoked?
8. Should images and extracted lists remain only on-device? Should the app save history or favorites?
9. Is a small private backend acceptable to mediate access to GitHub releases/configuration?
10. May the app send title/author/ISBN searches directly to providers, or should searches be brokered through a backend?
11. How will the first APK be shared privately? Later updates are intended to happen from within the installed app.
12. Which Android versions/devices must be supported? How often should update checks run on launch?
13. Should the interface support Arabic, English, or both?

## Priority 2 — measurement and later scope

14. Provide representative lists so extraction success can be measured before setting a target.
15. Set acceptable identification confidence and define what “accurate enough” means for users.
16. Decide whether price/stock freshness needs a target and how stale results should be handled.

Do not resolve these by assumption. Record each user answer in DECISIONS.md and update the plan before implementation.