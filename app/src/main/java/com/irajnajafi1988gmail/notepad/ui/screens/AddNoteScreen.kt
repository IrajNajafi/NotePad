package com.irajnajafi1988gmail.notepad.ui.screens

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.irajnajafi1988gmail.notepad.R
import com.irajnajafi1988gmail.notepad.domain.model.Note
import com.irajnajafi1988gmail.notepad.ui.components.ItemTabBar
import com.irajnajafi1988gmail.notepad.ui.components.common.bottombar.ItemAppBottomBar
import com.irajnajafi1988gmail.notepad.ui.components.common.bottombar.NoteAppBottomBar
import com.irajnajafi1988gmail.notepad.ui.components.common.textfield.NoteBodyTextField
import com.irajnajafi1988gmail.notepad.ui.components.common.textfield.TitleTextField
import com.irajnajafi1988gmail.notepad.ui.viewModel.NoteViewModel
import com.irajnajafi1988gmail.notepad.ui.viewModel.UiState
import kotlinx.coroutines.launch

@Composable
fun AddNoteScreen(
    navController: NavController,
    viewModel: NoteViewModel = hiltViewModel(),
    noteId: Long = -1,
) {
    // Snackbar message for empty title/body
    val message = stringResource(R.string.title_or_body_cannot_be_empty)

    // Collect notes state from ViewModel
    val allNotes by viewModel.allNotesState.collectAsState()

    // Find the note to edit if noteId is valid
    val notesToEdit = (allNotes as? UiState.Success)?.data?.find { it.id == noteId }

    // Title and content text fields
    var noteTitle by remember { mutableStateOf(TextFieldValue("")) }
    var content by remember { mutableStateOf(TextFieldValue("")) }

    // Focus requester for the title field
    val titleFocusRequester = remember { FocusRequester() }

    // Font sizes for title and content
    var fontSizeTitle by remember { mutableFloatStateOf(18f) }
    var fontSizeContent by remember { mutableFloatStateOf(24f) }

    // Snackbar and coroutine scope for messages
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Track which field is currently active (Title or Body)
    var isTitleActive by remember { mutableStateOf(true) }

    // Interaction sources for detecting focus/click
    val titleInteractionSource = remember { MutableInteractionSource() }
    val bodyInteractionSource = remember { MutableInteractionSource() }

    // Detect if title or body is pressed
    val isTitlePressed by titleInteractionSource.collectIsPressedAsState()
    val isBodyPressed by bodyInteractionSource.collectIsPressedAsState()

    var isSaving by remember { mutableStateOf(false) }

    // When editing an existing note, fill fields with existing data
    LaunchedEffect(notesToEdit) {
        notesToEdit?.let {
            noteTitle = TextFieldValue(it.title)
            content = TextFieldValue(it.content)
        }
    }

    // Detect which field (Title/Body) is currently selected
    LaunchedEffect(isTitlePressed, isBodyPressed) {
        isTitleActive = when {
            isTitlePressed -> true
            isBodyPressed -> false
            else -> isTitleActive
        }
    }

    // Scaffold: main layout with TopBar, BottomBar, and Body
    Scaffold(
        contentWindowInsets = WindowInsets.navigationBars,
        topBar = {
            ItemTabBar(
                title = stringResource(R.string.note),
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
                        // Decrease title font size
                        ItemAppBottomBar.SMALL_TITLE -> fontSizeTitle =
                            (fontSizeTitle - 2f).coerceAtLeast(12f)

                        // Increase title font size
                        ItemAppBottomBar.BIG_TITLE -> fontSizeTitle =
                            (fontSizeTitle + 2f).coerceAtMost(40f)

                        // Decrease body font size
                        ItemAppBottomBar.SMALL_BODY -> fontSizeContent =
                            (fontSizeContent - 2f).coerceAtLeast(12f)

                        // Increase body font size
                        ItemAppBottomBar.BIG_BODY -> fontSizeContent =
                            (fontSizeContent + 2f).coerceAtMost(40f)

                        // Save note
                        ItemAppBottomBar.SAVE -> {
                            if (isSaving) {
                                return@NoteAppBottomBar
                            }
                            if (noteTitle.text.isBlank() && content.text.isBlank()) {
                                coroutineScope.launch { snackbarHostState.showSnackbar(message) }
                                return@NoteAppBottomBar
                            }
                            isSaving = true
                            val note = Note(
                                id = notesToEdit?.id ?: 0,
                                title = noteTitle.text,
                                content = content.text
                            )

                            if (notesToEdit == null) {
                                viewModel.addNote(note) { navController.popBackStack() }
                            } else {
                                viewModel.editNote(note)
                                navController.popBackStack()
                            }
                        }
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        // Main content layout
        Column(
            modifier = Modifier
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Title input field
            TitleTextField(
                value = noteTitle,
                onValueChange = { noteTitle = it },
                focusRequester = titleFocusRequester,
                label = stringResource(R.string.noteTitle),
                fontSize = fontSizeTitle,
                interactionSource = titleInteractionSource,
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 30.dp)
            )

            Spacer(Modifier.height(16.dp))

            // Body input field
            NoteBodyTextField(
                value = content,
                onValueChange = { content = it },
                label = stringResource(R.string.content),
                fontSize = fontSizeContent,
                interactionSource = bodyInteractionSource,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
            )
        }
    }
}
