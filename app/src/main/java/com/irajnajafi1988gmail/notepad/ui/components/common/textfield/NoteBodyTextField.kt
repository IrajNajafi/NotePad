package com.irajnajafi1988gmail.notepad.ui.components.common.textfield

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NoteBodyTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    focusRequester: FocusRequester? = null,
    label: String,
    fontSize: Float,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onFocusChanged: ((Boolean) -> Unit)? = null,
    modifier: Modifier
) {
    val firstLetter = value.text.firstOrNull { it.isLetter() }
    val isRtl = firstLetter != null && firstLetter.code in 0x0600..0x06FF

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 400.dp)
            .then(if (focusRequester != null) Modifier.focusRequester(focusRequester) else Modifier)
            .onFocusChanged { onFocusChanged?.invoke(it.isFocused) },
        textStyle = TextStyle(
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = fontSize.sp,
            textDirection = if (isRtl) TextDirection.Rtl else TextDirection.Ltr,

            ),
        interactionSource = interactionSource,

        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),

        decorationBox = { innerTextField ->
            if (value.text.isEmpty()) {
                Text(
                    text = label,
                    fontSize = fontSize.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
                )
            }
            innerTextField()
        },
        maxLines = Int.MAX_VALUE
    )
}
