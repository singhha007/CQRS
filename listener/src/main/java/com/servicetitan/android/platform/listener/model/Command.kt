package com.servicetitan.android.platform.listener.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.servicetitan.android.platform.listener.helper.EMPTY
import com.servicetitan.android.platform.sourcer.Job
import java.util.*

@Entity
data class Command(
    @PrimaryKey @ColumnInfo(name = "commandId") var commandId: String = EMPTY,
    @ColumnInfo(name = "commandType") var commandType: String = EMPTY,
    @ColumnInfo(name = "createdAt") var createdAt: Date? = null,
    @Embedded val data: Job? = null
)