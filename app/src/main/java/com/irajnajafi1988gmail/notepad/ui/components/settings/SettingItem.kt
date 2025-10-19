package com.irajnajafi1988gmail.notepad.ui.components.settings

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.irajnajafi1988gmail.notepad.R


enum class SettingItem(
    @DrawableRes val iconRes: Int,
    @StringRes val titleRes: Int
) {
    DARKMODE(R.drawable.ic_darckmode, R.string.darkMode),
    LANGUAGE(R.drawable.language, R.string.language),
    INFO(R.drawable.info, R.string.info);

    companion object {
        val items = entries
    }
}