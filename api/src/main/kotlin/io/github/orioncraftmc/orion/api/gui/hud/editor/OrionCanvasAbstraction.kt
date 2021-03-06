package io.github.orioncraftmc.orion.api.gui.hud.editor

import com.github.ajalt.colormath.Color
import io.github.nickacpt.behaviours.canvas.abstractions.CanvasAbstraction
import io.github.nickacpt.behaviours.canvas.model.geometry.CanvasCorner
import io.github.nickacpt.behaviours.canvas.model.geometry.CanvasPoint
import io.github.nickacpt.behaviours.canvas.model.geometry.CanvasRectangle
import io.github.orioncraftmc.components.Component
import io.github.orioncraftmc.components.model.Anchor
import io.github.orioncraftmc.components.model.Padding
import io.github.orioncraftmc.components.model.Point
import io.github.orioncraftmc.components.utils.AnchorUtils
import io.github.orioncraftmc.orion.api.bridge.*
import io.github.orioncraftmc.orion.api.bridge.rendering.enums.DrawMode
import io.github.orioncraftmc.orion.api.gui.components.AnchorUpdateReceiver
import io.github.orioncraftmc.orion.utils.rendering.RectRenderingUtils

class OrionCanvasAbstraction(private val editorScreen: ModsEditorScreen) : CanvasAbstraction<Component, Color> {

	override val canvasRectangle: CanvasRectangle
		get() = MinecraftBridge.scaledResolution.let {
			CanvasRectangle(CanvasPoint(0f, 0f), CanvasPoint(it.scaledWidthFloat, it.scaledHeightFloat))
		}

	override val Component.resizeHandleCorner: CanvasCorner
		get() {
			val (isLeft, _, isRight) = AnchorUtils.extractXInformationFromAnchor(anchor)
			val (isTop, isMiddleVertical, isBottom) = AnchorUtils.extractYInformationFromAnchor(anchor)

			return when {
				isLeft && isTop -> CanvasCorner.BOTTOM_RIGHT
				isLeft && (isMiddleVertical || isBottom) -> CanvasCorner.TOP_RIGHT

				isRight && isTop -> CanvasCorner.BOTTOM_LEFT
				isRight && (isMiddleVertical || isBottom) -> CanvasCorner.TOP_LEFT

				else -> CanvasCorner.BOTTOM_RIGHT
			}
		}

	override val Component.rectangle: CanvasRectangle
		get() {
			val size = effectiveSize
			val location = effectivePosition
			return CanvasRectangle(
				CanvasPoint(location.x.toFloat(), location.y.toFloat()),
				CanvasPoint((location.x + size.width).toFloat(), (location.y + size.height).toFloat())
			)
		}

	override fun Component.moveTo(point: CanvasPoint) {
		val cell = editorScreen.modulesRenderer.modElementComponents.cellSet().first { it.value.id == this.id }

		// Get all relevant settings for the current component we are moving
		val settings = editorScreen.modulesRenderer.getHudElementSettings(cell.rowKey, cell.columnKey)
		val component = cell.value

		settings.position.x = point.x.toDouble()
		settings.position.y = point.y.toDouble()

		// Compute the new position with coordinates based on the top left anchor
		val resultPosition = component.run {
			AnchorUtils.computePosition(
				settings.position,
				this.size,
				Anchor.TOP_LEFT,
				parent!!.size,
				Padding(0.0),
				parent!!.padding,
				this.scale
			)
		}

		// Properly update the position of this element
		// Calculate the new anchor for this element based on the final position
		val newAnchor = editorScreen.getAnchorForPoint(resultPosition.x, resultPosition.y)

		// Some maths to calculate the position based on the new anchor
		val anchorScreenCornerPoint = AnchorUtils.computeAnchorOffset(component.parent!!.size, newAnchor)
		val componentCornerPoint =
			resultPosition + AnchorUtils.computeAnchorOffset(component.size, newAnchor) * component.scale

		val newLocalPoint = anchorScreenCornerPoint - componentCornerPoint
		AnchorUtils.convertGlobalAndLocalPositioning(newLocalPoint, newAnchor, false)

		// Properly change the settings with the new data
		val shouldNotifyAnchorUpdate = settings.anchor != newAnchor
		settings.anchor = newAnchor
		settings.position = newLocalPoint

		if (shouldNotifyAnchorUpdate) {
			(component as? AnchorUpdateReceiver)?.onAnchorUpdate(newAnchor)
		}

		// Apply the new position settings on this component
		editorScreen.modulesRenderer.applyComponentSettings(component, settings)
	}

	override val elements: Collection<Component>
		get() = editorScreen.modulesRenderer.modElementComponents.values()

	override var Component.elementScale: Float
		get() = this.scale.toFloat()
		set(value) {
			this.scale = value.toDouble().coerceIn(0.5, 2.5)

			val cell = editorScreen.modulesRenderer.modElementComponents.cellSet().first { it.value.id == this.id }
			val settings = editorScreen.modulesRenderer.getHudElementSettings(cell.rowKey, cell.columnKey)
			settings.scale = this.scale
		}

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

operator fun Point.times(factor: Double): Point = Point(x * factor, y * factor)
