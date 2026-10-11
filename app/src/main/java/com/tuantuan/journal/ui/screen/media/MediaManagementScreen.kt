package com.tuantuan.journal.ui.screen.media

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AudioFile
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.tuantuan.journal.R
import com.tuantuan.journal.domain.model.MediaItem
import com.tuantuan.journal.domain.model.MediaType
import com.tuantuan.journal.ui.component.TtLoadingIndicator
import java.io.File

/**
 * 媒体管理界面 — Phase 3 P1
 *
 * 显示所有媒体项，支持按类型筛选和删除。
 * 遵循 ROADMAP：查看和管理所有媒体。
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaManagementScreen(
    onBackClick: () -> Unit,
    viewModel: MediaManagementViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    var showDeleteDialog by remember { mutableStateOf<MediaItem?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.media_management)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // 类型筛选 — MEDIA_UX.md §5
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = state.typeFilter == null,
                    onClick = { viewModel.setTypeFilter(null) },
                    label = { Text(stringResource(R.string.media_filter_all)) }
                )
                FilterChip(
                    selected = state.typeFilter == MediaType.PHOTO,
                    onClick = { viewModel.setTypeFilter(MediaType.PHOTO) },
                    label = { Text(stringResource(R.string.media_filter_photos)) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Photo,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                )
                FilterChip(
                    selected = state.typeFilter == MediaType.VIDEO,
                    onClick = { viewModel.setTypeFilter(MediaType.VIDEO) },
                    label = { Text(stringResource(R.string.media_filter_videos)) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Videocam,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                )
                FilterChip(
                    selected = state.typeFilter == MediaType.AUDIO,
                    onClick = { viewModel.setTypeFilter(MediaType.AUDIO) },
                    label = { Text(stringResource(R.string.media_filter_audios)) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.AudioFile,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                )
            }

            // 媒体数量
            Text(
                text = stringResource(R.string.media_count_label, state.mediaItems.size),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )

            if (state.isLoading) {
                TtLoadingIndicator(modifier = Modifier.fillMaxSize())
            } else if (state.mediaItems.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.media_empty),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                // 媒体网格
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(2.dp)
                ) {
                    items(state.mediaItems, key = { it.id }) { item ->
                        MediaGridItem(
                            mediaItem = item,
                            onDeleteClick = { showDeleteDialog = item }
                        )
                    }
                }
            }
        }
    }

    // 删除确认对话框
    showDeleteDialog?.let { item ->
        AlertDialog(
            onDismissRequest = { showDeleteDialog = null },
            title = { Text(stringResource(R.string.media_delete_title)) },
            text = { Text(stringResource(R.string.media_delete_confirm, item.fileName)) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteMedia(item.id)
                    showDeleteDialog = null
                }) {
                    Text(stringResource(R.string.delete), color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = null }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}

@Composable
private fun MediaGridItem(
    mediaItem: MediaItem,
    onDeleteClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(4.dp))
    ) {
        when (mediaItem.mediaType) {
            MediaType.PHOTO -> {
                AsyncImage(
                    model = File(mediaItem.filePath),
                    contentDescription = stringResource(R.string.media_photo),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            MediaType.VIDEO -> {
                val thumbnailModel = mediaItem.thumbnailPath?.let { File(it) } ?: File(mediaItem.filePath)
                AsyncImage(
                    model = thumbnailModel,
                    contentDescription = stringResource(R.string.media_video),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Icon(
                    Icons.Default.Videocam,
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.Center),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
            MediaType.AUDIO -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable(onClick = onDeleteClick),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.AudioFile,
                        contentDescription = stringResource(R.string.media_audio),
                        modifier = Modifier.size(32.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        // 删除按钮（照片和视频）
        if (mediaItem.mediaType != MediaType.AUDIO) {
            IconButton(
                onClick = onDeleteClick,
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.TopEnd)
                    .padding(2.dp)
            ) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = stringResource(R.string.delete),
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}