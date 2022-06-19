package io.github.orioncraftmc.orion.api.gui.hud.editor

import com.github.ajalt.colormath.Color
import io.github.nickacpt.behaviours.canvas.abstractions.CanvasAbstraction
import io.github.nickacpt.behaviours.canvas.model.CanvasPoint
import io.github.nickacpt.behaviours.canvas.model.CanvasRectangle
import io.github.orioncraftmc.components.Component
import io.github.orioncraftmc.components.model.Anchor
import io.github.orioncraftmc.components.model.Padding
import io.github.orioncraftmc.components.utils.AnchorUtils
import io.github.orioncraftmc.orion.api.bridge.*
import io.github.orioncraftmc.orion.api.bridge.rendering.enums.DrawMode
import io.github.orioncraftmc.orion.api.gui.components.AnchorUpdateReceiver
import io.github.orioncraftmc.orion.utils.rendering.RectRenderingUtils
import java.util.*

class OrionCanvasAbstraction(private val editorScreen: ModsEditorScreen) : CanvasAbstraction<Component, Color> {

	override val rectangle: CanvasRectangle
		get() = MinecraftBridge.scaledResolution.let {
			CanvasRectangle(CanvasPoint(0f, 0f), CanvasPoint(it.scaledWidthFloat, it.scaledHeightFloat))
		}

	override val Component.elementId: UUID
		get() = this.id

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
		val cell = editorScreen.modulesRenderer.modElementComponents.cellSet().first { it.value.elementId == this.id }

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
		val componentCornerPoint = resultPosition + AnchorUtils.computeAnchorOffset(component.size, newAnchor)

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

	override fun getElementById(id: UUID): Component {
		return elements.first { it.id == id }
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
