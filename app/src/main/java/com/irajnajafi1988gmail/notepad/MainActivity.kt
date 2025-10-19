package com.irajnajafi1988gmail.notepad

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.irajnajafi1988gmail.notepad.data.local.datastore.prefKeys.LanguagePrefKeys
import com.irajnajafi1988gmail.notepad.data.local.datastore.provider.LanguageDataStore
import com.irajnajafi1988gmail.notepad.domain.model.ItemLanguage
import com.irajnajafi1988gmail.notepad.domain.utils.LanguageManager
import com.irajnajafi1988gmail.notepad.navigation.NaveGraph
import com.irajnajafi1988gmail.notepad.ui.theme.NotepadTheme
import com.irajnajafi1988gmail.notepad.ui.viewModel.DarkModeViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.irajnajafi1988gmail.notepad.domain.model.ItemDarkMode
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

@Suppress("DEPRECATION")
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // Store current language code
    private lateinit var languageCode: String

    override fun attachBaseContext(newBase: Context?) {
        // Retrieve saved language code from DataStore synchronously
        languageCode = runBlocking {
            newBase?.LanguageDataStore?.data
                ?.map { pref ->
                    pref[LanguagePrefKeys.LANGUAGE_CODE] ?: ItemLanguage.ENGLISH.code
                }
                ?.first() ?: ItemLanguage.ENGLISH.code
        }

        // Apply selected language to the context
        val updatedContext = newBase?.let {
            LanguageManager.applyLanguage(it, languageCode)
        }

        super.attachBaseContext(updatedContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge display (content behind system bars)
        enableEdgeToEdge()

        setContent {
            val darkModeViewModel: DarkModeViewModel = hiltViewModel()
            val darkMode by darkModeViewModel.selectedDarkMode.collectAsState(initial = null)

            // System UI controller for status & navigation bar customization
            val systemUiController = rememberSystemUiController()

            if (darkMode == null) {
                // Show loading indicator while dark mode is not loaded
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                // Update system bars based on dark mode
                val useDarkIcons = darkMode != ItemDarkMode.DARK
                systemUiController.setStatusBarColor(
                    color = androidx.compose.ui.graphics.Color.Transparent,
                    darkIcons = useDarkIcons
                )
                systemUiController.setNavigationBarColor(
                    color = androidx.compose.ui.graphics.Color.Transparent,
                    darkIcons = useDarkIcons
                )

                // Key on language code to recompose UI when language changes
                key(languageCode) {
                    NotepadTheme(darkMode = darkMode!!) {
                        // Launch main navigation graph
                        NaveGraph(languageCode = languageCode)
                    }
                }
            }
        }
    }
}
