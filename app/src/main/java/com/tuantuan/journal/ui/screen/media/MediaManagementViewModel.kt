package com.tuantuan.journal.ui.screen.media

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuantuan.journal.domain.model.MediaItem
import com.tuantuan.journal.domain.model.MediaType
import com.tuantuan.journal.domain.usecase.media.DeleteMediaUseCase
import com.tuantuan.journal.domain.usecase.media.GetAllMediaUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MediaManagementUiState(
    val mediaItems: List<MediaItem> = emptyList(),
    val typeFilter: MediaType? = null,
    val isLoading: Boolean = true
)

@HiltViewModel
class MediaManagementViewModel @Inject constructor(
    private val getAllMediaUseCase: GetAllMediaUseCase,
    private val deleteMediaUseCase: DeleteMediaUseCase
) : ViewModel() {

    private val _typeFilter = MutableStateFlow<MediaType?>(null)

    val uiState: StateFlow<MediaManagementUiState> = combine(
        getAllMediaUseCase(),
        _typeFilter
    ) { mediaItems, filter ->
        val filtered = if (filter != null) {
            mediaItems.filter { it.mediaType == filter }
        } else {
            mediaItems
        }
        MediaManagementUiState(
            mediaItems = filtered,
            typeFilter = filter,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MediaManagementUiState()
    )

    fun setTypeFilter(filter: MediaType?) {
        _typeFilter.value = filter
    }

    fun deleteMedia(mediaId: String) {
        viewModelScope.launch {
            deleteMediaUseCase(mediaId)
        }
    }
}