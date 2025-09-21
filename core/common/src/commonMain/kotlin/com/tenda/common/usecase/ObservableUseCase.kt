package com.tenda.common.usecase

import kotlinx.coroutines.flow.Flow

interface ObservableUseCase<T> : Usecase {
    operator fun invoke(): Flow<T>
}
