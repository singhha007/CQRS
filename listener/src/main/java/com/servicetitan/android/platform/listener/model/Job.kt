package com.servicetitan.android.platform.listener.model

import com.servicetitan.android.platform.listener.helper.EMPTY

data class Job(
    var id: String = EMPTY,
    var name: String = EMPTY,
    var location: String = EMPTY,
    var technician: String = EMPTY
)
