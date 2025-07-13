package com.simad.targetbudget.data.sources.db

import androidx.room.TypeConverter
import com.simad.targetbudget.domain.model.AccessibilityLevel
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)
class RoomConverters {

    @TypeConverter
    fun fromAccessibilityLevel(level: AccessibilityLevel): String = level.name

    @TypeConverter
    fun toAccessibilityLevel(name: String): AccessibilityLevel = AccessibilityLevel.valueOf(name)
}
