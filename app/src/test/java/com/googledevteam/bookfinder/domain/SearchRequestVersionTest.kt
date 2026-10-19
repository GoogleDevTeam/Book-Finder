package com.googledevteam.bookfinder.domain

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SearchRequestVersionTest {
    @Test
    fun beginningANewLookupInvalidatesThePreviousResponse() {
        val versions = SearchRequestVersion()
        val olderRequest = versions.begin()
        val newerRequest = versions.begin()

        assertFalse(versions.isCurrent(olderRequest))
        assertTrue(versions.isCurrent(newerRequest))
    }

    @Test
    fun clearingAScanInvalidatesAnInFlightLookup() {
        val versions = SearchRequestVersion()
        val request = versions.begin()

        versions.invalidate()

        assertFalse(versions.isCurrent(request))
    }
}
