package com.example.foodswiper

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import junit.framework.TestCase.assertFalse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

private const val DATASTORE_TEST_NAME: String = "test_datastore"

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)

class DataStoreTest {
    private val testContext: Context =
        InstrumentationRegistry.getInstrumentation().targetContext

    private val testDispatcher = UnconfinedTestDispatcher()

    private val testScope = TestScope(testDispatcher + Job())

    private val testDataStore: DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            scope = testScope,
            produceFile =
            { testContext.preferencesDataStoreFile(DATASTORE_TEST_NAME) }
        )
    private val dataStorage: PreferenceStorageClass =
        PreferenceStorageClass(testDataStore)


    @Test
    fun dataStoreInitial_Test() = testScope.runTest {

        assertFalse(dataStorage.getStartupFlag())

    }
}