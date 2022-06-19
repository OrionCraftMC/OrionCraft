package io.github.orioncraftmc.orion.api.gui.hud.editor

import io.github.nickacpt.behaviours.canvas.abstractions.CanvasElementAbstraction
import io.github.nickacpt.behaviours.canvas.model.CanvasPoint
import io.github.nickacpt.behaviours.canvas.model.CanvasRectangle
import io.github.orioncraftmc.components.Component

interface OrionComponentAbstraction : CanvasElementAbstraction<Component> {

	override val Component.rectangle: CanvasRectangle
		get() = CanvasRectangle(
            CanvasPoint(effectiveLeft.toFloat(), effectiveTop.toFloat()),
            CanvasPoint(effectiveRight.toFloat(), effectiveBottom.toFloat())
        )

}
