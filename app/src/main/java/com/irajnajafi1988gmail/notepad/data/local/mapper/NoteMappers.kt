package com.irajnajafi1988gmail.notepad.data.local.mapper

import com.irajnajafi1988gmail.notepad.data.local.db.entities.NoteEntity
import com.irajnajafi1988gmail.notepad.domain.model.Note
import kotlin.String

fun NoteEntity.toDomain(): Note {
    return Note(
        id = id,
        title = title,
        content = content,
        isTrashed = isTrashed,
        isFavorite = isFavorite,
        timestamp = timestamp,

        )
}

fun Note.toEntity(): NoteEntity {
    return NoteEntity(
        id = id,
        title = title,
        content = content,
        isTrashed = isTrashed,
        isFavorite = isFavorite,
        timestamp = timestamp,

    )
}
