package com.tuantuan.journal.ui.screen.diary

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tuantuan.journal.domain.model.DiaryEntry
import com.tuantuan.journal.ui.component.TtEmptyState
import com.tuantuan.journal.ui.component.TtLoadingIndicator
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiaryListScreen(
    childId: String,
    onDiaryClick: (String) -> Unit,
    onAddDiaryClick: () -> Unit,
    onSearchClick: () -> Unit = {},
    onBackClick: () -> Unit,
    viewModel: DiaryViewModel = hiltViewModel()
) {
    LaunchedEffect(childId) { viewModel.loadEntries(childId) }
    val state by viewModel.listState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("日记列表") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    IconButton(onClick = onSearchClick) {
                        Icon(Icons.Default.Search, contentDescription = "搜索")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddDiaryClick) {
                Icon(Icons.Default.Add, contentDescription = "写日记")
            }
        }
    ) { padding ->
        if (state.isLoading) {
            TtLoadingIndicator(
                modifier = Modifier.fillMaxSize().padding(padding)
            )
        } else if (state.entries.isEmpty()) {
            TtEmptyState(
                title = "还没有日记",
                description = "点击右下角 + 开始写第一篇",
                icon = Icons.Outlined.MenuBook,
                actionLabel = "写日记",
                onAction = onAddDiaryClick,
                modifier = Modifier.fillMaxSize().padding(padding)
            )
        } else {
            LazyColumn(
                Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.entries, key = { it.id }) { entry ->
                    DiaryCard(
                        entry = entry,
                        onClick = { onDiaryClick(entry.id) },
                        onToggleFavorite = { viewModel.toggleFavorite(entry.id, !entry.isFavorite) }
                    )
                }
            }
        }
    }
}

@Composable
private fun DiaryCard(
    entry: DiaryEntry,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日")
    val date = entry.eventDateTime.atZone(ZoneId.systemDefault()).format(dateFormatter)

    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(Modifier.padding(12.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    entry.title?.let {
                        Text(it, style = MaterialTheme.typography.titleSmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                    Text(date, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                IconButton(onClick = onToggleFavorite) {
                    Icon(
                        if (entry.isFavorite) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "收藏",
                        tint = if (entry.isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(Modifier.height(4.dp))
            Text(
                entry.content,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            entry.mood?.let { mood ->
                Spacer(Modifier.height(4.dp))
                Text(mood.name, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}