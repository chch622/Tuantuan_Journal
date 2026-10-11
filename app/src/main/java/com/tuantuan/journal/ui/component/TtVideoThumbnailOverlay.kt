package com.tuantuan.journal.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tuantuan.journal.R

/**
 * 视频缩略图叠加层 — MEDIA_UX.md §2.1
 *
 * 叠加在视频缩略图上方，显示：
 * - 播放图标（居中，Primary 色，半透明背景）
 * - 时长标签（右下角，LabelSmall，黑色半透明背景）
 *
 * 不要使用大面积控制元素覆盖照片。
 *
 * @param durationMs 视频时长（毫秒），null 时隐藏时长标签
 */
@Composable
fun TtVideoThumbnailOverlay(
    durationMs: Long?,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        // 播放图标 — 居中，半透明背景
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.4f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = stringResource(R.string.media_video_play),
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }

        // 时长标签 — 右下角，MEDIA_UX.md §2.1
        if (durationMs != null && durationMs > 0) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(4.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.Black.copy(alpha = 0.6f))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = formatVideoDuration(durationMs),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White
                )
            }
        }
    }
}

/**
 * 格式化视频时长为 mm:ss 或 h:mm:ss
 */
private fun formatVideoDuration(ms: Long): String {
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