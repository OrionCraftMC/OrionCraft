package io.github.nickacpt.behaviours.canvas.abstractions

import io.github.nickacpt.behaviours.canvas.model.CanvasRectangle

interface CanvasElementAbstraction<ElementType> {

	val ElementType.rectangle: CanvasRectangle

}
