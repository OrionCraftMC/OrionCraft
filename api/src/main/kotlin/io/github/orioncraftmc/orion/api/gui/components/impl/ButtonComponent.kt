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

package io.github.orioncraftmc.orion.api.gui.components.impl

import com.github.ajalt.colormath.Color
import com.github.ajalt.colormath.model.RGBInt
import io.github.orioncraftmc.components.containers.ComponentContainer
import io.github.orioncraftmc.components.model.Anchor
import io.github.orioncraftmc.components.model.Size
import io.github.orioncraftmc.components.utils.ComponentUtils
import io.github.orioncraftmc.orion.api.bridge.FontRendererBridge
import io.github.orioncraftmc.orion.api.bridge.MinecraftBridge
import io.github.orioncraftmc.orion.utils.NinePatchConstants
import io.github.orioncraftmc.orion.utils.rendering.NinePatchRendererUtils

open class ButtonComponent(
	text: String,
	var color: Color = RGBInt(255, 255, 255),
	var onClick: () -> Unit = {}
) : ComponentContainer() {

	var mousePressed = false

	init {
		snapToDevicePixels = true
	}

	var isAutomaticSize = false

	override var size: Size = Size()
		get() {
			if (isAutomaticSize) {
				field.width = FontRendererBridge.getStringWidth(text).toDouble()
				field.height = FontRendererBridge.fontHeight.toDouble()
			}
			return field
		}

	var text = text
		set(value) {
			field = value
			createLabelComponent()
		}

	init {
		createLabelComponent()
	}

	private fun createLabelComponent() {
		componentsList.clear()
		addComponent(LabelComponent(text, color).apply {
			snapToDevicePixels = true
			anchor = Anchor.MIDDLE
		})
	}

	override fun renderComponent(mouseX: Int, mouseY: Int) {
		val (ourSize, _) = ComponentUtils.computeEffectiveProperties(this)
		NinePatchRendererUtils.renderNinePatch(
			getNinePatch(mouseX, mouseY),
			0.0,
			0.0,
			ourSize.width,
			ourSize.height,
			2.0
		)
		super.renderComponent(mouseX, mouseY)
	}

	override fun handleMouseClick(mouseX: Int, mouseY: Int) {
		mousePressed = true
	}

	override fun handleMouseRelease(mouseX: Int, mouseY: Int) {
		if (mousePressed) {
			MinecraftBridge.playButtonClickSound()
			onClick()
		}
		mousePressed = false
		super.handleMouseRelease(mouseX, mouseY)
	}

	private fun getNinePatch(mouseX: Int, mouseY: Int) =
		if (ComponentUtils.isMouseWithinComponent(mouseX, mouseY, this)) {
			if (mousePressed) NinePatchConstants.buttonPressed else NinePatchConstants.buttonHover
		} else {
			mousePressed = false
			NinePatchConstants.button
		}
}
