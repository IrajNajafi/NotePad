package com.irajnajafi1988gmail.notepad.data.local.db.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.irajnajafi1988gmail.notepad.data.model.CheckItem

class Converters {

    private val gson = Gson()

    @TypeConverter
    fun fromCheckItemList(value: List<CheckItem>?): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toCheckItemList(value: String): List<CheckItem> {
        val listType = object : TypeToken<List<CheckItem>>() {}.type
        return gson.fromJson(value, listType)
    }
}
