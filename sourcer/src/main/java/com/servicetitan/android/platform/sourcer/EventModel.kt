package com.servicetitan.android.platform.sourcer

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Event(
    @PrimaryKey @ColumnInfo(name = "eventId") var eventId: String = "",
    @ColumnInfo(name = "eventType") var eventType: String = "",
    @ColumnInfo(name = "createdAt") var createdAt: Date? = null,
    @Embedded var data: Any? = null
)