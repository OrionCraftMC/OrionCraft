/*
 * MIT License
 *
 * Copyright (c) 2021 OrionCraftMC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.orioncraftmc.orion.api.gui.hud.editor

import com.google.common.collect.HashBasedTable
import com.google.common.collect.Table
import io.github.orioncraftmc.meditate.enums.YogaAlign
import io.github.orioncraftmc.meditate.enums.YogaJustify
import io.github.orioncraftmc.orion.api.OrionCraft
import io.github.orioncraftmc.orion.api.bridge.*
import io.github.orioncraftmc.orion.api.bridge.rendering.DrawMode
import io.github.orioncraftmc.orion.api.gui.components.AnchorUpdateReceiver
import io.github.orioncraftmc.orion.api.gui.components.Component
import io.github.orioncraftmc.orion.api.gui.components.flex
import io.github.orioncraftmc.orion.api.gui.components.impl.ButtonComponent
import io.github.orioncraftmc.orion.api.gui.components.nodeSize
import io.github.orioncraftmc.orion.api.gui.components.screens.ComponentOrionScreen
import io.github.orioncraftmc.orion.api.gui.hud.BaseHudModuleRenderer
import io.github.orioncraftmc.orion.api.gui.hud.editor.snapping.ComponentSnapEngine
import io.github.orioncraftmc.orion.api.gui.hud.editor.snapping.SnapAxis
import io.github.orioncraftmc.orion.api.gui.hud.editor.snapping.SnappedComponentData
import io.github.orioncraftmc.orion.api.gui.hud.mod.HudOrionMod
import io.github.orioncraftmc.orion.api.gui.model.Anchor
import io.github.orioncraftmc.orion.api.gui.model.Padding
import io.github.orioncraftmc.orion.api.gui.model.Point
import io.github.orioncraftmc.orion.api.gui.model.Size
import io.github.orioncraftmc.orion.api.logger
import io.github.orioncraftmc.orion.screens.modmenu.ModMenuScreen
import io.github.orioncraftmc.orion.utils.BrandingUtils
import io.github.orioncraftmc.orion.utils.ColorConstants.modComponentBackground
import io.github.orioncraftmc.orion.utils.ColorConstants.modComponentBackgroundSelected
import io.github.orioncraftmc.orion.utils.ColorConstants.modComponentSelectionBorder
import io.github.orioncraftmc.orion.utils.ColorConstants.rectangleBorder
import io.github.orioncraftmc.orion.utils.gui.AnchorUtils
import io.github.orioncraftmc.orion.utils.gui.ComponentUtils
import io.github.orioncraftmc.orion.utils.rendering.RectRenderingUtils
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.abs
import kotlin.math.floor

class ModsEditorScreen : ComponentOrionScreen(true) {

	init {
		flex {
			justifyContent = YogaJustify.CENTER
		}
	}

	inner class ModsEditorHudModuleRenderer : BaseHudModuleRenderer(true) {

		inline fun doActionIfMouseIsOverHudComponent(
			mouseX: Int, mouseY: Int, action: (HudOrionMod<*>, Enum<*>, Component) -> Unit
		) {
			modElementComponents.cellSet()
				.filter { cell -> ComponentUtils.isMouseWithinComponent(mouseX, mouseY, cell.value, true, 2) }
				.take(1)
				.forEach { cell ->
					action(cell.rowKey, cell.columnKey, cell.value)
				}
		}

		override fun renderComponent(mod: HudOrionMod<*>, hudElement: Enum<*>, component: Component) {
			OpenGlBridge.enableBlend()
			matrix {
				ComponentUtils.renderComponent(component, 0, 0)
				drawComponentRectangle(component)
			}
		}

		private fun drawComponentRectangle(component: Component) {
			val size = component.effectiveSize
			val position = ComponentUtils.getComponentOriginPosition(component)
			val yPositionOffset = -0.5

			RectRenderingUtils.drawRectangle(
				position.x,
				position.y + yPositionOffset,
				position.x + size.width,
				position.y + size.height + yPositionOffset,
				rectangleBorder,
				true,
				borderRectangleLineWidth
			)


			var backgroundColor = modComponentBackground
			if (ComponentUtils.isMouseWithinComponent(
					mousePosition.x.toInt(), mousePosition.y.toInt(), component, true, 2
				)
			) {
				backgroundColor = modComponentBackgroundSelected
			}

			RectRenderingUtils.drawRectangle(
				position.x,
				position.y + yPositionOffset,
				position.x + size.width,
				position.y + size.height + yPositionOffset,
				backgroundColor,
				false
			)
		}
	}

	val borderRectangleLineWidth = 2.0

	// Point variable used to track the first location of the selection  box
	var selectionBoxFirstPoint: Point? = null

	private val modulesRenderer = ModsEditorHudModuleRenderer()
	private val mousePosition = Point(0.0, 0.0)
	private val elementsBeingDraggedTable: Table<HudOrionMod<*>, Enum<*>, Component> = HashBasedTable.create()
	private val snappedElementsData = SnappedComponentData()
	private var componentDragMouseOffset: Point? = null
	private var componentSnappingLines: Map<SnapAxis, MutableList<Double>> = emptyMap()

	// Button used to display the mods list
	private val modsButton = ButtonComponent("Mods").apply {
		onClick = {
			MinecraftBridge.openScreen(ModMenuScreen())
		}
		flex {
			nodeSize = Size(85.0, 27.0)
			alignSelf = YogaAlign.CENTER
		}
	}

	private val branding = BrandingUtils.getBrandingComponent(2.5).apply {
		anchor = Anchor.TOP_MIDDLE
		padding = Padding(0.0, 25.0, 0.0, 0.0)
	}

	init {
		// Add button to our screen
		addComponent(modsButton)
		addComponent(branding)
	}

	override fun drawScreen(mouseX: Int, mouseY: Int, renderPartialTicks: Float) {
		mousePosition.apply {
			x = mouseX.toDouble()
			y = mouseY.toDouble()
		}
		super.drawScreen(mouseX, mouseY, renderPartialTicks)
		if (!handleComponentMouseMove(mouseX, mouseY)) {
			// If they are not moving a component, draw a selection box
			drawSelectionBox(mouseX, mouseY)
		}

		// One or many components got moved, update the hud
		modulesRenderer.renderHudElements()
		updateSnappingLines()
		drawSnappingLines()
	}

	private fun updateSnappingLines() {
		componentSnappingLines =
			ComponentSnapEngine.computeSnappingPositions(modulesRenderer.modElementComponents.cellSet()
				.filterNot { elementsBeingDraggedTable.contains(it.rowKey, it.columnKey) }.map { it.value })

		val maxX = modulesRenderer.lastScaledResolution.scaledWidthFloat.toDouble()
		val maxY = modulesRenderer.lastScaledResolution.scaledHeightFloat.toDouble()
		val sizePoint = Point(maxX, maxY)

		SnapAxis.values().forEach { v ->
			componentSnappingLines[v]?.run {
				add(axisBorderDistance)
				add(v.getValueFromPoint(sizePoint) - axisBorderDistance)
			}
		}
	}

	private fun drawSnappingLines() {
		val maxX = modulesRenderer.lastScaledResolution.scaledWidthFloat.toDouble()
		val maxY = modulesRenderer.lastScaledResolution.scaledHeightFloat.toDouble()
		componentSnappingLines.forEach { (axis, values) ->
			values.forEach { value ->
				var x = 0.0
				var y = 0.0
				val finalX: Double
				val finalY: Double
				if (axis == SnapAxis.HORIZONTAL) {
					x = value
					finalX = x
					finalY = maxY
				} else {
					y = value
					finalX = maxX
					finalY = y
				}

				val tessellator = TessellatorBridge

				basicShapesRendering {
					OpenGlBridge.setLineWidth(1f)
					tessellator.start(DrawMode.LINE_LOOP)
					tessellator.addVertex(x, y, 0.0)
					tessellator.addVertex(finalX, finalY, 0.0)
					tessellator.setColor(255, 255, 255, 50)
					tessellator.draw()
				}

			}
		}
	}

	private val anchorForPointArray = arrayOf(
		arrayOf(
			Anchor.TOP_LEFT, Anchor.TOP_MIDDLE, Anchor.TOP_RIGHT
		), arrayOf(
			Anchor.MIDDLE_LEFT, Anchor.MIDDLE, Anchor.MIDDLE_RIGHT
		), arrayOf(
			Anchor.BOTTOM_LEFT, Anchor.BOTTOM_MIDDLE, Anchor.BOTTOM_RIGHT
		)
	)

	private fun getAnchorForPoint(x: Double, y: Double): Anchor {
		val parentSize = modulesRenderer.parentComponent.size
		val dividedWidth = parentSize.width / 3
		val dividedHeight = parentSize.height / 3

		val xBucket = floor(x / dividedWidth).toInt()
		val yBucket = floor(y / dividedHeight).toInt()

		return anchorForPointArray[yBucket.coerceIn(0, 2)][xBucket.coerceIn(0, 2)]
	}

	private fun handleComponentMouseMove(mouseX: Int, mouseY: Int): Boolean {
		// Check if we are actually moving any components
		val mouseOffset = componentDragMouseOffset ?: return false

		// We are probably dragging some components
		elementsBeingDraggedTable.cellSet().forEach { cell ->
			// Get all relevant settings for the current component we are moving
			val settings = modulesRenderer.getHudElementSettings(cell.rowKey, cell.columnKey)
			val currentMousePos = Point(mouseX.toDouble(), mouseY.toDouble())
			val component = cell.value

			// Compute the current mouse offset with the previous mouse position
			val offset = currentMousePos - mouseOffset
			AnchorUtils.convertGlobalAndLocalPositioning(offset, settings.anchor, true)

			// Update the mouse offset for the next frame
			componentDragMouseOffset = currentMousePos

			// Compute final position based on the original anchor
			settings.position = settings.position + offset

			// Compute the new position with coordinates based on the top left anchor
			val resultPosition = component.run {
				AnchorUtils.computePosition(
					settings.position,
					this.size,
					settings.anchor,
					parent!!.size,
					Padding(0.0),
					parent!!.padding,
					this.scale
				)
			}.apply {
				val size = component.size
				x = x.coerceIn(0.0, modulesRenderer.lastScaledResolution.scaledWidthFloat.toDouble() - size.width)
				y = y.coerceIn(0.0, modulesRenderer.lastScaledResolution.scaledHeightFloat.toDouble() - size.height)

				val shouldSnapNextAxis = AtomicBoolean(true)
				if (shouldSnapNextAxis.get()) x =
					handlePositionSnapAxis(SnapAxis.HORIZONTAL, x, x + size.width, mousePosition, shouldSnapNextAxis)
				if (shouldSnapNextAxis.get()) y =
					handlePositionSnapAxis(SnapAxis.VERTICAL, y, y + size.height, mousePosition, shouldSnapNextAxis)
			}

			// Properly update the position of this element
			// Calculate the new anchor for this element based on the final position
			val newAnchor = getAnchorForPoint(resultPosition.x, resultPosition.y)

			// Some maths to calculate the position based on the new anchor
			val anchorScreenCornerPoint = AnchorUtils.computeAnchorOffset(component.parent!!.size, newAnchor)
			val componentCornerPoint = resultPosition + AnchorUtils.computeAnchorOffset(component.size, newAnchor)

			val point = anchorScreenCornerPoint - componentCornerPoint
			AnchorUtils.convertGlobalAndLocalPositioning(point, newAnchor, false)

			// Properly change the settings with the new data
			val shouldNotifyAnchorUpdate = settings.anchor != newAnchor
			settings.anchor = newAnchor
			settings.position = point

			if (shouldNotifyAnchorUpdate) {
				(component as? AnchorUpdateReceiver)?.onAnchorUpdate(newAnchor)
			}

			// Apply the new position settings on this component
			modulesRenderer.applyComponentSettings(component, settings)
		}

		return elementsBeingDraggedTable.size() > 0
	}

	private fun handlePositionSnapAxis(
		axis: SnapAxis,
		firstValue: Double,
		secondValue: Double,
		mousePos: Point,
		shouldSnapNextAxis: AtomicBoolean
	): Double {
		shouldSnapNextAxis.set(true)
		val firstSkipCurrentSnapping = AtomicBoolean(false)
		val secondSkipCurrentSnapping = AtomicBoolean(false)
		val axisSnapValues = componentSnappingLines[axis] ?: return firstValue
		axisSnapValues.forEach { snapValue ->
			val firstSnappingResult = snapValueToAxis(
				firstValue,
				axis,
				snapValue,
				mousePos,
				0.0,
				firstSkipCurrentSnapping
			)
			val secondSnappingResult = snapValueToAxis(
				secondValue,
				axis,
				snapValue,
				mousePos,
				secondValue - firstValue,
				secondSkipCurrentSnapping
			)
			(firstSnappingResult ?: secondSnappingResult)?.let { return it }
			if (firstSkipCurrentSnapping.get() || secondSkipCurrentSnapping.get()) return@forEach
		}

		shouldSnapNextAxis.set(true)

		// We are not snapped anywhere, clear the snapping data
		logger.debug("Clearing snapping data for current axis")
		snappedElementsData.setUnsnapped(axis)

		return firstValue
	}

	private fun snapValueToAxis(
		value: Double,
		axis: SnapAxis,
		snapValue: Double,
		mousePos: Point,
		secondValueOffset: Double,
		skipCurrentSnapping: AtomicBoolean
	): Double? {
		// Measure our current distance to the snapping guideline
		val finalSnapValue = (snapValue - secondValueOffset).coerceAtLeast(0.0)
		val finalValue = value - secondValueOffset
		val snapValueComparison = abs(finalValue - finalSnapValue)
		//Are we too far? If so, skip this guideline
		if (snapValueComparison > finalSnappingDistance) {
			skipCurrentSnapping.set(true)
			return null
		}

		// Check if we are previously snapped
		val isSnapped = snappedElementsData.isSnapped(axis)
		// Calculate the mouse distance to the snapping guideline of our current snap
		val snapMouseDistance = snappedElementsData.getSnapMouseDistance(axis) ?: 0.0
		logger.debug("isSnapped[${axis}]= $isSnapped")

		val snapMousePos = axis.getValueFromPoint(mousePos)
		// Calculate the distance of our mouse to the point where we snapped
		val mouseDistance = abs(snapMousePos - finalSnapValue - snapMouseDistance)

		// Check what we should do
		// In order to snap, we need to be within the "Snapping Distance"
		// In order to unsnap, we need to be snapped and our mouse needs to be
		// further than the "Exiting Snapping Distance"
		val shouldSnap = snapValueComparison <= snappingDistance
		val shouldUnsnap = isSnapped && mouseDistance >= exitSnappingDistance

		logger.debug("$finalSnapValue: Unsnapping: isSnapped=$isSnapped mouseDistance=$mouseDistance snapMouseDistance=$snapMouseDistance shouldUnsnap=$shouldUnsnap")
		if (shouldSnap && !shouldUnsnap) {
			// If we are not previously snapped, store some helpful values
			// that will help us check when to unsnap
			logger.debug("$finalSnapValue: Should've snapped on first value to [$finalValue -> $finalSnapValue]; [$mouseDistance] ")
			if (!isSnapped) snappedElementsData.setSnapped(axis, mouseDistance, snapMousePos)
			return finalSnapValue
		}

		if (shouldUnsnap) {
			// In case we should unsnap, we need to "teleport" the component to be offset with
			// the mouse movement, otherwise the component would snap again
			val originalMousePos = snappedElementsData.getSnapMousePosition(axis) ?: 0.0

			// Clear the current snapping data since we unsnapped
			snappedElementsData.setUnsnapped(axis)
			logger.debug("Clearing snapping data for $axis")
			return (finalValue + ((axis.getValueFromPoint(mousePos) - originalMousePos) / 2.0))
		}
		return null
	}

	override fun onClose() {
		super.onClose()

		// Notify Orion settings that we updated the hud mod settings
		modulesRenderer.modElementComponents.rowKeySet().forEach {
			it.hudModSetting.notifyUpdate(it)
		}
		// Destroy all in-game hud components to refresh everything
		OrionCraft.inGameHudRenderer.destroyAllComponents()
	}

	private fun drawSelectionBox(mouseX: Int, mouseY: Int) {
		val boxFirstPoint = selectionBoxFirstPoint
		if (boxFirstPoint != null) {
			RectRenderingUtils.drawRectangle(
				boxFirstPoint.x,
				boxFirstPoint.y,
				mouseX.toDouble(),
				mouseY.toDouble(),
				modComponentSelectionBorder,
				true,
				2.0
			)
			RectRenderingUtils.drawRectangle(
				boxFirstPoint.x, boxFirstPoint.y, mouseX.toDouble(), mouseY.toDouble(), modComponentBackground, false
			)
		}
	}

	override fun handleMouseClick(mouseX: Int, mouseY: Int) {
		handleComponentMouseDown(mouseX, mouseY)
		selectionBoxFirstPoint = Point(mouseX.toDouble(), mouseY.toDouble())
		super.handleMouseClick(mouseX, mouseY)
	}

	override fun handleMouseRelease(mouseX: Int, mouseY: Int) {
		handleComponentMouseRelease()
		selectionBoxFirstPoint = null
		snappedElementsData.clear()
		super.handleMouseRelease(mouseX, mouseY)
	}

	private fun handleComponentMouseDown(mouseX: Int, mouseY: Int) {
		componentDragMouseOffset = Point(mouseX.toDouble(), mouseY.toDouble())
		modulesRenderer.doActionIfMouseIsOverHudComponent(mouseX, mouseY) { mod, hudElement, component ->
			// Setup element as being currently dragged
			elementsBeingDraggedTable.put(mod, hudElement, component)
		}
	}

	private fun handleComponentMouseRelease() {
		componentDragMouseOffset = null
		elementsBeingDraggedTable.clear()
	}

	companion object {
		private const val snappingDistance = 4.0
		private const val exitSnappingDistance = 8.0
		private const val finalSnappingDistance = 15.0

		private const val axisBorderDistance = 15.0
	}
}
