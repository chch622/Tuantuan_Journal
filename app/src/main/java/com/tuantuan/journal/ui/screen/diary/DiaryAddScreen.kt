package com.tuantuan.journal.ui.screen.diary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tuantuan.journal.domain.model.Mood
import com.tuantuan.journal.domain.model.Weather
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiaryAddScreen(
    childId: String,
    onSaved: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: DiaryViewModel = hiltViewModel()
) {
    val formState by viewModel.formState.collectAsState()
    var moodExpanded by remember { mutableStateOf(false) }
    var weatherExpanded by remember { mutableStateOf(false) }

    // 初始化日期为今天
    LaunchedEffect(Unit) {
        val today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        viewModel.updateFormState { it.copy(eventDateTime = today) }
        viewModel.loadAvailableTags()
    }

    // 保存成功后回调
    LaunchedEffect(formState.savedSuccessfully) {
        if (formState.savedSuccessfully) {
            onSaved()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("写日记") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 标题（可选）
            OutlinedTextField(
                value = formState.title,
                onValueChange = { viewModel.updateFormState { s -> s.copy(title = it) } },
                label = { Text("标题（可选）") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // 日期
            OutlinedTextField(
                value = formState.eventDateTime,
                onValueChange = { viewModel.updateFormState { s -> s.copy(eventDateTime = it) } },
                label = { Text("日期") },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("yyyy-MM-dd") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )

            // 心情选择
            ExposedDropdownMenuBox(
                expanded = moodExpanded,
                onExpandedChange = { moodExpanded = !moodExpanded }
            ) {
                OutlinedTextField(
                    value = formState.mood,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("心情") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = moodExpanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryNotEditable)
                )
                ExposedDropdownMenu(
                    expanded = moodExpanded,
                    onDismissRequest = { moodExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("不选择") },
                        onClick = {
                            viewModel.updateFormState { it.copy(mood = "") }
                            moodExpanded = false
                        }
                    )
                    Mood.entries.forEach { mood ->
                        DropdownMenuItem(
                            text = { Text(moodDisplayName(mood)) },
                            onClick = {
                                viewModel.updateFormState { it.copy(mood = mood.name) }
                                moodExpanded = false
                            }
                        )
                    }
                }
            }

            // 天气选择
            ExposedDropdownMenuBox(
                expanded = weatherExpanded,
                onExpandedChange = { weatherExpanded = !weatherExpanded }
            ) {
                OutlinedTextField(
                    value = formState.weather,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("天气") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = weatherExpanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryNotEditable)
                )
                ExposedDropdownMenu(
                    expanded = weatherExpanded,
                    onDismissRequest = { weatherExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("不选择") },
                        onClick = {
                            viewModel.updateFormState { it.copy(weather = "") }
                            weatherExpanded = false
                        }
                    )
                    Weather.entries.forEach { weather ->
                        DropdownMenuItem(
                            text = { Text(weatherDisplayName(weather)) },
                            onClick = {
                                viewModel.updateFormState { it.copy(weather = weather.name) }
                                weatherExpanded = false
                            }
                        )
                    }
                }
            }

            // 地点（可选）
            OutlinedTextField(
                value = formState.location,
                onValueChange = { viewModel.updateFormState { s -> s.copy(location = it) } },
                label = { Text("地点（可选）") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // 标签选择
            if (formState.availableTags.isNotEmpty()) {
                Column {
                    Text(
                        "标签",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(4.dp))
                    TagFlowRow(
                        tags = formState.availableTags,
                        selectedTagIds = formState.selectedTagIds,
                        onTagToggle = { viewModel.toggleTagSelection(it) }
                    )
                }
            }

            // 日记内容（必填）
            OutlinedTextField(
                value = formState.content,
                onValueChange = { viewModel.updateFormState { s -> s.copy(content = it) } },
                label = { Text("日记内容 *") },
                modifier = Modifier.fillMaxWidth().height(200.dp),
                isError = formState.error != null && formState.content.isBlank()
            )

            // 错误提示
            formState.error?.let { error ->
                Text(error.displayMessage, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(Modifier.height(8.dp))

            // 保存按钮
            Button(
                onClick = {
                    viewModel.createEntry(childId)
                    // 简单处理：延迟后检查是否保存成功
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !formState.isSaving && formState.content.isNotBlank()
            ) {
                if (formState.isSaving) {
                    CircularProgressIndicator(modifier = Modifier.padding(end = 8.dp))
                }
                Text("保存日记")
            }
        }
    }
}

private fun moodDisplayName(mood: Mood): String = when (mood) {
    Mood.HAPPY -> "开心 😊"
    Mood.CALM -> "平静 😌"
    Mood.EXCITED -> "兴奋 🤩"
    Mood.SAD -> "难过 😢"
    Mood.ANGRY -> "生气 😠"
    Mood.SICK -> "不舒服 🤒"
    Mood.TIRED -> "疲惫 😴"
}

private fun weatherDisplayName(weather: Weather): String = when (weather) {
    Weather.SUNNY -> "晴天 ☀️"
    Weather.CLOUDY -> "多云 ☁️"
    Weather.RAINY -> "下雨 🌧️"
    Weather.SNOWY -> "下雪 ❄️"
    Weather.WINDY -> "刮风 💨"
    Weather.FOGGY -> "雾天 🌫️"
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TagFlowRow(
    tags: List<com.tuantuan.journal.domain.model.Tag>,
    selectedTagIds: Set<String>,
    onTagToggle: (String) -> Unit
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        tags.forEach { tag ->
            FilterChip(
                selected = tag.id in selectedTagIds,
                onClick = { onTagToggle(tag.id) },
                label = { Text("#${tag.name}") }
            )
        }
    }
}