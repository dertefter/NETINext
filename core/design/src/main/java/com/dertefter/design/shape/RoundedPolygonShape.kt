package com.dertefter.design.shape

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.graphics.shapes.RoundedPolygon

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
class RoundedPolygonShape(
    private val polygon: RoundedPolygon,
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
        val scale = minOf(size.width, size.height) / 1.414f
        matrix.scale(scale, scale)
        matrix.translate(-0.5f, -0.5f)

        val path = polygon.toComposePath()
        path.transform(matrix)

        return Outline.Generic(path)
    }

    private fun RoundedPolygon.toComposePath(path: Path = Path()): Path {
        path.rewind()
        var first = true
        cubics.forEach { cubic ->
            if (first) {
                path.moveTo(cubic.anchor0X, cubic.anchor0Y)
                first = false
            }
            path.cubicTo(
                cubic.control0X, cubic.control0Y,
                cubic.control1X, cubic.control1Y,
                cubic.anchor1X, cubic.anchor1Y
            )
        }
        path.close()
        return path
    }


}
