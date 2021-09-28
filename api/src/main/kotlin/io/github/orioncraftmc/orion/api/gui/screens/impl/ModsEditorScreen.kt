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

package io.github.orioncraftmc.orion.api.gui.screens.impl

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
				handleMouseMovement(mod, hudElement, component)
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

	var selectedComponent: HudElementSelection? = null

	var isMouseDown = false

	private fun handleMouseMovement(
		mod: HudOrionMod<*>,
		hudElement: Enum<*>,
		component: Component
	) {
		if (selectedComponent == null && isMouseDown && ComponentUtils.isMouseWithinComponent(
				mousePosition.x.toInt(),
				mousePosition.y.toInt(),
				component,
				true
			)
		) {
			selectedComponent = HudElementSelection(mod, hudElement, component)
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

	override fun drawScreen(mouseX: Int, mouseY: Int, renderPartialTicks: Float) {
		mousePosition.apply {
			x = mouseX.toDouble()
			y = mouseY.toDouble()
		}
		super.drawScreen(mouseX, mouseY, renderPartialTicks)
		drawSelectionBox(mouseX, mouseY)
		modulesRenderer.renderHudElements()
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

	override fun handleMouseRelease(mouseX: Int, mouseY: Int) {
		isMouseDown = false
		selectionBoxFirstPoint = null
		super.handleMouseRelease(mouseX, mouseY)
	}

	override fun handleMouseClick(mouseX: Int, mouseY: Int) {
		isMouseDown = true
		selectionBoxFirstPoint = Point(mouseX.toDouble(), mouseY.toDouble())
		super.handleMouseClick(mouseX, mouseY)
	}
}
