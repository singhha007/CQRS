package com.servicetitan.android.platform.writer.model

import com.servicetitan.android.platform.writer.helper.EMPTY

data class Job(
    var id: String = EMPTY,
    var name: String = EMPTY,
    var location: String = EMPTY,
    var technician: String = EMPTY
)
