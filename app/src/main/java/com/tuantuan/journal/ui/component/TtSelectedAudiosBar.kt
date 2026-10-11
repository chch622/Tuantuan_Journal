package com.tuantuan.journal.ui.component

import android.net.Uri
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tuantuan.journal.R

/**
 * 已选音频预览栏 — MEDIA_UX.md §4.2 / §3.3
 *
 * 显示 SAF 已选的音频文件列表，每个可移除。
 * 音频无缩略图，使用 AudioFile 图标 + 文件名展示。
 * 遵循 MEDIA_STORAGE.md §5.3：音频不生成缩略图，thumbnailPath 为 null。
 */
@Composable
fun TtSelectedAudiosBar(
    audioUris: List<Uri>,
    onRemoveAudio: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    if (audioUris.isEmpty()) return

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        audioUris.forEachIndexed { index, uri ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 音频图标 — MEDIA_UX.md §5 音频类型标识
                    Icon(
                        imageVector = Icons.Default.AudioFile,
                        contentDescription = stringResource(R.string.media_audio),
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(Modifier.width(12.dp))

                    // 文件名 — 从 URI 提取
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = uri.lastPathSegment?.substringAfterLast("/")
                                ?: stringResource(R.string.media_audio_index, index + 1),
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 1
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = stringResource(R.string.media_audio_index, index + 1),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // 移除按钮
                    IconButton(
                        onClick = { onRemoveAudio(index) },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(R.string.media_remove_audio),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}