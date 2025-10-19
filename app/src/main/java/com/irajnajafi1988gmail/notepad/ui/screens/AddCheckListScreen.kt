package com.irajnajafi1988gmail.notepad.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.irajnajafi1988gmail.notepad.R
import com.irajnajafi1988gmail.notepad.data.model.CheckItem
import com.irajnajafi1988gmail.notepad.domain.model.CheckList
import com.irajnajafi1988gmail.notepad.ui.components.ItemTabBar
import com.irajnajafi1988gmail.notepad.ui.components.common.bottombar.ItemAppBottomBar
import com.irajnajafi1988gmail.notepad.ui.components.common.bottombar.NoteAppBottomBar
import com.irajnajafi1988gmail.notepad.ui.components.common.dialog.TrashDialog
import com.irajnajafi1988gmail.notepad.ui.components.common.textfield.TitleTextField
import com.irajnajafi1988gmail.notepad.ui.theme.helpers.iconSize
import com.irajnajafi1988gmail.notepad.ui.viewModel.CheckListViewModel
import com.irajnajafi1988gmail.notepad.ui.viewModel.UiState
import kotlinx.coroutines.launch

@Composable
fun AddCheckListScreen(
    navController: NavController,
    checklistId: Long = -1,
    viewModel: CheckListViewModel = hiltViewModel(),
) {
    // Snackbar message for validation
    val message = stringResource(R.string.title_or_body_cannot_be_empty)

    // Collect checklist data from ViewModel
    val allCheckListsState by viewModel.allCheckListsState.collectAsState()

    // Find existing checklist if editing
    val checkListToEdit = when (allCheckListsState) {
        is UiState.Success -> (allCheckListsState as UiState.Success).data.find { it.id == checklistId }
        else -> null
    }

    // Local UI states
    var checkListTitle by remember { mutableStateOf(TextFieldValue("")) }
    val checkItems = remember { mutableStateListOf<CheckItem>() }

    var fontSizeTitle by remember { mutableFloatStateOf(20f) }
    var fontSizeNewItem by remember { mutableFloatStateOf(18f) }

    val focusRequesterTitle = remember { FocusRequester() }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Track user interactions for detecting active field
    val titleInteractionSource = remember { MutableInteractionSource() }
    val bodyInteractionSource = remember { MutableInteractionSource() }
    val isTitlePressed by titleInteractionSource.collectIsPressedAsState()
    val isBodyPressed by bodyInteractionSource.collectIsPressedAsState()
    var isTitleActive by remember { mutableStateOf(true) }

    var isSaving by remember { mutableStateOf(false) }

    // Load existing checklist data when editing
    LaunchedEffect(checkListToEdit) {
        checkListToEdit?.let {
            checkListTitle = TextFieldValue(it.title)
            checkItems.clear()
            checkItems.addAll(it.items)
        }
    }

    // Detect active field (Title vs. Body)
    LaunchedEffect(isTitlePressed, isBodyPressed) {
        isTitleActive = when {
            isTitlePressed -> true
            isBodyPressed -> false
            else -> isTitleActive
        }
    }

    // Main scaffold layout
    Scaffold(
        contentWindowInsets = WindowInsets.navigationBars,

        topBar = {
            ItemTabBar(
                title = stringResource(R.string.checkList),
                navigationIcon = R.drawable.back,
                onNavigationClick = { navController.popBackStack() },
                iconSizeDp = 30.dp
            )
        },
        bottomBar = {

            NoteAppBottomBar(
                modifier = Modifier.navigationBarsPadding(),
                isTitleActive = isTitleActive,
                selectedItem = null,
                onItemClick = { item ->
                    when (item) {
                        // Save checklist
                        ItemAppBottomBar.SAVE -> {
                            if (isSaving) {
                                return@NoteAppBottomBar
                            }
                            val nonEmptyItems = checkItems.filter { it.text.isNotBlank() }

                            if (checkListTitle.text.isBlank() && nonEmptyItems.isEmpty()) {
                                coroutineScope.launch { snackbarHostState.showSnackbar(message) }
                                return@NoteAppBottomBar
                            }
                            isSaving = true

                            val checkList = CheckList(
                                id = checkListToEdit?.id ?: 0,
                                title = checkListTitle.text,
                                items = nonEmptyItems
                            )

                            if (checkListToEdit == null) {
                                viewModel.addCheckList(checkList) { navController.popBackStack() }
                            } else {
                                viewModel.editCheckList(checkList)
                                navController.popBackStack()
                            }
                        }

                        // Font size controls
                        ItemAppBottomBar.SMALL_TITLE -> fontSizeTitle =
                            (fontSizeTitle - 2f).coerceAtLeast(12f)

                        ItemAppBottomBar.BIG_TITLE -> fontSizeTitle =
                            (fontSizeTitle + 2f).coerceAtMost(40f)

                        ItemAppBottomBar.SMALL_BODY -> fontSizeNewItem =
                            (fontSizeNewItem - 2f).coerceAtLeast(12f)

                        ItemAppBottomBar.BIG_BODY -> fontSizeNewItem =
                            (fontSizeNewItem + 2f).coerceAtMost(40f)
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
        ) {
            // Title text field
            item {
                TitleTextField(
                    value = checkListTitle,
                    onValueChange = { checkListTitle = it },
                    label = stringResource(R.string.checkList),
                    fontSize = fontSizeTitle,
                    interactionSource = titleInteractionSource,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 30.dp),
                    focusRequester = focusRequesterTitle
                )
            }

            // Checklist items
            item {
                CheckItemList(
                    items = checkItems,
                    onAddItem = {
                        if (checkItems.isEmpty() || checkItems.last().text.isNotBlank()) {
                            checkItems.add(CheckItem("", false))
                        }
                    },
                    onTextChange = { index, text ->
                        checkItems[index] = checkItems[index].copy(text = text)
                    },
                    onToggleCheck = { index ->
                        checkItems[index] =
                            checkItems[index].copy(isCheckItem = !checkItems[index].isCheckItem)
                    },
                    fontSize = fontSizeNewItem,
                    interactionSource = bodyInteractionSource,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }
        }
    }
}

@Composable
private fun CheckItemList(
    items: SnapshotStateList<CheckItem>,
    onToggleCheck: (index: Int) -> Unit,
    onAddItem: () -> Unit,
    onTextChange: (index: Int, text: String) -> Unit,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    fontSize: Float,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var itemToDeleteIndex by remember { mutableStateOf(-1) }
    val isLight = !isSystemInDarkTheme()

    // Keep a focus requester for each checklist item
    val focusRequesters = remember { mutableStateListOf<FocusRequester>() }
    if (focusRequesters.size < items.size) {
        repeat(items.size - focusRequesters.size) { focusRequesters.add(FocusRequester()) }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 20.dp)
    ) {
        // Add new checklist item button
        Row(verticalAlignment = Alignment.CenterVertically) {
            Card(
                shape = CircleShape,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            ) {
                IconButton(onClick = {
                    onAddItem()
                    focusRequesters.lastOrNull()?.requestFocus()
                }) {
                    Image(
                        painter = painterResource(R.drawable._add_media_social_icon),
                        contentDescription = null,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
            Text(
                text = stringResource(R.string.newItem),
                modifier = Modifier.padding(start = 10.dp),
                style = MaterialTheme.typography.titleMedium,
            )
        }

        // Render each checklist item
        items.forEachIndexed { index, item ->
            val firstLetter = item.text.firstOrNull { it.isLetter() }
            val isRtl = firstLetter != null && firstLetter.code in 0x0600..0x06FF

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(
                    containerColor = if (isLight)
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f)
                    else
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // آیکون چک‌باکس
                    IconButton(onClick = { onToggleCheck(index) }) {
                        val iconRes = if (item.isCheckItem) R.drawable.checkbox_full_tick_icon else R.drawable.blue_checkbox_empty_icon
                        Image(
                            painter = painterResource(id = iconRes),
                            contentDescription = "Check Item",
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    BasicTextField(
                        value = item.text,
                        onValueChange = { onTextChange(index, it) },
                        modifier = Modifier.weight(1f).focusRequester(focusRequesters[index]),
                        textStyle = TextStyle(
                            fontSize = fontSize.sp,
                            color = MaterialTheme.colorScheme.onBackground,
                            textDirection = if (isRtl) TextDirection.Rtl else TextDirection.Ltr
                        ),
                        interactionSource = interactionSource,
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    )

                    IconButton(onClick = {
                        if (item.text.isBlank()) {
                            items.removeAt(index)
                            focusRequesters.removeAt(index)
                        } else {
                            itemToDeleteIndex = index
                            showDeleteDialog = true
                        }
                    }) {
                        Image(
                            painter = painterResource(R.drawable.close_delete),
                            contentDescription = "Delete Item",
                            modifier = Modifier.size(iconSize())
                        )
                    }
                }
            }
        }

    }

    // Delete confirmation dialog
    if (showDeleteDialog && itemToDeleteIndex >= 0) {
        TrashDialog(
            onConfirm = {
                items.removeAt(itemToDeleteIndex)
                focusRequesters.removeAt(itemToDeleteIndex)
                showDeleteDialog = false
                itemToDeleteIndex = -1
            },
            onDismiss = {
                showDeleteDialog = false
                itemToDeleteIndex = -1
            },
            text = stringResource(R.string.delete_item_confirmation),
            imageRes = R.drawable.delete_recycle_trash_icon,
        )
    }
}
