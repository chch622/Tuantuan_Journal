package com.tuantuan.journal.ui.component

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.tuantuan.journal.R
import com.tuantuan.journal.domain.model.MediaItem
import java.io.File

/**
 * 照片展示区 — MEDIA_UX.md §1.1
 *
 * 在日记详情页展示照片，遵循「照片应该成为内容中心」原则。
 * 不缩成非常小的缩略图。
 *
 * @param photos 仅包含 PHOTO 类型的 MediaItem 列表
 * @param onPhotoClick 点击照片回调，参数为索引
 */
@Composable
fun TtPhotoGallery(
    photos: List<MediaItem>,
    onPhotoClick: (index: Int) -> Unit,
    modifier: Modifier = Modifier,
    isReorderMode: Boolean = false,
    onMovePhoto: ((fromIndex: Int, toIndex: Int) -> Unit)? = null
) {
    if (photos.isEmpty()) return

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        // 照片数量标识 — MEDIA_UX.md §5
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Photo,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = stringResource(R.string.media_photo_count, photos.size),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 4.dp)
            )
        }

        // 照片网格 — 根据数量自适应排版
        when (photos.size) {
            1 -> {
                // 单张：大图展示
                Row(modifier = Modifier.fillMaxWidth()) {
                    PhotoItem(
                        mediaItem = photos[0],
                        onClick = { onPhotoClick(0) },
                        modifier = Modifier.weight(1f)
                    )
                    if (isReorderMode) {
                        // 单张无需排序按钮
                    }
                }
            }
            2 -> {
                // 两张：并排
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    photos.forEachIndexed { index, photo ->
                        Box(modifier = Modifier.weight(1f)) {
                            PhotoItem(
                                mediaItem = photo,
                                onClick = { onPhotoClick(index) },
                                modifier = Modifier.fillMaxWidth()
                            )
                            if (isReorderMode && onMovePhoto != null) {
                                ReorderButtons(
                                    index = index,
                                    totalCount = photos.size,
                                    onMove = onMovePhoto,
                                    modifier = Modifier.align(Alignment.BottomEnd)
                                )
                            }
                        }
                    }
                }
            }
            else -> {
                // 三张及以上：2列网格
                photos.chunked(2).forEachIndexed { rowIndex, rowPhotos ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        rowPhotos.forEachIndexed { colIndex, photo ->
                            val index = rowIndex * 2 + colIndex
                            Box(modifier = Modifier.weight(1f)) {
                                PhotoItem(
                                    mediaItem = photo,
                                    onClick = { onPhotoClick(index) },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                if (isReorderMode && onMovePhoto != null) {
                                    ReorderButtons(
                                        index = index,
                                        totalCount = photos.size,
                                        onMove = onMovePhoto,
                                        modifier = Modifier.align(Alignment.BottomEnd)
                                    )
                                }
                            }
                        }
                        // 如果最后一行只有1张，填充空白
                        if (rowPhotos.size == 1) {
                            Modifier.weight(1f)
                        }
                    }
                }
            }
        }
    }
}

/**
 * 视频展示区 — MEDIA_UX.md §2.1
 *
 * 在日记详情页展示视频缩略图，带播放图标和时长标签。
 *
 * @param videos 仅包含 VIDEO 类型的 MediaItem 列表
 * @param onVideoClick 点击视频回调，参数为索引
 */
@Composable
fun TtVideoGallery(
    videos: List<MediaItem>,
    onVideoClick: (index: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    if (videos.isEmpty()) return

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        // 视频数量标识 — MEDIA_UX.md §5
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Videocam,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = stringResource(R.string.media_video_count, videos.size),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 4.dp)
            )
        }

        // 视频缩略图网格 — 与照片相同的自适应排版
        when (videos.size) {
            1 -> {
                VideoItem(
                    mediaItem = videos[0],
                    onClick = { onVideoClick(0) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            2 -> {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    videos.forEachIndexed { index, video ->
                        VideoItem(
                            mediaItem = video,
                            onClick = { onVideoClick(index) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
            else -> {
                videos.chunked(2).forEachIndexed { rowIndex, rowVideos ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        rowVideos.forEachIndexed { colIndex, video ->
                            val index = rowIndex * 2 + colIndex
                            VideoItem(
                                mediaItem = video,
                                onClick = { onVideoClick(index) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        if (rowVideos.size == 1) {
                            Modifier.weight(1f)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PhotoItem(
    mediaItem: MediaItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = File(mediaItem.filePath),
        contentDescription = stringResource(R.string.media_photo),
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick),
        contentScale = ContentScale.Crop
    )
}

/**
 * 视频缩略图项 — MEDIA_UX.md §2.1
 *
 * 显示视频缩略图 + 播放图标叠加层 + 时长标签。
 * 缩略图优先使用 thumbnailPath，回退到 filePath 首帧。
 */
@Composable
private fun VideoItem(
    mediaItem: MediaItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
    ) {
        // 缩略图 — 优先使用 thumbnailPath，回退到 filePath
        val thumbnailModel = mediaItem.thumbnailPath?.let { File(it) } ?: File(mediaItem.filePath)
        AsyncImage(
            model = thumbnailModel,
            contentDescription = stringResource(R.string.media_video),
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // 播放图标 + 时长标签叠加层
        TtVideoThumbnailOverlay(
            durationMs = mediaItem.duration,
            modifier = Modifier.fillMaxSize()
        )
    }
}

/**
 * 照片排序按钮 — Phase 3 P1
 *
 * 在每张照片上叠加上/下箭头按钮，用于调整照片顺序。
 */
@Composable
private fun ReorderButtons(
    index: Int,
    totalCount: Int,
    onMove: (fromIndex: Int, toIndex: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(2.dp),
        horizontalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        if (index > 0) {
            IconButton(
                onClick = { onMove(index, index - 1) },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowUpward,
                    contentDescription = stringResource(R.string.media_move_up),
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
        if (index < totalCount - 1) {
            IconButton(
                onClick = { onMove(index, index + 1) },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowDownward,
                    contentDescription = stringResource(R.string.media_move_down),
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}