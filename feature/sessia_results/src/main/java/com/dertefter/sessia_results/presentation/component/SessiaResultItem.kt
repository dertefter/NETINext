package com.dertefter.sessia_results.presentation.component

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dertefter.data.dto.sessia_results.SessiaResultDto
import com.dertefter.design.theme.AppTheme
import com.dertefter.design.theme.cornerShape
import com.dertefter.design.theme.customColors
import com.dertefter.design.theme.rounding
import com.dertefter.design.theme.spacing
import com.dertefter.sessia_results.R

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SessiaResultItem(
    sessiaResult: SessiaResultDto,
    modifier: Modifier = Modifier,
    isTop: Boolean = true,
    isBottom: Boolean = true,
) {

    val topRounding = if (isTop) MaterialTheme.rounding.largeIncreased else MaterialTheme.rounding.small

    val bottomRounding  = if (isBottom) MaterialTheme.rounding.largeIncreased else MaterialTheme.rounding.small

    val shape = MaterialTheme.cornerShape(
        topStart = topRounding,
        topEnd = topRounding,
        bottomStart = bottomRounding,
        bottomEnd = bottomRounding
    )

    var isExpanded by remember { mutableStateOf(false) }

    val ectsShapeAngle by animateIntAsState(
        targetValue = if (isExpanded) 0 else 46,
        animationSpec = MaterialTheme.motionScheme.defaultSpatialSpec()
    )

    val nameFontSize by animateFloatAsState(
        targetValue = if (isExpanded) MaterialTheme.typography.titleLarge.fontSize.value else MaterialTheme.typography.titleMedium.fontSize.value
    )

    val ectsFontSize by animateFloatAsState(
        targetValue = if (isExpanded) MaterialTheme.typography.titleLarge.fontSize.value else MaterialTheme.typography.titleSmall.fontSize.value
    )

    val typeNameFontSize by animateFloatAsState(
        targetValue = if (isExpanded) MaterialTheme.typography.bodyLarge.fontSize.value else MaterialTheme.typography.labelLargeEmphasized.fontSize.value
    )

    val ectsShapeSize by animateDpAsState(
        targetValue = if (isExpanded) 68.dp else 52.dp
    )


    Row(
        modifier = modifier
            .clip(shape)
            .animateContentSize()
            .clickable(
                onClick = {
                    isExpanded = !isExpanded
                }
            )
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(MaterialTheme.spacing.large)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
        verticalAlignment = Alignment.CenterVertically
    ){
        Column(
            Modifier
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
        ) {
            sessiaResult.name?.let { name ->
                Text(
                    text = name,
                    fontSize = nameFontSize.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            sessiaResult.typeName?.let { typeName ->
                Text(
                    text = typeName.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(
                            LocalLocale.current.platformLocale
                        ) else it.toString()
                    },
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.labelLargeEmphasized,
                    fontSize = typeNameFontSize.sp
                )
            }


            AnimatedVisibility(
                isExpanded,
                modifier = Modifier.padding(top = MaterialTheme.spacing.medium)
            ) {

                Column(
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
                ) {
                    sessiaResult.score?.let { score ->
                        if (!((score == 0 )
                                    &&  (sessiaResult.markName ?: "").contains("зач", ignoreCase = true))){
                            ValueItem(
                                title = stringResource(R.string.sessia_results_score),
                                value = score.toString()
                            )
                        }

                    }

                    sessiaResult.markName?.let { markName ->
                        ValueItem(
                            title = stringResource(R.string.sessia_results_mark),
                            value = markName
                        )

                    }
                }


            }


        }

        val progress = if (sessiaResult.score != null && sessiaResult.score != 0){
            ((sessiaResult.score!!.toFloat() - 20f) / 80f).coerceIn(0f, 1f)
        } else if ((sessiaResult.markName?: "").contains("зач", ignoreCase = true)) {
            1f
        } else {
            0f
        }

        val colorSuccess = MaterialTheme.customColors.success
        val colorOnSuccess = MaterialTheme.customColors.onSuccess
        val colorError = MaterialTheme.colorScheme.error
        val colorOnError = MaterialTheme.colorScheme.onError

        val bgColor = lerp(colorError, colorSuccess, progress)
        val onBgColor = lerp(colorOnError, colorOnSuccess, progress)

            Box(
            modifier = Modifier
                .clip(MaterialShapes.Cookie7Sided.toShape(ectsShapeAngle))
                .background(bgColor)
                .size(ectsShapeSize)
        ){

            Text(
                modifier = Modifier
                    .padding(horizontal = MaterialTheme.spacing.medium)
                    .align(Alignment.Center),
                text = sessiaResult.europeanMarkString ?: sessiaResult.markName ?: "",
                color = onBgColor,
                style = MaterialTheme.typography.titleSmallEmphasized,
                fontSize = ectsFontSize.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }



}

@Preview(showBackground = false)
@Composable
fun SessiaResultItemPreview() {
    AppTheme {
        Column() {
            SessiaResultItem(
                sessiaResult = SessiaResultDto(
                    name = "Математический анализ",
                    markName = "зачтено",
                    score = 50,
                    typeName = "зачёт",
                    europeanMarkString = null,
                    semester = 1
                )
            )
            SessiaResultItem(
                sessiaResult = SessiaResultDto(
                    name = "Математический анализ",
                    markName = "зачтено",
                    score = 100,
                    typeName = "зачёт",
                    europeanMarkString = null,
                    semester = 1
                )
            )
        }

    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun SessiaResultItemPreview2() {
    AppTheme {
        Column() {
            SessiaResultItem(
                sessiaResult = SessiaResultDto(
                    name = "Математический анализ",
                    markName = "зачтено",
                    score = 70,
                    typeName = "зачёт",
                    europeanMarkString = null,
                    semester = 1
                )
            )
            SessiaResultItem(
                sessiaResult = SessiaResultDto(
                    name = "Математический анализ",
                    markName = "зачтено",
                    score = 100,
                    typeName = "зачёт",
                    europeanMarkString = null,
                    semester = 1
                )
            )
        }

    }
}