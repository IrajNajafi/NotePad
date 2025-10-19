package com.irajnajafi1988gmail.notepad.domain.model

import com.irajnajafi1988gmail.notepad.R

enum class ItemLanguage(
    val code: String,
    val textRes: Int,
    val flag: String,
    val isRtl: Boolean = false
) {
    ARABIC(
        code = "ar",
        textRes = R.string.arabic,
        flag = "\uD83C\uDDF8\uD83C\uDDE6", // 🇸🇦
        isRtl = true
    ),
    ENGLISH(
        code = "en",
        textRes = R.string.english,
        flag = "\uD83C\uDDFA\uD83C\uDDF8" // 🇺🇸
    ),
    GERMAN(
        code = "de",
        textRes = R.string.german,
        flag = "\uD83C\uDDE9\uD83C\uDDEA" // 🇩🇪
    ),
    JAPANESE(
        code = "ja",
        textRes = R.string.japanese,
        flag = "\uD83C\uDDEF\uD83C\uDDF5" // 🇯🇵
    ),
    PERSIAN(
        code = "fa",
        textRes = R.string.persian,
        flag = "\uD83C\uDDEE\uD83C\uDDF7", // 🇮🇷
        isRtl = true
    ),
    SPANISH(
        code = "es",
        textRes = R.string.spanish,
        flag = "\uD83C\uDDEA\uD83C\uDDF8" // 🇪🇸
    ),
    TURKISH(
        code = "tr",
        textRes = R.string.turkish,
        flag = "\uD83C\uDDF9\uD83C\uDDF7" // 🇹🇷
    );

    companion object {
        fun fromCode(code: String): ItemLanguage {
            return entries.find { it.code == code } ?: ENGLISH
        }
    }
}
