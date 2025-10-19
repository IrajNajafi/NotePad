package com.irajnajafi1988gmail.notepad.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.irajnajafi1988gmail.notepad.R
import com.irajnajafi1988gmail.notepad.domain.model.CheckList
import com.irajnajafi1988gmail.notepad.domain.model.ItemDarkMode
import com.irajnajafi1988gmail.notepad.domain.model.Note
import com.irajnajafi1988gmail.notepad.domain.model.ToggleNotePad
import com.irajnajafi1988gmail.notepad.navigation.NaveScreen
import com.irajnajafi1988gmail.notepad.ui.components.ItemTabBar
import com.irajnajafi1988gmail.notepad.ui.components.common.bottombar.HomeBottomBar
import com.irajnajafi1988gmail.notepad.ui.components.common.bottombar.ItemsHomeBottomBar
import com.irajnajafi1988gmail.notepad.ui.components.common.cardCommon.cardNoteScreen.CardCheckListScreen
import com.irajnajafi1988gmail.notepad.ui.components.common.cardCommon.cardNoteScreen.CardNoteScreen
import com.irajnajafi1988gmail.notepad.ui.components.common.dialog.ItemDialog
import com.irajnajafi1988gmail.notepad.ui.components.settings.SettingsDrawer
import com.irajnajafi1988gmail.notepad.ui.viewModel.CheckListViewModel
import com.irajnajafi1988gmail.notepad.ui.viewModel.DarkModeViewModel
import com.irajnajafi1988gmail.notepad.ui.viewModel.DateViewModel
import com.irajnajafi1988gmail.notepad.ui.viewModel.DisplayViewModel
import com.irajnajafi1988gmail.notepad.ui.viewModel.HomeViewModel
import com.irajnajafi1988gmail.notepad.ui.viewModel.NoteViewModel
import com.irajnajafi1988gmail.notepad.ui.viewModel.UiState
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    navController: NavController,
    startSection: String? = null,   // Optional: initial section to open
    dateViewModel: DateViewModel = hiltViewModel(),
    checkListViewModel: CheckListViewModel = hiltViewModel(),
    noteViewModel: NoteViewModel = hiltViewModel(),
    darkModeViewModel: DarkModeViewModel = hiltViewModel(),
    displayViewModel: DisplayViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    // Collect the selected display mode (NOTE / CHECKLIST) from ViewModel
    val storedSection by displayViewModel.selectedDisplay.collectAsState()

    // Keep track of the currently selected section
    var selectedSection by remember {
        mutableStateOf(
            when (startSection) {
                "CHECKLIST" -> ToggleNotePad.CHECKLIST
                else -> storedSection
            }
        )
    }

    // Update section if display changes
    LaunchedEffect(storedSection) { selectedSection = storedSection }

    // Collect data from various ViewModels
    val persianDate by dateViewModel.today.collectAsState()
    val checkListsState by checkListViewModel.allCheckListsState.collectAsState()
    val notesState by noteViewModel.allNotesState.collectAsState()
    val darkMode by darkModeViewModel.selectedDarkMode.collectAsState()

    // Dialog state for adding a new note/checklist
    var showDialogAdd by remember { mutableStateOf(false) }

    // -------------------- Notes selection --------------------
    var noteSelectionMode by remember { mutableStateOf(false) }  // Flag to indicate selection mode
    var selectionAllNotes by remember { mutableStateOf(false) }  // Flag for select all
    var selectedNotes by remember { mutableStateOf(setOf<Note>()) } // Currently selected notes

    // -------------------- CheckLists selection --------------------
    var checkListSelectionMode by remember { mutableStateOf(false) }
    var selectionAllCheckLists by remember { mutableStateOf(false) }
    var selectedCheckLists by remember { mutableStateOf(setOf<CheckList>()) }

    // Collect trash and favorite counts
    val trashCount by homeViewModel.totalTrashCount.collectAsState()
    val favoriteCount by homeViewModel.totalFavoriteCount.collectAsState()

    // Drawer state for Settings
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Determine current section title and icons
    val currentSectionTitle = when (selectedSection) {
        ToggleNotePad.NOTE -> stringResource(R.string.note)
        ToggleNotePad.CHECKLIST -> stringResource(R.string.checkList)
    }
    val navigationIconRes = if (selectedSection == ToggleNotePad.NOTE) R.drawable.list else R.drawable.note
    val actionIconRes = R.drawable.settins

    // -------------------- Settings Drawer --------------------
    SettingsDrawer(
        drawerState = drawerState,
        onItemClick = { scope.launch { drawerState.close() } },
        content = {
            Scaffold(
                contentWindowInsets = WindowInsets.navigationBars,
                topBar = {
                    ItemTabBar(
                        title = persianDate,           // Show today's date
                        navigationIcon = navigationIconRes,
                        actionIcon = actionIconRes,
                        iconSizeDp = 45.dp,
                        onNavigationClick = { displayViewModel.toggleDisplay() }, // Toggle NOTE / CHECKLIST
                        onActionClick = {  // Open/close settings drawer
                            scope.launch {
                                if (drawerState.isClosed) drawerState.open()
                                else drawerState.close()
                            }
                        }
                    )
                },
                bottomBar = {
                    // -------------------- Bottom bar --------------------
                    HomeBottomBar(
                        modifier = Modifier.navigationBarsPadding(),
                        trashCount = trashCount,
                        favoriteCount = favoriteCount,
                        onItemClick = { item ->
                            when (item) {
                                ItemsHomeBottomBar.TRASH -> navController.navigate(NaveScreen.TrashScreen.route)
                                ItemsHomeBottomBar.SEARCH -> navController.navigate(NaveScreen.SearchScreen.route)
                                ItemsHomeBottomBar.ADD -> showDialogAdd = true
                                ItemsHomeBottomBar.FAVORITE -> navController.navigate(NaveScreen.FavoriteScreen.route)
                                ItemsHomeBottomBar.DARKMODE -> {
                                    // Toggle dark/light mode
                                    val newMode = when (darkMode) {
                                        ItemDarkMode.LIGHT -> ItemDarkMode.DARK
                                        ItemDarkMode.DARK -> ItemDarkMode.LIGHT
                                        ItemDarkMode.SYSTEM, null -> ItemDarkMode.LIGHT
                                    }
                                    darkModeViewModel.saveDarkMode(newMode)
                                }
                            }
                        }
                    )
                }
            ) { innerPadding ->

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    // Background image depending on section
                    val bgRes = if (selectedSection == ToggleNotePad.NOTE) R.drawable.back_note else R.drawable.back_checklist
                    Image(
                        painter = painterResource(id = bgRes),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        alpha = 0.1f
                    )

                    Column(modifier = Modifier.fillMaxSize()) {

                        // -------------------- Section Title --------------------
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = currentSectionTitle,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        // -------------------- Selection ActionBar --------------------
                        when (selectedSection) {
                            ToggleNotePad.NOTE -> SelectionActionBar(
                                selectedItems = selectedNotes.toList(),
                                totalItemsCount = (notesState as? UiState.Success)?.data?.size ?: 0,
                                selectionAllMode = selectionAllNotes,
                                onToggleSelectAll = { all ->
                                    selectionAllNotes = all
                                    selectedNotes = if (all) (notesState as? UiState.Success)?.data.orEmpty().toSet()
                                    else emptySet()
                                },
                                onDeleteClick = {
                                    selectedNotes.forEach { noteViewModel.moveNoteToTrash(it) }
                                    selectedNotes = emptySet()
                                    noteSelectionMode = false
                                    selectionAllNotes = false
                                },
                                onCancelClick = {
                                    selectedNotes = emptySet()
                                    noteSelectionMode = false
                                    selectionAllNotes = false
                                }
                            )
                            ToggleNotePad.CHECKLIST -> SelectionActionBar(
                                selectedItems = selectedCheckLists.toList(),
                                totalItemsCount = (checkListsState as? UiState.Success)?.data?.size ?: 0,
                                selectionAllMode = selectionAllCheckLists,
                                onToggleSelectAll = { all ->
                                    selectionAllCheckLists = all
                                    selectedCheckLists = if (all) (checkListsState as? UiState.Success)?.data.orEmpty().toSet()
                                    else emptySet()
                                },
                                onDeleteClick = {
                                    selectedCheckLists.forEach { checkListViewModel.moveCheckListToTrash(it) }
                                    selectedCheckLists = emptySet()
                                    checkListSelectionMode = false
                                    selectionAllCheckLists = false
                                },
                                onCancelClick = {
                                    selectedCheckLists = emptySet()
                                    checkListSelectionMode = false
                                    selectionAllCheckLists = false
                                }
                            )
                        }

                        // -------------------- Items List --------------------
                        when (selectedSection) {
                            ToggleNotePad.NOTE -> AnimatedContentState(notesState) { notes ->
                                if (notes.isEmpty()) EmptyMessage(R.string.there_is_no_note)
                                else CardNoteScreen(
                                    notes = notes,
                                    dateViewModel = dateViewModel,
                                    onClick = { note ->
                                        if (noteSelectionMode) {
                                            selectedNotes =
                                                if (selectedNotes.contains(note)) selectedNotes - note
                                                else selectedNotes + note
                                        } else {
                                            navController.navigate("${NaveScreen.AddNoteScreen.route}/${note.id}")
                                        }
                                    },
                                    onLongClick = { note ->
                                        noteSelectionMode = true
                                        selectedNotes = selectedNotes + note
                                    },
                                    onFavoriteClick = { noteViewModel.toggleFavorite(it) },
                                    selectedNotes = selectedNotes,
                                    selectionMode = noteSelectionMode,
                                    onCheckChange = { note, checked ->
                                        selectedNotes = if (checked) selectedNotes + note else selectedNotes - note
                                        selectionAllNotes = selectedNotes.size == ((notesState as? UiState.Success)?.data?.size ?: 0)
                                    }
                                )
                            }
                            ToggleNotePad.CHECKLIST -> AnimatedContentState(checkListsState) { lists ->
                                if (lists.isEmpty()) EmptyMessage(R.string.there_is_no_list)
                                else CardCheckListScreen(
                                    checkLists = lists,
                                    dateViewModel = dateViewModel,
                                    onClick = { item ->
                                        if (checkListSelectionMode) {
                                            selectedCheckLists =
                                                if (selectedCheckLists.contains(item)) selectedCheckLists - item
                                                else selectedCheckLists + item
                                        } else {
                                            navController.navigate("${NaveScreen.AddCheckListScreen.route}/${item.id}")
                                        }
                                    },
                                    onLongClick = { item ->
                                        checkListSelectionMode = true
                                        selectedCheckLists = selectedCheckLists + item
                                    },
                                    onFavoriteClick = { checkListViewModel.toggleFavorite(it) },
                                    selectedCheckLists = selectedCheckLists,
                                    selectionMode = checkListSelectionMode,
                                    onCheckChange = { item, checked ->
                                        selectedCheckLists =
                                            if (checked) selectedCheckLists + item else selectedCheckLists - item
                                        selectionAllCheckLists = selectedCheckLists.size == ((checkListsState as? UiState.Success)?.data?.size ?: 0)
                                    }
                                )
                            }
                        }
                    }

                    // -------------------- Add Note / CheckList Dialog --------------------
                    if (showDialogAdd) {
                        ItemDialog(
                            showDialog = true,
                            onDismiss = { showDialogAdd = false },
                            items = listOf(R.string.note, R.string.checkList),
                            onClickItem = {
                                showDialogAdd = false
                                when (it) {
                                    R.string.note -> {
                                        displayViewModel.saveDisplay(ToggleNotePad.NOTE)
                                        navController.navigate(NaveScreen.AddNoteScreen.route)
                                    }
                                    R.string.checkList -> {
                                        displayViewModel.saveDisplay(ToggleNotePad.CHECKLIST)
                                        navController.navigate(NaveScreen.AddCheckListScreen.route)
                                    }
                                }
                            }
                        )
                    }

                    // -------------------- Dim background when drawer is open --------------------
                    if (drawerState.isOpen) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.3f))
                                .clickable { scope.launch { drawerState.close() } }
                        )
                    }
                }
            }
        }
    )
}

// -------------------- Utility Composables --------------------
@Composable
fun <T> AnimatedContentState(
    state: UiState<T>,
    onSuccess: @Composable (T) -> Unit
) {
    // Show loading indicator
    AnimatedVisibility(visible = state is UiState.Loading, enter = fadeIn(), exit = fadeOut()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }

    // Show error message
    AnimatedVisibility(visible = state is UiState.Error, enter = fadeIn(), exit = fadeOut()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = (state as UiState.Error).message, color = MaterialTheme.colorScheme.error)
        }
    }

    // Show content if success
    if (state is UiState.Success) onSuccess(state.data)
}

@Composable
private fun EmptyMessage(stringRes: Int) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = stringResource(stringRes), style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun <T> SelectionActionBar(
    selectedItems: List<T>,
    totalItemsCount: Int,
    selectionAllMode: Boolean,
    onToggleSelectAll: (Boolean) -> Unit,
    onDeleteClick: () -> Unit,
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // Show action bar only if some items are selected
    if (selectedItems.isNotEmpty()) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            ActionItem(
                text = "All",
                iconRes = if (selectionAllMode) R.drawable.checkbox_full_tick_icon
                else R.drawable.blue_checkbox_empty_icon,
                onClick = { onToggleSelectAll(!selectionAllMode) }
            )

            ActionItem(
                text = "Delete",
                iconRes = R.drawable.bin_fly_garbage_trash_icon,
                onClick = onDeleteClick
            )

            ActionItem(
                text = "Cancel",
                iconRes = R.drawable.close_delete,
                onClick = onCancelClick
            )
        }
    }
}

@Composable
private fun ActionItem(
    text: String,
    iconRes: Int,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = text,
            modifier = Modifier.size(22.dp)
        )
        Text(
            text = text,
            fontSize = 12.sp,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
        )
    }
}
