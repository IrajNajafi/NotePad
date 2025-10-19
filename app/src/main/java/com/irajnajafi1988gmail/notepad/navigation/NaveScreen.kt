package com.irajnajafi1988gmail.notepad.navigation

sealed class NaveScreen(val route: String) {
    object HomeScreen : NaveScreen("home-screen")
    object AddNoteScreen : NaveScreen("addNote_screen")
    object AddCheckListScreen : NaveScreen("addCheckList_screen")
    object FavoriteScreen : NaveScreen("favorite_screen")

    object TrashScreen : NaveScreen("trash_screen")
    object SearchScreen: NaveScreen("search_screen")
}