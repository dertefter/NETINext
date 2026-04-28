package com.dertefter.design.components.buttons

import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.dertefter.design.R
import com.dertefter.design.icons.Icons
import com.dertefter.design.theme.AppTheme
import com.dertefter.design.theme.circleShape

@Composable
fun AppNavigationIcon(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    icon: ImageVector = Icons.ArrowBack,
    contentDescription: String? = null,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
){

    FilledIconButton(
        modifier = modifier,
        colors = IconButtonDefaults.iconButtonColors().copy(
            contentColor = contentColor,
            containerColor = containerColor
        ),
        onClick = onClick,
        shape = MaterialTheme.circleShape()
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription ?: stringResource(R.string.design_back_content_desc)
        )
    }
}

@Preview(showSystemUi = false, showBackground = true)
@Composable
private fun AppNavigationIconPreview(){
    AppTheme {
        AppNavigationIcon(
            onClick = {}
        )
    }
}

@Preview(showSystemUi = false, showBackground = true)
@Composable
private fun AppNavigationIconPreview2(){
    AppTheme(
        isCut = true
    ) {
        AppNavigationIcon(
            onClick = {}
        )
    }
}
