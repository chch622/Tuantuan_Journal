package com.tuantuan.journal.ui.component

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.tuantuan.journal.R

/**
 * 极简音频播放器 — MEDIA_UX.md §3.1
 *
 * 设计规范：
 * ▶ ━━━━━━━━━━━━ 00:18
 *
 * - 播放/暂停按钮：IconButton, Primary 色
 * - 进度条：Slider, Primary 色
 * - 时间标签：LabelSmall, OnSurfaceVariant
 * - 颜色使用主题色
 *
 * 使用 Media3 ExoPlayer 进行音频播放，内联展示（非全屏）。
 * 生命周期管理：remember 创建 + DisposableEffect release。
 */
@Composable
fun TtAudioPlayer(
    audioPath: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val exoPlayer = remember { createExoPlayer(context) }

    var isPlaying by remember { mutableStateOf(false) }
    var currentPositionMs by remember { mutableLongStateOf(0L) }
    var durationMs by remember { mutableLongStateOf(0L) }
    var sliderPosition by remember { mutableFloatStateOf(0f) }
    var isSeeking by remember { mutableStateOf(false) }

    // 设置音频源
    LaunchedEffect(audioPath) {
        val mediaItem = MediaItem.fromUri("file://$audioPath")
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
    }

    // 监听播放状态
    DisposableEffect(exoPlayer) {
        val listener = object : Player.Listener {
            override fun onIsPlayingChanged(playing: Boolean) {
                isPlaying = playing
            }
        }
        exoPlayer.addListener(listener)
        onDispose { exoPlayer.removeListener(listener) }
    }

    // 进度更新协程
    LaunchedEffect(exoPlayer) {
        while (true) {
            if (exoPlayer.duration > 0) {
                durationMs = exoPlayer.duration
                if (!isSeeking) {
                    currentPositionMs = exoPlayer.currentPosition
                    sliderPosition = currentPositionMs.toFloat() / durationMs.toFloat()
                }
            }
            kotlinx.coroutines.delay(200)
        }
    }

    // 释放播放器
    DisposableEffect(Unit) {
        onDispose { exoPlayer.release() }
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // 播放/暂停按钮 — MEDIA_UX.md §3.1
        IconButton(
            onClick = {
                if (isPlaying) {
                    exoPlayer.pause()
                } else {
                    exoPlayer.play()
                }
            }
        ) {
            Icon(
                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = if (isPlaying) stringResource(R.string.media_audio_pause) else stringResource(R.string.media_audio_play),
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(Modifier.width(4.dp))

        // 进度条 — MEDIA_UX.md §3.1
        Slider(
            value = sliderPosition,
            onValueChange = { new ->
                isSeeking = true
                sliderPosition = new
            },
            onValueChangeFinished = {
                isSeeking = false
                if (durationMs > 0) {
                    exoPlayer.seekTo((sliderPosition * durationMs).toLong())
                }
            },
            modifier = Modifier.weight(1f),
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.primary,
                activeTrackColor = MaterialTheme.colorScheme.primary
            )
        )

        Spacer(Modifier.width(4.dp))

        // 时间标签 — MEDIA_UX.md §3.1
        Text(
            text = formatAudioDuration(currentPositionMs),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

private fun createExoPlayer(context: Context): ExoPlayer {
    return ExoPlayer.Builder(context).build()
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