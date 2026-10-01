package com.tuantuan.journal.ui.screen.diary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import com.tuantuan.journal.ui.component.DeleteConfirmDialog
import com.tuantuan.journal.ui.component.TtErrorState
import com.tuantuan.journal.ui.component.TtLoadingIndicator
import com.tuantuan.journal.ui.component.TtPhotoGallery
import com.tuantuan.journal.ui.component.TtPhotoViewer

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tuantuan.journal.R
import com.tuantuan.journal.domain.model.DiaryEntry
import com.tuantuan.journal.domain.model.MediaType
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiaryDetailScreen(
    entryId: String,
    onEditClick: () -> Unit,
    onBackClick: () -> Unit,
    onDeleteClick: (() -> Unit)? = null,
    viewModel: DiaryViewModel = hiltViewModel()
) {
    LaunchedEffect(entryId) { viewModel.loadEntry(entryId) }
    val state by viewModel.detailState.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }

    // 全屏照片查看状态
    var showPhotoViewer by remember { mutableStateOf(false) }
    var photoViewerIndex by remember { mutableStateOf(0) }

    // 过滤出照片类型的媒体
    val photoItems = remember(state.entry) {
        state.entry?.mediaItems?.filter { it.mediaType == MediaType.PHOTO } ?: emptyList()
    }
    val photoPaths = remember(photoItems) {
        photoItems.map { it.filePath }
    }

    // 全屏照片查看器
    if (showPhotoViewer && photoPaths.isNotEmpty()) {
        TtPhotoViewer(
            photos = photoPaths,
            initialIndex = photoViewerIndex,
            onBackClick = { showPhotoViewer = false }
        )
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.entry?.title ?: stringResource(R.string.diary_detail)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                },
                actions = {
                    state.entry?.let { entry ->
                        IconButton(onClick = { viewModel.toggleFavorite(entry.id, !entry.isFavorite) }) {
                            Icon(
                                if (entry.isFavorite) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = stringResource(R.string.diary_favorite),
                                tint = if (entry.isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    IconButton(onClick = onEditClick) {
                        Icon(Icons.Default.Edit, contentDescription = stringResource(R.string.edit))
                    }
                    onDeleteClick?.let {
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.delete))
                        }
                    }
                }
            )
        }
    ) { padding ->
        if (state.isLoading) {
            TtLoadingIndicator(
                modifier = Modifier.fillMaxSize().padding(padding)
            )
        } else if (state.entry == null) {
            TtErrorState(
                message = stringResource(R.string.diary_not_found),
                modifier = Modifier.fillMaxSize().padding(padding)
            )
        } else {
            val entry = state.entry!!
            val dateFormatter = DateTimeFormatter.ofPattern(stringResource(R.string.date_format_full_time))
            val dateTime = entry.eventDateTime.atZone(ZoneId.systemDefault()).format(dateFormatter)

            Column(
                Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // 日期和心情
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(dateTime, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    entry.mood?.let {
                        Text(
                            stringResource(R.string.diary_mood_label, it.name),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                entry.weather?.let {
                    Text(
                        stringResource(R.string.diary_weather_label, it.name),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                entry.location?.let {
                    Text(
                        stringResource(R.string.diary_location_label, it),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // 照片展示 — MEDIA_UX.md §1.1
                if (photoItems.isNotEmpty()) {
                    TtPhotoGallery(
                        photos = photoItems,
                        onPhotoClick = { index ->
                            photoViewerIndex = index
                            showPhotoViewer = true
                        }
                    )
                }

                Spacer(Modifier.height(4.dp))

                // 日记内容
                Text(entry.content, style = MaterialTheme.typography.bodyLarge)

                // 标签
                if (entry.tags.isNotEmpty()) {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        stringResource(R.string.diary_tags),
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        entry.tags.forEach { tag ->
                            Text(
                                "#${tag.name}",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }

    // 删除确认对话框
    if (showDeleteDialog) {
        DeleteConfirmDialog(
            itemName = stringResource(R.string.diary_this_entry),
            isPermanent = false,
            onConfirm = {
                showDeleteDialog = false
                viewModel.deleteEntry(entryId)
                onDeleteClick?.invoke()
            },
            onDismiss = { showDeleteDialog = false }
        )
    }
}