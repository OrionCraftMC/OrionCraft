package io.github.nickacpt.behaviours.canvas

import io.github.nickacpt.behaviours.canvas.config.CanvasColourStyle
import io.github.nickacpt.behaviours.canvas.model.CanvasPoint
import io.github.nickacpt.behaviours.canvas.model.CanvasRectangle

class CanvasRenderer<ElementType, ColorType>(val canvas: Canvas<ElementType, ColorType>) {

	private fun getBackgroundColorToUse(element: ElementType, rect: CanvasRectangle, mousePosition: CanvasPoint, bgColor: CanvasColourStyle<ColorType>): ColorType {
		val containsMouse = rect.contains(mousePosition)
		return if (canvas.state.selectedElements.contains(element)) {
			bgColor.active
		} else if (containsMouse && canvas.state.mouseDown) {
			bgColor.mouseDown
		} else if (containsMouse) {
			bgColor.hover
		} else {
			bgColor.normal
		} ?: bgColor.normal
	}

	private fun renderSafeZone() = with(canvas) {
		val color = config.colours.selectionBackground ?: return
		val safeZone = abstraction.rectangle.expand(-config.safeZoneSize)

		abstraction.drawRectangle(safeZone, color, true, config.safeZoneBorderWidth)
	}

	private fun renderElementDecorators(canvasElements: Collection<ElementType>, mousePosition: CanvasPoint) =
		with(canvas) {
			for (element in canvasElements) {
				val rect = with(abstraction) { element.rectangle }
				val backgroundColor =
					getBackgroundColorToUse(element, rect, mousePosition, config.colours.elementBackground ?: break)
				abstraction.drawRectangle(rect, backgroundColor, false, 0f)

				// Draw border for element
				val borderColor =
					getBackgroundColorToUse(element, rect, mousePosition, config.colours.elementBorder ?: break)
				abstraction.drawRectangle(rect, borderColor, true, config.borderWidth)
			}
		}


	fun renderBackground(mousePosition: CanvasPoint) = with(canvas) {
		renderSafeZone()
		renderElementDecorators(abstraction.elements, mousePosition)
	}

}
