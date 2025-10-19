package com.irajnajafi1988gmail.notepad.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.irajnajafi1988gmail.notepad.R
import com.irajnajafi1988gmail.notepad.navigation.NaveScreen
import com.irajnajafi1988gmail.notepad.ui.components.ItemTabBar
import com.irajnajafi1988gmail.notepad.ui.components.common.cardCommon.ItemCardCheckList
import com.irajnajafi1988gmail.notepad.ui.components.common.cardCommon.ItemCardNote
import com.irajnajafi1988gmail.notepad.ui.viewModel.CheckListViewModel
import com.irajnajafi1988gmail.notepad.ui.viewModel.DateViewModel
import com.irajnajafi1988gmail.notepad.ui.viewModel.NoteViewModel
import com.irajnajafi1988gmail.notepad.ui.viewModel.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavController,
    noteViewModel: NoteViewModel = hiltViewModel(),
    checkListViewModel: CheckListViewModel = hiltViewModel(),
    dateViewModel: DateViewModel = hiltViewModel()
) {
    // -------------------- State variables --------------------
    var searchQuery by remember { mutableStateOf("") }      // Current search text
    var isNotesExpanded by remember { mutableStateOf(true) }    // Notes section expanded/collapsed
    var isListsExpanded by remember { mutableStateOf(true) }    // CheckLists section expanded/collapsed

    // Collect all notes and checklists from ViewModels
    val allNotes by noteViewModel.allNotesState.collectAsState()
    val allCheckLists by checkListViewModel.allCheckListsState.collectAsState()

    // Extract data if UiState is Success, otherwise empty lists
    val notes = (allNotes as? UiState.Success)?.data ?: emptyList()
    val checkLists = (allCheckLists as? UiState.Success)?.data ?: emptyList()

    // Filter items based on search query (case-insensitive)
    val filteredNotes = notes.filter {
        it.title.contains(searchQuery, ignoreCase = true) ||
                it.content.contains(searchQuery, ignoreCase = true)
    }

    val filteredCheckLists = checkLists.filter {
        it.title.contains(searchQuery, ignoreCase = true) ||
                it.content.contains(searchQuery, ignoreCase = true)
    }

    // -------------------- Scaffold with TopBar --------------------
    Scaffold(
        topBar = {
            ItemTabBar(
                title = stringResource(R.string.search),
                navigationIcon = R.drawable.back,
                onNavigationClick = { navController.popBackStack() } // Back navigation
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())  // Scrollable content
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // -------------------- Search input field --------------------
            BasicTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface
                ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                decorationBox = { innerTextField ->
                    // Background and placeholder
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                shape = MaterialTheme.shapes.medium
                            )
                            .padding(12.dp)
                    ) {
                        if (searchQuery.isEmpty()) {
                            Text(
                                text = stringResource(R.string.search____),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            )
                        }
                        innerTextField()
                    }
                }
            )

            // Only show search results if the query is not blank
            if (searchQuery.isNotBlank()) {
                // -------------------- Notes Section --------------------
                SearchSection(
                    title = "${stringResource(R.string.note)} ${filteredNotes.size}",
                    isExpanded = isNotesExpanded,
                    onHeaderClick = { isNotesExpanded = !isNotesExpanded }
                ) {
                    if (filteredNotes.isEmpty()) {
                        Text(
                            text = stringResource(R.string.no_notes_found),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                    filteredNotes.forEach { note ->
                        val displayDate = dateViewModel.getFormattedDate(note.timestamp)

                        // Note card
                        ItemCardNote(
                            note = note,
                            displayDate = displayDate,
                            onClick = { navController.navigate("${NaveScreen.AddNoteScreen.route}/${note.id}") },
                            onLongClick = {},
                            onFavoriteClick = { noteViewModel.toggleFavorite(note) },
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )

                        // Highlight matching text
                        HighlightedText(
                            note.title,
                            searchQuery,
                            Modifier.padding(horizontal = 8.dp)
                        )
                    }
                }

                // -------------------- CheckLists Section --------------------
                SearchSection(
                    title = "${stringResource(R.string.checkList)} ${filteredCheckLists.size}",
                    isExpanded = isListsExpanded,
                    onHeaderClick = { isListsExpanded = !isListsExpanded }
                ) {
                    if (filteredCheckLists.isEmpty()) {
                        Text(
                            text = stringResource(R.string.no_checklists_found),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                    filteredCheckLists.forEach { list ->
                        val displayDate = dateViewModel.getFormattedDate(list.timestamp)

                        // CheckList card
                        ItemCardCheckList(
                            checkList = list,
                            displayDate = displayDate,
                            onClick = { navController.navigate("${NaveScreen.AddCheckListScreen.route}/${list.id}") },
                            onLongClick = {},
                            onFavoriteClick = { checkListViewModel.toggleFavorite(list) },
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )

                        // Highlight matching text
                        HighlightedText(
                            list.title,
                            searchQuery,
                            Modifier.padding(horizontal = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

// -------------------- Collapsible Section Composable --------------------
@Composable
fun SearchSection(
    title: String,
    isExpanded: Boolean,
    onHeaderClick: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()  // Animate expand/collapse height changes
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onHeaderClick() }
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Section title
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )

            // Expand/collapse icon
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = if (isExpanded) "Collapse" else "Expand",
                modifier = Modifier
                    .size(24.dp)
                    .rotate(if (isExpanded) 180f else 0f) // Rotate arrow when expanded
            )
        }

        // Display content only when expanded
        if (isExpanded) {
            content()
        }
    }
}

// -------------------- Highlight text that matches search query --------------------
@Composable
fun HighlightedText(text: String, query: String, modifier: Modifier = Modifier) {
    if (query.isEmpty()) {
        Text(text = text, modifier = modifier)
    } else {
        val lowerText = text.lowercase()
        val lowerQuery = query.lowercase()
        val annotatedString = buildAnnotatedString {
            var startIndex = 0
            while (startIndex < text.length) {
                val index = lowerText.indexOf(lowerQuery, startIndex)
                if (index == -1) {
                    append(text.substring(startIndex))
                    break
                }
                append(text.substring(startIndex, index))
                withStyle(
                    SpanStyle(
                        background = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                        color = MaterialTheme.colorScheme.primary
                    )
                ) {
                    append(text.substring(index, index + query.length))
                }
                startIndex = index + query.length
            }
        }
        Text(text = annotatedString, modifier = modifier)
    }
}
