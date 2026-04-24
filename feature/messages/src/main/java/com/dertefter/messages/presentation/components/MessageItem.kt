package com.dertefter.messages.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.imageLoader
import com.dertefter.design.icons.Icons
import com.dertefter.design.theme.AppTheme
import com.dertefter.design.theme.circleShape
import com.dertefter.design.theme.cornerShape
import com.dertefter.design.theme.customColors
import com.dertefter.design.theme.rounding
import com.dertefter.design.theme.spacing
import com.dertefter.messages.R
import java.time.LocalDateTime
import kotlin.math.abs


enum class SwipeAction {
    ARCHIVE, DELETE, RESTORE
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageItem(
    modifier: Modifier = Modifier,
    name: String,
    message: String,
    avatarUrl: String?,
    date: LocalDateTime,
    isRead: Boolean = true,
    imageLoader: ImageLoader = LocalContext.current.imageLoader,
    leftAction: SwipeAction = SwipeAction.ARCHIVE,
    rightAction: SwipeAction = SwipeAction.DELETE,
    onArchive: () -> Unit = {},
    onDelete: () -> Unit = {},
    onRestore: () -> Unit = {},
    onClick: () -> Unit,
    messageId:  Long = 0,
    prev:  Long? = null,
    next:  Long? = null
) {

    val topRounding = if (prev == null) MaterialTheme.rounding.large else MaterialTheme.rounding.small

    val bottomRounding  = if (next == null) MaterialTheme.rounding.large else MaterialTheme.rounding.small

    val shape = MaterialTheme.cornerShape(
        topStart = topRounding,
        topEnd = topRounding,
        bottomStart = bottomRounding,
        bottomEnd = bottomRounding
    )


    val dismissState = remember(messageId) {
        SwipeToDismissBoxState(
            initialValue = SwipeToDismissBoxValue.Settled,
            positionalThreshold = {0f},
        )
    }

    LaunchedEffect(dismissState.settledValue) {
        when (dismissState.settledValue) {
            SwipeToDismissBoxValue.StartToEnd -> {
                when (leftAction){
                    SwipeAction.ARCHIVE -> onArchive()
                    SwipeAction.DELETE -> onDelete()
                    SwipeAction.RESTORE -> onRestore()
                }
            }
            SwipeToDismissBoxValue.EndToStart -> {
                when (rightAction){
                    SwipeAction.ARCHIVE -> onArchive()
                    SwipeAction.DELETE -> onDelete()
                    SwipeAction.RESTORE -> onRestore()
                }
            }
            else -> {}
        }
    }

    SwipeToDismissBox(
        modifier = modifier,
        state = dismissState,
        backgroundContent = {
            val density = LocalDensity.current

            val offset = try { dismissState.requireOffset() } catch (_: Exception) { 0f }

            val dynamicWidth = with(density) { abs(offset).toDp() }
            
            val thresholdPx = with(density) { 80.dp.toPx() }
            val fraction = (abs(offset) / thresholdPx).coerceIn(0f, 1f)

            val direction = dismissState.dismissDirection
            val isToEnd = direction == SwipeToDismissBoxValue.StartToEnd
            val action = if (isToEnd) leftAction else rightAction

            Box(Modifier.fillMaxSize()) {
                if (direction != SwipeToDismissBoxValue.Settled) {
                    Box(
                        modifier = Modifier
                            .clip(MaterialTheme.circleShape())
                            .align(if (isToEnd) Alignment.CenterStart else Alignment.CenterEnd)
                            .fillMaxHeight()
                            .width(dynamicWidth)
                            .background(
                                when (action){
                                    SwipeAction.ARCHIVE -> MaterialTheme.customColors.success
                                    SwipeAction.DELETE -> MaterialTheme.colorScheme.error
                                    SwipeAction.RESTORE -> MaterialTheme.colorScheme.secondary
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        CompositionLocalProvider(
                            LocalLayoutDirection provides if (isToEnd) {
                                LayoutDirection.Ltr
                            } else {
                                LayoutDirection.Rtl
                            }
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(horizontal = MaterialTheme.spacing.extraLarge),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
                            )
                            {
                                Icon(
                                    imageVector = when(action){
                                        SwipeAction.ARCHIVE -> Icons.Archive
                                        SwipeAction.DELETE -> Icons.Delete
                                        SwipeAction.RESTORE -> Icons.Unarchive
                                    },
                                    contentDescription = null,
                                    tint = when (action){
                                        SwipeAction.ARCHIVE -> MaterialTheme.customColors.onSuccess
                                        SwipeAction.DELETE -> MaterialTheme.colorScheme.onError
                                        SwipeAction.RESTORE -> MaterialTheme.colorScheme.onSecondary
                                    },
                                    modifier = Modifier
                                        .alpha(fraction)
                                )
                                Text(
                                    text = when (action){
                                        SwipeAction.ARCHIVE -> stringResource(R.string.move_to_archive)
                                        SwipeAction.DELETE -> stringResource(R.string.delete)
                                        SwipeAction.RESTORE -> stringResource(R.string.restore)
                                    },
                                    color = when (action){
                                        SwipeAction.ARCHIVE -> MaterialTheme.customColors.onSuccess
                                        SwipeAction.DELETE -> MaterialTheme.colorScheme.onError
                                        SwipeAction.RESTORE -> MaterialTheme.colorScheme.onSecondary
                                    },
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        }


                    }
                }
            }
        }
    ) {
        MessageCard(
            modifier = Modifier.fillMaxWidth(),
            name = name,
            message = message,
            avatarUrl = avatarUrl,
            date = date,
            isRead = isRead,
            imageLoader = imageLoader,
            cardShape = shape,
            onClick = onClick,
        )
    }
}

@Preview()
@Composable
fun MessageItemPreview() {
    AppTheme {
        MessageItem(
            name = "Иван Иванов",
            message = "Привет! Как дела? Ты придешь на пару?",
            avatarUrl = null,
            date = LocalDateTime.now(),
            isRead = false,
            onClick = {},
        )
    }
}
@Preview()
@Composable
fun MessageItemPreview2() {
    AppTheme {
        MessageItem(
            name = "Иван Иванов",
            message = "Привет! Как дела? Ты придешь на пару?",
            avatarUrl = null,
            date = LocalDateTime.now(),
            onClick = {},
        )
    }
}



