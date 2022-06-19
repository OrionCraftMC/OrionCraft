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

	private fun getColorToUse(rect: CanvasRectangle, mousePosition: CanvasPoint, mouseDown: Boolean, bgColor: CanvasColourStyle<ColorType>): ColorType {
		val containsMouse = rect.contains(mousePosition)
		return if (containsMouse && mouseDown) {
			bgColor.mouseDown
		} else if (containsMouse) {
			bgColor.hover
		} else {
			bgColor.normal
		} ?: bgColor.normal
	}

	fun onRender(mousePosition: CanvasPoint) {
		val canvasElements = abstraction.elements

		for (element in canvasElements) {
			val rect = with(abstraction) { element.rectangle }
			val backgroundColor = getColorToUse(rect, mousePosition, /* todo */ false, config.colours.background ?: break)
			abstraction.drawRectangle(rect, backgroundColor, false, 0f)

			// Draw border for element
			val borderColor = getColorToUse(rect, mousePosition, /* todo */ false, config.colours.border ?: break)
			abstraction.drawRectangle(rect, borderColor, true, config.borderWidth)
		}
	}
}
