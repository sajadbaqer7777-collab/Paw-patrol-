package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.model.PupCharacter
import com.example.model.ScreenState
import com.example.viewmodel.PawPortalViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read app_name string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Paw Portal", appName)
    }

    @Test
    fun `test PawPortalViewModel navigation and companion selection`() {
        val viewModel = PawPortalViewModel()
        assertEquals(ScreenState.PORTAL_HOME, viewModel.uiState.value.currentScreen)
        assertEquals(PupCharacter.CHASE, viewModel.uiState.value.selectedCompanion)

        viewModel.navigateTo(ScreenState.GAME_SKY_PHONICS)
        assertEquals(ScreenState.GAME_SKY_PHONICS, viewModel.uiState.value.currentScreen)

        viewModel.selectCompanion(PupCharacter.SKYE)
        assertEquals(PupCharacter.SKYE, viewModel.uiState.value.selectedCompanion)
    }

    @Test
    fun `test completing mission awards stars and treats`() {
        val viewModel = PawPortalViewModel()
        val initialStars = viewModel.uiState.value.starsCount
        val initialTreats = viewModel.uiState.value.treatsCount

        viewModel.completeMission("Mission Test", 3, 2)
        assertEquals(initialStars + 3, viewModel.uiState.value.starsCount)
        assertEquals(initialTreats + 2, viewModel.uiState.value.treatsCount)
        assertTrue(viewModel.uiState.value.isCelebrationOpen)
    }
}
