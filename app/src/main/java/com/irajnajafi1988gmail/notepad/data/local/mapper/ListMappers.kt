package com.irajnajafi1988gmail.notepad.data.local.mapper

import com.irajnajafi1988gmail.notepad.data.local.db.entities.ListEntity
import com.irajnajafi1988gmail.notepad.domain.model.CheckList

fun ListEntity.toDomain(): CheckList {
    return CheckList(
        id = id,
        title = title,
        content = content,
        isTrashed = isTrashed,
        isFavorite = isFavorite,
        timestamp = timestamp,
        items = items,
    )
}

fun CheckList.toEntity(): ListEntity {
    return ListEntity(
        id = id,
        title = title,
        content = content,
        isTrashed = isTrashed,
        isFavorite = isFavorite,
        timestamp = timestamp,
        items = items,
    )
}