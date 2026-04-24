package com.dertefter.design.shape

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.toPath
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.graphics.shapes.Morph

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
class MorphPolygonShape(
    private val morph: Morph,
    private val percentage: Float = 0f,
    private val rotation: Float = 0f
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val matrix = Matrix()
        matrix.translate(size.width / 2f, size.height / 2f)
        matrix.rotateZ(rotation)
        val scale = minOf(size.width, size.height)
        matrix.scale(scale, scale)
        matrix.translate(-0.5f, -0.5f)

        val path = morph.toPath(progress = percentage)
        path.transform(matrix)
        return Outline.Generic(path)
    }
}
