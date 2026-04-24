package com.dertefter.design.components.loading

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.LoadingIndicatorDefaults
import androidx.compose.material3.MaterialShapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Matrix
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.TransformResult


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AppLoadingIndicator(
    modifier: Modifier = Modifier,
    color: Color = LoadingIndicatorDefaults.indicatorColor
){
    val polygons: List<RoundedPolygon> = listOf(
        MaterialShapes.SoftBurst,
        MaterialShapes.Oval,
        MaterialShapes.Sunny,
        MaterialShapes.Cookie9Sided,
        MaterialShapes.Pill,
        MaterialShapes.Cookie6Sided,
        MaterialShapes.Square,
        MaterialShapes.Clover4Leaf,

    )

    LoadingIndicator(
        modifier = modifier,
        color = color,
        polygons = polygons
    )
}


fun RoundedPolygon.transformed(matrix: Matrix): RoundedPolygon = transformed { x, y ->
    val transformedPoint = matrix.map(Offset(x, y))
    TransformResult(transformedPoint.x, transformedPoint.y)
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AppLoadingIndicator(
    modifier: Modifier = Modifier,
    progress: () -> Float,
    color: Color = LoadingIndicatorDefaults.indicatorColor
){
    val polygons: List<RoundedPolygon> = listOf(
        MaterialShapes.Circle.transformed(Matrix().apply { rotateZ(360f / 20) }),
        MaterialShapes.SoftBurst,
        )

    LoadingIndicator(
        modifier = modifier,
        color = color,
        polygons = polygons,
        progress = progress
    )
}