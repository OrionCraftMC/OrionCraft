package io.github.nickacpt.behaviours.canvas.abstractions

import io.github.nickacpt.behaviours.canvas.model.geometry.CanvasPoint
import io.github.nickacpt.behaviours.canvas.model.geometry.CanvasRectangle

interface CanvasElementAbstraction<ElementType> {

	val ElementType.rectangle: CanvasRectangle

	fun ElementType.moveTo(point: CanvasPoint)

}
