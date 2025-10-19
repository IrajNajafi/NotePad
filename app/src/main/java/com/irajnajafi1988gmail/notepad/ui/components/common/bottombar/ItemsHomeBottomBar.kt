package com.irajnajafi1988gmail.notepad.ui.components.common.bottombar

import com.irajnajafi1988gmail.notepad.R

enum class ItemsHomeBottomBar(val icon: Int, val description: Int) {
    SEARCH(icon = R.drawable.search, description = R.string.search),
    TRASH(icon = R.drawable.delete_recycle_trash_icon, description = R.string.trash),
    ADD(icon = R.drawable.add, description = R.string.add),
    FAVORITE(icon = R.drawable.favorite_bottombar, description = R.string.favorites),
    DARKMODE(icon = R.drawable.ic_darckmode, description = R.string.darkMode);



    companion object {
        val rightItems = listOf( SEARCH ,DARKMODE)
        val leftItems = listOf(TRASH , FAVORITE)
        val middleItem = listOf(ADD)
    }


}