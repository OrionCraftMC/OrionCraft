package io.github.orioncraftmc.orion.api.gui.hud.editor

import com.github.ajalt.colormath.Color
import io.github.nickacpt.behaviours.canvas.Canvas
import io.github.nickacpt.behaviours.canvas.abstractions.CanvasAbstraction
import io.github.nickacpt.behaviours.canvas.model.CanvasPoint
import io.github.nickacpt.behaviours.canvas.model.CanvasRectangle
import io.github.orioncraftmc.components.Component
import io.github.orioncraftmc.orion.api.bridge.OpenGlBridge
import io.github.orioncraftmc.orion.api.bridge.TessellatorBridge
import io.github.orioncraftmc.orion.api.bridge.basicShapesRendering
import io.github.orioncraftmc.orion.api.bridge.rendering.enums.DrawMode
import io.github.orioncraftmc.orion.api.bridge.setTesselatorColor
import io.github.orioncraftmc.orion.utils.rendering.RectRenderingUtils

class OrionCanvasAbstraction(override val elements: Collection<Component>) : CanvasAbstraction<Component, Color>, OrionComponentAbstraction {

	override fun drawRectangle(rectangle: CanvasRectangle, color: Color, hollow: Boolean, lineWidth: Float) {
		RectRenderingUtils.drawRectangle(
			rectangle.topLeft.x.toDouble(),
			rectangle.topLeft.y.toDouble(),
			rectangle.bottomRight.x.toDouble(),
			rectangle.bottomRight.y.toDouble(),
			color,
			hollow,
			lineWidth.toDouble()
		)
	}

	override fun drawLine(p1: CanvasPoint, p2: CanvasPoint, color: Color, lineWidth: Float) {
		basicShapesRendering {
			val tessellator = TessellatorBridge

			tessellator.start(DrawMode.LINE_LOOP)

			OpenGlBridge.setLineWidth(lineWidth)
			tessellator.setTesselatorColor(color)

			tessellator.addVertex(p1.x.toDouble(), p1.y.toDouble(), 0.0)
			tessellator.addVertex(p2.x.toDouble(), p2.y.toDouble(), 0.0)

			tessellator.draw()
		}
	}
}
