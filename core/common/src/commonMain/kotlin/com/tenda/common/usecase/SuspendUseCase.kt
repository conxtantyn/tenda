package com.tenda.common.usecase

interface SuspendUseCase<T> : Usecase {
    suspend operator fun invoke(): T
}
