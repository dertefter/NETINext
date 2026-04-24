package com.dertefter.design.components.text_fields

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dertefter.design.icons.Icons
import com.dertefter.design.theme.AppTheme

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TextFieldItem(
    modifier: Modifier = Modifier,
    value: String,
    enabled: Boolean = true,
    hint: String,
    icon: ImageVector? = null,
    onValueChange: (String) -> Unit = {},
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    maxSymbols: Int = Int.MAX_VALUE,
    singleLine: Boolean = true,
    trailingIcon: ImageVector? = null,
    onTrailingIconClick: (() -> Unit)? = null
) {
    var textFieldValueState =  (TextFieldValue(text = value, selection = TextRange(value.length)))

    val containerColor = if (isError) {
        MaterialTheme.colorScheme.errorContainer
    } else if (!enabled) {
        MaterialTheme.colorScheme.surfaceVariant
    }else {
        MaterialTheme.colorScheme.surfaceContainer
    }

    val contentColor = if (isError) {
        MaterialTheme.colorScheme.error
    } else if (!enabled){
        MaterialTheme.colorScheme.onSurfaceVariant
    } else {
        MaterialTheme.colorScheme.primary
    }

    LaunchedEffect(value) {
        if (value != textFieldValueState.text) {
            textFieldValueState = textFieldValueState.copy(
                text = value,
                selection = TextRange(value.length)
            )
        }
    }

    Column(
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .background(containerColor)
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        AnimatedVisibility(
            visible = value.isNotEmpty(),
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                text = hint,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.labelLargeEmphasized,
                color = contentColor
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        )
        {
            icon?.let{
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = contentColor
                )
                Spacer(modifier = Modifier.width(16.dp))
            }

            Box(
                modifier = Modifier.weight(1f)
            ) {
                if (value.isEmpty()) {
                    Text(
                        text = hint,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                            alpha = 0.7f
                        )
                    )
                }
                BasicTextField(
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = singleLine,
                    enabled = enabled,
                    value = textFieldValueState,
                    onValueChange = { newValue ->
                        if (newValue.text.length <= maxSymbols){
                            textFieldValueState = newValue
                            onValueChange(newValue.text)
                        }
                    },
                    textStyle = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    cursorBrush = SolidColor(contentColor),
                    visualTransformation = visualTransformation,
                    keyboardOptions = keyboardOptions
                )
            }
            if (trailingIcon != null) {
                IconButton(
                    modifier = Modifier.size(24.dp),
                    onClick = onTrailingIconClick ?: {}
                ) {
                    Icon(
                        imageVector = trailingIcon,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TextFieldItemPreview() {
    AppTheme {
        TextFieldItem(
            value = "",
            hint = "Full Name",
            icon = Icons.User,
            onValueChange = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TextFieldItemWithValuePreview() {
    AppTheme {
        TextFieldItem(
            value = "Ivan Ivanov",
            hint = "Full Name",
            icon = Icons.User,
            onValueChange = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TextFieldItemWithValuePreview2() {
    AppTheme {
        TextFieldItem(
            value = "Ivan Ivanov",
            hint = "Full Name",
            onValueChange = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TextFieldItemWithErrorPreview() {
    AppTheme {
        TextFieldItem(
            value = "Invalid Inputя",
            hint = "Email",
            icon = Icons.User,
            isError = true
        )
    }
}
