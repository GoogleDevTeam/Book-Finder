package com.googledevteam.bookfinder.domain

/** Tracks the latest lookup so an older response cannot overwrite a newer scan. */
internal class SearchRequestVersion {
    private var currentVersion = 0

    fun begin(): Int {
        currentVersion += 1
        return currentVersion
    }

    fun invalidate() {
        currentVersion += 1
    }

    fun isCurrent(requestVersion: Int): Boolean = requestVersion == currentVersion
}
