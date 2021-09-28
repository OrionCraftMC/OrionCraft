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
import io.github.orioncraftmc.orion.api.logger
import io.github.orioncraftmc.orion.api.utils.ColorConstants.modComponentBackground
import io.github.orioncraftmc.orion.api.utils.ColorConstants.modComponentBackgroundSelected
import io.github.orioncraftmc.orion.api.utils.ColorConstants.modComponentSelectionBorder
import io.github.orioncraftmc.orion.api.utils.ColorConstants.rectangleBorder
import io.github.orioncraftmc.orion.api.utils.gui.ComponentUtils
import io.github.orioncraftmc.orion.api.utils.rendering.RectRenderingUtils

class ModsEditorScreen : ComponentOrionScreen() {

	inner class HudElementSelection(
		val mod: HudOrionMod<*>,
		val hudElement: Enum<*>,
		val component: Component
	) {
		val hudSettings
			get() = modulesRenderer.getHudElementSettings(mod, hudElement)
	}

	val borderRectangleLineWidth = 2.0

	var selectionBoxFirstPoint: Point? = null

	inner class ModsEditorHudModuleRenderer : BaseHudModuleRenderer() {

		override fun renderComponent(mod: HudOrionMod<*>, hudElement: Enum<*>, component: Component) {
			matrix {
				drawComponentRectangle(component)
				OpenGlBridge.enableBlend()
				ComponentUtils.renderComponent(component, 0, 0)
			}
		}

		private fun drawComponentRectangle(component: Component) {
			val size = component.effectiveSize
			val position = component.effectivePosition
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
					true
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

	private val modsButton = ButtonComponent("Mods").apply {
		size = Size(85.0, 27.0)
		anchor = Anchor.MIDDLE
		onClick = {
		}
	}

	init {
		addComponent(modsButton)
	}

	private val modulesRenderer = ModsEditorHudModuleRenderer()
	val mousePosition = Point(0.0, 0.0)

	val mapIsDragging: Table<HudOrionMod<*>, Enum<*>, Component> = HashBasedTable.create()

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

	private fun handleComponentMouseMove(mouseX: Int, mouseY: Int): Boolean {
		val mouseOffset = componentDragMouseOffset ?: return false

		logger.debug("Starting to move component(s)")
		var isDragging = false
		mapIsDragging.cellSet().forEach { cell ->
			val settings = modulesRenderer.getHudElementSettings(cell.rowKey, cell.columnKey)
			val currentMousePos = Point(mouseX.toDouble(), mouseY.toDouble())
			val offset = currentMousePos - mouseOffset

			componentDragMouseOffset = currentMousePos

			settings.position += offset
			cell.rowKey.hudModSetting.notifyUpdate()
			modulesRenderer.applyComponentSettings(cell.value, settings)
			logger.debug("Performed component movement to (${settings.position})")
			isDragging = true
			OrionCraft.inGameHudRenderer.destroyAllComponents()
		}
		return isDragging
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

	var componentDragMouseOffset: Point? = null

	override fun handleMouseClick(mouseX: Int, mouseY: Int) {
		handleComponentMouseDown(mouseX, mouseY)
		selectionBoxFirstPoint = Point(mouseX.toDouble(), mouseY.toDouble())
		super.handleMouseClick(mouseX, mouseY)
	}

	private fun handleComponentMouseDown(mouseX: Int, mouseY: Int) {
		modulesRenderer.doActionIfMouseIsOverHudComponent(mouseX, mouseY) { mod, hudElement, component ->
			componentDragMouseOffset = Point(mouseX.toDouble(), mouseY.toDouble())
			mapIsDragging.put(mod, hudElement, component)
			logger.debug("Started dragging component (mouse down)")
		}
	}

	override fun handleMouseRelease(mouseX: Int, mouseY: Int) {
		handleComponentMouseRelease(mouseX, mouseY)
		selectionBoxFirstPoint = null
		super.handleMouseRelease(mouseX, mouseY)
	}
	private fun handleComponentMouseRelease(mouseX: Int, mouseY: Int) {
		componentDragMouseOffset = null
		modulesRenderer.doActionIfMouseIsOverHudComponent(mouseX, mouseY) { mod, hudElement, _ ->
			mapIsDragging.remove(mod, hudElement)
			logger.debug("Stopped dragging component (mouse up)")
		}
	}
}
