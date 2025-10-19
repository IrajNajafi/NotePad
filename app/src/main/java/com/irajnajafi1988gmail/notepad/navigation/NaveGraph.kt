package com.irajnajafi1988gmail.notepad.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.irajnajafi1988gmail.notepad.ui.screens.AddCheckListScreen
import com.irajnajafi1988gmail.notepad.ui.screens.AddNoteScreen
import com.irajnajafi1988gmail.notepad.ui.screens.FavoriteScreen
import com.irajnajafi1988gmail.notepad.ui.screens.HomeScreen
import com.irajnajafi1988gmail.notepad.ui.screens.SearchScreen
import com.irajnajafi1988gmail.notepad.ui.screens.TrashScreen

@Composable
fun NaveGraph(languageCode: String) {

    val navController = rememberNavController()


    val isRtl = languageCode in listOf("fa", "ar", "he")
    val languageDirection = if (isRtl) LayoutDirection.Rtl else LayoutDirection.Ltr
    CompositionLocalProvider(LocalLayoutDirection provides languageDirection) {

        NavHost(
            navController = navController,
            startDestination = NaveScreen.HomeScreen.route
        ) {

            composable(route = NaveScreen.HomeScreen.route) {
                HomeScreen(navController = navController)
            }

            composable(route = NaveScreen.AddNoteScreen.route) {
                AddNoteScreen(navController = navController, noteId = -1L)
            }
            composable(
                route = "${NaveScreen.AddNoteScreen.route}/{noteId}",
                arguments = listOf(
                    navArgument("noteId") {
                        type = NavType.LongType
                        defaultValue = -1L
                    }
                )
            ) { backStackEntry ->
                val noteId = backStackEntry.arguments?.getLong("noteId") ?: -1L
                AddNoteScreen(navController = navController, noteId = noteId)
            }


            composable(route = NaveScreen.AddCheckListScreen.route) {
                AddCheckListScreen(navController = navController, checklistId = -1L)
            }

            composable(
                route = "${NaveScreen.AddCheckListScreen.route}/{checkListId}",
                arguments = listOf(
                    navArgument("checkListId") {
                        type = NavType.LongType
                        defaultValue = -1L
                    }
                )
            ) { backStackEntry ->
                val checkListId = backStackEntry.arguments?.getLong("checkListId") ?: -1L
                AddCheckListScreen(navController = navController, checklistId = checkListId)
            }

            composable(route = NaveScreen.FavoriteScreen.route) {
                FavoriteScreen(navController = navController)
            }

            composable(route = NaveScreen.TrashScreen.route) {
                TrashScreen(navController)
            }

            composable(route = NaveScreen.SearchScreen.route) {
                SearchScreen(navController)
            }


        }
    }
}