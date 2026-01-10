package com.example.sipora.domain.usecase

import com.example.sipora.core.result.ResultState
import com.example.sipora.data.remote.response.UserResponse
import com.example.sipora.domain.repository.PeriodePendaftaranRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMeUseCase @Inject constructor(
    private val repository: PeriodePendaftaranRepository
) {
    operator fun invoke(): Flow<ResultState<UserResponse>> {
        return repository.getMe()
    }
}
