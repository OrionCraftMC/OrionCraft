package io.github.nickacpt.behaviours.canvas.abstractions

import io.github.nickacpt.behaviours.canvas.model.CanvasPoint
import io.github.nickacpt.behaviours.canvas.model.CanvasRectangle
import java.util.UUID

interface CanvasElementAbstraction<ElementType> {

	val ElementType.elementId: UUID

	val ElementType.rectangle: CanvasRectangle

	fun ElementType.moveTo(point: CanvasPoint)

}
