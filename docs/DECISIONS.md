# Decision log

**Last updated:** 2026-10-14

Only items in **Confirmed** are settled by the user. **Proposed** items are recommendations, not authorization. **Open** items require a user decision.

## Confirmed

- Target is an Android APK app.
- The image contains a list of multiple books; it is not a cover-photo identification workflow.
- Book search can use ISBN, name/title, or both.
- Show identification confidence.
- Flag different editions and versions.
- Search physical books and ebooks.
- Saudi Arabia is the required destination/market. If shipping is uncertain, state that it is uncertain.
- Flag ebook regional restrictions or uncertain availability.
- Manage the provider list through GitHub. Amazon.sa and Noon were examples, not confirmed working sources.
- Check for a GitHub-based app update on launch; user approval is required to install the APK.
- Intended usage is about five people, with strong privacy and security.
- The plan is the source of truth. Do not add unapproved scope.

## Proposed, not yet approved

- Process list images on-device by default and discard the source image after extraction.
- Separate OCR confidence from catalog-match confidence.
- Use a server-side component to access private GitHub release/config data; never embed a GitHub token in the APK.
- Allow only invited accounts/devices, with a way to revoke access.
- Cache provider configuration safely and show when data was last checked.
- Check for a new app version on launch only when the last check is old enough, and keep the check non-blocking.

## Open

- Supported image types: printed, handwritten, screen capture, or a combination.
- Languages expected in lists and desired app languages, including Arabic/English support.
- Whether “number of books” means total entries, quantity per title, or both.
- Initial providers to support and the permitted search/API method for each. Example providers are not yet integrations.
- Whether “Saudi-only” means Saudi-based sellers only, or any seller that can deliver to Saudi Arabia; the confirmed requirement is that Saudi delivery must not be falsely asserted.
- What evidence is sufficient to mark delivery or ebook access as confirmed.
- How to present an uncertain shipping result and what user action should follow.
- How users choose between editions, translations, and formats.
- Whether images, recognized entries, history, or favorites are saved; where and for how long.
- How the five people are invited/authenticated and how access is revoked.
- How the first APK is distributed privately; in-app update flow applies after installation.
- Whether a small backend is acceptable for private GitHub access and update delivery.
- Android version/device support, update-check frequency, and whether update notifications beyond app launches are needed.
- Whether search details may be sent directly to external providers or must be brokered through a server.
- Numeric success targets for extraction and matching, after sample lists are available.