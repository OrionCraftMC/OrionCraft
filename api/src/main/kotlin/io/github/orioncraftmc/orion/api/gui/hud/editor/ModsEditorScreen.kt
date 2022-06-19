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

import com.github.ajalt.colormath.Color
import com.github.ajalt.colormath.model.RGBInt
import io.github.nickacpt.behaviours.canvas.Canvas
import io.github.nickacpt.behaviours.canvas.config.*
import io.github.nickacpt.behaviours.canvas.model.CanvasPoint
import io.github.orioncraftmc.components.Component
import io.github.orioncraftmc.components.flex
import io.github.orioncraftmc.components.model.Anchor
import io.github.orioncraftmc.components.model.Padding
import io.github.orioncraftmc.components.model.Point
import io.github.orioncraftmc.components.model.Size
import io.github.orioncraftmc.components.nodeSize
import io.github.orioncraftmc.components.utils.ComponentUtils
import io.github.orioncraftmc.meditate.enums.YogaAlign
import io.github.orioncraftmc.meditate.enums.YogaJustify
import io.github.orioncraftmc.orion.api.OrionCraft
import io.github.orioncraftmc.orion.api.bridge.*
import io.github.orioncraftmc.orion.api.bridge.rendering.enums.GlCapability
import io.github.orioncraftmc.orion.api.gui.components.impl.ButtonComponent
import io.github.orioncraftmc.orion.api.gui.components.screens.ComponentOrionScreen
import io.github.orioncraftmc.orion.api.gui.hud.BaseHudModuleRenderer
import io.github.orioncraftmc.orion.api.gui.hud.mod.HudOrionMod
import io.github.orioncraftmc.orion.api.logger
import io.github.orioncraftmc.orion.screens.modmenu.ModMenuScreen
import io.github.orioncraftmc.orion.utils.BrandingUtils
import io.github.orioncraftmc.orion.utils.ColorConstants.modComponentBackground
import io.github.orioncraftmc.orion.utils.ColorConstants.modComponentBackgroundHover
import io.github.orioncraftmc.orion.utils.ColorConstants.modComponentBackgroundSelected
import io.github.orioncraftmc.orion.utils.ColorConstants.modComponentBorderColor
import kotlin.math.floor

class ModsEditorScreen(val isFromMainMenu: Boolean = false) : ComponentOrionScreen(true) {

	init {
		flex {
			justifyContent = YogaJustify.CENTER
		}
	}

	inner class ModsEditorHudModuleRenderer : BaseHudModuleRenderer(true) {

		override fun renderComponent(mod: HudOrionMod<*>, hudElement: Enum<*>, component: Component) {
			OpenGlBridge.enableCapability(GlCapability.BLEND)
			matrix {
				ComponentUtils.renderComponent(component, 0, 0)
			}
		}
	}

	internal val modulesRenderer = ModsEditorHudModuleRenderer()
	private val editorConfig = CanvasConfig<Color>().apply {
		colours.elementBackground = CanvasColourStyle(modComponentBackground, modComponentBackgroundHover, modComponentBackgroundSelected, RGBInt.fromRGBA(0xff00000fu))
		colours.elementBorder = CanvasColourStyle(modComponentBorderColor, modComponentBorderColor, modComponentBorderColor)

		colours.selectionBackground = modComponentBackgroundSelected
		colours.selectionBorder = modComponentBorderColor

		borderWidth = borderRectangleLineWidth.toFloat()
		safeZoneSize = uiSafeZone.toFloat()
	}

	private val editorCanvas = Canvas(OrionCanvasAbstraction(this), editorConfig)

	private val mousePosition = Point(0.0, 0.0)
	private val canvasMousePosition = CanvasPoint(0.0f, 0.0f)

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
		if (isFromMainMenu) {
			MinecraftBridge.drawDefaultBackground()
		}

		val xRange = 0.0..modulesRenderer.lastGameWidth.toDouble()
		val yRange = 0.0..modulesRenderer.lastGameHeight.toDouble()

		mousePosition.apply {
			x = mouseX.toDouble().coerceIn(xRange)
			y = mouseY.toDouble().coerceIn(yRange)
		}

		canvasMousePosition.apply {
			x = mousePosition.x.toFloat()
			y = mousePosition.y.toFloat()
		}

		super.drawScreen(mouseX, mouseY, renderPartialTicks)

		// Render the UI elements themselves
		modulesRenderer.renderHudElements()

		// Invoke the canvas renderer to render the foreground of the components
		editorCanvas.onRender(canvasMousePosition)
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

	internal fun getAnchorForPoint(x: Double, y: Double): Anchor {
		val parentSize = modulesRenderer.parentComponent.size
		val dividedWidth = parentSize.width / 3
		val dividedHeight = parentSize.height / 3

		val xBucket = floor(x / dividedWidth).toInt()
		val yBucket = floor(y / dividedHeight).toInt()

		return anchorForPointArray[yBucket.coerceIn(0, 2)][xBucket.coerceIn(0, 2)]
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

	override fun handleMouseClick(mouseX: Int, mouseY: Int) {
		editorCanvas.onMouseDown(canvasMousePosition)
		super.handleMouseClick(mouseX, mouseY)
	}

	override fun handleMouseRelease(mouseX: Int, mouseY: Int) {
		editorCanvas.onMouseUp(canvasMousePosition)
		super.handleMouseRelease(mouseX, mouseY)
	}

	private fun debugMessage(msg: String) {
		if (debug) logger.debug(msg)
	}

	override fun toString(): String {
		return "Mod Editor Screen"
	}

	companion object {

		private const val borderRectangleLineWidth = 1.0
		private const val uiSafeZone = 4.0

		private const val debug = true
	}
}
