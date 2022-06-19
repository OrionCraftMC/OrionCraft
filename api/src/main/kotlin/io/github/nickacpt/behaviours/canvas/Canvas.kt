package io.github.nickacpt.behaviours.canvas

import io.github.nickacpt.behaviours.canvas.abstractions.CanvasAbstraction
import io.github.nickacpt.behaviours.canvas.config.CanvasConfig
import io.github.nickacpt.behaviours.canvas.model.CanvasAction
import io.github.nickacpt.behaviours.canvas.model.CanvasState
import io.github.nickacpt.behaviours.canvas.model.geometry.CanvasPoint
import io.github.nickacpt.behaviours.canvas.model.geometry.CanvasRectangle

class Canvas<ElementType, ColorType>(
	internal val abstraction: CanvasAbstraction<ElementType, ColorType>,
	internal val config: CanvasConfig<ColorType>
) {
	internal val state = CanvasState<ElementType> { snapper.notifyStateChange(it) }
	private val renderer = CanvasRenderer(this)
	private val snapper = CanvasSnapper(this)

	val safeZoneRectangle
		get() = abstraction.rectangle.expand(-config.safeZoneSize)

	fun onRender(mousePosition: CanvasPoint) {
		renderer.renderBackground(mousePosition)
		handleAction(mousePosition)

		state.lastRenderMousePosition.apply {
			x = mousePosition.x
			y = mousePosition.y
		}
	}

	private fun handleAction(mousePosition: CanvasPoint) {
		if (state.currentAction == CanvasAction.ELEMENT_MOVE) {
			moveElements(mousePosition)
		} else if (state.currentAction == CanvasAction.ELEMENT_SELECT) {
			selectElements(mousePosition)
		}
	}

	private fun selectElements(mousePosition: CanvasPoint) {
		val mouseDownPos = state.mouseDownPosition ?: return
		val topLeftX = minOf(mouseDownPos.x, mousePosition.x)
		val topLeftY = minOf(mouseDownPos.y, mousePosition.y)

		val bottomRightX = maxOf(mouseDownPos.x, mousePosition.x)
		val bottomRightY = maxOf(mouseDownPos.y, mousePosition.y)

		val selectionRectangle =
			CanvasRectangle(CanvasPoint(topLeftX, topLeftY), CanvasPoint(bottomRightX, bottomRightY))

		config.colors.selectionBackground?.let { abstraction.drawRectangle(selectionRectangle, it, false, 0f) }
		config.colors.selectionBorder?.let { abstraction.drawRectangle(selectionRectangle, it, true, 1f) }

		for (element in abstraction.elements) {
			val corners = with(abstraction) { element.rectangle }.corners()

			if (corners.any { selectionRectangle.contains(it) }) {
				state.selectedElements.add(element)
			} else {
				// TODO: only restart if not pressing control
				state.selectedElements.remove(element)
			}
		}
	}


	private fun moveElements(mousePosition: CanvasPoint) {
		if (state.selectedElements.isEmpty()) return
		val delta = mousePosition - state.lastRenderMousePosition

		state.selectedElements.forEach {
			with(abstraction) {
				val elementRect = it.rectangle
				val canvasRect = safeZoneRectangle
				val newRectangle = elementRect.copy(
					topLeft = elementRect.topLeft + delta,
					bottomRight = elementRect.bottomRight + delta
				)
				val point = newRectangle.topLeft

				if (state.selectedElements.size == 1) snapper.snap(mousePosition, newRectangle, point)

				point.apply {
					x = x.coerceIn(canvasRect.left, canvasRect.right - elementRect.width)
					y = y.coerceIn(canvasRect.top, canvasRect.bottom - elementRect.height)
				}

				it.moveTo(point)
			}
		}

		snapper.notifyStateChange(CanvasAction.ELEMENT_MOVE)
	}

	fun onMouseDown(mousePosition: CanvasPoint) {
		state.mouseDown = true
		state.mouseDownPosition = mousePosition.copy()

		snapper.notifyStateChange(CanvasAction.NONE)
		computeCurrentAction(mousePosition)

		for (element in abstraction.elements) {
			val rect = with(abstraction) { element.rectangle }
			if (rect.contains(mousePosition)) {

				var holdingMultiSelectKey = false /* TODO: detect if holding control */
				if (!holdingMultiSelectKey &&
					state.currentAction == CanvasAction.ELEMENT_MOVE &&
					state.selectedElements.isNotEmpty() &&
					!state.selectedElements.contains(element)
				) {
					state.selectedElements.clear()
				}

				state.selectedElements.add(element)

				return
			}
		}

		state.selectedElements.clear()
	}

	fun onMouseUp(mousePosition: CanvasPoint) {
		state.mouseDown = false
		state.mouseDownPosition = null

		if (state.currentAction == CanvasAction.ELEMENT_MOVE && state.selectedElements.size == 1) {
			state.selectedElements.clear()
		}

		computeCurrentAction(mousePosition)
	}

	private fun computeCurrentAction(mousePosition: CanvasPoint) {
		state.currentAction = CanvasAction.NONE

		val overAnyElement = abstraction.elements.any { with(abstraction) { it.rectangle }.contains(mousePosition) }
		if (!state.mouseDown) return

		if (overAnyElement) {
			state.currentAction = CanvasAction.ELEMENT_MOVE
		} else {
			state.currentAction = CanvasAction.ELEMENT_SELECT
		}
	}
}
