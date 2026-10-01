package com.tuantuan.journal.ui.screen.search

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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tuantuan.journal.R
import com.tuantuan.journal.domain.model.DiaryEntry
import com.tuantuan.journal.ui.component.TtEmptyState
import com.tuantuan.journal.ui.component.TtErrorState
import com.tuantuan.journal.ui.component.TtLoadingIndicator
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    childId: String,
    onEntryClick: (String) -> Unit,
    onBackClick: () -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.search_title)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding)
        ) {
            // 搜索栏
            SearchBar(
                query = state.query,
                onQueryChange = { viewModel.updateQuery(it) },
                onSearch = { viewModel.search(childId, it) },
                active = false,
                onActiveChange = {},
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text(stringResource(R.string.search_hint)) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
            ) {}

            // 搜索结果
            if (state.isSearching) {
                TtLoadingIndicator(
                    modifier = Modifier.fillMaxSize()
                )
            } else if (state.query.isNotBlank() && state.results.isEmpty()) {
                TtEmptyState(
                    title = stringResource(R.string.search_no_result_title),
                    description = stringResource(R.string.search_no_result_desc),
                    icon = Icons.Outlined.SearchOff,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.results, key = { it.id }) { entry ->
                        DiarySearchResultItem(
                            entry = entry,
                            onClick = { onEntryClick(entry.id) }
                        )
                    }
                }
            }

            // 错误提示
            state.error?.let { error ->
                TtErrorState(
                    error = error,
                    onRetry = { viewModel.search(childId, state.query) }
                )
            }
        }
    }
}

@Composable
private fun DiarySearchResultItem(
    entry: DiaryEntry,
    onClick: () -> Unit
) {
    val dateFormatter = DateTimeFormatter.ofPattern(stringResource(R.string.date_format_month_day))
    val date = entry.eventDateTime.atZone(ZoneId.systemDefault()).format(dateFormatter)

    ListItem(
        headlineContent = {
            Text(entry.title ?: stringResource(R.string.diary_no_title), style = MaterialTheme.typography.titleSmall)
        },
        supportingContent = {
            Text(
                entry.content.take(100) + if (entry.content.length > 100) "..." else "",
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2
            )
        },
        trailingContent = {
            Text(date, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        },
        modifier = Modifier.clickable(onClick = onClick)
    )
}