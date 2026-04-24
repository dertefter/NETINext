package com.dertefter.profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalGridApi
import androidx.compose.foundation.layout.Grid
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dertefter.design.icons.Icons
import com.dertefter.design.theme.AppTheme
import com.dertefter.design.theme.spacing


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CardItem(modifier: Modifier = Modifier,
             text: String,
             icon: ImageVector,
             iconContainerShape: Shape
){

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp),
        shape = MaterialTheme.shapes.small
    ) {
        Box(
            modifier = modifier
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .fillMaxWidth()
                .height(100.dp)
        )
        {

            Icon(
                modifier = Modifier
                    .padding(MaterialTheme.spacing.medium)
                    .clip(iconContainerShape)
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                    .padding(MaterialTheme.spacing.medium),
                tint = MaterialTheme.colorScheme.secondary,
                imageVector = icon,
                contentDescription = "",
            )

            Text(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(
                        vertical = MaterialTheme.spacing.medium,
                        horizontal = MaterialTheme.spacing.large
                    )
                    .fillMaxWidth(),
                text = text,
                textAlign = TextAlign.Start,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.titleSmall,

                )
        }
    }



}


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun Scores(
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
    ) {

        Row(
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
        ){
            CardItem(
                modifier = Modifier.weight(1f),
                text = "Результаты сессии",
                icon = Icons.SchoolFilled,
                iconContainerShape = MaterialShapes.Pill.toShape()
            )

            CardItem(
                modifier = Modifier.weight(1f),
                text = "Контрольные недели",
                icon = Icons.DataCheckFilled,
                iconContainerShape = MaterialShapes.Pentagon.toShape()
            )
        }


        Row(
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
        ){
            CardItem(
                modifier = Modifier.weight(1f),
                text = "Документы",
                icon = Icons.DocsFilled,
                iconContainerShape = MaterialShapes.Arch.toShape()
            )

            CardItem(
                modifier = Modifier.weight(1f),
                text = "Стипендии и выплаты",
                icon = Icons.PaymentsFilled,
                iconContainerShape = MaterialShapes.Slanted.toShape()
            )
        }

    }
}


@Preview(locale = "ru")
@Composable
fun ScoresPreview(){
    Scores()
}