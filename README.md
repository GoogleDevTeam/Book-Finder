# Book Finder

Book Finder is a planned, serverless native Android app (minimum Android API 29) for turning a photo of a book list or school syllabus into book details and one-tap retailer searches. The target architecture is local-first: no custom backend, account system, scraping pipeline, or price aggregator. This repository contains the specification; the app has not been implemented yet.

## What the app does

- Capture a printed or displayed list with the native CameraXViewfinder composable in embedded mode.
- Recognize Latin-script text on-device with Google ML Kit Text Recognition v2.
- Find and validate ISBN-10/ISBN-13 candidates; if no ISBN is present, let the user review or edit an English title.
- Query the Google Books API and show the normalized title, author, and cover image.
- Save scan history and cached book metadata on the device with Room.
- Open a prefilled search at Jarir, Amazon.sa, or Noon in the retailer app when Android can resolve it, otherwise in Chrome Custom Tabs.

Retailer links only hand off a search. Book Finder does not fetch retailer pages, scrape prices, or claim live price, stock, shipping, or ebook availability.

## How it works

CameraXViewfinder capture → on-device OCR → ISBN/title review → Google Books metadata lookup → Room cache/history → retailer search link

The camera image is processed on the device and is not uploaded in the default configuration. The app sends only the selected ISBN or English title to Google Books for a lookup. Cached results and scan history stay in the local Room database; there is no cloud sync or custom server.

## Arabic OCR

ML Kit Text Recognition v2's on-device model does not support Arabic text recognition. The default app therefore supports ISBNs and English-title fallback, not Arabic-title extraction. An ISBN can still be used when OCR reliably recognizes it on a page that also contains Arabic text.

If Arabic text extraction from school syllabi is a release requirement, use Google Cloud Vision OCR as an explicit, opt-in architecture change. That sends the selected image or crop to Google's service and changes the privacy and cost assumptions. Do not enable it silently; provide user notice and consent, restrict the Google Cloud API key to the Android app and Vision API, and set quotas. Never ship a service-account key in the app.

## Tech stack

- Kotlin, Jetpack Compose, and Material 3 for the native Android UI (minimum Android API 29)
- CameraX with the native CameraXViewfinder composable using ImplementationMode.EMBEDDED
- Google ML Kit Text Recognition v2, Latin model, for on-device OCR
- Google Books API for book metadata
- Android Room (SQLite) for local scan history and cached metadata
- Kotlin coroutines and Flow for asynchronous work and observable local state
- Retrofit/OkHttp for HTTP requests; Coil for cover images
- Firebase App Distribution for private testing with five users

## UI and motion

Use Material 3 tonal palettes and standard shape tokens: 12dp card corners and 28dp bottom sheets, without heavy drop shadows. Support predictive-back animations. Wrap scan/results transitions in a Compose LookaheadScope and use spring motion; animate a bounding box to the recognized OCR text, then transition the selected text into the results card or sheet when tapped.

## Scope boundaries

- No custom backend, broker, sign-in, cloud sync, or user tracking.
- No web scraping, DOM parsing, headless browser, or retailer API integration.
- No in-app price comparison or claim of current retailer prices.
- No custom APK updater and no GitHub Release proxy.
- Arabic OCR is not part of the default on-device feature set.

## Project documents

The current product and architecture specification is this README plus [IMPLEMENTATION_GUIDE.md](IMPLEMENTATION_GUIDE.md). The 13 legacy planning files were removed from the current main branch; their earlier versions remain in Git history and are not current requirements.
