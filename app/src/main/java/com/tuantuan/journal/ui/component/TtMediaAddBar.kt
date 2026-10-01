package com.tuantuan.journal.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.VideoLibrary
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tuantuan.journal.R

/**
 * 媒体添加栏 — MEDIA_UX.md §4
 *
 * 提供直观的媒体添加入口：
 * [📷 照片]  [🎬 视频]  [🎙 语音]
 *
 * 不藏到多级菜单里，直接在日记表单中显示。
 * Phase 1 仅实现照片选择；视频和语音按钮为 Phase 3 占位。
 */
@Composable
fun TtMediaAddBar(
    onPhotoClick: () -> Unit,
    onVideoClick: () -> Unit = {},
    onAudioClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 照片按钮 — Phase 1 实现
        FilledTonalButton(
            onClick = onPhotoClick
        ) {
            Icon(
                imageVector = Icons.Outlined.CameraAlt,
                contentDescription = stringResource(R.string.media_add_photo),
                modifier = Modifier.size(18.dp)
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text = stringResource(R.string.media_photo),
                style = MaterialTheme.typography.labelMedium
            )
        }

        // 视频按钮 — Phase 3 占位
        FilledTonalButton(
            onClick = onVideoClick,
            enabled = false
        ) {
            Icon(
                imageVector = Icons.Outlined.VideoLibrary,
                contentDescription = stringResource(R.string.media_add_video),
                modifier = Modifier.size(18.dp)
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text = stringResource(R.string.media_video),
                style = MaterialTheme.typography.labelMedium
            )
        }

        // 语音按钮 — Phase 3 占位
        FilledTonalButton(
            onClick = onAudioClick,
            enabled = false
        ) {
            Icon(
                imageVector = Icons.Default.Mic,
                contentDescription = stringResource(R.string.media_add_audio),
                modifier = Modifier.size(18.dp)
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text = stringResource(R.string.media_audio),
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}