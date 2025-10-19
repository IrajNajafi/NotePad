package com.irajnajafi1988gmail.notepad.domain.model

enum class CalendarType (val code: String){
    GREGORIAN(code = "gregorian"),
    JALALI(code = "jalali"),
    HIJRI(code = "hijri");

    companion object {
        fun fromCode(code: String): CalendarType {
            return entries.find { it.code == code } ?: GREGORIAN
        }
    }


}