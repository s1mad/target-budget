package com.simad.targetbudget.data.helpers

import kotlin.time.Clock.System.now
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalTime::class)
internal fun nowInIso(): String = now().toString()

@OptIn(ExperimentalUuidApi::class)
fun generateUuid(): String = Uuid.random().toString()