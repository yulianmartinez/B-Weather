package com.bold.feature.settings.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsRepositoryImplTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var dataStoreScope: CoroutineScope
    private lateinit var testFile: File
    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var repository: SettingsRepositoryImpl

    @Before
    fun setUp() {
        dataStoreScope = CoroutineScope(testDispatcher + Job())
        testFile = File.createTempFile("test_datastore", ".preferences_pb")
        dataStore = PreferenceDataStoreFactory.create(
            scope = dataStoreScope,
            produceFile = { testFile }
        )
        repository = SettingsRepositoryImpl(dataStore)
    }

    @After
    fun tearDown() {
        testFile.delete()
        dataStoreScope.cancel()
    }

    @Test
    fun `getUserSettings should return default values initially`() = runTest {
        val settings = repository.getUserSettings().first()
        
        assertEquals(true, settings.useCelsius)
        assertEquals("es", settings.language)
        assertEquals(null, settings.lastLat)
        assertEquals(null, settings.lastLon)
    }

    @Test
    fun `updateTemperatureUnit should save value to DataStore`() = runTest {
        repository.updateTemperatureUnit(false)
        
        val settings = repository.getUserSettings().first()
        assertEquals(false, settings.useCelsius)
    }

    @Test
    fun `updateLanguage should save value to DataStore`() = runTest {
        repository.updateLanguage("en")
        
        val settings = repository.getUserSettings().first()
        assertEquals("en", settings.language)
    }

    @Test
    fun `saveLastLocation should save coordinates to DataStore`() = runTest {
        repository.saveLastLocation(4.6, -74.0)
        
        val settings = repository.getUserSettings().first()
        assertEquals(4.6, settings.lastLat)
        assertEquals(-74.0, settings.lastLon)
    }
}
