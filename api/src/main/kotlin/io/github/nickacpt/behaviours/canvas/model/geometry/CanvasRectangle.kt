package io.github.nickacpt.behaviours.canvas.model.geometry

import io.github.nickacpt.behaviours.canvas.model.geometry.line.CanvasLine
import io.github.nickacpt.behaviours.canvas.model.geometry.line.CanvasLineDirection
import io.github.nickacpt.behaviours.canvas.model.geometry.line.CanvasLineSide

data class CanvasRectangle(val topLeft: CanvasPoint, val bottomRight: CanvasPoint) {
	val top get() = topLeft.y

	val left get() = topLeft.x

	val right get() = bottomRight.x

	val bottom get() = bottomRight.y

	val width get() = right - left

	val height get() = bottom - top

	fun contains(point: CanvasPoint): Boolean {
		return point.x in left..right && point.y in top..bottom
	}

	fun expand(amount: Float): CanvasRectangle {
		return CanvasRectangle(
			topLeft = CanvasPoint(left - amount, top - amount),
			bottomRight = CanvasPoint(right + amount, bottom + amount)
		)
	}

	val topRight: CanvasPoint get() = CanvasPoint(right, top)

	val bottomLeft: CanvasPoint get() = CanvasPoint(left, bottom)

	fun corners() = listOf(
		topLeft,
		topRight,
		bottomLeft,
		bottomRight
	)

	fun sides() = listOf(
		CanvasLine(top, CanvasLineDirection.HORIZONTAL, CanvasLineSide.FIRST),
		CanvasLine(bottom, CanvasLineDirection.HORIZONTAL, CanvasLineSide.SECOND),
		CanvasLine(left, CanvasLineDirection.VERTICAL, CanvasLineSide.FIRST),
		CanvasLine(right, CanvasLineDirection.VERTICAL, CanvasLineSide.SECOND)
	)

	fun getSideValue(direction: CanvasLineDirection, side: CanvasLineSide): Float {
		return when (direction) {
			CanvasLineDirection.HORIZONTAL -> when (side) {
				CanvasLineSide.FIRST -> top
				CanvasLineSide.SECOND -> bottom
			}

			CanvasLineDirection.VERTICAL -> when (side) {
				CanvasLineSide.FIRST -> left
				CanvasLineSide.SECOND -> right
			}
		}
	}
}
