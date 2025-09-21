package com.tenda.common.exception

class DomainException(
    val status: String,
    reason: String?,
) : Throwable(reason)
