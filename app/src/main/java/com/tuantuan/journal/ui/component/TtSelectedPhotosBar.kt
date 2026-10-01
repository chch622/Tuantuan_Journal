package com.tuantuan.journal.ui.component

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.tuantuan.journal.R

/**
 * 已选照片预览栏 — MEDIA_UX.md §4.2
 *
 * 显示 Photo Picker 已选的照片缩略图，每张可移除。
 * 缩略图 80dp 圆角，使用 Coil AsyncImage 加载 content:// URI。
 */
@Composable
fun TtSelectedPhotosBar(
    photoUris: List<Uri>,
    onRemovePhoto: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    if (photoUris.isEmpty()) return

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        photoUris.forEachIndexed { index, uri ->
            Box(
                modifier = Modifier.size(80.dp),
                contentAlignment = Alignment.TopEnd
            ) {
                AsyncImage(
                    model = uri,
                    contentDescription = stringResource(R.string.media_selected_photo, index + 1),
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                IconButton(
                    onClick = { onRemovePhoto(index) },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.media_remove_photo),
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}