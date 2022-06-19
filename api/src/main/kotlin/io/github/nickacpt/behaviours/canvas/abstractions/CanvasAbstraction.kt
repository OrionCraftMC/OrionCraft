package io.github.nickacpt.behaviours.canvas.abstractions

import io.github.nickacpt.behaviours.canvas.model.geometry.CanvasPoint
import io.github.nickacpt.behaviours.canvas.model.geometry.CanvasRectangle
import java.util.*

interface CanvasAbstraction<ElementType, ColorType> : CanvasElementAbstraction<ElementType> {

	val rectangle: CanvasRectangle

	val elements: Collection<ElementType>

	fun getElementById(id: UUID): ElementType

	fun drawLine(p1: CanvasPoint, p2: CanvasPoint, color: ColorType, lineWidth: Float)

	fun drawRectangle(rectangle: CanvasRectangle, color: ColorType, hollow: Boolean, lineWidth: Float)

}
