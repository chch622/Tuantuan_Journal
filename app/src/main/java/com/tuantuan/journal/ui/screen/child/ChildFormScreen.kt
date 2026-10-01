package com.tuantuan.journal.ui.screen.child

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tuantuan.journal.R
import com.tuantuan.journal.ui.model.resolveMessage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChildAddScreen(
    onSaved: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: ChildViewModel = hiltViewModel()
) {
    val formState by viewModel.formState.collectAsState()

    LaunchedEffect(formState.savedSuccessfully) {
        if (formState.savedSuccessfully) {
            onSaved()
        }
    }

    ChildFormContent(
        title = stringResource(R.string.child_add),
        formState = formState,
        onNameChange = { v -> viewModel.updateFormState { it.copy(name = v) } },
        onNicknameChange = { v -> viewModel.updateFormState { it.copy(nickname = v) } },
        onBirthDateChange = { v -> viewModel.updateFormState { it.copy(birthDate = v) } },
        onGenderChange = { v -> viewModel.updateFormState { it.copy(gender = v) } },
        onBirthWeightChange = { v -> viewModel.updateFormState { it.copy(birthWeight = v) } },
        onBirthHeightChange = { v -> viewModel.updateFormState { it.copy(birthHeight = v) } },
        onBloodTypeChange = { v -> viewModel.updateFormState { it.copy(bloodType = v) } },
        onBirthPlaceChange = { v -> viewModel.updateFormState { it.copy(birthPlace = v) } },
        onNotesChange = { v -> viewModel.updateFormState { it.copy(notes = v) } },
        onSave = { viewModel.createChild() },
        onBackClick = onBackClick,
        isSaving = formState.isSaving
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChildEditScreen(
    childId: String,
    onSaved: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: ChildViewModel = hiltViewModel()
) {
    val formState by viewModel.formState.collectAsState()
    val detailState by viewModel.detailState.collectAsState()

    LaunchedEffect(childId) {
        viewModel.loadChild(childId)
    }

    LaunchedEffect(detailState.child) {
        detailState.child?.let { viewModel.populateForm(it) }
    }

    LaunchedEffect(formState.savedSuccessfully) {
        if (formState.savedSuccessfully) {
            onSaved()
        }
    }

    ChildFormContent(
        title = stringResource(R.string.child_edit),
        formState = formState,
        onNameChange = { v -> viewModel.updateFormState { it.copy(name = v) } },
        onNicknameChange = { v -> viewModel.updateFormState { it.copy(nickname = v) } },
        onBirthDateChange = { v -> viewModel.updateFormState { it.copy(birthDate = v) } },
        onGenderChange = { v -> viewModel.updateFormState { it.copy(gender = v) } },
        onBirthWeightChange = { v -> viewModel.updateFormState { it.copy(birthWeight = v) } },
        onBirthHeightChange = { v -> viewModel.updateFormState { it.copy(birthHeight = v) } },
        onBloodTypeChange = { v -> viewModel.updateFormState { it.copy(bloodType = v) } },
        onBirthPlaceChange = { v -> viewModel.updateFormState { it.copy(birthPlace = v) } },
        onNotesChange = { v -> viewModel.updateFormState { it.copy(notes = v) } },
        onSave = { viewModel.updateChild(childId) },
        onBackClick = onBackClick,
        isSaving = formState.isSaving
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChildFormContent(
    title: String,
    formState: ChildFormState,
    onNameChange: (String) -> Unit,
    onNicknameChange: (String) -> Unit,
    onBirthDateChange: (String) -> Unit,
    onGenderChange: (String) -> Unit,
    onBirthWeightChange: (String) -> Unit,
    onBirthHeightChange: (String) -> Unit,
    onBloodTypeChange: (String) -> Unit,
    onBirthPlaceChange: (String) -> Unit,
    onNotesChange: (String) -> Unit,
    onSave: () -> Unit,
    onBackClick: () -> Unit,
    isSaving: Boolean
) {
    var genderExpanded by remember { mutableStateOf(false) }
    val genderOptions = listOf("", "MALE", "FEMALE", "OTHER")
    val genderLabels = mapOf(
        "" to stringResource(R.string.child_gender_select),
        "MALE" to stringResource(R.string.child_gender_male),
        "FEMALE" to stringResource(R.string.child_gender_female),
        "OTHER" to stringResource(R.string.child_gender_other)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = formState.name,
                onValueChange = onNameChange,
                label = { Text(stringResource(R.string.child_name_required)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = formState.nickname,
                onValueChange = onNicknameChange,
                label = { Text(stringResource(R.string.child_nickname_label)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = formState.birthDate,
                onValueChange = onBirthDateChange,
                label = { Text(stringResource(R.string.child_birth_date_format)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                placeholder = { Text(stringResource(R.string.child_birth_date_hint)) }
            )

            ExposedDropdownMenuBox(
                expanded = genderExpanded,
                onExpandedChange = { genderExpanded = !genderExpanded }
            ) {
                OutlinedTextField(
                    value = genderLabels[formState.gender] ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.child_gender)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = genderExpanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryNotEditable)
                )
                ExposedDropdownMenu(expanded = genderExpanded, onDismissRequest = { genderExpanded = false }) {
                    genderOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(genderLabels[option] ?: option) },
                            onClick = {
                                onGenderChange(option)
                                genderExpanded = false
                            }
                        )
                    }
                }
            }

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = formState.birthWeight,
                    onValueChange = onBirthWeightChange,
                    label = { Text(stringResource(R.string.child_birth_weight_unit)) },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                OutlinedTextField(
                    value = formState.birthHeight,
                    onValueChange = onBirthHeightChange,
                    label = { Text(stringResource(R.string.child_birth_height_unit)) },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }

            OutlinedTextField(
                value = formState.bloodType,
                onValueChange = onBloodTypeChange,
                label = { Text(stringResource(R.string.child_blood_type)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = formState.birthPlace,
                onValueChange = onBirthPlaceChange,
                label = { Text(stringResource(R.string.child_birth_place)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = formState.notes,
                onValueChange = onNotesChange,
                label = { Text(stringResource(R.string.child_notes)) },
                modifier = Modifier.fillMaxWidth().height(120.dp),
                maxLines = 5
            )

            formState.error?.let {
                Text(it.resolveMessage(), color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = onSave,
                modifier = Modifier.fillMaxWidth(),
                enabled = !isSaving
            ) {
                if (isSaving) {
                    CircularProgressIndicator(modifier = Modifier.padding(end = 8.dp))
                }
                Text(stringResource(R.string.save))
            }
        }
    }
}