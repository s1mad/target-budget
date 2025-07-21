package com.simad.targetbudget.data.sources.db

import androidx.room.TypeConverter
import com.simad.targetbudget.domain.model.Accessibility
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)
class RoomConverters {

    @TypeConverter
    fun fromAccessibilityLevel(level: Accessibility): String = level.name

    @TypeConverter
    fun toAccessibilityLevel(name: String): Accessibility = Accessibility.valueOf(name)
}
