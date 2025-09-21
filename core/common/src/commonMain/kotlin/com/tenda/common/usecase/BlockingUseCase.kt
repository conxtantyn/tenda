package com.tenda.common.usecase

interface BlockingUseCase<T> : Usecase {
    operator fun invoke(): T
}
