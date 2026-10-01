package com.tuantuan.journal.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.Icon
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
    modifier: Modifier = Modifier
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
                PhotoItem(
                    mediaItem = photos[0],
                    onClick = { onPhotoClick(0) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            2 -> {
                // 两张：并排
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    photos.forEachIndexed { index, photo ->
                        PhotoItem(
                            mediaItem = photo,
                            onClick = { onPhotoClick(index) },
                            modifier = Modifier.weight(1f)
                        )
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
                            PhotoItem(
                                mediaItem = photo,
                                onClick = { onPhotoClick(index) },
                                modifier = Modifier.weight(1f)
                            )
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