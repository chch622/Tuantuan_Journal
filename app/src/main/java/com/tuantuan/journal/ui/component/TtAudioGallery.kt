package com.tuantuan.journal.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AudioFile
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tuantuan.journal.R
import com.tuantuan.journal.domain.model.MediaItem

/**
 * 音频展示区 — MEDIA_UX.md §3.3
 *
 * 音频卡片格式：
 * [音频图标]  文件名
 *             00:18 · 日期
 *             ▶ ━━━━━━━━━━━━ 00:18
 *
 * 遵循 MEDIA_UX.md §5：音频类型通过 mic 图标 + 时长标签标识。
 * 遵循 MEDIA_STORAGE.md §5.3：音频不生成缩略图，thumbnailPath 为 null。
 */
@Composable
fun TtAudioGallery(
    audios: List<MediaItem>,
    modifier: Modifier = Modifier
) {
    if (audios.isEmpty()) return

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // 音频数量标识 — MEDIA_UX.md §5
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.AudioFile,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(18.dp)
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text = stringResource(R.string.media_audio_count, audios.size),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // 音频卡片列表
        audios.forEach { audioItem ->
            AudioCard(audioItem = audioItem)
        }
    }
}

/**
 * 单个音频卡片 — MEDIA_UX.md §3.3
 */
@Composable
private fun AudioCard(
    audioItem: MediaItem,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // 第一行：音频图标 + 文件名
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.AudioFile,
                    contentDescription = stringResource(R.string.media_audio),
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = audioItem.fileName,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1
                )
            }

            Spacer(Modifier.height(4.dp))

            // 第二行：时长 + 日期
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                audioItem.duration?.let { durationMs ->
                    Text(
                        text = formatAudioDuration(durationMs),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "·",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.width(8.dp))
                }
                Text(
                    text = formatAudioDate(audioItem.createdAt.toEpochMilli()),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(Modifier.height(8.dp))

            // 第三行：极简播放器 — MEDIA_UX.md §3.1
            TtAudioPlayer(
                audioPath = audioItem.filePath
            )
        }
    }
}

/**
 * 格式化音频时长：ms → mm:ss 或 h:mm:ss
 */
private fun formatAudioDuration(ms: Long): String {
    val totalSeconds = ms / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    return if (hours > 0) {
        "%d:%02d:%02d".format(hours, minutes, seconds)
    } else {
        "%02d:%02d".format(minutes, seconds)
    }
}

/**
 * 格式化音频日期：epoch millis → yyyy.MM.dd
 */
private fun formatAudioDate(epochMillis: Long): String {
    val date = java.time.Instant.ofEpochMilli(epochMillis)
        .atZone(java.time.ZoneId.systemDefault())
        .toLocalDate()
    return java.time.format.DateTimeFormatter.ofPattern("yyyy.MM.dd").format(date)
}