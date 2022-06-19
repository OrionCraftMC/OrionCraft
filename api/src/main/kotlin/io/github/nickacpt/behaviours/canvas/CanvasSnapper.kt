package io.github.nickacpt.behaviours.canvas

import io.github.nickacpt.behaviours.canvas.model.CanvasLineDirection
import io.github.nickacpt.behaviours.canvas.model.CanvasLineSide
import io.github.nickacpt.behaviours.canvas.model.CanvasPoint
import io.github.nickacpt.behaviours.canvas.model.CanvasRectangle
import java.util.*
import kotlin.math.abs

class CanvasSnapper<ElementType, ColorType>(private val canvas: Canvas<ElementType, ColorType>) {

	private var cachedSnapLines: Map<CanvasLineDirection, Set<Float>>? = null

	private val mouseOffsetSnapState = mutableMapOf<CanvasLineDirection, Float>()

	private fun computeSnapLines(): Map<CanvasLineDirection, Set<Float>> {
		val rectangles = canvas.abstraction.elements
			.asSequence()
			.filter { !canvas.state.selectedElements.contains(it) }
			.map { with(canvas.abstraction) { it.rectangle } }
			.toMutableList()

		// Add canvas bounds to the list of rectangles so that we can snap to the canvas bounds too
		rectangles.add(canvas.safeZoneRectangle)

		return rectangles
			.asSequence()
			.flatMap { it.sides() }
			.groupBy { it.direction }
			.mapValues { it.value.map { v -> v.value }.toSet() }
			.toMap()
	}

	fun notifyStateChange(newAction: CanvasAction = CanvasAction.NONE) {
		if (newAction == CanvasAction.NONE) mouseOffsetSnapState.clear()
		cachedSnapLines = computeSnapLines()
	}

	fun renderSnapLines() {
	}

	private fun computeLine(direction: CanvasLineDirection, value: Float, canvasRect: CanvasRectangle): Pair<CanvasPoint, CanvasPoint> {

		val p1 = if (direction == CanvasLineDirection.HORIZONTAL)
			CanvasPoint(0f, value) else CanvasPoint(value, 0f)
		val p2 = if (direction == CanvasLineDirection.HORIZONTAL)
			CanvasPoint(canvasRect.width, value) else CanvasPoint(value, canvasRect.height)

		return Pair(p1, p2)
	}

	fun snap(mousePosition: CanvasPoint, elementRect: CanvasRectangle, outPoint: CanvasPoint) {
		val canvasRect = canvas.abstraction.rectangle
		val sides = elementRect.sides()

		val snappedDirections = EnumSet.noneOf(CanvasLineDirection::class.java)
		for ((sidePos, direction, side) in sides) {
			val posProp = computePosProperty(direction)
			val snapLines = cachedSnapLines?.get(direction) ?: continue
			for (line in snapLines) {

				val sideDistToSnapLine = abs(sidePos - line)
				if (sideDistToSnapLine > canvas.config.snapDistance) continue

				var sideOffset = elementRect.getSideValue(direction, side) - elementRect.getSideValue(
					direction,
					CanvasLineSide.FIRST
				)

				val mouseOffsetFromCorner = (posProp.get(mousePosition) - posProp.get(elementRect.topLeft))
				val mouseOffsetDistFromLastSnap = (mouseOffsetFromCorner - (mouseOffsetSnapState[direction] ?: 0f))

				val isUnsnap = abs(mouseOffsetDistFromLastSnap) > canvas.config.mouseExitSnapDistance
				if (isUnsnap) {
					sideOffset -= mouseOffsetDistFromLastSnap
				}

				canvas.config.colors.selectionBackground?.let { color ->
					val (p1, p2) = computeLine(direction, line, canvasRect)
					canvas.abstraction.drawLine(p1, p2, color, canvas.config.snapLineWidth)
				}

				if (snappedDirections.contains(direction)) continue

				if (!isUnsnap) {
					snappedDirections.add(direction)
				}

				posProp.set(outPoint, line - sideOffset)

				if (!mouseOffsetSnapState.containsKey(direction))
					mouseOffsetSnapState[direction] = mouseOffsetFromCorner

				break
			}
		}
	}

	private fun computePosProperty(direction: CanvasLineDirection) =
		if (direction == CanvasLineDirection.HORIZONTAL) CanvasPoint::y else CanvasPoint::x

}
