package com.irajnajafi1988gmail.notepad.ui.components.common.textfield

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.irajnajafi1988gmail.notepad.R
import com.irajnajafi1988gmail.notepad.ui.theme.helpers.iconSize

@Composable
fun TitleTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    focusRequester: FocusRequester,
    label: String,
    fontSize: Float,
    onFocusChanged: ((Boolean) -> Unit)? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    modifier: Modifier,

    ) {
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    val firstLetter = value.text.firstOrNull { it.isLetter() }
    val isRtl = firstLetter != null && firstLetter.code in 0x0600..0x06FF

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.15f)
                    )
                ),
                shape = MaterialTheme.shapes.medium
            )
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.pencil),
            contentDescription = null,
            modifier = Modifier.size(iconSize())
        )

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .weight(1f)
                .focusRequester(focusRequester)
                .onFocusChanged { onFocusChanged?.invoke(it.isFocused) },
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = fontSize.sp,
                textDirection = if (isRtl) TextDirection.Rtl else TextDirection.Ltr,
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            interactionSource = interactionSource,
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
            maxLines = 2,
        )
    }
}