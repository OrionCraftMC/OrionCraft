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
import io.github.orioncraftmc.orion.api.OrionCraft
import io.github.orioncraftmc.orion.api.bridge.OpenGlBridge
import io.github.orioncraftmc.orion.api.bridge.matrix
import io.github.orioncraftmc.orion.api.gui.components.Component
import io.github.orioncraftmc.orion.api.gui.components.impl.ButtonComponent
import io.github.orioncraftmc.orion.api.gui.components.screens.ComponentOrionScreen
import io.github.orioncraftmc.orion.api.gui.hud.BaseHudModuleRenderer
import io.github.orioncraftmc.orion.api.gui.hud.mod.HudOrionMod
import io.github.orioncraftmc.orion.api.gui.model.Anchor
import io.github.orioncraftmc.orion.api.gui.model.Point
import io.github.orioncraftmc.orion.api.gui.model.Size
import io.github.orioncraftmc.orion.api.utils.ColorConstants.modComponentBackground
import io.github.orioncraftmc.orion.api.utils.ColorConstants.modComponentBackgroundSelected
import io.github.orioncraftmc.orion.api.utils.ColorConstants.modComponentSelectionBorder
import io.github.orioncraftmc.orion.api.utils.ColorConstants.rectangleBorder
import io.github.orioncraftmc.orion.api.utils.gui.AnchorUtils
import io.github.orioncraftmc.orion.api.utils.gui.ComponentUtils
import io.github.orioncraftmc.orion.api.utils.rendering.RectRenderingUtils
import kotlin.math.floor

class ModsEditorScreen : ComponentOrionScreen() {

	inner class ModsEditorHudModuleRenderer : BaseHudModuleRenderer(true) {

		inline fun doActionIfMouseIsOverHudComponent(
			mouseX: Int,
			mouseY: Int,
			action: (HudOrionMod<*>, Enum<*>, Component) -> Unit
		) {
			modElementComponents.cellSet().forEach { cell ->
				val isMouseWithinComponent = ComponentUtils.isMouseWithinComponent(mouseX, mouseY, cell.value, true, 2)
				if (isMouseWithinComponent) {
					action(cell.rowKey, cell.columnKey, cell.value)
				}
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
			val position = Point()
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
					mousePosition.x.toInt(),
					mousePosition.y.toInt(),
					component,
					true,
					2
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
	private var componentDragMouseOffset: Point? = null

	// Button used to display the mods list
	private val modsButton = ButtonComponent("Mods").apply {
		size = Size(85.0, 27.0)
		anchor = Anchor.MIDDLE
		onClick = {
		}
	}

	init {
		// Add button to our screen
		addComponent(modsButton)
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

		modulesRenderer.renderHudElements()
	}

	private val anchorForPointArray =
		arrayOf(
			arrayOf(
				Anchor.TOP_LEFT, Anchor.TOP_MIDDLE, Anchor.TOP_RIGHT
			),
			arrayOf(
				Anchor.MIDDLE_LEFT, Anchor.MIDDLE, Anchor.MIDDLE_RIGHT
			),
			arrayOf(
				Anchor.BOTTOM_LEFT, Anchor.BOTTOM_MIDDLE, Anchor.BOTTOM_RIGHT
			)
		)

	fun getAnchorForPoint(x: Double, y: Double): Anchor {
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
					this.padding,
					parent!!.padding,
					this.scale
				)
			}.apply {
				val size = component.effectiveSize
				x = x.coerceIn(0.0, modulesRenderer.lastScaledResolution.scaledWidthFloat.toDouble() - size.width)
				y = y.coerceIn(0.0, modulesRenderer.lastScaledResolution.scaledHeightFloat.toDouble() - size.height)
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
			settings.anchor = newAnchor
			settings.position = point

			// Apply the new position settings on this component
			modulesRenderer.applyComponentSettings(component, settings)
		}

		return elementsBeingDraggedTable.size() > 0
	}

	override fun onClose() {
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
				boxFirstPoint.x, boxFirstPoint.y,
				mouseX.toDouble(), mouseY.toDouble(), modComponentSelectionBorder, true, 2.0
			)
			RectRenderingUtils.drawRectangle(
				boxFirstPoint.x, boxFirstPoint.y,
				mouseX.toDouble(), mouseY.toDouble(), modComponentBackground, false
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
}
