package com.tuantuan.journal.domain.usecase.diary

import com.tuantuan.journal.domain.repository.DiaryRepository
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val repository: DiaryRepository
) {
    suspend operator fun invoke(id: String, isFavorite: Boolean) =
        repository.toggleFavorite(id, isFavorite)
}