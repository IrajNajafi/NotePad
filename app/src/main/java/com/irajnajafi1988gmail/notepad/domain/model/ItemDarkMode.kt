package com.irajnajafi1988gmail.notepad.domain.model

import com.irajnajafi1988gmail.notepad.R

enum class ItemDarkMode(
    val code: String,
    val icon: Int,
    val text: Int,
) {
    LIGHT(code = "light", icon = R.drawable.light, text = R.string.light),
    DARK(code = "dark", icon = R.drawable.dark, text = R.string.dark),
    SYSTEM(code = "system", icon = R.drawable.system, text = R.string.system);

    companion object {
        fun fromCode(code: String): ItemDarkMode {
            return ItemDarkMode.entries.find { it.code == code } ?: SYSTEM
        }
    }
}
