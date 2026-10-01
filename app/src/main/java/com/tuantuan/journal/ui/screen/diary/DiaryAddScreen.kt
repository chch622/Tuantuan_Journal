package com.tuantuan.journal.ui.screen.diary

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tuantuan.journal.R
import com.tuantuan.journal.domain.model.Mood
import com.tuantuan.journal.domain.model.Weather
import com.tuantuan.journal.ui.model.resolveMessage
import com.tuantuan.journal.ui.component.TtMediaAddBar
import com.tuantuan.journal.ui.component.TtSelectedPhotosBar
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

    // Photo Picker — MEDIA_UX.md §4.2
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(10)
    ) { uris: List<Uri> ->
        if (uris.isNotEmpty()) {
            viewModel.addSelectedPhotos(uris)
        }
    }

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
                title = { Text(stringResource(R.string.diary_add)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.back))
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
                label = { Text(stringResource(R.string.diary_title_optional)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // 日期
            OutlinedTextField(
                value = formState.eventDateTime,
                onValueChange = { viewModel.updateFormState { s -> s.copy(eventDateTime = it) } },
                label = { Text(stringResource(R.string.diary_date)) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(stringResource(R.string.diary_date_hint)) },
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
                    label = { Text(stringResource(R.string.diary_mood)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = moodExpanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryNotEditable)
                )
                ExposedDropdownMenu(
                    expanded = moodExpanded,
                    onDismissRequest = { moodExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.diary_mood_none)) },
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
                    label = { Text(stringResource(R.string.diary_weather)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = weatherExpanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryNotEditable)
                )
                ExposedDropdownMenu(
                    expanded = weatherExpanded,
                    onDismissRequest = { weatherExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.diary_weather_none)) },
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
                label = { Text(stringResource(R.string.diary_location_optional)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // 标签选择
            if (formState.availableTags.isNotEmpty()) {
                Column {
                    Text(
                        stringResource(R.string.diary_tags),
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

            // 媒体添加栏 — MEDIA_UX.md §4
            TtMediaAddBar(
                onPhotoClick = {
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }
            )

            // 已选照片预览 — MEDIA_UX.md §4.2
            if (formState.selectedMediaUris.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.media_selected_count, formState.selectedMediaUris.size),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                TtSelectedPhotosBar(
                    photoUris = formState.selectedMediaUris,
                    onRemovePhoto = { viewModel.removeSelectedPhoto(it) }
                )
            }

            // 日记内容（必填）
            OutlinedTextField(
                value = formState.content,
                onValueChange = { viewModel.updateFormState { s -> s.copy(content = it) } },
                label = { Text(stringResource(R.string.diary_content_required)) },
                modifier = Modifier.fillMaxWidth().height(200.dp),
                isError = formState.error != null && formState.content.isBlank()
            )

            // 错误提示
            formState.error?.let { error ->
                Text(error.resolveMessage(), color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(Modifier.height(8.dp))

            // 保存按钮
            Button(
                onClick = {
                    viewModel.createEntry(childId)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !formState.isSaving && formState.content.isNotBlank()
            ) {
                if (formState.isSaving) {
                    CircularProgressIndicator(modifier = Modifier.padding(end = 8.dp))
                }
                Text(stringResource(R.string.diary_save))
            }
        }
    }
}

@Composable
private fun moodDisplayName(mood: Mood): String = when (mood) {
    Mood.HAPPY -> stringResource(R.string.mood_happy)
    Mood.CALM -> stringResource(R.string.mood_calm)
    Mood.EXCITED -> stringResource(R.string.mood_excited)
    Mood.SAD -> stringResource(R.string.mood_sad)
    Mood.ANGRY -> stringResource(R.string.mood_angry)
    Mood.SICK -> stringResource(R.string.mood_sick)
    Mood.TIRED -> stringResource(R.string.mood_tired)
}

@Composable
private fun weatherDisplayName(weather: Weather): String = when (weather) {
    Weather.SUNNY -> stringResource(R.string.weather_sunny)
    Weather.CLOUDY -> stringResource(R.string.weather_cloudy)
    Weather.RAINY -> stringResource(R.string.weather_rainy)
    Weather.SNOWY -> stringResource(R.string.weather_snowy)
    Weather.WINDY -> stringResource(R.string.weather_windy)
    Weather.FOGGY -> stringResource(R.string.weather_foggy)
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