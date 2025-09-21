package com.tenda.common.usecase

interface SuspendWithArgsUseCase<A, T> : Usecase {
    suspend operator fun invoke(args: A): T
}
