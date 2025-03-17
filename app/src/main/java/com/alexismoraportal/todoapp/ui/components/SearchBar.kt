package com.alexismoraportal.todoapp.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.alexismoraportal.todoapp.ui.theme.Primary
import com.alexismoraportal.todoapp.ui.theme.Secondary
import com.alexismoraportal.todoapp.ui.theme.delete

/**
 * SearchBar
 *
 * A composable that shows an animated search text field.
 * When visible, it slides in from the bottom and fades in.
 * The entered query is passed back via [onQueryChanged].
 *
 * A trailing "clear" icon appears on the right when there is text,
 * and tapping it clears the text. Additionally, input is capped to 20 characters.
 *
 * @param visible Controls whether the search bar is shown.
 * @param query The current search query.
 * @param onQueryChanged Callback to update the search query.
 * @param modifier Optional Modifier to be applied to the text field.
 */
@Composable
fun SearchBar(
    visible: Boolean,
    query: String,
    onQueryChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = { newValue ->
                // Limit the input to 20 characters
                if (newValue.length <= 20) {
                    onQueryChanged(newValue)
                } else {
                    onQueryChanged(newValue.take(20))
                }
            },
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            label = { Text("Search tasks") },
            singleLine = true,
            trailingIcon = {
                // Show clear icon only when there is text.
                if (query.isNotEmpty()) {
                    IconButton(onClick = { onQueryChanged("") }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear search text",
                            modifier = Modifier.size(16.dp),
                            tint = delete
                        )
                    }
                }
            },
            textStyle = MaterialTheme.typography.bodyMedium,
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedIndicatorColor = Primary,
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                focusedLabelColor = Primary,
                unfocusedIndicatorColor = Secondary,
                unfocusedLabelColor = Color.LightGray
            )
        )
    }
}
