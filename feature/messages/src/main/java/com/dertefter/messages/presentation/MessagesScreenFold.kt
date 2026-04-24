package com.dertefter.messages.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.dertefter.design.components.adaptive.PanelsLayout
import com.dertefter.messages_detail.MessagesDetailRoute
import com.dertefter.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MessagesScreenFold(
    onEvent: (Event) -> Unit,
    uiState: UiState,
) {

    var currentRoute by remember { mutableStateOf<Routes.MessagesDetail?>(null) }

    PanelsLayout(
        modifier = Modifier.fillMaxSize(),
        contentLeft = {
            MessagesScreenPhone(
                onEvent = { event ->
                    if (event is Event.OnOpenMessageDetail) {
                        currentRoute = Routes.MessagesDetail(
                            idMessage = event.idMessage,
                            idStudent = event.idStudent
                        )
                    } else {
                        onEvent(event)
                    }
                },
                uiState = uiState
            )
        },
        contentRight = {
            currentRoute?.let { MessagesDetailRoute(it.idMessage, it.idStudent, isPanel = true) }
        }
    )

}

@Preview(showBackground = true)
@Composable
private fun MessagesScreenFoldPreview() {


}
