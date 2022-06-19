package io.github.nickacpt.behaviours.canvas

import io.github.nickacpt.behaviours.canvas.abstractions.CanvasAbstraction
import io.github.nickacpt.behaviours.canvas.config.CanvasColourStyle
import io.github.nickacpt.behaviours.canvas.config.CanvasConfig
import io.github.nickacpt.behaviours.canvas.model.CanvasPoint
import io.github.nickacpt.behaviours.canvas.model.CanvasRectangle

class Canvas<ElementType, ColorType>(
	private val abstraction: CanvasAbstraction<ElementType, ColorType>,
	private val config: CanvasConfig<ColorType>
) {
	private val state = CanvasState<ElementType>()

	private fun getColorToUse(element: ElementType, rect: CanvasRectangle, mousePosition: CanvasPoint, bgColor: CanvasColourStyle<ColorType>): ColorType {
		val containsMouse = rect.contains(mousePosition)
		return if (state.selectedElements.contains(element)) {
			bgColor.active
		} else if (containsMouse && state.mouseDown) {
			bgColor.mouseDown
		} else if (containsMouse) {
			bgColor.hover
		} else {
			bgColor.normal
		} ?: bgColor.normal
	}

	fun onRender(mousePosition: CanvasPoint) {
		renderSafeZone()
		renderElementDecorators(abstraction.elements, mousePosition)

		if (state.mouseDown) moveSelectedElements(mousePosition)

		state.lastRenderMousePosition.apply {
			x = mousePosition.x
			y = mousePosition.y
		}
	}

	private fun renderSafeZone() {
		val color = config.colours.background?.active ?: return
		val safeZone = abstraction.rectangle.expand(-config.safeZoneSize)

		abstraction.drawRectangle(safeZone, color, true, config.safeZoneBorderWidth)
	}

	private fun moveSelectedElements(mousePosition: CanvasPoint) {
		val delta = mousePosition - state.lastRenderMousePosition

		state.selectedElements.forEach {
			with(abstraction) {
				val elementRect = it.rectangle
				val canvasRect = abstraction.rectangle.expand(-config.safeZoneSize)
				val point = elementRect.topLeft + delta

				point.apply {
					x = x.coerceIn(canvasRect.left, canvasRect.right - elementRect.width)
					y = y.coerceIn(canvasRect.top, canvasRect.bottom - elementRect.height)
				}

				it.moveTo(point)
			}
		}
	}

	private fun renderElementDecorators(canvasElements: Collection<ElementType>, mousePosition: CanvasPoint) {
		for (element in canvasElements) {
			val rect = with(abstraction) { element.rectangle }
			val backgroundColor = getColorToUse(element, rect, mousePosition, config.colours.background ?: break)
			abstraction.drawRectangle(rect, backgroundColor, false, 0f)

			// Draw border for element
			val borderColor = getColorToUse(element, rect, mousePosition, config.colours.border ?: break)
			abstraction.drawRectangle(rect, borderColor, true, config.borderWidth)
		}
	}

	fun onMouseDown(mousePosition: CanvasPoint) {
		state.mouseDown = true

		var holdingMultiSelectKey = false /* TODO: detect if holding control */
		if (!holdingMultiSelectKey) {
			state.selectedElements.clear()
		}

		for (element in abstraction.elements) {
			if (with(abstraction) { element.rectangle }.contains(mousePosition)) {
				state.selectedElements.add(element)
				return
			}
		}

		state.selectedElements.clear()
	}

	fun onMouseUp(mousePosition: CanvasPoint) {
		state.mouseDown = false

		if (state.selectedElements.size == 1) {
			state.selectedElements.clear()
		}

	}
}
