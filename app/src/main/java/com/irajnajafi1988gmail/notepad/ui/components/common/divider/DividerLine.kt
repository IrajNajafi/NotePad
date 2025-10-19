package com.irajnajafi1988gmail.notepad.ui.components.common.divider
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.irajnajafi1988gmail.notepad.ui.theme.helpers.itemSpacing

@Composable
fun DividerLine() {
    HorizontalDivider(
        modifier = Modifier.fillMaxWidth(),
        thickness = itemSpacing(1.dp),
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
    )
}
