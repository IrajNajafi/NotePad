package com.irajnajafi1988gmail.notepad.ui.components.common.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.irajnajafi1988gmail.notepad.ui.components.common.divider.DividerLine
import com.irajnajafi1988gmail.notepad.ui.theme.helpers.cornerRadius
import com.irajnajafi1988gmail.notepad.ui.theme.helpers.fontScale
import com.irajnajafi1988gmail.notepad.ui.theme.helpers.scaledPadding
import com.irajnajafi1988gmail.notepad.ui.theme.helpers.scaledTextSize
import com.irajnajafi1988gmail.notepad.ui.theme.typography.LocalTypography

@Composable
fun ItemDialog(
    onDismiss: () -> Unit,
    showDialog: Boolean,
    items: List<Int>,
    onClickItem: (Int) -> Unit
) {

    if (showDialog) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = true
            )

        ) {
            Surface(
                shape = RoundedCornerShape(cornerRadius()),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp,
                modifier = Modifier
                    .width(250.dp * fontScale())
                    .wrapContentHeight()
            ) {
                Column(
                    modifier = Modifier
                        .padding(vertical = scaledPadding())
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items.forEachIndexed { index, item ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onClickItem(item) }
                                .padding(vertical = scaledPadding()),
                            contentAlignment = Alignment.Center)
                        {
                            Text(
                                text = stringResource(id = item),
                                fontSize = scaledTextSize(LocalTypography.current.bodyMedium.fontSize),
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        if (index < items.lastIndex) {
                            DividerLine()
                        }
                    }
                    }
                }
            }
        }
  }


